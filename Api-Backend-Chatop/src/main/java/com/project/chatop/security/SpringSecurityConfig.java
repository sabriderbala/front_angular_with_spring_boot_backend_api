// SpringSecurityConfig.java
package com.project.chatop.security;


import java.util.Arrays;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.project.chatop.service.UserService;

@Configuration
@EnableWebSecurity
// Fichier de configuration sécurité
public class SpringSecurityConfig {

    private final RsaKeyProperties rsaKeys;
    
 // Injecter le UserService et le PasswordEncoder ici
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;


    public SpringSecurityConfig(RsaKeyProperties rsaKeys) {
        this.rsaKeys = rsaKeys;
    }


    @Bean
    DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userService); // Utilisation directe de userService
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }


 // Méthode pour les filtres de sécurité
    @SuppressWarnings({"removal"})
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authenticationProvider(authenticationProvider())
            .cors(withDefaults())
            .csrf(csrf -> csrf.ignoringRequestMatchers(
                "/api/auth/**",
                "/login/**",
                "/h2-ui/**",
                "/v3/api-docs/**",
                "/swagger-ui/**"
            ))
            .headers(headers -> headers.frameOptions().sameOrigin())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/api/user/**",
                    "/api/auth/**"
                ).permitAll()
                .requestMatchers("/api/rentals/**", "/api/messages/**").authenticated() // Accès autorisé uniquement aux utilisateurs authentifiés
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/h2-ui/**").permitAll() // Accès autorisé uniquement aux utilisateurs authentifiés
            )
            .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .httpBasic(withDefaults())
            .formLogin(form -> form
                .defaultSuccessUrl("/swagger-ui/index.html", true)
                .loginProcessingUrl("/login")
                .permitAll()
            );
        return http.build();
    }



    // Méthode pour CORS autorisé les requêtes depuis l'app Angular
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(Arrays.asList("*"));

        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    @Bean
    JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(rsaKeys.publicKey()).build();
    }

    @Bean
    JwtEncoder jwtEncoder() {
        JWK jwk = new RSAKey.Builder(rsaKeys.publicKey()).privateKey(rsaKeys.privateKey()).build();
        JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }

}
