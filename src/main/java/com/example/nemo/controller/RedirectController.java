package com.example.nemo.controller;

import com.example.nemo.entity.HashEntity;
import com.example.nemo.services.HashService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.nemo.supports.ResponseMessage;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

<<<<<<< HEAD
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("")
@CrossOrigin("*")

public class RedirectController {
    
    @Autowired
    HashService hashService;

    @GetMapping("/{hash}")
    public RedirectView localRedirect(@PathVariable String hash) {
        RedirectView redirectView = new RedirectView();
        HashEntity hashEntity = hashService.findUrlBySh(hash);
        if(hashEntity!=null) {

            redirectView.setUrl(hashEntity.getUrl());
            return redirectView;
        }
        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Url not found");
=======
@RestController
@RequestMapping("")
@CrossOrigin("*")
public class RedirectController {
    @Autowired
    HashService hashservice;
    @GetMapping("/")
    public ResponseEntity home() {
        return new ResponseEntity<>(new ResponseMessage("Benvenuto!!!!!!1111!!!!"), HttpStatus.OK);
    }
    @GetMapping("/{hash}")
    public RedirectView localRedirect(@PathVariable String hash) {
        RedirectView redirectView = new RedirectView();
        HashEntity hashEntity = hashservice.findHashbyShUrl(hash);
        if(hashEntity!=null) {
            redirectView.setUrl(hashEntity.getUrl());
            long v=hashservice.getVisite(hashEntity);
            hashservice.setVisite(v+1,hashEntity);
            return redirectView;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND,"url not found");
>>>>>>> origin/master
    }

}
