package com.avanade.curso.solid.ocp;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes para Open/Closed Principle
 */
class OpenClosedTest {
    
    @Test
    @DisplayName("Processador deve aceitar pagamento por cartão")
    void processarCartaoCredito() {
        ProcessadorPagamento processador = new ProcessadorPagamento();
        MetodoPagamento cartao = new PagamentoCartaoCredito("1234567890123456");
        
        assertDoesNotThrow(() -> {
            processador.processar(cartao, 100.0);
        });
    }
    
    @Test
    @DisplayName("Processador deve aceitar pagamento por boleto")
    void processarBoleto() {
        ProcessadorPagamento processador = new ProcessadorPagamento();
        MetodoPagamento boleto = new PagamentoBoleto();
        
        assertDoesNotThrow(() -> {
            processador.processar(boleto, 250.0);
        });
    }
    
    @Test
    @DisplayName("Processador deve aceitar pagamento por PIX")
    void processarPix() {
        ProcessadorPagamento processador = new ProcessadorPagamento();
        MetodoPagamento pix = new PagamentoPix("joao@email.com");
        
        assertDoesNotThrow(() -> {
            processador.processar(pix, 50.0);
        });
    }
    
    @Test
    @DisplayName("Processador deve aceitar NOVO método (criptomoeda) sem modificação")
    void processarCripto() {
        ProcessadorPagamento processador = new ProcessadorPagamento();
        MetodoPagamento cripto = new PagamentoCripto("0x123abc", "ETH");
        
        //  Funciona sem modificar ProcessadorPagamento!
        assertDoesNotThrow(() -> {
            processador.processar(cripto, 500.0);
        });
    }
    
    @Test
    @DisplayName("Processador deve rejeitar valor negativo")
    void rejeitarValorNegativo() {
        ProcessadorPagamento processador = new ProcessadorPagamento();
        MetodoPagamento pix = new PagamentoPix("teste@teste.com");
        
        assertThrows(IllegalArgumentException.class, () -> {
            processador.processar(pix, -100.0);
        });
    }
    
    @Test
    @DisplayName("Processador deve rejeitar valor zero")
    void rejeitarValorZero() {
        ProcessadorPagamento processador = new ProcessadorPagamento();
        MetodoPagamento pix = new PagamentoPix("teste@teste.com");
        
        assertThrows(IllegalArgumentException.class, () -> {
            processador.processar(pix, 0.0);
        });
    }
    
    @Test
    @DisplayName("Deve processar múltiplos pagamentos")
    void processarMultiplosPagamentos() {
        ProcessadorPagamento processador = new ProcessadorPagamento();
        MetodoPagamento[] metodos = {
            new PagamentoPix("user1@test.com"),
            new PagamentoBoleto(),
            new PagamentoCripto("wallet123", "BTC")
        };
        double[] valores = {100.0, 200.0, 300.0};
        
        assertDoesNotThrow(() -> {
            processador.processarMultiplos(metodos, valores);
        });
    }
    
    @Test
    @DisplayName("Deve lançar exceção quando arrays têm tamanhos diferentes")
    void arraysTamanhosDiferentes() {
        ProcessadorPagamento processador = new ProcessadorPagamento();
        MetodoPagamento[] metodos = {new PagamentoPix("teste@teste.com")};
        double[] valores = {100.0, 200.0};
        
        assertThrows(IllegalArgumentException.class, () -> {
            processador.processarMultiplos(metodos, valores);
        });
    }
}
