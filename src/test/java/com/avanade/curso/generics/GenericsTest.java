package com.avanade.curso.generics;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import java.util.function.Function;

/**
 * Testes para Generics
 */
class GenericsTest {
    
    // ============================================
    // Testes de Classes Genéricas
    // ============================================
    
    @Test
    @DisplayName("Caixa deve guardar e retirar String")
    void caixaString() {
        Caixa<String> caixa = new Caixa<>();
        caixa.guardar("Teste");
        
        assertFalse(caixa.estaVazia());
        assertEquals("Teste", caixa.retirar());
        assertTrue(caixa.estaVazia());
    }
    
    @Test
    @DisplayName("Caixa deve guardar e retirar Integer")
    void caixaInteger() {
        Caixa<Integer> caixa = new Caixa<>();
        caixa.guardar(42);
        
        Integer valor = caixa.retirar();
        assertEquals(42, valor);
    }
    
    @Test
    @DisplayName("Caixa deve guardar tipos complexos")
    void caixaTipoComplexo() {
        Caixa<List<String>> caixa = new Caixa<>();
        List<String> lista = Arrays.asList("A", "B", "C");
        caixa.guardar(lista);
        
        List<String> retirada = caixa.retirar();
        assertEquals(3, retirada.size());
    }
    
    // ============================================
    // Testes de Bounded Types
    // ============================================
    
    @Test
    @DisplayName("NumberBox deve aceitar Integer")
    void numberBoxInteger() {
        NumberBox<Integer> box = new NumberBox<>();
        box.set(100);
        
        assertEquals(100, box.get());
        assertEquals(100.0, box.doubleValue());
    }
    
    @Test
    @DisplayName("NumberBox deve aceitar Double")
    void numberBoxDouble() {
        NumberBox<Double> box = new NumberBox<>();
        box.set(3.14);
        
        assertEquals(3.14, box.get());
        assertEquals(3, box.intValue());
    }
    
    @Test
    @DisplayName("NumberBox deve comparar números")
    void numberBoxComparar() {
        NumberBox<Double> box = new NumberBox<>();
        box.set(10.5);
        
        assertTrue(box.isGreaterThan(5.0));
        assertFalse(box.isGreaterThan(20.0));
    }
    
    // ============================================
    // Testes de Métodos Genéricos
    // ============================================
    
    @Test
    @DisplayName("Swap deve trocar elementos em array de Strings")
    void swapStrings() {
        String[] array = {"A", "B", "C"};
        
        UtilitariosGenericos.swap(array, 0, 2);
        
        assertEquals("C", array[0]);
        assertEquals("B", array[1]);
        assertEquals("A", array[2]);
    }
    
    @Test
    @DisplayName("Swap deve trocar elementos em array de Integers")
    void swapIntegers() {
        Integer[] array = {1, 2, 3};
        
        UtilitariosGenericos.swap(array, 0, 1);
        
        assertEquals(2, array[0]);
        assertEquals(1, array[1]);
    }
    
    @Test
    @DisplayName("Max deve retornar maior String")
    void maxStrings() {
        String maior = UtilitariosGenericos.max("Java", "Python");
        
        assertEquals("Python", maior);
    }
    
    @Test
    @DisplayName("Max deve retornar maior Integer")
    void maxIntegers() {
        Integer maior = UtilitariosGenericos.max(10, 20);
        
        assertEquals(20, maior);
    }
    
    @Test
    @DisplayName("CriarMap deve criar mapa com tipos específicos")
    void criarMap() {
        Map<String, Integer> map = UtilitariosGenericos.criarMap("idade", 25);
        
        assertEquals(25, map.get("idade"));
    }
    
    @Test
    @DisplayName("CriarLista deve criar lista de varargs")
    void criarLista() {
        List<String> lista = UtilitariosGenericos.criarLista("A", "B", "C");
        
        assertEquals(3, lista.size());
        assertEquals("A", lista.get(0));
    }
    
    @Test
    @DisplayName("Converter deve transformar lista")
    void converterLista() {
        List<String> strings = Arrays.asList("1", "2", "3");
        List<Integer> inteiros = UtilitariosGenericos.converter(strings, Integer::parseInt);
        
        assertEquals(3, inteiros.size());
        assertEquals(1, inteiros.get(0));
    }
    
    @Test
    @DisplayName("Maximo deve encontrar maior em lista")
    void maximoLista() {
        List<Integer> numeros = Arrays.asList(5, 2, 8, 1, 9);
        
        Optional<Integer> max = UtilitariosGenericos.maximo(numeros);
        
        assertTrue(max.isPresent());
        assertEquals(9, max.get());
    }
    
    @Test
    @DisplayName("Maximo deve retornar vazio para lista vazia")
    void maximoListaVazia() {
        List<Integer> numeros = Collections.emptyList();
        
        Optional<Integer> max = UtilitariosGenericos.maximo(numeros);
        
        assertFalse(max.isPresent());
    }
    
    // ============================================
    // Testes de Wildcards
    // ============================================
    
    @Test
    @DisplayName("Somar numeros deve aceitar lista de Integer")
    void somarIntegers() {
        List<Integer> inteiros = Arrays.asList(1, 2, 3, 4, 5);
        
        double soma = ExemplosWildcards.somarNumeros(inteiros);
        
        assertEquals(15.0, soma);
    }
    
    @Test
    @DisplayName("Somar numeros deve aceitar lista de Double")
    void somarDoubles() {
        List<Double> doubles = Arrays.asList(1.5, 2.5, 3.0);
        
        double soma = ExemplosWildcards.somarNumeros(doubles);
        
        assertEquals(7.0, soma);
    }
    
    @Test
    @DisplayName("Imprimir qualquer lista deve funcionar com diferentes tipos")
    void imprimirQualquerLista() {
        List<String> strings = new ArrayList<>(Arrays.asList("A", "B"));
        List<Integer> inteiros = new ArrayList<>(Arrays.asList(1, 2));
        
        // Não deve lançar exceção
        assertDoesNotThrow(() -> ExemplosWildcards.imprimirQualquerLista(strings));
        assertDoesNotThrow(() -> ExemplosWildcards.imprimirQualquerLista(inteiros));
    }
    
    @Test
    @DisplayName("Copiar deve transferir elementos entre listas")
    void copiarListas() {
        List<Integer> origem = Arrays.asList(1, 2, 3);
        List<Number> destino = new ArrayList<>();
        
        UtilitariosGenericos.copiar(origem, destino);
        
        assertEquals(3, destino.size());
    }
    
    // ============================================
    // Testes de Type Erasure
    // ============================================
    
    @Test
    @DisplayName("Criar array deve funcionar com reflexão")
    void criarArrayViaReflexao() {
        String[] array = DemonstracaoErasure.criarArray(String.class, 5);
        
        array[0] = "Teste";
        assertEquals("Teste", array[0]);
        assertEquals(5, array.length);
    }
    
    @Test
    @DisplayName("IsTipo deve verificar tipo em runtime")
    void verificarTipo() {
        assertTrue(DemonstracaoErasure.isTipo("texto", String.class));
        assertFalse(DemonstracaoErasure.isTipo(123, String.class));
        assertTrue(DemonstracaoErasure.isTipo(123, Integer.class));
    }
    
    // ============================================
    // Testes de Herança com Generics
    // ============================================
    
    @Test
    @DisplayName("FazerTodosSom deve aceitar lista de Cachorro")
    void fazerSomCachorros() {
        List<Cachorro> cachorros = Arrays.asList(new Cachorro("Rex"), new Cachorro("Buddy"));
        
        // Não deve lançar exceção
        assertDoesNotThrow(() -> HerancaComGenerics.fazerTodosSom(cachorros));
    }
    
    @Test
    @DisplayName("FazerTodosSom deve aceitar lista mista de Animais")
    void fazerSomAnimais() {
        List<Animal> animais = Arrays.asList(new Cachorro("Rex"), new Gato("Mimi"));
        
        assertDoesNotThrow(() -> HerancaComGenerics.fazerTodosSom(animais));
    }
    
    @Test
    @DisplayName("AdicionarCachorro deve aceitar lista de Animais")
    void adicionarCachorroEmAnimais() {
        List<Animal> animais = new ArrayList<>();
        
        HerancaComGenerics.adicionarCachorro(animais);
        
        assertEquals(1, animais.size());
        assertTrue(animais.get(0) instanceof Cachorro);
    }
    
    // ============================================
    // Testes de Repositório Genérico
    // ============================================
    
    @Test
    @DisplayName("UsuarioRepository deve salvar e buscar usuário")
    void repositorioUsuario() {
        UsuarioRepository repo = new UsuarioRepository();
        
        Usuario salvo = repo.salvar(new Usuario(null, "João"));
        
        assertNotNull(salvo.getId());
        
        Usuario encontrado = repo.buscarPorId(salvo.getId());
        assertEquals("João", encontrado.getNome());
    }
    
    @Test
    @DisplayName("UsuarioRepository deve listar todos")
    void repositorioListarTodos() {
        UsuarioRepository repo = new UsuarioRepository();
        
        repo.salvar(new Usuario(null, "João"));
        repo.salvar(new Usuario(null, "Maria"));
        
        List<Usuario> todos = repo.buscarTodos();
        
        assertEquals(2, todos.size());
    }
    
    @Test
    @DisplayName("UsuarioRepository deve deletar usuário")
    void repositorioDeletar() {
        UsuarioRepository repo = new UsuarioRepository();
        Usuario usuario = repo.salvar(new Usuario(null, "João"));
        
        repo.deletar(usuario.getId());
        
        assertNull(repo.buscarPorId(usuario.getId()));
    }
    
    @Test
    @DisplayName("UsuarioRepository deve atualizar usuário existente")
    void repositorioAtualizar() {
        UsuarioRepository repo = new UsuarioRepository();
        Usuario usuario = repo.salvar(new Usuario(null, "João"));
        
        repo.salvar(new Usuario(usuario.getId(), "João Silva"));
        
        Usuario atualizado = repo.buscarPorId(usuario.getId());
        assertEquals("João Silva", atualizado.getNome());
    }
    
    // ============================================
    // Testes de Type Safety
    // ============================================
    
    @Test
    @DisplayName("Generics evita ClassCastException em tempo de execução")
    void typeSafety() {
        // Com generics, isso é seguro
        Caixa<String> caixa = new Caixa<>();
        caixa.guardar("texto");
        
        String valor = caixa.getConteudo();
        // Não precisa de cast explícito
        assertEquals("texto", valor);
    }
    
    @Test
    @DisplayName("Não é possível adicionar tipo errado em lista parametrizada")
    void compilacaoSegura() {
        List<String> lista = new ArrayList<>();
        lista.add("texto");
        
        // lista.add(123); // Erro de compilação!
        
        assertEquals(1, lista.size());
    }
}
