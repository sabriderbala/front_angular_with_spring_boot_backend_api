package com.project.chatop.controller;

import java.util.Date;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.chatop.model.AuthSuccess;
import com.project.chatop.model.User;
import com.project.chatop.repository.UserRepository;
import com.project.chatop.service.TokenService;
import com.project.chatop.service.UserService;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "AuthController", description = "API pour les opérations de connexion register login etc..")
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    private static final Logger LOG = LoggerFactory.getLogger(AuthController.class);

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final UserRepository userRepository;
    @Autowired
    private final UserService userService;


    public AuthController(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder,
            TokenService tokenService, UserRepository userRepository, UserService userService) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    // Route pour login
    @PostMapping("/login")
    public ResponseEntity<AuthSuccess> login(@RequestBody User user) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        if (userDetails != null && passwordEncoder.matches(user.getPassword(), userDetails.getPassword())) {
            LOG.debug("Login successful for user: '{}'", userDetails.getUsername());
            String token = tokenService.generateToken(userDetails);
            AuthSuccess authSuccess = new AuthSuccess(token);
            return ResponseEntity.ok(authSuccess);
        } else {
            LOG.debug("Login failed for user: '{}'", user.getEmail());
            throw new RuntimeException("Invalid credentials");
        }
    }

    // Route pour register
    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody User user) {
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            LOG.debug("User registration failed: Email already exists");
            return new ResponseEntity<>(Map.of("message", "Email already exists"), HttpStatus.BAD_REQUEST);
        }

        String encodedPassword = passwordEncoder.encode(user.getPassword());

        User newUser = new User();
        newUser.setName(user.getName());
        newUser.setEmail(user.getEmail());
        newUser.setPassword(encodedPassword);
        newUser.setCreatedAt(new Date());
        newUser.setUpdatedAt(new Date());

        userRepository.save(newUser);

        LOG.debug("User registered: '{}'", newUser.getName());
        return new ResponseEntity<>(Map.of("message", "Registration successfull!"), HttpStatus.OK);
    }



    // Route pour information utilisateur
    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();
            User user = userService.getUserByEmail(email);
            if (user != null) {
                return ResponseEntity.ok(user);
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null); // User is not authenticated
    }

    





}
