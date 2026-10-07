package com.tiocantinas.senac.dto.cliente;

import java.math.BigDecimal;

import com.tiocantinas.senac.model.Setor;

public record ClienteResponse(
                Long id,
                String nome,
                String cpf,
                String telefone,
                Setor setor,
                boolean ativo,
                BigDecimal limiteCredito) {
}
