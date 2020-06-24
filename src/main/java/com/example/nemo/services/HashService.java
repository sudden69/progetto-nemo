package com.example.nemo.services;

import com.example.nemo.entity.Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.nemo.repositories.HashRepository;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class HashService {
    @Autowired
    private HashRepository hashRepository;

    @Transactional
    public void addUrl(Hash hash){
        hashRepository.save(hash);
    }

    public Hash findUrlByHash(String id){
        Optional<Hash> ret = hashRepository.findById(id);
        if(ret.isPresent())
            return ret.get();
        else
            return null;
    }

}
