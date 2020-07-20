package com.example.nemo.repositories;

import com.example.nemo.entity.HashEntity;
import com.example.nemo.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.*;


import java.util.List;
import java.util.Set;


@Repository
public interface HashRepository extends JpaRepository<HashEntity,String> {
    //@Query("select e from #{#HashEntity} e where e.alive=false")
    HashEntity findByUrl (String url);
    List <HashEntity> findByBuyer(UserEntity user);
    boolean existsByIdAndBuyerNot(String id,UserEntity user);
    HashEntity findByUrlAndBuyer(String url, UserEntity user);
    boolean existsByShUrl(String shUrl);
    HashEntity findByShUrl(String shUrl);
    Page<HashEntity> findAllByBuyer(Pageable pageable, UserEntity user);
}
