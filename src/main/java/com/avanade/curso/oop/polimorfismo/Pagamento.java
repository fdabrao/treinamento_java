package com.avanade.curso.oop.polimorfismo;

/**
 * Interface que define o contrato para todos os tipos de pagamento.
 * 
 * Cada implementação terá seu próprio comportamento específico,
 * mas todos respeitarão este contrato.
 */
public interface Pagamento {
    
    double getValor();
    
    /**
     * Processa o pagamento - cada tipo implementa de forma diferente
     */
    boolean processar();
    
    /**
     * Calcula a taxa específica do método de pagamento
     */
    double calcularTaxa();
    
    /**
     * Retorna descrição do método
     */
    String obterDescricao();
    
    /**
     * Estorna o pagamento
     */
    boolean estornar();
    
    /**
     * Método default - disponível para todas as implementações
     */
    default boolean validar() {
        return getValor() > 0;
    }
}
