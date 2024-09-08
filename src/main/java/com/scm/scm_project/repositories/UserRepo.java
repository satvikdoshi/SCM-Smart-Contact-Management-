package com.scm.scm_project.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.scm.scm_project.entities.User;

public interface UserRepo extends JpaRepository<User,String>{
    
    public Optional<User> findByEmail(String email);

    public Optional<User> findByEmailToken(String emailToken);
}
