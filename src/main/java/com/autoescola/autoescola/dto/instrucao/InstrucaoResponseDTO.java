package com.autoescola.autoescola.dto.instrucao;

import java.time.LocalDateTime;

import com.autoescola.autoescola.model.Instrucao;
import com.autoescola.autoescola.model.MotivoCancelamento;
import com.autoescola.autoescola.model.StatusInstrucao;

public record InstrucaoResponseDTO(
        Long id,
        Long alunoId,
        String alunoNome,
        Long instrutorId,
        String instrutorNome,
        LocalDateTime dataHora,
        StatusInstrucao status,
        MotivoCancelamento motivoCancelamento,
        LocalDateTime canceladaEm) {

    public static InstrucaoResponseDTO from(Instrucao instrucao) {
        return new InstrucaoResponseDTO(
                instrucao.getId(),
                instrucao.getAluno().getId(),
                instrucao.getAluno().getNome(),
                instrucao.getInstrutor().getId(),
                instrucao.getInstrutor().getNome(),
                instrucao.getDataHora(),
                instrucao.getStatus(),
                instrucao.getMotivoCancelamento(),
                instrucao.getCanceladaEm());
    }
}
