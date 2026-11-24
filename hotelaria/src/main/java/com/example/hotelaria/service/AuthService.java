package com.example.hotelaria.service;

import com.example.hotelaria.model.Usuario;
import com.example.hotelaria.repository.UsuarioRepository;
import com.example.hotelaria.dto.LoginRequestDTO;
import com.example.hotelaria.dto.LoginResponseDTO;
import com.example.hotelaria.security.JwtService;

import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.Locale;
import java.nio.charset.StandardCharsets;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {

        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public LoginResponseDTO login(LoginRequestDTO dto) {

        String email = dto.email()
                .trim()
                .toLowerCase(Locale.ROOT);

        if (dto.senha().getBytes(StandardCharsets.UTF_8).length > 72) {
            throw credenciaisInvalidas();
        }

Usuario usuario = usuarioRepository
        .findByEmail(email)
        .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.UNAUTHORIZED,
                "E-mail ou senha inválidos"
        ));

        // Verificar senha
        if (!passwordEncoder.matches(
                dto.senha(),
                usuario.getSenha())) {

            throw credenciaisInvalidas();
        }

        // Verificar se o usuário está ativo
        if (!Boolean.TRUE.equals(usuario.getAtivo())) {

            throw credenciaisInvalidas();
        }

        // Gerar token
        String token = jwtService.gerarToken(usuario);

        // Retornar dados do usuário
        return new LoginResponseDTO(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getPerfil(),
                token
        );
    }

    private ResponseStatusException credenciaisInvalidas() {

        return new ResponseStatusException(
                HttpStatus.UNAUTHORIZED,
                "E-mail ou senha inválidos"
        );
    }
}