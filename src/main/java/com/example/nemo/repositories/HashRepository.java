package com.example.nemo.repositories;

import com.example.nemo.entity.Hash;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.*;


@Repository
public interface HashRepository extends JpaRepository<Hash,String> {

    Hash findByUrl (String url);

}
