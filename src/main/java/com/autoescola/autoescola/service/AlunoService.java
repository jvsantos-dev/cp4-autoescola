package com.autoescola.autoescola.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.autoescola.autoescola.dto.aluno.AlunoDetalheResponseDTO;
import com.autoescola.autoescola.dto.aluno.AlunoRequestDTO;
import com.autoescola.autoescola.dto.aluno.AlunoResponseDTO;
import com.autoescola.autoescola.dto.aluno.AlunoUpdateRequestDTO;
import com.autoescola.autoescola.dto.common.EnderecoDTO;
import com.autoescola.autoescola.exception.RecursoNaoEncontradoException;
import com.autoescola.autoescola.exception.RegraDeNegocioException;
import com.autoescola.autoescola.model.Aluno;
import com.autoescola.autoescola.model.Endereco;
import com.autoescola.autoescola.repository.AlunoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AlunoService {

    private static final int TAMANHO_PAGINA = 10;

    private final AlunoRepository alunoRepository;

    public AlunoDetalheResponseDTO cadastrar(AlunoRequestDTO dto) {
        if (alunoRepository.existsByEmail(dto.email())) {
            throw new RegraDeNegocioException("Já existe um aluno cadastrado com esse e-mail");
        }
        if (alunoRepository.existsByCpf(dto.cpf())) {
            throw new RegraDeNegocioException("Já existe um aluno cadastrado com esse CPF");
        }

        Aluno aluno = Aluno.builder()
                .nome(dto.nome())
                .email(dto.email())
                .telefone(dto.telefone())
                .cpf(dto.cpf())
                .endereco(toEndereco(dto.endereco()))
                .ativo(true)
                .build();

        return AlunoDetalheResponseDTO.from(alunoRepository.save(aluno));
    }

    @Transactional(readOnly = true)
    public Page<AlunoResponseDTO> listar(int pagina) {
        Pageable pageable = PageRequest.of(pagina, TAMANHO_PAGINA, Sort.by("nome").ascending());
        return alunoRepository.findByAtivoTrue(pageable).map(AlunoResponseDTO::from);
    }

    @Transactional(readOnly = true)
    public AlunoDetalheResponseDTO buscar(Long id) {
        return AlunoDetalheResponseDTO.from(buscarEntidade(id));
    }

    public AlunoDetalheResponseDTO atualizar(Long id, AlunoUpdateRequestDTO dto) {
        Aluno aluno = buscarEntidade(id);
        aluno.setNome(dto.nome());
        aluno.setTelefone(dto.telefone());
        aluno.setEndereco(toEndereco(dto.endereco()));
        return AlunoDetalheResponseDTO.from(alunoRepository.save(aluno));
    }

    public void excluir(Long id) {
        Aluno aluno = buscarEntidade(id);
        if (!aluno.isAtivo()) {
            throw new RegraDeNegocioException("Aluno já está inativo");
        }
        aluno.setAtivo(false);
        alunoRepository.save(aluno);
    }

    private Aluno buscarEntidade(Long id) {
        return alunoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Aluno não encontrado"));
    }

    private Endereco toEndereco(EnderecoDTO dto) {
        return new Endereco(dto.logradouro(), dto.numero(), dto.complemento(), dto.bairro(), dto.cidade(), dto.uf(), dto.cep());
    }
}
