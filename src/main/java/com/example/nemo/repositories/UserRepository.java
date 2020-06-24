package com.example.nemo.repositories;

import com.example.nemo.entity.Hash;
import com.example.nemo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,String> {
    User findByName (String name);
    
}
