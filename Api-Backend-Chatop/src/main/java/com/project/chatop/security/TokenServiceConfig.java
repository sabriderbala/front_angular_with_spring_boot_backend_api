package com.project.chatop.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;

import com.project.chatop.service.TokenService;

// Fichier de config pour le token
@Configuration
public class TokenServiceConfig {

    @Bean
    public TokenService tokenService(JwtEncoder jwtEncoder, JwtDecoder jwtDecoder) {
        return new TokenService(jwtEncoder, jwtDecoder);
    }
}
