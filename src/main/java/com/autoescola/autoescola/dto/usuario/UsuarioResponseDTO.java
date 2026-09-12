package com.autoescola.autoescola.dto.usuario;

import com.autoescola.autoescola.model.Perfil;
import com.autoescola.autoescola.model.Usuario;

public record UsuarioResponseDTO(
        Long id,
        String nome,
        String email,
        Perfil perfil) {

    public static UsuarioResponseDTO from(Usuario usuario) {
        return new UsuarioResponseDTO(usuario.getId(), usuario.getNome(), usuario.getEmail(), usuario.getPerfil());
    }
}
