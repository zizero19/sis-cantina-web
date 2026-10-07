package com.tiocantinas.senac.exception;

public class SetorJaExisteException extends RuntimeException {
    public SetorJaExisteException(String nome) {
        super("Setor já existe com este nome: " + nome);
    }

}