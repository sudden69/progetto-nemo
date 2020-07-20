package com.example.nemo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;
import java.util.Set;

//NEMO
@Entity
@Table(name = "user",schema = "public")
public class UserEntity {
    @Id
    @Column(name = "user_id", nullable = false)
    private String id;

    @JsonIgnore
    @OneToMany(targetEntity = HashEntity.class ,mappedBy = "buyer",cascade=CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<HashEntity> hashes;



    public Set<HashEntity> getHashes() {
        return hashes;
    }

    public void setHashes(Set<HashEntity> hashes) {
        this.hashes = hashes;
    }

    public String getUserId() {
        return id;
    }

    public void setUserId(String id) {
        this.id = id;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity that = (UserEntity) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                '}';
    }


}