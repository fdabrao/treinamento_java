package com.avanade.curso.oop.abstracao;

/**
 * ABSTRAÇÃO
 * 
 * A abstração é o processo de identificar características essenciais de um objeto
 * e ignorar detalhes irrelevantes. Em OOP, usamos classes abstratas e interfaces
 * para definir "contratos" sem se preocupar com implementação.
 * 
 * Benefícios:
 * - Simplifica modelagem de sistemas complexos
 * - Separa o "o que" do "como"
 * - Facilita manutenção e evolução
 * - Reduz acoplamento
 * 
 * Exemplo: Sistema de Notificações
 * - O usuário quer "notificar" (o quê)
 * - Não importa como (email, SMS, push) - isso é detalhe de implementação
 */
public abstract class ServicoNotificacao {
    
    protected boolean ativo;
    protected String nomeServico;
    
    public ServicoNotificacao(String nomeServico) {
        this.nomeServico = nomeServico;
        this.ativo = true;
    }
    
    /**
     * MÉTODO ABSTRATO - deve ser implementado pelas subclasses
     * Define O QUÊ o serviço faz, não COMO
     */
    public abstract boolean enviar(String destinatario, String mensagem);
    
    /**
     * MÉTODO ABSTRATO - configuração específica de cada serviço
     */
    public abstract void configurar(java.util.Map<String, String> configuracoes);
    
    /**
     * MÉTODO CONCRETO - comportamento comum a todos os serviços
     */
    public boolean isAtivo() {
        return ativo;
    }
    
    public void ativar() {
        this.ativo = true;
    }
    
    public void desativar() {
        this.ativo = false;
    }
    
    /**
     * Método template - define o fluxo padrão, subclasses customizam passos
     */
    public final boolean notificar(String destinatario, String mensagem) {
        // 1. Validações comuns
        if (!ativo) {
            System.err.println("Serviço " + nomeServico + " está inativo");
            return false;
        }
        
        if (!validarDestinatario(destinatario)) {
            System.err.println("Destinatário inválido: " + destinatario);
            return false;
        }
        
        if (mensagem == null || mensagem.trim().isEmpty()) {
            System.err.println("Mensagem não pode ser vazia");
            return false;
        }
        
        // 2. Pré-processamento
        String mensagemProcessada = preProcessarMensagem(mensagem);
        
        // 3. Envio (específico de cada implementação)
        boolean enviado = enviar(destinatario, mensagemProcessada);
        
        // 4. Pós-processamento comum
        if (enviado) {
            registrarEnvio(destinatario, mensagemProcessada);
        }
        
        return enviado;
    }
    
    /**
     * Hook method - pode ser sobrescrito mas tem implementação padrão
     */
    protected String preProcessarMensagem(String mensagem) {
        return mensagem.trim();
    }
    
    /**
     * Hook method - cada serviço valida seu tipo de destinatário
     */
    protected abstract boolean validarDestinatario(String destinatario);
    
    /**
     * Método privado - detalhe de implementação escondido
     */
    private void registrarEnvio(String destinatario, String mensagem) {
        System.out.printf("[LOG] %s: Enviado para %s (%d caracteres)%n", 
                         nomeServico, destinatario, mensagem.length());
    }
    
    public String getNomeServico() {
        return nomeServico;
    }
}
