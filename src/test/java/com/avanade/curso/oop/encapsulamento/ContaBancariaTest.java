package com.avanade.curso.oop.encapsulamento;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes demonstrando o Encapsulamento
 * 
 * Os testes verificam que:
 * - Atributos privados não são acessíveis diretamente
 * - Validações impedem estados inválidos
 * - Comportamento interno está protegido
 */
class ContaBancariaTest {
    
    private ContaBancaria conta;
    
    @BeforeEach
    void setUp() {
        conta = new ContaBancaria("12345-6", "João Silva", 1000.0);
    }
    
    @Test
    @DisplayName("Deve criar conta com saldo inicial válido")
    void deveCriarContaComSaldoInicial() {
        assertEquals("12345-6", conta.getNumeroConta());
        assertEquals("João Silva", conta.getTitular());
        assertEquals(1000.0, conta.getSaldo());
        assertTrue(conta.isAtiva());
    }
    
    @Test
    @DisplayName("Não deve permitir saldo inicial negativo (encapsulamento protege)")
    void naoDevePermitirSaldoInicialNegativo() {
        ContaBancaria contaNegativa = new ContaBancaria("000-1", "Teste", -500.0);
        // O encapsulamento corrige para 0
        assertEquals(0.0, contaNegativa.getSaldo());
    }
    
    @Test
    @DisplayName("Deve depositar valor válido")
    void deveDepositarValorValido() {
        conta.depositar(500.0);
        assertEquals(1500.0, conta.getSaldo());
    }
    
    @Test
    @DisplayName("Não deve permitir depósito negativo")
    void naoDevePermitirDepositoNegativo() {
        assertThrows(IllegalArgumentException.class, () -> {
            conta.depositar(-100.0);
        });
        assertEquals(1000.0, conta.getSaldo()); // Saldo não alterado
    }
    
    @Test
    @DisplayName("Não deve permitir depósito zero")
    void naoDevePermitirDepositoZero() {
        assertThrows(IllegalArgumentException.class, () -> {
            conta.depositar(0.0);
        });
    }
    
    @Test
    @DisplayName("Deve sacar valor dentro do limite")
    void deveSacarValorDentroDoLimite() {
        conta.sacar(500.0);
        assertEquals(500.0, conta.getSaldo());
    }
    
    @Test
    @DisplayName("Não deve permitir saque acima do saldo")
    void naoDevePermitirSaqueAcimaDoSaldo() {
        assertThrows(IllegalStateException.class, () -> {
            conta.sacar(2000.0);
        });
        assertEquals(1000.0, conta.getSaldo());
    }
    
    @Test
    @DisplayName("Não deve permitir saque acima do limite permitido")
    void naoDevePermitirSaqueAcimaDoLimite() {
        // Depositar mais para ter saldo suficiente, mas tentar sacar acima do limite
        conta.depositar(1000.0); // Saldo agora é 2000
        assertThrows(IllegalArgumentException.class, () -> {
            conta.sacar(1500.0); // Limite é 1000, mas saldo é suficiente
        });
    }
    
    @Test
    @DisplayName("Deve transferir entre contas")
    void deveTransferirEntreContas() {
        ContaBancaria destino = new ContaBancaria("999-9", "Maria", 0.0);
        
        conta.transferir(destino, 300.0);
        
        assertEquals(700.0, conta.getSaldo());
        assertEquals(300.0, destino.getSaldo());
    }
    
    @Test
    @DisplayName("Não deve permitir transferir para mesma conta")
    void naoDeveTransferirParaMesmaConta() {
        assertThrows(IllegalArgumentException.class, () -> {
            conta.transferir(conta, 100.0);
        });
    }
    
    @Test
    @DisplayName("Não deve permitir alterar titular para nome vazio")
    void naoDevePermitirTitularVazio() {
        assertThrows(IllegalArgumentException.class, () -> {
            conta.setTitular("");
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            conta.setTitular(null);
        });
    }
    
    @Test
    @DisplayName("Deve permitir alterar titular com nome válido")
    void devePermitirAlterarTitular() {
        conta.setTitular("João Santos");
        assertEquals("João Santos", conta.getTitular());
    }
    
    @Test
    @DisplayName("Não deve permitir operações em conta inativa")
    void naoDeveOperarContaInativa() {
        conta.sacar(1000.0); // Zera o saldo
        conta.encerrarConta();
        
        assertFalse(conta.isAtiva());
        
        assertThrows(IllegalStateException.class, () -> {
            conta.depositar(100.0);
        });
        
        assertThrows(IllegalStateException.class, () -> {
            conta.sacar(50.0);
        });
    }
    
    @Test
    @DisplayName("Não deve permitir encerrar conta com saldo")
    void naoDeveEncerrarContaComSaldo() {
        assertThrows(IllegalStateException.class, () -> {
            conta.encerrarConta();
        });
        assertTrue(conta.isAtiva());
    }
    
    @Test
    @DisplayName("Deve encerrar conta zerada")
    void deveEncerrarContaZerada() {
        conta.sacar(1000.0); // Zera
        conta.encerrarConta();
        
        assertFalse(conta.isAtiva());
    }
    
    @Test
    @DisplayName("Não expõe saldo diretamente - apenas via getter")
    void naoExpoemSaldoDiretamente() {
        // Este teste demonstra que não há como modificar saldo diretamente
        // A única forma é através dos métodos de negócio (depositar/sacar/transferir)
        
        double saldoAtual = conta.getSaldo();
        assertEquals(1000.0, saldoAtual);
        
        // Não existe setSaldo() - encapsulamento protege o dado
    }
}
