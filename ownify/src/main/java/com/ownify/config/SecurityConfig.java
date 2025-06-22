package com.ownify.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf
                .ignoringRequestMatchers(
                    "/api/**", "/ownify-chat/**", "/ownify/**", "/login",
                    "/wishlist/**", "/dashboard/**", "/messaging/**",
                    "/api/users/check-email", "/api/users/forgot-password",
                    "/api/users/reset-password", "/api/users/change-password",
                    "/h2-console/**" // H2 için CSRF devre dışı
                )
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
            )
            .authorizeHttpRequests(authz -> authz
                .requestMatchers(
                    "/api/users", "/api/login", "/login",
                    "/api/users/check-email", "/signin", "/signup",
                    "/forgotPass", "/css/**", "/js/**", "/images/**",
                    "/dashboard.html", "/dashboardSetting.html", 
                    "/adPosting.html", "/wishlist.html", 
                    "/dashboard/**",
                    "/h2-console/**" // H2 için izin verildi
                ).permitAll()
                .anyRequest().permitAll()
            )
            .formLogin(form -> form.disable())
            .httpBasic(basic -> basic.disable())
            .headers(headers -> headers
                .frameOptions(frame -> frame.sameOrigin()) // H2 iframe için gerekli
            )
            .logout(logout -> logout.disable());

        return http.build();
    }
}
