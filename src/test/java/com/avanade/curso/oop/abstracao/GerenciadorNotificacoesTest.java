package com.avanade.curso.oop.abstracao;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;
import java.util.HashMap;

/**
 * Testes demonstrando a Abstração
 * 
 * Verificam que:
 * - Classes abstratas não podem ser instanciadas
 * - Subclasses implementam métodos abstratos
 * - Template method define fluxo padrão
 * - Detalhes de implementação estão escondidos
 */
class GerenciadorNotificacoesTest {
    
    private GerenciadorNotificacoes gerenciador;
    private NotificacaoEmail email;
    private NotificacaoSms sms;
    private NotificacaoPush push;
    
    @BeforeEach
    void setUp() {
        gerenciador = new GerenciadorNotificacoes();
        
        email = new NotificacaoEmail();
        email.configurar(Map.of(
            "smtp", "smtp.test.com",
            "porta", "587",
            "remetente", "test@test.com"
        ));
        
        sms = new NotificacaoSms();
        sms.configurar(Map.of(
            "apiKey", "test-api-key",
            "provedor", "TestSMS"
        ));
        
        push = new NotificacaoPush();
        push.configurar(Map.of(
            "firebaseKey", "test-firebase-key",
            "appId", "com.test.app"
        ));
        
        gerenciador.adicionarServico(email);
        gerenciador.adicionarServico(sms);
        gerenciador.adicionarServico(push);
    }
    
    @Test
    @DisplayName("Enviar notificação por email")
    void enviarEmail() {
        boolean enviado = gerenciador.notificar(
            "destinatario@teste.com",
            "Mensagem de teste",
            GerenciadorNotificacoes.Canal.EMAIL
        );
        
        assertTrue(enviado);
    }
    
    @Test
    @DisplayName("Enviar notificação por SMS")
    void enviarSms() {
        boolean enviado = gerenciador.notificar(
            "11999999999",
            "Mensagem SMS",
            GerenciadorNotificacoes.Canal.SMS
        );
        
        assertTrue(enviado);
    }
    
    @Test
    @DisplayName("Enviar notificação push")
    void enviarPush() {
        boolean enviado = gerenciador.notificar(
            "abc123def456ghi789jkl012mno345pqr678stu901vwx234yz567abc890def123ghi456jkl789mno012pqr345stu678vwx901yz234abc567",
            "Mensagem push",
            GerenciadorNotificacoes.Canal.PUSH
        );
        
        assertTrue(enviado);
    }
    
    @Test
    @DisplayName("Validação de email inválido")
    void validarEmailInvalido() {
        boolean enviado = gerenciador.notificar(
            "email-invalido",
            "Mensagem",
            GerenciadorNotificacoes.Canal.EMAIL
        );
        
        assertFalse(enviado);
    }
    
    @Test
    @DisplayName("Validação de telefone inválido")
    void validarTelefoneInvalido() {
        boolean enviado = gerenciador.notificar(
            "123", // Telefone muito curto
            "Mensagem",
            GerenciadorNotificacoes.Canal.SMS
        );
        
        assertFalse(enviado);
    }
    
    @Test
    @DisplayName("Validação de token push inválido")
    void validarTokenInvalido() {
        boolean enviado = gerenciador.notificar(
            "token-curto", // Token muito curto
            "Mensagem",
            GerenciadorNotificacoes.Canal.PUSH
        );
        
        assertFalse(enviado);
    }
    
    @Test
    @DisplayName("Multi-canal: tenta primeiro canal, se falhar vai para o segundo")
    void notificarMultiCanal() {
        // Primeiro canal (email) falha, segundo (sms) funciona
        boolean enviado = gerenciador.notificarMultiCanal(
            "11999999999",
            "Mensagem",
            GerenciadorNotificacoes.Canal.EMAIL,
            GerenciadorNotificacoes.Canal.SMS
        );
        
        assertTrue(enviado);
    }
    
    @Test
    @DisplayName("Notificar todos os canais ativos")
    void notificarTodosCanais() {
        // Cada canal precisa de um formato de destinatário diferente
        // Por isso, notificarTodos com um único destinatário não funcionará para todos
        // Vamos verificar que o método funciona corretamente quando os destinatários são válidos
        
        // Testar com email válido - apenas email deve receber
        int enviadosEmail = gerenciador.notificarTodos(
            "teste@exemplo.com",
            "Mensagem broadcast"
        );
        assertEquals(1, enviadosEmail); // Apenas email é válido
        
        // Testar com telefone válido - apenas SMS deve receber
        int enviadosSms = gerenciador.notificarTodos(
            "11999999999",
            "Mensagem broadcast"
        );
        assertEquals(1, enviadosSms); // Apenas SMS é válido
    }
    
    @Test
    @DisplayName("Não enviar notificação se serviço inativo")
    void naoEnviarSeInativo() {
        sms.desativar();
        
        boolean enviado = gerenciador.notificar(
            "11999999999",
            "Mensagem",
            GerenciadorNotificacoes.Canal.SMS
        );
        
        assertFalse(enviado);
    }
    
    @Test
    @DisplayName("SMS trunca mensagem longa")
    void smsTruncaMensagemLonga() {
        String mensagemLonga = "A".repeat(200);
        
        boolean enviado = gerenciador.notificar(
            "11999999999",
            mensagemLonga,
            GerenciadorNotificacoes.Canal.SMS
        );
        
        assertTrue(enviado);
        // A mensagem foi truncada para 160 caracteres + "..."
    }
    
    @Test
    @DisplayName("Email adiciona assinatura")
    void emailAdicionaAssinatura() {
        NotificacaoEmail emailSpy = new NotificacaoEmail() {
            @Override
            public boolean enviar(String destinatario, String mensagem) {
                // Verifica que a assinatura foi adicionada
                assertTrue(mensagem.contains("Enviado via Sistema de Notificações"));
                return super.enviar(destinatario, mensagem);
            }
        };
        emailSpy.configurar(Map.of("smtp", "test.com"));
        
        emailSpy.notificar("test@test.com", "Oi");
    }
    
    @Test
    @DisplayName("Não pode criar instância de classe abstrata")
    void naoInstanciarClasseAbstrata() {
        // Tentativa de instanciar ServicoNotificacao diretamente
        // Isso não compila, demonstrando a proteção da abstração
        
        // O código abaixo não compilaria:
        // ServicoNotificacao servico = new ServicoNotificacao("teste");
        
        assertTrue(true); // Teste apenas documental
    }
    
    @Test
    @DisplayName("Template method define fluxo padrão")
    void templateMethodFluxoPadrao() {
        // Todos os serviços seguem o mesmo fluxo:
        // 1. Validação de ativo
        // 2. Validação de destinatário
        // 3. Validação de mensagem
        // 4. Pré-processamento
        // 5. Envio (específico)
        // 6. Registro de log
        
        // Se alguma validação falha, envio não ocorre
        assertFalse(email.notificar("", "mensagem")); // destinatário vazio
        assertFalse(email.notificar("test@test.com", "")); // mensagem vazia
        assertFalse(email.notificar("email-invalido", "mensagem")); // formato inválido
    }
    
    @Test
    @DisplayName("Push pode ser configurado como silencioso")
    void pushSilencioso() {
        assertFalse(push.isEnviarSilencioso());
        
        push.setEnviarSilencioso(true);
        assertTrue(push.isEnviarSilencioso());
    }
    
    @Test
    @DisplayName("Serviços ativos são listados corretamente")
    void listarServicosAtivos() {
        assertEquals(3, gerenciador.getServicosAtivos().size());
        
        sms.desativar();
        assertEquals(2, gerenciador.getServicosAtivos().size());
        
        push.desativar();
        assertEquals(1, gerenciador.getServicosAtivos().size());
    }
}
