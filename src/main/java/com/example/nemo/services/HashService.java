package com.example.nemo.services;

import com.example.nemo.entity.HashEntity;
import com.example.nemo.entity.UserEntity;
import com.example.nemo.repositories.UserRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.example.nemo.repositories.HashRepository;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

import static java.lang.Math.abs;
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
    //mappa massimi hashing raggiunti
    private static HashMap<Integer,Integer> hashing=new HashMap<Integer, Integer>();
    //non mi ricordo come si chiama maxint poi tolgo la costante che è brutta
    //private static Set<Integer> pool;
    //lista massimo hash raggiungibile per porzione, indice 1 based
    private static Integer [] lista= new Integer[22];
    //contatore indici non riempiti. Sto pensando se tenerlo o meno
    //se vedi nel service comunque blocco tutto se trovo un indice pieno
    //servirebbe una gestione con i thread per usare questo contatore
    //penso alla possibilità di far accedere più user contemporaneamente
    //non saprei come testarla in locale
    private static int count=21;

    //inizializza la mappa
    //Con questa annotazione viene lanciato in automatico il metodo inizialize
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
    //aggiorna la mappa quando un indice è pieno
    public void removeFromLista(int ind)
    {
        if(lista[ind]!=-1)
        {
            lista[ind] = -1;
            count--;
        }
        hashing.remove(ind);
    }
    //ripristina un indice della mappa ad un valore che si è liberato per expiration
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
    //serve per non chiamare 2 volte un indice pieno
    public int getListaSize()
    {
        return count;
    }
    //sta cosa si può fare in O(1) sicuramente
    //non so la chiave ma so la posizione nella mappa a cui accedere
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
    //prende l'ultimo hash usato per porzione, nel service viene incrementato
    public int getCurrentMap(int ind)
    {
        return hashing.get(ind);
    }
    //controlla il massimo hash libero
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
        { //controllo il mediano e vedo se ce ne sono di più grandi liberi
              if (shouldBeKilled(Timestamp.valueOf(LocalDateTime.now()),hash)) {

                      hashLess = hash; //da aggiustare col costruttore per copia;
                      int k=(Integer.parseInt(hash.getId()) + hashMore) / 2;
                      if(k!=hashMore-1)
                          hash=hashRepository.findById(String.valueOf(k)).get();
                      else
                      {
                          upper= Integer.parseInt(hashLess.getId());
                          addToLista(t, upper);
                          control = true;
                      }

              }

          //non ci sono elementi più grandi,controllo se è la prima iterazione o meno
            else if(shouldBeKilled(Timestamp.valueOf(LocalDateTime.now()),hashLess)&&hashLess.getId()!=String.valueOf((t-1)*MAX/21))
            {   upper= Integer.parseInt(hashLess.getId());
                addToLista(t, upper);
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
    //convertono l'id in base 64 o personalizzato

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

    public int getSize(@NotNull String url)  //tinyurl lo fa, noi non possiamo essere da meno
    {return url.length();
    }

    public void deleteHash(String id){
        hashRepository.deleteById(id);
    }

}
