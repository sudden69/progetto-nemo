package com.example.nemo.services;
import com.example.nemo.entity.HashEntity;
import com.example.nemo.entity.UserEntity;
import com.example.nemo.repositories.UserRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.example.nemo.repositories.HashRepository;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

import static java.lang.Math.random;

@Service
public class HashService {
    @Autowired
    private HashRepository hashRepository;

    @Autowired
    private UserRepository userRepository;

    private final int MAX = 63;
    //check expiration
    public boolean shouldBeKilled(Timestamp then, HashEntity hashEntity)
    { long creation= hashEntity.getCreation().getTime();
      long now=then.getTime();
      long duration=now-creation;
        if(duration>1800)
            return true;
        return false;
    }
    private static HashMap<Integer,Integer> hashing=new HashMap<Integer, Integer>();
    private static Integer [] lista= new Integer[22];
    private static int count=21;
    @PostConstruct
    public void inizialize()
    {
        System.out.println("Fatto");
        int k=MAX/21;
        for (int i = 1; i < 22; i++)
        {
            lista[i]=k*i;
            hashing.put(i, (i - 1) * k);
        }
    }
    public void removeFromLista(int ind)
    {
        if(lista[ind]!=-1)
        {
            lista[ind] = -1;
            count--;
        }
        hashing.remove(ind);
    }

    public void addToLista(int ind,int val)
    {
        if(lista[ind]==-1)
            count++;
        lista[ind]=val;
        int k=MAX/21;
        k=val-val%k;
        //la porzione di mappa riparte dal suo "0"
        hashing.put(ind,k);
    }

    public void setShUrlById(HashEntity hashEntity)
    {
        hashEntity.setShUrl(Base64.getUrlEncoder().encodeToString(hashEntity.getId().getBytes()));
    }

    public int getListaSize()
    {
        return count;
    }

    public int getListaIndex(int ind)
    {
        Iterator it= hashing.entrySet().iterator();
        while(ind>1)
        {
            it.next();
            ind--;
        }
        Map.Entry pair=(Map.Entry) it.next();
        return (Integer) pair.getKey();
    }

    public int getCurrentMap(int ind)
    {
        return hashing.get(ind);
    }
    public int getCurrentLista(int ind)
    {
        return lista[ind];
    }
    public void setCurrentMap(Integer ind,Integer val)
    {
        this.hashing.put(ind,val);
    }
    @Transactional
    public void addUrl(HashEntity hash){
        hashRepository.save(hash);
    }

    public Set <HashEntity> showAllHash(int pageNumber, int pageSize)
    {   Pageable paging = PageRequest.of(pageNumber, pageSize);
        Page<HashEntity> pagedResult = hashRepository.findAll(paging);
        if ( pagedResult.hasContent() ) {
            return (Set<HashEntity>) pagedResult.getContent();
        }
        return null;
    }
    public Set <HashEntity> getUserHashes(UserEntity user, int pageNumber, int pageSize)
    {
        Pageable paging = PageRequest.of(pageNumber, pageSize);
        Page<HashEntity> pagedResult = hashRepository.findByBuyer(paging, user);
        if ( pagedResult.hasContent() ) {
            return (Set<HashEntity>) pagedResult.getContent();
        }
        return null;
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
        HashEntity hash=new HashEntity();
        Random random=new Random();
        int k;
        int size=getListaSize();
        int t;
        if(size>0)
            t = random.nextInt(size+1);
        else throw new RuntimeException();
        t=getListaIndex(t);
        k = getCurrentMap(t);
        hash.setUrl(url);
        try
        {hashRepository.findById(String.valueOf(k)).get();
         hash.setCreation(Timestamp.valueOf(LocalDateTime.now()));
        }
        catch(Exception e)
        {
        }
        hash.setId(String.valueOf(k));
        try {
            if (shouldBeKilled(Timestamp.valueOf(LocalDateTime.now()), hashRepository.findById(String.valueOf(t*MAX/21)).get())) {
                addToLista(t, t*MAX/21);
            }
        }
        catch(Exception e){}
        if(getCurrentLista(t)-k==1)
        {   removeFromLista(t);
            check(t);
            k=getCurrentMap(t)-1;
        }
        k++;
        setShUrlById(hash);
        setCurrentMap(t,k);
        return hash;
    }

    public void check(int t)
    {
        HashEntity hash=hashRepository.findById(String.valueOf(((t)*MAX/21+(t-1)*MAX/21)/2)).get();
        HashEntity hashLess=hashRepository.findById(String.valueOf((t-1)*MAX/21)).get();
        int hashMore=t*MAX/21;
        boolean control=false;
        int upper=-1;
        while(!control)
        {
              if (shouldBeKilled(Timestamp.valueOf(LocalDateTime.now()),hash)) {

                      hashLess = hash;
                      int k=(Integer.parseInt(hash.getId()) + hashMore) / 2;
                      if(k!=hashMore-1)
                          hash=hashRepository.findById(String.valueOf(k)).get();
                      else
                      {
                          upper= Integer.parseInt(hashLess.getId());
                          addToLista(t, upper+1);
                          control = true;
                      }

              }
            else if(shouldBeKilled(Timestamp.valueOf(LocalDateTime.now()),hashLess)&&hashLess.getId()!=String.valueOf((t-1)*MAX/21))
            {   upper= Integer.parseInt(hashLess.getId());
                addToLista(t, upper+1);
                control = true;
            }

       }
    }
    @Transactional
    public HashEntity makeIdNoUser(String url){
        HashEntity hash = makeId(url);
        addUrl(hash);
        return hash;
    }
    @Transactional
    public HashEntity makeIdByUser(String url,UserEntity user)
    {
        HashEntity hash = hashRepository.findByUrlAndBuyer(url,user);
        if(hash != null) return hash;
        hash = makeId(url);
        hash.setBuyer(user);
        addUrl(hash);
        return hash;
    }
    public boolean setCustomShUrl(HashEntity hash,String custom)
    { if(hashRepository.existsByShUrl(custom))
        return false;
     hash.setShUrl(custom);
     return true;
    }
    public void inizialize(HashEntity hash)
    {
        inizialize();
    }

    public int getSize(HashEntity hash)  //tinyurl lo fa, noi non possiamo essere da meno
    {String temp=hash.getUrl();
     return temp.length();
    }

    public int getShSize(HashEntity hash)
    {String temp=hash.getShUrl();
     return temp.length();
    }
    public void deleteHash(String id){
        hashRepository.deleteById(id);
    }

    public HashEntity findHashbyShUrl(String shUrl)
    {return hashRepository.findHashByShUrl(shUrl);
    }
    @Transactional
    public void setVisite(long visite, HashEntity hash)
    {
        hash.setVisite(visite);
        hashRepository.save(hash);
    }
    @Transactional
    public long getVisite(HashEntity hash)
    {
        return hash.getVisite();
    }

}
