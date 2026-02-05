package com.avanade.curso.jvm;

import java.lang.management.*;
import java.util.*;

/**
 * JVM INTERNALS - FUNCIONAMENTO INTERNO DA JVM
 * 
 * A Java Virtual Machine (JVM) e a maquina virtual que executa bytecode Java.
 * Entender seu funcionamento e crucial para otimizacao e debugging.
 * 
 * Componentes Principais:
 * 1. Class Loader: Carrega classes na memoria
 * 2. Runtime Data Areas: Areas de memoria da JVM
 * 3. Execution Engine: Executa o bytecode
 * 4. Garbage Collector: Gerencia memoria automaticamente
 * 
 * Arquitetura da JVM:
 * 
 * +--------------------------------------------------+
 * |                  Class Loader                     |
 * |  (Bootstrap -> Extension -> Application)         |
 * +--------------------------------------------------+
 *                          |
 *                          v
 * +--------------------------------------------------+
 * |              Runtime Data Areas                   |
 * |  +------------+ +------------+ +-------------+   |
 * |  |   Heap     | |    Stack   | |   Method    |   |
 * |  | (objetos)  | | (frames)   | |    Area     |   |
 * |  +------------+ +------------+ +-------------+   |
 * |  +------------+ +------------+                   |
 * |  |   PC       | |  Native    |                   |
 * |  | Registers  | |    Stack   |                   |
 * |  +------------+ +------------+                   |
 * +--------------------------------------------------+
 *                          |
 *                          v
 * +--------------------------------------------------+
 * |              Execution Engine                     |
 * |  Interpreter -> JIT Compiler -> Native Code      |
 * +--------------------------------------------------+
 *                          |
 *                          v
 * +--------------------------------------------------+
 * |            Garbage Collector                      |
 * |         (G1, Parallel, CMS, ZGC)               |
 * +--------------------------------------------------+
 */

public class JVMInternalsExample {
    
    // ============================================
    // CLASS LOADING
    // ============================================
    
    /**
     * Hierarquia de ClassLoaders:
     * 
     * 1. Bootstrap ClassLoader (nativo)
     *    - Carrega classes do Java (java.lang.*, java.util.*)
     *    - Escrito em C/C++, nao e visivel em Java
     *    - rt.jar (Java 8) ou modules (Java 9+)
     * 
     * 2. Extension/Platform ClassLoader
     *    - Carrega extensoes do JDK
     *    - lib/ext (Java 8) ou modules de plataforma
     * 
     * 3. Application/System ClassLoader
     *    - Carrega classes do classpath da aplicacao
     *    - -cp ou CLASSPATH
     * 
     * Principio: Delegacao Parent-First
     * - ClassLoader pede ao pai primeiro
     * - So carrega se pai nao encontrar
     * - Evita carregar a mesma classe multiplas vezes
     */
    
    public void demonstrarClassLoaders() {
        // Obtem o ClassLoader da classe atual
        ClassLoader appLoader = getClass().getClassLoader();
        System.out.println("Application ClassLoader: " + appLoader);
        
        // Pai do Application e o Platform (Java 9+) ou Extension (Java 8)
        ClassLoader platformLoader = appLoader.getParent();
        System.out.println("Platform ClassLoader: " + platformLoader);
        
        // Pai do Platform e o Bootstrap (retorna null)
        ClassLoader bootstrapLoader = platformLoader.getParent();
        System.out.println("Bootstrap ClassLoader: " + bootstrapLoader);
        
        // ClassLoader de classes do Java core
        ClassLoader stringLoader = String.class.getClassLoader();
        System.out.println("String ClassLoader: " + stringLoader); // null = Bootstrap
    }
    
    // ============================================
    // RUNTIME DATA AREAS (AREAS DE MEMORIA)
    // ============================================
    
    /**
     * HEAP - Onde objetos sao alocados
     * 
     * Estrutura do Heap (antes Java 8):
     * +--------------------------------------------------+
     * |                  Young Generation                 |
     * |  +-----------+ +-----------+ +----------------+  |
     * |  |   Eden    | | Survivor 0| |   Survivor 1   |  |
     * |  |           | |   (S0)    | |     (S1)       |  |
     * |  +-----------+ +-----------+ +----------------+  |
     * +--------------------------------------------------+
     * |              Old Generation                       |
     * |         (Tenured Generation)                      |
     * +--------------------------------------------------+
     * |              Permanent Generation                 |
     * |              (metadados - ate Java 7)            |
     * +--------------------------------------------------+
     * 
     * Estrutura do Heap (Java 8+):
     * - Metaspace (fora do heap) substitui PermGen
     * - Metaspace usa memoria nativa (nao e limitado pelo heap)
     */
    
    public void demonstrarHeap() {
        // Obtem informacoes do heap
        Runtime runtime = Runtime.getRuntime();
        
        long maxMemory = runtime.maxMemory();      // Maximo que a JVM pode usar
        long totalMemory = runtime.totalMemory();  // Memoria atualmente alocada
        long freeMemory = runtime.freeMemory();    // Memoria livre na alocacao
        long usedMemory = totalMemory - freeMemory;
        
        System.out.println("=== Informacoes do Heap ===");
        System.out.println("Memoria Maxima: " + formatBytes(maxMemory));
        System.out.println("Memoria Alocada: " + formatBytes(totalMemory));
        System.out.println("Memoria Usada: " + formatBytes(usedMemory));
        System.out.println("Memoria Livre: " + formatBytes(freeMemory));
        
        // Informacoes detalhadas via ManagementFactory
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage heapUsage = memoryMXBean.getHeapMemoryUsage();
        
        System.out.println("\n=== Heap Memory Usage ===");
        System.out.println("Inicial: " + formatBytes(heapUsage.getInit()));
        System.out.println("Usada: " + formatBytes(heapUsage.getUsed()));
        System.out.println("Comprometida: " + formatBytes(heapUsage.getCommitted()));
        System.out.println("Maxima: " + formatBytes(heapUsage.getMax()));
    }
    
    /**
     * STACK - Pilha de execucao de metodos
     * 
     * - Cada thread tem sua propria pilha
     * - Contem frames (contexto de execucao de metodos)
     * - Tamanho configuravel: -Xss (ex: -Xss1m)
     * - StackOverflowError: pilha estoura (recursao infinita)
     */
    
    public void demonstrarStack() {
        // Informacoes da pilha da thread atual
        Thread currentThread = Thread.currentThread();
        System.out.println("Thread: " + currentThread.getName());
        System.out.println("Stack Trace:");
        
        StackTraceElement[] stackTrace = currentThread.getStackTrace();
        for (StackTraceElement element : stackTrace) {
            System.out.println("  at " + element);
        }
    }
    
    // Demonstra estouro de pilha
    private int contadorRecursao = 0;
    
    public void causarStackOverflow() {
        try {
            recursaoInfinita();
        } catch (StackOverflowError e) {
            System.out.println("StackOverflow apos " + contadorRecursao + " chamadas");
        }
    }
    
    private void recursaoInfinita() {
        contadorRecursao++;
        recursaoInfinita(); // Recursao infinita
    }
    
    // ============================================
    // GARBAGE COLLECTION (GC)
    // ============================================
    
    /**
     * Garbage Collection e o processo automatico de liberacao de memoria
     * ocupada por objetos que nao sao mais referenciados.
     * 
     * Algoritmos de GC:
     * 
     * 1. MARK AND SWEEP (Marcar e Varredura)
     *    - Fase Mark: Identifica objetos alcancaveis
     *    - Fase Sweep: Remove objetos nao alcancaveis
     *    - Fase Compact: Move objetos para eliminar fragmentacao
     * 
     * 2. COPY COLLECTION (Geracoes)
     *    - Usado na Young Generation
     *    - Move objetos vivos entre Survivor spaces
     *    - Objetos sobrevivem varias colecoes vao para Old Gen
     * 
     * Tipos de Garbage Collectors:
     * 
     * SERIAL GC (-XX:+UseSerialGC)
     * - Uma thread, pausa todas as outras
     * - Bom para aplicacoes pequenas/single-thread
     * 
     * PARALLEL GC (-XX:+UseParallelGC)
     * - Multiplas threads na Young Gen
     * - Throughput maximo
     * - Padrao em Java 8
     * 
     * CMS (Concurrent Mark Sweep) (-XX:+UseConcMarkSweepGC)
     * - Colecoes concurrentes (menor pausa)
     * - Deprecado no Java 9, removido no 14
     * 
     * G1 GC (-XX:+UseG1GC)
     * - Divide heap em regioes
     * - Colecoes incrementais
     * - Target de pausa configuravel (default 200ms)
     * - Padrao em Java 9+
     * 
     * ZGC (-XX:+UseZGC) - Java 11+
     * - Latencia extremamente baixa (< 10ms)
     * - Escalavel para heaps enormes (TB)
     * - Concorrente e paralelo
     * 
     * SHENANDOAH (-XX:+UseShenandoahGC)
     * - Similar ao ZGC
     * - Reduz pauses independente do tamanho do heap
     */
    
    public void demonstrarGC() {
        // Obtem informacoes dos Garbage Collectors
        List<GarbageCollectorMXBean> gcBeans = ManagementFactory.getGarbageCollectorMXBeans();
        
        System.out.println("=== Garbage Collectors ===");
        for (GarbageCollectorMXBean gcBean : gcBeans) {
            System.out.println("Nome: " + gcBean.getName());
            System.out.println("  Colecoes: " + gcBean.getCollectionCount());
            System.out.println("  Tempo total: " + gcBean.getCollectionTime() + "ms");
            
            // Memoria gerenciada por este GC
            for (String memoryPool : gcBean.getMemoryPoolNames()) {
                System.out.println("  Pool: " + memoryPool);
            }
        }
        
        // Forca uma colecao (nao recomendado em producao)
        System.out.println("\nSolicitando GC...");
        long memoriaAntes = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        System.gc(); // Apenas uma sugestao a JVM
        try {
            Thread.sleep(1000); // Da tempo ao GC
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        long memoriaDepois = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        
        System.out.println("Memoria antes: " + formatBytes(memoriaAntes));
        System.out.println("Memoria depois: " + formatBytes(memoriaDepois));
        System.out.println("Liberado: " + formatBytes(memoriaAntes - memoriaDepois));
    }
    
    /**
     * Demonstra como objetos sobrevivem ou sao coletados
     */
    public void demonstrarCicloDeVida() {
        System.out.println("=== Ciclo de Vida de Objetos ===\n");
        
        // Objeto criado no Eden
        System.out.println("1. Criando objeto...");
        Object objeto = new Object();
        System.out.println("   Objeto criado no Eden");
        
        // Objeto continua referenciado
        System.out.println("2. Mantendo referencia...");
        System.out.println("   Objeto sobrevive na Young Gen");
        
        // Objeto vai para Old Gen apos varias colecoes
        System.out.println("3. Apos varias colecoes...");
        System.out.println("   Objeto promovido para Old Gen (Tenured)");
        
        // Referencia removida
        System.out.println("4. Removendo referencia...");
        objeto = null;
        System.out.println("   Objeto elegivel para coleta");
        
        System.out.println("\nColeta de Geracoes:");
        System.out.println("  - Minor GC: Young Generation (rapido, frequente)");
        System.out.println("  - Major GC: Old Generation (lento, menos frequente)");
        System.out.println("  - Full GC: Todo o heap (mais lento)");
    }
    
    // ============================================
    // MEMORY LEAKS
    // ============================================
    
    /**
     * Exemplos comuns de memory leaks em Java
     */
    
    // Exemplo 1: Cache sem limite
    static class CacheProblematico {
        private static Map<String, Object> cache = new HashMap<>();
        
        public void adicionar(String chave, Object valor) {
            cache.put(chave, valor); // Nunca remove! Memory leak!
        }
    }
    
    // Solucao: WeakHashMap ou cache com expiracao
    static class CacheCorreto {
        // Opcao 1: WeakHashMap (entradas removidas quando chave nao e mais usada)
        private Map<String, Object> weakCache = new WeakHashMap<>();
        
        // Opcao 2: Cache com limite (Guava, Caffeine, EhCache)
        // private Cache<String, Object> boundedCache = CacheBuilder.newBuilder()
        //     .maximumSize(1000)
        //     .expireAfterWrite(10, TimeUnit.MINUTES)
        //     .build();
    }
    
    // Exemplo 2: Listeners nao removidos
    static class GerenciadorEventos {
        private List<Listener> listeners = new ArrayList<>();
        
        public void adicionarListener(Listener l) {
            listeners.add(l);
        }
        
        // PROBLEMA: Nao ha metodo remover!
        // Solucao: Usar WeakReference ou remover explicitamente
    }
    
    interface Listener {
        void onEvent();
    }
    
    // ============================================
    // JIT COMPILER
    // ============================================
    
    /**
     * JIT (Just-In-Time) Compiler
     * 
     - Interpretador executa bytecode inicialmente
     * - JVM identifica "hot spots" (codigo executado frequentemente)
     * - JIT compila bytecode para codigo nativo da maquina
     * - Proximas execucoes usam codigo nativo (muito mais rapido)
     * 
     * Niveis de Otimizacao:
     * - C1 (Client Compiler): Compilacao rapida, otimizacoes basicas
     * - C2 (Server Compiler): Compilacao lenta, otimizacoes agressivas
     * 
     * Otimizacoes comuns:
     * - Inlining: Substitui chamada de metodo pelo corpo do metodo
     * - Escape Analysis: Identifica objetos que nao escapam do metodo
     * - Dead Code Elimination: Remove codigo inalcancavel
     */
    
    public void demonstrarJIT() {
        // Compilacao JIT pode ser monitorada
        CompilationMXBean compilationBean = ManagementFactory.getCompilationMXBean();
        
        System.out.println("=== JIT Compiler ===");
        System.out.println("Nome: " + compilationBean.getName());
        
        if (compilationBean.isCompilationTimeMonitoringSupported()) {
            System.out.println("Tempo total de compilacao: " + 
                compilationBean.getTotalCompilationTime() + "ms");
        }
        
        // Demonstracao de otimizacao
        System.out.println("\nDemonstracao de JIT (executar varias vezes):");
        long inicio = System.nanoTime();
        
        // Metodo que sera otimizado pelo JIT apos varias execucoes
        int resultado = 0;
        for (int i = 0; i < 1000000; i++) {
            resultado += calcular(i);
        }
        
        long fim = System.nanoTime();
        System.out.println("Resultado: " + resultado);
        System.out.println("Tempo: " + (fim - inicio) / 1_000_000 + "ms");
        System.out.println("(Primeira execucao: interpretada | Proximas: compilada JIT)");
    }
    
    // Metodo candidato a otimizacao JIT
    private int calcular(int x) {
        return x * x + 2 * x + 1;
    }
    
    // ============================================
    // UTILITARIO
    // ============================================
    
    private String formatBytes(long bytes) {
        if (bytes < 1024) return bytes + " B";
        if (bytes < 1024 * 1024) return String.format("%.2f KB", bytes / 1024.0);
        if (bytes < 1024 * 1024 * 1024) return String.format("%.2f MB", bytes / (1024.0 * 1024));
        return String.format("%.2f GB", bytes / (1024.0 * 1024 * 1024));
    }
    
    // ============================================
    // DEMONSTRACAO PRINCIPAL
    // ============================================
    
    public static void main(String[] args) {
        JVMInternalsExample exemplo = new JVMInternalsExample();
        
        System.out.println("=== JVM INTERNALS ===\n");
        
        System.out.println("1. CLASS LOADERS");
        exemplo.demonstrarClassLoaders();
        
        System.out.println("\n2. HEAP MEMORY");
        exemplo.demonstrarHeap();
        
        System.out.println("\n3. STACK TRACE");
        exemplo.demonstrarStack();
        
        System.out.println("\n4. GARBAGE COLLECTION");
        exemplo.demonstrarGC();
        
        System.out.println("\n5. CICLO DE VIDA DOS OBJETOS");
        exemplo.demonstrarCicloDeVida();
        
        System.out.println("\n6. JIT COMPILER");
        exemplo.demonstrarJIT();
        
        System.out.println("\n=== FIM ===");
    }
}
