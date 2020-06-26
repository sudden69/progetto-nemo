package com.example.nemo.repositories;

import com.example.nemo.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    List<UserEntity> findByFirstName(String firstName);
    List<UserEntity> findByLastName(String lastName);
    List<UserEntity> findByEmail(String email);
    List<UserEntity> findByCode(String code);
   // Optional <UserEntity> findById(Integer id);
    boolean existsByEmail(String email);

}