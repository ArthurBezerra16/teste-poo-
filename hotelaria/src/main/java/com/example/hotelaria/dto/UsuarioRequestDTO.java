package com.example.hotelaria.dto;

import com.example.hotelaria.model.Perfil;
import jakarta.validation.constraints.*;

public record UsuarioRequestDTO(

    @NotBlank(message = "Informe o nome")
    String nome,

    @NotBlank(message = "Informe o e-mail")
    @Email(message = "E-mail inválido")
    String email,

    @NotBlank(message = "Informe a senha")
    @Size(min = 8, max = 72)
    String senha,

    @NotNull(message = "Informe o perfil")
    Perfil perfil

) {}