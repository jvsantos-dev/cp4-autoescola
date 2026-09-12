package com.autoescola.autoescola.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.autoescola.autoescola.dto.instrucao.CancelamentoRequestDTO;
import com.autoescola.autoescola.dto.instrucao.InstrucaoRequestDTO;
import com.autoescola.autoescola.dto.instrucao.InstrucaoResponseDTO;
import com.autoescola.autoescola.service.InstrucaoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/instrucoes")
@RequiredArgsConstructor
public class InstrucaoController {

    private final InstrucaoService instrucaoService;

    @PostMapping
    public ResponseEntity<InstrucaoResponseDTO> agendarInstrucao(@RequestBody @Valid InstrucaoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(instrucaoService.agendar(dto));
    }

    @GetMapping
    public ResponseEntity<Page<InstrucaoResponseDTO>> listarInstrucoes(
            @RequestParam(defaultValue = "0") int page) {
        return ResponseEntity.ok(instrucaoService.listar(page));
    }

    @GetMapping("/{id}")
    public ResponseEntity<InstrucaoResponseDTO> buscarInstrucao(@PathVariable Long id) {
        return ResponseEntity.ok(instrucaoService.buscar(id));
    }

    // Cancelamento exige um motivo no corpo da requisição, por isso usa PATCH em vez de DELETE puro.
    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<InstrucaoResponseDTO> cancelarInstrucao(
            @PathVariable Long id, @RequestBody @Valid CancelamentoRequestDTO dto) {
        return ResponseEntity.ok(instrucaoService.cancelar(id, dto));
    }
}
