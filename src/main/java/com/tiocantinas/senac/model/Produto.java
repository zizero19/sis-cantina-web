package com.tiocantinas.senac.model;

import java.math.BigDecimal;

import com.tiocantinas.senac.model.enums.CategoriaProduto;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "produtos")
@NoArgsConstructor
@Getter

@Setter
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private BigDecimal preco;
    private int quantidadeEstoque;
    private CategoriaProduto categoria;
}
