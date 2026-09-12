package com.autoescola.autoescola.dto.auth;

public record LoginResponseDTO(String token, String tipo) {

    public LoginResponseDTO(String token) {
        this(token, "Bearer");
    }
}
