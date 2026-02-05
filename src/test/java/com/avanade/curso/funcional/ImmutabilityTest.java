package com.avanade.curso.funcional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

/**
 * Testes para Imutabilidade
 */
class ImmutabilityTest {
    
    @Test
    @DisplayName("Deve criar pessoa imutável")
    void criarPessoaImutavel() {
        List<String> emails = new ArrayList<>();
        emails.add("joao@email.com");
        ImmutabilityExample.EnderecoImutavel endereco = new ImmutabilityExample.EnderecoImutavel(
            "Rua A", "São Paulo", "01000-000"
        );
        
        ImmutabilityExample.PessoaImutavel pessoa = new ImmutabilityExample.PessoaImutavel(
            "João", 30, emails, endereco
        );
        
        assertEquals("João", pessoa.getNome());
        assertEquals(30, pessoa.getIdade());
        assertEquals(1, pessoa.getEmails().size());
        
        // Tentar modificar a lista original não afeta a pessoa
        emails.add("outro@email.com");
        assertEquals(1, pessoa.getEmails().size());
        
        // Tentar modificar a lista da pessoa lança exceção
        assertThrows(UnsupportedOperationException.class, () -> {
            pessoa.getEmails().add("novo@email.com");
        });
    }
    
    @Test
    @DisplayName("Deve criar nova instância ao modificar")
    void criarNovaInstancia() {
        List<String> emails = List.of("joao@email.com");
        ImmutabilityExample.EnderecoImutavel endereco = new ImmutabilityExample.EnderecoImutavel(
            "Rua A", "São Paulo", "01000-000"
        );
        ImmutabilityExample.PessoaImutavel original = new ImmutabilityExample.PessoaImutavel(
            "João", 30, emails, endereco
        );
        
        ImmutabilityExample.PessoaImutavel modificada = original.comNome("João Silva");
        
        // Original não foi alterada
        assertEquals("João", original.getNome());
        // Nova instância tem o novo nome
        assertEquals("João Silva", modificada.getNome());
        // Outros campos permanecem iguais
        assertEquals(original.getIdade(), modificada.getIdade());
    }
    
    @Test
    @DisplayName("Deve adicionar email criando nova instância")
    void adicionarEmail() {
        List<String> emails = new ArrayList<>();
        emails.add("joao@email.com");
        ImmutabilityExample.PessoaImutavel original = new ImmutabilityExample.PessoaImutavel(
            "João", 30, emails, null
        );
        
        ImmutabilityExample.PessoaImutavel comNovoEmail = original.adicionarEmail("novo@email.com");
        
        assertEquals(1, original.getEmails().size());
        assertEquals(2, comNovoEmail.getEmails().size());
        assertTrue(comNovoEmail.getEmails().contains("novo@email.com"));
    }
    
    @Test
    @DisplayName("Deve criar coleções imutáveis")
    void criarColecoesImutaveis() {
        List<String> lista = List.of("A", "B", "C");
        Set<Integer> conjunto = Set.of(1, 2, 3);
        Map<String, Integer> mapa = Map.of("um", 1, "dois", 2);
        
        assertThrows(UnsupportedOperationException.class, () -> lista.add("D"));
        assertThrows(UnsupportedOperationException.class, () -> conjunto.add(4));
        assertThrows(UnsupportedOperationException.class, () -> mapa.put("tres", 3));
    }
    
    @Test
    @DisplayName("Deve dobrar valores sem modificar original")
    void dobrarValores() {
        ImmutabilityExample example = new ImmutabilityExample();
        List<Integer> original = new ArrayList<>(List.of(1, 2, 3));
        
        List<Integer> dobrados = example.dobrarValores(original);
        
        assertEquals(List.of(1, 2, 3), original);
        assertEquals(List.of(2, 4, 6), dobrados);
    }
    
    @Test
    @DisplayName("Deve criar Record imutável")
    void criarRecord() {
        ImmutabilityExample.Produto produto = new ImmutabilityExample.Produto("Notebook", 5000.0, "Eletrônicos");
        
        assertEquals("Notebook", produto.nome());
        assertEquals(5000.0, produto.preco());
        
        // Records são imutáveis
        // produto = produto.withPreco(4000.0); // Não existe
    }
    
    @Test
    @DisplayName("Deve calcular preço com desconto no Record")
    void recordPrecoComDesconto() {
        ImmutabilityExample.Produto produto = new ImmutabilityExample.Produto("Notebook", 5000.0, "Eletrônicos");
        
        double comDesconto = produto.precoComDesconto(10);
        
        assertEquals(4500.0, comDesconto);
        assertEquals(5000.0, produto.preco()); // Original não muda
    }
    
    @Test
    @DisplayName("Record deve criar novo com desconto")
    void recordNovoComDesconto() {
        ImmutabilityExample.Produto produto = new ImmutabilityExample.Produto("Notebook", 5000.0, "Eletrônicos");
        
        ImmutabilityExample.Produto comDesconto = produto.comDesconto(10);
        
        assertEquals(5000.0, produto.preco());
        assertEquals(4500.0, comDesconto.preco());
        assertEquals("Notebook", comDesconto.nome()); // Nome permanece igual
    }
    
    @Test
    @DisplayName("Record deve validar no construtor")
    void recordValidacao() {
        assertThrows(IllegalArgumentException.class, () -> {
            new ImmutabilityExample.Produto("", -100.0, "Teste");
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            new ImmutabilityExample.Produto("Produto", -100.0, "Teste");
        });
    }
    
    @Test
    @DisplayName("Record deve ter equals e hashCode automáticos")
    void recordEqualsHashCode() {
        ImmutabilityExample.Produto p1 = new ImmutabilityExample.Produto("Notebook", 5000.0, "Eletrônicos");
        ImmutabilityExample.Produto p2 = new ImmutabilityExample.Produto("Notebook", 5000.0, "Eletrônicos");
        ImmutabilityExample.Produto p3 = new ImmutabilityExample.Produto("Mouse", 50.0, "Periféricos");
        
        assertEquals(p1, p2);
        assertNotEquals(p1, p3);
        assertEquals(p1.hashCode(), p2.hashCode());
    }
    
    @Test
    @DisplayName("Deve criar cópia defensiva")
    void criarCopiaDefensiva() {
        ImmutabilityExample example = new ImmutabilityExample();
        List<String> original = new ArrayList<>(List.of("A", "B"));
        
        List<String> copia = example.criarCopiaDefensiva(original);
        
        // Modificar original não afeta cópia
        original.add("C");
        assertEquals(2, copia.size());
        
        // Cópia é imutável
        assertThrows(UnsupportedOperationException.class, () -> copia.add("D"));
    }
}
