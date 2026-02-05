# OOP - 4 Pilares da Orientacao a Objetos & SOLID

Projeto Java demonstrando os 4 pilares fundamentais da Orientacao a Objetos e os 5 principios SOLID, com exemplos praticos e testes unitarios.

## Estrutura do Projeto

```
oop-pilares/
├── pom.xml                          # Configuracao Maven
└── src/
    ├── main/java/com/avanade/curso/
    │   ├── oop/
    │   │   ├── encapsulamento/      # Pilar 1: Encapsulamento
    │   │   ├── heranca/             # Pilar 2: Heranca
    │   │   ├── polimorfismo/        # Pilar 3: Polimorfismo
    │   │   ├── abstracao/           # Pilar 4: Abstracao
    │   │   └── composicao/          # Composicao vs Heranca
    │   ├── solid/
    │   │   ├── srp/                 # Principio 1: Responsabilidade Unica
    │   │   ├── ocp/                 # Principio 2: Aberto/Fechado
    │   │   ├── lsp/                 # Principio 3: Substituicao de Liskov
    │   │   ├── isp/                 # Principio 4: Segregacao de Interfaces
    │   │   └── dip/                 # Principio 5: Inversao de Dependencia
    │   ├── funcional/               # Programacao Funcional
    │   ├── excecoes/                # Tratamento de Excecoes
    │   ├── generics/                # Generics
    │   ├── collections/             # Collections Framework
    │   ├── time/                    # Java Time API
    │   ├── annotations/             # Annotations
    │   ├── enums/                   # Enums
    │   ├── nio/                     # NIO.2 (Arquivos)
    │   ├── concorrencia/            # Multithreading e Concorrencia
    │   └── jvm/                     # JVM Internals
    └── test/java/com/avanade/curso/
        └── [pacotes de teste correspondentes]
```

## Os 4 Pilares da OOP

### 1. Encapsulamento

**Conceito:** Ocultar detalhes internos e expor apenas o necessario.

**Exemplo:** `ContaBancaria`
- Atributos sao `private` (nao acessiveis diretamente)
- Acesso controlado via getters/setters com validacao
- Saldo so pode ser modificado atraves de metodos de negocio (`depositar()`, `sacar()`)

**Beneficios:**
- Protege dados de acesso indevido
- Permite validacoes antes de modificar valores
- Facilita manutencao (mudancas internas nao afetam codigo externo)

### 2. Heranca

**Conceito:** Permitir que uma classe herde caracteristicas de outra (relacionamento "e um").

**Exemplo:** `Funcionario` -> `Gerente` / `Desenvolvedor`
- `Gerente` **e um** `Funcionario` com caracteristicas adicionais
- `Desenvolvedor` **e um** `Funcionario` com niveis de senioridade

**Beneficios:**
- Reuso de codigo comum
- Especializacao de comportamentos
- Hierarquia logica de classes

### 3. Polimorfismo

**Conceito:** "Muitas formas" - objetos diferentes responderem ao mesmo metodo de formas distintas.

**Exemplo:** Interface `Pagamento`
- `PagamentoCartaoCredito`, `PagamentoBoleto`, `PagamentoPix`
- Todos implementam `processar()` e `calcularTaxa()`, mas cada um a sua maneira

**Tipos demonstrados:**
- **Sobrescrita (Override):** Mesmo metodo, comportamento diferente
- **Sobrecarga (Overload):** Mesmo nome, parametros diferentes
- **Polimorfismo de inclusao:** Tratar objetos filhos como o pai

**Beneficios:**
- Codigo flexivel e extensivel
- Tratamento uniforme de objetos diferentes
- Facilidade para adicionar novos tipos

### 4. Abstracao

**Conceito:** Modelar apenas o essencial, escondendo complexidades.

**Exemplo:** `ServicoNotificacao` (classe abstrata)
- Define O QUE o servico faz (`enviar()`, `configurar()`)
- Cada implementacao define COMO fazer (`NotificacaoEmail`, `NotificacaoSms`, `NotificacaoPush`)
- Usa Template Method para definir fluxo padrao

**Beneficios:**
- Simplifica modelagem de sistemas complexos
- Separa o "o que" do "como"
- Reduz acoplamento entre componentes

### 5. Composicao (Bonus)

**Conceito:** "Favor composition over inheritance" - preferir composicao a heranca quando possivel.

**Problemas da Heranca:**
- Heranca e estatica - nao pode mudar em tempo de execucao
- Acoplamento forte - mudancas na classe pai afetam todas as filhas
- Hierarquias profundas ficam complexas
- Java nao permite heranca multipla
- Viola o encapsulamento - filhas dependem de detalhes do pai

**Solucao com Composicao:**
- Compor comportamentos atraves de interfaces
- Cada comportamento e uma estrategia que pode ser injetada
- Comportamentos podem ser trocados dinamicamente

**Exemplo: Veiculos**

*Heranca (problema):*
```java
class VeiculoRuim { }
class CarroRuim extends VeiculoRuim { }
class BarcoRuim extends VeiculoRuim { }
// PROBLEMA: Como criar um veiculo anfibio? Nao herda de ambos!
```

*Composicao (solucao):*
```java
// Estrategias de movimento
interface EstrategiaMovimento {
    void mover(String modelo);
}

class MovimentoTerrestre implements EstrategiaMovimento { }
class MovimentoAquatico implements EstrategiaMovimento { }
class MovimentoAereo implements EstrategiaMovimento { }

// Veiculo flexivel
class Veiculo {
    private List<EstrategiaMovimento> estrategias = new ArrayList<>();
    
    public void adicionarEstrategia(EstrategiaMovimento e) {
        estrategias.add(e);
    }
    
    public void mover() {
        for (EstrategiaMovimento e : estrategias) {
            e.mover(modelo);
        }
    }
}

// USO:
Veiculo carro = new Veiculo("Carro");
carro.adicionarEstrategia(new MovimentoTerrestre());

Veiculo anfibio = new Veiculo("Anfibio");
anfibio.adicionarEstrategia(new MovimentoTerrestre());
anfibio.adicionarEstrategia(new MovimentoAquatico());

// Troca dinamica!
anfibio.removerEstrategia(terrestre);
anfibio.adicionarEstrategia(new MovimentoAereo());
```

**Exemplo Pratico: Sistema de Notificacoes**

```java
// Comportamentos
interface Sender {
    void enviar(String mensagem, String destinatario);
}

class EmailSender implements Sender { }
class SmsSender implements Sender { }
class PushSender implements Sender { }

// Servico composto
class NotificationService {
    private List<Sender> senders = new ArrayList<>();
    
    public void addSender(Sender sender) {
        senders.add(sender);
    }
    
    public void notificar(String mensagem, String destinatario) {
        for (Sender sender : senders) {
            sender.enviar(mensagem, destinatario);
        }
    }
}

// USO:
NotificationService service = new NotificationService();
service.addSender(new EmailSender());
service.addSender(new SmsSender());
service.addSender(new PushSender());
// Pode adicionar/remover canais em tempo de execucao!
```

**Quando Usar:**

| Heranca | Composicao |
|---------|-----------|
| Relacao "e um" forte e permanente | Relacao "tem um" ou comportamentos variaveis |
| Especializacao de tipo | Combinacao de comportamentos |
| Pouca variacao esperada | Necessidade de mudar em runtime |
| Hierarquia simples | Multiplas dimensoes de variacao |

**Vantagens da Composicao:**
- Flexibilidade em tempo de execucao
- Baixo acoplamento entre componentes
- Facilidade de testar (mockar componentes)
- Evita problemas de heranca multipla
- Cumpre SRP e DIP

**Arquivos:**
- `oop/composicao/ComposicaoVsHerancaExample.java`
- `oop/composicao/ComposicaoVsHerancaTest.java`

## Os 5 Principios SOLID

SOLID e um acronimo para cinco principios de design de software que visam tornar o codigo mais facil de manter, entender e estender.

### 1. SRP - Single Responsibility Principle (Responsabilidade Unica)

**Conceito:** Uma classe deve ter apenas um motivo para mudar.

**Problema:** Uma classe fazendo muitas coisas
- `FuncionarioErrado` calcula salario, envia email, salva no banco e gera relatorio

**Solucao:** Separar em classes especializadas
- `Funcionario`: Apenas dados
- `CalculadoraSalario`: Calcula salarios
- `ServicoEmail`: Envia notificacoes
- `FuncionarioRepository`: Persiste dados
- `GeradorRelatorio`: Gera relatorios

**Arquivos:**
- `solid/srp/SingleResponsibilityExample.java`
- `solid/srp/SingleResponsibilityTest.java`

### 2. OCP - Open/Closed Principle (Aberto/Fechado)

**Conceito:** Entidades devem estar abertas para extensao, mas fechadas para modificacao.

**Problema:** Modificar codigo existente para adicionar novos tipos
- `ProcessadorPagamentoErrado` usa if/else para cada tipo de pagamento

**Solucao:** Usar abstracoes (interfaces)
- Interface `MetodoPagamento` define o contrato
- Cada tipo (`PagamentoCartaoCredito`, `PagamentoBoleto`, `PagamentoPix`) implementa a interface
- Novos tipos sao adicionados como novas classes (ex: `PagamentoCripto`)
- `ProcessadorPagamento` funciona com qualquer implementacao

**Arquivos:**
- `solid/ocp/OpenClosedExample.java`
- `solid/ocp/OpenClosedTest.java`

### 3. LSP - Liskov Substitution Principle (Substituicao de Liskov)

**Conceito:** Classes filhas devem poder substituir classes pai sem alterar o comportamento.

**Problema:** Subclasse quebra o contrato da classe pai
- `QuadradoErrado` estende `RetanguloErrado` e altera comportamento dos setters
- `FuncionarioVoluntarioErrado` lanca excecao em `calcularSalario()`

**Solucao:** Respeitar o contrato
- `Retangulo` e `Quadrado` implementam `FormaGeometrica` (sem heranca problemática)
- `Desenvolvedor`, `Estagiario` e `Voluntario` respeitam o contrato de `Funcionario`
- `Voluntario` retorna 0 (mantem contrato) em vez de lancar excecao

**Arquivos:**
- `solid/lsp/LiskovSubstitutionExample.java`
- `solid/lsp/LiskovSubstitutionTest.java`

### 4. ISP - Interface Segregation Principle (Segregacao de Interfaces)

**Conceito:** Uma classe nao deve ser forcada a implementar interfaces que nao usa.

**Problema:** Interface "gorda" obriga classes a implementar metodos desnecessarios
- `TrabalhadorErrado` exige: trabalhar, comer, dormir, programar, atender cliente
- `RoboErrado` e forcado a implementar `comer()` e `dormir()` (lanca excecao!)

**Solucao:** Interfaces pequenas e especificas
- `Trabalhador`: Apenas `trabalhar()`
- `SerVivo`: `comer()`, `dormir()`
- `Programador`: `programar()`, `revisarCodigo()`
- `Atendente`: `atenderCliente()`, `resolverProblema()`
- `Desenvolvedor` implementa `Trabalhador`, `SerVivo`, `Programador`
- `Robo` implementa apenas `Trabalhador`

**Arquivos:**
- `solid/isp/InterfaceSegregationExample.java`
- `solid/isp/InterfaceSegregationTest.java`

### 5. DIP - Dependency Inversion Principle (Inversao de Dependencia)

**Conceito:** Modulos de alto nivel nao devem depender de modulos de baixo nivel. Ambos devem depender de abstracoes.

**Problema:** Classe de alto nivel depende diretamente de implementacoes
- `ServicoUsuarioErrado` cria `MySQLDatabase` diretamente (acoplamento forte)

**Solucao:** Depender de abstrações (interfaces)
- Interface `Database` define o contrato
- `MySQLDatabaseImpl`, `PostgreSQLDatabaseImpl`, `InMemoryDatabase` implementam a interface
- `ServicoUsuario` recebe `Database` via construtor (injecao de dependencia)
- Facilidade para testar com `InMemoryDatabase` ou mocks

**Beneficios adicionais:**
- Testabilidade: usar `FakeEmailProvider` em vez de `SendGrid`
- Flexibilidade: trocar `MySQL` por `PostgreSQL` sem mudar `ServicoUsuario`

**Arquivos:**
- `solid/dip/DependencyInversionExample.java`
- `solid/dip/DependencyInversionTest.java`

## Tratamento de Excecoes

Java divide excecoes em duas categorias principais: **Checked** (verificadas) e **Unchecked** (nao verificadas).

### Checked Exceptions

**Caracteristicas:**
- Herdam de `Exception` (mas nao de `RuntimeException`)
- Obrigatorias de tratar ou declarar com `throws`
- Representam condicoes recuperaveis

**Exemplos:** `IOException`, `SQLException`, `ContaNaoEncontradaException`

```java
// Deve ser tratada ou declarada
public void transferir(String contaOrigem, String contaDestino, double valor) 
        throws ContaNaoEncontradaException, SQLException {
    // implementacao
}
```

### Unchecked Exceptions

**Caracteristicas:**
- Herdam de `RuntimeException`
- Nao obrigatorias de tratar
- Representam bugs de programacao

**Exemplos:** `NullPointerException`, `IllegalArgumentException`, `ValorInvalidoException`

```java
// Lançada automaticamente, nao precisa declarar
public void depositar(double valor) {
    if (valor <= 0) {
        throw new ValorInvalidoException(valor, 0.01, Double.MAX_VALUE);
    }
}
```

### Estruturas de Tratamento

**Try-Catch-Finally:**
```java
try {
    // codigo que pode lançar excecao
} catch (ContaNaoEncontradaException e) {
    // tratamento especifico
} catch (SQLException e) {
    // tratamento de banco
} finally {
    // sempre executa (fechar recursos)
}
```

**Try-With-Resources (Java 7+):**
```java
try (BufferedReader reader = Files.newBufferedReader(arquivo)) {
    // usa recurso
} catch (IOException e) {
    // tratamento
}
// recurso fechado automaticamente
```

**Multi-Catch (Java 7+):**
```java
try {
    // codigo
} catch (ContaNaoEncontradaException | SQLException e) {
    // tratamento para ambas
}
```

### Boas Praticas

- Criar excecoes customizadas para erros de dominio
- Sempre usar `finally` ou try-with-resources para liberar recursos
- Nao capturar `Exception` generica (esconda bugs)
- Documentar excecoes no Javadoc com `@throws`

**Arquivos:**
- `excecoes/ExcecoesExample.java`
- `excecoes/ExcecoesTest.java`

## Generics

Generics permitem criar classes, interfaces e metodos que operam em tipos especificos, fornecendo type safety em tempo de compilacao.

### Beneficios

- **Type Safety:** Detecta erros em tempo de compilacao
- **Eliminacao de Casts:** Nao precisa converter tipos manualmente
- **Reutilizacao de Codigo:** Uma implementacao serve para varios tipos

### Sintaxe Basica

```java
// Classe generica
class Caixa<T> {
    private T conteudo;
    
    public void guardar(T item) {
        this.conteudo = item;
    }
    
    public T retirar() {
        return conteudo;
    }
}

// Uso
Caixa<String> caixaTexto = new Caixa<>();
caixaTexto.guardar("Ola");
String valor = caixaTexto.retirar(); // Sem cast!
```

### Bounded Types

Restringir tipos genericos:

```java
// T deve ser Number ou subclasse
class NumberBox<T extends Number> {
    private T numero;
    
    public double doubleValue() {
        return numero.doubleValue(); // Metodo de Number disponivel
    }
}

// Uso
NumberBox<Integer> intBox = new NumberBox<>();
NumberBox<Double> doubleBox = new NumberBox<>();
// NumberBox<String> erro! String nao extends Number
```

### Wildcards

**Unbounded (`<?>`):**
```java
public void imprimirQualquerLista(List<?> lista) {
    for (Object item : lista) {
        System.out.println(item);
    }
}
```

**Upper Bounded (`<? extends T>`):**
```java
// Aceita List<Integer>, List<Double>, etc.
public double somarNumeros(List<? extends Number> numeros) {
    return numeros.stream().mapToDouble(Number::doubleValue).sum();
}
```

**Lower Bounded (`<? super T>`):**
```java
// Aceita List<Number>, List<Object>
public void adicionarInteiros(List<? super Integer> lista) {
    lista.add(1);
    lista.add(2);
}
```

### Metodos Genericos

```java
public static <T> void swap(T[] array, int i, int j) {
    T temp = array[i];
    array[i] = array[j];
    array[j] = temp;
}

public static <T extends Comparable<T>> T max(T a, T b) {
    return a.compareTo(b) > 0 ? a : b;
}
```

### Type Erasure

Em tempo de execucao, generics sao removidos (erasure). Nao e possivel:
- Criar array de tipo generico: `new T[10]`
- Usar `instanceof` com tipo generico
- Usar primitivos (use `Integer`, `Double`, etc.)

**Arquivos:**
- `generics/GenericsExample.java`
- `generics/GenericsTest.java`

## Multithreading e Concorrencia

Java suporta programacao concorrente desde suas primeiras versoes. Com o pacote `java.util.concurrent` (Java 5+), tornou-se muito mais facil e seguro trabalhar com multiplas threads.

### Conceitos Fundamentais

**Thread vs Processo:**
- **Processo:** Programa em execucao com seu proprio espaco de memoria isolado
- **Thread:** Unidade basica de execucao dentro de um processo
- **Concorrencia:** Multiplas threads executando simultaneamente (aparentemente)
- **Paralelismo:** Multiplas threads executando ao mesmo tempo (multi-core)

**Desafios:**
- **Race Condition:** Duas threads acessando dados compartilhados
- **Deadlock:** Threads bloqueadas esperando umas pelas outras
- **Starvation:** Thread nunca obtem acesso ao recurso
- **Livelock:** Threads mudam estado sem progredir

### Criando Threads

**Tres formas:**

```java
// 1. Estendendo Thread
class MinhaThread extends Thread {
    public void run() {
        System.out.println("Thread executando");
    }
}
new MinhaThread().start();

// 2. Implementando Runnable (preferido)
class MeuRunnable implements Runnable {
    public void run() {
        System.out.println("Runnable executando");
    }
}
new Thread(new MeuRunnable()).start();

// 3. Lambda (Java 8+)
new Thread(() -> System.out.println("Lambda")).start();
```

### Sincronizacao

**Synchronized:**
```java
public class Contador {
    private int valor = 0;
    
    // Metodo synchronized
    public synchronized void incrementar() {
        valor++; // Atomico
    }
    
    // Bloco synchronized
    public void incrementarBloco() {
        synchronized (this) {
            valor++;
        }
    }
}
```

**Volatile:**
```java
private volatile boolean running = true;

// Garante visibilidade entre threads
// Toda thread ve o valor atualizado
public void parar() {
    running = false;
}
```

### Atomic Classes

Operacoes atomicas sem bloqueio (CAS - Compare-And-Swap):

```java
AtomicInteger contador = new AtomicInteger(0);

contador.incrementAndGet();     // ++i
contador.getAndIncrement();     // i++
contador.addAndGet(10);         // i += 10
contador.compareAndSet(0, 1);   // Atomico: se 0, vira 1
```

### Executors e Thread Pools

```java
// Thread Pool fixo
ExecutorService executor = Executors.newFixedThreadPool(4);

// Executa tarefa
executor.submit(() -> System.out.println("Tarefa"));

// Agenda execucao
ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);
scheduler.schedule(() -> System.out.println("Depois de 5s"), 5, TimeUnit.SECONDS);

// IMPORTANTE: Sempre desligar
executor.shutdown();
```

### Callable e Future

```java
// Tarefa que retorna valor
Callable<Integer> tarefa = () -> {
    Thread.sleep(1000);
    return 42;
};

Future<Integer> future = executor.submit(tarefa);
Integer resultado = future.get(); // Bloqueia ate terminar

// Com timeout
Integer resultado = future.get(2, TimeUnit.SECONDS);
```

### Colecoes Concorrentes

```java
// Map thread-safe (melhor que Hashtable/synchronizedMap)
ConcurrentHashMap<String, Integer> mapa = new ConcurrentHashMap<>();

// Lista - otima para leituras frequentes
CopyOnWriteArrayList<String> lista = new CopyOnWriteArrayList<>();

// Fila bloqueante
BlockingQueue<String> fila = new LinkedBlockingQueue<>();
fila.put("elemento");     // Bloqueia se cheia
String item = fila.take(); // Bloqueia se vazia
```

### Sincronizadores

**CountDownLatch:**
```java
CountDownLatch latch = new CountDownLatch(3); // Aguarda 3 eventos

// Cada thread chama ao terminar
latch.countDown();

// Thread principal aguarda
latch.await(); // Bloqueia ate contador zerar
```

**Semaphore:**
```java
Semaphore semaphore = new Semaphore(2); // Max 2 acessos simultaneos

semaphore.acquire();  // Adquire permissao (bloqueia se nao houver)
// Usa recurso
semaphore.release();  // Libera permissao
```

**CyclicBarrier:**
```java
CyclicBarrier barrier = new CyclicBarrier(3); // 3 threads

// Cada thread chama
barrier.await(); // Bloqueia ate todas as 3 chamarem
// Continua quando todas chegarem
```

### CompletableFuture (Java 8+)

Programacao assincrona funcional:

```java
CompletableFuture<String> future = CompletableFuture
    .supplyAsync(() -> "Hello")           // Executa assincrono
    .thenApply(String::toUpperCase)       // Transforma
    .thenApply(s -> s + " World")         // Transforma novamente
    .thenAccept(System.out::println);     // Consome resultado

// Combinacao
CompletableFuture<String> f1 = CompletableFuture.supplyAsync(() -> "A");
CompletableFuture<String> f2 = CompletableFuture.supplyAsync(() -> "B");

CompletableFuture<String> combinado = f1.thenCombine(f2, (a, b) -> a + b);
```

**Arquivos:**
- `concorrencia/ConcorrenciaExample.java`
- `concorrencia/ConcorrenciaTest.java`

## JVM Internals

### Arquitetura da JVM

```
+--------------------------------------------------+
|                  Class Loader                     |
|  (Bootstrap -> Extension -> Application)         |
+--------------------------------------------------+
                          |
                          v
+--------------------------------------------------+
|              Runtime Data Areas                   |
|  +------------+ +------------+ +-------------+   |
|  |   Heap     | |    Stack   | |   Method    |   |
|  | (objetos)  | | (frames)   | |    Area     |   |
|  +------------+ +------------+ +-------------+   |
+--------------------------------------------------+
                          |
                          v
+--------------------------------------------------+
|              Execution Engine                     |
|  Interpreter -> JIT Compiler -> Native Code      |
+--------------------------------------------------+
```

### Class Loading

**Hierarquia de ClassLoaders:**

1. **Bootstrap ClassLoader** (nativo)
   - Carrega classes do Java (`java.lang.*`, `java.util.*`)
   - Escrito em C/C++, retorna `null` em Java

2. **Platform/Extension ClassLoader**
   - Carrega extensoes do JDK
   - Pai do Application

3. **Application ClassLoader**
   - Carrega classes do classpath
   - Padrão para classes da aplicacao

**Principio de Delegacao:**
```java
ClassLoader loader = getClass().getClassLoader();
ClassLoader parent = loader.getParent(); // Platform
ClassLoader bootstrap = parent.getParent(); // null = Bootstrap
```

### Memory Management

**Heap - Estrutura:**

```
+--------------------------------------------------+
|                  Young Generation                 |
|  +-----------+ +-----------+ +----------------+  |
|  |   Eden    | | Survivor 0| |   Survivor 1   |  |
|  |  (novos)  | |   (S0)    | |     (S1)       |  |
|  +-----------+ +-----------+ +----------------+  |
+--------------------------------------------------+
|              Old Generation                       |
|         (objetos longevos)                        |
+--------------------------------------------------+
|              Metaspace (Java 8+)                  |
|         (fora do heap - metadados)               |
+--------------------------------------------------+
```

**Ciclo de Vida dos Objetos:**
1. Criado no **Eden**
2. Se sobreviver GC, vai para **Survivor 0** ou **S1**
3. Apos varias colecoes, promovido para **Old Gen**
4. Eventualmente coletado por **Major GC**

### Garbage Collection

**Tipos de GC:**

| Tipo | Caracteristica | Use Case |
|------|---------------|----------|
| **Serial** | Uma thread, pausa total | Aplicacoes pequenas |
| **Parallel** | Multi-thread, throughput | Padrao Java 8 |
| **G1** | Regioes, pausa previsivel | Padrao Java 9+ |
| **ZGC** | Pausa < 10ms | Heaps enormes (TB) |
| **Shenandoah** | Pausa independente do heap | Latencia baixa |

**Monitoramento:**
```java
// Informacoes dos GCs
List<GarbageCollectorMXBean> gcs = ManagementFactory.getGarbageCollectorMXBeans();
for (GarbageCollectorMXBean gc : gcs) {
    System.out.println(gc.getName() + ": " + gc.getCollectionCount());
}

// Memoria do heap
Runtime runtime = Runtime.getRuntime();
long used = runtime.totalMemory() - runtime.freeMemory();
```

**Memory Leaks Comuns:**

1. **Cache sem limite:**
```java
// PROBLEMA
Map<String, Object> cache = new HashMap<>(); // Nunca remove!

// SOLUCAO
Map<String, Object> cache = new WeakHashMap<>(); // Permite GC
// Ou use cache com expiracao (Caffeine, Guava)
```

2. **Listeners nao removidos:**
```java
// PROBLEMA: Adiciona listener mas nunca remove
button.addActionListener(listener);

// SOLUCAO: Use WeakReference ou remova explicitamente
```

### JIT Compiler

**Just-In-Time Compilation:**
- Bytecode e interpretado inicialmente
- JVM identifica "hot spots" (codigo frequente)
- JIT compila para codigo nativo da maquina
- Proximas execucoes usam codigo nativo (muito mais rapido)

**Otimizacoes:**
- **Inlining:** Substitui chamada de metodo pelo corpo
- **Escape Analysis:** Identifica objetos que nao escapam do metodo
- **Dead Code Elimination:** Remove codigo inalcancavel

```java
// Metodo candidato a otimizacao JIT
private int calcular(int x) {
    return x * x + 2 * x + 1; // Sera inline e otimizado
}
```

### Stack e StackOverflow

**Caracteristicas da Stack:**
- Cada thread tem sua propria pilha
- Contem frames de execucao de metodos
- Tamanho configuravel: `-Xss1m` (1MB)
- **StackOverflowError:** Recursao infinita ou muitas chamadas

```java
// Causa StackOverflowError
void recursaoInfinita() {
    recursaoInfinita(); // Chama indefinidamente
}
```

**Arquivos:**
- `jvm/JVMInternalsExample.java`
- `jvm/JVMInternalsTest.java`

## Como Executar

### Compilar o projeto:
```bash
mvn clean compile
```

### Executar todos os testes:
```bash
mvn test
```

### Executar testes especificos:
```bash
# Testes de OOP
mvn test -Dtest=ContaBancariaTest
mvn test -Dtest=FuncionarioTest
mvn test -Dtest=CalculadoraPagamentoTest
mvn test -Dtest=GerenciadorNotificacoesTest

# Testes de SOLID
mvn test -Dtest=SingleResponsibilityTest
mvn test -Dtest=OpenClosedTest
mvn test -Dtest=LiskovSubstitutionTest
mvn test -Dtest=InterfaceSegregationTest
mvn test -Dtest=DependencyInversionTest

# Testes de outros modulos
mvn test -Dtest=LambdasTest
mvn test -Dtest=StreamsTest
mvn test -Dtest=GenericsTest
mvn test -Dtest=ExcecoesTest
mvn test -Dtest=CollectionsTest
mvn test -Dtest=TimeTest
mvn test -Dtest=AnnotationsTest
mvn test -Dtest=EnumsTest
mvn test -Dtest=NIOTest
```

## Cobertura de Testes

Cada modulo tem testes abrangentes demonstrando:
- Comportamentos esperados
- Casos de erro
- Validacoes de regras de negocio
- Uso correto dos conceitos

**Total de testes:** 370+ testes automatizados

## Requisitos

- Java 17 ou superior
- Maven 3.8+

## Objetivo de Aprendizado

Este projeto e ideal para:
- Compreender os 4 pilares de OOP na pratica
- Entender e aplicar os principios SOLID
- Ver como os conceitos se aplicam em codigo real
- Estudar padroes de projeto relacionados
- Aprender boas praticas de desenvolvimento Java

## Estrutura de Pacotes

```
com.avanade.curso
├── oop              # Orientacao a Objetos (4 pilares)
├── solid            # Principios SOLID
├── funcional        # Programacao Funcional
├── excecoes         # Tratamento de Excecoes
├── generics         # Generics
├── collections      # Collections Framework
├── time             # Java Time API
├── annotations      # Annotations e Reflection
├── enums            # Enums
└── nio              # NIO.2 (Manipulacao de Arquivos)
```
