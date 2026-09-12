package com.autoescola.autoescola.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.autoescola.autoescola.dto.aluno.AlunoDetalheResponseDTO;
import com.autoescola.autoescola.dto.aluno.AlunoRequestDTO;
import com.autoescola.autoescola.dto.aluno.AlunoResponseDTO;
import com.autoescola.autoescola.dto.aluno.AlunoUpdateRequestDTO;
import com.autoescola.autoescola.service.AlunoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/alunos")
@RequiredArgsConstructor
public class AlunoController {

    private final AlunoService alunoService;

    @PostMapping
    public ResponseEntity<AlunoDetalheResponseDTO> cadastrarAluno(@RequestBody @Valid AlunoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(alunoService.cadastrar(dto));
    }

    @GetMapping
    public ResponseEntity<Page<AlunoResponseDTO>> listarAlunos(@RequestParam(defaultValue = "0") int page) {
        return ResponseEntity.ok(alunoService.listar(page));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlunoDetalheResponseDTO> buscarAluno(@PathVariable Long id) {
        return ResponseEntity.ok(alunoService.buscar(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AlunoDetalheResponseDTO> atualizarAluno(
            @PathVariable Long id, @RequestBody @Valid AlunoUpdateRequestDTO dto) {
        return ResponseEntity.ok(alunoService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirAluno(@PathVariable Long id) {
        alunoService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
