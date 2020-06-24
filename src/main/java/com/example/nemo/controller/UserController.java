package com.example.nemo.controller;

import com.example.nemo.entity.Hash;
import com.example.nemo.entity.User;
import com.example.nemo.services.HashService;
import com.example.nemo.services.UserService;
import com.example.nemo.supports.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private  UserService userService;

    @PostMapping
    public ResponseEntity create (@RequestBody @Valid User user){
        userService.addUser(user);
        return new ResponseEntity<>(new ResponseMessage("Added user"), HttpStatus.OK);
    }
    @GetMapping ("/{id}")
    public ResponseEntity getById(@PathVariable("id") String id){
        User user = userService.findNameByUser(id);
        if(user == null)
            return new ResponseEntity<>(new ResponseMessage("Id not found"), HttpStatus.OK);
        return new ResponseEntity<>(user,HttpStatus.OK);
    }

}
