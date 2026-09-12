package com.autoescola.autoescola.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.autoescola.autoescola.dto.instrutor.InstrutorDetalheResponseDTO;
import com.autoescola.autoescola.dto.instrutor.InstrutorRequestDTO;
import com.autoescola.autoescola.dto.instrutor.InstrutorResponseDTO;
import com.autoescola.autoescola.dto.instrutor.InstrutorUpdateRequestDTO;
import com.autoescola.autoescola.service.InstrutorService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/instrutores")
@RequiredArgsConstructor
public class InstrutorController {

    private final InstrutorService instrutorService;

    @PostMapping
    public ResponseEntity<InstrutorDetalheResponseDTO> cadastrarInstrutor(
            @RequestBody @Valid InstrutorRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(instrutorService.cadastrar(dto));
    }

    @GetMapping
    public ResponseEntity<Page<InstrutorResponseDTO>> listarInstrutores(
            @RequestParam(defaultValue = "0") int page) {
        return ResponseEntity.ok(instrutorService.listar(page));
    }

    @GetMapping("/{id}")
    public ResponseEntity<InstrutorDetalheResponseDTO> buscarInstrutor(@PathVariable Long id) {
        return ResponseEntity.ok(instrutorService.buscar(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<InstrutorDetalheResponseDTO> atualizarInstrutor(
            @PathVariable Long id, @RequestBody @Valid InstrutorUpdateRequestDTO dto) {
        return ResponseEntity.ok(instrutorService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirInstrutor(@PathVariable Long id) {
        instrutorService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
