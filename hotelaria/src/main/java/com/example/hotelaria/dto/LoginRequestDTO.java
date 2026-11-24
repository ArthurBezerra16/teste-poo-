package com.example.hotelaria.dto;

import jakarta.validation.constraints.*;

public record LoginRequestDTO(

    @NotBlank
    @Email
    String email,

    @NotBlank
    String senha

) {}