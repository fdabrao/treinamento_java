package com.avanade.curso.funcional;

import java.util.Optional;

/**
 * PROGRAMAÇÃO FUNCIONAL - Optional
 * 
 * Optional é um container que pode ou não conter um valor.
 * Evita NullPointerException e null checks explícitos.
 */
public class OptionalExample {
    
    /**
     * Criando Optionals
     */
    public void criarOptionals() {
        // De um valor não-nulo
        Optional<String> presente = Optional.of("Valor");
        
        // De um valor que pode ser nulo
        String possivelNulo = null;
        Optional<String> vazio = Optional.ofNullable(possivelNulo);
        
        // Vazio explicitamente
        Optional<String> explicitamenteVazio = Optional.empty();
    }
    
    /**
     * Verificando presença de valor
     */
    public boolean verificarPresenca(Optional<String> opt) {
        return opt.isPresent();  // true se tem valor
    }
    
    public boolean verificarVazio(Optional<String> opt) {
        return opt.isEmpty();    // true se está vazio (Java 11+)
    }
    
    /**
     * Obtendo o valor
     */
    public String obterValorOuException(Optional<String> opt) {
        return opt.orElseThrow(() -> new IllegalArgumentException("Valor não presente"));
    }
    
    public String obterValorOuPadrao(Optional<String> opt) {
        return opt.orElse("Valor padrão");
    }
    
    public String obterValorOuCalcular(Optional<String> opt) {
        return opt.orElseGet(() -> calcularValorPadrao());
    }
    
    private String calcularValorPadrao() {
        return "Valor calculado";
    }
    
    /**
     * Executando ação se presente
     */
    public void executarSePresente(Optional<String> opt) {
        opt.ifPresent(valor -> System.out.println("Valor: " + valor));
    }
    
    /**
     * Executando ação se presente ou ausente
     */
    public void executarSePresenteOuAusente(Optional<String> opt) {
        opt.ifPresentOrElse(
            valor -> System.out.println("Presente: " + valor),
            () -> System.out.println("Ausente")
        );
    }
    
    /**
     * Transformando Optional (map)
     */
    public Optional<Integer> obterTamanho(Optional<String> opt) {
        return opt.map(String::length);
    }
    
    /**
     * Encadeando Optionals (flatMap)
     */
    public Optional<String> obterMaiusculas(Optional<Optional<String>> optAninhado) {
        return optAninhado.flatMap(opt -> opt)
                         .map(String::toUpperCase);
    }
    
    /**
     * Filtrando Optional
     */
    public Optional<String> filtrarPorTamanho(Optional<String> opt, int minimo) {
        return opt.filter(s -> s.length() >= minimo);
    }
    
    /**
     * Evitando null com Optional em buscas
     */
    public Optional<Usuario> buscarUsuarioPorId(int id) {
        if (id <= 0) {
            return Optional.empty();
        }
        // Simula busca no banco
        return Optional.of(new Usuario(id, "Usuário " + id));
    }
    
    /**
     * Encadeando operações
     */
    public String obterNomeMaiusculoOuPadrao(Optional<Usuario> usuarioOpt) {
        return usuarioOpt
            .map(Usuario::getNome)
            .filter(nome -> !nome.isEmpty())
            .map(String::toUpperCase)
            .orElse("DESCONHECIDO");
    }
    
    /**
     * Convertendo nullable para Optional
     */
    public Optional<String> processarValor(String valor) {
        return Optional.ofNullable(valor)
            .filter(s -> !s.trim().isEmpty())
            .map(String::trim);
    }
    
    /**
     * Stream de Optional
     */
    public java.util.List<String> removerVazios(java.util.List<Optional<String>> optionals) {
        return optionals.stream()
            .flatMap(Optional::stream)  // Java 9+: converte Optional em Stream
            .toList();
    }
    
    // Classe interna para exemplo
    public static class Usuario {
        private final int id;
        private final String nome;
        
        public Usuario(int id, String nome) {
            this.id = id;
            this.nome = nome;
        }
        
        public int getId() { return id; }
        public String getNome() { return nome; }
    }
}
