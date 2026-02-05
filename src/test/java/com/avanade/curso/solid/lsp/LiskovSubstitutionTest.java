package com.avanade.curso.solid.lsp;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes para Liskov Substitution Principle
 */
class LiskovSubstitutionTest {
    
    @Test
    @DisplayName("Retângulo deve calcular área corretamente")
    void retanguloArea() {
        FormaGeometrica retangulo = new Retangulo(5.0, 3.0);
        
        assertEquals(15.0, retangulo.calcularArea());
        assertEquals(16.0, retangulo.calcularPerimetro());
    }
    
    @Test
    @DisplayName("Quadrado deve calcular área corretamente")
    void quadradoArea() {
        FormaGeometrica quadrado = new Quadrado(4.0);
        
        assertEquals(16.0, quadrado.calcularArea());
        assertEquals(16.0, quadrado.calcularPerimetro());
    }
    
    @Test
    @DisplayName("Deve rejeitar dimensões negativas no retângulo")
    void retanguloDimensoesNegativas() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Retangulo(-5.0, 3.0);
        });
    }
    
    @Test
    @DisplayName("Deve rejeitar dimensões negativas no quadrado")
    void quadradoDimensoesNegativas() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Quadrado(-4.0);
        });
    }
    
    @Test
    @DisplayName("Desenvolvedor deve calcular salário corretamente")
    void desenvolvedorSalario() {
        Funcionario dev = new Desenvolvedor("João", 5000.0, 1000.0);
        
        assertEquals(6000.0, dev.calcularSalario()); // 5000 + 1000
    }
    
    @Test
    @DisplayName("Estagiário deve calcular salário corretamente")
    void estagiarioSalario() {
        Funcionario estag = new Estagiario("Maria", 25.0, 160); // 25/hora * 160 horas
        
        assertEquals(4000.0, estag.calcularSalario());
    }
    
    @Test
    @DisplayName("Voluntário deve retornar 0 como salário (respeita contrato)")
    void voluntarioSalario() {
        Funcionario vol = new Voluntario("Pedro");
        
        //  Voluntário respeita o contrato: retorna 0 em vez de lançar exceção
        assertEquals(0.0, vol.calcularSalario());
    }
    
    @Test
    @DisplayName("Folha de pagamento deve funcionar com qualquer tipo de funcionário")
    void folhaPagamentoPolimorfismo() {
        FolhaPagamento folha = new FolhaPagamento();
        Funcionario[] funcionarios = {
            new Desenvolvedor("Dev1", 5000.0, 500.0),
            new Estagiario("Est1", 20.0, 160),
            new Voluntario("Vol1")
        };
        
        double total = folha.calcularTotal(funcionarios);
        
        // 5500 + 3200 + 0 = 8700
        assertEquals(8700.0, total);
    }
    
    @Test
    @DisplayName("Voluntário deve ter benefícios específicos")
    void voluntarioBeneficios() {
        Voluntario vol = new Voluntario("Ana");
        
        assertEquals("Certificado de participação", vol.getBeneficios());
    }
}
