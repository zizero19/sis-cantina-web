package com.tiocantinas.senac.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.tiocantinas.senac.dto.setor.SetorRequest;
import com.tiocantinas.senac.dto.setor.SetorResponse;
import com.tiocantinas.senac.exception.SetorJaExisteException;
import com.tiocantinas.senac.exception.SetorNaoEncontradoException;
import com.tiocantinas.senac.model.Setor;
import com.tiocantinas.senac.repository.SetorRepository;

@ExtendWith(MockitoExtension.class)
public class SetorServiceTest {

    @Mock
    private SetorRepository setorRepository;

    @InjectMocks
    private SetorService setorService;

    @Test
    void deveCadastrarSetor() {
        // Arrange
        SetorRequest request = new SetorRequest("Administrativo");

        Setor setorSalvo = new Setor();
        setorSalvo.setId(1L);
        setorSalvo.setNome("Administrativo");
        setorSalvo.setAtivo(true);

        when(setorRepository.existsByNomeIgnoreCase("Administrativo"))
                .thenReturn(false);

        when(setorRepository.save(any(Setor.class)))
                .thenReturn(setorSalvo);

        // Act
        SetorResponse response = setorService.cadastrar(request);

        // Assert
        assertEquals(1L, response.id());
        assertEquals("Administrativo", response.nome());
        assertTrue(response.ativo());

        verify(setorRepository)
                .existsByNomeIgnoreCase("Administrativo");

        verify(setorRepository)
                .save(any(Setor.class));
    }

    @Test
    void deveRemoverEspacosDoNomeAoCadastrar() {
        // Arrange
        SetorRequest request = new SetorRequest("  Administrativo  ");

        Setor setorSalvo = new Setor();
        setorSalvo.setId(1L);
        setorSalvo.setNome("Administrativo");
        setorSalvo.setAtivo(true);

        when(setorRepository.existsByNomeIgnoreCase("Administrativo"))
                .thenReturn(false);

        when(setorRepository.save(any(Setor.class)))
                .thenReturn(setorSalvo);

        // Act
        SetorResponse response = setorService.cadastrar(request);

        // Assert
        assertEquals("Administrativo", response.nome());

        verify(setorRepository)
                .existsByNomeIgnoreCase("Administrativo");
    }

    @Test
    void deveLancarExcecaoAoCadastrarSetorComNomeExistente() {
        // Arrange
        SetorRequest request = new SetorRequest("Administrativo");

        when(setorRepository.existsByNomeIgnoreCase("Administrativo"))
                .thenReturn(true);

        // Act + Assert
        assertThrows(
                SetorJaExisteException.class,
                () -> setorService.cadastrar(request));

        verify(setorRepository, never())
                .save(any());
    }

    @Test
    void deveBuscarSetorPorId() {
        // Arrange
        Setor setor = criarSetor();

        when(setorRepository.findById(1L))
                .thenReturn(Optional.of(setor));

        // Act
        SetorResponse response = setorService.buscarPorId(1L);

        // Assert
        assertEquals(1L, response.id());
        assertEquals("Administrativo", response.nome());
        assertTrue(response.ativo());

        verify(setorRepository).findById(1L);
    }

    @Test
    void deveLancarExcecaoQuandoSetorNaoExistir() {
        // Arrange
        Long id = 999L;

        when(setorRepository.findById(id))
                .thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(
                SetorNaoEncontradoException.class,
                () -> setorService.buscarPorId(id));

        verify(setorRepository).findById(id);
    }

    @Test
    void deveListarTodosSetores() {
        // Arrange
        Setor setor1 = criarSetor();

        Setor setor2 = new Setor();
        setor2.setId(2L);
        setor2.setNome("Financeiro");
        setor2.setAtivo(true);

        when(setorRepository.findAll())
                .thenReturn(List.of(setor1, setor2));

        // Act
        List<SetorResponse> response = setorService.listarTodos();

        // Assert
        assertEquals(2, response.size());
        assertEquals("Administrativo", response.get(0).nome());
        assertEquals("Financeiro", response.get(1).nome());

        verify(setorRepository).findAll();
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoExistiremSetores() {
        // Arrange
        when(setorRepository.findAll())
                .thenReturn(List.of());

        // Act
        List<SetorResponse> response = setorService.listarTodos();

        // Assert
        assertTrue(response.isEmpty());

        verify(setorRepository).findAll();
    }

    @Test
    void deveAtualizarSetor() {
        // Arrange
        Setor setorExistente = criarSetor();

        SetorRequest request = new SetorRequest("Financeiro");

        when(setorRepository.findById(1L))
                .thenReturn(Optional.of(setorExistente));

        when(setorRepository.existsByNomeIgnoreCaseAndIdNot(
                "Financeiro", 1L))
                .thenReturn(false);

        when(setorRepository.save(setorExistente))
                .thenReturn(setorExistente);

        // Act
        SetorResponse response = setorService.atualizar(1L, request);

        // Assert
        assertEquals(1L, response.id());
        assertEquals("Financeiro", response.nome());
        assertTrue(response.ativo());

        verify(setorRepository).findById(1L);

        verify(setorRepository)
                .existsByNomeIgnoreCaseAndIdNot("Financeiro", 1L);

        verify(setorRepository).save(setorExistente);
    }

    @Test
    void deveLancarExcecaoAoAtualizarParaNomeDeOutroSetor() {
        // Arrange
        Setor setorExistente = criarSetor();

        SetorRequest request = new SetorRequest("Financeiro");

        when(setorRepository.findById(1L))
                .thenReturn(Optional.of(setorExistente));

        when(setorRepository.existsByNomeIgnoreCaseAndIdNot(
                "Financeiro", 1L))
                .thenReturn(true);

        // Act + Assert
        assertThrows(
                SetorJaExisteException.class,
                () -> setorService.atualizar(1L, request));

        verify(setorRepository, never())
                .save(any());
    }

    @Test
    void deveLancarExcecaoAoAtualizarSetorInexistente() {
        // Arrange
        Long id = 999L;

        SetorRequest request = new SetorRequest("Financeiro");

        when(setorRepository.findById(id))
                .thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(
                SetorNaoEncontradoException.class,
                () -> setorService.atualizar(id, request));

        verify(setorRepository, never())
                .existsByNomeIgnoreCaseAndIdNot(any(), any());

        verify(setorRepository, never())
                .save(any());
    }

    @Test
    void deveDesativarSetor() {
        // Arrange
        Setor setor = criarSetor();

        when(setorRepository.findById(1L))
                .thenReturn(Optional.of(setor));

        when(setorRepository.save(setor))
                .thenReturn(setor);

        // Act
        SetorResponse response = setorService.desativar(1L);

        // Assert
        assertFalse(response.ativo());

        verify(setorRepository).findById(1L);
        verify(setorRepository).save(setor);
    }

    @Test
    void deveAtivarSetor() {
        // Arrange
        Setor setor = criarSetor();
        setor.setAtivo(false);

        when(setorRepository.findById(1L))
                .thenReturn(Optional.of(setor));

        when(setorRepository.save(setor))
                .thenReturn(setor);

        // Act
        SetorResponse response = setorService.ativar(1L);

        // Assert
        assertTrue(response.ativo());

        verify(setorRepository).findById(1L);
        verify(setorRepository).save(setor);
    }

    @Test
    void deveLancarExcecaoAoDesativarSetorInexistente() {
        // Arrange
        Long id = 999L;

        when(setorRepository.findById(id))
                .thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(
                SetorNaoEncontradoException.class,
                () -> setorService.desativar(id));

        verify(setorRepository, never())
                .save(any());
    }

    @Test
    void deveLancarExcecaoAoAtivarSetorInexistente() {
        // Arrange
        Long id = 999L;

        when(setorRepository.findById(id))
                .thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(
                SetorNaoEncontradoException.class,
                () -> setorService.ativar(id));

        verify(setorRepository, never())
                .save(any());
    }

    private Setor criarSetor() {
        Setor setor = new Setor();
        setor.setId(1L);
        setor.setNome("Administrativo");
        setor.setAtivo(true);

        return setor;
    }
}