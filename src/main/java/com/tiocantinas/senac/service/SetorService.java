package com.tiocantinas.senac.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tiocantinas.senac.dto.setor.SetorRequest;
import com.tiocantinas.senac.dto.setor.SetorResponse;
import com.tiocantinas.senac.exception.SetorJaExisteException;
import com.tiocantinas.senac.exception.SetorNaoEncontradoException;
import com.tiocantinas.senac.model.Setor;
import com.tiocantinas.senac.repository.SetorRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SetorService {

    private final SetorRepository setorRepository;

    @Transactional
    public SetorResponse cadastrar(SetorRequest request) {
        validarNomeParaCadastro(request.nome());

        Setor setor = new Setor();
        setor.setNome(request.nome().trim());

        Setor setorSalvo = setorRepository.save(setor);

        return new SetorResponse(setorSalvo.getId(), setorSalvo.getNome(), setorSalvo.getAtivo());
    }

    @Transactional(readOnly = true)
    public SetorResponse buscarPorId(Long id) {
        Setor setor = buscarEntidadePorId(id);
        return new SetorResponse(setor.getId(), setor.getNome(), setor.getAtivo());
    }

    @Transactional(readOnly = true)
    public List<SetorResponse> listarTodos() {
        return setorRepository.findAll()
                .stream()
                .map(setor -> new SetorResponse(setor.getId(), setor.getNome(), setor.getAtivo()))
                .toList();
    }

    @Transactional
    public SetorResponse atualizar(Long id, SetorRequest request) {
        Setor setor = buscarEntidadePorId(id);

        validarNomeParaAtualizacao(id, request.nome());

        setor.setNome(request.nome().trim());

        Setor setorAtualizado = setorRepository.save(setor);

        return new SetorResponse(setorAtualizado.getId(), setorAtualizado.getNome(), setorAtualizado.getAtivo());
    }

    @Transactional
    public SetorResponse excluir(Long id) {
        Setor setor = buscarEntidadePorId(id);

        setorRepository.delete(setor);

        return new SetorResponse(setor.getId(), setor.getNome(), setor.getAtivo());
    }

    @Transactional
    public SetorResponse ativar(Long id) {
        Setor setor = buscarEntidadePorId(id);

        setor.setAtivo(true);

        Setor setorAtivado = setorRepository.save(setor);

        return new SetorResponse(setorAtivado.getId(), setorAtivado.getNome(), setorAtivado.getAtivo());
    }

    @Transactional
    public SetorResponse desativar(Long id) {
        Setor setor = buscarEntidadePorId(id);

        setor.setAtivo(false);

        Setor setorDesativado = setorRepository.save(setor);

        return new SetorResponse(setorDesativado.getId(), setorDesativado.getNome(), setorDesativado.getAtivo());
    }

    private Setor buscarEntidadePorId(Long id) {
        return setorRepository.findById(id)
                .orElseThrow(() -> new SetorNaoEncontradoException(id));
    }

    private void validarNomeParaCadastro(String nome) {
        if (setorRepository.existsByNomeIgnoreCase(nome)) {
            throw new SetorJaExisteException(nome);
        }
    }

    private void validarNomeParaAtualizacao(Long id, String nome) {
        if (setorRepository.existsByNomeIgnoreCaseAndIdNot(nome, id)) {
            throw new SetorJaExisteException(nome);
        }
    }
}
