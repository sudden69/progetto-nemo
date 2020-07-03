package com.example.nemo.services;

import com.example.nemo.entity.HashEntity;
import com.example.nemo.entity.UserEntity;
import com.example.nemo.repositories.UserRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.example.nemo.repositories.HashRepository;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

import static java.lang.Math.abs;
import static java.lang.Math.random;

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
    */ /* bravo */
    public HashEntity makeId(String url)
    {
        HashEntity hash=new HashEntity();
        Random random=new Random();
        int k;
        int size=hash.getListaSize();
        int t;
        if(size>0)
            t = random.nextInt(size);
        else throw new RuntimeException();
        t=hash.getListaIndex(t);
        k = hash.getCurrentMap(t);
        k++;
        hash.setUrl(url);
        hash.setId(String.valueOf(k));
        if(hash.getCurrentLista(t)-k==0)
        {   hash.removeFromLista(t);
            check(t);
        }
        return hash;
    }
    public void check(int t)
    { HashEntity hash=hashRepository.findById(String.valueOf(((t+1)*2147483647/21-t*2147483647/21)/2)).get();
      HashEntity hashLess=hashRepository.findById(String.valueOf(t*2147483647/21)).get();
      HashEntity hashMore=hashRepository.findById(String.valueOf((t+1)*2147483647/21)).get();
      boolean control=false;
      while(!control)
      { //controllo il mediano e vedo se ce ne sono di più grandi liberi
          if (hash.shouldBeKilled(LocalDateTime.now()))
        { if (hashMore.shouldBeKilled(LocalDateTime.now()))
            hash.addToLista(t, Integer.parseInt(hashMore.getId()));
          else
              { hashLess = hash; //da aggiustare col costruttore per copia;
                hash=hashRepository.findById(String.valueOf((Integer.parseInt(hash.getId())+Integer.parseInt(hashMore.getId()))/2)).get();
              }
        }
          //non ci sono elementi più grandi,controllo se è la prima iterazione o meno
        else if(hashLess.shouldBeKilled(LocalDateTime.now())&&hashLess.getId()!=String.valueOf(t*2147483647/21))
        {
          hash.addToLista(t, Integer.parseInt(hashLess.getId()));
          control = true;
        }
       }
        //se nessuna condizione si verifica, aspetto ciclando attivamente
          // un wait prima del prossimo ciclo potrebbe essere una buona idea
        //la ricerca dicotomica a sinistra preferisco non farla
        //con meno di metà porzione libera è meglio aspettare
    }

    @Transactional
    public HashEntity makeIdNoUser(String url){

        HashEntity hash = makeId(url);
        /*if(hashRepository.existsById(hash.getId()))
            hash.setId(String.valueOf(Integer.parseInt(hash.getId())+1));
         */ // no longer necessary
        addUrl(hash);
        return hash;
    }

    @Transactional
    public HashEntity makeIdByUser(String url,UserEntity user)
    {
        HashEntity hash = makeId(url);
        hash.setBuyer(user);
        //Qui faccio il controllo con un nuovo metodo creato nel repository
       /* if(hashRepository.existsByIdAndBuyerNot(hash.getId(),hash.getBuyer()))
            hash.setId(String.valueOf(Integer.parseInt(hash.getId())+1));*/
        //no longer necessary
        addUrl(hash);
        return hash;
    }
    //convertono l'id in base 64 o personalizzato
    public void setShUrl(HashEntity hash)
    {hash.setShUrl();
    }
    public boolean setCustomShUrl(HashEntity hash,String custom)
    { if(hashRepository.existsByShUrl(custom))
        return false;
     hash.setCustomShUrl(custom);
     return true;
    }
    public void inizialize(HashEntity hash)
    {hash.inizialize();
    }

    public int getSize(@NotNull String url)  //tinyurl lo fa, noi non possiamo essere da meno
    {return url.length();
    }

}
