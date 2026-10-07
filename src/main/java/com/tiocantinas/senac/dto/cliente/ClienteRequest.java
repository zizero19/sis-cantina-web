package com.tiocantinas.senac.dto.cliente;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record ClienteRequest(

        @NotBlank(message = "O nome do cliente é obrigatório.") @Size(min = 2, max = 100, message = "O nome deve possuir entre 2 e 100 caracteres.") String nome,

        @NotBlank(message = "O CPF do cliente é obrigatório.") @Size(min = 11, max = 11, message = "O CPF deve possuir 11 caracteres.") String cpf,

        @NotBlank(message = "O telefone do cliente é obrigatório.") @Size(min = 10, max = 11, message = "O telefone deve possuir entre 10 e 11 caracteres.") String telefone,

        @NotNull(message = "O setor do cliente é obrigatório.") Long setorId,

        @NotNull(message = "O limite de crédito do cliente é obrigatório.") @PositiveOrZero(message = "O limite de crédito não pode ser negativo.") BigDecimal limiteCredito) {
}