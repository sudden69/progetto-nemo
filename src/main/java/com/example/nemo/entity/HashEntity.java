package com.example.nemo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.CreationTimestamp;
import org.jetbrains.annotations.NotNull;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "hash",schema = "public")
public class HashEntity {

    @Id
    @Column(name = "id")
    private String id;
    @Basic
    @Column(name = "shUrl")
    private String shUrl;
    @Basic
    @Column(name = "url")
    private String url;

    @ManyToOne
    @JoinColumn(name = "buyer")
    @JsonIgnoreProperties("buyers")
    private UserEntity buyer;
//convertitore timestamp
    @Converter(autoApply = true)
    public class LocalDateTimeConverter implements AttributeConverter<LocalDateTime,Timestamp>
    {
        @Override
        public Timestamp convertToDatabaseColumn(LocalDateTime localDateTime) {
            return (localDateTime == null ? null : Timestamp.valueOf(localDateTime));

        }

        @Override
        public LocalDateTime convertToEntityAttribute(Timestamp sqlTimestamp) {
            return (sqlTimestamp == null ? null : sqlTimestamp.toLocalDateTime());

        }
    }

        //qua avevo screenato sai cos'è
    @Basic
    @CreationTimestamp
    @Column(name = "creationTime")
    LocalDateTime creation;

    //check expiration
    public boolean shouldBeKilled(LocalDateTime then)
    {Duration duration=Duration.between((java.time.temporal.Temporal) creation,then);
     if(duration.getSeconds()>1800)
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
    public void inizialize()
    {int k=2147483647/21;
        for (int i = 1; i < 22; i++)
        {
            lista[i]=k*i;
            hashing.put(i, (i - 1) * k);
        }
    }
    //aggiorna la mappa quando un indice è pieno
    public void removeFromLista(int ind)
    {if(lista[ind]!=-1)
     {
        lista[ind] = -1;
        count--;
     }
     hashing.remove(ind);
    }
    //ripristina un indice della mappa ad un valore che si è liberato per expiration
    public void addToLista(int ind,int val)
    {if(lista[ind]==-1)
        count++;
     lista[ind]=val;
     int k=2147483647/21;
     k=val-val%k;
     //la porzione di mappa riparte dal suo "0"
     hashing.put(ind,k);
    }
    //serve per non chiamare 2 volte un indice pieno
    public int getListaSize()
    {return count;
    }
    //sta cosa si può fare in O(1) sicuramente
    //non so la chiave ma so la posizione nella mappa a cui accedere
    public int getListaIndex(int ind)
    {Iterator it= hashing.entrySet().iterator();
     while(ind>1)
     { it.next();
       ind--;
     }
     Map.Entry pair=(Map.Entry) it.next();
     return (Integer) pair.getKey();
    }
    //prende l'ultimo hash usato per porzione, nel service viene incrementato
    public int getCurrentMap(int ind)
    {return hashing.get(ind);
    }
    //controlla il massimo hash libero
    public int getCurrentLista(int ind)
    {return lista[ind];
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UserEntity getBuyer()
    {
        return buyer;
    }

    public void setBuyer(UserEntity buyer)
    {
        this.buyer=buyer;
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    public void setShUrl()
    {shUrl=Base64.getUrlEncoder().encodeToString(id.getBytes());
    }
    public void setCustomShUrl(String custom)
    {shUrl=custom;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HashEntity hash = (HashEntity) o;

        if (id != null ? !id.equals(hash.id) : hash.id != null) return false;
        if (url != null ? !url.equals(hash.url) : hash.url != null) return false;

        return true;
    }

    @Override
    public String toString() {
        return "HashEntity{" +
                "id='" + id + '\'' +
                ", url='" + url + '\'' +
                ", user=" + buyer +
                '}';
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (url != null ? url.hashCode() : 0);
        return result;
    }
}
