package com.avanade.curso.funcional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

/**
 * Testes para Streams
 */
class StreamsTest {
    
    @Test
    @DisplayName("Deve obter apenas números pares")
    void obterPares() {
        StreamsExample example = new StreamsExample();
        
        List<Integer> pares = example.obterPares();
        
        assertEquals(List.of(2, 4, 6, 8, 10), pares);
    }
    
    @Test
    @DisplayName("Deve calcular quadrados dos números")
    void obterQuadrados() {
        StreamsExample example = new StreamsExample();
        
        List<Integer> quadrados = example.obterQuadrados();
        
        assertEquals(List.of(1, 4, 9, 16, 25, 36, 49, 64, 81, 100), quadrados);
    }
    
    @Test
    @DisplayName("Deve ordenar por tamanho decrescente")
    void ordenarPorTamanhoDecrescente() {
        StreamsExample example = new StreamsExample();
        
        List<String> ordenado = example.ordenarPorTamanhoDecrescente();
        
        assertEquals("Eduardo", ordenado.get(0));
        assertEquals("Ana", ordenado.get(4));
    }
    
    @Test
    @DisplayName("Deve remover duplicados")
    void removerDuplicados() {
        StreamsExample example = new StreamsExample();
        List<Integer> comDuplicados = List.of(1, 2, 2, 3, 3, 3);
        
        List<Integer> resultado = example.removerDuplicados(comDuplicados);
        
        assertEquals(List.of(1, 2, 3), resultado);
    }
    
    @Test
    @DisplayName("Deve obter do terceiro ao quinto elemento")
    void obterDoTerceiroAoQuinto() {
        StreamsExample example = new StreamsExample();
        
        List<Integer> resultado = example.obterDoTerceiroAoQuinto();
        
        assertEquals(List.of(3, 4, 5), resultado);
    }
    
    @Test
    @DisplayName("Deve juntar nomes com separador")
    void juntarNomes() {
        StreamsExample example = new StreamsExample();
        
        String resultado = example.juntarNomes(", ");
        
        assertEquals("Ana, Bruno, Carlos, Diana, Eduardo", resultado);
    }
    
    @Test
    @DisplayName("Deve somar todos os números")
    void somarTodos() {
        StreamsExample example = new StreamsExample();
        
        int soma = example.somarTodos();
        
        assertEquals(55, soma); // 1+2+3+4+5+6+7+8+9+10
    }
    
    @Test
    @DisplayName("Deve multiplicar todos os números")
    void multiplicarTodos() {
        StreamsExample example = new StreamsExample();
        
        Optional<Integer> produto = example.multiplicarTodos();
        
        assertTrue(produto.isPresent());
        assertEquals(3628800, produto.get()); // 10!
    }
    
    @Test
    @DisplayName("Deve contar números maiores que valor")
    void contarMaioresQue() {
        StreamsExample example = new StreamsExample();
        
        long contagem = example.contarMaioresQue(5);
        
        assertEquals(5, contagem); // 6, 7, 8, 9, 10
    }
    
    @Test
    @DisplayName("Deve verificar se existe número par")
    void existePar() {
        StreamsExample example = new StreamsExample();
        
        assertTrue(example.existePar());
    }
    
    @Test
    @DisplayName("Deve verificar se todos são positivos")
    void todosPositivos() {
        StreamsExample example = new StreamsExample();
        
        assertTrue(example.todosPositivos());
    }
    
    @Test
    @DisplayName("Deve verificar se nenhum é negativo")
    void nenhumNegativo() {
        StreamsExample example = new StreamsExample();
        
        assertTrue(example.nenhumNegativo());
    }
    
    @Test
    @DisplayName("Deve encontrar primeiro nome com mais de 3 letras")
    void encontrarPrimeiroComMaisDe3Letras() {
        StreamsExample example = new StreamsExample();
        
        Optional<String> resultado = example.encontrarPrimeiroComMaisDe3Letras();
        
        assertTrue(resultado.isPresent());
        assertEquals("Bruno", resultado.get());
    }
    
    @Test
    @DisplayName("Deve achar todos os números de listas aninhadas")
    void obterTodosOsNumeros() {
        StreamsExample example = new StreamsExample();
        List<List<Integer>> listas = List.of(
            List.of(1, 2),
            List.of(3, 4),
            List.of(5, 6)
        );
        
        List<Integer> resultado = example.obterTodosOsNumeros(listas);
        
        assertEquals(List.of(1, 2, 3, 4, 5, 6), resultado);
    }
    
    @Test
    @DisplayName("Deve calcular estatísticas")
    void calcularEstatisticas() {
        StreamsExample example = new StreamsExample();
        
        IntSummaryStatistics stats = example.calcularEstatisticas();
        
        assertEquals(1, stats.getMin());
        assertEquals(10, stats.getMax());
        assertEquals(55, stats.getSum());
        assertEquals(10, stats.getCount());
        assertEquals(5.5, stats.getAverage());
    }
    
    @Test
    @DisplayName("Pipeline deve processar palavras corretamente")
    void pipelineCompleto() {
        StreamsExample example = new StreamsExample();
        List<String> palavras = List.of("java", "programação", "oi", "funcional", "streams", "oi");
        
        List<String> resultado = example.pipelineCompleto(palavras);
        
        // Filtra > 3, maiúsculas, ordena, remove duplicados, limita 5
        assertTrue(resultado.contains("JAVA"));
        assertTrue(resultado.contains("FUNCIONAL"));
        assertFalse(resultado.contains("OI")); // Menor ou igual a 3
        // "java", "programação", "funcional", "streams" = 4 itens ("oi" é filtrado)
        assertEquals(4, resultado.size());
    }
}
