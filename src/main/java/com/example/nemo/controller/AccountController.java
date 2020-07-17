package com.example.nemo.controller;

import com.example.nemo.entity.HashEntity;
import com.example.nemo.entity.UserEntity;
import com.example.nemo.repositories.HashRepository;
import com.example.nemo.services.HashService;
import com.example.nemo.services.UserService;
import com.example.nemo.supports.ResponseMessage;



import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;

import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/users")
public class AccountController {
    @Autowired
    private HashService hashService;
    @Autowired
    private UserService userService;

    // Questo tag serve a dire che a questo metodo ci puoi accedere solo se sei loggato come utente
    @RolesAllowed("user")
    @PostMapping("/create")
    public ResponseEntity addShortened (@RequestBody @Valid HashMap<String,String> body,HttpServletRequest request)
    {

        KeycloakAuthenticationToken principal = (KeycloakAuthenticationToken) request.getUserPrincipal();
        //String userId = principal.getAccount().getKeycloakSecurityContext().getIdToken().getSubject();
        UserEntity user = userService.getById(principal.getName());
        HashEntity hash = hashService.makeIdByUser(body.get("url"),user);
        userService.addHash(user,hash);
        return new ResponseEntity<>(hash,HttpStatus.OK);
    }

    @RolesAllowed("user")
    @GetMapping("/hashes")
    public Set<HashEntity> getAllHashes(HttpServletRequest request){
        KeycloakAuthenticationToken principal = (KeycloakAuthenticationToken) request.getUserPrincipal();
        //String userId = principal.getAccount().getKeycloakSecurityContext().getIdToken().getSubject();
        UserEntity user = userService.getById(principal.getName());
        if(user==null)
            userService.addUser(principal);
        return hashService.getUserHashes(user);
    }
    @CrossOrigin(origins = "*")
    @RolesAllowed("user")
    @GetMapping()
    public ResponseEntity getById(HttpServletRequest request)
    {
        KeycloakAuthenticationToken principal = (KeycloakAuthenticationToken) request.getUserPrincipal();
        UserEntity user=userService.getById(principal.getName());
        if(user == null)
            return new ResponseEntity<>(new ResponseMessage("Id not found"), HttpStatus.OK);
        return new ResponseEntity<>(user,HttpStatus.OK);
    }

    @GetMapping("/size/{id}")
    public int getShSize(@PathVariable("id") String id)
    {   //il nome findUrlByHash non va bene, l'hash è nello shurl
        HashEntity hash= hashService.findUrlByHash(id);
        return hashService.getShSize(hash);
    }
    //scelta del nome molto poco originale
    //forse conviene accoppiarli questi due metodi
    //e restituire i valori come coppia
    @GetMapping("/length/{id}")
    public int getSize(@PathVariable("id") String id)
    {HashEntity hash= hashService.findUrlByHash(id);
     return hashService.getSize(hash);
    }
}