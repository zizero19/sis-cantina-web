package com.tiocantinas.senac.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.tiocantinas.senac.dto.produto.ProdutoRequest;
import com.tiocantinas.senac.dto.produto.ProdutoResponse;
import com.tiocantinas.senac.service.ProdutoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.List;

import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/produtos")
@RequiredArgsConstructor
public class ProdutoController {

    private final ProdutoService produtoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProdutoResponse cadastrarProduto(@Valid @RequestBody ProdutoRequest request) {
        return produtoService.cadastrar(request);
    }

    @GetMapping("/{id}")
    public ProdutoResponse buscarProdutoPorId(@PathVariable Long id) {
        return produtoService.buscarPorId(id);
    }

    @GetMapping
    public List<ProdutoResponse> listarProdutos() {
        return produtoService.listarTodos();
    }

    @PutMapping("/{id}")
    public ProdutoResponse atualizarProduto(@PathVariable Long id, @Valid @RequestBody ProdutoRequest request) {
        return produtoService.atualizar(id, request);
    }

    @PutMapping("/{id}/desativar")
    public ProdutoResponse desativarProduto(@PathVariable Long id) {
        return produtoService.desativar(id);
    }

}
