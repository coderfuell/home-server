package com.server.home.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.server.home.Model.User;



public interface UserRepository extends JpaRepository<User, Integer>{
    User findByUsername(String username);
    User findByEmail(String email);
}
