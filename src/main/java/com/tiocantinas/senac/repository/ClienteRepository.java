package com.tiocantinas.senac.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tiocantinas.senac.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

}
