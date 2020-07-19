package com.example.nemo.services;

import com.example.nemo.entity.HashEntity;
import com.example.nemo.entity.UserEntity;
import com.example.nemo.repositories.UserRepository;
import com.example.nemo.supports.exceptions.MailUserAlreadyExistException;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;


@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public void addUser(KeycloakAuthenticationToken principal){
        UserEntity user = new UserEntity();
        user.setUserId(principal.getName());
        userRepository.save(user);
    }
    public void addHash(UserEntity user,HashEntity hash){
        Set<HashEntity> a = user.getHashes();
        a.add(hash);
        user.setHashes(a);
    }
    public HashMap<String,String> getUserInfo(KeycloakAuthenticationToken principal, AccessToken accessToken){
        HashMap<String,String> utente = new HashMap<String,String>();
        utente.put("email",accessToken.getEmail());
        utente.put("firstname",accessToken.getName());
        utente.put("username",principal.getName());
        return utente;
    }
    @Transactional (readOnly= false)
    public UserEntity getById(String id)
    {
        UserEntity user = userRepository.findById(id);
        return user;
    }
    public List<UserEntity> getAllUser(int pageNumber, int pageSize){
        Pageable paging = PageRequest.of(pageNumber, pageSize);
        Page<UserEntity> pagedResult = userRepository.findAll(paging);
        if ( pagedResult.hasContent() ) {
            return pagedResult.getContent();
        }
        return null;
    }


}
