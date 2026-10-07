package com.tiocantinas.senac.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.tiocantinas.senac.dto.cliente.ClienteRequest;
import com.tiocantinas.senac.dto.cliente.ClienteResponse;
import com.tiocantinas.senac.service.ClienteService;

import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClienteResponse cadastrarCliente(@Valid @RequestBody ClienteRequest request) {
        return clienteService.cadastrar(request);
    }

    @GetMapping("/{id}")
    public ClienteResponse buscarClientePorId(@PathVariable Long id) {
        return clienteService.buscarPorId(id);
    }

    @GetMapping
    public List<ClienteResponse> listarClientes() {
        return clienteService.listarTodos();
    }

    @PutMapping("/{id}")
    public ClienteResponse atualizarCliente(@PathVariable Long id, @Valid @RequestBody ClienteRequest request) {
        return clienteService.atualizar(id, request);
    }

    @PutMapping("/{id}/desativar")
    public ClienteResponse desativarCliente(@PathVariable Long id) {
        return clienteService.desativar(id);
    }

}
