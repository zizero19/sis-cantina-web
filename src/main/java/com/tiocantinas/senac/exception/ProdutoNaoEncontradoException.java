package com.tiocantinas.senac.exception;

public class ProdutoNaoEncontradoException extends RuntimeException {
    public ProdutoNaoEncontradoException(Long id) {
        super("Produto não encontrado: " + id + " não foi encontrado.");
    }
}
