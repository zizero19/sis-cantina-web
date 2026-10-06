package com.tiocantinas.senac.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tiocantinas.senac.model.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

}
