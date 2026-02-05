package com.avanade.curso.oop.encapsulamento;

/**
 * ENCAPSULAMENTO
 * 
 * O encapsulamento é o princípio de ocultar os detalhes internos de uma classe
 * e expor apenas o que é necessário através de métodos públicos.
 * 
 * Benefícios:
 * - Protege os dados de acesso direto indevido
 * - Permite validação antes de modificar valores
 * - Facilita manutenção (mudança interna não afeta código externo)
 * - Controle de acesso (getters e setters com lógica)
 */
public class ContaBancaria {
    
    // Atributos PRIVATE - não acessíveis diretamente de fora
    private String numeroConta;
    private String titular;
    private double saldo;
    private boolean ativa;
    
    // Constante para limitação
    private static final double LIMITE_SAQUE = 1000.0;
    
    public ContaBancaria(String numeroConta, String titular, double saldoInicial) {
        this.numeroConta = numeroConta;
        this.titular = titular;
        // Validação no construtor - encapsulamento protege dados desde a criação
        this.saldo = Math.max(saldoInicial, 0);
        this.ativa = true;
    }
    
    // Getter - permite leitura controlada
    public String getNumeroConta() {
        return numeroConta;
    }
    
    public String getTitular() {
        return titular;
    }
    
    // Setter com validação - controle total sobre modificação
    public void setTitular(String novoTitular) {
        if (novoTitular == null || novoTitular.trim().isEmpty()) {
            throw new IllegalArgumentException("Titular não pode ser vazio");
        }
        this.titular = novoTitular;
    }
    
    // Getter calculado - não expõe o atributo diretamente
    public double getSaldo() {
        return saldo;
    }
    
    // NÃO há setter para saldo! Modificação só via métodos de negócio
    
    public boolean isAtiva() {
        return ativa;
    }
    
    // Método de negócio com regras encapsuladas
    public void depositar(double valor) {
        if (!ativa) {
            throw new IllegalStateException("Conta inativa não pode receber depósitos");
        }
        if (valor <= 0) {
            throw new IllegalArgumentException("Valor do depósito deve ser positivo");
        }
        this.saldo += valor;
    }
    
    public void sacar(double valor) {
        if (!ativa) {
            throw new IllegalStateException("Conta inativa não permite saques");
        }
        if (valor <= 0) {
            throw new IllegalArgumentException("Valor do saque deve ser positivo");
        }
        if (valor > saldo) {
            throw new IllegalStateException("Saldo insuficiente");
        }
        if (valor > LIMITE_SAQUE) {
            throw new IllegalArgumentException(
                String.format("Limite de saque excedido. Máximo: R$ %.2f", LIMITE_SAQUE)
            );
        }
        this.saldo -= valor;
    }
    
    public void transferir(ContaBancaria destino, double valor) {
        if (destino == null) {
            throw new IllegalArgumentException("Conta de destino não pode ser nula");
        }
        if (destino == this) {
            throw new IllegalArgumentException("Não pode transferir para a mesma conta");
        }
        // Reutiliza a lógica encapsulada de saque e depósito
        this.sacar(valor);
        destino.depositar(valor);
    }
    
    public void encerrarConta() {
        if (saldo != 0) {
            throw new IllegalStateException(
                String.format("Conta com saldo não pode ser encerrada. Saldo atual: R$ %.2f", saldo)
            );
        }
        this.ativa = false;
    }
    
    @Override
    public String toString() {
        return String.format("ContaBancaria{numero='%s', titular='%s', saldo=R$ %.2f, ativa=%s}",
                numeroConta, titular, saldo, ativa);
    }
}
