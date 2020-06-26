package com.example.nemo.services;

import com.example.nemo.entity.HashEntity;
import com.example.nemo.entity.UserEntity;
import com.example.nemo.repositories.UserRepository;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.nemo.repositories.HashRepository;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.lang.Math.abs;

@Service
public class HashService {
    @Autowired
    private HashRepository hashRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager entityManager;

    @Transactional
    public void addUrl(HashEntity hash){
        hashRepository.save(hash);
    }

    public List<HashEntity> showAllHash(){
        return hashRepository.findAll();
    }

    public Set<HashEntity> getUserHashes(UserEntity user){
            Set<HashEntity> ret = hashRepository.findByBuyer(user);
            return ret;
    }
    public HashEntity findUrlByHash(String id){
        Optional<HashEntity> ret = hashRepository.findById(id);
        if(ret.isPresent())
            return ret.get();
        else
            return null;
    }

    public HashEntity makeId(String url)
    {
        StringBuilder Sb=new StringBuilder(30);
        //Sb.append("https://nemoswimmer.it/"); //https si ci voglio credere :P
        Sb.append(abs(url.hashCode()));
        HashEntity hash=new HashEntity();
        hash.setUrl(url);
        hash.setId(Sb.toString());
        return hash;
    }
    @Transactional
    public HashEntity makeIdByUser(String url,UserEntity user)
    {
        HashEntity hash = makeId(url);
        hash.setBuyer(user);
        addUrl(hash);
        return hash;
    }

    public int getSize(String url)  //tinyurl lo fa, noi non possiamo essere da meno
    {return url.length();
    }

}
