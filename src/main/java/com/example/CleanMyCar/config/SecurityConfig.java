package com.example.CleanMyCar.config;

import com.example.CleanMyCar.security.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf(csrf -> csrf.disable());

        // CORS
        http.cors(cors -> cors.configurationSource(request -> {
            var config = new org.springframework.web.cors.CorsConfiguration();
            config.addAllowedOriginPattern("*");
            config.addAllowedHeader("*");
            config.addAllowedMethod("*");
            config.setAllowCredentials(true);
            return config;
        }));

        http.authorizeHttpRequests(auth -> auth

                // --------- PUBLIC ROUTES (NO JWT) ----------
                .requestMatchers(
                        "/api/customers/**",
                        "/api/plans/**",
                        "/api/subscriptions/**",
                        "/api/admin/login",
                        "/api/admin/decode",
                        "/api/admins/export/**","/api/admin/employees"
                ).permitAll()

                // --------- ADMIN ROUTES (JWT REQUIRED) ----------
                .requestMatchers("/api/admin/**", "/api/admins/**").authenticated()

                // everything else public
                .anyRequest().permitAll()
        );

        // Attach JWT filter only for admin
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
