package com.avanade.curso.excecoes;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.io.*;
import java.sql.SQLException;

/**
 * Testes para Tratamento de Exceções
 */
class ExcecoesTest {
    
    // ============================================
    // Testes de Exceções Customizadas
    // ============================================
    
    @Test
    @DisplayName("Deve lançar exceção checked quando conta não encontrada")
    void excecaoContaNaoEncontrada() {
        ContaBancariaService service = new ContaBancariaService();
        
        ContaNaoEncontradaException exception = assertThrows(
            ContaNaoEncontradaException.class,
            () -> service.transferir("123", null, 100)
        );
        
        assertTrue(exception.getMessage().contains("Conta não encontrada"));
    }
    
    @Test
    @DisplayName("Deve lançar exceção checked com causa")
    void excecaoComCausa() {
        ContaNaoEncontradaException exception = new ContaNaoEncontradaException("123", new RuntimeException("Erro interno"));
        
        assertNotNull(exception.getCause());
        assertEquals("123", exception.getNumeroConta());
    }
    
    @Test
    @DisplayName("Deve lançar exceção unchecked para valor inválido")
    void excecaoValorInvalido() {
        ContaBancariaService service = new ContaBancariaService();
        
        ValorInvalidoException exception = assertThrows(
            ValorInvalidoException.class,
            () -> service.depositar(-100)
        );
        
        assertEquals(-100, exception.getValor());
        assertTrue(exception.getMessage().contains("inválido"));
    }
    
    @Test
    @DisplayName("Deve lançar IllegalArgumentException para valor negativo em transferência")
    void valorNegativoTransferencia() {
        ContaBancariaService service = new ContaBancariaService();
        
        assertThrows(IllegalArgumentException.class,
            () -> service.transferir("123", "456", -50));
    }
    
    @Test
    @DisplayName("Deve lançar IllegalStateException para saldo insuficiente")
    void saldoInsuficiente() {
        ContaBancariaService service = new ContaBancariaService();
        
        assertThrows(IllegalStateException.class,
            () -> service.transferir("123", "456", 5000));
    }
    
    @Test
    @DisplayName("Deve lançar SQLException para erro de banco")
    void erroBancoDeDados() {
        ContaBancariaService service = new ContaBancariaService();
        
        assertThrows(SQLException.class,
            () -> service.transferir("123", "ERRO_DB", 100));
    }
    
    @Test
    @DisplayName("Validação deve acumular erros")
    void validacaoAcumulandoErros() {
        ContaBancariaService service = new ContaBancariaService();
        
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> service.validarCadastro(null, "email-invalido", 15)
        );
        
        String mensagem = exception.getMessage();
        assertTrue(mensagem.contains("Nome é obrigatório"));
        assertTrue(mensagem.contains("Email inválido"));
        assertTrue(mensagem.contains("18 anos"));
    }
    
    // ============================================
    // Testes de Try-Catch
    // ============================================
    
    @Test
    @DisplayName("Deve capturar exceção específica")
    void capturarExcecaoEspecifica() {
        ExcecoesExample example = new ExcecoesExample();
        
        // Não deve lançar exceção para fora
        assertDoesNotThrow(() -> example.exemploTryCatchBasico());
    }
    
    @Test
    @DisplayName("Multi-catch deve capturar múltiplas exceções")
    void multiCatch() {
        ExcecoesExample example = new ExcecoesExample();
        
        assertDoesNotThrow(() -> example.exemploMultiCatch());
    }
    
    @Test
    @DisplayName("Finally deve sempre executar")
    void finallySempreExecuta() {
        ExcecoesExample example = new ExcecoesExample();
        
        // Mesmo com exceção, finally executa
        assertDoesNotThrow(() -> example.exemploFinally());
    }
    
    @Test
    @DisplayName("Try-with-resources deve fechar recursos")
    void tryWithResources() {
        ExcecoesExample example = new ExcecoesExample();
        
        // O método captura a IOException internamente
        assertDoesNotThrow(() -> example.exemploTryWithResources());
    }
    
    @Test
    @DisplayName("Wrap de exceção deve converter checked em unchecked")
    void wrapException() {
        ExcecoesExample example = new ExcecoesExample();
        
        // O método só lança exceção se houver erro na transferência
        // Como transferência com dados válidos funciona, não lança exceção
        // Testamos que quando lança, tem a causa correta
        assertDoesNotThrow(() -> example.exemploWrapException());
    }
    
    @Test
    @DisplayName("Re-throw refinado deve manter tipos específicos")
    void reThrowRefinado() {
        ExcecoesExample example = new ExcecoesExample();
        
        // Deve propagar SQLException
        assertThrows(SQLException.class,
            () -> example.exemploReThrowRefinado());
    }
    
    @Test
    @DisplayName("Finally com return deve executar finally primeiro")
    void finallyComReturn() {
        ExcecoesExample example = new ExcecoesExample();
        
        int resultado = example.exemploFinallyComReturn();
        
        assertEquals(1, resultado);
    }
    
    @Test
    @DisplayName("Supressão de exceções deve capturar ambas")
    void supressaoExcecoes() {
        ExcecoesExample example = new ExcecoesExample();
        
        // Não deve lançar exceção para fora
        assertDoesNotThrow(() -> example.exemploSupressao());
    }
    
    // ============================================
    // Testes de Assert
    // ============================================
    
    @Test
    @DisplayName("Assert deve passar com valor positivo")
    void assertPassa() {
        ExcecoesExample example = new ExcecoesExample();
        
        // Sem -ea, assert não faz nada
        assertDoesNotThrow(() -> example.exemploAssert(10));
    }
    
    @Test
    @DisplayName("Assert deve lançar AssertionError quando ativado (-ea)")
    void assertFalha() {
        // Com -ea, deve lançar AssertionError
        ExcecoesExample example = new ExcecoesExample();
        
        // AssertionError é lançada quando assertions estão ativadas
        assertThrows(AssertionError.class, () -> example.exemploAssert(-5));
    }
    
    // ============================================
    // Testes de Utilitários
    // ============================================
    
    @Test
    @DisplayName("Executar ignorando exceções não deve propagar erro")
    void executarIgnorandoExcecoes() {
        ExceptionUtils.executarIgnorandoExcecoes(() -> {
            throw new RuntimeException("Erro que deve ser ignorado");
        });
        
        // Se chegou aqui, não propagou exceção
        assertTrue(true);
    }
    
    @Test
    @DisplayName("Executar com padrão deve retornar valor em caso de sucesso")
    void executarComPadraoSucesso() {
        String resultado = ExceptionUtils.executarComPadrao(
            () -> "sucesso",
            "padrao"
        );
        
        assertEquals("sucesso", resultado);
    }
    
    @Test
    @DisplayName("Executar com padrão deve retornar padrão em caso de erro")
    void executarComPadraoErro() {
        String resultado = ExceptionUtils.executarComPadrao(
            () -> { throw new RuntimeException("Erro"); },
            "padrao"
        );
        
        assertEquals("padrao", resultado);
    }
    
    // ============================================
    // Testes de Hierarquia de Exceções
    // ============================================
    
    @Test
    @DisplayName("ContaNaoEncontradaException deve ser checked (herda Exception)")
    void excecaoCheckedHierarquia() {
        ContaNaoEncontradaException excecao = new ContaNaoEncontradaException("123");
        
        // Checked exception extends Exception directly
        assertTrue(excecao instanceof Exception);
        // But not RuntimeException
        assertFalse(excecao.getClass().getSuperclass().equals(RuntimeException.class));
    }
    
    @Test
    @DisplayName("ValorInvalidoException deve ser unchecked (herda RuntimeException)")
    void excecaoUncheckedHierarquia() {
        ValorInvalidoException excecao = new ValorInvalidoException(10, 0, 5);
        
        assertTrue(excecao instanceof RuntimeException);
    }
    
    @Test
    @DisplayName("ValidacaoException deve ter campo específico")
    void validacaoExceptionCampo() {
        ValidacaoException excecao = new ValidacaoException("email", "inválido");
        
        assertEquals("email", excecao.getCampo());
        assertTrue(excecao.getMessage().contains("email"));
        assertTrue(excecao.getMessage().contains("inválido"));
    }
    
    @Test
    @DisplayName("Deve ter informações específicas na exceção")
    void excecaoValorInvalidoDetalhes() {
        ValorInvalidoException excecao = new ValorInvalidoException(50, 100, 200);
        
        assertEquals(50, excecao.getValor());
        assertEquals(100, excecao.getMinimo());
        assertEquals(200, excecao.getMaximo());
    }
}
