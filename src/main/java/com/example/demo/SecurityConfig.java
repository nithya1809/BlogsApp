package com.example.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Disable CSRF globally (H2 console requires it disabled)
            .csrf(csrf -> csrf.disable())

            // Allow frames for H2 console UI to work properly
            .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()))

            .authorizeHttpRequests(auth -> auth
            	    // Public blog access
            	    .requestMatchers(HttpMethod.GET, "/blogs/**").permitAll()

            	    // H2 console
            	    .requestMatchers("/h2-console/**").permitAll()

            	    // Swagger UI and OpenAPI docs
            	    .requestMatchers(
            	        "/swagger-ui/**",
            	        "/v3/api-docs/**",
            	        "/swagger-ui.html"
            	    ).permitAll()

            	    // Authenticated blog modifications
            	    .requestMatchers(HttpMethod.POST, "/blogs/**").authenticated()
            	    .requestMatchers(HttpMethod.PUT, "/blogs/**").authenticated()
            	    .requestMatchers(HttpMethod.DELETE, "/blogs/**").authenticated()

            	    // All other requests require authentication
            	    .anyRequest().authenticated()
            	)
            .oauth2ResourceServer(oauth2 -> oauth2.jwt())
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint(authenticationEntryPoint())
                .accessDeniedHandler(accessDeniedHandler())
            );

        return http.build();
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new BearerTokenAuthenticationEntryPoint();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new BearerTokenAccessDeniedHandler();
    }
}