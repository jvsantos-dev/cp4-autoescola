package com.autoescola.autoescola.service;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Random;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.autoescola.autoescola.dto.instrucao.CancelamentoRequestDTO;
import com.autoescola.autoescola.dto.instrucao.InstrucaoRequestDTO;
import com.autoescola.autoescola.dto.instrucao.InstrucaoResponseDTO;
import com.autoescola.autoescola.exception.RecursoNaoEncontradoException;
import com.autoescola.autoescola.exception.RegraDeNegocioException;
import com.autoescola.autoescola.model.Aluno;
import com.autoescola.autoescola.model.Instrucao;
import com.autoescola.autoescola.model.Instrutor;
import com.autoescola.autoescola.model.StatusInstrucao;
import com.autoescola.autoescola.repository.AlunoRepository;
import com.autoescola.autoescola.repository.InstrucaoRepository;
import com.autoescola.autoescola.repository.InstrutorRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class InstrucaoService {

    private static final int TAMANHO_PAGINA = 10;
    private static final LocalTime ABERTURA = LocalTime.of(6, 0);
    private static final LocalTime FECHAMENTO = LocalTime.of(21, 0);
    private static final Duration DURACAO_INSTRUCAO = Duration.ofHours(1);
    private static final int LIMITE_INSTRUCOES_POR_DIA_ALUNO = 2;

    private final InstrucaoRepository instrucaoRepository;
    private final AlunoRepository alunoRepository;
    private final InstrutorRepository instrutorRepository;
    private final Random random = new Random();

    public InstrucaoResponseDTO agendar(InstrucaoRequestDTO dto) {
        LocalDateTime dataHora = dto.dataHora();
        validarAntecedenciaMinima(dataHora);
        validarHorarioFuncionamento(dataHora);

        Aluno aluno = alunoRepository.findById(dto.alunoId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Aluno não encontrado"));
        if (!aluno.isAtivo()) {
            throw new RegraDeNegocioException("Não é possível agendar instrução para um aluno inativo");
        }
        validarLimiteDiarioDoAluno(aluno.getId(), dataHora);

        Instrutor instrutor = dto.instrutorId() != null
                ? buscarInstrutorDisponivel(dto.instrutorId(), dataHora)
                : selecionarInstrutorAleatorioDisponivel(dataHora);

        Instrucao instrucao = Instrucao.builder()
                .aluno(aluno)
                .instrutor(instrutor)
                .dataHora(dataHora)
                .status(StatusInstrucao.AGENDADA)
                .build();

        return InstrucaoResponseDTO.from(instrucaoRepository.save(instrucao));
    }

    public InstrucaoResponseDTO cancelar(Long id, CancelamentoRequestDTO dto) {
        Instrucao instrucao = instrucaoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Instrução não encontrada"));

        if (instrucao.getStatus() == StatusInstrucao.CANCELADA) {
            throw new RegraDeNegocioException("Instrução já está cancelada");
        }
        if (instrucao.getDataHora().isBefore(LocalDateTime.now().plusHours(24))) {
            throw new RegraDeNegocioException("O cancelamento deve ser feito com antecedência mínima de 24 horas");
        }

        instrucao.setStatus(StatusInstrucao.CANCELADA);
        instrucao.setMotivoCancelamento(dto.motivo());
        instrucao.setCanceladaEm(LocalDateTime.now());

        return InstrucaoResponseDTO.from(instrucaoRepository.save(instrucao));
    }

    @Transactional(readOnly = true)
    public Page<InstrucaoResponseDTO> listar(int pagina) {
        Pageable pageable = PageRequest.of(pagina, TAMANHO_PAGINA, Sort.by("dataHora").ascending());
        return instrucaoRepository.findAll(pageable).map(InstrucaoResponseDTO::from);
    }

    @Transactional(readOnly = true)
    public InstrucaoResponseDTO buscar(Long id) {
        Instrucao instrucao = instrucaoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Instrução não encontrada"));
        return InstrucaoResponseDTO.from(instrucao);
    }

    private void validarAntecedenciaMinima(LocalDateTime dataHora) {
        if (dataHora.isBefore(LocalDateTime.now().plusMinutes(30))) {
            throw new RegraDeNegocioException("Instruções devem ser agendadas com antecedência mínima de 30 minutos");
        }
    }

    private void validarHorarioFuncionamento(LocalDateTime dataHora) {
        if (dataHora.getDayOfWeek() == DayOfWeek.SUNDAY) {
            throw new RegraDeNegocioException("A auto-escola não funciona aos domingos");
        }
        LocalTime hora = dataHora.toLocalTime();
        LocalTime ultimoHorarioPossivel = FECHAMENTO.minus(DURACAO_INSTRUCAO);
        if (hora.isBefore(ABERTURA) || hora.isAfter(ultimoHorarioPossivel)) {
            throw new RegraDeNegocioException(
                    "Instruções só podem ser agendadas entre 06:00 e 20:00, de segunda a sábado");
        }
    }

    private void validarLimiteDiarioDoAluno(Long alunoId, LocalDateTime dataHora) {
        LocalDateTime inicioDia = dataHora.toLocalDate().atStartOfDay();
        LocalDateTime fimDia = inicioDia.plusDays(1);
        long totalNoDia = instrucaoRepository.countByAlunoIdAndStatusAndDataHoraBetween(
                alunoId, StatusInstrucao.AGENDADA, inicioDia, fimDia);
        if (totalNoDia >= LIMITE_INSTRUCOES_POR_DIA_ALUNO) {
            throw new RegraDeNegocioException("Aluno já possui duas instruções agendadas nesse dia");
        }
    }

    private Instrutor buscarInstrutorDisponivel(Long instrutorId, LocalDateTime dataHora) {
        Instrutor instrutor = instrutorRepository.findById(instrutorId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Instrutor não encontrado"));
        if (!instrutor.isAtivo()) {
            throw new RegraDeNegocioException("Não é possível agendar instrução com um instrutor inativo");
        }
        if (instrucaoRepository.existsByInstrutorIdAndDataHoraAndStatus(
                instrutor.getId(), dataHora, StatusInstrucao.AGENDADA)) {
            throw new RegraDeNegocioException("Instrutor já possui outra instrução agendada nesse horário");
        }
        return instrutor;
    }

    private Instrutor selecionarInstrutorAleatorioDisponivel(LocalDateTime dataHora) {
        List<Instrutor> disponiveis = instrutorRepository.findByAtivoTrue().stream()
                .filter(instrutor -> !instrucaoRepository.existsByInstrutorIdAndDataHoraAndStatus(
                        instrutor.getId(), dataHora, StatusInstrucao.AGENDADA))
                .toList();

        if (disponiveis.isEmpty()) {
            throw new RegraDeNegocioException("Nenhum instrutor disponível para o horário solicitado");
        }
        return disponiveis.get(random.nextInt(disponiveis.size()));
    }
}
