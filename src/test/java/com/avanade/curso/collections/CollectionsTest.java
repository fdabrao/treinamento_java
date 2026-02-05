package com.avanade.curso.collections;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

/**
 * Testes para Collections Framework
 */
class CollectionsTest {
    
    // ============================================
    // Testes de List
    // ============================================
    
    @Test
    @DisplayName("ArrayList deve permitir duplicados")
    void arrayListPermiteDuplicados() {
        List<String> lista = new ArrayList<>();
        lista.add("A");
        lista.add("A");
        lista.add("B");
        
        assertEquals(3, lista.size());
        assertEquals("A", lista.get(0));
        assertEquals("A", lista.get(1));
    }
    
    @Test
    @DisplayName("ArrayList deve manter ordem de inserção")
    void arrayListMantemOrdem() {
        List<Integer> lista = new ArrayList<>();
        lista.add(3);
        lista.add(1);
        lista.add(2);
        
        assertEquals(3, lista.get(0));
        assertEquals(1, lista.get(1));
        assertEquals(2, lista.get(2));
    }
    
    @Test
    @DisplayName("LinkedList deve funcionar como Deque")
    void linkedListComoDeque() {
        LinkedList<String> lista = new LinkedList<>();
        lista.addFirst("Primeiro");
        lista.addLast("Último");
        
        assertEquals("Primeiro", lista.getFirst());
        assertEquals("Último", lista.getLast());
    }
    
    @Test
    @DisplayName("Lista deve ser modificada durante iteração com ListIterator")
    void listIteratorModificacao() {
        List<String> lista = new ArrayList<>(Arrays.asList("A", "B", "C"));
        ListIterator<String> iterator = lista.listIterator();
        
        while (iterator.hasNext()) {
            String item = iterator.next();
            if (item.equals("B")) {
                iterator.set("Modificado");
            }
        }
        
        assertEquals("Modificado", lista.get(1));
    }
    
    // ============================================
    // Testes de Set
    // ============================================
    
    @Test
    @DisplayName("HashSet deve remover duplicados")
    void hashSetRemoveDuplicados() {
        Set<String> set = new HashSet<>();
        set.add("A");
        set.add("A");
        set.add("B");
        set.add("B");
        
        assertEquals(2, set.size());
    }
    
    @Test
    @DisplayName("LinkedHashSet deve manter ordem de inserção")
    void linkedHashSetMantemOrdem() {
        Set<String> set = new LinkedHashSet<>();
        set.add("Primeiro");
        set.add("Segundo");
        set.add("Terceiro");
        
        Iterator<String> it = set.iterator();
        assertEquals("Primeiro", it.next());
        assertEquals("Segundo", it.next());
        assertEquals("Terceiro", it.next());
    }
    
    @Test
    @DisplayName("TreeSet deve manter ordenado")
    void treeSetOrdenado() {
        Set<String> set = new TreeSet<>();
        set.add("Charlie");
        set.add("Alice");
        set.add("Bob");
        
        Iterator<String> it = set.iterator();
        assertEquals("Alice", it.next());
        assertEquals("Bob", it.next());
        assertEquals("Charlie", it.next());
    }
    
    @Test
    @DisplayName("Operações de conjunto devem funcionar")
    void operacoesConjunto() {
        Set<Integer> set1 = new HashSet<>(Arrays.asList(1, 2, 3, 4));
        Set<Integer> set2 = new HashSet<>(Arrays.asList(3, 4, 5, 6));
        
        // União
        Set<Integer> uniao = new HashSet<>(set1);
        uniao.addAll(set2);
        assertEquals(6, uniao.size());
        
        // Interseção
        Set<Integer> intersecao = new HashSet<>(set1);
        intersecao.retainAll(set2);
        assertEquals(2, intersecao.size());
        assertTrue(intersecao.contains(3));
        assertTrue(intersecao.contains(4));
        
        // Diferença
        Set<Integer> diferenca = new HashSet<>(set1);
        diferenca.removeAll(set2);
        assertEquals(2, diferenca.size());
        assertTrue(diferenca.contains(1));
        assertTrue(diferenca.contains(2));
    }
    
    // ============================================
    // Testes de Map
    // ============================================
    
    @Test
    @DisplayName("HashMap deve armazenar chave-valor")
    void hashMapBasico() {
        Map<String, Integer> map = new HashMap<>();
        map.put("Alice", 25);
        map.put("Bob", 30);
        
        assertEquals(25, map.get("Alice"));
        assertEquals(30, map.get("Bob"));
        assertNull(map.get("Inexistente"));
    }
    
    @Test
    @DisplayName("HashMap deve sobrescrever valor com mesma chave")
    void hashMapSobrescreve() {
        Map<String, Integer> map = new HashMap<>();
        map.put("Alice", 25);
        map.put("Alice", 26);
        
        assertEquals(26, map.get("Alice"));
        assertEquals(1, map.size());
    }
    
    @Test
    @DisplayName("LinkedHashMap deve manter ordem de inserção")
    void linkedHashMapOrdem() {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("BR", "Brasil");
        map.put("AR", "Argentina");
        map.put("CL", "Chile");
        
        Iterator<String> it = map.keySet().iterator();
        assertEquals("BR", it.next());
        assertEquals("AR", it.next());
        assertEquals("CL", it.next());
    }
    
    @Test
    @DisplayName("TreeMap deve manter chaves ordenadas")
    void treeMapOrdenado() {
        Map<String, Integer> map = new TreeMap<>();
        map.put("Charlie", 30);
        map.put("Alice", 25);
        map.put("Bob", 35);
        
        Iterator<String> it = map.keySet().iterator();
        assertEquals("Alice", it.next());
        assertEquals("Bob", it.next());
        assertEquals("Charlie", it.next());
    }
    
    @Test
    @DisplayName("Map deve suportar operações Java 8+")
    void mapJava8() {
        Map<String, Integer> map = new HashMap<>();
        
        // getOrDefault
        assertEquals(0, map.getOrDefault("Alice", 0));
        
        // putIfAbsent
        map.putIfAbsent("Alice", 25);
        map.putIfAbsent("Alice", 99); // Não altera
        assertEquals(25, map.get("Alice"));
        
        // compute
        map.compute("Alice", (k, v) -> v + 1);
        assertEquals(26, map.get("Alice"));
        
        // merge
        map.merge("Alice", 10, Integer::sum);
        assertEquals(36, map.get("Alice"));
    }
    
    // ============================================
    // Testes de Queue
    // ============================================
    
    @Test
    @DisplayName("Queue deve funcionar como FIFO")
    void queueFIFO() {
        Queue<String> fila = new LinkedList<>();
        fila.offer("Primeiro");
        fila.offer("Segundo");
        fila.offer("Terceiro");
        
        assertEquals("Primeiro", fila.peek());
        assertEquals("Primeiro", fila.poll()); // Remove e retorna
        assertEquals("Segundo", fila.poll());
        assertEquals("Terceiro", fila.poll());
        assertNull(fila.poll()); // Fila vazia
    }
    
    @Test
    @DisplayName("PriorityQueue deve ordenar por prioridade")
    void priorityQueue() {
        PriorityQueue<Integer> fila = new PriorityQueue<>();
        fila.offer(5);
        fila.offer(1);
        fila.offer(3);
        fila.offer(2);
        
        assertEquals(1, fila.poll()); // Menor primeiro
        assertEquals(2, fila.poll());
        assertEquals(3, fila.poll());
        assertEquals(5, fila.poll());
    }
    
    @Test
    @DisplayName("Deque deve permitir inserção em ambas as extremidades")
    void dequeOperacoes() {
        Deque<String> deque = new ArrayDeque<>();
        deque.addFirst("Primeiro");
        deque.addLast("Último");
        deque.addFirst("Novo Primeiro");
        
        assertEquals("Novo Primeiro", deque.getFirst());
        assertEquals("Último", deque.getLast());
    }
    
    // ============================================
    // Testes de Utilitários
    // ============================================
    
    @Test
    @DisplayName("Collections.sort deve ordenar lista")
    void sortLista() {
        List<Integer> lista = new ArrayList<>(Arrays.asList(5, 2, 8, 1, 9));
        Collections.sort(lista);
        
        assertEquals(Arrays.asList(1, 2, 5, 8, 9), lista);
    }
    
    @Test
    @DisplayName("Collections.reverseOrder deve ordenar decrescente")
    void sortDecrescente() {
        List<Integer> lista = new ArrayList<>(Arrays.asList(3, 1, 4, 1, 5));
        Collections.sort(lista, Collections.reverseOrder());
        
        assertEquals(Arrays.asList(5, 4, 3, 1, 1), lista);
    }
    
    @Test
    @DisplayName("Collections.max e min devem funcionar")
    void maxMin() {
        List<Integer> lista = Arrays.asList(5, 2, 8, 1, 9, 3);
        
        assertEquals(9, Collections.max(lista));
        assertEquals(1, Collections.min(lista));
    }
    
    @Test
    @DisplayName("Collections.frequency deve contar ocorrências")
    void frequency() {
        List<String> lista = Arrays.asList("A", "B", "A", "C", "A");
        
        assertEquals(3, Collections.frequency(lista, "A"));
        assertEquals(1, Collections.frequency(lista, "B"));
        assertEquals(0, Collections.frequency(lista, "D"));
    }
    
    @Test
    @DisplayName("Collections.binarySearch deve encontrar elemento")
    void binarySearch() {
        List<Integer> lista = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
        
        int index = Collections.binarySearch(lista, 5);
        assertEquals(4, index);
        
        int naoEncontrado = Collections.binarySearch(lista, 99);
        assertTrue(naoEncontrado < 0);
    }
    
    @Test
    @DisplayName("Collections.unmodifiableList deve impedir modificações")
    void unmodifiableList() {
        List<String> original = new ArrayList<>(Arrays.asList("A", "B"));
        List<String> imutavel = Collections.unmodifiableList(original);
        
        assertThrows(UnsupportedOperationException.class, () -> {
            imutavel.add("C");
        });
    }
    
    @Test
    @DisplayName("Collections.singleton deve criar set com um elemento")
    void singletonSet() {
        Set<String> set = Collections.singleton("Único");
        
        assertEquals(1, set.size());
        assertTrue(set.contains("Único"));
        assertThrows(UnsupportedOperationException.class, () -> {
            set.add("Outro");
        });
    }
    
    @Test
    @DisplayName("Collections.emptyList deve retornar lista vazia imutável")
    void emptyList() {
        List<String> vazia = Collections.emptyList();
        
        assertTrue(vazia.isEmpty());
        assertThrows(UnsupportedOperationException.class, () -> {
            vazia.add("Item");
        });
    }
    
    // ============================================
    // Testes de Boas Práticas
    // ============================================
    
    @Test
    @DisplayName("Deve usar interface List na declaração")
    void programarParaInterface() {
        //  Bom: Programar para interface
        List<String> lista = new ArrayList<>();
        
        // Pode trocar implementação facilmente
        lista = new LinkedList<>();
        
        assertNotNull(lista);
    }
    
    @Test
    @DisplayName("Deve usar isEmpty ao invés de size() == 0")
    void usarIsEmpty() {
        List<String> lista = new ArrayList<>();
        
        assertTrue(lista.isEmpty());
        assertFalse(lista.size() > 0);
    }
    
    @Test
    @DisplayName("Deve definir capacidade inicial quando possível")
    void capacidadeInicial() {
        // Evita redimensionamentos
        List<String> lista = new ArrayList<>(1000);
        Map<String, String> map = new HashMap<>(500);
        
        assertTrue(lista.isEmpty());
        assertTrue(map.isEmpty());
    }
    
    @Test
    @DisplayName("Deve usar diamond operator")
    void diamondOperator() {
        //  Java 7+: Diamond operator
        List<String> lista = new ArrayList<>();
        Map<String, Integer> map = new HashMap<>();
        
        lista.add("Teste");
        map.put("Chave", 1);
        
        assertEquals(1, lista.size());
        assertEquals(1, map.size());
    }
}
