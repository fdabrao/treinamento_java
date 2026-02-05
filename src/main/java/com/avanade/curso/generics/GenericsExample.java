package com.avanade.curso.generics;

import java.util.*;
import java.util.function.Function;

/**
 * GENERICS EM JAVA
 * 
 * Generics permitem criar classes, interfaces e métodos que operam
 * em tipos específicos, fornecendo type safety em tempo de compilação
 * e eliminando a necessidade de casts.
 * 
 * Benefícios:
 * - Type safety (segurança de tipos)
 * - Eliminação de casts explícitos
 * - Reutilização de código
 * - Detecção de erros em tempo de compilação
 * 
 * Notação:
 * - <T> : Type (tipo genérico)
 * - <E> : Element (usado em coleções)
 * - <K> : Key (chave em maps)
 * - <V> : Value (valor em maps)
 * - <N> : Number (tipos numéricos)
 */

// ============================================
// CLASSE GENÉRICA SIMPLES
// ============================================

/**
 * Caixa que pode conter qualquer tipo
 */
class Caixa<T> {
    private T conteudo;
    
    public void guardar(T item) {
        this.conteudo = item;
    }
    
    public T retirar() {
        T item = conteudo;
        conteudo = null;
        return item;
    }
    
    public boolean estaVazia() {
        return conteudo == null;
    }
    
    public T getConteudo() {
        return conteudo;
    }
}

// ============================================
// CLASSE GENÉRICA COM RESTRIÇÃO
// ============================================

/**
 * NumberBox só aceita tipos numéricos
 * <T extends Number> = T deve ser Number ou subclasse
 */
class NumberBox<T extends Number> {
    private T numero;
    
    public void set(T numero) {
        this.numero = numero;
    }
    
    public T get() {
        return numero;
    }
    
    /**
     * Pode chamar métodos de Number porque T extends Number
     */
    public double doubleValue() {
        return numero.doubleValue();
    }
    
    public int intValue() {
        return numero.intValue();
    }
    
    /**
     * Verifica se é maior que outro número
     */
    public boolean isGreaterThan(T outro) {
        return this.numero.doubleValue() > outro.doubleValue();
    }
}

// ============================================
// INTERFACE GENÉRICA
// ============================================

interface Repositorio<T, ID> {
    T salvar(T entidade);
    T buscarPorId(ID id);
    List<T> buscarTodos();
    void deletar(ID id);
}

// Implementação da interface genérica
class Usuario {
    private Long id;
    private String nome;
    
    public Usuario(Long id, String nome) {
        this.id = id;
        this.nome = nome;
    }
    
    public Long getId() { return id; }
    public String getNome() { return nome; }
    
    @Override
    public String toString() {
        return "Usuario{id=" + id + ", nome='" + nome + "'}";
    }
}

class UsuarioRepository implements Repositorio<Usuario, Long> {
    private Map<Long, Usuario> usuarios = new HashMap<>();
    private long nextId = 1;
    
    @Override
    public Usuario salvar(Usuario entidade) {
        if (entidade.getId() == null) {
            // Simula auto-incremento
            usuarios.put(nextId, new Usuario(nextId, entidade.getNome()));
            nextId++;
        } else {
            usuarios.put(entidade.getId(), entidade);
        }
        return usuarios.get(nextId - 1);
    }
    
    @Override
    public Usuario buscarPorId(Long id) {
        return usuarios.get(id);
    }
    
    @Override
    public List<Usuario> buscarTodos() {
        return new ArrayList<>(usuarios.values());
    }
    
    @Override
    public void deletar(Long id) {
        usuarios.remove(id);
    }
}

// ============================================
// MÉTODOS GENÉRICOS
// ============================================

class UtilitariosGenericos {
    
    /**
     * Método genérico simples
     * Troca dois elementos em um array
     */
    public static <T> void swap(T[] array, int i, int j) {
        T temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
    
    /**
     * Método genérico com bounded type
     * Só aceita tipos que implementam Comparable
     */
    public static <T extends Comparable<T>> T max(T a, T b) {
        return a.compareTo(b) > 0 ? a : b;
    }
    
    /**
     * Método genérico com múltiplos tipos
     */
    public static <K, V> Map<K, V> criarMap(K chave, V valor) {
        Map<K, V> map = new HashMap<>();
        map.put(chave, valor);
        return map;
    }
    
    /**
     * Varargs genérico
     */
    @SafeVarargs
    public static <T> List<T> criarLista(T... elementos) {
        return Arrays.asList(elementos);
    }
    
    /**
     * Converter lista de um tipo para outro
     */
    public static <T, R> List<R> converter(List<T> lista, Function<T, R> converter) {
        List<R> resultado = new ArrayList<>();
        for (T item : lista) {
            resultado.add(converter.apply(item));
        }
        return resultado;
    }
    
    /**
     * Encontrar máximo em uma lista
     */
    public static <T extends Comparable<T>> Optional<T> maximo(List<T> lista) {
        if (lista.isEmpty()) {
            return Optional.empty();
        }
        T max = lista.get(0);
        for (T item : lista) {
            if (item.compareTo(max) > 0) {
                max = item;
            }
        }
        return Optional.of(max);
    }
    
    /**
     * Imprimir qualquer coleção
     */
    public static <T> void imprimirColecao(Collection<T> colecao) {
        for (T item : colecao) {
            System.out.println(item);
        }
    }
    
    /**
     * Copiar elementos de uma lista para outra
     * Usando wildcards
     */
    public static <T> void copiar(List<? extends T> origem, List<? super T> destino) {
        destino.addAll(origem);
    }
}

// ============================================
// WILDCARDS
// ============================================

class ExemplosWildcards {
    
    /**
     * Unbounded wildcard <?>
     * Lista de qualquer tipo
     * Só pode ler como Object
     */
    public static void imprimirQualquerLista(List<?> lista) {
        for (Object item : lista) {
            System.out.println(item);
        }
        // Não pode adicionar elementos (exceto null)
        // lista.add("teste"); // Erro de compilação
        lista.add(null); // Ok
    }
    
    /**
     * Upper bounded wildcard <? extends T>
     * Lista de T ou subclasses de T
     * Pode ler como T, mas não pode adicionar
     */
    public static double somarNumeros(List<? extends Number> numeros) {
        double soma = 0;
        for (Number n : numeros) {
            soma += n.doubleValue();
        }
        return soma;
    }
    
    /**
     * Lower bounded wildcard <? super T>
     * Lista de T ou superclasses de T
     * Pode adicionar T, mas lê como Object
     */
    public static void adicionarInteiros(List<? super Integer> lista) {
        lista.add(1);
        lista.add(2);
        lista.add(3);
        // Lê como Object
        for (Object item : lista) {
            System.out.println(item);
        }
    }
    
    /**
     * PECS Principle: Producer-Extends, Consumer-Super
     * 
     - Producer (produz dados): use <? extends T>
     * - Consumer (consome dados): use <? super T>
     */
    
    /**
     * Producer - lê da lista (extends)
     */
    public static void copiarPara(List<? extends Number> origem, List<Number> destino) {
        for (Number n : origem) {
            destino.add(n);
        }
    }
    
    /**
     * Consumer - escreve na lista (super)
     */
    public static void copiarDe(List<Integer> origem, List<? super Integer> destino) {
        destino.addAll(origem);
    }
}

// ============================================
// TYPE ERASURE
// ============================================

/**
 * Java usa type erasure para implementar generics
 * Em tempo de execução, <T> vira Object (ou o bound)
 */
class DemonstracaoErasure {
    
    /**
     * NÃO É POSSÍVEL:
     * - Criar array de tipo genérico: new T[10]
     * - Usar instanceof com tipo genérico: obj instanceof T
     * - Usar primitivos como tipo: List<int> (use Integer)
     * - Criar exceção genérica
     */
    
    /**
     * Workaround para criar array
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] criarArray(Class<T> clazz, int tamanho) {
        return (T[]) java.lang.reflect.Array.newInstance(clazz, tamanho);
    }
    
    /**
     * Verificar tipo em tempo de execução
     */
    public static <T> boolean isTipo(Object obj, Class<T> clazz) {
        return clazz.isInstance(obj);
    }
}

// ============================================
// GENERICS COM HERANÇA
// ============================================

class Animal {
    protected String nome;
    
    public Animal(String nome) {
        this.nome = nome;
    }
    
    public void fazerSom() {
        System.out.println(nome + " faz som");
    }
}

class Cachorro extends Animal {
    public Cachorro(String nome) {
        super(nome);
    }
    
    @Override
    public void fazerSom() {
        System.out.println(nome + " late");
    }
    
    public void abanarRabo() {
        System.out.println(nome + " abana o rabo");
    }
}

class Gato extends Animal {
    public Gato(String nome) {
        super(nome);
    }
    
    @Override
    public void fazerSom() {
        System.out.println(nome + " mia");
    }
}

/**
 * Demonstra covariância e contravariância
 */
class HerancaComGenerics {
    
    /**
     * Aceita List de Animal ou qualquer subclasse
     */
    public static void fazerTodosSom(List<? extends Animal> animais) {
        for (Animal a : animais) {
            a.fazerSom();
        }
        // Não pode adicionar: animais.add(new Cachorro("Novo"));
    }
    
    /**
     * Aceita List onde podemos adicionar Cachorro
     */
    public static void adicionarCachorro(List<? super Cachorro> lista) {
        lista.add(new Cachorro("Novo"));
        // Lê como Object: Animal a = lista.get(0); // Erro!
    }
}

// ============================================
// PADRÃO DE PROJETO: GENERIC BUILDER
// ============================================

class GenericBuilder<T> {
    private T objeto;
    private Class<T> clazz;
    
    public GenericBuilder(Class<T> clazz) throws Exception {
        this.clazz = clazz;
        this.objeto = clazz.getDeclaredConstructor().newInstance();
    }
    
    public GenericBuilder<T> com(String propriedade, Object valor) throws Exception {
        // Usa reflection para setar propriedade
        clazz.getMethod("set" + propriedade.substring(0, 1).toUpperCase() + propriedade.substring(1), 
            valor.getClass()).invoke(objeto, valor);
        return this;
    }
    
    public T build() {
        return objeto;
    }
}

// ============================================
// CLASSE DE DEMONSTRAÇÃO
// ============================================

public class GenericsExample {
    
    public static void demonstrar() {
        System.out.println("=== Caixa Genérica ===");
        
        // Caixa de String
        Caixa<String> caixaString = new Caixa<>();
        caixaString.guardar("Olá, Generics!");
        String valor = caixaString.retirar(); // Sem cast necessário!
        System.out.println(valor);
        
        // Caixa de Integer
        Caixa<Integer> caixaInt = new Caixa<>();
        caixaInt.guardar(42);
        int numero = caixaInt.retirar(); // Unboxing automático
        System.out.println(numero);
        
        System.out.println("\n=== NumberBox (Bounded Type) ===");
        
        NumberBox<Integer> intBox = new NumberBox<>();
        intBox.set(100);
        System.out.println("Valor: " + intBox.get());
        System.out.println("Como double: " + intBox.doubleValue());
        
        NumberBox<Double> doubleBox = new NumberBox<>();
        doubleBox.set(3.14);
        System.out.println("Maior: " + doubleBox.isGreaterThan(2.0));
        
        System.out.println("\n=== Métodos Genéricos ===");
        
        String[] nomes = {"Ana", "Bruno", "Carlos"};
        UtilitariosGenericos.swap(nomes, 0, 2);
        System.out.println("Após swap: " + Arrays.toString(nomes));
        
        String maior = UtilitariosGenericos.max("Java", "Python");
        System.out.println("String maior: " + maior);
        
        Integer maxInt = UtilitariosGenericos.max(10, 20);
        System.out.println("Inteiro maior: " + maxInt);
        
        List<String> lista = UtilitariosGenericos.criarLista("A", "B", "C");
        System.out.println("Lista: " + lista);
        
        System.out.println("\n=== Wildcards ===");
        
        List<Integer> inteiros = Arrays.asList(1, 2, 3, 4, 5);
        double soma = ExemplosWildcards.somarNumeros(inteiros);
        System.out.println("Soma: " + soma);
        
        List<Double> doubles = Arrays.asList(1.5, 2.5, 3.5);
        System.out.println("Soma doubles: " + ExemplosWildcards.somarNumeros(doubles));
        
        System.out.println("\n=== Herança com Generics ===");
        
        List<Cachorro> cachorros = Arrays.asList(new Cachorro("Rex"), new Cachorro("Buddy"));
        HerancaComGenerics.fazerTodosSom(cachorros);
        
        List<Animal> animais = new ArrayList<>();
        HerancaComGenerics.adicionarCachorro(animais);
        System.out.println("Animais: " + animais.size());
        
        System.out.println("\n=== Type Erasure Demo ===");
        
        String[] array = DemonstracaoErasure.criarArray(String.class, 5);
        array[0] = "Teste";
        System.out.println("Array criado: " + array[0]);
        
        boolean isString = DemonstracaoErasure.isTipo("texto", String.class);
        System.out.println("É String? " + isString);
        
        System.out.println("\n=== Repositório Genérico ===");
        
        UsuarioRepository repo = new UsuarioRepository();
        Usuario novo = repo.salvar(new Usuario(null, "João"));
        System.out.println("Salvo: " + novo);
        
        Usuario encontrado = repo.buscarPorId(1L);
        System.out.println("Encontrado: " + encontrado);
    }
    
    public static void main(String[] args) throws Exception {
        demonstrar();
    }
}
