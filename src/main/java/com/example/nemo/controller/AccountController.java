package com.example.nemo.controller;

import com.example.nemo.entity.HashEntity;
import com.example.nemo.entity.UserEntity;
import com.example.nemo.repositories.UserRepository;
import com.example.nemo.services.HashService;
import com.example.nemo.services.UserService;
import com.example.nemo.supports.ResponseMessage;
import com.example.nemo.supports.exceptions.MailUserAlreadyExistException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/users")
@CrossOrigin("http://localhost:4200")
public class AccountController {
    @Autowired
    private HashService hashService;
    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity create(@RequestBody HashMap<String,String> signupRequest) {
        try {
            UserEntity user = userService.registerUser(signupRequest);
            return  ResponseEntity.ok(user);
        } catch (MailUserAlreadyExistException e) {
            return new ResponseEntity<>(new ResponseMessage("E-mail address already exist!"), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/create")
    public ResponseEntity addShortened (@RequestBody @Valid HashMap<String,String> body)
    {
        UserEntity user = userService.getById(Integer.parseInt(body.get("id")));
        if(user==null){
            return new ResponseEntity(new ResponseMessage("User inesistente"),HttpStatus.OK);
        }
        HashEntity hash = hashService.makeIdByUser(body.get("url"),user);
        userService.addHash(user,hash);
        return new ResponseEntity<>(hash,HttpStatus.OK);

    }


    @GetMapping("/hashes/{id}")
    public Set<HashEntity> getAllHashes(@PathVariable Integer id){
        UserEntity user = userService.getById(id);
        if(user==null)
            return null;
        return hashService.getUserHashes(user);
    }

    @GetMapping
    public List<UserEntity> getAll() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity getById(@PathVariable Integer id)
    {UserEntity user=userService.getById(id);
        if(user == null)
            return new ResponseEntity<>(new ResponseMessage("Id not found"), HttpStatus.OK);
        return new ResponseEntity<>(user,HttpStatus.OK);
    }


}