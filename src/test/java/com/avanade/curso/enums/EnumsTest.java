package com.avanade.curso.enums;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.util.EnumMap;
import java.util.EnumSet;

/**
 * Testes para Enums
 */
class EnumsTest {
    
    // ============================================
    // Testes de Enum Simples
    // ============================================
    
    @Test
    @DisplayName("Enum deve ter valores predefinidos")
    void enumValores() {
        DiaSemana[] dias = DiaSemana.values();
        
        assertEquals(7, dias.length);
        assertEquals(DiaSemana.DOMINGO, dias[0]);
        assertEquals(DiaSemana.SABADO, dias[6]);
    }
    
    @Test
    @DisplayName("Enum deve comparar com ==")
    void enumComparacao() {
        DiaSemana hoje = DiaSemana.QUARTA;
        
        assertTrue(hoje == DiaSemana.QUARTA);
        assertFalse(hoje == DiaSemana.SEGUNDA);
    }
    
    @Test
    @DisplayName("Enum deve ter ordinal")
    void enumOrdinal() {
        assertEquals(0, DiaSemana.DOMINGO.ordinal());
        assertEquals(1, DiaSemana.SEGUNDA.ordinal());
        assertEquals(6, DiaSemana.SABADO.ordinal());
    }
    
    @Test
    @DisplayName("Enum deve converter de string")
    void enumValueOf() {
        DiaSemana dia = DiaSemana.valueOf("QUARTA");
        
        assertEquals(DiaSemana.QUARTA, dia);
    }
    
    // ============================================
    // Testes de Enum com Campos
    // ============================================
    
    @Test
    @DisplayName("StatusPedido deve ter descrição e código")
    void statusPedidoCampos() {
        StatusPedido pendente = StatusPedido.PENDENTE;
        
        assertEquals("Aguardando pagamento", pendente.getDescricao());
        assertEquals(1, pendente.getCodigo());
    }
    
    @Test
    @DisplayName("StatusPedido deve buscar por código")
    void statusPedidoPorCodigo() {
        StatusPedido encontrado = StatusPedido.porCodigo(3);
        
        assertEquals(StatusPedido.PROCESSANDO, encontrado);
    }
    
    @Test
    @DisplayName("StatusPedido deve lançar exceção para código inválido")
    void statusPedidoCodigoInvalido() {
        assertThrows(IllegalArgumentException.class,
            () -> StatusPedido.porCodigo(999));
    }
    
    @Test
    @DisplayName("StatusPedido deve verificar se é finalizado")
    void statusPedidoFinalizado() {
        assertFalse(StatusPedido.PENDENTE.isFinalizado());
        assertFalse(StatusPedido.PAGO.isFinalizado());
        assertTrue(StatusPedido.ENTREGUE.isFinalizado());
        assertTrue(StatusPedido.CANCELADO.isFinalizado());
    }
    
    @Test
    @DisplayName("StatusPedido deve verificar se pode cancelar")
    void statusPedidoPodeCancelar() {
        assertTrue(StatusPedido.PENDENTE.podeCancelar());
        assertTrue(StatusPedido.PAGO.podeCancelar());
        assertFalse(StatusPedido.ENVIADO.podeCancelar());
        assertFalse(StatusPedido.ENTREGUE.podeCancelar());
    }
    
    // ============================================
    // Testes de Métodos Abstratos
    // ============================================
    
    @Test
    @DisplayName("OperacaoMatematica deve somar")
    void operacaoSoma() {
        double resultado = OperacaoMatematica.SOMA.aplicar(10, 5);
        
        assertEquals(15.0, resultado);
    }
    
    @Test
    @DisplayName("OperacaoMatematica deve subtrair")
    void operacaoSubtracao() {
        double resultado = OperacaoMatematica.SUBTRACAO.aplicar(10, 5);
        
        assertEquals(5.0, resultado);
    }
    
    @Test
    @DisplayName("OperacaoMatematica deve multiplicar")
    void operacaoMultiplicacao() {
        double resultado = OperacaoMatematica.MULTIPLICACAO.aplicar(10, 5);
        
        assertEquals(50.0, resultado);
    }
    
    @Test
    @DisplayName("OperacaoMatematica deve dividir")
    void operacaoDivisao() {
        double resultado = OperacaoMatematica.DIVISAO.aplicar(10, 5);
        
        assertEquals(2.0, resultado);
    }
    
    @Test
    @DisplayName("OperacaoMatematica deve lançar exceção na divisão por zero")
    void operacaoDivisaoPorZero() {
        assertThrows(ArithmeticException.class,
            () -> OperacaoMatematica.DIVISAO.aplicar(10, 0));
    }
    
    @Test
    @DisplayName("OperacaoMatematica deve ter símbolo")
    void operacaoSimbolo() {
        assertEquals("+", OperacaoMatematica.SOMA.getSimbolo());
        assertEquals("-", OperacaoMatematica.SUBTRACAO.getSimbolo());
        assertEquals("*", OperacaoMatematica.MULTIPLICACAO.getSimbolo());
        assertEquals("/", OperacaoMatematica.DIVISAO.getSimbolo());
    }
    
    // ============================================
    // Testes de Interface
    // ============================================
    
    @Test
    @DisplayName("TipoCliente PADRAO deve ter desconto zero")
    void tipoClientePadrao() {
        double desconto = TipoCliente.PADRAO.calcularDesconto(1000);
        
        assertEquals(0.0, desconto);
    }
    
    @Test
    @DisplayName("TipoCliente VIP deve ter 10% de desconto")
    void tipoClienteVIP() {
        double desconto = TipoCliente.VIP.calcularDesconto(1000);
        
        assertEquals(100.0, desconto);
    }
    
    @Test
    @DisplayName("TipoCliente PREMIUM deve ter 20% de desconto")
    void tipoClientePremium() {
        double desconto = TipoCliente.PREMIUM.calcularDesconto(1000);
        
        assertEquals(200.0, desconto);
    }
    
    // ============================================
    // Testes de Strategy Pattern
    // ============================================
    
    @Test
    @DisplayName("TipoNotificacao deve enviar email")
    void notificacaoEmail() {
        assertDoesNotThrow(() -> 
            TipoNotificacao.EMAIL.enviar("teste@email.com", "Mensagem"));
    }
    
    @Test
    @DisplayName("TipoNotificacao deve enviar SMS")
    void notificacaoSMS() {
        assertDoesNotThrow(() -> 
            TipoNotificacao.SMS.enviar("11999999999", "Código"));
    }
    
    @Test
    @DisplayName("TipoNotificacao deve enviar PUSH")
    void notificacaoPUSH() {
        assertDoesNotThrow(() -> 
            TipoNotificacao.PUSH.enviar("device-token", "Alerta"));
    }
    
    // ============================================
    // Testes de EnumSet
    // ============================================
    
    @Test
    @DisplayName("EnumSet deve conter enum específico")
    void enumSetContains() {
        EnumSet<DiaSemana> diasUteis = EnumSet.of(
            DiaSemana.SEGUNDA, DiaSemana.TERCA, 
            DiaSemana.QUARTA, DiaSemana.QUINTA, DiaSemana.SEXTA
        );
        
        assertTrue(diasUteis.contains(DiaSemana.SEGUNDA));
        assertFalse(diasUteis.contains(DiaSemana.SABADO));
    }
    
    @Test
    @DisplayName("EnumSet deve criar de todos os valores")
    void enumSetAllOf() {
        EnumSet<DiaSemana> todos = EnumSet.allOf(DiaSemana.class);
        
        assertEquals(7, todos.size());
    }
    
    @Test
    @DisplayName("EnumSet deve criar complemento")
    void enumSetComplement() {
        EnumSet<DiaSemana> fimDeSemana = EnumSet.of(DiaSemana.SABADO, DiaSemana.DOMINGO);
        EnumSet<DiaSemana> diasExcetoFDS = EnumSet.complementOf(fimDeSemana);
        
        assertEquals(5, diasExcetoFDS.size());
        assertTrue(diasExcetoFDS.contains(DiaSemana.SEGUNDA));
        assertFalse(diasExcetoFDS.contains(DiaSemana.SABADO));
    }
    
    // ============================================
    // Testes de EnumMap
    // ============================================
    
    @Test
    @DisplayName("EnumMap deve usar enum como chave")
    void enumMapBasico() {
        EnumMap<StatusPedido, String> map = new EnumMap<>(StatusPedido.class);
        map.put(StatusPedido.PENDENTE, "Aguardando");
        map.put(StatusPedido.PAGO, "Confirmado");
        
        assertEquals("Aguardando", map.get(StatusPedido.PENDENTE));
        assertEquals(2, map.size());
    }
    
    @Test
    @DisplayName("EnumMap deve iterar na ordem natural do enum")
    void enumMapOrdem() {
        EnumMap<StatusPedido, String> map = new EnumMap<>(StatusPedido.class);
        map.put(StatusPedido.PAGO, "Segundo");
        map.put(StatusPedido.PENDENTE, "Primeiro");
        
        // A ordem de inserção não importa - ordena pelo ordinal do enum
        String[] valores = map.values().toArray(new String[0]);
        assertEquals("Primeiro", valores[0]);
        assertEquals("Segundo", valores[1]);
    }
    
    // ============================================
    // Testes de Processamento
    // ============================================
    
    @Test
    @DisplayName("ProcessadorPedido deve processar status")
    void processadorPedido() {
        ProcessadorPedido processador = new ProcessadorPedido();
        
        assertDoesNotThrow(() -> processador.processar(StatusPedido.PENDENTE));
        assertDoesNotThrow(() -> processador.processar(StatusPedido.ENTREGUE));
    }
    
    @Test
    @DisplayName("ProcessadorPedido deve retornar próximo passo")
    void processadorProximoPasso() {
        ProcessadorPedido processador = new ProcessadorPedido();
        
        assertEquals("Confirmar pagamento", processador.getProximoPasso(StatusPedido.PENDENTE));
        assertEquals("Despachar", processador.getProximoPasso(StatusPedido.PROCESSANDO));
        assertEquals("Pedido completo", processador.getProximoPasso(StatusPedido.ENTREGUE));
    }
    
    @Test
    @DisplayName("Calculadora deve calcular com operação")
    void calculadora() {
        Calculadora calc = new Calculadora();
        
        assertEquals(15.0, calc.calcular(10, 5, OperacaoMatematica.SOMA));
        assertEquals(5.0, calc.calcular(10, 5, OperacaoMatematica.SUBTRACAO));
        assertEquals(50.0, calc.calcular(10, 5, OperacaoMatematica.MULTIPLICACAO));
        assertEquals(2.0, calc.calcular(10, 5, OperacaoMatematica.DIVISAO));
    }
    
    // ============================================
    // Testes de Type Safety
    // ============================================
    
    @Test
    @DisplayName("Enum deve ser type-safe")
    void enumTypeSafety() {
        // Não é possível criar valor inválido em compile-time
        // DiaSemana invalido = "SEGUNDA"; // Erro de compilação!
        
        // Deve usar um dos valores definidos
        DiaSemana dia = DiaSemana.SEGUNDA;
        assertNotNull(dia);
    }
    
    @Test
    @DisplayName("Enum deve ser singleton por constante")
    void enumSingleton() {
        // Cada constante é uma única instância
        assertSame(StatusPedido.PENDENTE, StatusPedido.PENDENTE);
    }
}
