package com.tiocantinas.senac.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.tiocantinas.senac.dto.produto.ProdutoRequest;
import com.tiocantinas.senac.dto.produto.ProdutoResponse;
import com.tiocantinas.senac.exception.ProdutoNaoEncontradoException;
import com.tiocantinas.senac.model.Produto;
import com.tiocantinas.senac.model.enums.CategoriaProduto;
import com.tiocantinas.senac.model.enums.UnidadeMedida;
import com.tiocantinas.senac.repository.ProdutoRepository;

@ExtendWith(MockitoExtension.class)
public class ProdutoServiceTest {

    @Mock
    private ProdutoRepository produtoRepository;

    @InjectMocks
    private ProdutoService produtoService;

    @Test
    void deveCadastrarProduto() {
        // Arrange
        ProdutoRequest request = new ProdutoRequest(
                "Coca-Cola 350ml",
                "Refrigerante em lata",
                new BigDecimal("6.00"),
                new BigDecimal("3.50"),
                new BigDecimal("10"),
                LocalDate.of(2027, 3, 20),
                new BigDecimal("50"),
                CategoriaProduto.BEBIDA_FRIA,
                UnidadeMedida.UNIDADE);

        Produto produtoSalvo = new Produto();
        produtoSalvo.setId(1L);
        produtoSalvo.setNome(request.nome());
        produtoSalvo.setDescricao(request.descricao());
        produtoSalvo.setPreco(request.preco());
        produtoSalvo.setPrecoCusto(request.precoCusto());
        produtoSalvo.setEstoqueMinimo(request.estoqueMinimo());
        produtoSalvo.setDataValidade(request.dataValidade());
        produtoSalvo.setQuantidadeEstoque(request.quantidadeEstoque());
        produtoSalvo.setCategoria(request.categoria());
        produtoSalvo.setUnidadeMedida(request.unidadeMedida());
        produtoSalvo.setAtivo(true);

        when(produtoRepository.save(any(Produto.class))).thenReturn(produtoSalvo);

        // Act
        ProdutoResponse response = produtoService.cadastrar(request);

        // Assert
        assertEquals(1L, response.id());
        assertEquals("Coca-Cola 350ml", response.nome());
        assertEquals(new BigDecimal("6.00"), response.preco());
        assertTrue(response.ativo());

        verify(produtoRepository).save(any(Produto.class));
    }

    @Test
    void deveBuscarProdutoPorId() {
        // Arrange
        Produto produto = new Produto();
        produto.setId(1L);
        produto.setNome("Coca-Cola 350ml");
        produto.setDescricao("Refrigerante em lata");
        produto.setPreco(new BigDecimal("6.00"));
        produto.setPrecoCusto(new BigDecimal("3.50"));
        produto.setEstoqueMinimo(new BigDecimal("10"));
        produto.setDataValidade(LocalDate.of(2027, 3, 20));
        produto.setQuantidadeEstoque(new BigDecimal("50"));
        produto.setCategoria(CategoriaProduto.BEBIDA_FRIA);
        produto.setUnidadeMedida(UnidadeMedida.UNIDADE);

        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));

        // Act
        ProdutoResponse response = produtoService.buscarPorId(1L);

        // Assert
        assertEquals(1L, response.id());
        assertEquals("Coca-Cola 350ml", response.nome());
        assertEquals(new BigDecimal("6.00"), response.preco());
        assertTrue(response.ativo());

        verify(produtoRepository).findById(1L);
    }

    @Test
    void deveLancarExcecaoQuandoProdutoNaoExistir() {
        // Arrange
        Long id = 999L;

        when(produtoRepository.findById(id))
                .thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(
                ProdutoNaoEncontradoException.class,
                () -> produtoService.buscarPorId(id));

        verify(produtoRepository).findById(id);
    }

    @Test
    void deveAtualizarProduto() {
        // Arrange
        Long id = 1L;

        Produto produtoExistente = new Produto();
        produtoExistente.setId(id);
        produtoExistente.setNome("Coca-Cola 350ml");
        produtoExistente.setDescricao("Refrigerante em lata");
        produtoExistente.setPreco(new BigDecimal("6.00"));
        produtoExistente.setPrecoCusto(new BigDecimal("3.50"));
        produtoExistente.setEstoqueMinimo(new BigDecimal("10"));
        produtoExistente.setQuantidadeEstoque(new BigDecimal("50"));
        produtoExistente.setCategoria(CategoriaProduto.BEBIDA_FRIA);
        produtoExistente.setUnidadeMedida(UnidadeMedida.UNIDADE);
        produtoExistente.setAtivo(true);

        ProdutoRequest request = new ProdutoRequest(
                "Coca-Cola Zero 350ml",
                "Refrigerante sem açúcar",
                new BigDecimal("6.50"),
                new BigDecimal("3.80"),
                new BigDecimal("15"),
                LocalDate.of(2027, 5, 10),
                new BigDecimal("40"),
                CategoriaProduto.BEBIDA_FRIA,
                UnidadeMedida.UNIDADE);

        when(produtoRepository.findById(id))
                .thenReturn(Optional.of(produtoExistente));

        when(produtoRepository.save(any(Produto.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        ProdutoResponse response = produtoService.atualizar(id, request);

        // Assert
        assertEquals("Coca-Cola Zero 350ml", response.nome());
        assertEquals("Refrigerante sem açúcar", response.descricao());
        assertEquals(new BigDecimal("6.50"), response.preco());
        assertEquals(new BigDecimal("3.80"), response.precoCusto());
        assertEquals(new BigDecimal("15"), response.estoqueMinimo());
        assertEquals(new BigDecimal("40"), response.quantidadeEstoque());
        assertEquals(LocalDate.of(2027, 5, 10), response.dataValidade());
        assertEquals(CategoriaProduto.BEBIDA_FRIA, response.categoria());
        assertEquals(UnidadeMedida.UNIDADE, response.unidadeMedida());
        assertTrue(response.ativo());

        verify(produtoRepository).findById(id);
        verify(produtoRepository).save(produtoExistente);
    }

    @Test
    void deveLancarExcecaoAoAtualizarProdutoInexistente() {
        Long id = 999L;

        ProdutoRequest request = new ProdutoRequest(
                "Produto Inexistente",
                "Descrição do produto inexistente",
                new BigDecimal("10.00"),
                new BigDecimal("5.00"),
                new BigDecimal("5"),
                LocalDate.of(2025, 1, 1),
                new BigDecimal("20"),
                CategoriaProduto.DOCE,
                UnidadeMedida.UNIDADE);

        when(produtoRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(
                ProdutoNaoEncontradoException.class,
                () -> produtoService.atualizar(id, request));

        verify(produtoRepository, never()).save(any());
    }

    @Test
    void deveListarTodosProdutos() {
        // Arrange
        Produto produto1 = new Produto();
        produto1.setId(1L);
        produto1.setNome("Coca-Cola");
        produto1.setPreco(new BigDecimal("6.00"));
        produto1.setPrecoCusto(new BigDecimal("3.50"));
        produto1.setEstoqueMinimo(new BigDecimal("10"));
        produto1.setQuantidadeEstoque(new BigDecimal("50"));
        produto1.setCategoria(CategoriaProduto.BEBIDA_FRIA);
        produto1.setUnidadeMedida(UnidadeMedida.UNIDADE);
        produto1.setAtivo(true);

        Produto produto2 = new Produto();
        produto2.setId(2L);
        produto2.setNome("Brigadeiro");
        produto2.setPreco(new BigDecimal("4.00"));
        produto2.setPrecoCusto(new BigDecimal("1.50"));
        produto2.setEstoqueMinimo(new BigDecimal("5"));
        produto2.setQuantidadeEstoque(new BigDecimal("20"));
        produto2.setCategoria(CategoriaProduto.DOCE);
        produto2.setUnidadeMedida(UnidadeMedida.UNIDADE);
        produto2.setAtivo(true);

        when(produtoRepository.findAll())
                .thenReturn(List.of(produto1, produto2));

        // Act
        List<ProdutoResponse> response = produtoService.listarTodos();

        // Assert
        assertEquals(2, response.size());
        assertEquals("Coca-Cola", response.get(0).nome());
        assertEquals("Brigadeiro", response.get(1).nome());

        verify(produtoRepository).findAll();
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoExistiremProdutos() {
        // Arrange
        when(produtoRepository.findAll())
                .thenReturn(List.of());

        // Act
        List<ProdutoResponse> response = produtoService.listarTodos();

        // Assert
        assertTrue(response.isEmpty());

        verify(produtoRepository).findAll();
    }

    @Test
    void deveDesativarProduto() {
        // Arrange
        Produto produto = new Produto();
        produto.setId(1L);
        produto.setAtivo(true);

        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));

        when(produtoRepository.save(produto)).thenReturn(produto);

        // Act
        ProdutoResponse response = produtoService.desativar(1L);

        // Assert
        assertFalse(response.ativo());

        verify(produtoRepository).save(produto);
    }

    @Test
    void deveLancarExcecaoAoDesativarProdutoInexistente() {
        // Arrange
        Long id = 999L;

        when(produtoRepository.findById(id))
                .thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(
                ProdutoNaoEncontradoException.class,
                () -> produtoService.desativar(id));

        verify(produtoRepository).findById(id);
        verify(produtoRepository, never()).save(any());
    }
}
