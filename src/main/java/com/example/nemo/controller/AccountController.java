package com.example.nemo.controller;

import com.example.nemo.entity.HashEntity;
import com.example.nemo.entity.UserEntity;
import com.example.nemo.repositories.HashRepository;
import com.example.nemo.services.HashService;
import com.example.nemo.services.UserService;
import com.example.nemo.supports.ResponseMessage;



import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;

import org.keycloak.representations.AccessToken;
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
@CrossOrigin(origins = "*")

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
        if(user==null)
            userService.addUser(principal);
        HashEntity hash = hashService.makeIdByUser(body.get("url"),user);
        userService.addHash(user,hash);
        return new ResponseEntity<>(hash,HttpStatus.OK);
    }

    @RolesAllowed("user")
    @GetMapping("/hashes")
    public Set<HashEntity> getAllHashes(@RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,HttpServletRequest request){
        KeycloakAuthenticationToken principal = (KeycloakAuthenticationToken) request.getUserPrincipal();
        //String userId = principal.getAccount().getKeycloakSecurityContext().getIdToken().getSubject();
        UserEntity user = userService.getById(principal.getName());
        if(user==null)
            userService.addUser(principal);
        Set<HashEntity> result=hashService.getUserHashes(user ,pageNumber,pageSize );
        if ( result.size() <= 0 ) {
            throw new RuntimeException();
        }
        return result;
    }
    //test
    @GetMapping("/hash")
    public Set<HashEntity> getHashes(@RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,HttpServletRequest request){
        //KeycloakAuthenticationToken principal = (KeycloakAuthenticationToken) request.getUserPrincipal();
        //String userId = principal.getAccount().getKeycloakSecurityContext().getIdToken().getSubject();
        //UserEntity user = userService.getById(principal.getName());
        //if(user==null)
         //   userService.addUser(principal);
        Set<HashEntity> result=hashService.showAllHash(pageNumber,pageSize );
        if ( result.size() <= 0 ) {
            throw new RuntimeException();
        }
        return result;
    }

    @RolesAllowed("user")
    @GetMapping()
    public HashMap<String,String> getById(HttpServletRequest request)
    {
        KeycloakAuthenticationToken principal = (KeycloakAuthenticationToken) request.getUserPrincipal();
        UserEntity user=userService.getById(principal.getName());
        if(user == null)
                userService.addUser(principal);
        AccessToken accessToken = principal.getAccount().getKeycloakSecurityContext().getToken();
        HashMap<String,String> utente = new HashMap<String,String>();
        utente.put("email",accessToken.getEmail());
        utente.put("firstname",accessToken.getName());
        utente.put("username",principal.getName());
        return utente;
    }

    @GetMapping("/size/{id}")
    public int getShSize(@PathVariable("id") String id)
    {   //il nome findUrlByHash non va bene, l'hash è nello shurl
        HashEntity hash= hashService.findUrlByHash(id);
        return hashService.getShSize(hash);
    }
    @GetMapping("/length/{id}")
    public int getSize(@PathVariable("id") String id)
    {HashEntity hash= hashService.findUrlByHash(id);
     return hashService.getSize(hash);
    }
}