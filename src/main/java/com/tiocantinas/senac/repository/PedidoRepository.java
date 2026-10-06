package com.tiocantinas.senac.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tiocantinas.senac.model.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

}
