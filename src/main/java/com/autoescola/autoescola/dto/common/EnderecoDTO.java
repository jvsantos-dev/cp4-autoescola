package com.autoescola.autoescola.dto.common;

import com.autoescola.autoescola.model.UnidadeFederativa;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record EnderecoDTO(
        @NotBlank(message = "Logradouro é obrigatório") String logradouro,

        String numero,

        String complemento,

        @NotBlank(message = "Bairro é obrigatório") String bairro,

        @NotBlank(message = "Cidade é obrigatória") String cidade,

        @NotNull(message = "UF é obrigatória") UnidadeFederativa uf,

        @NotBlank(message = "CEP é obrigatório")
        @Pattern(regexp = "\\d{5}-?\\d{3}", message = "CEP inválido")
        String cep) {
}
