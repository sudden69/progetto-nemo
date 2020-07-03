package com.example.nemo.repositories;

import com.example.nemo.entity.HashEntity;
import com.example.nemo.entity.UserEntity;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.*;

import java.util.List;
import java.util.Set;


@Repository
public interface HashRepository extends JpaRepository<HashEntity,String> {

    HashEntity findByUrl (String url);
    Set<HashEntity> findByBuyer(UserEntity user);
    boolean existsByIdAndBuyerNot(String id,UserEntity user);
    //boolean existsByShUrl(String shUrl);
}
