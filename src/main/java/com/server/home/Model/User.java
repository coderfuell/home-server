package com.server.home.Model;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;

import jakarta.annotation.Nonnull;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;




@Entity
public class User implements UserDetails{
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    @Nonnull
    private String username;

    @Nonnull
    private String name;

    @Column(unique = true)
    @Nonnull
    @Email
    private String email;

    @Nonnull
    private String password;
    
    @Enumerated(EnumType.STRING)
    private Role role;

    public User() {
    }

    public User(String username, String name, String email, String password, Role role) {
        this.username = username;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    @Override
    public Collection<Role> getAuthorities(){
        return List.of(this.role);
    }

    public Integer getId() {
        return id;
    }
    @Override
    public String getUsername() {
        return username;
    }
    public String getName() {
        return name;
    }
    public String getEmail() {
        return email;
    }
    @Override
    public String getPassword() {
        return password;
    }
    public Role getRole() {
        return role;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setRole(Role role) {
        this.role = role;
    } 
}
