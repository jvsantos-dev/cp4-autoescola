package com.autoescola.autoescola.dto.usuario;

import com.autoescola.autoescola.model.Perfil;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UsuarioUpdateRequestDTO(
        @NotBlank(message = "Nome é obrigatório") String nome,

        @NotNull(message = "Perfil é obrigatório")
        Perfil perfil) {
}
