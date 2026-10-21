package com.fullstyle.practica1barrosso;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityConfig.class);

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, SecurityErrorWriter errorWriter) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/health").permitAll()
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated())
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint(authenticationEntryPoint(errorWriter))
                        .accessDeniedHandler((request, response, exception) -> {
                            LOGGER.warn("Authorization denied for {} {}", request.getMethod(),
                                    request.getRequestURI());
                            errorWriter.write(response, HttpStatus.FORBIDDEN,
                                    "No tienes permisos para acceder a este recurso.");
                        }))
                .oauth2ResourceServer(oauth2 -> oauth2
                        .authenticationEntryPoint(authenticationEntryPoint(errorWriter))
                        .jwt(Customizer.withDefaults()))
                .build();
    }

    private AuthenticationEntryPoint authenticationEntryPoint(SecurityErrorWriter errorWriter) {
        return (request, response, exception) -> {
            LOGGER.warn("Authentication rejected for {} {}", request.getMethod(), request.getRequestURI());
            errorWriter.write(response, HttpStatus.UNAUTHORIZED,
                    "Autenticación requerida o token inválido.");
        };
    }
}
