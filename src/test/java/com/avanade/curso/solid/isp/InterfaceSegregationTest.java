package com.avanade.curso.solid.isp;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes para Interface Segregation Principle
 */
class InterfaceSegregationTest {
    
    @Test
    @DisplayName("Desenvolvedor deve implementar todas as interfaces necessárias")
    void desenvolvedorInterfaces() {
        Desenvolvedor dev = new Desenvolvedor("João");
        
        // Deve ser um trabalhador
        assertDoesNotThrow(dev::trabalhar);
        
        // Deve ser um ser vivo
        assertDoesNotThrow(dev::comer);
        assertDoesNotThrow(dev::dormir);
        
        // Deve ser um programador
        assertDoesNotThrow(dev::programar);
        assertDoesNotThrow(dev::revisarCodigo);
        
        // Deve participar de reuniões
        assertDoesNotThrow(dev::fazerReuniao);
        assertDoesNotThrow(dev::apresentarRelatorio);
    }
    
    @Test
    @DisplayName("Robô deve trabalhar sem precisar implementar métodos desnecessários")
    void roboTrabalho() {
        Robo robo = new Robo("Modelo X");
        
        //  Robô só implementa Trabalhador (não precisa comer/dormir)
        assertDoesNotThrow(robo::trabalhar);
    }
    
    @Test
    @DisplayName("Atendente deve implementar apenas interfaces necessárias")
    void atendenteInterfaces() {
        AtendenteCliente atendente = new AtendenteCliente("Maria");
        
        // Deve trabalhar
        assertDoesNotThrow(atendente::trabalhar);
        
        // Deve ser um ser vivo
        assertDoesNotThrow(atendente::comer);
        assertDoesNotThrow(atendente::dormir);
        
        // Deve atender cliente
        assertDoesNotThrow(() -> atendente.atenderCliente());
        assertDoesNotThrow(() -> atendente.resolverProblema("Problema de login"));
    }
    
    @Test
    @DisplayName("Robô programador deve implementar Trabalhador e Programador")
    void roboProgramador() {
        RoboProgramador roboDev = new RoboProgramador("2.0");
        
        // Deve trabalhar
        assertDoesNotThrow(roboDev::trabalhar);
        
        // Deve programar
        assertDoesNotThrow(roboDev::programar);
        assertDoesNotThrow(roboDev::revisarCodigo);
        
        //  Não precisa implementar comer/dormir/atenderCliente
    }
    
    @Test
    @DisplayName("Empresa deve iniciar trabalho com qualquer trabalhador")
    void empresaIniciarTrabalho() {
        Empresa empresa = new Empresa();
        Trabalhador[] trabalhadores = {
            new Desenvolvedor("Dev1"),
            new Robo("Robo1"),
            new AtendenteCliente("Atend1"),
            new RoboProgramador("3.0")
        };
        
        assertDoesNotThrow(() -> {
            empresa.iniciarTrabalho(trabalhadores);
        });
    }
    
    @Test
    @DisplayName("Empresa deve permitir almoço apenas para seres vivos")
    void empresaHoraDoAlmoco() {
        Empresa empresa = new Empresa();
        SerVivo[] seres = {
            new Desenvolvedor("Dev1"),
            new AtendenteCliente("Atend1")
        };
        
        assertDoesNotThrow(() -> {
            empresa.horaDoAlmoço(seres);
        });
    }
    
    @Test
    @DisplayName("Empresa deve fazer code review com programadores")
    void empresaCodeReview() {
        Empresa empresa = new Empresa();
        Programador[] programadores = {
            new Desenvolvedor("Dev1"),
            new RoboProgramador("4.0")
        };
        
        assertDoesNotThrow(() -> {
            empresa.codeReview(programadores);
        });
    }
}
