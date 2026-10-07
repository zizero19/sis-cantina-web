package com.tiocantinas.senac.exception;

public class SetorNaoEncontradoException extends RuntimeException {
    public SetorNaoEncontradoException(Long id) {
        super("Setor não encontrado: " + id + " não foi encontrado.");
    }
}
