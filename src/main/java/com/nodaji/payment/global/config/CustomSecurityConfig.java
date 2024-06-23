package com.nodaji.payment.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
@EnableWebSecurity
public class CustomSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http.csrf(AbstractHttpConfigurer::disable);
        http.cors(c-> c.configurationSource(request -> {
            var corsConfiguration = new CorsConfiguration();
            corsConfiguration.addAllowedHeader("*");
            corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE","OPTIONS"));
            corsConfiguration.setAllowedOrigins(List.of("*"));
            return corsConfiguration;
        }));

        http.authorizeHttpRequests(req->
                req.requestMatchers("/api/v1/**")
                        .permitAll()
                        .anyRequest()
                        .authenticated()
        );
        return http.build();
    }
}
