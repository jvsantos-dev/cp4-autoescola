package com.autoescola.autoescola.dto.aluno;

import com.autoescola.autoescola.model.Aluno;

public record AlunoResponseDTO(
        Long id,
        String nome,
        String email,
        String cpf) {

    public static AlunoResponseDTO from(Aluno aluno) {
        return new AlunoResponseDTO(aluno.getId(), aluno.getNome(), aluno.getEmail(), aluno.getCpf());
    }
}
