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
    │   └── nio/                     # NIO.2 (Arquivos)
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
