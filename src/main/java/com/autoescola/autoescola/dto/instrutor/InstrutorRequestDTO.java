package com.autoescola.autoescola.dto.instrutor;

import com.autoescola.autoescola.dto.common.EnderecoDTO;
import com.autoescola.autoescola.model.Especialidade;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record InstrutorRequestDTO(
        @NotBlank(message = "Nome é obrigatório") String nome,

        @NotBlank(message = "E-mail é obrigatório")
        @Email(message = "E-mail inválido")
        String email,

        @NotBlank(message = "Telefone é obrigatório")
        @Pattern(regexp = "^\\(?\\d{2}\\)?\\s?\\d{4,5}-?\\d{4}$", message = "Telefone inválido")
        String telefone,

        @NotBlank(message = "CNH é obrigatória")
        @Pattern(regexp = "\\d{11}", message = "CNH deve conter 11 dígitos")
        String cnh,

        @NotNull(message = "Especialidade é obrigatória")
        Especialidade especialidade,

        @NotNull(message = "Endereço é obrigatório")
        @Valid
        EnderecoDTO endereco) {
}
