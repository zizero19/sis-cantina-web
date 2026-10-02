package com.tiocantinas.senac.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.tiocantinas.senac.model.enums.FormaPagamento;
import com.tiocantinas.senac.model.enums.StatusPedido;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
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

@Entity
@Table(name = "pedido")
@NoArgsConstructor
@Getter
// Para não quebrar o encapsulamento, criar os setters durante o desenvolvimento
// apenas quando necessario.
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemPedido> itens = new ArrayList<>();

    @NotNull
    @Column(nullable = false)
    private LocalDateTime dataHora = LocalDateTime.now();

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status_pedido", nullable = false)
    private StatusPedido statusPedido = StatusPedido.CRIADO;

    @Column(length = 255)
    private String observacoes;

    @NotNull
    @PositiveOrZero
    @Column(name = "preco_total", nullable = false, precision = 10, scale = 2)
    private BigDecimal precoTotal = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(name = "forma_pagamento")
    private FormaPagamento formaPagamento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "caixa_id")
    private Caixa caixa;

    // Construtor para criar o pedido a prazo
    public Pedido(Cliente cliente, String observacoes) {
        this.cliente = cliente;
        this.observacoes = observacoes;
    }

    public void adicionarItem(ItemPedido item) {
        item.setPedido(this);
        itens.add(item);
        recalcularTotal();
    }

    public void recalcularTotal() {
        precoTotal = itens.stream()
                .map(ItemPedido::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void aguardarPagamento() {
        if (statusPedido != StatusPedido.CRIADO)
            throw new IllegalStateException("Somente um pedido criado pode aguardar pagamento.");

        statusPedido = StatusPedido.AGUARDANDO_PAGAMENTO;
    }

    public void finalizarPedido() {
        if (statusPedido != StatusPedido.AGUARDANDO_PAGAMENTO)
            throw new IllegalStateException("Somente um pedido aguardando pagamento pode ser finalizado.");

        statusPedido = StatusPedido.FINALIZADO;
    }

    public void cancelarPedido() {
        if (statusPedido == StatusPedido.FINALIZADO)
            throw new IllegalStateException("Um pedido finalizado não pode ser cancelado.");

        statusPedido = StatusPedido.CANCELADO;
    }

    void definirCaixa(Caixa caixa) {
        this.caixa = caixa;
    }

}
