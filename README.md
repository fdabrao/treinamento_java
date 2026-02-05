# OOP - 4 Pilares da Orientação a Objetos

Projeto Java demonstrando os 4 pilares fundamentais da Orientação a Objetos com exemplos práticos e testes unitários.

## 📁 Estrutura do Projeto

```
oop-pilares/
├── pom.xml                          # Configuração Maven
└── src/
    ├── main/java/com/exemplo/
    │   ├── encapsulamento/          # Pilar 1: Encapsulamento
    │   │   └── ContaBancaria.java
    │   ├── heranca/                 # Pilar 2: Herança
    │   │   ├── Funcionario.java
    │   │   ├── Gerente.java
    │   │   └── Desenvolvedor.java
    │   ├── polimorfismo/            # Pilar 3: Polimorfismo
    │   │   ├── Pagamento.java
    │   │   ├── PagamentoCartaoCredito.java
    │   │   ├── PagamentoBoleto.java
    │   │   ├── PagamentoPix.java
    │   │   └── CalculadoraPagamento.java
    │   └── abstracao/               # Pilar 4: Abstração
    │       ├── ServicoNotificacao.java
    │       ├── NotificacaoEmail.java
    │       ├── NotificacaoSms.java
    │       ├── NotificacaoPush.java
    │       └── GerenciadorNotificacoes.java
    └── test/java/com/exemplo/
        ├── encapsulamento/ContaBancariaTest.java
        ├── heranca/FuncionarioTest.java
        ├── polimorfismo/CalculadoraPagamentoTest.java
        └── abstracao/GerenciadorNotificacoesTest.java
```

## 🏛️ Os 4 Pilares

### 1️⃣ Encapsulamento

**Conceito:** Ocultar detalhes internos e expor apenas o necessário.

**Exemplo:** `ContaBancaria`
- Atributos são `private` (não acessíveis diretamente)
- Acesso controlado via getters/setters com validação
- Saldo só pode ser modificado através de métodos de negócio (`depositar()`, `sacar()`)

**Benefícios:**
- Protege dados de acesso indevido
- Permite validações antes de modificar valores
- Facilita manutenção (mudanças internas não afetam código externo)

### 2️⃣ Herança

**Conceito:** Permitir que uma classe herde características de outra (relacionamento "é um").

**Exemplo:** `Funcionario` → `Gerente` / `Desenvolvedor`
- `Gerente` **é um** `Funcionario` com características adicionais
- `Desenvolvedor` **é um** `Funcionario` com níveis de senioridade

**Benefícios:**
- Reuso de código comum
- Especialização de comportamentos
- Hierarquia lógica de classes

### 3️⃣ Polimorfismo

**Conceito:** "Muitas formas" - objetos diferentes responderem ao mesmo método de formas distintas.

**Exemplo:** Interface `Pagamento`
- `PagamentoCartaoCredito`, `PagamentoBoleto`, `PagamentoPix`
- Todos implementam `processar()` e `calcularTaxa()`, mas cada um à sua maneira

**Tipos demonstrados:**
- **Sobrescrita (Override):** Mesmo método, comportamento diferente
- **Sobrecarga (Overload):** Mesmo nome, parâmetros diferentes
- **Polimorfismo de inclusão:** Tratar objetos filhos como o pai

**Benefícios:**
- Código flexível e extensível
- Tratamento uniforme de objetos diferentes
- Facilidade para adicionar novos tipos

### 4️⃣ Abstração

**Conceito:** Modelar apenas o essencial, escondendo complexidades.

**Exemplo:** `ServicoNotificacao` (classe abstrata)
- Define O QUÊ o serviço faz (`enviar()`, `configurar()`)
- Cada implementação define COMO fazer (`NotificacaoEmail`, `NotificacaoSms`, `NotificacaoPush`)
- Usa Template Method para definir fluxo padrão

**Benefícios:**
- Simplifica modelagem de sistemas complexos
- Separa o "o que" do "como"
- Reduz acoplamento entre componentes

## 🚀 Como Executar

### Compilar o projeto:
```bash
mvn clean compile
```

### Executar todos os testes:
```bash
mvn test
```

### Executar testes específicos:
```bash
# Testes de Encapsulamento
mvn test -Dtest=ContaBancariaTest

# Testes de Herança
mvn test -Dtest=FuncionarioTest

# Testes de Polimorfismo
mvn test -Dtest=CalculadoraPagamentoTest

# Testes de Abstração
mvn test -Dtest=GerenciadorNotificacoesTest
```

## 📊 Cobertura de Testes

Cada pilar tem testes abrangentes demonstrando:
- Comportamentos esperados
- Casos de erro
- Validações de regras de negócio
- Uso correto dos conceitos de OOP

## 📝 Requisitos

- Java 17 ou superior
- Maven 3.8+

## 🎯 Objetivo de Aprendizado

Este projeto é ideal para:
- Compreender os 4 pilares de OOP na prática
- Ver como os conceitos se aplicam em código real
- Estudar padrões de projeto relacionados
- Entender a relação entre os pilares (herança + polimorfismo, encapsulamento + abstração)
