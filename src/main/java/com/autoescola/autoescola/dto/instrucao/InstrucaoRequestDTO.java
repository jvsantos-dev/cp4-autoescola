package com.autoescola.autoescola.dto.instrucao;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

public record InstrucaoRequestDTO(
        @NotNull(message = "Aluno é obrigatório") Long alunoId,

        Long instrutorId,

        @NotNull(message = "Data/hora é obrigatória")
        @Future(message = "Data/hora deve ser futura")
        LocalDateTime dataHora) {
}
