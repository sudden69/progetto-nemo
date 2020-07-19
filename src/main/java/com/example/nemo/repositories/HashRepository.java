package com.example.nemo.repositories;

import com.example.nemo.entity.HashEntity;
import com.example.nemo.entity.UserEntity;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.*;

import java.util.List;
import java.util.Set;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

@Repository
public interface HashRepository extends JpaRepository<HashEntity,String> {

    HashEntity findByUrl (String url);
    Set<HashEntity> findByBuyer(UserEntity user);
    Page <HashEntity> findByBuyer(Pageable paging, UserEntity user);
    //boolean existsByIdAndBuyerNot(String id,UserEntity user);
    HashEntity findByUrlAndBuyer(String url, UserEntity user);
    boolean existsByShUrl(String shUrl);
    HashEntity findHashByShUrl(String shUrl);
    Page <HashEntity> findAll(Pageable paging);

}
