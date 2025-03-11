package com.popov.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsService userDetailsService;

    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf.disable()) // ✅ Disable CSRF protection
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/register", "/login").permitAll()
            .requestMatchers("/events/create", "/events/edit/**", "/events/delete/**").authenticated()
            .anyRequest().permitAll()
        )
        .formLogin(form -> form
            .loginPage("/login")
            .defaultSuccessUrl("/events", true) // ✅ Force redirect after login
            .failureUrl("/login?error=true")
            .permitAll()
        )
        .logout(logout -> logout
            .logoutSuccessUrl("/login")
            .permitAll()
        )
        .userDetailsService(userDetailsService);

    return http.build();
}

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}