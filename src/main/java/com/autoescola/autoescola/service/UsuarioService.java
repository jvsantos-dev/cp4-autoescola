package com.autoescola.autoescola.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.autoescola.autoescola.dto.usuario.AlterarSenhaRequestDTO;
import com.autoescola.autoescola.dto.usuario.UsuarioRequestDTO;
import com.autoescola.autoescola.dto.usuario.UsuarioResponseDTO;
import com.autoescola.autoescola.dto.usuario.UsuarioUpdateRequestDTO;
import com.autoescola.autoescola.exception.RecursoNaoEncontradoException;
import com.autoescola.autoescola.exception.RegraDeNegocioException;
import com.autoescola.autoescola.model.Usuario;
import com.autoescola.autoescola.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class UsuarioService {

    private static final int TAMANHO_PAGINA = 10;

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioResponseDTO cadastrar(UsuarioRequestDTO dto) {
        if (usuarioRepository.existsByEmail(dto.email())) {
            throw new RegraDeNegocioException("Já existe um usuário cadastrado com esse e-mail");
        }

        Usuario usuario = Usuario.builder()
                .nome(dto.nome())
                .email(dto.email())
                .senha(passwordEncoder.encode(dto.senha()))
                .perfil(dto.perfil())
                .build();

        return UsuarioResponseDTO.from(usuarioRepository.save(usuario));
    }

    @Transactional(readOnly = true)
    public Page<UsuarioResponseDTO> listar(int pagina) {
        Pageable pageable = PageRequest.of(pagina, TAMANHO_PAGINA, Sort.by("nome").ascending());
        return usuarioRepository.findAll(pageable).map(UsuarioResponseDTO::from);
    }

    public UsuarioResponseDTO atualizar(Long id, UsuarioUpdateRequestDTO dto) {
        Usuario usuario = buscarEntidade(id);
        usuario.setNome(dto.nome());
        usuario.setPerfil(dto.perfil());
        return UsuarioResponseDTO.from(usuarioRepository.save(usuario));
    }

    public void excluir(Long id) {
        Usuario usuario = buscarEntidade(id);
        usuarioRepository.delete(usuario);
    }

    public void alterarSenha(String emailUsuarioLogado, AlterarSenhaRequestDTO dto) {
        Usuario usuario = usuarioRepository.findByEmail(emailUsuarioLogado)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado"));

        if (!passwordEncoder.matches(dto.senhaAtual(), usuario.getSenha())) {
            throw new RegraDeNegocioException("Senha atual incorreta");
        }

        usuario.setSenha(passwordEncoder.encode(dto.novaSenha()));
        usuarioRepository.save(usuario);
    }

    private Usuario buscarEntidade(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado"));
    }
}
