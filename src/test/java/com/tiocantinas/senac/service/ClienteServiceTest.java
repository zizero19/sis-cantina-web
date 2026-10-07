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
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.tiocantinas.senac.dto.cliente.ClienteRequest;
import com.tiocantinas.senac.dto.cliente.ClienteResponse;
import com.tiocantinas.senac.exception.ClienteNaoEncontradoException;
import com.tiocantinas.senac.model.Cliente;
import com.tiocantinas.senac.model.Setor;
import com.tiocantinas.senac.repository.ClienteRepository;
import com.tiocantinas.senac.repository.SetorRepository;

@ExtendWith(MockitoExtension.class)
public class ClienteServiceTest {

        @Mock
        private ClienteRepository clienteRepository;

        @Mock
        private SetorRepository setorRepository;

        @InjectMocks
        private ClienteService clienteService;

        @Test
        void deveCadastrarCliente() {
                // Arrange
                Setor setor = criarSetor();

                ClienteRequest request = new ClienteRequest(
                                "João Silva",
                                "12345678901",
                                "48999999999",
                                1L,
                                new BigDecimal("500.00"));

                Cliente clienteSalvo = new Cliente();
                clienteSalvo.setId(1L);
                clienteSalvo.setNome(request.nome());
                clienteSalvo.setCpf(request.cpf());
                clienteSalvo.setTelefone(request.telefone());
                clienteSalvo.setSetor(setor);
                clienteSalvo.setAtivo(true);
                clienteSalvo.setLimiteCredito(request.limiteCredito());

                when(setorRepository.findById(1L))
                                .thenReturn(Optional.of(setor));

                when(clienteRepository.save(any(Cliente.class)))
                                .thenReturn(clienteSalvo);

                // Act
                ClienteResponse response = clienteService.cadastrar(request);

                // Assert
                assertEquals(1L, response.id());
                assertEquals("João Silva", response.nome());
                assertEquals("12345678901", response.cpf());
                assertEquals("48999999999", response.telefone());
                assertEquals(setor, response.setor());
                assertEquals(new BigDecimal("500.00"), response.limiteCredito());
                assertTrue(response.ativo());

                verify(setorRepository).findById(1L);
                verify(clienteRepository).save(any(Cliente.class));
        }

        @Test
        void deveBuscarClientePorId() {
                // Arrange
                Cliente cliente = criarCliente();

                when(clienteRepository.findById(1L))
                                .thenReturn(Optional.of(cliente));

                // Act
                ClienteResponse response = clienteService.buscarPorId(1L);

                // Assert
                assertEquals(1L, response.id());
                assertEquals("João Silva", response.nome());
                assertEquals("12345678901", response.cpf());
                assertTrue(response.ativo());

                verify(clienteRepository).findById(1L);
        }

        @Test
        void deveLancarExcecaoQuandoClienteNaoExistir() {
                // Arrange
                Long id = 999L;

                when(clienteRepository.findById(id))
                                .thenReturn(Optional.empty());

                // Act + Assert
                assertThrows(
                                ClienteNaoEncontradoException.class,
                                () -> clienteService.buscarPorId(id));

                verify(clienteRepository).findById(id);
        }

        @Test
        void deveListarTodosClientes() {
                // Arrange
                Cliente cliente1 = criarCliente();

                Cliente cliente2 = new Cliente();
                cliente2.setId(2L);
                cliente2.setNome("Maria Souza");
                cliente2.setCpf("98765432100");
                cliente2.setTelefone("48988888888");
                cliente2.setSetor(criarSetor());
                cliente2.setAtivo(true);
                cliente2.setLimiteCredito(new BigDecimal("800.00"));

                when(clienteRepository.findAll())
                                .thenReturn(List.of(cliente1, cliente2));

                // Act
                List<ClienteResponse> response = clienteService.listarTodos();

                // Assert
                assertEquals(2, response.size());
                assertEquals("João Silva", response.get(0).nome());
                assertEquals("Maria Souza", response.get(1).nome());

                verify(clienteRepository).findAll();
        }

        @Test
        void deveRetornarListaVaziaQuandoNaoExistiremClientes() {
                // Arrange
                when(clienteRepository.findAll())
                                .thenReturn(List.of());

                // Act
                List<ClienteResponse> response = clienteService.listarTodos();

                // Assert
                assertTrue(response.isEmpty());

                verify(clienteRepository).findAll();
        }

        @Test
        void deveAtualizarCliente() {
                // Arrange
                Cliente clienteExistente = criarCliente();

                Setor novoSetor = new Setor();
                novoSetor.setId(2L);
                novoSetor.setNome("Financeiro");
                novoSetor.setAtivo(true);

                ClienteRequest request = new ClienteRequest(
                                "João da Silva",
                                "12345678901",
                                "48977777777",
                                2L,
                                new BigDecimal("750.00"));

                when(clienteRepository.findById(1L))
                                .thenReturn(Optional.of(clienteExistente));

                when(setorRepository.findById(2L))
                                .thenReturn(Optional.of(novoSetor));

                when(clienteRepository.save(any(Cliente.class)))
                                .thenAnswer(invocation -> invocation.getArgument(0));

                // Act
                ClienteResponse response = clienteService.atualizar(1L, request);

                // Assert
                assertEquals("João da Silva", response.nome());
                assertEquals("12345678901", response.cpf());
                assertEquals("48977777777", response.telefone());
                assertEquals(2L, response.setor().getId());
                assertEquals(new BigDecimal("750.00"), response.limiteCredito());
                assertTrue(response.ativo());

                verify(clienteRepository).findById(1L);
                verify(setorRepository).findById(2L);
                verify(clienteRepository).save(clienteExistente);
        }

        @Test
        void deveLancarExcecaoAoAtualizarClienteInexistente() {
                // Arrange
                Long id = 999L;

                ClienteRequest request = new ClienteRequest(
                                "Cliente Inexistente",
                                "12345678901",
                                "48999999999",
                                1L,
                                new BigDecimal("500.00"));

                when(clienteRepository.findById(id))
                                .thenReturn(Optional.empty());

                // Act + Assert
                assertThrows(
                                ClienteNaoEncontradoException.class,
                                () -> clienteService.atualizar(id, request));

                verify(clienteRepository, never()).save(any());
                verify(setorRepository, never()).findById(any());
        }

        @Test
        void deveDesativarCliente() {
                // Arrange
                Cliente cliente = criarCliente();

                when(clienteRepository.findById(1L))
                                .thenReturn(Optional.of(cliente));

                when(clienteRepository.save(cliente))
                                .thenReturn(cliente);

                // Act
                ClienteResponse response = clienteService.desativar(1L);

                // Assert
                assertFalse(response.ativo());

                verify(clienteRepository).findById(1L);
                verify(clienteRepository).save(cliente);
        }

        @Test
        void deveLancarExcecaoAoDesativarClienteInexistente() {
                // Arrange
                Long id = 999L;

                when(clienteRepository.findById(id))
                                .thenReturn(Optional.empty());

                // Act + Assert
                assertThrows(
                                ClienteNaoEncontradoException.class,
                                () -> clienteService.desativar(id));

                verify(clienteRepository, never()).save(any());
        }

        private Cliente criarCliente() {
                Cliente cliente = new Cliente();
                cliente.setId(1L);
                cliente.setNome("João Silva");
                cliente.setCpf("12345678901");
                cliente.setTelefone("48999999999");
                cliente.setSetor(criarSetor());
                cliente.setAtivo(true);
                cliente.setLimiteCredito(new BigDecimal("500.00"));

                return cliente;
        }

        private Setor criarSetor() {
                Setor setor = new Setor();
                setor.setId(1L);
                setor.setNome("Administrativo");
                setor.setAtivo(true);

                return setor;
        }
}