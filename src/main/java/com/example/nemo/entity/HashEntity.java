package com.example.nemo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.CreationTimestamp;
import org.jetbrains.annotations.NotNull;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import java.util.*;

@Entity
@Table(name = "hash",schema = "public")
public class HashEntity {

    @Id
    @Column(name = "id")
    private String id;

    @Basic
    @Column(name = "shurl")
    private String shUrl;

    @Basic
    @Column(name = "url")
    private String url;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties("buyer")
    private UserEntity buyer;

    //qua avevo screenato sai cos'è
    @Basic
    @CreationTimestamp
    @Column(name = "creation_time")
    Timestamp creation;


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

    public String getShUrl(){
        return shUrl;
    }

    public void setShUrl(String custom)
    {
        shUrl=custom;
    }

    public Timestamp getCreation(){
        return creation;
    }

    public void setCreation(Timestamp creation) {
        this.creation = creation;
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
