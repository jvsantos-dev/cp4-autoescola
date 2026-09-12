package com.autoescola.autoescola.dto.common;

import com.autoescola.autoescola.model.Endereco;
import com.autoescola.autoescola.model.UnidadeFederativa;

public record EnderecoResponseDTO(
        String logradouro,
        String numero,
        String complemento,
        String bairro,
        String cidade,
        UnidadeFederativa uf,
        String cep) {

    public static EnderecoResponseDTO from(Endereco endereco) {
        return new EnderecoResponseDTO(
                endereco.getLogradouro(),
                endereco.getNumero(),
                endereco.getComplemento(),
                endereco.getBairro(),
                endereco.getCidade(),
                endereco.getUf(),
                endereco.getCep());
    }
}
