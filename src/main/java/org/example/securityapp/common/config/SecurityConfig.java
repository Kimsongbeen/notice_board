package org.example.securityapp.common.config;

import lombok.RequiredArgsConstructor;
import org.example.securityapp.security.auth.CustomAuthenticationProvider;
import org.example.securityapp.security.exception.CustomAuthenticationFailureHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, CustomAuthenticationFailureHandler customAuthenticationFailureHandler) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .formLogin(form -> form
                        .loginProcessingUrl("/login")
                        .failureHandler(customAuthenticationFailureHandler))
                .httpBasic(basic -> basic.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll()
                        .anyRequest().authenticated());
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(
            CustomAuthenticationProvider provider
    ) {
        return provider;
    }

}

