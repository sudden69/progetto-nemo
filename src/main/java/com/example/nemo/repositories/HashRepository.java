package com.example.nemo.repositories;

import com.example.nemo.entity.HashEntity;
import com.example.nemo.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.*;


import java.util.List;
import java.util.Set;


//NEMO
@Repository
public interface HashRepository extends JpaRepository<HashEntity,String> {
    List <HashEntity> findByBuyerAndAliveIsTrue(UserEntity user);
    HashEntity findByUrlAndBuyerAndAliveIsTrue(String url, UserEntity user);
    boolean existsByShUrl(String shUrl);
    HashEntity findByShUrlAndAliveIsTrue(String shUrl);
    Page<HashEntity> findAllByBuyerAndAliveIsTrue(Pageable pageable, UserEntity user);
}
