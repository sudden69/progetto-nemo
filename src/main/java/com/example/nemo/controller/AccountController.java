package com.example.nemo.controller;

import com.example.nemo.entity.UserEntity;
import com.example.nemo.repositories.UserRepository;
import com.example.nemo.services.HashService;
import com.example.nemo.services.UserService;
import com.example.nemo.supports.ResponseMessage;
import com.example.nemo.supports.exceptions.MailUserAlreadyExistException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class AccountController {
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

    /*@PostMapping("/{id}")
    public ResponseEntity addShortened (@PathVariable ("id") Integer id,@RequestParam String url)
    { UserEntity user=new UserEntity();
        Optional<UserEntity> Optuser= userService.getById(id);
        if( Optuser.isPresent())
            user=Optuser.get();
        else return new ResponseEntity<>(new ResponseMessage("Id non valido"),HttpStatus.OK);
        HashService hashService=new HashService();
        user.addUrl(hashService.makeId(url));
        //non ho capito che fai con l'hashmap quando fai il signup
        //per salvare le modifiche mi serve un servizio apposta
        //ma preferisco non toccare visto che è delicato
        return new ResponseEntity<>(new ResponseMessage("url shortened and added"),HttpStatus.OK);

    }
*/

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