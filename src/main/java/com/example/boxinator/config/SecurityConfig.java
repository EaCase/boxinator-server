package com.example.boxinator.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    /**
     *  Prevent XSS attacks
     **/
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.headers()
                .xssProtection()
                .and()
                .contentSecurityPolicy("script-src 'self'");
        return http.build();
    }


}
