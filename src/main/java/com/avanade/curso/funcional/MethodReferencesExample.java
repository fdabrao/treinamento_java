package com.avanade.curso.funcional;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

/**
 * PROGRAMAÇÃO FUNCIONAL - Referências de Método
 * 
 * Sintaxe curta para lambdas que chamam apenas um método.
 * Tipos: estático, instância de objeto, instância de classe, construtor
 */
public class MethodReferencesExample {
    
    /**
     * 1. Referência a método estático: Classe::metodoEstatico
     */
    public void referenciaMetodoEstatico() {
        // Lambda
        Function<String, Integer> lambda = s -> Integer.parseInt(s);
        
        // Referência de método
        Function<String, Integer> referencia = Integer::parseInt;
        
        // Uso
        int numero = referencia.apply("42"); // 42
        
        // Outros exemplos
        Function<Double, Double> raiz = Math::sqrt;
        BiFunction<Integer, Integer, Integer> max = Math::max;
        Predicate<String> vazio = String::isEmpty; // método de instância usado como estático
    }
    
    /**
     * 2. Referência a método de instância de objeto específico: objeto::metodo
     */
    public void referenciaObjetoEspecifico() {
        String prefixo = "Prefixo: ";
        
        // Lambda
        Function<String, String> lambda = s -> prefixo.concat(s);
        
        // Referência de método
        Function<String, String> referencia = prefixo::concat;
        
        System.out.println(referencia.apply("Mensagem")); // "Prefixo: Mensagem"
        
        // Outro exemplo
        List<String> lista = new ArrayList<>();
        Consumer<String> adicionar = lista::add; // equivalente a s -> lista.add(s)
        adicionar.accept("Item");
    }
    
    /**
     * 3. Referência a método de instância de classe: Classe::metodoInstancia
     */
    public void referenciaClasse() {
        // Lambda
        Function<String, String> lambda = s -> s.toUpperCase();
        
        // Referência de método (String é o tipo do parâmetro)
        Function<String, String> referencia = String::toUpperCase;
        
        List<String> nomes = List.of("ana", "bruno", "carlos");
        List<String> maiusculas = nomes.stream()
            .map(String::toUpperCase)  // cada String chama toUpperCase()
            .toList();
        
        // Comparator com referência
        List<String> ordenado = nomes.stream()
            .sorted(String::compareToIgnoreCase)
            .toList();
    }
    
    /**
     * 4. Referência a construtor: Classe::new
     */
    public void referenciaConstrutor() {
        // Lambda
        Supplier<List<String>> lambda = () -> new ArrayList<>();
        
        // Referência de construtor
        Supplier<List<String>> referencia = ArrayList::new;
        
        List<String> lista = referencia.get();
        
        // Construtor com parâmetros
        Function<String, StringBuilder> criarStringBuilder = StringBuilder::new;
        StringBuilder sb = criarStringBuilder.apply("Texto inicial");
        
        // Construtor com múltiplos parâmetros - usar lambda
        BiFunction<String, Integer, String> substring = (s, inicio) -> s.substring(inicio);
    }
    
    /**
     * Exemplos práticos combinados
     */
    public void exemplosPraticos() {
        List<String> palavras = Arrays.asList("java", "programação", "funcional", "streams");
        
        // Ordenação natural
        palavras.sort(String::compareTo);
        
        // Imprimir cada elemento
        palavras.forEach(System.out::println);
        
        // Converter para maiúsculas
        List<String> maiusculas = palavras.stream()
            .map(String::toUpperCase)
            .toList();
        
        // Obter tamanhos
        List<Integer> tamanhos = palavras.stream()
            .map(String::length)
            .toList();
    }
    
    /**
     * Exemplo com classe customizada
     */
    public static class Produto {
        private String nome;
        private double preco;
        
        // Construtor padrão
        public Produto() {}
        
        // Construtor com nome
        public Produto(String nome) {
            this.nome = nome;
        }
        
        // Construtor completo
        public Produto(String nome, double preco) {
            this.nome = nome;
            this.preco = preco;
        }
        
        public String getNome() { return nome; }
        public void setNome(String nome) { this.nome = nome; }
        public double getPreco() { return preco; }
        public void setPreco(double preco) { this.preco = preco; }
        
        public boolean estaCaro() {
            return preco > 1000;
        }
        
        public double aplicarDesconto(double percentual) {
            return preco * (1 - percentual / 100);
        }
        
        @Override
        public String toString() {
            return nome + " - R$ " + preco;
        }
    }
    
    public void exemplosComProduto() {
        List<String> nomes = List.of("Notebook", "Mouse", "Teclado");
        
        // Construtor com um parâmetro
        List<Produto> produtos = nomes.stream()
            .map(Produto::new)  // String nome -> new Produto(nome)
            .toList();
        
        // Ordenar por nome
        produtos.sort(Comparator.comparing(Produto::getNome));
        
        // Filtrar caros
        List<Produto> caros = produtos.stream()
            .filter(Produto::estaCaro)  // p -> p.estaCaro()
            .toList();
        
        // Obter preços
        List<Double> precos = produtos.stream()
            .map(Produto::getPreco)
            .toList();
    }
    
    /**
     * Referências em coleções
     */
    public void referenciasEmColecoes() {
        List<Integer> numeros = List.of(1, 2, 3, 4, 5);
        
        // Redução
        int soma = numeros.stream()
            .reduce(0, Integer::sum);  // (a, b) -> Integer.sum(a, b)
        
        // Máximo
        Optional<Integer> max = numeros.stream()
            .max(Integer::compareTo);
        
        // Mínimo
        Optional<Integer> min = numeros.stream()
            .min(Integer::compareTo);
        
        // Concatenar strings
        List<String> strings = List.of("A", "B", "C");
        String resultado = strings.stream()
            .reduce("", String::concat);
    }
    
    /**
     * Comparativo: Lambda vs Referência
     */
    public void comparativo() {
        List<String> lista = List.of("zebra", "águia", "banana");
        
        // Com lambda
        lista.stream()
            .map(s -> s.length())
            .filter(n -> n > 5)
            .sorted((a, b) -> Integer.compare(a, b))
            .forEach(s -> System.out.println(s));
        
        // Com referências de método (mais limpo)
        lista.stream()
            .map(String::length)
            .filter(n -> n > 5)
            .sorted(Integer::compareTo)
            .forEach(System.out::println);
    }
}
