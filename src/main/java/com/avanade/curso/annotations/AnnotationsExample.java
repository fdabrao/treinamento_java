package com.avanade.curso.annotations;

import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;

/**
 * ANNOTATIONS EM JAVA
 * 
 * Annotations (anotações) são metadados que fornecem informações sobre o código
 * sem afetar diretamente sua execução. São amplamente usadas por frameworks.
 * 
 * Tipos de Retention:
 * - SOURCE: Descartada pelo compilador (ex: @Override)
 * - CLASS: Armazenada no .class mas não disponível em runtime (padrão)
 * - RUNTIME: Disponível em runtime via reflection
 * 
 * Tipos de Target:
 * - TYPE: Classes, interfaces, enums
 * - FIELD: Campos (atributos)
 * - METHOD: Métodos
 * - PARAMETER: Parâmetros
 * - CONSTRUCTOR: Construtores
 * - LOCAL_VARIABLE: Variáveis locais
 * - ANNOTATION_TYPE: Outras anotações
 * - PACKAGE: Pacotes
 */

// ============================================
// ANOTAÇÕES CUSTOMIZADAS
// ============================================

/**
 * Anotação de marcação (sem elementos)
 * Indica que uma classe precisa de refatoração
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@interface Refatorar {
    String motivo() default "";
    int prioridade() default 1; // 1=Baixa, 2=Média, 3=Alta
}

/**
 * Anotação com elementos obrigatórios
 * Documenta informações do autor
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@interface Autor {
    String nome();
    String email() default "";
    String data();
}

/**
 * Anotação para validação de campos
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@interface Validar {
    boolean obrigatorio() default true;
    int tamanhoMinimo() default 0;
    int tamanhoMaximo() default Integer.MAX_VALUE;
    String regex() default "";
}

/**
 * Anotação repetível (Java 8+)
 * Permite usar múltiplas vezes no mesmo elemento
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Repeatable(Validacoes.class)
@interface Validacao {
    String campo();
    String regra();
}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@interface Validacoes {
    Validacao[] value();
}

// ============================================
// CLASSES COM ANOTAÇÕES
// ============================================

@Autor(nome = "João Silva", email = "joao@empresa.com", data = "2024-01-15")
@Refatorar(motivo = "Melhorar performance", prioridade = 2)
class ServicoLegado {
    
    @Validar(obrigatorio = true, tamanhoMinimo = 5, tamanhoMaximo = 100)
    private String nome;
    
    @Validar(obrigatorio = true, regex = "^[A-Za-z0-9+_.-]+@(.+)$")
    private String email;
    
    @Autor(nome = "Maria Souza", data = "2024-02-20")
    @Refatorar(motivo = "Extrair para classe utilitária")
    public void processarDados() {
        System.out.println("Processando...");
    }
    
    @Validacao(campo = "idade", regra = "maiorQueZero")
    @Validacao(campo = "salario", regra = "positivo")
    public void validarUsuario() {
        System.out.println("Validando...");
    }
}

// ============================================
// PROCESSADOR DE ANOTAÇÕES (REFLECTION)
// ============================================

class AnnotationProcessor {
    
    /**
     * Processa anotações @Autor em uma classe
     */
    public static void processarAutor(Class<?> clazz) {
        System.out.println("Processando anotações de: " + clazz.getName());
        
        // Verifica se tem anotação @Autor
        if (clazz.isAnnotationPresent(Autor.class)) {
            Autor autor = clazz.getAnnotation(Autor.class);
            System.out.println("  Autor: " + autor.nome());
            System.out.println("  Email: " + autor.email());
            System.out.println("  Data: " + autor.data());
        }
        
        // Verifica @Refatorar
        if (clazz.isAnnotationPresent(Refatorar.class)) {
            Refatorar ref = clazz.getAnnotation(Refatorar.class);
            System.out.println("    Precisa refatorar: " + ref.motivo());
            System.out.println("     Prioridade: " + ref.prioridade());
        }
    }
    
    /**
     * Processa anotações em métodos
     */
    public static void processarMetodos(Class<?> clazz) {
        System.out.println("\nMétodos anotados:");
        
        for (Method method : clazz.getDeclaredMethods()) {
            System.out.println("  " + method.getName() + ":");
            
            // Processa @Autor em métodos
            if (method.isAnnotationPresent(Autor.class)) {
                Autor autor = method.getAnnotation(Autor.class);
                System.out.println("    Autor: " + autor.nome());
            }
            
            // Processa @Refatorar em métodos
            if (method.isAnnotationPresent(Refatorar.class)) {
                Refatorar ref = method.getAnnotation(Refatorar.class);
                System.out.println("      Refatorar: " + ref.motivo());
            }
            
            // Processa anotações repetíveis
            if (method.isAnnotationPresent(Validacoes.class)) {
                Validacoes validacoes = method.getAnnotation(Validacoes.class);
                System.out.println("    Validações:");
                for (Validacao v : validacoes.value()) {
                    System.out.println("      - " + v.campo() + ": " + v.regra());
                }
            }
            
            // Também funciona com getAnnotationsByType para repetíveis
            Validacao[] vals = method.getAnnotationsByType(Validacao.class);
            if (vals.length > 0) {
                System.out.println("    (via getAnnotationsByType):");
                for (Validacao v : vals) {
                    System.out.println("      - " + v.campo());
                }
            }
        }
    }
    
    /**
     * Processa anotações em campos (validação)
     */
    public static void validarObjeto(Object obj) throws IllegalAccessException {
        Class<?> clazz = obj.getClass();
        System.out.println("\nValidando campos de: " + clazz.getSimpleName());
        
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            
            if (field.isAnnotationPresent(Validar.class)) {
                Validar validar = field.getAnnotation(Validar.class);
                Object valor = field.get(obj);
                
                System.out.println("  Campo: " + field.getName());
                
                // Validação obrigatória
                if (validar.obrigatorio() && (valor == null || valor.toString().isEmpty())) {
                    System.out.println("     Campo obrigatório está vazio");
                    continue;
                }
                
                if (valor != null) {
                    String strValor = valor.toString();
                    
                    // Validação de tamanho
                    if (strValor.length() < validar.tamanhoMinimo()) {
                        System.out.println("     Tamanho mínimo: " + validar.tamanhoMinimo());
                    }
                    if (strValor.length() > validar.tamanhoMaximo()) {
                        System.out.println("     Tamanho máximo: " + validar.tamanhoMaximo());
                    }
                    
                    // Validação de regex
                    if (!validar.regex().isEmpty() && !strValor.matches(validar.regex())) {
                        System.out.println("     Não corresponde ao padrão");
                    } else {
                        System.out.println("     Válido");
                    }
                }
            }
        }
    }
    
    /**
     * Lista todas as anotações de uma classe
     */
    public static void listarAnotacoes(Class<?> clazz) {
        System.out.println("\nTodas as anotações de " + clazz.getSimpleName() + ":");
        
        Annotation[] anotacoes = clazz.getAnnotations();
        for (Annotation a : anotacoes) {
            System.out.println("  - " + a.annotationType().getSimpleName());
        }
        
        // Também pode verificar meta-anotações
        System.out.println("\nMeta-anotações de @Autor:");
        Retention retention = Autor.class.getAnnotation(Retention.class);
        if (retention != null) {
            System.out.println("  Retention: " + retention.value());
        }
        
        Target target = Autor.class.getAnnotation(Target.class);
        if (target != null) {
            System.out.println("  Target: " + Arrays.toString(target.value()));
        }
    }
}

// ============================================
// USO DE ANOTAÇÕES BUILT-IN
// ============================================

class ExemploBuiltInAnnotations {
    private String nome;
    
    // @Override: Indica que método sobrescreve da superclasse
    @Override
    public String toString() {
        return "ExemploBuiltInAnnotations{nome='" + nome + "'}";
    }
    
    // @Deprecated: Marca elemento como obsoleto
    @Deprecated
    public void metodoAntigo() {
        System.out.println("Este método está obsoleto");
    }
    
    // @Deprecated com since e forRemoval (Java 9+)
    @Deprecated(since = "2.0", forRemoval = true)
    public void metodoParaRemover() {
        System.out.println("Será removido em versão futura");
    }
    
    // @SuppressWarnings: Suprime warnings do compilador
    @SuppressWarnings("unchecked")
    public void metodoComWarning() {
        // Código que geraria warning de unchecked
        java.util.List lista = new java.util.ArrayList();
        lista.add("teste");
    }
    
    // @SafeVarargs: Indica que varargs genérico é seguro
    @SafeVarargs
    public final <T> void processarVarargs(T... elementos) {
        for (T elemento : elementos) {
            System.out.println(elemento);
        }
    }
    
    // @FunctionalInterface: Garante que interface tem apenas um método abstrato
    @FunctionalInterface
    interface MinhaInterfaceFuncional {
        void executar();
        // Não pode ter mais métodos abstratos!
        
        default void metodoDefault() {
            System.out.println("Default");
        }
    }
}

// ============================================
// FRAMEWORK SIMULADO COM ANOTAÇÕES
// ============================================

/**
 * Simula um framework de injeção de dependência
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@interface Componente {}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@interface Injecao {
    String valor() default "";
}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@interface Transacional {}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@interface Get {
    String path();
}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@interface Post {
    String path();
}

@Componente
class MeuServico {
    @Injecao
    private Repositorio repositorio;
    
    @Transacional
    public void salvarDados(String dados) {
        System.out.println("Salvando: " + dados);
    }
    
    @Get(path = "/usuarios")
    public String listarUsuarios() {
        return "Lista de usuários";
    }
    
    @Post(path = "/usuarios")
    public String criarUsuario(String dados) {
        return "Usuário criado";
    }
}

@Componente
class Repositorio {
    public void salvar(Object obj) {
        System.out.println("Salvo no banco");
    }
}

/**
 * Container de Injeção de Dependência simplificado
 */
class ContainerDI {
    private Map<Class<?>, Object> componentes = new HashMap<>();
    
    public void registrar(Class<?> clazz) throws Exception {
        if (clazz.isAnnotationPresent(Componente.class)) {
            Object instancia = clazz.getDeclaredConstructor().newInstance();
            componentes.put(clazz, instancia);
            
            // Injeta dependências
            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(Injecao.class)) {
                    Class<?> tipo = field.getType();
                    Object dependencia = componentes.get(tipo);
                    if (dependencia != null) {
                        field.setAccessible(true);
                        field.set(instancia, dependencia);
                    }
                }
            }
        }
    }
    
    public <T> T getComponente(Class<T> clazz) {
        return clazz.cast(componentes.get(clazz));
    }
    
    public void invocarGet(String path) throws Exception {
        for (Object componente : componentes.values()) {
            for (Method method : componente.getClass().getMethods()) {
                if (method.isAnnotationPresent(Get.class)) {
                    Get anotacao = method.getAnnotation(Get.class);
                    if (anotacao.path().equals(path)) {
                        Object resultado = method.invoke(componente);
                        System.out.println("GET " + path + " -> " + resultado);
                        return;
                    }
                }
            }
        }
        System.out.println("Rota não encontrada: " + path);
    }
}

// ============================================
// CLASSE DE DEMONSTRAÇÃO
// ============================================

public class AnnotationsExample {
    
    public static void demonstrar() throws Exception {
        System.out.println("=== ANOTAÇÕES CUSTOMIZADAS ===\n");
        
        // Processa anotações da classe
        AnnotationProcessor.processarAutor(ServicoLegado.class);
        AnnotationProcessor.processarMetodos(ServicoLegado.class);
        
        System.out.println("\n=== VALIDAÇÃO POR ANOTAÇÃO ===");
        ServicoLegado servico = new ServicoLegado();
        AnnotationProcessor.validarObjeto(servico);
        
        // Simula dados válidos
        System.out.println("\n--- Com dados válidos ---");
        // Usando reflection para setar valores (apenas demonstração)
        
        System.out.println("\n=== METADADOS DAS ANOTAÇÕES ===");
        AnnotationProcessor.listarAnotacoes(ServicoLegado.class);
        
        System.out.println("\n=== FRAMEWORK SIMULADO ===");
        ContainerDI container = new ContainerDI();
        container.registrar(Repositorio.class);
        container.registrar(MeuServico.class);
        
        MeuServico meuServico = container.getComponente(MeuServico.class);
        if (meuServico != null) {
            System.out.println("Serviço injetado com sucesso!");
            meuServico.salvarDados("Teste");
        }
        
        container.invocarGet("/usuarios");
        container.invocarGet("/produtos");
    }
    
    public static void main(String[] args) throws Exception {
        demonstrar();
    }
}
