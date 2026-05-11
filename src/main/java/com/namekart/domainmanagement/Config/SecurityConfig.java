package com.namekart.domainmanagement.Config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final String[] PUBLIC_PATHS = {
        "/health",
        "/actuator/**",
        "/api-docs/**",
        "/swagger-ui/**",
        "/swagger-ui.html",
        "/v3/api-docs/**"
    };

    @Value("${domainmanagement.security.enforce:false}")
    private boolean securityEnforce;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
        http.sessionManagement(session ->
            session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.cors(cors -> {});

        if (!securityEnforce) {
            log.warn("Security enforcement is DISABLED (domainmanagement.security.enforce=false). All endpoints are open.");
            http.authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
        } else {
            log.info("Security enforcement is ENABLED. Public paths: {}", (Object) PUBLIC_PATHS);
            http.authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
        }

        return http.build();
    }
}
