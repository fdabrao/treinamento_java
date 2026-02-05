package com.avanade.curso.solid.ocp;

/**
 * O - Open/Closed Principle (Princípio Aberto/Fechado)
 * 
 * Entidades devem estar abertas para extensão, mas fechadas para modificação.
 * Use abstrações (interfaces/classes abstratas) para permitir extensão.
 * 
 * ANTES (errado): Modificar código existente para adicionar novos tipos
 * DEPOIS (correto): Estender através de novas classes
 */

//  ANTES: Precisa modificar toda vez que adicionar novo tipo
enum TipoPagamentoErrado {
    CARTAO_CREDITO, BOLETO, PIX
}

class ProcessadorPagamentoErrado {
    public void processar(String tipo, double valor) {
        if (tipo.equals("CARTAO_CREDITO")) {
            System.out.println("Processando cartão: " + valor);
        } else if (tipo.equals("BOLETO")) {
            System.out.println("Gerando boleto: " + valor);
        } else if (tipo.equals("PIX")) {
            System.out.println("Processando PIX: " + valor);
        }
        //  Problema: Adicionar novo tipo requer modificar esta classe!
    }
}

//  DEPOIS: Extensão sem modificação

// Abstração que permite extensão
interface MetodoPagamento {
    void processar(double valor);
    String getDescricao();
}

// Novos tipos são adicionados como novas classes
class PagamentoCartaoCredito implements MetodoPagamento {
    private String numeroCartao;
    
    public PagamentoCartaoCredito(String numeroCartao) {
        this.numeroCartao = numeroCartao;
    }
    
    @Override
    public void processar(double valor) {
        System.out.println("Processando cartão " + mascararCartao() + " no valor de R$ " + valor);
        // Lógica específica de cartão
    }
    
    @Override
    public String getDescricao() {
        return "Cartão de Crédito";
    }
    
    private String mascararCartao() {
        return "****-****-****-" + numeroCartao.substring(numeroCartao.length() - 4);
    }
}

class PagamentoBoleto implements MetodoPagamento {
    @Override
    public void processar(double valor) {
        String codigoBarras = gerarCodigoBarras(valor);
        System.out.println("Boleto gerado: " + codigoBarras + " - Valor: R$ " + valor);
    }
    
    @Override
    public String getDescricao() {
        return "Boleto Bancário";
    }
    
    private String gerarCodigoBarras(double valor) {
        return "34191.79001 " + String.format("%.2f", valor).replace(".", "");
    }
}

class PagamentoPix implements MetodoPagamento {
    private String chavePix;
    
    public PagamentoPix(String chavePix) {
        this.chavePix = chavePix;
    }
    
    @Override
    public void processar(double valor) {
        System.out.println("PIX enviado para " + chavePix + " - Valor: R$ " + valor);
    }
    
    @Override
    public String getDescricao() {
        return "PIX";
    }
}

//  Novo tipo adicionado SEM modificar código existente
class PagamentoCripto implements MetodoPagamento {
    private String carteira;
    private String moeda;
    
    public PagamentoCripto(String carteira, String moeda) {
        this.carteira = carteira;
        this.moeda = moeda;
    }
    
    @Override
    public void processar(double valor) {
        System.out.println("Transferindo " + moeda + " para carteira " + carteira + " - Valor: R$ " + valor);
    }
    
    @Override
    public String getDescricao() {
        return "Criptomoeda " + moeda;
    }
}

// Processador que funciona com QUALQUER método de pagamento (extensível)
class ProcessadorPagamento {
    
    public void processar(MetodoPagamento metodo, double valor) {
        if (valor <= 0) {
            throw new IllegalArgumentException("Valor deve ser positivo");
        }
        metodo.processar(valor);
    }
    
    public void processarMultiplos(MetodoPagamento[] metodos, double[] valores) {
        if (metodos.length != valores.length) {
            throw new IllegalArgumentException("Arrays devem ter mesmo tamanho");
        }
        
        for (int i = 0; i < metodos.length; i++) {
            processar(metodos[i], valores[i]);
        }
    }
}
