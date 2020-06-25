package com.example.nemo.services;

import com.example.nemo.entity.UserEntity;
import com.example.nemo.repositories.UserRepository;
import com.example.nemo.supports.exceptions.MailUserAlreadyExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;


@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;


    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public UserEntity registerUser(HashMap<String, String> signupRequest) throws MailUserAlreadyExistException {
        if ( userRepository.existsByEmail(signupRequest.get("email")) ) {
            throw new MailUserAlreadyExistException();
        }
        UserEntity user = new UserEntity();
        user.setCode(signupRequest.get("code"));
        user.setFirstName(signupRequest.get("first_name"));
        user.setLastName(signupRequest.get("last_name"));
        user.setTelephoneNumber(signupRequest.get("telephone_number"));
        user.setEmail(signupRequest.get("email"));
        user.setAddress(signupRequest.get("address"));
        userRepository.save(user);
        return user;
    }

    @Transactional(readOnly = true)
    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }


}
