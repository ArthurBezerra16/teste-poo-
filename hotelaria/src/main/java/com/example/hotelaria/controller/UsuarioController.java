package com.example.hotelaria.controller;

import com.example.hotelaria.dto.UsuarioRequestDTO;
import com.example.hotelaria.dto.UsuarioResponseDTO;
import com.example.hotelaria.service.UsuarioService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(
            UsuarioService usuarioService) {

        this.usuarioService = usuarioService;
    }

    // CADASTRAR USUARIO
    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> cadastrar(
            @Valid @RequestBody UsuarioRequestDTO dto) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(usuarioService.cadastrar(dto));
    }

    // LISTAR USUARIOS
    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> listar() {

        return ResponseEntity.ok(
                usuarioService.listar()
        );
    }

    // BUSCAR USUARIO POR ID
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> buscarPorId(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                usuarioService.buscarPorId(id)
        );
    }

    // DESATIVAR USUARIO
    @PatchMapping("/{id}/desativar")
    public ResponseEntity<UsuarioResponseDTO> desativar(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                usuarioService.desativar(id)
        );
    }

    // ATIVAR USUARIO
    @PatchMapping("/{id}/ativar")
    public ResponseEntity<UsuarioResponseDTO> ativar(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                usuarioService.ativar(id)
        );
    }
}