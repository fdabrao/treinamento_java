package com.avanade.curso.oop.polimorfismo;

/**
 * Pagamento via Boleto Bancário
 * Implementação específica da interface Pagamento
 */
public class PagamentoBoleto implements Pagamento {
    
    private double valor;
    private String codigoBarras;
    private java.time.LocalDate dataVencimento;
    private boolean processado;
    private static final double TAXA_BOLETO = 2.50; // Taxa fixa
    private static final double DESCONTO_A_VISTA = 0.10; // 10% de desconto
    
    public PagamentoBoleto(double valor, String codigoBarras, java.time.LocalDate dataVencimento) {
        this.valor = valor;
        this.codigoBarras = codigoBarras;
        this.dataVencimento = dataVencimento;
        this.processado = false;
    }
    
    @Override
    public double getValor() {
        // Boleto tem desconto à vista
        return valor * (1 - DESCONTO_A_VISTA);
    }
    
    @Override
    public boolean processar() {
        if (!validar()) {
            return false;
        }
        System.out.println("Gerando boleto: " + codigoBarras);
        this.processado = true;
        return true;
    }
    
    @Override
    public double calcularTaxa() {
        return TAXA_BOLETO; // Taxa fixa para boleto
    }
    
    @Override
    public String obterDescricao() {
        return "Boleto Bancário";
    }
    
    @Override
    public boolean estornar() {
        // Boleto só pode ser cancelado, não estornado
        System.out.println("Cancelando boleto: " + codigoBarras);
        this.processado = false;
        return true;
    }
    
    public boolean isVencido() {
        return java.time.LocalDate.now().isAfter(dataVencimento);
    }
    
    public String getCodigoBarras() {
        return codigoBarras;
    }
    
    public boolean isProcessado() {
        return processado;
    }
}
