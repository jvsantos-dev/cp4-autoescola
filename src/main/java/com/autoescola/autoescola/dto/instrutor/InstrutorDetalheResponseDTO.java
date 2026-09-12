package com.autoescola.autoescola.dto.instrutor;

import com.autoescola.autoescola.dto.common.EnderecoResponseDTO;
import com.autoescola.autoescola.model.Especialidade;
import com.autoescola.autoescola.model.Instrutor;

public record InstrutorDetalheResponseDTO(
        Long id,
        String nome,
        String email,
        String telefone,
        String cnh,
        Especialidade especialidade,
        EnderecoResponseDTO endereco,
        boolean ativo) {

    public static InstrutorDetalheResponseDTO from(Instrutor instrutor) {
        return new InstrutorDetalheResponseDTO(
                instrutor.getId(),
                instrutor.getNome(),
                instrutor.getEmail(),
                instrutor.isTelefoneVisivel() ? instrutor.getTelefone() : null,
                instrutor.getCnh(),
                instrutor.getEspecialidade(),
                EnderecoResponseDTO.from(instrutor.getEndereco()),
                instrutor.isAtivo());
    }
}
