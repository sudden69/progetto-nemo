package com.example.nemo.services;

import com.example.nemo.entity.Hash;
import com.example.nemo.entity.User;
import com.example.nemo.repositories.HashRepository;
import com.example.nemo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private HashRepository hashRepository;
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
    // Qui sto usando un metodo di tipo Hash
    //
    public List<Hash> findAllMyUrls(String id)
    { List <Hash> ret= (List<Hash>) hashRepository.findByUser(id);
     return ret;
    }
    //per l'inserimento probabilmente è conveniente procedere come in HashService
    //e creare una nuova istanza di Hash. Con questo tipo di associazione si
    //potrebbe potenzialmente evitare che user diversi vedano lo stesso tinyurl

}
