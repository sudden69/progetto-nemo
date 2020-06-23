package com.example.nemo.controller;

import com.example.nemo.entity.Hash;
import com.example.nemo.services.HashService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.nemo.supports.ResponseMessage;

import javax.validation.Valid;

@RestController
@RequestMapping("/hashes")
public class HashController {

    @Autowired
    private HashService hashService;

    @PostMapping
    public ResponseEntity create (@RequestBody @Valid Hash hash){
        hashService.addUrl(hash);
        return new ResponseEntity<>(new ResponseMessage("Added hash"), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity getByHash(@PathVariable("id") String id){
        Hash hash = hashService.findUrlByHash(id);
        if(hash == null)
            return new ResponseEntity<>(new ResponseMessage("Id not found"), HttpStatus.OK);
        return new ResponseEntity<>(hash,HttpStatus.OK);
    }
}
