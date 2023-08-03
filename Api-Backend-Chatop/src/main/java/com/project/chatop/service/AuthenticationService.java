package com.project.chatop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.chatop.repository.UserRepository;

import java.util.Collections;

// Service pour Authentication
@Service
public class AuthenticationService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService customTokenService;

    @Autowired
    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder, TokenService customTokenService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.customTokenService = customTokenService;
    }

    // Méthode pour trouver rechercher si le user existe en DB
    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        com.project.chatop.model.User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new User(user.getEmail(), passwordEncoder.encode(user.getPassword()), Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
    }

    // Méthode pour l'authentification
    public Authentication authenticateUser(String email, String password) {
        User user = loadUserByUsername(email);
        if (passwordEncoder.matches(password, user.getPassword())) {
            return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        } else {
            throw new BadCredentialsException("Invalid username/password");
        }
    }

    // Méthode pour généré un token
    public String generateToken(UserDetails userDetails) {
        return customTokenService.generateToken(userDetails);
    }

}
