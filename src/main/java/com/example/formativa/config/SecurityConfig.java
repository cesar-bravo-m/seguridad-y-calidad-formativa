// package com.example.formativa.config;
// 
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.security.web.SecurityFilterChain;
// 
// import com.example.formativa.services.UserService;
// 
// @Configuration
// @EnableWebSecurity
// public class SecurityConfig {
// 
//     @Autowired
//     private PasswordEncoder passwordEncoder;
// 
//     @Autowired
//     public void configureGlobal(AuthenticationManagerBuilder auth, UserService userService) throws Exception {
//         auth.userDetailsService(userService)
//             .passwordEncoder(passwordEncoder);
//         // The users are now created by DataInitializer
//     }
// 
//     @Bean
//     public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//         http
//             .csrf(csrf -> csrf.disable())
//             .headers(headers -> headers.frameOptions().disable())
//             .authorizeHttpRequests(authorize -> authorize
//                 .requestMatchers("/", "/home").permitAll()
//                 .requestMatchers("/h2-console/**").permitAll()
//                 .requestMatchers("/css/**", "/js/**", "/images/**", "/styles.css").permitAll()
//                 .requestMatchers("/register", "/login").permitAll()
//                 .requestMatchers("/details/**", "/profile/**", "/search", "/search/**").authenticated()
//                 .anyRequest().authenticated()
//             )
//             .formLogin(form -> form
//                 .loginPage("/login")
//                 .permitAll()
//                 .defaultSuccessUrl("/", true)
//             )
//             .logout(logout -> logout
//                 .permitAll()
//             );
//         
//         return http.build();
//     }
// } 
package com.example.formativa.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.formativa.backend.Constants;
import com.example.formativa.backend.JWTAuthorizationFilter;

@EnableWebSecurity()
@Configuration
class WebSecurityConfig{

    @Autowired
    JWTAuthorizationFilter jwtAuthorizationFilter;

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {

        http
                .csrf((csrf) -> csrf
                        .disable())
                .authorizeHttpRequests( authz -> authz
                        .requestMatchers(HttpMethod.GET, "/", "/home", "/register", "/css/**", "/images/**").permitAll()
                        .requestMatchers(HttpMethod.GET, Constants.LOGIN_URL).permitAll()
                        .requestMatchers(HttpMethod.POST, Constants.LOGIN_URL).permitAll()
                        .anyRequest().authenticated())
                .addFilterAfter(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}