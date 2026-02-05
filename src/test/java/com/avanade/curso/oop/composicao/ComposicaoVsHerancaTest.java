package com.avanade.curso.oop.composicao;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes demonstrando vantagens da Composição sobre Herança
 */
class ComposicaoVsHerancaTest {
    
    // ============================================
    // Testes da Abordagem com Herança
    // ============================================
    
    @Test
    @DisplayName("Carro deve se mover na estrada")
    void carroDeveMover() {
        CarroRuim carro = new CarroRuim();
        carro.modelo = "Sedan";
        
        // Teste simples - apenas verifica se não lança exceção
        assertDoesNotThrow(carro::mover);
    }
    
    @Test
    @DisplayName("Barco deve navegar")
    void barcoDeveNavegar() {
        BarcoRuim barco = new BarcoRuim();
        barco.modelo = "Iate";
        
        assertDoesNotThrow(barco::mover);
    }
    
    // ============================================
    // Testes da Abordagem com Composição
    // ============================================
    
    @Test
    @DisplayName("Veículo terrestre deve andar")
    void veiculoTerrestre() {
        Veiculo carro = new Veiculo("Sedan");
        carro.adicionarEstrategia(new MovimentoTerrestre(4, "asfalto"));
        
        assertDoesNotThrow(carro::mover);
    }
    
    @Test
    @DisplayName("Veículo aquático deve navegar")
    void veiculoAquatico() {
        Veiculo barco = new Veiculo("Iate");
        barco.adicionarEstrategia(new MovimentoAquatico(3.5, "fibra"));
        
        assertDoesNotThrow(barco::mover);
    }
    
    @Test
    @DisplayName("Veículo aéreo deve voar")
    void veiculoAereo() {
        Veiculo aviao = new Veiculo("Boeing");
        aviao.adicionarEstrategia(new MovimentoAereo(12000, "fixa"));
        
        assertDoesNotThrow(aviao::mover);
    }
    
    @Test
    @DisplayName("Veículo anfíbio deve funcionar em ambos os modos")
    void veiculoAnfibio() {
        Veiculo anfibio = new Veiculo("Tanque");
        MovimentoAnfibio movimento = new MovimentoAnfibio(
            new MovimentoTerrestre(8, "terra"),
            new MovimentoAquatico(1.5, "blindado")
        );
        anfibio.adicionarEstrategia(movimento);
        
        // Modo terrestre
        movimento.setEmTerra(true);
        assertDoesNotThrow(anfibio::mover);
        
        // Modo aquático
        movimento.setEmTerra(false);
        assertDoesNotThrow(anfibio::mover);
    }
    
    @Test
    @DisplayName("Veículo sem estratégia deve informar que não pode mover")
    void veiculoSemEstrategia() {
        Veiculo veiculo = new Veiculo("Inerte");
        
        assertDoesNotThrow(veiculo::mover);
    }
    
    @Test
    @DisplayName("Deve adicionar estratégia dinamicamente")
    void adicionarEstrategiaDinamicamente() {
        Veiculo veiculo = new Veiculo("Transformável");
        
        veiculo.adicionarEstrategia(new MovimentoTerrestre(4, "terra"));
        assertDoesNotThrow(veiculo::mover);
        
        veiculo.adicionarEstrategia(new MovimentoAquatico(2.0, "metal"));
        // Agora pode fazer ambos
        assertDoesNotThrow(veiculo::mover);
    }
    
    @Test
    @DisplayName("Deve remover estratégia dinamicamente")
    void removerEstrategiaDinamicamente() {
        Veiculo veiculo = new Veiculo("Reduzível");
        EstrategiaMovimento terrestre = new MovimentoTerrestre(4, "asfalto");
        
        veiculo.adicionarEstrategia(terrestre);
        veiculo.adicionarEstrategia(new MovimentoAquatico(2.0, "metal"));
        
        // Remove estratégia terrestre
        veiculo.removerEstrategia(terrestre);
        
        assertDoesNotThrow(veiculo::mover);
    }
    
    @Test
    @DisplayName("Deve substituir estratégia em tempo de execução")
    void substituirEstrategia() {
        Veiculo veiculo = new Veiculo("Transformável");
        EstrategiaMovimento terrestre = new MovimentoTerrestre(4, "terra");
        
        veiculo.adicionarEstrategia(terrestre);
        assertDoesNotThrow(veiculo::mover);
        
        // Troca por estratégia aquática
        veiculo.substituirEstrategia(terrestre, new MovimentoAquatico(2.0, "metal"));
        assertDoesNotThrow(veiculo::mover);
    }
    
    // ============================================
    // Testes do Sistema de Notificações
    // ============================================
    
    @Test
    @DisplayName("Serviço de notificação com um sender")
    void notificacaoComUmSender() {
        FakeSender fakeSender = new FakeSender();
        NotificationService service = new NotificationService(new HtmlFormatter());
        service.addSender(fakeSender);
        
        service.notificar("Teste", "user@email.com");
        
        assertTrue(fakeSender.foiEnviado("user@email.com"));
        assertEquals(1, fakeSender.getMensagens().size());
    }
    
    @Test
    @DisplayName("Serviço de notificação com múltiplos senders")
    void notificacaoComMultiplosSenders() {
        FakeSender emailSender = new FakeSender();
        FakeSender smsSender = new FakeSender();
        
        NotificationService service = new NotificationService(new MarkdownFormatter());
        service.addSender(emailSender);
        service.addSender(smsSender);
        
        service.notificar("Alerta", "cliente@empresa.com");
        
        assertEquals(1, emailSender.getMensagens().size());
        assertEquals(1, smsSender.getMensagens().size());
    }
    
    @Test
    @DisplayName("Deve trocar formatador dinamicamente")
    void trocarFormatadorDinamicamente() {
        FakeSender sender = new FakeSender();
        NotificationService service = new NotificationService(new HtmlFormatter());
        service.addSender(sender);
        
        service.notificar("Teste HTML", "user@email.com");
        String mensagemHtml = sender.getMensagens().get(0);
        assertTrue(mensagemHtml.contains("<html>"));
        
        // Troca para JSON
        service.setFormatter(new JsonFormatter());
        sender.getMensagens().clear();
        
        service.notificar("Teste JSON", "user@email.com");
        String mensagemJson = sender.getMensagens().get(0);
        assertTrue(mensagemJson.contains("{"));
    }
    
    @Test
    @DisplayName("Deve adicionar sender dinamicamente")
    void adicionarSenderDinamicamente() {
        FakeSender emailSender = new FakeSender();
        FakeSender pushSender = new FakeSender();
        
        NotificationService service = new NotificationService(new HtmlFormatter());
        service.addSender(emailSender);
        
        service.notificar("Primeira", "user@email.com");
        assertEquals(1, emailSender.getMensagens().size());
        assertEquals(0, pushSender.getMensagens().size());
        
        // Adiciona push depois
        service.addSender(pushSender);
        service.notificar("Segunda", "user@email.com");
        
        assertEquals(2, emailSender.getMensagens().size());
        assertEquals(1, pushSender.getMensagens().size());
    }
    
    // ============================================
    // Testes de comparação
    // ============================================
    
    @Test
    @DisplayName("Composição permite criar anfíbio sem duplicar código")
    void composicaoPermiteAnfibio() {
        // Com herança, isso seria muito difícil ou impossível
        // Com composição, é simples:
        
        Veiculo anfibio = new Veiculo("Veículo Anfíbio");
        anfibio.adicionarEstrategia(new MovimentoTerrestre(6, "mistos"));
        anfibio.adicionarEstrategia(new MovimentoAquatico(1.0, "híbrido"));
        
        // Ambos os comportamentos são executados
        assertDoesNotThrow(anfibio::mover);
    }
    
    @Test
    @DisplayName("Composição permite testar componentes isoladamente")
    void componentesTestaveisIsoladamente() {
        // Podemos testar apenas o MovimentoTerrestre
        MovimentoTerrestre movimento = new MovimentoTerrestre(4, "asfalto");
        assertDoesNotThrow(() -> movimento.mover("Carro Teste"));
        
        // Podemos mockar o comportamento em testes
        FakeSender mockSender = new FakeSender();
        NotificationService service = new NotificationService(new HtmlFormatter());
        service.addSender(mockSender);
        
        service.notificar("Teste", "user@test.com");
        
        assertTrue(mockSender.foiEnviado("user@test.com"));
    }
    
    @Test
    @DisplayName("Veículo pode ter múltiplos comportamentos simultâneos")
    void multiplosComportamentosSimultaneos() {
        Veiculo veiculo = new Veiculo("Super Veículo");
        veiculo.adicionarEstrategia(new MovimentoTerrestre(4, "estrada"));
        veiculo.adicionarEstrategia(new MovimentoAquatico(2.0, "naval"));
        veiculo.adicionarEstrategia(new MovimentoAereo(5000, "dobra"));
        
        // Executa todos os movimentos
        assertDoesNotThrow(veiculo::mover);
    }
}
