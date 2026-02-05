package com.avanade.curso.funcional;

import java.util.*;
import java.util.stream.*;

/**
 * PROGRAMAÇÃO FUNCIONAL - Streams API
 * 
 * Streams permitem processamento declarativo de coleções.
 * Operações encadeadas: filter, map, reduce, collect, etc.
 */
public class StreamsExample {
    
    private List<Integer> numeros = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
    private List<String> nomes = List.of("Ana", "Bruno", "Carlos", "Diana", "Eduardo");
    
    /**
     * Operação intermediária: filter
     */
    public List<Integer> obterPares() {
        return numeros.stream()
            .filter(n -> n % 2 == 0)  // Mantém apenas pares
            .toList();
    }
    
    /**
     * Operação intermediária: map (transformação)
     */
    public List<Integer> obterQuadrados() {
        return numeros.stream()
            .map(n -> n * n)  // Transforma cada número em seu quadrado
            .toList();
    }
    
    /**
     * Operação intermediária: sorted
     */
    public List<String> ordenarPorTamanhoDecrescente() {
        return nomes.stream()
            .sorted(Comparator.comparingInt(String::length).reversed())
            .toList();
    }
    
    /**
     * Operação intermediária: distinct
     */
    public List<Integer> removerDuplicados(List<Integer> lista) {
        return lista.stream()
            .distinct()
            .toList();
    }
    
    /**
     * Operação intermediária: limit e skip
     */
    public List<Integer> obterDoTerceiroAoQuinto() {
        return numeros.stream()
            .skip(2)      // Pula os 2 primeiros
            .limit(3)     // Pega apenas 3 elementos
            .toList();
    }
    
    /**
     * Operação terminal: collect (para diferentes estruturas)
     */
    public Set<Integer> converterParaSet(List<Integer> lista) {
        return lista.stream()
            .collect(Collectors.toSet());
    }
    
    /**
     * Operação terminal: collect com joining
     */
    public String juntarNomes(String separador) {
        return nomes.stream()
            .collect(Collectors.joining(separador));
    }
    
    /**
     * Operação terminal: collect groupingBy
     */
    public Map<Integer, List<String>> agruparPorTamanho() {
        return nomes.stream()
            .collect(Collectors.groupingBy(String::length));
    }
    
    /**
     * Operação terminal: collect partitioningBy
     */
    public Map<Boolean, List<Integer>> separarParesEImpares() {
        return numeros.stream()
            .collect(Collectors.partitioningBy(n -> n % 2 == 0));
    }
    
    /**
     * Operação terminal: reduce (agregação)
     */
    public int somarTodos() {
        return numeros.stream()
            .reduce(0, Integer::sum);  // Identidade: 0, Acumulador: soma
    }
    
    /**
     * Reduce com valor opcional
     */
    public Optional<Integer> multiplicarTodos() {
        return numeros.stream()
            .reduce((a, b) -> a * b);
    }
    
    /**
     * Operação terminal: count
     */
    public long contarMaioresQue(int valor) {
        return numeros.stream()
            .filter(n -> n > valor)
            .count();
    }
    
    /**
     * Operação terminal: anyMatch, allMatch, noneMatch
     */
    public boolean existePar() {
        return numeros.stream().anyMatch(n -> n % 2 == 0);
    }
    
    public boolean todosPositivos() {
        return numeros.stream().allMatch(n -> n > 0);
    }
    
    public boolean nenhumNegativo() {
        return numeros.stream().noneMatch(n -> n < 0);
    }
    
    /**
     * Operação terminal: findFirst, findAny
     */
    public Optional<String> encontrarPrimeiroComMaisDe3Letras() {
        return nomes.stream()
            .filter(n -> n.length() > 3)
            .findFirst();
    }
    
    /**
     * Pipeline completo: múltiplas operações
     */
    public List<String> pipelineCompleto(List<String> palavras) {
        return palavras.stream()
            .filter(p -> p.length() > 3)           // Filtra
            .map(String::toUpperCase)              // Transforma
            .sorted()                              // Ordena
            .distinct()                            // Remove duplicados
            .limit(5)                              // Limita resultados
            .collect(Collectors.toList());         // Coleta
    }
    
    /**
     * Stream paralelo para processamento em paralelo
     */
    public long contarPrimosAte(int limite) {
        return IntStream.rangeClosed(2, limite)
            .parallel()  // Processa em paralelo
            .filter(this::ehPrimo)
            .count();
    }
    
    private boolean ehPrimo(int n) {
        if (n < 2) return false;
        for (int i = 2; i <= Math.sqrt(n); i++) {
            if (n % i == 0) return false;
        }
        return true;
    }
    
    /**
     * FlatMap para "achatar" streams aninhados
     */
    public List<Integer> obterTodosOsNumeros(List<List<Integer>> listas) {
        return listas.stream()
            .flatMap(List::stream)  // "Achata" List<List<Integer>> para Stream<Integer>
            .toList();
    }
    
    /**
     * Estatísticas com IntStream
     */
    public IntSummaryStatistics calcularEstatisticas() {
        return numeros.stream()
            .mapToInt(Integer::intValue)
            .summaryStatistics();
    }
}
