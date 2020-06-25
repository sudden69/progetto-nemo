package com.example.nemo.controller;

import com.example.nemo.entity.UserEntity;
import com.example.nemo.services.UserService;
import com.example.nemo.supports.ResponseMessage;
import com.example.nemo.supports.exceptions.MailUserAlreadyExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

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

    @GetMapping
    public List<UserEntity> getAll() {
        return userService.getAllUsers();
    }


}