package com.avanade.curso.solid.srp;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes para Single Responsibility Principle
 */
class SingleResponsibilityTest {
    
    @Test
    @DisplayName("Deve criar funcionário corretamente")
    void deveCriarFuncionario() {
        Funcionario func = new Funcionario("João", "DESENVOLVEDOR", 5000.0);
        
        assertEquals("João", func.getNome());
        assertEquals("DESENVOLVEDOR", func.getCargo());
        assertEquals(5000.0, func.getSalarioBase());
    }
    
    @Test
    @DisplayName("Calculadora deve calcular salário do desenvolvedor")
    void calculadoraDesenvolvedor() {
        Funcionario dev = new Funcionario("Maria", "DESENVOLVEDOR", 5000.0);
        CalculadoraSalario calc = new CalculadoraSalario();
        
        double salario = calc.calcular(dev);
        
        assertEquals(4000.0, salario); // 5000 * 0.8
    }
    
    @Test
    @DisplayName("Calculadora deve calcular salário do gerente")
    void calculadoraGerente() {
        Funcionario gerente = new Funcionario("Carlos", "GERENTE", 10000.0);
        CalculadoraSalario calc = new CalculadoraSalario();
        
        double salario = calc.calcular(gerente);
        
        assertEquals(7500.0, salario); // 10000 * 0.75
    }
    
    @Test
    @DisplayName("Serviço de email deve enviar email")
    void servicoEmail() {
        Funcionario func = new Funcionario("Ana", "DESENVOLVEDOR", 5000.0);
        ServicoEmail email = new ServicoEmail();
        
        // Não lança exceção = sucesso
        assertDoesNotThrow(() -> {
            email.enviarEmail(func, "Bem-vindo", "Seja bem-vindo à empresa!");
        });
    }
    
    @Test
    @DisplayName("Repository deve salvar funcionário")
    void repositorySalvar() {
        Funcionario func = new Funcionario("Pedro", "ANALISTA", 4500.0);
        FuncionarioRepository repo = new FuncionarioRepository();
        
        assertDoesNotThrow(() -> {
            repo.salvar(func);
        });
    }
    
    @Test
    @DisplayName("Repository deve buscar funcionário")
    void repositoryBuscar() {
        FuncionarioRepository repo = new FuncionarioRepository();
        
        Funcionario encontrado = repo.buscarPorNome("Teste");
        
        assertNotNull(encontrado);
        assertEquals("Teste", encontrado.getNome());
    }
    
    @Test
    @DisplayName("Gerador de relatório deve criar relatório formatado")
    void geradorRelatorio() {
        Funcionario func = new Funcionario("Luiz", "DESENVOLVEDOR", 6000.0);
        GeradorRelatorio gerador = new GeradorRelatorio();
        
        String relatorio = gerador.gerarRelatorioPagamento(func, 4800.0);
        
        assertTrue(relatorio.contains("Luiz"));
        assertTrue(relatorio.contains("DESENVOLVEDOR"));
        assertTrue(relatorio.contains("6000"));
        assertTrue(relatorio.contains("4800"));
    }
}
