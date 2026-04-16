package com.app.pos.system.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{

        http
                .csrf(csrf -> csrf.disable())
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint))
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth ->auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/products/**").hasAnyRole("ADMIN", "MANAGER")
                        .requestMatchers(HttpMethod.POST,"/api/products/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,"/api/products/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH,"/api/products/**").hasRole("ADMIN")
                        .requestMatchers("/api/pos/sales/**").hasAnyRole("CASHIER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/promotions/**").hasAnyRole("ADMIN", "MANAGER")
                        .requestMatchers("/api/promotions/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/stores/{id}/details", "/api/stores/{id}").hasAnyRole("ADMIN", "MANAGER")
                        .requestMatchers(HttpMethod.GET, "/api/stores/**").hasAnyRole("ADMIN")
                        .requestMatchers("/api/stores/**").hasRole("ADMIN")
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/mgmt/**").hasAnyRole("ADMIN", "MANAGER")
                        .requestMatchers("/api/pos/**").hasAnyRole("ADMIN", "MANAGER", "CASHIER")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(Customizer.withDefaults())
                );

        return http.build();
    }
}
