package com.example.nemo.services;

import com.example.nemo.entity.HashEntity;
import com.example.nemo.entity.UserEntity;
import com.example.nemo.repositories.UserRepository;
import com.example.nemo.supports.exceptions.MailUserAlreadyExistException;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
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
        System.out.println(principal.getName());
        UserEntity user = new UserEntity();
        user.setUserId(principal.getName());
        userRepository.save(user);
    }
    public void addHash(UserEntity user,HashEntity hash){
        Set<HashEntity> a = user.getHashes();
        a.add(hash);
        user.setHashes(a);
    }

    @Transactional (readOnly= false)
    public UserEntity getById(String id)
    {
        UserEntity user = userRepository.findById(id);
        return user;
    }


}
