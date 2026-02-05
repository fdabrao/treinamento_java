package com.avanade.curso.concorrencia;

import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.util.concurrent.locks.*;
import java.util.*;

/**
 * MULTITHREADING E CONCORRENCIA EM JAVA
 * 
 * Java suporta programacao concorrente desde suas primeiras versoes.
 * Com o pacote java.util.concurrent (Java 5+), tornou-se muito mais
 * facil e seguro trabalhar com multiplas threads.
 * 
 * Conceitos Fundamentais:
 * - Thread: Unidade basica de execucao
 * - Processo: Programa em execucao com seu proprio espaco de memoria
 * - Concorrencia: Multiplas threads executando simultaneamente
 * - Paralelismo: Multiplas threads executando ao mesmo tempo (multi-core)
 * 
 * Desafios da Concorrencia:
 * - Race Condition: Duas threads acessando dados compartilhados simultaneamente
 * - Deadlock: Threads bloqueadas esperando umas pelas outras
 * - Starvation: Thread nunca obtem acesso ao recurso
 * - Livelock: Threads continuam mudando estado sem progredir
 */

public class ConcorrenciaExample {
    
    // ============================================
    // CRIANDO THREADS
    // ============================================
    
    /**
     * Forma 1: Estendendo Thread
     */
    static class MinhaThread extends Thread {
        private String nome;
        
        public MinhaThread(String nome) {
            this.nome = nome;
        }
        
        @Override
        public void run() {
            System.out.println(nome + " iniciou - Thread ID: " + Thread.currentThread().getId());
            try {
                Thread.sleep(1000); // Simula trabalho
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println(nome + " terminou");
        }
    }
    
    /**
     * Forma 2: Implementando Runnable (preferido)
     * - Permite estender outra classe
     * - Melhor separacao de responsabilidades
     */
    static class MeuRunnable implements Runnable {
        private String nome;
        
        public MeuRunnable(String nome) {
            this.nome = nome;
        }
        
        @Override
        public void run() {
            System.out.println(nome + " executando - " + Thread.currentThread().getName());
        }
    }
    
    /**
     * Forma 3: Usando Lambda (Java 8+)
     */
    public void exemploLambda() {
        Thread t1 = new Thread(() -> {
            System.out.println("Thread com Lambda");
        });
        
        t1.start();
    }
    
    // ============================================
    // SYNCHRONIZED - BLOQUEIO INTRINSECO
    // ============================================
    
    /**
     * Problema: Race Condition sem sincronizacao
     */
    static class ContadorInseguro {
        private int contador = 0;
        
        public void incrementar() {
            contador++; // NAO e atomico! (le-modifica-escreve)
        }
        
        public int getContador() {
            return contador;
        }
    }
    
    /**
     * Solucao 1: Metodo synchronized
     * - Usa o monitor do objeto (this)
     * - Apenas uma thread por vez pode executar
     */
    static class ContadorSincronizado {
        private int contador = 0;
        
        public synchronized void incrementar() {
            contador++;
        }
        
        public synchronized int getContador() {
            return contador;
        }
    }
    
    /**
     * Solucao 2: Bloco synchronized
     * - Mais granular, sincroniza apenas o necessario
     */
    static class ContadorBlocoSincronizado {
        private int contador = 0;
        private final Object lock = new Object();
        
        public void incrementar() {
            synchronized (lock) {
                contador++;
            }
        }
    }
    
    // ============================================
    // VOLATILE - VISIBILIDADE ENTRE THREADS
    // ============================================
    
    /**
     * volatile garante:
     * 1. Visibilidade: Todas as threads veem o valor atualizado
     * 2. Ordenacao: Impede reordenacao de instrucoes
     * 
     * NAO garante atomicidade!
     */
    static class FlagCompartilhada {
        private volatile boolean running = true;
        
        public void parar() {
            running = false;
        }
        
        public boolean isRunning() {
            return running;
        }
    }
    
    // ============================================
    // ATOMIC CLASSES - OPERACOES ATOMICAS
    // ============================================
    
    /**
     * Classes atomicas usam CAS (Compare-And-Swap)
     * - Operacoes atomicas sem bloqueio
     * - Mais eficiente que synchronized para contadores simples
     */
    static class ContadorAtomico {
        private AtomicInteger contador = new AtomicInteger(0);
        
        public void incrementar() {
            contador.incrementAndGet();
        }
        
        public void adicionar(int valor) {
            contador.addAndGet(valor);
        }
        
        public int getContador() {
            return contador.get();
        }
        
        // Operacao atomica complexa
        public boolean compararEIncrementar(int esperado, int novo) {
            return contador.compareAndSet(esperado, novo);
        }
    }
    
    // ============================================
    // LOCKS EXPLICITOS (REENTRANTLOCK)
    // ============================================
    
    /**
     * ReentrantLock oferece mais controle que synchronized:
     * - Tenta adquirir lock com timeout
     * - Lock interruptivel
     * - Lock justo (fair)
     * - Multiplas Condition variables
     */
    static class ContadorComLock {
        private int contador = 0;
        private final Lock lock = new ReentrantLock();
        
        public void incrementar() {
            lock.lock();
            try {
                contador++;
            } finally {
                lock.unlock(); // Sempre libera no finally!
            }
        }
        
        public boolean tentarIncrementar(long timeout, TimeUnit unit) 
                throws InterruptedException {
            if (lock.tryLock(timeout, unit)) {
                try {
                    contador++;
                    return true;
                } finally {
                    lock.unlock();
                }
            }
            return false;
        }
    }
    
    // ============================================
    // EXECUTORS - THREAD POOLS
    // ============================================
    
    /**
     * Executors gerenciam um pool de threads reutilizaveis
     * - Evita criar/destruir threads frequentemente
     * - Controla numero maximo de threads
     * - Facilita gerenciamento de tarefas
     */
    
    public void exemploExecutors() throws Exception {
        // 1. Fixed Thread Pool
        // Numero fixo de threads
        ExecutorService fixedPool = Executors.newFixedThreadPool(4);
        
        // 2. Cached Thread Pool
        // Cria threads conforme necessario, reutiliza ociosas
        ExecutorService cachedPool = Executors.newCachedThreadPool();
        
        // 3. Single Thread Executor
        // Apenas uma thread (sequencial)
        ExecutorService singleThread = Executors.newSingleThreadExecutor();
        
        // 4. Scheduled Thread Pool
        // Agenda execucoes periodicas
        ScheduledExecutorService scheduled = Executors.newScheduledThreadPool(2);
        
        // Submetendo tarefas
        fixedPool.submit(() -> System.out.println("Tarefa 1"));
        fixedPool.submit(() -> System.out.println("Tarefa 2"));
        
        // Agendando tarefas
        scheduled.schedule(() -> System.out.println("Executa uma vez"), 5, TimeUnit.SECONDS);
        scheduled.scheduleAtFixedRate(
            () -> System.out.println("A cada 10 segundos"), 
            0, 10, TimeUnit.SECONDS
        );
        
        // Desligar executor (IMPORTANTE!)
        fixedPool.shutdown();
        if (!fixedPool.awaitTermination(60, TimeUnit.SECONDS)) {
            fixedPool.shutdownNow(); // Forca terminacao
        }
    }
    
    // ============================================
    // CALLABLE E FUTURE
    // ============================================
    
    /**
     * Callable: Como Runnable, mas retorna resultado
     * Future: Representa resultado futuro de uma computacao
     */
    public void exemploCallableFuture() throws Exception {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        
        // Submete tarefa que retorna valor
        Future<Integer> future = executor.submit(() -> {
            Thread.sleep(1000);
            return 42;
        });
        
        // Faz outras coisas enquanto espera...
        
        // Obtem resultado (bloqueia se ainda nao terminou)
        Integer resultado = future.get();
        System.out.println("Resultado: " + resultado);
        
        // Com timeout
        try {
            Integer resultadoComTimeout = future.get(2, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            System.out.println("Timeout!");
        }
        
        executor.shutdown();
    }
    
    // ============================================
    // COMPLETABLEFUTURE - PROGRAMACAO ASSINCRONA
    // ============================================
    
    /**
     * CompletableFuture (Java 8+): Futuro que pode ser completado manualmente
     * - Compoe operacoes assincronas
     * - Callbacks (thenApply, thenAccept, thenCompose)
     * - Combinacao de futures
     * - Tratamento de excecoes
     */
    public void exemploCompletableFuture() {
        // Criacao
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            return "Resultado";
        });
        
        // Encadeamento
        future.thenApply(String::toUpperCase)    // Transforma
              .thenAccept(System.out::println);   // Consome
        
        // Combinacao
        CompletableFuture<String> f1 = CompletableFuture.supplyAsync(() -> "Hello");
        CompletableFuture<String> f2 = CompletableFuture.supplyAsync(() -> "World");
        
        CompletableFuture<String> combinado = f1.thenCombine(f2, (s1, s2) -> s1 + " " + s2);
        
        // Tratamento de excecao
        CompletableFuture<String> comErro = CompletableFuture.<String>supplyAsync(() -> {
            throw new RuntimeException("Erro!");
        }).exceptionally(ex -> "Valor padrao");
    }
    
    // ============================================
    // COLECOES CONCORRENTES
    // ============================================
    
    public void exemploColecoesConcorrentes() {
        // ConcurrentHashMap
        // Map thread-safe sem bloquear todo o mapa
        // Usa segmentacao (lock em partes do mapa)
        ConcurrentHashMap<String, Integer> mapa = new ConcurrentHashMap<>();
        mapa.put("chave1", 100);
        mapa.computeIfAbsent("chave2", k -> 200);
        mapa.merge("chave1", 50, Integer::sum); // Atomico
        
        // CopyOnWriteArrayList
        // Cria copia do array em cada escrita
        // Otimo para leituras frequentes, escritas raras
        CopyOnWriteArrayList<String> lista = new CopyOnWriteArrayList<>();
        lista.add("item1");
        
        // BlockingQueue
        // Fila thread-safe com operacoes bloqueantes
        BlockingQueue<String> fila = new LinkedBlockingQueue<>(10);
        try {
            fila.put("elemento");       // Bloqueia se cheia
            String elemento = fila.take(); // Bloqueia se vazia
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // ConcurrentLinkedQueue
        // Fila nao bloqueante, alta performance
        ConcurrentLinkedQueue<String> filaNaoBloqueante = new ConcurrentLinkedQueue<>();
        filaNaoBloqueante.offer("item");
        String item = filaNaoBloqueante.poll();
    }
    
    // ============================================
    // SEMAPHORE - CONTROLE DE ACESSO
    // ============================================
    
    /**
     * Semaphore controla quantas threads podem acessar um recurso simultaneamente
     */
    static class PoolConexoes {
        private final Semaphore semaphore;
        private final List<String> conexoesDisponiveis;
        
        public PoolConexoes(int maxConexoes) {
            this.semaphore = new Semaphore(maxConexoes);
            this.conexoesDisponiveis = new ArrayList<>();
            for (int i = 0; i < maxConexoes; i++) {
                conexoesDisponiveis.add("Conexao-" + i);
            }
        }
        
        public String adquirirConexao() throws InterruptedException {
            semaphore.acquire();
            synchronized (conexoesDisponiveis) {
                return conexoesDisponiveis.remove(0);
            }
        }
        
        public void liberarConexao(String conexao) {
            synchronized (conexoesDisponiveis) {
                conexoesDisponiveis.add(conexao);
            }
            semaphore.release();
        }
    }
    
    // ============================================
    // COUNTDOWNLATCH - SINCRONIZACAO
    // ============================================
    
    /**
     * CountDownLatch: Aguarda N eventos antes de prosseguir
     */
    public void exemploCountDownLatch() throws InterruptedException {
        int numeroThreads = 3;
        CountDownLatch latch = new CountDownLatch(numeroThreads);
        
        for (int i = 0; i < numeroThreads; i++) {
            final int id = i;
            new Thread(() -> {
                System.out.println("Thread " + id + " trabalhando...");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                System.out.println("Thread " + id + " terminou");
                latch.countDown(); // Decrementa contador
            }).start();
        }
        
        System.out.println("Aguardando todas as threads terminarem...");
        latch.await(); // Bloqueia ate contador chegar a zero
        System.out.println("Todas as threads terminaram!");
    }
    
    // ============================================
    // CYCLICBARRIER - BARRIER
    // ============================================
    
    /**
     * CyclicBarrier: Threads esperam umas pelas outras em um ponto
     */
    public void exemploCyclicBarrier() {
        int numeroParticipantes = 3;
        CyclicBarrier barrier = new CyclicBarrier(numeroParticipantes, () -> {
            System.out.println("Todos chegaram! Liberando...");
        });
        
        for (int i = 0; i < numeroParticipantes; i++) {
            final int id = i;
            new Thread(() -> {
                try {
                    System.out.println("Thread " + id + " chegou a barreira");
                    barrier.await(); // Espera todas as threads
                    System.out.println("Thread " + id + " continuou");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
    
    // ============================================
    // DEADLOCK - EXEMPLO E PREVENCAO
    // ============================================
    
    /**
     * Exemplo de Deadlock (NAO FACA ISSO!)
     */
    static class ExemploDeadlock {
        private final Object recurso1 = new Object();
        private final Object recurso2 = new Object();
        
        public void causarDeadlock() {
            Thread t1 = new Thread(() -> {
                synchronized (recurso1) {
                    System.out.println("Thread 1: Segurando recurso 1");
                    try { Thread.sleep(100); } catch (InterruptedException e) {}
                    
                    System.out.println("Thread 1: Esperando recurso 2");
                    synchronized (recurso2) { // BLOQUEIA AQUI
                        System.out.println("Thread 1: Segurando recurso 2");
                    }
                }
            });
            
            Thread t2 = new Thread(() -> {
                synchronized (recurso2) {
                    System.out.println("Thread 2: Segurando recurso 2");
                    try { Thread.sleep(100); } catch (InterruptedException e) {}
                    
                    System.out.println("Thread 2: Esperando recurso 1");
                    synchronized (recurso1) { // BLOQUEIA AQUI
                        System.out.println("Thread 2: Segurando recurso 1");
                    }
                }
            });
            
            t1.start();
            t2.start();
        }
    }
    
    // ============================================
    // DEMONSTRACAO PRINCIPAL
    // ============================================
    
    public static void main(String[] args) throws Exception {
        System.out.println("=== DEMONSTRACAO DE CONCORRENCIA ===\n");
        
        ConcorrenciaExample exemplo = new ConcorrenciaExample();
        
        // 1. Criando threads
        System.out.println("1. Criando Threads:");
        Thread t1 = new MinhaThread("Thread-1");
        Thread t2 = new Thread(new MeuRunnable("Runnable-1"));
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        
        // 2. Contador sincronizado
        System.out.println("\n2. Contador Sincronizado:");
        ContadorSincronizado contador = new ContadorSincronizado();
        ExecutorService executor = Executors.newFixedThreadPool(4);
        
        for (int i = 0; i < 1000; i++) {
            executor.submit(contador::incrementar);
        }
        
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);
        System.out.println("Contador final: " + contador.getContador());
        
        // 3. Colecoes concorrentes
        System.out.println("\n3. Colecoes Concorrentes:");
        exemplo.exemploColecoesConcorrentes();
        System.out.println("OK");
        
        System.out.println("\n=== FIM ===");
    }
}
