package com.autoescola.autoescola.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.autoescola.autoescola.dto.auth.LoginRequestDTO;
import com.autoescola.autoescola.dto.auth.LoginResponseDTO;
import com.autoescola.autoescola.exception.RecursoNaoEncontradoException;
import com.autoescola.autoescola.model.Usuario;
import com.autoescola.autoescola.repository.UsuarioRepository;
import com.autoescola.autoescola.security.JwtService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UsuarioRepository usuarioRepository;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginRequestDTO dto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.email(), dto.senha()));

        Usuario usuario = usuarioRepository.findByEmail(dto.email())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado"));

        String token = jwtService.gerarToken(usuario.getEmail(), usuario.getPerfil().name());
        return ResponseEntity.ok(new LoginResponseDTO(token));
    }
}
