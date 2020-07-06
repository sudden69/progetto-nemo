package com.example.nemo.controller;

import com.example.nemo.entity.HashEntity;
import com.example.nemo.services.HashService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.nemo.supports.ResponseMessage;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/hashes")
@CrossOrigin(origins = "*")
public class HashController {

    @Autowired
    private HashService hashService;

    @PostMapping("/make")
    public HashEntity makeIdByAttribute(@RequestBody @Valid HashMap<String,String> url)
    {
        return hashService.makeIdNoUser(url.get("url"));
    }

    @GetMapping("/{id}")
    public ResponseEntity getByHash(@PathVariable("id") String id){
        HashEntity hash = hashService.findUrlByHash(id);
        if(hash == null)
            return new ResponseEntity<>(new ResponseMessage("Id not found"), HttpStatus.OK);
        return new ResponseEntity<>(hash,HttpStatus.OK);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteHash(@PathVariable("id") String id){
        hashService.deleteHash(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @CrossOrigin("http://localhost:4200")
    @GetMapping
    public List<HashEntity> getAll(){
        return hashService.showAllHash();
    }
}
