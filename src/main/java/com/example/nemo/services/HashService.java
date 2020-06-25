package com.example.nemo.services;

import com.example.nemo.entity.HashEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.nemo.repositories.HashRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class HashService {
    @Autowired
    private HashRepository hashRepository;
    /*
    @Autowired
    private UserRepository userRepository;
    */
    @Transactional
    public void addUrl(HashEntity hash){
        hashRepository.save(hash);
    }
    public List<HashEntity> showAllHash(){
        return hashRepository.findAll();
    }
    /*
    public List<Hash> getUserHashes(User user){
        if(userRepository.existsById(user.getId())){
            List<Hash> ret = hashRepository.findByUser(user);
            return ret;
        }
        return null;
    }
    */
    public HashEntity findUrlByHash(String id){
        Optional<HashEntity> ret = hashRepository.findById(id);
        if(ret.isPresent())
            return ret.get();
        else
            return null;
    }
    /*
    public void makeId()
    {StringBuilder Sb=new StringBuilder(30);
        Sb.append("https://nemoswimmer.it/"); //https si ci voglio credere :P
        Sb.append(this.url.hashCode());
        setId(Sb.toString());
    }
    public int getSize(String url)  //tinyurl lo fa, noi non possiamo essere da meno
    {return url.length();
    }
    */
}
