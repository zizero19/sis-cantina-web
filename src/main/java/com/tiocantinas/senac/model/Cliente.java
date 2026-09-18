package com.tiocantinas.senac.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity 
@Table(name = "cliente")
@NoArgsConstructor
@Getter
@Setter
public class Cliente extends Pessoa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull 
    @ManyToOne
    @JoinColumn(name = "setor_id", nullable = false)
    private Setor setor; 

    @NotNull 
    @Column(nullable = false)
    private Boolean ativo = true;

    @NotNull
    @PositiveOrZero 
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal limiteCredito = BigDecimal.ZERO;

    @OneToMany (mappedBy = "cliente")
    private List<LancamentoConta> lancamentos = new ArrayList<>();
}
