package com.avanade.curso.oop.polimorfismo;

/**
 * Pagamento via Pix
 * Implementação específica da interface Pagamento
 */
public class PagamentoPix implements Pagamento {
    
    private double valor;
    private String chavePix;
    private boolean processado;
    private static final double TAXA_PIX_PESSOA_FISICA = 0.0; // Gratuito PF
    private static final double TAXA_PIX_PJ = 0.005; // 0.5% para PJ
    private boolean isPessoaJuridica;
    
    public PagamentoPix(double valor, String chavePix, boolean isPessoaJuridica) {
        this.valor = valor;
        this.chavePix = chavePix;
        this.isPessoaJuridica = isPessoaJuridica;
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
        // Pix processa instantaneamente
        System.out.println("Processando PIX para: " + chavePix);
        this.processado = true;
        return true;
    }
    
    @Override
    public double calcularTaxa() {
        if (isPessoaJuridica) {
            return valor * TAXA_PIX_PJ;
        }
        return TAXA_PIX_PESSOA_FISICA; // Gratuito para pessoa física
    }
    
    @Override
    public String obterDescricao() {
        return "Pix" + (isPessoaJuridica ? " (Pessoa Jurídica)" : " (Pessoa Física)");
    }
    
    @Override
    public boolean estornar() {
        if (!processado) {
            return false;
        }
        // Pix não tem estorno direto, mas pode ser feito via devolução
        System.out.println("Solicitando devolução PIX para: " + chavePix);
        this.processado = false;
        return true;
    }
    
    public boolean isProcessado() {
        return processado;
    }
}
