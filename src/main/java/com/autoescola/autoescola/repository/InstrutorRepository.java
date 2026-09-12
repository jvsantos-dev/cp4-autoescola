package com.autoescola.autoescola.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.autoescola.autoescola.model.Instrutor;

public interface InstrutorRepository extends JpaRepository<Instrutor, Long> {

    boolean existsByEmail(String email);

    boolean existsByCnh(String cnh);

    Page<Instrutor> findByAtivoTrue(Pageable pageable);

    List<Instrutor> findByAtivoTrue();
}
