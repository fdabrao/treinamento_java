package com.avanade.curso.funcional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import java.util.function.*;

/**
 * Testes para Referências de Método
 */
class MethodReferencesTest {
    
    @Test
    @DisplayName("Deve usar referência a método estático")
    void referenciaMetodoEstatico() {
        // Integer::parseInt
        Function<String, Integer> parser = Integer::parseInt;
        
        assertEquals(42, parser.apply("42"));
        assertEquals(-10, parser.apply("-10"));
    }
    
    @Test
    @DisplayName("Deve usar referência a método de instância de objeto")
    void referenciaObjetoEspecifico() {
        String prefixo = "Olá, ";
        Function<String, String> saudacao = prefixo::concat;
        
        assertEquals("Olá, Mundo", saudacao.apply("Mundo"));
    }
    
    @Test
    @DisplayName("Deve usar referência a método de classe")
    void referenciaClasse() {
        List<String> nomes = new ArrayList<>(List.of("ana", "bruno", "carlos"));
        
        // String::toUpperCase aplicado a cada elemento
        List<String> maiusculas = nomes.stream()
            .map(String::toUpperCase)
            .toList();
        
        assertEquals(List.of("ANA", "BRUNO", "CARLOS"), maiusculas);
    }
    
    @Test
    @DisplayName("Deve usar referência a construtor")
    void referenciaConstrutor() {
        Supplier<List<String>> factory = ArrayList::new;
        Function<String, StringBuilder> builder = StringBuilder::new;
        
        List<String> lista = factory.get();
        StringBuilder sb = builder.apply("Texto");
        
        assertNotNull(lista);
        assertEquals("Texto", sb.toString());
    }
    
    @Test
    @DisplayName("Deve ordenar com referência de método")
    void ordenarComReferencia() {
        List<String> palavras = new ArrayList<>(List.of("zebra", "aguia", "banana"));
        
        palavras.sort(String::compareToIgnoreCase);
        
        assertEquals("aguia", palavras.get(0));
        assertEquals("zebra", palavras.get(2));
    }
    
    @Test
    @DisplayName("Deve reduzir com referência de método")
    void reduzirComReferencia() {
        List<Integer> numeros = List.of(1, 2, 3, 4, 5);
        
        int soma = numeros.stream()
            .reduce(0, Integer::sum);
        
        assertEquals(15, soma);
    }
    
    @Test
    @DisplayName("Deve usar max e min com referência")
    void maxMinComReferencia() {
        List<Integer> numeros = List.of(5, 2, 8, 1, 9);
        
        Optional<Integer> max = numeros.stream().max(Integer::compareTo);
        Optional<Integer> min = numeros.stream().min(Integer::compareTo);
        
        assertTrue(max.isPresent());
        assertTrue(min.isPresent());
        assertEquals(9, max.get());
        assertEquals(1, min.get());
    }
    
    @Test
    @DisplayName("Deve concatenar strings com referência")
    void concatenarStrings() {
        List<String> strings = List.of("A", "B", "C");
        
        String resultado = strings.stream()
            .reduce("", String::concat);
        
        assertEquals("ABC", resultado);
    }
    
    @Test
    @DisplayName("Deve criar produtos com construtor")
    void criarProdutos() {
        List<String> nomes = List.of("Notebook", "Mouse", "Teclado");
        
        List<MethodReferencesExample.Produto> produtos = nomes.stream()
            .map(MethodReferencesExample.Produto::new)
            .toList();
        
        assertEquals(3, produtos.size());
        assertEquals("Notebook", produtos.get(0).getNome());
    }
    
    @Test
    @DisplayName("Deve ordenar produtos por nome")
    void ordenarProdutos() {
        List<MethodReferencesExample.Produto> produtos = new ArrayList<>();
        produtos.add(new MethodReferencesExample.Produto("Mouse"));
        produtos.add(new MethodReferencesExample.Produto("Teclado"));
        produtos.add(new MethodReferencesExample.Produto("Monitor"));
        
        produtos.sort(Comparator.comparing(MethodReferencesExample.Produto::getNome));
        
        assertEquals("Monitor", produtos.get(0).getNome());
        assertEquals("Mouse", produtos.get(1).getNome());
        assertEquals("Teclado", produtos.get(2).getNome());
    }
    
    @Test
    @DisplayName("Deve filtrar produtos caros")
    void filtrarProdutosCaros() {
        List<MethodReferencesExample.Produto> produtos = new ArrayList<>();
        MethodReferencesExample.Produto caro = new MethodReferencesExample.Produto("Notebook", 5000.0);
        MethodReferencesExample.Produto barato = new MethodReferencesExample.Produto("Mouse", 50.0);
        produtos.add(caro);
        produtos.add(barato);
        
        List<MethodReferencesExample.Produto> caros = produtos.stream()
            .filter(MethodReferencesExample.Produto::estaCaro)
            .toList();
        
        assertEquals(1, caros.size());
        assertEquals("Notebook", caros.get(0).getNome());
    }
    
    @Test
    @DisplayName("Deve obter preços dos produtos")
    void obterPrecos() {
        List<MethodReferencesExample.Produto> produtos = List.of(
            new MethodReferencesExample.Produto("A", 100.0),
            new MethodReferencesExample.Produto("B", 200.0),
            new MethodReferencesExample.Produto("C", 300.0)
        );
        
        List<Double> precos = produtos.stream()
            .map(MethodReferencesExample.Produto::getPreco)
            .toList();
        
        assertEquals(List.of(100.0, 200.0, 300.0), precos);
    }
}
