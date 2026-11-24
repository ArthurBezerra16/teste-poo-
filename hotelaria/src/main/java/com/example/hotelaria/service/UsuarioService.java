package com.example.hotelaria.service;

import com.example.hotelaria.model.Usuario;
import com.example.hotelaria.repository.UsuarioRepository;
import com.example.hotelaria.dto.UsuarioRequestDTO;
import com.example.hotelaria.dto.UsuarioResponseDTO;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Locale;
import java.nio.charset.StandardCharsets;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder) {

        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // CADASTRAR USUÁRIO
    @Transactional
    public UsuarioResponseDTO cadastrar(UsuarioRequestDTO dto) {

        String email = dto.email()
                .trim()
                .toLowerCase(Locale.ROOT);

        if (usuarioRepository.existsByEmail(email)) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "E-mail já cadastrado"
            );
        }

        if (dto.senha().getBytes(StandardCharsets.UTF_8).length > 72) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "A senha deve possuir no máximo 72 bytes"
            );
        }

        Usuario usuario = new Usuario();

        usuario.setNome(dto.nome().trim());
        usuario.setEmail(email);

        usuario.setSenha(
            passwordEncoder.encode(dto.senha())
        );

        usuario.setPerfil(dto.perfil());
        usuario.setAtivo(true);

        Usuario salvo = usuarioRepository.save(usuario);

        return converterDTO(salvo);
    }

    // LISTAR USUÁRIOS
    @Transactional(readOnly = true)
    public List<UsuarioResponseDTO> listar() {

        return usuarioRepository.findAll()
                .stream()
                .map(this::converterDTO)
                .toList();
    }

    // BUSCAR USUÁRIO POR ID
    @Transactional(readOnly = true)
    public UsuarioResponseDTO buscarPorId(Long id) {

        Usuario usuario = encontrarUsuario(id);

        return converterDTO(usuario);
    }

    // DESATIVAR USUÁRIO
    @Transactional
    public UsuarioResponseDTO desativar(Long id) {

        Usuario usuario = encontrarUsuario(id);

        usuario.setAtivo(false);

        return converterDTO(
            usuarioRepository.save(usuario)
        );
    }

    // REATIVAR USUÁRIO
    @Transactional
    public UsuarioResponseDTO ativar(Long id) {

        Usuario usuario = encontrarUsuario(id);

        usuario.setAtivo(true);

        return converterDTO(
            usuarioRepository.save(usuario)
        );
    }

    // MÉTODO AUXILIAR DE BUSCA
    private Usuario encontrarUsuario(Long id) {

        return usuarioRepository.findById(id)
                .orElseThrow(() ->
                    new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Usuário não encontrado"
                    )
                );
    }

    // CONVERTER ENTIDADE PARA DTO
    private UsuarioResponseDTO converterDTO(Usuario usuario) {

        return new UsuarioResponseDTO(
            usuario.getId(),
            usuario.getNome(),
            usuario.getEmail(),
            usuario.getPerfil(),
            usuario.getAtivo()
        );
    }
}