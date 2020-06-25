package com.example.nemo.repositories;

import com.example.nemo.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    List<UserEntity> findByFirstName(String firstName);
    List<UserEntity> findByLastName(String lastName);
    List<UserEntity> findByEmail(String email);
    List<UserEntity> findByCode(String code);
    boolean existsByEmail(String email);

}