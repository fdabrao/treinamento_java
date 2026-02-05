package com.avanade.curso.funcional;

import java.util.*;
import java.util.function.*;

/**
 * PROGRAMAÇÃO FUNCIONAL - Interfaces Funcionais
 * 
 * Interfaces com apenas um método abstrato.
 * Java fornece várias interfaces funcionais built-in em java.util.function
 */
public class FunctionalInterfacesExample {
    
    /**
     * Predicate<T> - recebe T, retorna boolean
     * Usado para testes/filtros
     */
    public List<Integer> filtrar(List<Integer> numeros, Predicate<Integer> condicao) {
        return numeros.stream()
            .filter(condicao)
            .toList();
    }
    
    /**
     * Function<T, R> - recebe T, retorna R
     * Usado para transformações
     */
    public List<String> transformar(List<Integer> numeros, Function<Integer, String> transformacao) {
        return numeros.stream()
            .map(transformacao)
            .toList();
    }
    
    /**
     * Consumer<T> - recebe T, retorna void
     * Usado para ações/efeitos colaterais
     */
    public void processar(List<String> itens, Consumer<String> acao) {
        itens.forEach(acao);
    }
    
    /**
     * Supplier<T> - não recebe nada, retorna T
     * Usado para fornecer/gerar valores
     */
    public String obterOuPadrao(String valor, Supplier<String> padrao) {
        return valor != null ? valor : padrao.get();
    }
    
    /**
     * UnaryOperator<T> - recebe T, retorna T (mesmo tipo)
     * Especialização de Function
     */
    public List<Integer> aplicarOperacao(List<Integer> numeros, UnaryOperator<Integer> operacao) {
        return numeros.stream()
            .map(operacao)
            .toList();
    }
    
    /**
     * BinaryOperator<T> - recebe (T, T), retorna T
     * Usado para reduções/combinações
     */
    public int reduzir(List<Integer> numeros, int inicial, BinaryOperator<Integer> operacao) {
        return numeros.stream()
            .reduce(inicial, operacao);
    }
    
    /**
     * BiFunction<T, U, R> - recebe (T, U), retorna R
     */
    public List<String> combinar(List<String> nomes, List<Integer> idades, 
                                  BiFunction<String, Integer, String> combinador) {
        List<String> resultado = new ArrayList<>();
        for (int i = 0; i < Math.min(nomes.size(), idades.size()); i++) {
            resultado.add(combinador.apply(nomes.get(i), idades.get(i)));
        }
        return resultado;
    }
    
    /**
     * BiPredicate<T, U> - recebe (T, U), retorna boolean
     */
    public boolean testarPar(String nome, int idade, BiPredicate<String, Integer> teste) {
        return teste.test(nome, idade);
    }
    
    /**
     * BiConsumer<T, U> - recebe (T, U), retorna void
     */
    public void processarPares(List<String> chaves, List<Integer> valores, 
                                BiConsumer<String, Integer> acao) {
        for (int i = 0; i < Math.min(chaves.size(), valores.size()); i++) {
            acao.accept(chaves.get(i), valores.get(i));
        }
    }
    
    /**
     * Interfaces primitivas - evitam autoboxing
     */
    public double calcularMedia(int[] numeros) {
        return Arrays.stream(numeros)
            .average()
            .orElse(0.0);
    }
    
    public long contarMaioresQue(long[] numeros, long limite) {
        return Arrays.stream(numeros)
            .filter(n -> n > limite)
            .count();
    }
    
    /**
     * Composição de Predicates
     */
    public List<Integer> filtrarComposto(List<Integer> numeros) {
        Predicate<Integer> ehPar = n -> n % 2 == 0;
        Predicate<Integer> maiorQue10 = n -> n > 10;
        Predicate<Integer> menorQue100 = n -> n < 100;
        
        // Compõe com AND
        Predicate<Integer> composto = ehPar.and(maiorQue10).and(menorQue100);
        
        return numeros.stream()
            .filter(composto)
            .toList();
    }
    
    /**
     * Exemplo prático: Validador flexível
     */
    public <T> List<String> validar(T objeto, List<Predicate<T>> regras, List<String> mensagens) {
        List<String> erros = new ArrayList<>();
        for (int i = 0; i < regras.size(); i++) {
            if (!regras.get(i).test(objeto)) {
                erros.add(mensagens.get(i));
            }
        }
        return erros;
    }
    
    /**
     * Exemplo prático: Conversor genérico
     */
    public <T, R> List<R> converter(List<T> entrada, Function<T, R> conversor) {
        return entrada.stream()
            .map(conversor)
            .toList();
    }
    
    /**
     * Interface funcional customizada
     */
    @FunctionalInterface
    public interface TriFunction<A, B, C, R> {
        R apply(A a, B b, C c);
    }
    
    public double calcularVolume(double largura, double altura, double profundidade, 
                                  TriFunction<Double, Double, Double, Double> formula) {
        return formula.apply(largura, altura, profundidade);
    }
}
