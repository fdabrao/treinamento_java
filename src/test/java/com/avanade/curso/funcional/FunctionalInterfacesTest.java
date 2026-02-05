package com.avanade.curso.funcional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import java.util.function.*;

/**
 * Testes para Interfaces Funcionais
 */
class FunctionalInterfacesTest {
    
    @Test
    @DisplayName("Deve filtrar usando Predicate")
    void filtrar() {
        FunctionalInterfacesExample example = new FunctionalInterfacesExample();
        List<Integer> numeros = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        
        List<Integer> pares = example.filtrar(numeros, n -> n % 2 == 0);
        List<Integer> maioresQue5 = example.filtrar(numeros, n -> n > 5);
        
        assertEquals(List.of(2, 4, 6, 8, 10), pares);
        assertEquals(List.of(6, 7, 8, 9, 10), maioresQue5);
    }
    
    @Test
    @DisplayName("Deve transformar usando Function")
    void transformar() {
        FunctionalInterfacesExample example = new FunctionalInterfacesExample();
        List<Integer> numeros = List.of(1, 2, 3);
        
        List<String> texto = example.transformar(numeros, n -> "Número: " + n);
        List<String> quadrados = example.transformar(numeros, n -> String.valueOf(n * n));
        
        assertEquals(List.of("Número: 1", "Número: 2", "Número: 3"), texto);
        assertEquals(List.of("1", "4", "9"), quadrados);
    }
    
    @Test
    @DisplayName("Deve aplicar operação unária")
    void aplicarOperacao() {
        FunctionalInterfacesExample example = new FunctionalInterfacesExample();
        List<Integer> numeros = List.of(1, 2, 3);
        
        List<Integer> dobrados = example.aplicarOperacao(numeros, n -> n * 2);
        List<Integer> incrementados = example.aplicarOperacao(numeros, n -> n + 1);
        
        assertEquals(List.of(2, 4, 6), dobrados);
        assertEquals(List.of(2, 3, 4), incrementados);
    }
    
    @Test
    @DisplayName("Deve reduzir lista")
    void reduzir() {
        FunctionalInterfacesExample example = new FunctionalInterfacesExample();
        List<Integer> numeros = List.of(1, 2, 3, 4, 5);
        
        int soma = example.reduzir(numeros, 0, Integer::sum);
        int produto = example.reduzir(numeros, 1, (a, b) -> a * b);
        
        assertEquals(15, soma);
        assertEquals(120, produto);
    }
    
    @Test
    @DisplayName("Deve combinar duas listas")
    void combinar() {
        FunctionalInterfacesExample example = new FunctionalInterfacesExample();
        List<String> nomes = List.of("Ana", "Bruno");
        List<Integer> idades = List.of(25, 30);
        
        List<String> resultado = example.combinar(nomes, idades, (nome, idade) -> nome + " tem " + idade + " anos");
        
        assertEquals(List.of("Ana tem 25 anos", "Bruno tem 30 anos"), resultado);
    }
    
    @Test
    @DisplayName("Deve testar par usando BiPredicate")
    void testarPar() {
        FunctionalInterfacesExample example = new FunctionalInterfacesExample();
        
        boolean resultado = example.testarPar("Maria", 25, (nome, idade) -> nome.length() > 3 && idade >= 18);
        
        assertTrue(resultado);
    }
    
    @Test
    @DisplayName("Deve obter valor ou padrão usando Supplier")
    void obterOuPadrao() {
        FunctionalInterfacesExample example = new FunctionalInterfacesExample();
        
        String valor = example.obterOuPadrao("Original", () -> "Padrão");
        String padrao = example.obterOuPadrao(null, () -> "Padrão");
        
        assertEquals("Original", valor);
        assertEquals("Padrão", padrao);
    }
    
    @Test
    @DisplayName("Deve calcular média de inteiros")
    void calcularMedia() {
        FunctionalInterfacesExample example = new FunctionalInterfacesExample();
        
        double media = example.calcularMedia(new int[]{10, 20, 30});
        
        assertEquals(20.0, media);
    }
    
    @Test
    @DisplayName("Deve filtrar compostos")
    void filtrarComposto() {
        FunctionalInterfacesExample example = new FunctionalInterfacesExample();
        List<Integer> numeros = List.of(2, 12, 20, 25, 50, 100, 150);
        
        List<Integer> resultado = example.filtrarComposto(numeros);
        
        // Pares, > 10, < 100
        assertEquals(List.of(12, 20, 50), resultado);
    }
    
    @Test
    @DisplayName("Deve converter lista")
    void converter() {
        FunctionalInterfacesExample example = new FunctionalInterfacesExample();
        List<String> strings = List.of("1", "2", "3");
        
        List<Integer> inteiros = example.converter(strings, Integer::parseInt);
        List<Double> dobros = example.converter(strings, s -> Double.parseDouble(s) * 2);
        
        assertEquals(List.of(1, 2, 3), inteiros);
        assertEquals(List.of(2.0, 4.0, 6.0), dobros);
    }
    
    @Test
    @DisplayName("Deve calcular volume usando TriFunction")
    void calcularVolume() {
        FunctionalInterfacesExample example = new FunctionalInterfacesExample();
        
        double volume = example.calcularVolume(2.0, 3.0, 4.0, (l, a, p) -> l * a * p);
        
        assertEquals(24.0, volume);
    }
}
