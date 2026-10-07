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
        String nome = request.nome().trim();

        validarNomeParaCadastro(nome);

        Setor setor = new Setor();
        setor.setNome(nome);

        Setor setorSalvo = setorRepository.save(setor);

        return toResponse(setorSalvo);
    }

    @Transactional(readOnly = true)
    public SetorResponse buscarPorId(Long id) {
        Setor setor = buscarEntidadePorId(id);
        return toResponse(setor);
    }

    @Transactional(readOnly = true)
    public List<SetorResponse> listarTodos() {
        return setorRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public SetorResponse atualizar(Long id, SetorRequest request) {
        Setor setor = buscarEntidadePorId(id);

        String nome = request.nome().trim();

        validarNomeParaAtualizacao(id, nome);

        setor.setNome(nome);

        Setor setorAtualizado = setorRepository.save(setor);

        return toResponse(setorAtualizado);
    }

    @Transactional
    public SetorResponse ativar(Long id) {
        Setor setor = buscarEntidadePorId(id);

        setor.setAtivo(true);

        Setor setorAtivado = setorRepository.save(setor);

        return toResponse(setorAtivado);
    }

    @Transactional
    public SetorResponse desativar(Long id) {
        Setor setor = buscarEntidadePorId(id);

        setor.setAtivo(false);

        Setor setorDesativado = setorRepository.save(setor);

        return toResponse(setorDesativado);
    }

    private Setor buscarEntidadePorId(Long id) {
        return setorRepository.findById(id)
                .orElseThrow(() -> new SetorNaoEncontradoException(id));
    }

    private SetorResponse toResponse(Setor setor) {
        return new SetorResponse(
                setor.getId(),
                setor.getNome(),
                setor.getAtivo());
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
