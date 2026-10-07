package com.tiocantinas.senac.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tiocantinas.senac.dto.cliente.ClienteRequest;
import com.tiocantinas.senac.dto.cliente.ClienteResponse;
import com.tiocantinas.senac.exception.ClienteNaoEncontradoException;
import com.tiocantinas.senac.model.Cliente;
import com.tiocantinas.senac.repository.ClienteRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;

    @Transactional
    public ClienteResponse cadastrar(ClienteRequest request) {
        Cliente cliente = new Cliente();

        atualizarDados(cliente, request);

        Cliente clienteSalvo = clienteRepository.save(cliente);

        return toResponse(clienteSalvo);
    }

    @Transactional(readOnly = true)
    public ClienteResponse buscarPorId(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ClienteNaoEncontradoException(id));
        return toResponse(cliente);
    }

    @Transactional(readOnly = true)
    public List<ClienteResponse> listarTodos() {
        return clienteRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public ClienteResponse atualizar(Long id, ClienteRequest request) {
        Cliente cliente = buscarEntidadePorId(id);

        atualizarDados(cliente, request);

        Cliente clienteAtualizado = clienteRepository.save(cliente);

        return toResponse(clienteAtualizado);
    }

    @Transactional
    public ClienteResponse desativar(Long id) {
        Cliente cliente = buscarEntidadePorId(id);

        cliente.setAtivo(false);

        Cliente clienteDesativado = clienteRepository.save(cliente);

        return toResponse(clienteDesativado);
    }

    private void atualizarDados(Cliente cliente, ClienteRequest request) {
        cliente.setNome(request.nome());
        cliente.setCpf(request.cpf());
        cliente.setTelefone(request.telefone());
        cliente.setSetor(request.setor());
        cliente.setAtivo(request.ativo());
        cliente.setLimiteCredito(request.limiteCredito());
    }

    private ClienteResponse toResponse(Cliente cliente) {
        return new ClienteResponse(
                cliente.getId(),
                cliente.getNome(),
                cliente.getCpf(),
                cliente.getTelefone(),
                cliente.getSetor(),
                cliente.getAtivo(),
                cliente.getLimiteCredito());
    }

    private Cliente buscarEntidadePorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ClienteNaoEncontradoException(id));
    }
}
