package com.example.nemo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.nemo.supports.ResponseMessage;

@RestController
@RequestMapping("/home")
@CrossOrigin("http://localhost:4200")
public class HomeController {
    @GetMapping("/")
    public ResponseEntity home() {
        return new ResponseEntity<>(new ResponseMessage("Benvenuto!!!!!!1111!!!!"), HttpStatus.OK);
    }
}
