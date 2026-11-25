package com.example.hotelaria.security;

import com.example.hotelaria.repository.UsuarioRepository;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.HttpMethod;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtService jwtService;
    private final UsuarioRepository usuarioRepository;

    public SecurityConfig(
            JwtService jwtService,
            UsuarioRepository usuarioRepository) {

        this.jwtService = jwtService;
        this.usuarioRepository = usuarioRepository;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        JwtFilter jwtFilter = new JwtFilter(
                jwtService,
                usuarioRepository
        );

        http
            .csrf(AbstractHttpConfigurer::disable)

            .cors(cors -> cors.configurationSource(
                    corsConfigurationSource()
            ))

            .sessionManagement(session ->
                session.sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS
                )
            )

            .formLogin(AbstractHttpConfigurer::disable)

            .httpBasic(AbstractHttpConfigurer::disable)

            .exceptionHandling(exception -> exception
                .authenticationEntryPoint(
                    (request, response, ex) ->
                        response.setStatus(401)
                )
                .accessDeniedHandler(
                    (request, response, ex) ->
                        response.setStatus(403)
                )
            )

            .authorizeHttpRequests(auth -> auth

                // Permitir login
                .requestMatchers(
                    HttpMethod.POST,
                    "/auth/login"
                ).permitAll()

                // Gerenciamento de usuarios
                .requestMatchers(
                    "/usuarios",
                    "/usuarios/**"
                ).hasRole("ADMIN")

                // Consultar quartos e servicos
                .requestMatchers(
                    HttpMethod.GET,
                    "/quartos",
                    "/quartos/**",
                    "/servicos",
                    "/servicos/**"
                ).hasAnyRole("ADMIN", "FUNCIONARIO")

                // Gerenciar quartos e servicos
                .requestMatchers(
                    "/quartos",
                    "/quartos/**",
                    "/servicos",
                    "/servicos/**"
                ).hasRole("ADMIN")

                // Operacoes de hospedagem
                .requestMatchers(
                    "/hospedes",
                    "/hospedes/**",
                    "/reservas",
                    "/reservas/**",
                    "/pagamentos",
                    "/pagamentos/**"
                ).hasAnyRole("ADMIN", "FUNCIONARIO")

                // Dashboard
                .requestMatchers(
                    HttpMethod.GET,
                    "/dashboard",
                    "/dashboard/**"
                ).hasAnyRole("ADMIN", "FUNCIONARIO")

                // Bloquear rotas nao configuradas
                .anyRequest().denyAll()
            )

            .addFilterBefore(
                jwtFilter,
                UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }

    @Bean
public CorsConfigurationSource corsConfigurationSource() {

    CorsConfiguration configuration =
            new CorsConfiguration();

    configuration.setAllowedOriginPatterns(
    List.of(
        "http://localhost:5173",
        "https://localhost:5173",
        "http://127.0.0.1:5173",
        "https://127.0.0.1:5173",
        "https://*-5173.app.github.dev"
    )
);

    configuration.setAllowedMethods(
        List.of(
            "GET",
            "POST",
            "PUT",
            "PATCH",
            "DELETE",
            "OPTIONS"
        )
    );

    configuration.setAllowedHeaders(
        List.of("*")
    );

    configuration.setAllowCredentials(false);

    configuration.setMaxAge(3600L);

    UrlBasedCorsConfigurationSource source =
            new UrlBasedCorsConfigurationSource();

    source.registerCorsConfiguration(
            "/**",
            configuration
    );

    return source;
}
}