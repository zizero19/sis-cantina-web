package com.tiocantinas.senac.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.tiocantinas.senac.dto.cliente.ClienteRequest;
import com.tiocantinas.senac.dto.cliente.ClienteResponse;
import com.tiocantinas.senac.exception.ClienteNaoEncontradoException;
import com.tiocantinas.senac.model.Setor;
import com.tiocantinas.senac.service.ClienteService;

@WebMvcTest(ClienteController.class)
public class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ClienteService clienteService;

    @Test
    void deveCadastrarCliente() throws Exception {
        Setor setor = criarSetor();

        ClienteResponse response = new ClienteResponse(
                1L,
                "João Silva",
                "12345678901",
                "48999999999",
                setor,
                true,
                new BigDecimal("500.00"));

        when(clienteService.cadastrar(any(ClienteRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "nome": "João Silva",
                            "cpf": "12345678901",
                            "telefone": "48999999999",
                            "setorId": 1,
                            "limiteCredito": 500.00
                        }
                        """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("João Silva"))
                .andExpect(jsonPath("$.cpf").value("12345678901"))
                .andExpect(jsonPath("$.ativo").value(true));

        verify(clienteService).cadastrar(any(ClienteRequest.class));
    }

    @Test
    void deveBuscarClientePorId() throws Exception {
        ClienteResponse response = criarResponse();

        when(clienteService.buscarPorId(1L))
                .thenReturn(response);

        mockMvc.perform(get("/clientes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("João Silva"))
                .andExpect(jsonPath("$.cpf").value("12345678901"))
                .andExpect(jsonPath("$.ativo").value(true));

        verify(clienteService).buscarPorId(1L);
    }

    @Test
    void deveListarClientes() throws Exception {
        ClienteResponse cliente1 = criarResponse();

        ClienteResponse cliente2 = new ClienteResponse(
                2L,
                "Maria Souza",
                "98765432100",
                "48988888888",
                criarSetor(),
                true,
                new BigDecimal("800.00"));

        when(clienteService.listarTodos())
                .thenReturn(List.of(cliente1, cliente2));

        mockMvc.perform(get("/clientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].nome").value("João Silva"))
                .andExpect(jsonPath("$[1].nome").value("Maria Souza"));

        verify(clienteService).listarTodos();
    }

    @Test
    void deveAtualizarCliente() throws Exception {
        ClienteResponse response = new ClienteResponse(
                1L,
                "João da Silva",
                "12345678901",
                "48977777777",
                criarSetor(),
                true,
                new BigDecimal("750.00"));

        when(clienteService.atualizar(
                eq(1L),
                any(ClienteRequest.class)))
                .thenReturn(response);

        mockMvc.perform(put("/clientes/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "nome": "João da Silva",
                            "cpf": "12345678901",
                            "telefone": "48977777777",
                            "setorId": 1,
                            "limiteCredito": 750.00
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("João da Silva"))
                .andExpect(jsonPath("$.telefone").value("48977777777"))
                .andExpect(jsonPath("$.limiteCredito").value(750.00));

        verify(clienteService)
                .atualizar(eq(1L), any(ClienteRequest.class));
    }

    @Test
    void deveDesativarCliente() throws Exception {
        ClienteResponse response = new ClienteResponse(
                1L,
                "João Silva",
                "12345678901",
                "48999999999",
                criarSetor(),
                false,
                new BigDecimal("500.00"));

        when(clienteService.desativar(1L))
                .thenReturn(response);

        mockMvc.perform(put("/clientes/1/desativar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.ativo").value(false));

        verify(clienteService).desativar(1L);
    }

    @Test
    void deveRetornarBadRequestQuandoClienteForInvalido() throws Exception {
        mockMvc.perform(post("/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "nome": "",
                            "cpf": "",
                            "telefone": "",
                            "setorId": null,
                            "limiteCredito": -10.00
                        }
                        """))
                .andExpect(status().isBadRequest());

        verify(clienteService, never())
                .cadastrar(any());
    }

    @Test
    void deveRetornarBadRequestAoAtualizarClienteComDadosInvalidos()
            throws Exception {

        mockMvc.perform(put("/clientes/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "nome": "",
                            "cpf": "",
                            "telefone": "",
                            "setorId": null,
                            "limiteCredito": -10.00
                        }
                        """))
                .andExpect(status().isBadRequest());

        verify(clienteService, never())
                .atualizar(any(), any());
    }

    @Test
    void deveRetornarNotFoundQuandoClienteNaoExistir()
            throws Exception {

        when(clienteService.buscarPorId(999L))
                .thenThrow(new ClienteNaoEncontradoException(999L));

        mockMvc.perform(get("/clientes/999"))
                .andExpect(status().isNotFound());

        verify(clienteService).buscarPorId(999L);
    }

    private ClienteResponse criarResponse() {
        return new ClienteResponse(
                1L,
                "João Silva",
                "12345678901",
                "48999999999",
                criarSetor(),
                true,
                new BigDecimal("500.00"));
    }

    private Setor criarSetor() {
        Setor setor = new Setor();
        setor.setId(1L);
        setor.setNome("Administrativo");
        setor.setAtivo(true);

        return setor;
    }
}