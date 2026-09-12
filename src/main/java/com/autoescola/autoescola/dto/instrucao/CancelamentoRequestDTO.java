package com.autoescola.autoescola.dto.instrucao;

import com.autoescola.autoescola.model.MotivoCancelamento;

import jakarta.validation.constraints.NotNull;

public record CancelamentoRequestDTO(
        @NotNull(message = "Motivo do cancelamento é obrigatório") MotivoCancelamento motivo) {
}
