package com.example.nemo.repositories;

import com.example.nemo.entity.HashEntity;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.*;


@Repository
public interface HashRepository extends JpaRepository<HashEntity,String> {

    HashEntity findByUrl (String url);


}
