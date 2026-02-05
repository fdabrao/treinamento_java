package com.avanade.curso.funcional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.function.Predicate;
import java.util.function.IntBinaryOperator;

/**
 * Testes para Lambdas
 */
class LambdasTest {
    
    @Test
    @DisplayName("Deve filtrar números pares usando lambda")
    void filtrarPares() {
        LambdasExample example = new LambdasExample();
        List<Integer> numeros = List.of(1, 2, 3, 4, 5, 6);
        
        List<Integer> pares = example.filtrarPares(numeros);
        
        assertEquals(List.of(2, 4, 6), pares);
    }
    
    @Test
    @DisplayName("Deve transformar textos para maiúsculas")
    void transformarMaiusculas() {
        LambdasExample example = new LambdasExample();
        List<String> textos = List.of("java", "programação", "funcional");
        
        List<String> maiusculas = example.transformarMaiusculas(textos);
        
        assertEquals(List.of("JAVA", "PROGRAMAÇÃO", "FUNCIONAL"), maiusculas);
    }
    
    @Test
    @DisplayName("Deve ordenar por tamanho do texto")
    void ordenarPorTamanho() {
        LambdasExample example = new LambdasExample();
        List<String> textos = List.of("java", "programação", "oi", "funcional");
        
        List<String> ordenado = example.ordenarPorTamanho(textos);
        
        assertEquals(List.of("oi", "java", "funcional", "programação"), ordenado);
    }
    
    @Test
    @DisplayName("Deve adicionar valor a cada número")
    void adicionarValor() {
        LambdasExample example = new LambdasExample();
        List<Integer> numeros = List.of(1, 2, 3);
        
        List<Integer> resultado = example.adicionarValor(numeros, 10);
        
        assertEquals(List.of(11, 12, 13), resultado);
    }
    
    @Test
    @DisplayName("Deve processar textos em múltiplas linhas")
    void processarTextos() {
        LambdasExample example = new LambdasExample();
        List<String> textos = List.of("  JAVA  ", "PROGRAMAÇÃO  ");
        
        List<String> processados = example.processarTextos(textos);
        
        assertEquals(List.of("java", "programação"), processados);
    }
    
    @Test
    @DisplayName("Deve criar validador que aceita valores maiores ou iguais ao mínimo")
    void criarValidador() {
        LambdasExample example = new LambdasExample();
        Predicate<Integer> validador = example.criarValidador(18);
        
        assertTrue(validador.test(18));
        assertTrue(validador.test(25));
        assertFalse(validador.test(17));
    }
    
    @Test
    @DisplayName("Deve executar operação com lambda")
    void executarOperacao() {
        LambdasExample example = new LambdasExample();
        
        int soma = example.executarOperacao(5, 3, (a, b) -> a + b);
        int multiplicacao = example.executarOperacao(5, 3, (a, b) -> a * b);
        
        assertEquals(8, soma);
        assertEquals(15, multiplicacao);
    }
    
    @Test
    @DisplayName("Deve executar operação com method reference")
    void executarOperacaoComMethodReference() {
        LambdasExample example = new LambdasExample();
        
        int soma = example.executarOperacao(5, 3, Integer::sum);
        int max = example.executarOperacao(5, 3, Math::max);
        
        assertEquals(8, soma);
        assertEquals(5, max);
    }
}
