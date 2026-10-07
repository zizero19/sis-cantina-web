package com.tiocantinas.senac.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.tiocantinas.senac.dto.setor.SetorRequest;
import com.tiocantinas.senac.dto.setor.SetorResponse;
import com.tiocantinas.senac.service.SetorService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/setores")
@RequiredArgsConstructor
public class SetorController {

    private final SetorService setorService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SetorResponse cadastrarSetor(@Valid @RequestBody SetorRequest request) {
        return setorService.cadastrar(request);
    }

    @GetMapping("/{id}")
    public SetorResponse buscarSetorPorId(@PathVariable Long id) {
        return setorService.buscarPorId(id);
    }

    @GetMapping
    public List<SetorResponse> listarSetores() {
        return setorService.listarTodos();
    }

    @PutMapping("/{id}")
    public SetorResponse atualizarSetor(@PathVariable Long id, @Valid @RequestBody SetorRequest request) {
        return setorService.atualizar(id, request);
    }

    @DeleteMapping("/{id}/excluir")
    public SetorResponse excluirSetor(@PathVariable Long id) {
        return setorService.excluir(id);
    }

    @PutMapping("/{id}/ativar")
    public SetorResponse ativarSetor(@PathVariable Long id) {
        return setorService.ativar(id);
    }

    @PutMapping("/{id}/desativar")
    public SetorResponse desativarSetor(@PathVariable Long id) {
        return setorService.desativar(id);
    }

}
