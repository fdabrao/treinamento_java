package com.avanade.curso.oop.abstracao;

/**
 * Implementação concreta: Serviço de SMS
 */
public class NotificacaoSms extends ServicoNotificacao {
    
    private String apiKey;
    private String provedor;
    private int limiteCaracteres;
    
    public NotificacaoSms() {
        super("SMS");
        this.limiteCaracteres = 160; // Limite padrão de um SMS
    }
    
    @Override
    public void configurar(java.util.Map<String, String> configuracoes) {
        this.apiKey = configuracoes.get("apiKey");
        this.provedor = configuracoes.getOrDefault("provedor", "Twilio");
        if (configuracoes.containsKey("limiteCaracteres")) {
            this.limiteCaracteres = Integer.parseInt(configuracoes.get("limiteCaracteres"));
        }
    }
    
    @Override
    public boolean enviar(String destinatario, String mensagem) {
        System.out.printf("Enviando SMS via %s para %s%n", provedor, destinatario);
        System.out.printf("Mensagem: %s%n", mensagem);
        
        // Simula chamada API
        if (apiKey == null || apiKey.isEmpty()) {
            System.err.println("API Key não configurada");
            return false;
        }
        
        return true;
    }
    
    @Override
    protected boolean validarDestinatario(String destinatario) {
        // Remove caracteres não numéricos e valida
        String numeros = destinatario.replaceAll("\\D", "");
        // Brasil: DDD (2 dígitos) + número (8-9 dígitos)
        return numeros.length() >= 10 && numeros.length() <= 11;
    }
    
    @Override
    protected String preProcessarMensagem(String mensagem) {
        String limpa = super.preProcessarMensagem(mensagem);
        // Trunca se exceder limite
        if (limpa.length() > limiteCaracteres) {
            limpa = limpa.substring(0, limiteCaracteres - 3) + "...";
        }
        return limpa;
    }
    
    public int getLimiteCaracteres() {
        return limiteCaracteres;
    }
}
