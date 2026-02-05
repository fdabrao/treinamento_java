package com.avanade.curso.funcional;

import java.util.*;
import java.util.function.*;

/**
 * PROGRAMAÇÃO FUNCIONAL - Composição de Funções
 * 
 * Combinar funções simples para criar funções complexas.
 * Funções são blocos de construção que se encaixam.
 */
public class CompositionExample {
    
    /**
     * Composição com Function.compose e Function.andThen
     */
    public void demonstrarComposicao() {
        Function<Integer, Integer> dobrar = x -> x * 2;
        Function<Integer, Integer> adicionar10 = x -> x + 10;
        Function<Integer, String> paraString = x -> "Resultado: " + x;
        
        // andThen: executa na ordem (primeiro this, depois argumento)
        Function<Integer, Integer> dobrarEDepoisAdicionar = dobrar.andThen(adicionar10);
        // (5 * 2) + 10 = 20
        System.out.println(dobrarEDepoisAdicionar.apply(5)); // 20
        
        // compose: executa na ordem reversa (primeiro argumento, depois this)
        Function<Integer, Integer> adicionarEDepoisDobrar = dobrar.compose(adicionar10);
        // (5 + 10) * 2 = 30
        System.out.println(adicionarEDepoisDobrar.apply(5)); // 30
        
        // Composição em cadeia
        Function<Integer, String> pipeline = dobrar
            .andThen(adicionar10)
            .andThen(paraString);
        System.out.println(pipeline.apply(5)); // "Resultado: 20"
    }
    
    /**
     * Pipeline de processamento de dados
     */
    public List<String> processarTextos(List<String> textos) {
        Function<String, String> pipeline = ((Function<String, String>) String::trim)
            .andThen(String::toLowerCase)
            .andThen(this::removerAcentos)
            .andThen(s -> s.replaceAll("\\s+", " "))  // normaliza espaços
            .andThen(this::capitalizar);
        
        return textos.stream()
            .map(pipeline)
            .toList();
    }
    
    private String removerAcentos(String s) {
        return s.replaceAll("[áàãâä]", "a")
                .replaceAll("[éèêë]", "e")
                .replaceAll("[íìîï]", "i")
                .replaceAll("[óòõôö]", "o")
                .replaceAll("[úùûü]", "u")
                .replaceAll("ç", "c");
    }
    
    private String capitalizar(String s) {
        if (s.isEmpty()) return s;
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }
    
    /**
     * Validador composto usando Predicate
     */
    public <T> Predicate<T> criarValidador(List<Predicate<T>> regras) {
        return regras.stream()
            .reduce(Predicate::and)
            .orElse(x -> true);  // Se não houver regras, tudo é válido
    }
    
    public <T> boolean validar(T objeto, Predicate<T>... regras) {
        Predicate<T> validador = Arrays.stream(regras)
            .reduce(Predicate::and)
            .orElse(x -> true);
        return validador.test(objeto);
    }
    
    /**
     * Builder de processamento funcional
     */
    public static class ProcessadorBuilder<T, R> {
        private List<Function<Object, Object>> passos = new ArrayList<>();
        
        @SuppressWarnings("unchecked")
        public <U> ProcessadorBuilder<T, U> adicionar(Function<R, U> passo) {
            passos.add((Function<Object, Object>) passo);
            return (ProcessadorBuilder<T, U>) this;
        }
        
        @SuppressWarnings("unchecked")
        public Function<T, R> build() {
            return (T entrada) -> {
                Object resultado = entrada;
                for (Function<Object, Object> passo : passos) {
                    resultado = passo.apply(resultado);
                }
                return (R) resultado;
            };
        }
    }
    
    /**
     * Uso do builder
     */
    public void demonstrarBuilder() {
        Function<String, Integer> processador = new ProcessadorBuilder<String, String>()
            .adicionar((Function<String, String>) s -> s.trim())
            .<Integer>adicionar(String::length)
            .<Integer>adicionar(n -> n * 2)
            .build();
        
        System.out.println(processador.apply("  hello  ")); // 10 (5 * 2)
    }
    
    /**
     * Currying: transformar função de múltiplos parâmetros em sequência de funções
     */
    public void demonstrarCurrying() {
        // Função normal: (a, b, c) -> resultado
        // Curried: a -> (b -> (c -> resultado))
        
        // Imposto: valor * taxa - deducao
        Function<Double, Function<Double, Function<Double, Double>>> calcularImposto = 
            valor -> taxa -> deducao -> valor * taxa - deducao;
        
        // Uso parcial
        Function<Double, Function<Double, Double>> impostoRenda = calcularImposto.apply(50000.0);
        Function<Double, Double> calculo2024 = impostoRenda.apply(0.275); // 27.5% de taxa
        
        double imposto = calculo2024.apply(5000.0); // com dedução de 5000
        System.out.println("Imposto: " + imposto); // 50000 * 0.275 - 5000 = 8750
    }
    
    /**
     * Memoization: cache de resultados de funções puras
     */
    public static class Memoizador<T, R> {
        private final Function<T, R> funcao;
        private final Map<T, R> cache = new HashMap<>();
        
        public Memoizador(Function<T, R> funcao) {
            this.funcao = funcao;
        }
        
        public R aplicar(T entrada) {
            return cache.computeIfAbsent(entrada, funcao);
        }
    }
    
    /**
     * Fibonacci com memoization
     */
    public long fibonacci(int n) {
        if (n <= 1) return n;
        return fibonacci(n - 1) + fibonacci(n - 2);
    }
    
    public long fibonacciMemoizado(int n) {
        Memoizador<Integer, Long> memo = new Memoizador<>(this::calcularFib);
        return memo.aplicar(n);
    }
    
    private long calcularFib(int n) {
        if (n <= 1) return n;
        return fibonacciMemoizado(n - 1) + fibonacciMemoizado(n - 2);
    }
    
    /**
     * Partial Application: fixar alguns parâmetros
     */
    public void demonstrarPartialApplication() {
        // Função original: (taxa, valor) -> valor * taxa
        BiFunction<Double, Double, Double> calcularJuros = (taxa, valor) -> valor * taxa;
        
        // Fixar taxa em 10%
        Function<Double, Double> juros10Porcento = valor -> calcularJuros.apply(0.10, valor);
        
        System.out.println(juros10Porcento.apply(1000.0)); // 100.0
        System.out.println(juros10Porcento.apply(5000.0)); // 500.0
    }
    
    /**
     * Pipeline de validação
     */
    public static class Validador<T> {
        private final List<Predicate<T>> regras = new ArrayList<>();
        private final List<String> mensagens = new ArrayList<>();
        
        public Validador<T> adicionarRegra(Predicate<T> regra, String mensagem) {
            regras.add(regra);
            mensagens.add(mensagem);
            return this;
        }
        
        public ResultadoValidacao validar(T objeto) {
            List<String> erros = new ArrayList<>();
            for (int i = 0; i < regras.size(); i++) {
                if (!regras.get(i).test(objeto)) {
                    erros.add(mensagens.get(i));
                }
            }
            return new ResultadoValidacao(erros.isEmpty(), erros);
        }
    }
    
    public record ResultadoValidacao(boolean valido, List<String> erros) {}
    
    /**
     * Uso do validador
     */
    public void demonstrarValidador() {
        Validador<String> validadorSenha = new Validador<String>()
            .adicionarRegra(s -> s.length() >= 8, "Mínimo 8 caracteres")
            .adicionarRegra(s -> s.matches(".*[A-Z].*"), "Pelo menos 1 maiúscula")
            .adicionarRegra(s -> s.matches(".*[0-9].*"), "Pelo menos 1 número")
            .adicionarRegra(s -> s.matches(".*[!@#$%].*"), "Pelo menos 1 especial");
        
        ResultadoValidacao resultado = validadorSenha.validar("Senha123!");
        System.out.println("Válido: " + resultado.valido());
    }
}
