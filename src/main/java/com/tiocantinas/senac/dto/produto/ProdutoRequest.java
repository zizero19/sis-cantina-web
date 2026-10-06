package com.tiocantinas.senac.dto.produto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.tiocantinas.senac.model.enums.CategoriaProduto;
import com.tiocantinas.senac.model.enums.UnidadeMedida;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record ProdutoRequest(
        @NotBlank(message = "O nome do produto é obrigatório.") @Size(max = 100, message = "O nome deve possuir no máximo 100 caracteres.") String nome,

        @Size(max = 255, message = "A descrição deve possuir no máximo 255 caracteres.") String descricao,

        @NotNull(message = "O preço é obrigatório.") @PositiveOrZero(message = "O preço não pode ser negativo.") BigDecimal preco,

        @NotNull(message = "O preço de custo é obrigatório.") @PositiveOrZero(message = "O preço de custo não pode ser negativo.") BigDecimal precoCusto,

        @NotNull(message = "O estoque mínimo é obrigatório.") @PositiveOrZero(message = "O estoque mínimo não pode ser negativo.") BigDecimal estoqueMinimo,

        LocalDate dataValidade,

        @NotNull(message = "A quantidade em estoque é obrigatória.") @PositiveOrZero(message = "A quantidade em estoque não pode ser negativa.") BigDecimal quantidadeEstoque,

        @NotNull(message = "A categoria é obrigatória.") CategoriaProduto categoria,

        @NotNull(message = "A unidade de medida é obrigatória.") UnidadeMedida unidadeMedida

) {
}