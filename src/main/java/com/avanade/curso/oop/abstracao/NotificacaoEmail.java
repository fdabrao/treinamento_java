package com.avanade.curso.oop.abstracao;

/**
 * Implementação concreta: Serviço de Email
 * 
 * Implementa os métodos abstratos definidos na classe pai,
 * fornecendo o "como" específico para notificações por email.
 */
public class NotificacaoEmail extends ServicoNotificacao {
    
    private String servidorSmtp;
    private int porta;
    private String remetentePadrao;
    private boolean usarTls;
    
    public NotificacaoEmail() {
        super("Email");
    }
    
    @Override
    public void configurar(java.util.Map<String, String> configuracoes) {
        this.servidorSmtp = configuracoes.getOrDefault("smtp", "smtp.gmail.com");
        this.porta = Integer.parseInt(configuracoes.getOrDefault("porta", "587"));
        this.remetentePadrao = configuracoes.getOrDefault("remetente", "noreply@exemplo.com");
        this.usarTls = Boolean.parseBoolean(configuracoes.getOrDefault("tls", "true"));
    }
    
    @Override
    public boolean enviar(String destinatario, String mensagem) {
        // Simula envio de email
        System.out.printf("Conectando a %s:%d (TLS=%b)%n", servidorSmtp, porta, usarTls);
        System.out.printf("De: %s%nPara: %s%nMensagem: %s%n", 
                         remetentePadrao, destinatario, mensagem.substring(0, Math.min(50, mensagem.length())) + "...");
        
        // Aqui iria a lógica real de envio via JavaMail ou similar
        return true;
    }
    
    @Override
    protected boolean validarDestinatario(String destinatario) {
        return destinatario != null && 
               destinatario.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }
    
    @Override
    protected String preProcessarMensagem(String mensagem) {
        // Email pode adicionar assinatura
        return super.preProcessarMensagem(mensagem) + 
               "\n\n--\nEnviado via Sistema de Notificações";
    }
}
