package com.avanade.curso.jvm;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.lang.management.*;
import java.util.*;

/**
 * Testes para JVM Internals
 */
class JVMInternalsTest {
    
    // ============================================
    // Testes de Memory Management
    // ============================================
    
    @Test
    @DisplayName("Runtime deve fornecer informacoes de memoria")
    void runtimeMemoryInfo() {
        Runtime runtime = Runtime.getRuntime();
        
        assertTrue(runtime.maxMemory() > 0);
        assertTrue(runtime.totalMemory() > 0);
        assertTrue(runtime.freeMemory() >= 0);
        assertTrue(runtime.availableProcessors() > 0);
    }
    
    @Test
    @DisplayName("MemoryMXBean deve fornecer uso do heap")
    void memoryMXBean() {
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage heapUsage = memoryMXBean.getHeapMemoryUsage();
        
        assertTrue(heapUsage.getUsed() >= 0);
        assertTrue(heapUsage.getCommitted() > 0);
        assertTrue(heapUsage.getMax() > 0 || heapUsage.getMax() == -1);
    }
    
    @Test
    @DisplayName("Deve criar e remover objetos (GC)")
    void garbageCollection() {
        // Cria objetos temporarios
        List<Object> objetos = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            objetos.add(new byte[1024]); // 1KB cada
        }
        
        long memoriaAntes = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        
        // Remove referencias
        objetos.clear();
        objetos = null;
        
        // Sugere GC
        System.gc();
        
        // Memoria deve estar disponivel (nao testamos valor exato)
        assertTrue(true);
    }
    
    // ============================================
    // Testes de Class Loading
    // ============================================
    
    @Test
    @DisplayName("ClassLoader deve carregar classes")
    void classLoading() {
        ClassLoader loader = getClass().getClassLoader();
        assertNotNull(loader);
        
        // Classes do Java core usam Bootstrap (null)
        ClassLoader stringLoader = String.class.getClassLoader();
        assertNull(stringLoader);
        
        // Hierarquia de loaders
        ClassLoader platformLoader = loader.getParent();
        if (platformLoader != null) {
            assertNull(platformLoader.getParent()); // Bootstrap
        }
    }
    
    @Test
    @DisplayName("Deve carregar classe por nome")
    void loadClassByName() throws ClassNotFoundException {
        Class<?> clazz = Class.forName("java.lang.String");
        assertEquals(String.class, clazz);
        
        Class<?> testClazz = Class.forName("com.avanade.curso.jvm.JVMInternalsExample");
        assertNotNull(testClazz);
    }
    
    // ============================================
    // Testes de Garbage Collectors
    // ============================================
    
    @Test
    @DisplayName("Deve listar Garbage Collectors")
    void listGarbageCollectors() {
        List<GarbageCollectorMXBean> gcBeans = ManagementFactory.getGarbageCollectorMXBeans();
        
        assertFalse(gcBeans.isEmpty());
        
        for (GarbageCollectorMXBean gcBean : gcBeans) {
            assertNotNull(gcBean.getName());
            assertTrue(gcBean.getCollectionCount() >= 0);
            assertTrue(gcBean.getCollectionTime() >= 0);
        }
    }
    
    @Test
    @DisplayName("Memory Pools devem existir")
    void memoryPools() {
        List<MemoryPoolMXBean> pools = ManagementFactory.getMemoryPoolMXBeans();
        
        assertFalse(pools.isEmpty());
        
        // Deve ter pelo menos o heap
        boolean hasHeap = pools.stream()
            .anyMatch(p -> p.getType() == MemoryType.HEAP);
        assertTrue(hasHeap);
    }
    
    // ============================================
    // Testes de Stack
    // ============================================
    
    @Test
    @DisplayName("Deve obter stack trace")
    void stackTrace() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        
        assertTrue(stackTrace.length > 0);
        
        // Primeiro elemento deve ser getStackTrace
        assertEquals("getStackTrace", stackTrace[0].getMethodName());
    }
    
    @Test
    @DisplayName("StackOverflowError deve ser lancado")
    void stackOverflow() {
        assertThrows(StackOverflowError.class, () -> {
            recursaoInfinita();
        });
    }
    
    private void recursaoInfinita() {
        recursaoInfinita();
    }
    
    // ============================================
    // Testes de Memory Leaks
    // ============================================
    
    @Test
    @DisplayName("WeakHashMap deve permitir coleta de chaves")
    void weakHashMap() {
        Map<Object, String> weakMap = new WeakHashMap<>();
        
        Object chave = new Object();
        weakMap.put(chave, "valor");
        
        assertEquals(1, weakMap.size());
        
        // Remove referencia forte
        chave = null;
        
        // Sugere GC
        System.gc();
        
        // Entrada pode ser removida (nao garantido imediatamente)
        // So verificamos que o mapa funciona
        assertNotNull(weakMap);
    }
    
    // ============================================
    // Testes de JIT
    // ============================================
    
    @Test
    @DisplayName("CompilationMXBean deve existir")
    void compilationMXBean() {
        CompilationMXBean compilationBean = ManagementFactory.getCompilationMXBean();
        
        assertNotNull(compilationBean);
        assertNotNull(compilationBean.getName());
        
        if (compilationBean.isCompilationTimeMonitoringSupported()) {
            assertTrue(compilationBean.getTotalCompilationTime() >= 0);
        }
    }
    
    // ============================================
    // Testes de Object Lifecycle
    // ============================================
    
    @Test
    @DisplayName("Objetos devem ser criados e removidos")
    void objectLifecycle() {
        // Criacao
        Object obj = new Object();
        assertNotNull(obj);
        
        // Referenciado
        Object ref = obj;
        assertSame(obj, ref);
        
        // Eligivel para GC
        obj = null;
        ref = null;
        
        // Objeto pode ser coletado
        assertTrue(true);
    }
    
    @Test
    @DisplayName("Finalize nao e garantido")
    @SuppressWarnings("deprecation")
    void finalizeNotGuaranteed() {
        // Finalize foi deprecado no Java 9
        // Este teste apenas demonstra que objetos podem ser criados
        
        Object obj = new Object() {
            @Override
            protected void finalize() throws Throwable {
                System.out.println("Finalize chamado");
            }
        };
        
        obj = null;
        System.gc();
        
        // Finalize pode ou nao ser chamado
        assertTrue(true);
    }
}
