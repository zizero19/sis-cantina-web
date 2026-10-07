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

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.tiocantinas.senac.dto.setor.SetorRequest;
import com.tiocantinas.senac.dto.setor.SetorResponse;
import com.tiocantinas.senac.exception.SetorJaExisteException;
import com.tiocantinas.senac.exception.SetorNaoEncontradoException;
import com.tiocantinas.senac.service.SetorService;

@WebMvcTest(SetorController.class)
public class SetorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SetorService setorService;

    @Test
    void deveCadastrarSetor() throws Exception {
        SetorResponse response = new SetorResponse(
                1L,
                "Administrativo",
                true);

        when(setorService.cadastrar(any(SetorRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/setores")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "nome": "Administrativo"
                        }
                        """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Administrativo"))
                .andExpect(jsonPath("$.ativo").value(true));

        verify(setorService)
                .cadastrar(any(SetorRequest.class));
    }

    @Test
    void deveBuscarSetorPorId() throws Exception {
        SetorResponse response = criarResponse();

        when(setorService.buscarPorId(1L))
                .thenReturn(response);

        mockMvc.perform(get("/setores/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Administrativo"))
                .andExpect(jsonPath("$.ativo").value(true));

        verify(setorService).buscarPorId(1L);
    }

    @Test
    void deveListarSetores() throws Exception {
        SetorResponse setor1 = criarResponse();

        SetorResponse setor2 = new SetorResponse(
                2L,
                "Financeiro",
                true);

        when(setorService.listarTodos())
                .thenReturn(List.of(setor1, setor2));

        mockMvc.perform(get("/setores"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].nome")
                        .value("Administrativo"))
                .andExpect(jsonPath("$[1].nome")
                        .value("Financeiro"));

        verify(setorService).listarTodos();
    }

    @Test
    void deveAtualizarSetor() throws Exception {
        SetorResponse response = new SetorResponse(
                1L,
                "Financeiro",
                true);

        when(setorService.atualizar(
                eq(1L),
                any(SetorRequest.class)))
                .thenReturn(response);

        mockMvc.perform(put("/setores/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "nome": "Financeiro"
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Financeiro"))
                .andExpect(jsonPath("$.ativo").value(true));

        verify(setorService)
                .atualizar(eq(1L), any(SetorRequest.class));
    }

    @Test
    void deveDesativarSetor() throws Exception {
        SetorResponse response = new SetorResponse(
                1L,
                "Administrativo",
                false);

        when(setorService.desativar(1L))
                .thenReturn(response);

        mockMvc.perform(put("/setores/1/desativar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.ativo").value(false));

        verify(setorService).desativar(1L);
    }

    @Test
    void deveAtivarSetor() throws Exception {
        SetorResponse response = new SetorResponse(
                1L,
                "Administrativo",
                true);

        when(setorService.ativar(1L))
                .thenReturn(response);

        mockMvc.perform(put("/setores/1/ativar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.ativo").value(true));

        verify(setorService).ativar(1L);
    }

    @Test
    void deveRetornarBadRequestQuandoSetorForInvalido()
            throws Exception {

        mockMvc.perform(post("/setores")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "nome": ""
                        }
                        """))
                .andExpect(status().isBadRequest());

        verify(setorService, never())
                .cadastrar(any());
    }

    @Test
    void deveRetornarBadRequestAoAtualizarSetorComNomeInvalido()
            throws Exception {

        mockMvc.perform(put("/setores/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "nome": ""
                        }
                        """))
                .andExpect(status().isBadRequest());

        verify(setorService, never())
                .atualizar(any(), any());
    }

    @Test
    void deveRetornarNotFoundQuandoSetorNaoExistir()
            throws Exception {

        when(setorService.buscarPorId(999L))
                .thenThrow(new SetorNaoEncontradoException(999L));

        mockMvc.perform(get("/setores/999"))
                .andExpect(status().isNotFound());

        verify(setorService).buscarPorId(999L);
    }

    @Test
    void deveRetornarConflictQuandoNomeDoSetorJaExistir()
            throws Exception {

        when(setorService.cadastrar(any(SetorRequest.class)))
                .thenThrow(new SetorJaExisteException("Administrativo"));

        mockMvc.perform(post("/setores")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "nome": "Administrativo"
                        }
                        """))
                .andExpect(status().isConflict());

        verify(setorService)
                .cadastrar(any(SetorRequest.class));
    }

    @Test
    void deveRetornarConflictAoAtualizarParaNomeJaExistente()
            throws Exception {

        when(setorService.atualizar(
                eq(1L),
                any(SetorRequest.class)))
                .thenThrow(new SetorJaExisteException("Financeiro"));

        mockMvc.perform(put("/setores/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "nome": "Financeiro"
                        }
                        """))
                .andExpect(status().isConflict());

        verify(setorService)
                .atualizar(eq(1L), any(SetorRequest.class));
    }

    private SetorResponse criarResponse() {
        return new SetorResponse(
                1L,
                "Administrativo",
                true);
    }
}