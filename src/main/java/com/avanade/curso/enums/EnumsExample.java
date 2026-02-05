package com.avanade.curso.enums;

import java.util.*;

/**
 * ENUMS EM JAVA
 * 
 * Enums são tipos especiais de classe que representam um conjunto fixo de constantes.
 * São imutáveis, type-safe e podem ter construtores, métodos e campos.
 * 
 * Benefícios:
 * - Type safety: Impede valores inválidos em tempo de compilação
 * - Singleton pattern: Cada constante é uma instância única
 * - Podem implementar interfaces
 * - Ideais para switches
 * - Serialização segura
 */

// ============================================
// ENUM SIMPLES
// ============================================

enum DiaSemana {
    DOMINGO, SEGUNDA, TERCA, QUARTA, QUINTA, SEXTA, SABADO
}

// ============================================
// ENUM COM CONSTRUTOR E CAMPOS
// ============================================

enum StatusPedido {
    PENDENTE("Aguardando pagamento", 1),
    PAGO("Pagamento confirmado", 2),
    PROCESSANDO("Em separação", 3),
    ENVIADO("Enviado", 4),
    ENTREGUE("Entregue", 5),
    CANCELADO("Cancelado", 99);
    
    private final String descricao;
    private final int codigo;
    
    // Construtor dos enums é sempre private (ou package-private)
    StatusPedido(String descricao, int codigo) {
        this.descricao = descricao;
        this.codigo = codigo;
    }
    
    public String getDescricao() {
        return descricao;
    }
    
    public int getCodigo() {
        return codigo;
    }
    
    // Método de instância
    public boolean isFinalizado() {
        return this == ENTREGUE || this == CANCELADO;
    }
    
    // Método de instância
    public boolean podeCancelar() {
        return this == PENDENTE || this == PAGO;
    }
    
    // Método estático para buscar por código
    public static StatusPedido porCodigo(int codigo) {
        for (StatusPedido status : values()) {
            if (status.codigo == codigo) {
                return status;
            }
        }
        throw new IllegalArgumentException("Código inválido: " + codigo);
    }
}

// ============================================
// ENUM COM MÉTODOS ABSTRATOS
// ============================================

enum OperacaoMatematica {
    SOMA {
        @Override
        public double aplicar(double x, double y) {
            return x + y;
        }
    },
    SUBTRACAO {
        @Override
        public double aplicar(double x, double y) {
            return x - y;
        }
    },
    MULTIPLICACAO {
        @Override
        public double aplicar(double x, double y) {
            return x * y;
        }
    },
    DIVISAO {
        @Override
        public double aplicar(double x, double y) {
            if (y == 0) {
                throw new ArithmeticException("Divisão por zero");
            }
            return x / y;
        }
    };
    
    // Método abstrato que cada constante deve implementar
    public abstract double aplicar(double x, double y);
    
    // Método concreto disponível para todas
    public String getSimbolo() {
        return switch (this) {
            case SOMA -> "+";
            case SUBTRACAO -> "-";
            case MULTIPLICACAO -> "*";
            case DIVISAO -> "/";
        };
    }
}

// ============================================
// ENUM IMPLEMENTANDO INTERFACE
// ============================================

interface Desconto {
    double calcularDesconto(double valor);
}

enum TipoCliente implements Desconto {
    PADRAO(0.0) {
        @Override
        public double calcularDesconto(double valor) {
            return 0;
        }
    },
    VIP(0.10) {
        @Override
        public double calcularDesconto(double valor) {
            return valor * percentual;
        }
    },
    PREMIUM(0.20) {
        @Override
        public double calcularDesconto(double valor) {
            return valor * percentual;
        }
    };
    
    protected final double percentual;
    
    TipoCliente(double percentual) {
        this.percentual = percentual;
    }
}

// ============================================
// ENUM COM STRATEGY PATTERN
// ============================================

enum TipoNotificacao {
    EMAIL {
        @Override
        public void enviar(String destinatario, String mensagem) {
            System.out.println("[EMAIL] Para: " + destinatario + " | " + mensagem);
        }
    },
    SMS {
        @Override
        public void enviar(String destinatario, String mensagem) {
            System.out.println("[SMS] Para: " + destinatario + " | " + mensagem);
        }
    },
    PUSH {
        @Override
        public void enviar(String destinatario, String mensagem) {
            System.out.println("[PUSH] Para: " + destinatario + " | " + mensagem);
        }
    };
    
    public abstract void enviar(String destinatario, String mensagem);
}

// ============================================
// ENUM COM COLEÇÕES ESPECIALIZADAS
// ============================================

enum Prioridade {
    BAIXA(1),
    MEDIA(2),
    ALTA(3),
    CRITICA(4);
    
    private final int valor;
    
    Prioridade(int valor) {
        this.valor = valor;
    }
    
    public int getValor() {
        return valor;
    }
}

// ============================================
// USO DE ENUMS
// ============================================

class ProcessadorPedido {
    
    public void processar(StatusPedido status) {
        // Switch com enums (Java 17+: pode usar pattern matching)
        switch (status) {
            case PENDENTE -> System.out.println("Aguardando pagamento...");
            case PAGO -> System.out.println("Preparando envio...");
            case PROCESSANDO -> System.out.println("Separando itens...");
            case ENVIADO -> System.out.println("Em transporte...");
            case ENTREGUE -> System.out.println("Entregue ao cliente!");
            case CANCELADO -> System.out.println("Pedido cancelado");
            default -> throw new IllegalStateException("Status desconhecido");
        }
        
        // Antes do Java 14
        /*
        switch (status) {
            case PENDENTE:
                System.out.println("Aguardando...");
                break;
            case PAGO:
                System.out.println("Preparando...");
                break;
            // ...
        }
        */
    }
    
    public String getProximoPasso(StatusPedido atual) {
        return switch (atual) {
            case PENDENTE -> "Confirmar pagamento";
            case PAGO -> "Iniciar separação";
            case PROCESSANDO -> "Despachar";
            case ENVIADO -> "Aguardar entrega";
            case ENTREGUE -> "Pedido completo";
            case CANCELADO -> "Processar reembolso";
        };
    }
}

class SistemaNotificacao {
    
    public void notificarUsuario(String usuario, String mensagem, TipoNotificacao tipo) {
        tipo.enviar(usuario, mensagem);
    }
    
    public void notificarTodos(String mensagem, Set<TipoNotificacao> canais, String usuario) {
        for (TipoNotificacao canal : canais) {
            canal.enviar(usuario, mensagem);
        }
    }
}

class Calculadora {
    
    public double calcular(double x, double y, OperacaoMatematica operacao) {
        System.out.printf("Calculando: %.2f %s %.2f = ", x, operacao.getSimbolo(), y);
        double resultado = operacao.aplicar(x, y);
        System.out.println(resultado);
        return resultado;
    }
}

// ============================================
// EXEMPLOS COM ENUMSET E ENUMMAP
// ============================================

class ExemploEnumCollections {
    
    public void demonstrarEnumSet() {
        // EnumSet: implementação eficiente de Set para enums
        EnumSet<DiaSemana> diasUteis = EnumSet.of(
            DiaSemana.SEGUNDA, DiaSemana.TERCA, DiaSemana.QUARTA,
            DiaSemana.QUINTA, DiaSemana.SEXTA
        );
        
        EnumSet<DiaSemana> fimDeSemana = EnumSet.of(
            DiaSemana.SABADO, DiaSemana.DOMINGO
        );
        
        // Todos os dias
        EnumSet<DiaSemana> todosDias = EnumSet.allOf(DiaSemana.class);
        
        // Nenhum dia
        EnumSet<DiaSemana> nenhumDia = EnumSet.noneOf(DiaSemana.class);
        
        // Complemento
        EnumSet<DiaSemana> diasExcetoSegunda = EnumSet.complementOf(
            EnumSet.of(DiaSemana.SEGUNDA)
        );
        
        System.out.println("Dias úteis: " + diasUteis);
        System.out.println("Fim de semana: " + fimDeSemana);
        System.out.println("Todos os dias: " + todosDias);
    }
    
    public void demonstrarEnumMap() {
        // EnumMap: implementação eficiente de Map com chaves enum
        EnumMap<StatusPedido, String> descricoes = new EnumMap<>(StatusPedido.class);
        descricoes.put(StatusPedido.PENDENTE, "Aguardando");
        descricoes.put(StatusPedido.PAGO, "Pago");
        descricoes.put(StatusPedido.ENVIADO, "Enviado");
        
        System.out.println("EnumMap: " + descricoes);
        
        // Iteração na ordem natural do enum
        for (Map.Entry<StatusPedido, String> entry : descricoes.entrySet()) {
            System.out.println("  " + entry.getKey() + " -> " + entry.getValue());
        }
    }
}

// ============================================
// CLASSE DE DEMONSTRAÇÃO
// ============================================

public class EnumsExample {
    
    public static void demonstrar() {
        System.out.println("=== ENUM SIMPLES ===\n");
        
        DiaSemana hoje = DiaSemana.QUARTA;
        System.out.println("Hoje é: " + hoje);
        System.out.println("Ordinal: " + hoje.ordinal());
        System.out.println("Nome: " + hoje.name());
        
        // Comparar enums
        if (hoje == DiaSemana.QUARTA) {
            System.out.println("É quarta-feira!");
        }
        
        // Switch com enum
        switch (hoje) {
            case SEGUNDA -> System.out.println("Início da semana");
            case SEXTA -> System.out.println("Sextou!");
            case SABADO, DOMINGO -> System.out.println("Fim de semana!");
            default -> System.out.println("Dia normal");
        }
        
        System.out.println("\n=== ENUM COM CAMPOS ===");
        
        StatusPedido status = StatusPedido.PAGO;
        System.out.println("Status: " + status);
        System.out.println("Descrição: " + status.getDescricao());
        System.out.println("Código: " + status.getCodigo());
        System.out.println("Finalizado? " + status.isFinalizado());
        System.out.println("Pode cancelar? " + status.podeCancelar());
        
        // Buscar por código
        StatusPedido encontrado = StatusPedido.porCodigo(4);
        System.out.println("Status código 4: " + encontrado);
        
        System.out.println("\n=== MÉTODOS ABSTRATOS ===");
        
        Calculadora calc = new Calculadora();
        calc.calcular(10, 5, OperacaoMatematica.SOMA);
        calc.calcular(10, 5, OperacaoMatematica.SUBTRACAO);
        calc.calcular(10, 5, OperacaoMatematica.MULTIPLICACAO);
        calc.calcular(10, 5, OperacaoMatematica.DIVISAO);
        
        System.out.println("\n=== INTERFACE ===");
        
        double valor = 1000.0;
        for (TipoCliente tipo : TipoCliente.values()) {
            double desconto = tipo.calcularDesconto(valor);
            System.out.printf("%s: R$ %.2f de desconto%n", tipo, desconto);
        }
        
        System.out.println("\n=== STRATEGY PATTERN ===");
        
        SistemaNotificacao notificacao = new SistemaNotificacao();
        notificacao.notificarUsuario("joao@email.com", "Bem-vindo!", TipoNotificacao.EMAIL);
        notificacao.notificarUsuario("11999999999", "Código: 1234", TipoNotificacao.SMS);
        notificacao.notificarUsuario("device-token", "Nova mensagem", TipoNotificacao.PUSH);
        
        System.out.println("\n=== ITERANDO SOBRE ENUMS ===");
        
        System.out.println("Todos os status:");
        for (StatusPedido s : StatusPedido.values()) {
            System.out.printf("  %s (código: %d) - %s%n", s, s.getCodigo(), s.getDescricao());
        }
        
        System.out.println("\n=== ENUMSET E ENUMMAP ===");
        
        ExemploEnumCollections collections = new ExemploEnumCollections();
        collections.demonstrarEnumSet();
        collections.demonstrarEnumMap();
        
        System.out.println("\n=== PROCESSAMENTO DE PEDIDO ===");
        
        ProcessadorPedido processador = new ProcessadorPedido();
        processador.processar(StatusPedido.PENDENTE);
        processador.processar(StatusPedido.ENVIADO);
        
        System.out.println("\nPróximos passos:");
        for (StatusPedido s : StatusPedido.values()) {
            System.out.println("  " + s + " -> " + processador.getProximoPasso(s));
        }
        
        System.out.println("\n=== BOAS PRÁTICAS ===");
        System.out.println(" Use enums para conjuntos fixos de constantes");
        System.out.println(" Enums são type-safe: impede valores inválidos em compile-time");
        System.out.println(" Podem ter construtores, métodos e implementar interfaces");
        System.out.println(" Use EnumSet e EnumMap para coleções de enums (mais eficientes)");
        System.out.println(" Cada constante de enum é uma instância singleton");
    }
    
    public static void main(String[] args) {
        demonstrar();
    }
}
