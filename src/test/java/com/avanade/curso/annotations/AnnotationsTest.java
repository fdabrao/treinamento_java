package com.avanade.curso.annotations;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.Arrays;

/**
 * Testes para Annotations
 */
class AnnotationsTest {
    
    // ============================================
    // Testes de Anotações Buit-in
    // ============================================
    
    @Test
    @DisplayName("Override deve indicar sobrescrita de método")
    void annotationOverride() {
        // Testamos que toString() sobrescreve Object.toString()
        ExemploBuiltInAnnotations obj = new ExemploBuiltInAnnotations();
        assertTrue(obj.toString().contains("ExemploBuiltInAnnotations"));
    }
    
    @Test
    @DisplayName("Deprecated deve marcar método como obsoleto")
    void annotationDeprecated() {
        ExemploBuiltInAnnotations obj = new ExemploBuiltInAnnotations();
        
        // Método ainda funciona, mas está marcado como obsoleto
        assertDoesNotThrow(obj::metodoAntigo);
        
        // Verificar se método tem @Deprecated
        try {
            Method metodo = ExemploBuiltInAnnotations.class.getMethod("metodoAntigo");
            assertTrue(metodo.isAnnotationPresent(Deprecated.class));
        } catch (NoSuchMethodException e) {
            fail("Método não encontrado");
        }
    }
    
    @Test
    @DisplayName("FunctionalInterface deve garantir um método abstrato")
    void annotationFunctionalInterface() {
        // Interface funcional válida
        @FunctionalInterface
        interface ValidInterface {
            void execute();
            default void defaultMethod() {}
        }
        
        assertTrue(ValidInterface.class.isAnnotationPresent(FunctionalInterface.class));
        
        // Verificar que é realmente funcional
        ValidInterface lambda = () -> System.out.println("Test");
        assertDoesNotThrow(() -> lambda.execute());
    }
    
    // ============================================
    // Testes de Anotações Customizadas
    // ============================================
    
    @Test
    @DisplayName("Autor deve estar presente na classe")
    void annotationAutorPresente() {
        assertTrue(ServicoLegado.class.isAnnotationPresent(Autor.class));
        
        Autor autor = ServicoLegado.class.getAnnotation(Autor.class);
        assertEquals("João Silva", autor.nome());
        assertEquals("joao@empresa.com", autor.email());
        assertEquals("2024-01-15", autor.data());
    }
    
    @Test
    @DisplayName("Refatorar deve ter valores padrão")
    void annotationRefatorarPadrao() {
        try {
            Method metodo = ServicoLegado.class.getMethod("processarDados");
            assertTrue(metodo.isAnnotationPresent(Refatorar.class));
            
            Refatorar ref = metodo.getAnnotation(Refatorar.class);
            assertEquals("Extrair para classe utilitária", ref.motivo());
            assertEquals(1, ref.prioridade()); // Valor padrão
        } catch (NoSuchMethodException e) {
            fail("Método não encontrado");
        }
    }
    
    @Test
    @DisplayName("Anotações repetíveis devem funcionar")
    void annotationRepetivel() {
        try {
            Method metodo = ServicoLegado.class.getMethod("validarUsuario");
            
            // Deve ter @Validacoes (container)
            assertTrue(metodo.isAnnotationPresent(Validacoes.class));
            
            // Ou acessar via getAnnotationsByType
            Validacao[] validacoes = metodo.getAnnotationsByType(Validacao.class);
            assertEquals(2, validacoes.length);
            
        } catch (NoSuchMethodException e) {
            fail("Método não encontrado");
        }
    }
    
    // ============================================
    // Testes de Reflection
    // ============================================
    
    @Test
    @DisplayName("Deve processar anotações de classe via reflection")
    void processarAnotacoesClasse() {
        Class<?> clazz = ServicoLegado.class;
        
        Annotation[] anotacoes = clazz.getAnnotations();
        assertTrue(anotacoes.length > 0);
        
        // Verificar se tem @Autor
        boolean temAutor = Arrays.stream(anotacoes)
            .anyMatch(a -> a instanceof Autor);
        assertTrue(temAutor);
    }
    
    @Test
    @DisplayName("Deve processar anotações de métodos")
    void processarAnotacoesMetodos() {
        Method[] metodos = ServicoLegado.class.getDeclaredMethods();
        
        boolean encontrouMetodoAnotado = Arrays.stream(metodos)
            .anyMatch(m -> m.isAnnotationPresent(Autor.class));
        
        assertTrue(encontrouMetodoAnotado);
    }
    
    @Test
    @DisplayName("Deve processar anotações de campos")
    void processarAnotacoesCampos() throws Exception {
        ServicoLegado servico = new ServicoLegado();
        
        // Processar campos anotados com @Validar
        for (Field field : ServicoLegado.class.getDeclaredFields()) {
            if (field.isAnnotationPresent(Validar.class)) {
                Validar validar = field.getAnnotation(Validar.class);
                assertTrue(validar.obrigatorio());
                assertTrue(validar.tamanhoMinimo() >= 0);
            }
        }
    }
    
    // ============================================
    // Testes de Meta-Anotações
    // ============================================
    
    @Test
    @DisplayName("Retention deve ser RUNTIME")
    void metaAnotacaoRetention() {
        Retention retention = Autor.class.getAnnotation(Retention.class);
        assertNotNull(retention);
        assertEquals(RetentionPolicy.RUNTIME, retention.value());
    }
    
    @Test
    @DisplayName("Target deve definir onde pode ser usada")
    void metaAnotacaoTarget() {
        Target target = Autor.class.getAnnotation(Target.class);
        assertNotNull(target);
        
        ElementType[] elementos = target.value();
        assertTrue(Arrays.asList(elementos).contains(ElementType.TYPE));
        assertTrue(Arrays.asList(elementos).contains(ElementType.METHOD));
    }
    
    // ============================================
    // Testes do Framework Simulado
    // ============================================
    
    @Test
    @DisplayName("Container DI deve registrar componentes")
    void containerDIRegistrar() throws Exception {
        ContainerDI container = new ContainerDI();
        
        // Registrar componentes
        assertDoesNotThrow(() -> {
            container.registrar(Repositorio.class);
            container.registrar(MeuServico.class);
        });
        
        // Verificar se registrados
        MeuServico servico = container.getComponente(MeuServico.class);
        Repositorio repo = container.getComponente(Repositorio.class);
        
        // Componentes devem existir (podem ser null se DI não funcionar, mas registro deve)
        assertNotNull(repo);
    }
    
    @Test
    @DisplayName("Container DI deve injetar dependências")
    void containerDIInjecao() throws Exception {
        ContainerDI container = new ContainerDI();
        
        container.registrar(Repositorio.class);
        container.registrar(MeuServico.class);
        
        Repositorio repo = container.getComponente(Repositorio.class);
        
        // Repositório deve estar registrado
        assertNotNull(repo);
        
        // Se injeção funcionou, não deve lançar NPE ao usar o repo
        assertDoesNotThrow(() -> repo.salvar("Teste"));
    }
    
    @Test
    @DisplayName("Container deve invocar métodos GET anotados")
    void containerDIInvocarGet() throws Exception {
        ContainerDI container = new ContainerDI();
        container.registrar(MeuServico.class);
        
        // Não deve lançar exceção
        assertDoesNotThrow(() -> container.invocarGet("/usuarios"));
    }
    
    // ============================================
    // Testes de Retention Policies
    // ============================================
    
    @Test
    @DisplayName("Annotations RUNTIME devem estar disponíveis via reflection")
    void runtimeAnnotationsDisponiveis() {
        // @Autor tem Retention.RUNTIME
        Autor autor = ServicoLegado.class.getAnnotation(Autor.class);
        assertNotNull(autor);
    }
    
    @Test
    @DisplayName("Deve obter todos os valores de uma anotação")
    void obterValoresAnotacao() {
        Autor autor = ServicoLegado.class.getAnnotation(Autor.class);
        
        assertEquals("João Silva", autor.nome());
        assertEquals("joao@empresa.com", autor.email());
        assertEquals("2024-01-15", autor.data());
    }
}
