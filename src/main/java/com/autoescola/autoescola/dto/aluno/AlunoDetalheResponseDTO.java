package com.autoescola.autoescola.dto.aluno;

import com.autoescola.autoescola.dto.common.EnderecoResponseDTO;
import com.autoescola.autoescola.model.Aluno;

public record AlunoDetalheResponseDTO(
        Long id,
        String nome,
        String email,
        String telefone,
        String cpf,
        EnderecoResponseDTO endereco,
        boolean ativo) {

    public static AlunoDetalheResponseDTO from(Aluno aluno) {
        return new AlunoDetalheResponseDTO(
                aluno.getId(),
                aluno.getNome(),
                aluno.getEmail(),
                aluno.getTelefone(),
                aluno.getCpf(),
                EnderecoResponseDTO.from(aluno.getEndereco()),
                aluno.isAtivo());
    }
}
