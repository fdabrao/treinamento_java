package com.avanade.curso.funcional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * PROGRAMAÇÃO FUNCIONAL - Imutabilidade
 * 
 * Objetos imutáveis não podem ser modificados após criação.
 * São thread-safe e previsíveis.
 */
public class ImmutabilityExample {
    
    /**
     * Classe imutável - todos os campos são final e privados
     */
    public static final class PessoaImutavel {
        private final String nome;
        private final int idade;
        private final List<String> emails;  // Coleção imutável
        private final EnderecoImutavel endereco;  // Objeto imutável
        
        public PessoaImutavel(String nome, int idade, List<String> emails, EnderecoImutavel endereco) {
            this.nome = nome;
            this.idade = idade;
            // Defesa contra mutação externa - cópia defensiva
            this.emails = List.copyOf(emails);
            this.endereco = endereco;
        }
        
        // Apenas getters, nenhum setter
        public String getNome() { return nome; }
        public int getIdade() { return idade; }
        public List<String> getEmails() { return emails; }  // Já é imutável
        public EnderecoImutavel getEndereco() { return endereco; }
        
        // "Modificação" retorna nova instância (padrão Builder-like)
        public PessoaImutavel comNome(String novoNome) {
            return new PessoaImutavel(novoNome, this.idade, this.emails, this.endereco);
        }
        
        public PessoaImutavel comIdade(int novaIdade) {
            return new PessoaImutavel(this.nome, novaIdade, this.emails, this.endereco);
        }
        
        public PessoaImutavel adicionarEmail(String email) {
            List<String> novosEmails = new ArrayList<>(this.emails);
            novosEmails.add(email);
            return new PessoaImutavel(this.nome, this.idade, novosEmails, this.endereco);
        }
    }
    
    /**
     * Outra classe imutável
     */
    public static final class EnderecoImutavel {
        private final String rua;
        private final String cidade;
        private final String cep;
        
        public EnderecoImutavel(String rua, String cidade, String cep) {
            this.rua = rua;
            this.cidade = cidade;
            this.cep = cep;
        }
        
        public String getRua() { return rua; }
        public String getCidade() { return cidade; }
        public String getCep() { return cep; }
        
        public EnderecoImutavel comCidade(String novaCidade) {
            return new EnderecoImutavel(this.rua, novaCidade, this.cep);
        }
    }
    
    /**
     * Coleções imutáveis com Java 9+
     */
    public void demonstrarColecoesImutaveis() {
        // List imutável
        List<String> listaImutavel = List.of("A", "B", "C");
        
        // Set imutável
        Set<Integer> setImutavel = Set.of(1, 2, 3);
        
        // Map imutável
        Map<String, Integer> mapImutavel = Map.of(
            "um", 1,
            "dois", 2,
            "tres", 3
        );
        
        // Map imutável com mais de 10 entradas
        Map<String, String> mapMaior = Map.ofEntries(
            Map.entry("a", "1"),
            Map.entry("b", "2"),
            Map.entry("c", "3")
        );
    }
    
    /**
     * Transformação funcional preservando imutabilidade
     */
    public List<Integer> dobrarValores(List<Integer> original) {
        // Cria nova lista, não modifica a original
        return original.stream()
            .map(n -> n * 2)
            .collect(Collectors.toUnmodifiableList());
    }
    
    /**
     * Cópia defensiva para garantir imutabilidade
     */
    public List<String> criarCopiaDefensiva(List<String> original) {
        // Retorna cópia imutável
        return List.copyOf(original);
    }
    
    /**
     * Comparativo: mutável vs imutável
     */
    public void compararAbordagens() {
        // Abordagem mutável (problemática)
        List<String> listaMutavel = new ArrayList<>();
        listaMutavel.add("A");
        listaMutavel.add("B");
        // Qualquer código pode modificar esta lista
        
        // Abordagem imutável (segura)
        List<String> listaImutavel = List.of("A", "B");
        // listaImutavel.add("C"); // Lança UnsupportedOperationException
        
        // Para "modificar", criamos nova instância
        List<String> novaLista = new ArrayList<>(listaImutavel);
        novaLista.add("C");
        List<String> novaImutavel = List.copyOf(novaLista);
    }
    
    /**
     * Thread-safety com imutabilidade
     */
    public void demonstrarThreadSafety() throws InterruptedException {
        PessoaImutavel pessoa = new PessoaImutavel(
            "João", 
            30, 
            List.of("joao@email.com"),
            new EnderecoImutavel("Rua A", "São Paulo", "01000-000")
        );
        
        // Múltiplas threads podem acessar simultaneamente sem locks
        Runnable tarefa = () -> {
            System.out.println(pessoa.getNome());
            // pessoa.setNome("Maria"); // Não compila! Não existe setter
        };
        
        Thread t1 = new Thread(tarefa);
        Thread t2 = new Thread(tarefa);
        t1.start();
        t2.start();
        t1.join();
        t2.join();
    }
    
    /**
     * Record (Java 16+) - classes imutáveis automáticas
     */
    public record Produto(String nome, double preco, String categoria) {
        // Construtor compacto para validação
        public Produto {
            if (preco < 0) {
                throw new IllegalArgumentException("Preço não pode ser negativo");
            }
            if (nome == null || nome.isBlank()) {
                throw new IllegalArgumentException("Nome não pode ser vazio");
            }
        }
        
        // Métodos adicionais são permitidos
        public double precoComDesconto(double percentual) {
            return preco * (1 - percentual / 100);
        }
        
        public Produto comDesconto(double percentual) {
            return new Produto(nome, precoComDesconto(percentual), categoria);
        }
    }
    
    /**
     * Uso de Records
     */
    public void demonstrarRecords() {
        Produto produto = new Produto("Notebook", 5000.0, "Eletrônicos");
        
        // Acesso aos componentes
        System.out.println(produto.nome());
        System.out.println(produto.preco());
        
        // toString, equals, hashCode automáticos
        Produto outro = new Produto("Notebook", 5000.0, "Eletrônicos");
        System.out.println(produto.equals(outro)); // true
        
        // Criar cópia modificada
        Produto comDesconto = produto.comDesconto(10);
        System.out.println(comDesconto.preco()); // 4500.0
    }
}
