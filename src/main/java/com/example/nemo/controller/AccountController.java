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
import java.util.*;

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
    public List<HashEntity> getAllUserHashes(@RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,@RequestParam(value = "sortBy", defaultValue = "id") String sortBy,HttpServletRequest request){
        KeycloakAuthenticationToken principal = (KeycloakAuthenticationToken) request.getUserPrincipal();
        //String userId = principal.getAccount().getKeycloakSecurityContext().getIdToken().getSubject();
        UserEntity user = userService.getById(principal.getName());
        if(user==null)
            userService.addUser(principal);
        hashService.refresh(user.getUserId());
        List<HashEntity> result=hashService.getUserHashes(user,pageNumber,pageSize,sortBy);
        if ( result.size() <= 0 ) {
            return new ArrayList<>();
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
        return userService.getUserInfo(principal,accessToken);
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
    @RolesAllowed("admin")
    public List<UserEntity> getAllUser(@RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize){

        return userService.getAllUser(pageNumber,pageSize);
    }
/*
    //aggiorna lo stato degli hash
    @RolesAllowed("user")
    @PostMapping("/refresh")
    public void refresh(@RequestParam(value = "id") String id,@RequestParam(value= "pageNumber", defaultValue= "0") int pageNumber, @RequestParam(value = "pageSize", defaultValue ="10")int pageSize)
    {List <HashEntity> lista=hashService.getUserHashes(userService.getById(id),pageNumber,pageSize, "shUrl");
     Iterator <HashEntity> it=lista.iterator();
     while(it.hasNext())
     hashService.setAlive(it.next());
    }
 */
    @RolesAllowed ("user")
    @PostMapping("/customize")
    public ResponseEntity setCustomShUrl(@RequestBody @Valid HashMap<String,String> body,HttpServletRequest request)
    {
        KeycloakAuthenticationToken principal = (KeycloakAuthenticationToken) request.getUserPrincipal();
        //String userId = principal.getAccount().getKeycloakSecurityContext().getIdToken().getSubject();
        UserEntity user = userService.getById(principal.getName());
        if(user==null)
            userService.addUser(principal);
        HashEntity hash=hashService.makeIdByUser(body.get("url"),user);
        hashService.setCustomShUrl(hash,body.get("alias"));
        return new ResponseEntity<>(hash,HttpStatus.OK);
    }

}