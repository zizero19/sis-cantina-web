package com.tiocantinas.senac.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.tiocantinas.senac.model.enums.StatusCaixa;
import com.tiocantinas.senac.model.enums.StatusPedido;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "caixa")
@NoArgsConstructor
@Getter
public class Caixa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "caixa")
    private List<Pedido> pedidos = new ArrayList<>();

    @NotBlank
    @Column(nullable = false, length = 100)
    private String responsavelAbertura;

    @NotNull
    @PositiveOrZero
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valorAbertura;

    @PositiveOrZero
    @Column(precision = 10, scale = 2)
    private BigDecimal valorFechamento;

    @NotNull
    @PositiveOrZero
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalDinheiro = BigDecimal.ZERO;

    @NotNull
    @PositiveOrZero
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalCartao = BigDecimal.ZERO;

    @NotNull
    @PositiveOrZero
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPix = BigDecimal.ZERO;

    @NotNull
    @PositiveOrZero
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAPrazo = BigDecimal.ZERO;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusCaixa status = StatusCaixa.ABERTO;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime abertura;

    private LocalDateTime fechamento;

    @Column(length = 255)
    private String observacoes;

    public Caixa(String responsavelAbertura,
            BigDecimal valorAbertura,
            String observacoes) {

        this.responsavelAbertura = responsavelAbertura;
        this.valorAbertura = valorAbertura;
        this.observacoes = observacoes;
        this.abertura = LocalDateTime.now();
    }

    public void registrarPedido(Pedido pedido) {
        if (status == StatusCaixa.FECHADO)
            throw new IllegalStateException("Não é possível registrar um pedido em um caixa fechado.");

        if (pedido.getStatusPedido() != StatusPedido.FINALIZADO)
            throw new IllegalStateException("Somente pedidos finalizados podem ser registrados no caixa.");

        if (pedido.getCaixa() != null)
            throw new IllegalStateException("O pedido já foi registrado neste caixa.");

        pedidos.add(pedido);
        pedido.definirCaixa(this);
    }

    // private void atualizarTotal(Pedido pedido) {
    // switch (pedido.getFormaPagamento()) {
    // case DINHEIRO -> totalDinheiro = totalDinheiro.add(pedido.getPrecoTotal());
    // case DEBITO, CREDITO -> totalCartao =
    // totalCartao.add(pedido.getPrecoTotal());
    // case PIX -> totalPix = totalPix.add(pedido.getPrecoTotal());
    // case A_PRAZO -> totalAPrazo = totalAPrazo.add(pedido.getPrecoTotal());
    // }
    // }

    public void fechar(BigDecimal valorFechamento) {
        if (status == StatusCaixa.FECHADO)
            throw new IllegalStateException("O caixa já está fechado.");

        if (valorFechamento == null || valorFechamento.signum() < 0)
            throw new IllegalArgumentException("O valor de fechamento não pode ser negativo.");

        this.valorFechamento = valorFechamento;
        this.fechamento = LocalDateTime.now();
        this.status = StatusCaixa.FECHADO;
    }

    public BigDecimal calcularValorEsperado() {
        return valorAbertura.add(totalDinheiro);
    }

    public BigDecimal calcularDiferenca() {
        if (valorFechamento == null)
            throw new IllegalStateException("O caixa ainda não foi fechado.");

        return valorFechamento.subtract(calcularValorEsperado());
    }
}
