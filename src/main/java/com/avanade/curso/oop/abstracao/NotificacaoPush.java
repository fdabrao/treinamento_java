package com.avanade.curso.oop.abstracao;

/**
 * Implementação concreta: Notificação Push
 */
public class NotificacaoPush extends ServicoNotificacao {
    
    private String firebaseKey;
    private String appId;
    private boolean enviarSilencioso;
    
    public NotificacaoPush() {
        super("Push Notification");
    }
    
    @Override
    public void configurar(java.util.Map<String, String> configuracoes) {
        this.firebaseKey = configuracoes.get("firebaseKey");
        this.appId = configuracoes.getOrDefault("appId", "com.exemplo.app");
        this.enviarSilencioso = Boolean.parseBoolean(
            configuracoes.getOrDefault("silencioso", "false")
        );
    }
    
    @Override
    public boolean enviar(String deviceToken, String mensagem) {
        System.out.printf("Enviando push para app %s (device: %.8s...)%n", 
                         appId, deviceToken);
        
        if (enviarSilencioso) {
            System.out.println("Modo silencioso ativado");
        }
        
        // Simula envio via FCM
        if (firebaseKey == null) {
            System.err.println("Firebase key não configurada");
            return false;
        }
        
        return true;
    }
    
    @Override
    protected boolean validarDestinatario(String deviceToken) {
        // Token FCM tem ~152 caracteres alfanuméricos
        return deviceToken != null && deviceToken.length() >= 100;
    }
    
    public boolean isEnviarSilencioso() {
        return enviarSilencioso;
    }
    
    public void setEnviarSilencioso(boolean silencioso) {
        this.enviarSilencioso = silencioso;
    }
}
