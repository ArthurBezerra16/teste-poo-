package com.example.hotelaria.dto;

import com.example.hotelaria.model.Perfil;

public record LoginResponseDTO(

    Long id,
    String nome,
    String email,
    Perfil perfil,
    String token

) {}