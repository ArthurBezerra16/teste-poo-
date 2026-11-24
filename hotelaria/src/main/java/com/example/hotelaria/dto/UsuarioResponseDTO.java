package com.example.hotelaria.dto;

import com.example.hotelaria.model.Perfil;

public record UsuarioResponseDTO(

    Long id,
    String nome,
    String email,
    Perfil perfil,
    Boolean ativo

) {}