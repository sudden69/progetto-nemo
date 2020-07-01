package com.example.nemo.services;

import com.example.nemo.entity.HashEntity;
import com.example.nemo.entity.UserEntity;
import com.example.nemo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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

    @Transactional
    public void addUrl(HashEntity hash){
        hashRepository.save(hash);
    }
    /*
    Questa è l'unica cosa che sono riuscito a fare, il compareTo anche se lo scrivo non lo usa, penso che lo voglia
    passato solo così, ovviamente fa ordine lessicografico
    */
    public List<HashEntity> showAllHash(){
        return hashRepository.findAll(Sort.by("id").descending());
    }

    public Set<HashEntity> getUserHashes(UserEntity user){
            return hashRepository.findByBuyer(user);
    }

    public HashEntity findUrlByHash(String id){
        Optional<HashEntity> ret = hashRepository.findById(id);
        if(ret.isPresent())
            return ret.get();
        else
            return null;
    }
    /*
    ho accomunato questo metodo per la creazione esclusiva dell'hash delegando poi ai due metodi makeIdNoUser e makeIdByUser il controllo
    dell'eventuale esistenza dell'hash
    */
    public HashEntity makeId(String url)
    {
        HashEntity hash=new HashEntity();
        hash.setUrl(url);
        hash.setId(String.valueOf(abs(url.hashCode())));
        return hash;
    }
    /*
    Qui a priori se l'hash esiste poiché non sarà associato ad alcun user posso incrementare di uno
     */
    @Transactional
    public HashEntity makeIdNoUser(String url){
        HashEntity hash = makeId(url);
        if(hashRepository.existsById(hash.getId()))
            hash.setId(String.valueOf(Integer.parseInt(hash.getId())+1));
        addUrl(hash);
        return hash;
    }
    /*
    Qui invece prima di incrementare controllo che oltre ad esistere già l'id esso non sia già in possesso dell'utente corrente, in tal caso
    non devo fare nulla
     */
    @Transactional
    public HashEntity makeIdByUser(String url,UserEntity user)
    {
        HashEntity hash = makeId(url);
        hash.setBuyer(user);
        //Qui faccio il controllo con un nuovo metodo creato nel repository
        if(hashRepository.existsByIdAndBuyerNot(hash.getId(),hash.getBuyer()))
            hash.setId(String.valueOf(Integer.parseInt(hash.getId())+1));
        addUrl(hash);
        return hash;
    }

    public int getSize(String url)  //tinyurl lo fa, noi non possiamo essere da meno
    {return url.length();
    }

}
