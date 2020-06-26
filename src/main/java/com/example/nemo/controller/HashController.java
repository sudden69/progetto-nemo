package com.example.nemo.controller;

import com.example.nemo.entity.HashEntity;
import com.example.nemo.services.HashService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.nemo.supports.ResponseMessage;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/hashes")
public class HashController {

    @Autowired
    private HashService hashService;

    @PostMapping
    public ResponseEntity create (@RequestBody @Valid HashEntity hash){
        hashService.addUrl(hash);
        return new ResponseEntity<>(new ResponseMessage("Added hash"), HttpStatus.OK);
    }

//questo restituisce bad request

    @PostMapping("/make")
    public ResponseEntity makeIdByAttribute(@RequestParam @Valid String url )
    {HashEntity hash=hashService.makeId(url);
        hashService.addUrl(hash);
        return new ResponseEntity<>(new ResponseMessage("Id created"),HttpStatus.OK);
    }

    //questo invece funziona

    @PostMapping("/{url}")
    public ResponseEntity makeId(@PathVariable("url") String url )
    {HashEntity hash=hashService.makeId(url);
     hashService.addUrl(hash);
     return new ResponseEntity<>(new ResponseMessage("Id created"),HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity getByHash(@PathVariable("id") String id){
        HashEntity hash = hashService.findUrlByHash(id);
        if(hash == null)
            return new ResponseEntity<>(new ResponseMessage("Id not found"), HttpStatus.OK);
        return new ResponseEntity<>(hash,HttpStatus.OK);
    }
    @GetMapping
    public List<HashEntity> getAll(){
        return hashService.showAllHash();
    }
    /*
    @GetMapping("/user/{id}")
    public ResponseEntity getUserHashes(@PathVariable("id") int id){
        List<Hash> ret = hashService.getUserHashes(id);
        if(ret == null){
            return new ResponseEntity(new ResponseMessage("No hash found"), HttpStatus.OK);
        }
        return new ResponseEntity(ret,HttpStatus.OK);
    }
    */
}
