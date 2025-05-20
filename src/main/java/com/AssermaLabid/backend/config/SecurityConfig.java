package com.AssermaLabid.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/uploads/**","/maintenance", "/login", "/register", "/inscription", "/home/**", "/error").permitAll()
                        .requestMatchers("/produit/**").hasAnyAuthority("ADMIN", "USER")
                        .requestMatchers("/","/api/placeholder/**", "/panier/**").hasAuthority("USER")
                        .requestMatchers("/admin/**").hasAuthority("ADMIN")

                        .anyRequest().denyAll()
                )

                .formLogin(form -> form
                        .loginPage("/login")
                        .usernameParameter("email")
                        .passwordParameter("motDePasse")
                        .defaultSuccessUrl("/home", true)
                        .failureHandler((request, response, exception) -> {
                            response.sendRedirect("/login?error=true");
                        })
                        .permitAll()
                )

                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
                        .logoutSuccessUrl("/home")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                );

        return http.build();
    }



    @Bean
    static public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
