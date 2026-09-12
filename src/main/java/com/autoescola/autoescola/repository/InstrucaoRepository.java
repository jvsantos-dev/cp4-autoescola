package com.autoescola.autoescola.repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;

import com.autoescola.autoescola.model.Instrucao;
import com.autoescola.autoescola.model.StatusInstrucao;

public interface InstrucaoRepository extends JpaRepository<Instrucao, Long> {

    boolean existsByInstrutorIdAndDataHoraAndStatus(Long instrutorId, LocalDateTime dataHora, StatusInstrucao status);

    long countByAlunoIdAndStatusAndDataHoraBetween(
            Long alunoId, StatusInstrucao status, LocalDateTime inicio, LocalDateTime fim);
}
