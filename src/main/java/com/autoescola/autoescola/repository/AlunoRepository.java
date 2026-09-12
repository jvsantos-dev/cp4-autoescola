package com.autoescola.autoescola.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.autoescola.autoescola.model.Aluno;

public interface AlunoRepository extends JpaRepository<Aluno, Long> {

    boolean existsByEmail(String email);

    boolean existsByCpf(String cpf);

    Page<Aluno> findByAtivoTrue(Pageable pageable);
}
