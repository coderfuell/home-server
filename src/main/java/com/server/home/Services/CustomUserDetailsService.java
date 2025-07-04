package com.server.home.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.server.home.Exception.EmailAlreadyTakenException;
import com.server.home.Exception.UsernameAlreadyExistsException;
import com.server.home.Model.Role;
import com.server.home.Model.User;
import com.server.home.Repositories.UserRepository;


@Service
public class CustomUserDetailsService implements UserDetailsService{
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User loadUserByUsername(String username){
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("user not found");
        }
        return user;
    }

    public User createUser(String username, String name, String password, String email, Role role){
        if (role == null){
            role = Role.WRITE;
        }
        if (userRepository.findByEmail(email) != null){
            throw new EmailAlreadyTakenException();
        }
        if (userRepository.findByUsername(username) != null){
            throw new UsernameAlreadyExistsException();
        }
        String hashedPassword = passwordEncoder.encode(password);
        User user = new User(username, name, email, hashedPassword, role);
        userRepository.save(user);
        return user;
    }

    public User createUser(User user){
        createUser(user.getUsername(), user.getName(), user.getPassword(), user.getEmail(), user.getRole());
        return user;
    }

    public User verifyUserByUsername(String username, String password){
        User user = userRepository.findByUsername(username);
        if (user == null){
            throw new UsernameNotFoundException("username is invalid");
        }
        if (verifyPassword(password, user.getPassword())) {
            return user;
        }
        throw new BadCredentialsException("invalid password");
    }

    public User verifyUserByEmail(String email, String password){
        User user = userRepository.findByEmail(email);
        if (user == null){
            throw new UsernameNotFoundException("email is invalid");
        }
        if (verifyPassword(password, user.getPassword())){
            return user;
        }
        throw new BadCredentialsException("invalid password");
    }

    private Boolean verifyPassword(String enteredPassword, String userPassword){
        return passwordEncoder.matches(enteredPassword, userPassword);
    }

    
}
