package com.tiocantinas.senac.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.tiocantinas.senac.dto.produto.ProdutoRequest;
import com.tiocantinas.senac.dto.produto.ProdutoResponse;
import com.tiocantinas.senac.exception.ProdutoNaoEncontradoException;
import com.tiocantinas.senac.model.enums.CategoriaProduto;
import com.tiocantinas.senac.model.enums.UnidadeMedida;
import com.tiocantinas.senac.service.ProdutoService;

@WebMvcTest(ProdutoController.class)
public class ProdutoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProdutoService produtoService;

    @Test
    void deveCadastrarProduto() throws Exception {
        ProdutoResponse response = new ProdutoResponse(
                1L,
                "Coca-Cola 350ml",
                "Refrigerante em lata",
                new BigDecimal("6.00"),
                new BigDecimal("3.50"),
                new BigDecimal("10"),
                LocalDate.of(2027, 3, 20),
                true,
                new BigDecimal("50"),
                CategoriaProduto.BEBIDA_FRIA,
                UnidadeMedida.UNIDADE);

        when(produtoService.cadastrar(any(ProdutoRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/produtos")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "nome": "Coca-Cola 350ml",
                            "descricao": "Refrigerante em lata",
                            "preco": 6.00,
                            "precoCusto": 3.50,
                            "estoqueMinimo": 10,
                            "dataValidade": "2027-03-20",
                            "quantidadeEstoque": 50,
                            "categoria": "BEBIDA_FRIA",
                            "unidadeMedida": "UNIDADE"
                        }
                        """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Coca-Cola 350ml"))
                .andExpect(jsonPath("$.ativo").value(true));
    }

    @Test
    void deveBuscarProdutoPorId() throws Exception {
        ProdutoResponse response = new ProdutoResponse(
                1L,
                "Coca-Cola 350ml",
                "Refrigerante em lata",
                new BigDecimal("6.00"),
                new BigDecimal("3.50"),
                new BigDecimal("10"),
                LocalDate.of(2027, 3, 20),
                true,
                new BigDecimal("50"),
                CategoriaProduto.BEBIDA_FRIA,
                UnidadeMedida.UNIDADE);

        when(produtoService.buscarPorId(1L))
                .thenReturn(response);

        mockMvc.perform(get("/produtos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Coca-Cola 350ml"))
                .andExpect(jsonPath("$.ativo").value(true));

        verify(produtoService).buscarPorId(1L);
    }

    @Test
    void deveListarProdutos() throws Exception {
        ProdutoResponse produto1 = new ProdutoResponse(
                1L,
                "Coca-Cola",
                "Refrigerante",
                new BigDecimal("6.00"),
                new BigDecimal("3.50"),
                new BigDecimal("10"),
                null,
                true,
                new BigDecimal("50"),
                CategoriaProduto.BEBIDA_FRIA,
                UnidadeMedida.UNIDADE);

        ProdutoResponse produto2 = new ProdutoResponse(
                2L,
                "Brigadeiro",
                "Doce",
                new BigDecimal("4.00"),
                new BigDecimal("1.50"),
                new BigDecimal("5"),
                null,
                true,
                new BigDecimal("20"),
                CategoriaProduto.DOCE,
                UnidadeMedida.UNIDADE);

        when(produtoService.listarTodos())
                .thenReturn(List.of(produto1, produto2));

        mockMvc.perform(get("/produtos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].nome").value("Coca-Cola"))
                .andExpect(jsonPath("$[1].nome").value("Brigadeiro"));

        verify(produtoService).listarTodos();
    }

    @Test
    void deveAtualizarProduto() throws Exception {
        ProdutoResponse response = new ProdutoResponse(
                1L,
                "Coca-Cola 350ml",
                "Refrigerante em lata",
                new BigDecimal("6.00"),
                new BigDecimal("3.50"),
                new BigDecimal("10"),
                LocalDate.of(2027, 3, 20),
                true,
                new BigDecimal("50"),
                CategoriaProduto.BEBIDA_FRIA,
                UnidadeMedida.UNIDADE);

        when(produtoService.atualizar(eq(1L), any(ProdutoRequest.class)))
                .thenReturn(response);

        mockMvc.perform(put("/produtos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "nome": "Coca-Cola 350ml",
                            "descricao": "Refrigerante em lata",
                            "preco": 6.00,
                            "precoCusto": 3.50,
                            "estoqueMinimo": 10,
                            "dataValidade": "2027-03-20",
                            "quantidadeEstoque": 50,
                            "categoria": "BEBIDA_FRIA",
                            "unidadeMedida": "UNIDADE"
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Coca-Cola 350ml"))
                .andExpect(jsonPath("$.ativo").value(true));

        verify(produtoService).atualizar(eq(1L), any(ProdutoRequest.class));
    }

    @Test
    void deveDesativarProduto() throws Exception {
        ProdutoResponse response = new ProdutoResponse(
                1L,
                "Coca-Cola 350ml",
                "Refrigerante em lata",
                new BigDecimal("6.00"),
                new BigDecimal("3.50"),
                new BigDecimal("10"),
                LocalDate.of(2027, 3, 20),
                false,
                new BigDecimal("50"),
                CategoriaProduto.BEBIDA_FRIA,
                UnidadeMedida.UNIDADE);

        when(produtoService.desativar(1L))
                .thenReturn(response);

        mockMvc.perform(put("/produtos/1/desativar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Coca-Cola 350ml"))
                .andExpect(jsonPath("$.ativo").value(false));

        verify(produtoService).desativar(1L);
    }

    @Test
    void deveRetornarBadRequestQuandoProdutoForInvalido() throws Exception {
        mockMvc.perform(post("/produtos")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "nome": "",
                            "preco": -1,
                            "precoCusto": 3.50,
                            "estoqueMinimo": 10,
                            "quantidadeEstoque": 50,
                            "categoria": "BEBIDA_FRIA",
                            "unidadeMedida": "UNIDADE"
                        }
                        """))
                .andExpect(status().isBadRequest());

        verify(produtoService, never())
                .cadastrar(any());
    }

    @Test
    void deveRetornarNotFoundQuandoProdutoNaoExistir() throws Exception {
        when(produtoService.buscarPorId(999L))
                .thenThrow(new ProdutoNaoEncontradoException(999L));

        mockMvc.perform(get("/produtos/999"))
                .andExpect(status().isNotFound());

        verify(produtoService).buscarPorId(999L);
    }

    @Test
    void deveRetornarBadRequestAoAtualizarProdutoComDadosInvalidos() throws Exception {

        mockMvc.perform(put("/produtos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "nome": "",
                            "preco": -10,
                            "precoCusto": 3.50,
                            "estoqueMinimo": 10,
                            "quantidadeEstoque": 50,
                            "categoria": "BEBIDA_FRIA",
                            "unidadeMedida": "UNIDADE"
                        }
                        """))
                .andExpect(status().isBadRequest());

        verify(produtoService, never())
                .atualizar(any(), any());
    }
}
