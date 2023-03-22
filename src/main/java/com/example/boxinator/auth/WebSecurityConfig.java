package com.example.boxinator.auth;

import com.example.boxinator.auth.jwt.JwtAuthConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;


@RequiredArgsConstructor
@Configuration
@EnableMethodSecurity
public class WebSecurityConfig {
    private final JwtAuthConverter jwtAuthConverter;

    @Bean
    @Order(1)
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors()
                .and()
                .sessionManagement().disable()
                .csrf().disable();

        http.authorizeHttpRequests(authorize -> {
            authorize
                    .requestMatchers("/auth/**").permitAll()
                    .requestMatchers("/swagger-ui/**").permitAll()
                    .requestMatchers("/v3/api-docs/**").permitAll()
                    .requestMatchers(HttpMethod.GET, "/boxes/**").permitAll()
                    .requestMatchers(HttpMethod.GET, "/shipments/cost/**").permitAll()
                    .requestMatchers(HttpMethod.GET, "/settings/countries/**").permitAll()
                    .anyRequest().authenticated();
        });

        http.oauth2ResourceServer()
                .jwt()
                .jwtAuthenticationConverter(jwtAuthConverter);

        return http.build();
    }
}