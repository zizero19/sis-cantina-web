package com.tiocantinas.senac.dto.produto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.tiocantinas.senac.model.enums.CategoriaProduto;
import com.tiocantinas.senac.model.enums.UnidadeMedida;

public record ProdutoResponse(
                Long id,
                String nome,
                String descricao,
                BigDecimal preco,
                BigDecimal precoCusto,
                BigDecimal estoqueMinimo,
                LocalDate dataValidade,
                Boolean ativo,
                BigDecimal quantidadeEstoque,
                CategoriaProduto categoria,
                UnidadeMedida unidadeMedida

) {
}