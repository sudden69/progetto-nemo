package com.example.nemo.services;

import com.example.nemo.entity.User;
import com.example.nemo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Transactional
    public void addUser(User user)
    {userRepository.save(user);

    }

    public User findNameByUser(String id)
    {
        Optional<User> ret=userRepository.findById(id);
        if(ret.isPresent())
            return ret.get();
        else
            return null;

    }
}
