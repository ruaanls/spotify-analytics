package br.com.spotifyanalytics.infra.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig
{

    @Bean
    public JwtAuthenticatorFilter jwtAuthenticatorFilter()
    {
        return new JwtAuthenticatorFilter();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
    {
        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth

                        // 🔓 endpoints públicos
                        .requestMatchers("/auth/**").permitAll()

                        // 🔐 roles
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/premium/**").hasRole("PREMIUM")

                        // 🔒 autenticado
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticatorFilter(),
                        UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
