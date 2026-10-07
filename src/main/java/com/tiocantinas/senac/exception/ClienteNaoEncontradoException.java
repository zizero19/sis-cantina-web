package com.tiocantinas.senac.exception;

public class ClienteNaoEncontradoException extends RuntimeException {
    public ClienteNaoEncontradoException(Long id) {
        super("Cliente não encontrado: " + id + " não foi encontrado.");
    }
}