package com.tiocantinas.senac.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProdutoNaoEncontradoException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleProdutoNaoEncontradoException(
            ProdutoNaoEncontradoException exception) {

        return exception.getMessage();
    }

    @ExceptionHandler(ClienteNaoEncontradoException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleClienteNaoEncontradoException(
            ClienteNaoEncontradoException exception) {

        return exception.getMessage();
    }

    @ExceptionHandler(SetorNaoEncontradoException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleSetorNaoEncontradoException(
            SetorNaoEncontradoException exception) {

        return exception.getMessage();
    }

    @ExceptionHandler(SetorJaExisteException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleSetorJaExisteException(
            SetorJaExisteException exception) {

        return exception.getMessage();
    }
}
