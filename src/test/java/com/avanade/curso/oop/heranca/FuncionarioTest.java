package com.avanade.curso.oop.heranca;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes demonstrando a Herança
 * 
 * Verificam que:
 * - Classes filhas herdam comportamentos do pai
 * - Sobrescrita permite especialização
 * - Polimorfismo funciona com herança
 */
class FuncionarioTest {
    
    @Test
    @DisplayName("Funcionario base deve calcular salário sem bônus")
    void funcionarioBaseSemBonus() {
        Funcionario func = new Funcionario("Ana", "123.456.789-00", 3000.0);
        
        assertEquals(3000.0, func.calcularSalario());
        assertEquals("Funcionário", func.obterDescricaoCargo());
    }
    
    @Test
    @DisplayName("Gerente herda de Funcionario e tem bônus")
    void gerenteHerdaComBonus() {
        Gerente gerente = new Gerente("Carlos", "987.654.321-00", 5000.0, "TI", 5);
        
        // Salário base (5000) + Bônus gerente (1500) + Bônus equipe (5 * 100)
        double salarioEsperado = 5000.0 + 1500.0 + 500.0;
        
        assertEquals(salarioEsperado, gerente.calcularSalario());
        assertEquals("Gerente de TI", gerente.obterDescricaoCargo());
        assertTrue(gerente.podeAprovarDespesa(3000.0));
        assertFalse(gerente.podeAprovarDespesa(6000.0));
    }
    
    @Test
    @DisplayName("Desenvolvedor herda de Funcionario e tem bônus por nível")
    void desenvolvedorPorNivel() {
        Desenvolvedor junior = new Desenvolvedor(
            "Dev Junior", "111.222.333-44", 3000.0, 
            Desenvolvedor.Nivel.JUNIOR, "Java"
        );
        
        Desenvolvedor senior = new Desenvolvedor(
            "Dev Senior", "555.666.777-88", 8000.0,
            Desenvolvedor.Nivel.SENIOR, "Java"
        );
        
        // Junior: 3000 + (500 * 0.5) = 3250
        assertEquals(3250.0, junior.calcularSalario(), 0.01);
        
        // Senior: 8000 + (500 * 2) = 9000
        assertEquals(9000.0, senior.calcularSalario(), 0.01);
        
        assertEquals("Desenvolvedor JUNIOR - Java", junior.obterDescricaoCargo());
        assertEquals("Desenvolvedor SENIOR - Java", senior.obterDescricaoCargo());
    }
    
    @Test
    @DisplayName("Polimorfismo: tratando objetos filhos como pai")
    void polimorfismoTratandoComoPai() {
        Funcionario func = new Funcionario("Ana", "123.456.789-00", 3000.0);
        Funcionario gerente = new Gerente("Carlos", "987.654.321-00", 5000.0, "TI", 3);
        Funcionario dev = new Desenvolvedor("Pedro", "111.222.333-44", 4000.0, 
                                            Desenvolvedor.Nivel.PLENO, "Python");
        
        // Todos são Funcionario, mas comportamentos são diferentes
        Funcionario[] equipe = {func, gerente, dev};
        
        double folhaSalarial = 0;
        for (Funcionario f : equipe) {
            folhaSalarial += f.calcularSalario();
        }
        
        // Func: 3000
        // Gerente: 5000 + 1500 + 300 = 6800
        // Dev: 4000 + 500 = 4500
        assertEquals(14300.0, folhaSalarial, 0.01);
    }
    
    @Test
    @DisplayName("Desenvolvedor pode ser promovido")
    void desenvolvedorPromocao() {
        Desenvolvedor dev = new Desenvolvedor(
            "Dev", "123.456.789-00", 3000.0,
            Desenvolvedor.Nivel.JUNIOR, "Java"
        );
        
        assertEquals(Desenvolvedor.Nivel.JUNIOR, dev.getNivel());
        
        dev.promover();
        assertEquals(Desenvolvedor.Nivel.PLENO, dev.getNivel());
        
        dev.promover();
        assertEquals(Desenvolvedor.Nivel.SENIOR, dev.getNivel());
        
        dev.promover();
        assertEquals(Desenvolvedor.Nivel.ESPECIALISTA, dev.getNivel());
        
        // Especialista não sobe mais
        dev.promover();
        assertEquals(Desenvolvedor.Nivel.ESPECIALISTA, dev.getNivel());
    }
    
    @Test
    @DisplayName("Desenvolvedor recebe bônus por certificação")
    void desenvolvedorCertificacoes() {
        Desenvolvedor dev = new Desenvolvedor(
            "Dev", "123.456.789-00", 5000.0,
            Desenvolvedor.Nivel.PLENO, "Java"
        );
        
        // Sem certificações: 5000 + 500 = 5500
        assertEquals(5500.0, dev.calcularSalario(), 0.01);
        
        dev.adicionarCertificacao();
        dev.adicionarCertificacao();
        // Com 2 certificações: 5000 + 500 + (2 * 200) = 5900
        assertEquals(5900.0, dev.calcularSalario(), 0.01);
        assertEquals(2, dev.getCertificacoes());
    }
    
    @Test
    @DisplayName("Todos funcionários registram horas")
    void todosRegistramHoras() {
        Funcionario func = new Funcionario("Ana", "123.456.789-00", 3000.0);
        Gerente gerente = new Gerente("Carlos", "987.654.321-00", 5000.0, "TI", 3);
        
        func.registrarHoras(8);
        gerente.registrarHoras(8);
        
        assertEquals(8, func.getHorasTrabalhadas());
        assertEquals(8, gerente.getHorasTrabalhadas());
    }
    
    @Test
    @DisplayName("Não deve permitir horas inválidas")
    void naoPermitirHorasInvalidas() {
        Funcionario func = new Funcionario("Ana", "123.456.789-00", 3000.0);
        
        assertThrows(IllegalArgumentException.class, () -> {
            func.registrarHoras(-1);
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            func.registrarHoras(25);
        });
    }
    
    @Test
    @DisplayName("Gerente pode mudar departamento")
    void gerenteMudaDepartamento() {
        Gerente gerente = new Gerente("Carlos", "987.654.321-00", 5000.0, "TI", 3);
        
        assertEquals("TI", gerente.getDepartamento());
        
        gerente.setDepartamento("Vendas");
        assertEquals("Vendas", gerente.getDepartamento());
        assertEquals("Gerente de Vendas", gerente.obterDescricaoCargo());
    }
}
