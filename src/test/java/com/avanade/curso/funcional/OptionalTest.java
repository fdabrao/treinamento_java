package com.avanade.curso.funcional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

/**
 * Testes para Optional
 */
class OptionalTest {
    
    @Test
    @DisplayName("Deve verificar presença de valor")
    void verificarPresenca() {
        OptionalExample example = new OptionalExample();
        
        assertTrue(example.verificarPresenca(Optional.of("valor")));
        assertFalse(example.verificarPresenca(Optional.empty()));
    }
    
    @Test
    @DisplayName("Deve verificar se está vazio")
    void verificarVazio() {
        OptionalExample example = new OptionalExample();
        
        assertTrue(example.verificarVazio(Optional.empty()));
        assertFalse(example.verificarVazio(Optional.of("valor")));
    }
    
    @Test
    @DisplayName("Deve obter valor ou lançar exceção")
    void obterValorOuException() {
        OptionalExample example = new OptionalExample();
        
        assertEquals("valor", example.obterValorOuException(Optional.of("valor")));
        assertThrows(IllegalArgumentException.class, () -> {
            example.obterValorOuException(Optional.empty());
        });
    }
    
    @Test
    @DisplayName("Deve obter valor ou padrão")
    void obterValorOuPadrao() {
        OptionalExample example = new OptionalExample();
        
        assertEquals("valor", example.obterValorOuPadrao(Optional.of("valor")));
        assertEquals("Valor padrão", example.obterValorOuPadrao(Optional.empty()));
    }
    
    @Test
    @DisplayName("Deve obter valor ou calcular padrão")
    void obterValorOuCalcular() {
        OptionalExample example = new OptionalExample();
        
        assertEquals("valor", example.obterValorOuCalcular(Optional.of("valor")));
        assertEquals("Valor calculado", example.obterValorOuCalcular(Optional.empty()));
    }
    
    @Test
    @DisplayName("Deve obter tamanho do texto")
    void obterTamanho() {
        OptionalExample example = new OptionalExample();
        
        Optional<Integer> tamanho = example.obterTamanho(Optional.of("Java"));
        
        assertTrue(tamanho.isPresent());
        assertEquals(4, tamanho.get());
    }
    
    @Test
    @DisplayName("Deve filtrar por tamanho mínimo")
    void filtrarPorTamanho() {
        OptionalExample example = new OptionalExample();
        
        Optional<String> resultado = example.filtrarPorTamanho(Optional.of("Programação"), 5);
        Optional<String> vazio = example.filtrarPorTamanho(Optional.of("Oi"), 5);
        
        assertTrue(resultado.isPresent());
        assertEquals("Programação", resultado.get());
        assertTrue(vazio.isEmpty());
    }
    
    @Test
    @DisplayName("Deve buscar usuário por ID")
    void buscarUsuarioPorId() {
        OptionalExample example = new OptionalExample();
        
        Optional<OptionalExample.Usuario> encontrado = example.buscarUsuarioPorId(1);
        Optional<OptionalExample.Usuario> naoEncontrado = example.buscarUsuarioPorId(-1);
        
        assertTrue(encontrado.isPresent());
        assertEquals(1, encontrado.get().getId());
        assertTrue(naoEncontrado.isEmpty());
    }
    
    @Test
    @DisplayName("Deve obter nome maiúsculo ou padrão")
    void obterNomeMaiusculoOuPadrao() {
        OptionalExample example = new OptionalExample();
        OptionalExample.Usuario usuario = new OptionalExample.Usuario(1, "João Silva");
        
        String nome = example.obterNomeMaiusculoOuPadrao(Optional.of(usuario));
        String padrao = example.obterNomeMaiusculoOuPadrao(Optional.empty());
        
        assertEquals("JOÃO SILVA", nome);
        assertEquals("DESCONHECIDO", padrao);
    }
    
    @Test
    @DisplayName("Deve processar valor removendo espaços")
    void processarValor() {
        OptionalExample example = new OptionalExample();
        
        Optional<String> resultado = example.processarValor("  Java  ");
        Optional<String> vazio = example.processarValor("   ");
        Optional<String> nulo = example.processarValor(null);
        
        assertTrue(resultado.isPresent());
        assertEquals("Java", resultado.get());
        assertTrue(vazio.isEmpty());
        assertTrue(nulo.isEmpty());
    }
    
    @Test
    @DisplayName("Deve remover Optionais vazios")
    void removerVazios() {
        OptionalExample example = new OptionalExample();
        List<Optional<String>> optionals = List.of(
            Optional.of("A"),
            Optional.empty(),
            Optional.of("B"),
            Optional.empty(),
            Optional.of("C")
        );
        
        List<String> resultado = example.removerVazios(optionals);
        
        assertEquals(List.of("A", "B", "C"), resultado);
    }
}
