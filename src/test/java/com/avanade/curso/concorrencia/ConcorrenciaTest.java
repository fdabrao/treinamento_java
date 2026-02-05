package com.avanade.curso.concorrencia;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Timeout;
import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.util.concurrent.locks.*;
import java.util.*;

/**
 * Testes para Multithreading e Concorrencia
 */
class ConcorrenciaTest {
    
    // ============================================
    // Testes de Threads Basicas
    // ============================================
    
    @Test
    @DisplayName("Deve criar e executar thread estendendo Thread")
    @Timeout(2)
    void threadEstendendo() throws InterruptedException {
        final boolean[] executou = {false};
        
        Thread thread = new Thread(() -> {
            executou[0] = true;
        });
        
        thread.start();
        thread.join();
        
        assertTrue(executou[0]);
    }
    
    @Test
    @DisplayName("Deve criar thread com Runnable")
    @Timeout(2)
    void threadRunnable() throws InterruptedException {
        final String[] resultado = {null};
        
        Runnable tarefa = () -> resultado[0] = "Executado";
        Thread thread = new Thread(tarefa);
        
        thread.start();
        thread.join();
        
        assertEquals("Executado", resultado[0]);
    }
    
    @Test
    @DisplayName("Deve obter ID e nome da thread")
    void threadInfo() {
        Thread thread = Thread.currentThread();
        
        assertTrue(thread.getId() > 0);
        assertNotNull(thread.getName());
        assertEquals("main", thread.getName()); // Thread do teste
    }
    
    // ============================================
    // Testes de Sincronizacao
    // ============================================
    
    @Test
    @DisplayName("Contador sem sincronizacao deve ter problema de concorrencia")
    @Timeout(5)
    void contadorInseguro() throws InterruptedException {
        ConcorrenciaExample.ContadorInseguro contador = new ConcorrenciaExample.ContadorInseguro();
        ExecutorService executor = Executors.newFixedThreadPool(4);
        
        // 1000 incrementos em paralelo
        for (int i = 0; i < 1000; i++) {
            executor.submit(contador::incrementar);
        }
        
        executor.shutdown();
        executor.awaitTermination(3, TimeUnit.SECONDS);
        
        // Pode ser menor que 1000 devido a race condition
        // (nao testamos o valor exato pois e nao deterministico)
        System.out.println("Contador inseguro: " + contador.getContador());
    }
    
    @Test
    @DisplayName("Contador sincronizado deve ser thread-safe")
    @Timeout(5)
    void contadorSincronizado() throws InterruptedException {
        ConcorrenciaExample.ContadorSincronizado contador = new ConcorrenciaExample.ContadorSincronizado();
        ExecutorService executor = Executors.newFixedThreadPool(10);
        
        for (int i = 0; i < 1000; i++) {
            executor.submit(contador::incrementar);
        }
        
        executor.shutdown();
        executor.awaitTermination(3, TimeUnit.SECONDS);
        
        assertEquals(1000, contador.getContador());
    }
    
    // ============================================
    // Testes de Atomic
    // ============================================
    
    @Test
    @DisplayName("AtomicInteger deve ser thread-safe")
    @Timeout(5)
    void atomicInteger() throws InterruptedException {
        AtomicInteger contador = new AtomicInteger(0);
        ExecutorService executor = Executors.newFixedThreadPool(10);
        
        for (int i = 0; i < 1000; i++) {
            executor.submit(contador::incrementAndGet);
        }
        
        executor.shutdown();
        executor.awaitTermination(3, TimeUnit.SECONDS);
        
        assertEquals(1000, contador.get());
    }
    
    @Test
    @DisplayName("AtomicInteger deve suportar operacoes atomicas complexas")
    void atomicOperacoes() {
        AtomicInteger valor = new AtomicInteger(10);
        
        // getAndAdd: retorna valor atual e depois adiciona
        int anterior = valor.getAndAdd(5);
        assertEquals(10, anterior);
        assertEquals(15, valor.get());
        
        // compareAndSet: atomico
        boolean sucesso = valor.compareAndSet(15, 100);
        assertTrue(sucesso);
        assertEquals(100, valor.get());
        
        // Tentativa falha (valor atual nao e 999)
        boolean falha = valor.compareAndSet(999, 200);
        assertFalse(falha);
        assertEquals(100, valor.get());
    }
    
    @Test
    @DisplayName("AtomicBoolean deve funcionar corretamente")
    void atomicBoolean() {
        AtomicBoolean flag = new AtomicBoolean(false);
        
        assertFalse(flag.get());
        
        flag.set(true);
        assertTrue(flag.get());
        
        // getAndSet retorna valor anterior
        boolean anterior = flag.getAndSet(false);
        assertTrue(anterior);
        assertFalse(flag.get());
    }
    
    // ============================================
    // Testes de Locks
    // ============================================
    
    @Test
    @DisplayName("ReentrantLock deve garantir exclusao mutua")
    @Timeout(5)
    void reentrantLock() throws InterruptedException {
        Lock lock = new ReentrantLock();
        AtomicInteger contador = new AtomicInteger(0);
        ExecutorService executor = Executors.newFixedThreadPool(5);
        
        for (int i = 0; i < 100; i++) {
            executor.submit(() -> {
                lock.lock();
                try {
                    contador.incrementAndGet();
                } finally {
                    lock.unlock();
                }
            });
        }
        
        executor.shutdown();
        executor.awaitTermination(3, TimeUnit.SECONDS);
        
        assertEquals(100, contador.get());
    }
    
    @Test
    @DisplayName("TryLock com timeout deve funcionar")
    @Timeout(3)
    void tryLockTimeout() throws Exception {
        Lock lock = new ReentrantLock();
        
        // Thread 1 adquire lock
        lock.lock();
        try {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            Future<Boolean> future = executor.submit(() -> {
                // Tenta adquirir com timeout curto
                return lock.tryLock(100, TimeUnit.MILLISECONDS);
            });
            
            // Deve retornar false (lock ja ocupado)
            Boolean conseguiu = future.get(1, TimeUnit.SECONDS);
            assertFalse(conseguiu);
            
            executor.shutdown();
        } finally {
            lock.unlock();
        }
    }
    
    // ============================================
    // Testes de Executors
    // ============================================
    
    @Test
    @DisplayName("FixedThreadPool deve executar tarefas")
    @Timeout(3)
    void fixedThreadPool() throws InterruptedException, ExecutionException {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        AtomicInteger contador = new AtomicInteger(0);
        
        Future<?> future = executor.submit(() -> contador.incrementAndGet());
        
        future.get(); // Aguarda conclusao
        assertEquals(1, contador.get());
        
        executor.shutdown();
    }
    
    @Test
    @DisplayName("Callable deve retornar resultado")
    @Timeout(3)
    void callable() throws Exception {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        
        Callable<Integer> tarefa = () -> {
            Thread.sleep(100);
            return 42;
        };
        
        Future<Integer> future = executor.submit(tarefa);
        Integer resultado = future.get();
        
        assertEquals(42, resultado);
        executor.shutdown();
    }
    
    @Test
    @DisplayName("Future com timeout deve lancar excecao")
    @Timeout(3)
    void futureTimeout() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        
        Future<?> future = executor.submit(() -> {
            try {
                Thread.sleep(5000); // Tarefa longa
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        
        assertThrows(TimeoutException.class, () -> {
            future.get(100, TimeUnit.MILLISECONDS);
        });
        
        future.cancel(true);
        executor.shutdownNow();
    }
    
    // ============================================
    // Testes de Colecoes Concorrentes
    // ============================================
    
    @Test
    @DisplayName("ConcurrentHashMap deve ser thread-safe")
    @Timeout(5)
    void concurrentHashMap() throws InterruptedException {
        ConcurrentHashMap<String, Integer> mapa = new ConcurrentHashMap<>();
        ExecutorService executor = Executors.newFixedThreadPool(10);
        
        // Multiplas threads inserindo
        for (int i = 0; i < 100; i++) {
            final int valor = i;
            executor.submit(() -> mapa.put("chave-" + valor, valor));
        }
        
        executor.shutdown();
        executor.awaitTermination(3, TimeUnit.SECONDS);
        
        assertEquals(100, mapa.size());
    }
    
    @Test
    @DisplayName("CopyOnWriteArrayList deve permitir leitura concorrente")
    @Timeout(3)
    void copyOnWriteArrayList() {
        CopyOnWriteArrayList<String> lista = new CopyOnWriteArrayList<>();
        lista.add("item1");
        lista.add("item2");
        
        // Leitura segura enquanto outra thread modifica
        for (String item : lista) {
            assertNotNull(item);
            // Pode modificar lista durante iteracao
            lista.add("novo");
        }
        
        // Lista foi modificada
        assertTrue(lista.size() > 2);
    }
    
    @Test
    @DisplayName("BlockingQueue deve bloquear em take quando vazia")
    @Timeout(3)
    void blockingQueue() throws InterruptedException {
        BlockingQueue<String> fila = new LinkedBlockingQueue<>(1);
        
        // Insere elemento
        fila.put("elemento");
        
        // Remove (nao bloqueia pois tem elemento)
        String elemento = fila.take();
        assertEquals("elemento", elemento);
        
        // Thread separada para inserir depois
        Executors.newSingleThreadScheduledExecutor().schedule(() -> {
            try {
                fila.put("novo-elemento");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, 100, TimeUnit.MILLISECONDS);
        
        // Take bloqueia ate ter elemento
        String novo = fila.take();
        assertEquals("novo-elemento", novo);
    }
    
    // ============================================
    // Testes de Sincronizadores
    // ============================================
    
    @Test
    @DisplayName("CountDownLatch deve aguardar contagem zerar")
    @Timeout(3)
    void countDownLatch() throws InterruptedException {
        int numeroThreads = 3;
        CountDownLatch latch = new CountDownLatch(numeroThreads);
        AtomicInteger contador = new AtomicInteger(0);
        
        for (int i = 0; i < numeroThreads; i++) {
            new Thread(() -> {
                contador.incrementAndGet();
                latch.countDown();
            }).start();
        }
        
        latch.await(); // Aguarda todas as threads
        assertEquals(numeroThreads, contador.get());
    }
    
    @Test
    @DisplayName("Semaphore deve limitar acesso")
    @Timeout(3)
    void semaphore() throws InterruptedException {
        Semaphore semaphore = new Semaphore(2); // Max 2 acessos simultaneos
        AtomicInteger simultaneos = new AtomicInteger(0);
        AtomicInteger maxSimultaneos = new AtomicInteger(0);
        
        ExecutorService executor = Executors.newFixedThreadPool(5);
        
        for (int i = 0; i < 5; i++) {
            executor.submit(() -> {
                try {
                    semaphore.acquire();
                    int atual = simultaneos.incrementAndGet();
                    maxSimultaneos.updateAndGet(v -> Math.max(v, atual));
                    
                    Thread.sleep(100); // Simula trabalho
                    
                    simultaneos.decrementAndGet();
                    semaphore.release();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }
        
        executor.shutdown();
        executor.awaitTermination(2, TimeUnit.SECONDS);
        
        assertTrue(maxSimultaneos.get() <= 2);
    }
    
    @Test
    @DisplayName("CyclicBarrier deve sincronizar threads")
    @Timeout(3)
    void cyclicBarrier() throws InterruptedException {
        int numeroThreads = 3;
        CyclicBarrier barrier = new CyclicBarrier(numeroThreads);
        AtomicInteger chegaram = new AtomicInteger(0);
        
        ExecutorService executor = Executors.newFixedThreadPool(numeroThreads);
        
        for (int i = 0; i < numeroThreads; i++) {
            executor.submit(() -> {
                try {
                    chegaram.incrementAndGet();
                    barrier.await(); // Espera todas as threads
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        
        executor.shutdown();
        executor.awaitTermination(2, TimeUnit.SECONDS);
        
        assertEquals(numeroThreads, chegaram.get());
    }
    
    // ============================================
    // Testes de CompletableFuture
    // ============================================
    
    @Test
    @DisplayName("CompletableFuture deve completar assincronamente")
    @Timeout(3)
    void completableFuture() throws Exception {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return "Resultado";
        });
        
        String resultado = future.get();
        assertEquals("Resultado", resultado);
    }
    
    @Test
    @DisplayName("CompletableFuture deve encadear operacoes")
    @Timeout(3)
    void completableFutureChain() throws Exception {
        CompletableFuture<String> future = CompletableFuture
            .supplyAsync(() -> "hello")
            .thenApply(String::toUpperCase)
            .thenApply(s -> s + " WORLD");
        
        assertEquals("HELLO WORLD", future.get());
    }
    
    @Test
    @DisplayName("CompletableFuture deve tratar excecao")
    @Timeout(3)
    void completableFutureException() throws Exception {
        CompletableFuture<String> future = CompletableFuture.<String>supplyAsync(() -> {
            throw new RuntimeException("Erro!");
        }).exceptionally(ex -> "Valor padrao");
        
        assertEquals("Valor padrao", future.get());
    }
    
    // ============================================
    // Testes de Volatile
    // ============================================
    
    @Test
    @DisplayName("Volatile deve garantir visibilidade")
    @Timeout(2)
    void volatileVisibilidade() throws InterruptedException {
        ConcorrenciaExample.FlagCompartilhada flag = new ConcorrenciaExample.FlagCompartilhada();
        
        Thread worker = new Thread(() -> {
            while (flag.isRunning()) {
                // Loop ate flag mudar
            }
        });
        
        worker.start();
        Thread.sleep(100); // Da tempo de iniciar
        
        flag.parar(); // Thread principal muda flag
        worker.join(1000); // Worker deve ver a mudanca
        
        assertFalse(worker.isAlive());
    }
}
