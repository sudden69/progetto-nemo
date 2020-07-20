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


//NEMO
@RestController
@RequestMapping("")
@CrossOrigin("*")
public class RedirectController {
    @Autowired
    HashService hashservice;

    @GetMapping("/{hash}")
    public RedirectView redirect(@PathVariable String hash) {
        RedirectView redirectView = new RedirectView();
        HashEntity hashEntity = hashservice.findHashbyShUrl(hash);
        if (hashEntity != null) {
            redirectView.setUrl(hashEntity.getUrl());
            hashservice.incrementaVisite(hashEntity);
            return redirectView;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "url not found");

    }
}


