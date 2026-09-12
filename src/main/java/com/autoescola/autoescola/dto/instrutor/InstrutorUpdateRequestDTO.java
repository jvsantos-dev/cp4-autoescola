package com.autoescola.autoescola.dto.instrutor;

import com.autoescola.autoescola.dto.common.EnderecoDTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record InstrutorUpdateRequestDTO(
        @NotBlank(message = "Nome é obrigatório") String nome,

        @NotBlank(message = "Telefone é obrigatório")
        @Pattern(regexp = "^\\(?\\d{2}\\)?\\s?\\d{4,5}-?\\d{4}$", message = "Telefone inválido")
        String telefone,

        @NotNull(message = "Endereço é obrigatório")
        @Valid
        EnderecoDTO endereco) {
}
