package com.example.hotelaria.config;

import com.example.hotelaria.model.Usuario;
import com.example.hotelaria.model.Perfil;
import com.example.hotelaria.repository.UsuarioRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

@Component
public class AdminInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${admin.email}")
    private String email;

    @Value("${admin.senha}")
    private String senha;

    public AdminInitializer(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder) {

        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {

        String emailNormalizado = email.trim()
                .toLowerCase(Locale.ROOT);

        if (usuarioRepository.existsByEmail(emailNormalizado)) {
            return;
        }

        if (senha.length() < 8 ||
                senha.getBytes(StandardCharsets.UTF_8).length > 72) {

            throw new IllegalStateException(
                "Senha inicial do administrador invalida"
            );
        }

        Usuario admin = new Usuario();

        admin.setNome("Administrador");
        admin.setEmail(emailNormalizado);
        admin.setSenha(passwordEncoder.encode(senha));
        admin.setPerfil(Perfil.ADMIN);
        admin.setAtivo(true);

        usuarioRepository.save(admin);

        System.out.println(
            "Administrador inicial cadastrado com sucesso!"
        );
    }
}