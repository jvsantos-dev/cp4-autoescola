package com.autoescola.autoescola.dto.instrutor;

import com.autoescola.autoescola.model.Especialidade;
import com.autoescola.autoescola.model.Instrutor;

public record InstrutorResponseDTO(
        Long id,
        String nome,
        String email,
        String cnh,
        Especialidade especialidade) {

    public static InstrutorResponseDTO from(Instrutor instrutor) {
        return new InstrutorResponseDTO(
                instrutor.getId(),
                instrutor.getNome(),
                instrutor.getEmail(),
                instrutor.getCnh(),
                instrutor.getEspecialidade());
    }
}
