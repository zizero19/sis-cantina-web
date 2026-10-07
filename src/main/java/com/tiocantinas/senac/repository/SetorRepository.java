package com.tiocantinas.senac.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tiocantinas.senac.model.Setor;

public interface SetorRepository extends JpaRepository<Setor, Long> {

    boolean existsByNomeIgnoreCase(String nome);

    boolean existsByNomeIgnoreCaseAndIdNot(String nome, Long id);
}
