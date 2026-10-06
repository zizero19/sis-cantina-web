package com.tiocantinas.senac.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tiocantinas.senac.dto.produto.ProdutoRequest;
import com.tiocantinas.senac.dto.produto.ProdutoResponse;
import com.tiocantinas.senac.exception.ProdutoNaoEncontradoException;
import com.tiocantinas.senac.model.Produto;
import com.tiocantinas.senac.repository.ProdutoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    @Transactional
    public ProdutoResponse cadastrar(ProdutoRequest request) {
        Produto produto = new Produto();

        atualizarDados(produto, request);

        Produto produtoSalvo = produtoRepository.save(produto);

        return toResponse(produtoSalvo);
    }

    @Transactional(readOnly = true)
    public ProdutoResponse buscarPorId(Long id) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new ProdutoNaoEncontradoException(id));
        return toResponse(produto);
    }

    @Transactional(readOnly = true)
    public List<ProdutoResponse> listarTodos() {
        return produtoRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public ProdutoResponse atualizar(Long id, ProdutoRequest request) {
        Produto produto = buscarEntidadePorId(id);

        atualizarDados(produto, request);

        Produto produtoAtualizado = produtoRepository.save(produto);

        return toResponse(produtoAtualizado);
    }

    @Transactional
    public ProdutoResponse desativar(Long id) {
        Produto produto = buscarEntidadePorId(id);

        produto.setAtivo(false);

        Produto produtoDesativado = produtoRepository.save(produto);

        return toResponse(produtoDesativado);
    }

    private void atualizarDados(Produto produto, ProdutoRequest request) {
        produto.setNome(request.nome());
        produto.setDescricao(request.descricao());
        produto.setPreco(request.preco());
        produto.setPrecoCusto(request.precoCusto());
        produto.setEstoqueMinimo(request.estoqueMinimo());
        produto.setDataValidade(request.dataValidade());
        produto.setQuantidadeEstoque(request.quantidadeEstoque());
        produto.setCategoria(request.categoria());
        produto.setUnidadeMedida(request.unidadeMedida());
    }

    private ProdutoResponse toResponse(Produto produto) {
        return new ProdutoResponse(
                produto.getId(),
                produto.getNome(),
                produto.getDescricao(),
                produto.getPreco(),
                produto.getPrecoCusto(),
                produto.getEstoqueMinimo(),
                produto.getDataValidade(),
                produto.getAtivo(),
                produto.getQuantidadeEstoque(),
                produto.getCategoria(),
                produto.getUnidadeMedida());
    }

    private Produto buscarEntidadePorId(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado."));
    }
}