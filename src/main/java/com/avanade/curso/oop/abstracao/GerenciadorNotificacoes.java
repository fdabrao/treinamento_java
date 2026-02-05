package com.avanade.curso.oop.abstracao;

/**
 * Fachada (Facade) que simplifica o uso do sistema de notificações
 * 
 * Demonstra como a abstração esconde complexidade do usuário final.
 * O usuário só precisa chamar "notificar()", não importa qual serviço está sendo usado.
 */
public class GerenciadorNotificacoes {
    
    private final java.util.List<ServicoNotificacao> servicos;
    
    public GerenciadorNotificacoes() {
        this.servicos = new java.util.ArrayList<>();
    }
    
    public void adicionarServico(ServicoNotificacao servico) {
        servicos.add(servico);
    }
    
    /**
     * Método de alto nível - usuário não precisa saber qual serviço será usado
     * Pode escolher o canal ou deixar o sistema decidir
     */
    public boolean notificar(String destinatario, String mensagem, Canal canal) {
        ServicoNotificacao servico = encontrarServico(canal);
        if (servico == null) {
            System.err.println("Canal não disponível: " + canal);
            return false;
        }
        return servico.notificar(destinatario, mensagem);
    }
    
    /**
     * Notificação multi-canal: tenta vários canais até conseguir
     */
    public boolean notificarMultiCanal(String destinatario, String mensagem, Canal... canais) {
        for (Canal canal : canais) {
            if (notificar(destinatario, mensagem, canal)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Notificação broadcast: envia para todos os canais ativos
     */
    public int notificarTodos(String destinatario, String mensagem) {
        int sucessos = 0;
        for (ServicoNotificacao servico : servicos) {
            if (servico.isAtivo() && servico.notificar(destinatario, mensagem)) {
                sucessos++;
            }
        }
        return sucessos;
    }
    
    private ServicoNotificacao encontrarServico(Canal canal) {
        return servicos.stream()
            .filter(s -> s.isAtivo() && matchesCanal(s, canal))
            .findFirst()
            .orElse(null);
    }
    
    private boolean matchesCanal(ServicoNotificacao servico, Canal canal) {
        return switch (canal) {
            case EMAIL -> servico instanceof NotificacaoEmail;
            case SMS -> servico instanceof NotificacaoSms;
            case PUSH -> servico instanceof NotificacaoPush;
            case QUALQUER -> true;
        };
    }
    
    public enum Canal {
        EMAIL, SMS, PUSH, QUALQUER
    }
    
    public java.util.List<ServicoNotificacao> getServicosAtivos() {
        return servicos.stream()
            .filter(ServicoNotificacao::isAtivo)
            .toList();
    }
}
