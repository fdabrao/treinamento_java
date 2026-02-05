package com.avanade.curso.funcional;

import java.util.*;
import java.util.function.*;

/**
 * PROGRAMAÇÃO FUNCIONAL - Lambdas
 * 
 * Expressões lambda são funções anônimas que podem ser tratadas como valores.
 * São a base da programação funcional em Java.
 * 
 * Sintaxe: (parâmetros) -> { corpo }
 */
public class LambdasExample {
    
    /**
     * Demonstra diferentes formas de escrever lambdas
     */
    public void demonstrarSintaxeLambda() {
        // Lambda completo com tipo explícito
        Predicate<String> comTipo = (String s) -> { return s.length() > 5; };
        
        // Lambda com inferência de tipo
        Predicate<String> semTipo = (s) -> { return s.length() > 5; };
        
        // Lambda sem parênteses (apenas um parâmetro)
        Predicate<String> semParenteses = s -> { return s.length() > 5; };
        
        // Lambda sem chaves (corpo de uma linha)
        Predicate<String> umaLinha = s -> s.length() > 5;
        
        // Lambda com múltiplos parâmetros
        Comparator<String> comparador = (s1, s2) -> s1.length() - s2.length();
        
        // Lambda sem parâmetros
        Runnable semParametros = () -> System.out.println("Executando!");
    }
    
    /**
     * Filtra elementos usando lambda
     */
    public List<Integer> filtrarPares(List<Integer> numeros) {
        return numeros.stream()
            .filter(n -> n % 2 == 0)  // Lambda como predicado
            .toList();
    }
    
    /**
     * Transforma elementos usando lambda
     */
    public List<String> transformarMaiusculas(List<String> textos) {
        return textos.stream()
            .map(s -> s.toUpperCase())  // Lambda como função
            .toList();
    }
    
    /**
     * Ordena usando lambda
     */
    public List<String> ordenarPorTamanho(List<String> textos) {
        List<String> copia = new ArrayList<>(textos);
        copia.sort((s1, s2) -> Integer.compare(s1.length(), s2.length()));
        return copia;
    }
    
    /**
     * Executa ação para cada elemento
     */
    public void imprimirTodos(List<String> textos) {
        textos.forEach(texto -> System.out.println(texto));
    }
    
    /**
     * Lambda que captura variável externa (closure)
     */
    public List<Integer> adicionarValor(List<Integer> numeros, int valor) {
        return numeros.stream()
            .map(n -> n + valor)  // 'valor' é capturado do escopo externo
            .toList();
    }
    
    /**
     * Lambda com múltiplas linhas
     */
    public List<String> processarTextos(List<String> textos) {
        return textos.stream()
            .map(texto -> {
                String processado = texto.trim();
                processado = processado.toLowerCase();
                return processado;
            })
            .toList();
    }
    
    /**
     * Comparação: Classe anônima vs Lambda
     */
    public void compararSintaxes() {
        // Classe anônima (verboso)
        Runnable comClasseAnonima = new Runnable() {
            @Override
            public void run() {
                System.out.println("Olá!");
            }
        };
        
        // Lambda (conciso)
        Runnable comLambda = () -> System.out.println("Olá!");
        
        // Ambos funcionam igual
        comClasseAnonima.run();
        comLambda.run();
    }
    
    /**
     * Lambda como retorno de método
     */
    public Predicate<Integer> criarValidador(int minimo) {
        return valor -> valor >= minimo;
    }
    
    /**
     * Lambda como parâmetro
     */
    public int executarOperacao(int a, int b, IntBinaryOperator operacao) {
        return operacao.applyAsInt(a, b);
    }
}
