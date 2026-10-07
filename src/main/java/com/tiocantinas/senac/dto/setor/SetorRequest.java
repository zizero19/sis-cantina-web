package com.tiocantinas.senac.dto.setor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SetorRequest(

        @NotBlank(message = "O nome do setor é obrigatório") @Size(max = 100, message = "O nome deve possuir no máximo 100 caracteres") String nome) {
}
