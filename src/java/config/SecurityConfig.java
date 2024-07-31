package com.springflights.config;

import com.springflights.model.Customer;
import com.springflights.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
public class SecurityConfig {


    @Autowired
    private CustomerRepository customerRepository;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeRequests(authorize -> authorize
                        .requestMatchers("/flights", "/flights/searchFlights", "/myPage").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                        .requestMatchers("/adminPanel").hasRole("ADMIN")
                        .requestMatchers("/", "/**").permitAll())
                .formLogin(login -> login
                        .loginPage("/login")
                        .failureUrl("/login?error=true")
                        .defaultSuccessUrl("/home", true))
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true"));

        return http.build();
    }

    @Bean
    public UserDetailsService getUserDetailsService() {
        return username -> {
            Customer user = customerRepository.findByUsername(username);
            if (user != null) {
                return (UserDetails) user;
            }
            throw new UsernameNotFoundException("User '" + username + "' not found");
        };
    }









}
