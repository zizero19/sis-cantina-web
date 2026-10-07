package com.tiocantinas.senac.dto.cliente;

import java.math.BigDecimal;

import com.tiocantinas.senac.model.Setor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ClienteRequest(
                @NotBlank(message = "O nome do cliente é obrigatório.") @Size(max = 100, min = 2) String nome,
                @NotBlank(message = "O CPF do cliente é obrigatório.") @Size(max = 14, min = 11) String cpf,
                @Size(max = 20) String telefone,
                @NotBlank(message = "O setor do cliente é obrigatório.") Setor setor,
                @NotBlank(message = "O status do cliente é obrigatório.") boolean ativo,
                @NotBlank(message = "O limite de crédito do cliente é obrigatório.") @Size(max = 1000000) BigDecimal limiteCredito) {
}
