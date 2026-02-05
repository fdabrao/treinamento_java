package com.avanade.curso.oop.polimorfismo;

/**
 * Pagamento via Cartão de Crédito
 * Implementação específica da interface Pagamento
 */
public class PagamentoCartaoCredito implements Pagamento {
    
    private double valor;
    private String numeroCartao;
    private int parcelas;
    private boolean processado;
    private static final double TAXA_POR_PARCELA = 0.02; // 2% por parcela
    private static final double TAXA_BASE = 0.05; // 5% base
    
    public PagamentoCartaoCredito(double valor, String numeroCartao, int parcelas) {
        this.valor = valor;
        this.numeroCartao = numeroCartao;
        this.parcelas = Math.max(1, parcelas);
        this.processado = false;
    }
    
    @Override
    public double getValor() {
        return valor;
    }
    
    @Override
    public boolean processar() {
        if (!validar()) {
            return false;
        }
        // Simula processamento no gateway de pagamento
        System.out.println("Processando cartão " + mascaraCartao() + " em " + parcelas + "x");
        this.processado = true;
        return true;
    }
    
    @Override
    public double calcularTaxa() {
        // Taxa aumenta com número de parcelas
        double taxaPercentual = TAXA_BASE + (TAXA_POR_PARCELA * (parcelas - 1));
        return valor * taxaPercentual;
    }
    
    @Override
    public String obterDescricao() {
        return String.format("Cartão de Crédito (%dx)", parcelas);
    }
    
    @Override
    public boolean estornar() {
        if (!processado) {
            return false;
        }
        System.out.println("Estornando pagamento no cartão " + mascaraCartao());
        this.processado = false;
        return true;
    }
    
    private String mascaraCartao() {
        if (numeroCartao == null || numeroCartao.length() < 4) {
            return "****";
        }
        return "****-****-****-" + numeroCartao.substring(numeroCartao.length() - 4);
    }
    
    public int getParcelas() {
        return parcelas;
    }
    
    public boolean isProcessado() {
        return processado;
    }
}
