package com.avanade.curso.oop.polimorfismo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

/**
 * Testes demonstrando o Polimorfismo
 * 
 * Verificam que:
 * - Mesmo método se comporta diferente em cada classe
 * - É possível tratar objetos diferentes uniformemente
 * - Sobrecarga permite múltiplas formas de chamar métodos
 */
class CalculadoraPagamentoTest {
    
    private CalculadoraPagamento calculadora;
    
    @BeforeEach
    void setUp() {
        calculadora = new CalculadoraPagamento();
    }
    
    @Test
    @DisplayName("Cartão de crédito processa com taxa por parcela")
    void cartaoCreditoComTaxa() {
        PagamentoCartaoCredito cartao = new PagamentoCartaoCredito(
            1000.0, "4111111111111111", 3
        );
        
        var resultado = calculadora.processarPagamento(cartao);
        
        assertTrue(resultado.sucesso());
        assertEquals("Cartão de Crédito (3x)", resultado.metodo());
        assertEquals(1000.0, resultado.valor());
        // Taxa: 5% + (2% * 2 parcelas extras) = 9% de 1000 = 90
        assertEquals(90.0, resultado.taxa(), 0.01);
        assertEquals(1090.0, resultado.getValorTotal(), 0.01);
    }
    
    @Test
    @DisplayName("Boleto tem desconto e taxa fixa")
    void boletoComDesconto() {
        PagamentoBoleto boleto = new PagamentoBoleto(
            1000.0, "34191.79001 01043.510047 91020.150008 7 92890000100000",
            LocalDate.now().plusDays(7)
        );
        
        var resultado = calculadora.processarPagamento(boleto);
        
        assertTrue(resultado.sucesso());
        // Boleto tem 10% de desconto: 1000 - 10% = 900
        assertEquals(900.0, resultado.valor(), 0.01);
        // Taxa fixa de R$ 2,50
        assertEquals(2.50, resultado.taxa(), 0.01);
    }
    
    @Test
    @DisplayName("Pix PF é gratuito")
    void pixPessoaFisicaGratuito() {
        PagamentoPix pix = new PagamentoPix(
            500.0, "joao@email.com", false
        );
        
        var resultado = calculadora.processarPagamento(pix);
        
        assertTrue(resultado.sucesso());
        assertEquals(0.0, resultado.taxa(), 0.01);
        assertEquals("Pix (Pessoa Física)", resultado.metodo());
    }
    
    @Test
    @DisplayName("Pix PJ tem taxa de 0.5%")
    void pixPessoaJuridicaComTaxa() {
        PagamentoPix pix = new PagamentoPix(
            1000.0, "empresa@cnpj.com", true
        );
        
        var resultado = calculadora.processarPagamento(pix);
        
        assertTrue(resultado.sucesso());
        // 0.5% de 1000 = 5
        assertEquals(5.0, resultado.taxa(), 0.01);
    }
    
    @Test
    @DisplayName("Processar múltiplos pagamentos - sobrecarga com varargs")
    void processarMultiplosPagamentosVarargs() {
        Pagamento pix = new PagamentoPix(100.0, "user@test.com", false);
        Pagamento boleto = new PagamentoBoleto(200.0, "34191.79001...", LocalDate.now());
        
        var resultados = calculadora.processarPagamentos(pix, boleto);
        
        assertEquals(2, resultados.length);
        assertTrue(resultados[0].sucesso());
        assertTrue(resultados[1].sucesso());
    }
    
    @Test
    @DisplayName("Processar múltiplos pagamentos - sobrecarga com List")
    void processarMultiplosPagamentosList() {
        List<Pagamento> pagamentos = Arrays.asList(
            new PagamentoPix(100.0, "user@test.com", false),
            new PagamentoPix(200.0, "user2@test.com", true),
            new PagamentoBoleto(300.0, "34191.79001...", LocalDate.now())
        );
        
        var resultados = calculadora.processarPagamentos(pagamentos);
        
        assertEquals(3, resultados.length);
    }
    
    @Test
    @DisplayName("Calcular total de taxas de vários pagamentos")
    void calcularTotalTaxas() {
        List<Pagamento> pagamentos = Arrays.asList(
            new PagamentoPix(1000.0, "user@test.com", false),        // Taxa: 0
            new PagamentoPix(1000.0, "empresa@cnpj.com", true),     // Taxa: 5
            new PagamentoBoleto(1000.0, "34191.79001...", LocalDate.now()) // Taxa: 2.50
        );
        
        double totalTaxas = calculadora.calcularTotalTaxas(pagamentos);
        
        assertEquals(7.50, totalTaxas, 0.01);
    }
    
    @Test
    @DisplayName("Polimorfismo: mesmo tratamento para tipos diferentes")
    void polimorfismoTratamentoUniforme() {
        // Array de Pagamento (interface) aceita qualquer implementação
        Pagamento[] pagamentos = {
            new PagamentoCartaoCredito(500.0, "4111111111111111", 1),
            new PagamentoBoleto(500.0, "34191.79001...", LocalDate.now()),
            new PagamentoPix(500.0, "user@test.com", false)
        };
        
        // Mesmo loop, comportamentos diferentes
        for (Pagamento p : pagamentos) {
            assertTrue(p.validar());
            assertTrue(p.processar());
            // Cada um calcula taxa de forma diferente
            assertTrue(p.calcularTaxa() >= 0);
        }
    }
    
    @Test
    @DisplayName("Estornar pagamento processado")
    void estornarPagamento() {
        PagamentoCartaoCredito cartao = new PagamentoCartaoCredito(
            100.0, "4111111111111111", 1
        );
        
        cartao.processar();
        assertTrue(cartao.isProcessado());
        
        boolean estornado = calculadora.processarReembolso(cartao);
        assertTrue(estornado);
        assertFalse(cartao.isProcessado());
    }
    
    @Test
    @DisplayName("Não pode estornar pagamento não processado")
    void naoEstornarNaoProcessado() {
        PagamentoCartaoCredito cartao = new PagamentoCartaoCredito(
            100.0, "4111111111111111", 1
        );
        
        // Não processou ainda
        assertFalse(cartao.isProcessado());
        
        boolean estornado = calculadora.processarReembolso(cartao);
        assertFalse(estornado);
    }
    
    @Test
    @DisplayName("Boleto vencido deve ser detectado")
    void boletoVencido() {
        PagamentoBoleto boleto = new PagamentoBoleto(
            100.0, "34191.79001...", LocalDate.now().minusDays(1)
        );
        
        assertTrue(boleto.isVencido());
    }
    
    @Test
    @DisplayName("Cartão com muitas parcelas tem taxa maior")
    void cartaoTaxaPorParcelas() {
        PagamentoCartaoCredito umaX = new PagamentoCartaoCredito(1000.0, "4111111111111111", 1);
        PagamentoCartaoCredito dozeX = new PagamentoCartaoCredito(1000.0, "4111111111111111", 12);
        
        // 1x: 5%
        assertEquals(50.0, umaX.calcularTaxa(), 0.01);
        
        // 12x: 5% + (2% * 11) = 27%
        assertEquals(270.0, dozeX.calcularTaxa(), 0.01);
    }
}
