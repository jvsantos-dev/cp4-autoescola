package com.autoescola.autoescola.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.autoescola.autoescola.dto.common.EnderecoDTO;
import com.autoescola.autoescola.dto.instrutor.InstrutorDetalheResponseDTO;
import com.autoescola.autoescola.dto.instrutor.InstrutorRequestDTO;
import com.autoescola.autoescola.dto.instrutor.InstrutorResponseDTO;
import com.autoescola.autoescola.dto.instrutor.InstrutorUpdateRequestDTO;
import com.autoescola.autoescola.exception.RecursoNaoEncontradoException;
import com.autoescola.autoescola.exception.RegraDeNegocioException;
import com.autoescola.autoescola.model.Endereco;
import com.autoescola.autoescola.model.Instrutor;
import com.autoescola.autoescola.repository.InstrutorRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class InstrutorService {

    private static final int TAMANHO_PAGINA = 10;

    private final InstrutorRepository instrutorRepository;

    public InstrutorDetalheResponseDTO cadastrar(InstrutorRequestDTO dto) {
        if (instrutorRepository.existsByEmail(dto.email())) {
            throw new RegraDeNegocioException("Já existe um instrutor cadastrado com esse e-mail");
        }
        if (instrutorRepository.existsByCnh(dto.cnh())) {
            throw new RegraDeNegocioException("Já existe um instrutor cadastrado com essa CNH");
        }

        Instrutor instrutor = Instrutor.builder()
                .nome(dto.nome())
                .email(dto.email())
                .telefone(dto.telefone())
                .cnh(dto.cnh())
                .especialidade(dto.especialidade())
                .endereco(toEndereco(dto.endereco()))
                .ativo(true)
                .telefoneVisivel(false)
                .build();

        return InstrutorDetalheResponseDTO.from(instrutorRepository.save(instrutor));
    }

    @Transactional(readOnly = true)
    public Page<InstrutorResponseDTO> listar(int pagina) {
        Pageable pageable = PageRequest.of(pagina, TAMANHO_PAGINA, Sort.by("nome").ascending());
        return instrutorRepository.findByAtivoTrue(pageable).map(InstrutorResponseDTO::from);
    }

    @Transactional(readOnly = true)
    public InstrutorDetalheResponseDTO buscar(Long id) {
        return InstrutorDetalheResponseDTO.from(buscarEntidade(id));
    }

    public InstrutorDetalheResponseDTO atualizar(Long id, InstrutorUpdateRequestDTO dto) {
        Instrutor instrutor = buscarEntidade(id);
        instrutor.setNome(dto.nome());
        instrutor.setTelefone(dto.telefone());
        instrutor.setTelefoneVisivel(true);
        instrutor.setEndereco(toEndereco(dto.endereco()));
        return InstrutorDetalheResponseDTO.from(instrutorRepository.save(instrutor));
    }

    public void excluir(Long id) {
        Instrutor instrutor = buscarEntidade(id);
        if (!instrutor.isAtivo()) {
            throw new RegraDeNegocioException("Instrutor já está inativo");
        }
        instrutor.setAtivo(false);
        instrutorRepository.save(instrutor);
    }

    private Instrutor buscarEntidade(Long id) {
        return instrutorRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Instrutor não encontrado"));
    }

    private Endereco toEndereco(EnderecoDTO dto) {
        return new Endereco(dto.logradouro(), dto.numero(), dto.complemento(), dto.bairro(), dto.cidade(), dto.uf(), dto.cep());
    }
}
