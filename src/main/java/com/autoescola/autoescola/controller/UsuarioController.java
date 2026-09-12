package com.autoescola.autoescola.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.autoescola.autoescola.dto.usuario.AlterarSenhaRequestDTO;
import com.autoescola.autoescola.dto.usuario.UsuarioRequestDTO;
import com.autoescola.autoescola.dto.usuario.UsuarioResponseDTO;
import com.autoescola.autoescola.dto.usuario.UsuarioUpdateRequestDTO;
import com.autoescola.autoescola.service.UsuarioService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioResponseDTO> cadastrarUsuario(@RequestBody @Valid UsuarioRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.cadastrar(dto));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<UsuarioResponseDTO>> listarUsuarios(@RequestParam(defaultValue = "0") int page) {
        return ResponseEntity.ok(usuarioService.listar(page));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioResponseDTO> atualizarUsuario(
            @PathVariable Long id, @RequestBody @Valid UsuarioUpdateRequestDTO dto) {
        return ResponseEntity.ok(usuarioService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> excluirUsuario(@PathVariable Long id) {
        usuarioService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    // Qualquer usuário autenticado pode alterar a própria senha.
    @PutMapping("/senha")
    public ResponseEntity<Void> alterarSenha(
            @RequestBody @Valid AlterarSenhaRequestDTO dto, Authentication authentication) {
        usuarioService.alterarSenha(authentication.getName(), dto);
        return ResponseEntity.noContent().build();
    }
}
