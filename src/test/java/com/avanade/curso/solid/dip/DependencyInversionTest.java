package com.avanade.curso.solid.dip;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes para Dependency Inversion Principle
 */
class DependencyInversionTest {
    
    @Test
    @DisplayName("ServicoUsuario deve funcionar com MySQL")
    void servicoComMySQL() {
        Database mysql = new MySQLDatabaseImpl("localhost", "test_db");
        ServicoUsuario servico = new ServicoUsuario(mysql);
        
        assertDoesNotThrow(() -> {
            servico.salvarUsuario("João");
        });
    }
    
    @Test
    @DisplayName("ServicoUsuario deve funcionar com PostgreSQL")
    void servicoComPostgreSQL() {
        Database postgres = new PostgreSQLDatabaseImpl("localhost", "test_db");
        ServicoUsuario servico = new ServicoUsuario(postgres);
        
        assertDoesNotThrow(() -> {
            servico.salvarUsuario("Maria");
        });
    }
    
    @Test
    @DisplayName("ServicoUsuario deve funcionar com banco em memória (testes)")
    void servicoComInMemory() {
        InMemoryDatabase memoryDb = new InMemoryDatabase();
        ServicoUsuario servico = new ServicoUsuario(memoryDb);
        
        servico.salvarUsuario("Teste");
        
        //  Podemos verificar as queries executadas
        assertFalse(memoryDb.getQueries().isEmpty());
        assertTrue(memoryDb.getQueries().get(0).contains("INSERT"));
        assertTrue(memoryDb.getQueries().get(0).contains("Teste"));
    }
    
    @Test
    @DisplayName("NotificacaoService deve funcionar com SendGrid")
    void notificacaoComSendGrid() {
        ProvedorEmail sendGrid = new SendGridProvider("SG.teste123");
        NotificacaoService service = new NotificacaoService(sendGrid);
        
        assertDoesNotThrow(() -> {
            service.notificarBoasVindas("cliente@empresa.com");
        });
    }
    
    @Test
    @DisplayName("NotificacaoService deve funcionar com SMTP")
    void notificacaoComSMTP() {
        ProvedorEmail smtp = new SMTPEmailProvider("smtp.empresa.com", 587);
        NotificacaoService service = new NotificacaoService(smtp);
        
        assertDoesNotThrow(() -> {
            service.notificarPromocao("cliente@empresa.com", "50% OFF");
        });
    }
    
    @Test
    @DisplayName("NotificacaoService deve funcionar com fake email (testes)")
    void notificacaoComFakeEmail() {
        FakeEmailProvider fakeEmail = new FakeEmailProvider();
        NotificacaoService service = new NotificacaoService(fakeEmail);
        
        service.notificarBoasVindas("teste@teste.com");
        
        //  Podemos verificar se o email foi "enviado"
        assertEquals(1, fakeEmail.getEmailsEnviados().size());
        assertTrue(fakeEmail.foiEnviadoPara("teste@teste.com"));
    }
    
    @Test
    @DisplayName("Deve enviar múltiplas notificações e verificar no fake")
    void multiplasNotificacoes() {
        FakeEmailProvider fakeEmail = new FakeEmailProvider();
        NotificacaoService service = new NotificacaoService(fakeEmail);
        
        service.notificarBoasVindas("user1@teste.com");
        service.notificarBoasVindas("user2@teste.com");
        service.notificarPromocao("user1@teste.com", "Natal 2024");
        
        assertEquals(3, fakeEmail.getEmailsEnviados().size());
        assertTrue(fakeEmail.foiEnviadoPara("user1@teste.com"));
        assertTrue(fakeEmail.foiEnviadoPara("user2@teste.com"));
    }
}
