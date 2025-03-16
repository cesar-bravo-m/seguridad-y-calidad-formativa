package com.example.formativa.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.formativa.services.UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth, UserService userService) throws Exception {
        auth.userDetailsService(userService)
            .passwordEncoder(passwordEncoder);

        // Add users user1, user2, user3, user4 with password pass1, pass2, pass3, pass4
        auth.inMemoryAuthentication()
            .withUser("user1").password(passwordEncoder.encode("pass1")).roles("USER")
            .and()
            .withUser("user2").password(passwordEncoder.encode("pass2")).roles("USER")
            .and()
            .withUser("user3").password(passwordEncoder.encode("pass3")).roles("USER")
            .and()
            .withUser("user4").password(passwordEncoder.encode("pass4")).roles("USER");
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .headers(headers -> headers.frameOptions().disable())
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/", "/home").permitAll()
                .requestMatchers("/h2-console/**").permitAll()
                .requestMatchers("/css/**", "/js/**", "/images/**", "/styles.css").permitAll()
                .requestMatchers("/register", "/login").permitAll()
                .requestMatchers("/details/**", "/profile/**", "/search", "/search/**").authenticated()
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .permitAll()
                .defaultSuccessUrl("/", true)
            )
            .logout(logout -> logout
                .permitAll()
            );
        
        return http.build();
    }
} 