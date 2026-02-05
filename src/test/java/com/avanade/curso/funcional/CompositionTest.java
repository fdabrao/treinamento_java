package com.avanade.curso.funcional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.function.Predicate;

/**
 * Testes para Composição de Funções
 */
class CompositionTest {
    
    @Test
    @DisplayName("Deve filtrar compostos com AND")
    void filtrarComposto() {
        FunctionalInterfacesExample example = new FunctionalInterfacesExample();
        List<Integer> numeros = List.of(2, 12, 20, 25, 50, 100, 150);
        
        List<Integer> resultado = example.filtrarComposto(numeros);
        
        // Pares AND > 10 AND < 100
        assertEquals(List.of(12, 20, 50), resultado);
    }
    
    @Test
    @DisplayName("Deve criar validador composto")
    void criarValidador() {
        CompositionExample example = new CompositionExample();
        List<Predicate<String>> regras = List.of(
            s -> s.length() >= 5,
            s -> s.contains("@"),
            s -> s.endsWith(".com")
        );
        
        Predicate<String> validador = example.criarValidador(regras);
        
        assertTrue(validador.test("teste@email.com"));
        assertFalse(validador.test("abc"));
        assertFalse(validador.test("semarroba.com"));
    }
    
    @Test
    @DisplayName("Deve validar com múltiplas regras")
    void validar() {
        CompositionExample example = new CompositionExample();
        
        boolean resultado = example.validar("Java", 
            s -> s.length() >= 3,
            s -> s.matches("[A-Z].*"),
            s -> s.contains("a")
        );
        
        assertTrue(resultado);
    }
    
    @Test
    @DisplayName("Deve usar memoizador para fibonacci")
    void fibonacciMemoizado() {
        CompositionExample example = new CompositionExample();
        
        long fib10 = example.fibonacciMemoizado(10);
        long fib20 = example.fibonacciMemoizado(20);
        
        assertEquals(55, fib10);
        assertEquals(6765, fib20);
    }
    
    @Test
    @DisplayName("Deve calcular volume usando TriFunction")
    void calcularVolume() {
        FunctionalInterfacesExample example = new FunctionalInterfacesExample();
        
        double volume = example.calcularVolume(2.0, 3.0, 4.0, (l, a, p) -> l * a * p);
        
        assertEquals(24.0, volume);
    }
    
    @Test
    @DisplayName("Validador deve aceitar regras encadeadas")
    void validadorEncadeado() {
        CompositionExample.Validador<String> validador = new CompositionExample.Validador<String>()
            .adicionarRegra(s -> s.length() >= 8, "Mínimo 8 caracteres")
            .adicionarRegra(s -> s.matches(".*[A-Z].*"), "Pelo menos 1 maiúscula")
            .adicionarRegra(s -> s.matches(".*[0-9].*"), "Pelo menos 1 número");
        
        CompositionExample.ResultadoValidacao sucesso = validador.validar("Senha123");
        CompositionExample.ResultadoValidacao falha = validador.validar("abc");
        
        assertTrue(sucesso.valido());
        assertTrue(sucesso.erros().isEmpty());
        
        assertFalse(falha.valido());
        assertFalse(falha.erros().isEmpty());
    }
    
    @Test
    @DisplayName("Deve processar textos com pipeline")
    void processarTextos() {
        CompositionExample example = new CompositionExample();
        List<String> textos = List.of("  JAVA  ", "PROGRAMAÇÃO  ");
        
        List<String> resultado = example.processarTextos(textos);
        
        // Trim, lowercase, remove acentos, normaliza espaços, capitaliza
        assertEquals("Java", resultado.get(0));
        assertEquals("Programacao", resultado.get(1));
    }
    
    @Test
    @DisplayName("Memoizador deve cachear resultados")
    void memoizadorCache() {
        CompositionExample.Memoizador<Integer, Long> memo = new CompositionExample.Memoizador<>(n -> {
            // Simula cálculo caro
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return (long) n * n;
        });
        
        long start1 = System.currentTimeMillis();
        Long resultado1 = memo.aplicar(5);
        long tempo1 = System.currentTimeMillis() - start1;
        
        long start2 = System.currentTimeMillis();
        Long resultado2 = memo.aplicar(5);
        long tempo2 = System.currentTimeMillis() - start2;
        
        assertEquals(25L, resultado1);
        assertEquals(resultado1, resultado2);
        // Segunda chamada deve ser mais rápida (do cache)
        assertTrue(tempo2 < tempo1);
    }
}
