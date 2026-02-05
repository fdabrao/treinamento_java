package com.avanade.curso.collections;

import java.util.*;

/**
 * COLLECTIONS FRAMEWORK
 * 
 * Hierarquia de coleções em Java:
 * 
 * Iterable
 *    └── Collection
 *           ├── List (ordenada, permite duplicados)
 *           │      ├── ArrayList (array dinâmico)
 *           │      ├── LinkedList (lista duplamente ligada)
 *           │      └── Vector (legado, thread-safe)
 *           │
 *           ├── Set (não ordenado, sem duplicados)
 *           │      ├── HashSet (hash table, O(1))
 *           │      ├── LinkedHashSet (mantém ordem de inserção)
 *           │      └── TreeSet (árvore ordenada, O(log n))
 *           │
 *           └── Queue (FIFO)
 *                  ├── LinkedList
 *                  ├── PriorityQueue
 *                  └── ArrayDeque
 * 
 * Map (não extends Collection)
 *      ├── HashMap
 *      ├── LinkedHashMap
 *      ├── TreeMap
 *      └── Hashtable (legado)
 */

public class CollectionsExample {
    
    // ============================================
    // LIST - Ordenada, permite duplicados
    // ============================================
    
    /**
     * ArrayList vs LinkedList:
     * 
     * ArrayList:
     * - Baseado em array dinâmico
     * - Acesso por índice: O(1)
     * - Inserção no final: O(1) amortizado
     * - Inserção/remoção no meio: O(n)
     * - Melhor para: acesso aleatório, iteração
     * 
     * LinkedList:
     * - Lista duplamente ligada
     * - Acesso por índice: O(n)
     * - Inserção/remoção: O(1) se tiver referência
     * - Melhor para: inserções/remoções frequentes
     */
    
    public void demonstrarList() {
        // ArrayList - mais comum
        List<String> arrayList = new ArrayList<>();
        arrayList.add("Java");
        arrayList.add("Python");
        arrayList.add("JavaScript");
        arrayList.add("Java"); // Permite duplicados
        
        System.out.println("ArrayList: " + arrayList);
        System.out.println("Elemento 0: " + arrayList.get(0)); // O(1)
        
        // LinkedList
        List<String> linkedList = new LinkedList<>();
        linkedList.add("Primeiro");
        linkedList.add("Segundo");
        ((LinkedList<String>) linkedList).addFirst("Novo Primeiro");
        ((LinkedList<String>) linkedList).addLast("Novo Último");
        
        System.out.println("LinkedList: " + linkedList);
        
        // Métodos comuns
        System.out.println("Tamanho: " + arrayList.size());
        System.out.println("Contém 'Java': " + arrayList.contains("Java"));
        System.out.println("Índice de 'Python': " + arrayList.indexOf("Python"));
        
        // Iteração
        System.out.println("\nIterando:");
        for (String item : arrayList) {
            System.out.println("  " + item);
        }
        
        // ListIterator (permite modificação durante iteração)
        ListIterator<String> iterator = arrayList.listIterator();
        while (iterator.hasNext()) {
            String item = iterator.next();
            if (item.equals("Python")) {
                iterator.set("Kotlin"); // Substitui
            }
        }
        System.out.println("Após modificação: " + arrayList);
    }
    
    // ============================================
    // SET - Sem duplicados
    // ============================================
    
    /**
     * HashSet vs LinkedHashSet vs TreeSet:
     * 
     * HashSet:
     * - Hash table
     * - O(1) para add/remove/contains
     * - Ordem não garantida
     * - Requer equals() e hashCode()
     * 
     * LinkedHashSet:
     * - Mantém ordem de inserção
     * - O(1) para operações
     * - Pouco mais de memória que HashSet
     * 
     * TreeSet:
     * - Árvore Red-Black
     * - O(log n) para operações
     * - Mantém elementos ordenados
     * - Requer Comparable ou Comparator
     */
    
    public void demonstrarSet() {
        // HashSet - mais rápido, sem ordem garantida
        Set<String> hashSet = new HashSet<>();
        hashSet.add("Banana");
        hashSet.add("Maçã");
        hashSet.add("Laranja");
        hashSet.add("Banana"); // Ignorado (duplicado)
        
        System.out.println("HashSet: " + hashSet);
        System.out.println("Tamanho: " + hashSet.size()); // 3
        
        // LinkedHashSet - mantém ordem de inserção
        Set<String> linkedHashSet = new LinkedHashSet<>();
        linkedHashSet.add("Primeiro");
        linkedHashSet.add("Segundo");
        linkedHashSet.add("Terceiro");
        
        System.out.println("LinkedHashSet: " + linkedHashSet);
        
        // TreeSet - ordenado naturalmente
        Set<String> treeSet = new TreeSet<>();
        treeSet.add("Charlie");
        treeSet.add("Alice");
        treeSet.add("Bob");
        
        System.out.println("TreeSet (ordenado): " + treeSet);
        
        // Operações de conjunto
        Set<Integer> set1 = new HashSet<>(Arrays.asList(1, 2, 3, 4));
        Set<Integer> set2 = new HashSet<>(Arrays.asList(3, 4, 5, 6));
        
        // União
        Set<Integer> uniao = new HashSet<>(set1);
        uniao.addAll(set2);
        System.out.println("União: " + uniao);
        
        // Interseção
        Set<Integer> intersecao = new HashSet<>(set1);
        intersecao.retainAll(set2);
        System.out.println("Interseção: " + intersecao);
        
        // Diferença
        Set<Integer> diferenca = new HashSet<>(set1);
        diferenca.removeAll(set2);
        System.out.println("Diferença: " + diferenca);
    }
    
    // ============================================
    // MAP - Chave-Valor
    // ============================================
    
    /**
     * HashMap vs LinkedHashMap vs TreeMap:
     * 
     * HashMap:
     * - O(1) para get/put/remove
     * - Ordem não garantida
     * - Permite null key e null values
     * 
     * LinkedHashMap:
     * - Mantém ordem de inserção
     * - O(1) para operações
     * 
     * TreeMap:
     * - O(log n) para operações
     * - Ordenado por chave
     */
    
    public void demonstrarMap() {
        // HashMap - mais comum
        Map<String, Integer> idades = new HashMap<>();
        idades.put("Alice", 25);
        idades.put("Bob", 30);
        idades.put("Charlie", 35);
        idades.put("Alice", 26); // Sobrescreve
        
        System.out.println("HashMap: " + idades);
        System.out.println("Idade de Alice: " + idades.get("Alice"));
        System.out.println("Contém 'Bob': " + idades.containsKey("Bob"));
        System.out.println("Alguém tem 30: " + idades.containsValue(30));
        
        // LinkedHashMap - mantém ordem
        Map<String, String> capitais = new LinkedHashMap<>();
        capitais.put("Brasil", "Brasília");
        capitais.put("Argentina", "Buenos Aires");
        capitais.put("Chile", "Santiago");
        
        System.out.println("LinkedHashMap: " + capitais);
        
        // TreeMap - ordenado por chave
        Map<String, Double> precos = new TreeMap<>();
        precos.put("Banana", 2.50);
        precos.put("Maçã", 3.00);
        precos.put("Laranja", 2.00);
        
        System.out.println("TreeMap (ordenado): " + precos);
        
        // Iteração
        System.out.println("\nIterando sobre entries:");
        for (Map.Entry<String, Integer> entry : idades.entrySet()) {
            System.out.println("  " + entry.getKey() + " = " + entry.getValue());
        }
        
        // Java 8+ methods
        idades.forEach((nome, idade) -> 
            System.out.println(nome + " tem " + idade + " anos"));
        
        // getOrDefault
        int idadeDavid = idades.getOrDefault("David", 0);
        System.out.println("Idade de David: " + idadeDavid);
        
        // putIfAbsent
        idades.putIfAbsent("Alice", 99); // Não altera, já existe
        idades.putIfAbsent("David", 28); // Adiciona
        System.out.println("Após putIfAbsent: " + idades);
        
        // compute
        idades.compute("Alice", (k, v) -> v + 1); // Incrementa
        System.out.println("Após compute: " + idades);
        
        // merge
        idades.merge("David", 1, Integer::sum); // Adiciona 1
        System.out.println("Após merge: " + idades);
    }
    
    // ============================================
    // QUEUE e DEQUE
    // ============================================
    
    public void demonstrarQueue() {
        // Queue (FIFO) - implementado com LinkedList
        Queue<String> fila = new LinkedList<>();
        fila.offer("Primeiro");
        fila.offer("Segundo");
        fila.offer("Terceiro");
        
        System.out.println("Fila: " + fila);
        System.out.println("Próximo: " + fila.peek()); // Olha sem remover
        System.out.println("Removendo: " + fila.poll()); // Remove e retorna
        System.out.println("Fila após poll: " + fila);
        
        // Deque (Double-ended queue)
        Deque<String> deque = new ArrayDeque<>();
        deque.addFirst("Primeiro");
        deque.addLast("Último");
        deque.addFirst("Novo Primeiro");
        
        System.out.println("Deque: " + deque);
        System.out.println("Primeiro: " + deque.getFirst());
        System.out.println("Último: " + deque.getLast());
        
        // PriorityQueue - ordenada por prioridade
        PriorityQueue<Integer> prioridade = new PriorityQueue<>();
        prioridade.offer(5);
        prioridade.offer(1);
        prioridade.offer(3);
        prioridade.offer(2);
        
        System.out.println("\nPriorityQueue (ordem de remoção):");
        while (!prioridade.isEmpty()) {
            System.out.println("  " + prioridade.poll());
        }
    }
    
    // ============================================
    // UTILITÁRIOS - Collections
    // ============================================
    
    public void demonstrarUtilitarios() {
        List<Integer> numeros = new ArrayList<>(Arrays.asList(5, 2, 8, 1, 9, 3));
        
        // Ordenar
        Collections.sort(numeros);
        System.out.println("Ordenado: " + numeros);
        
        // Ordenar reverso
        Collections.sort(numeros, Collections.reverseOrder());
        System.out.println("Reverso: " + numeros);
        
        // Embaralhar
        Collections.shuffle(numeros);
        System.out.println("Embaralhado: " + numeros);
        
        // Máximo e mínimo
        System.out.println("Máximo: " + Collections.max(numeros));
        System.out.println("Mínimo: " + Collections.min(numeros));
        
        // Frequência
        List<String> lista = Arrays.asList("A", "B", "A", "C", "A");
        System.out.println("Frequência de 'A': " + Collections.frequency(lista, "A"));
        
        // Busca binária (lista deve estar ordenada)
        Collections.sort(numeros);
        int index = Collections.binarySearch(numeros, 5);
        System.out.println("Índice do 5: " + index);
        
        // Coleções imutáveis
        List<String> imutavel = Collections.unmodifiableList(new ArrayList<>(lista));
        // imutavel.add("D"); // Lança UnsupportedOperationException
        
        // Coleções singleton
        Set<String> singleton = Collections.singleton("Único");
        List<String> singletonList = Collections.singletonList("Item");
        Map<String, String> singletonMap = Collections.singletonMap("chave", "valor");
        
        // Coleções vazias
        List<String> listaVazia = Collections.emptyList();
        Set<String> setVazio = Collections.emptySet();
        Map<String, String> mapVazio = Collections.emptyMap();
    }
    
    // ============================================
    // BOAS PRÁTICAS
    // ============================================
    
    public void boasPraticas() {
        //  Programar para interfaces
        List<String> lista = new ArrayList<>(); // Bom
        // ArrayList<String> lista = new ArrayList<>(); // Evitar
        
        //  Usar diamond operator <>
        Map<String, Integer> map = new HashMap<>();
        
        //  Escolher a implementação certa
        // ArrayList para acesso aleatório
        // LinkedList para inserções/remoções frequentes
        // HashSet para unicidade sem ordem
        // TreeSet para unicidade com ordem
        // HashMap para mapas gerais
        // TreeMap para mapas ordenados
        
        //  Definir capacidade inicial quando possível
        List<String> grandeLista = new ArrayList<>(10000);
        Map<String, String> grandeMap = new HashMap<>(1000);
        
        //  Usar isEmpty() ao invés de size() == 0
        if (lista.isEmpty()) { // Melhor
            System.out.println("Vazia");
        }
        
        //  Para thread-safe, usar Collections.synchronizedXXX
        // Ou melhor: ConcurrentHashMap, CopyOnWriteArrayList, etc.
    }
    
    public static void main(String[] args) {
        CollectionsExample example = new CollectionsExample();
        
        System.out.println("=== LIST ===");
        example.demonstrarList();
        
        System.out.println("\n=== SET ===");
        example.demonstrarSet();
        
        System.out.println("\n=== MAP ===");
        example.demonstrarMap();
        
        System.out.println("\n=== QUEUE ===");
        example.demonstrarQueue();
        
        System.out.println("\n=== UTILITÁRIOS ===");
        example.demonstrarUtilitarios();
    }
}
