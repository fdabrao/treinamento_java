package com.avanade.curso.oop.composicao;

import java.util.ArrayList;
import java.util.List;

/**
 * COMPOSIÇÃO vs HERANÇA
 * 
 * "Favor composition over inheritance" - Design Patterns (GoF)
 * 
 * Este exemplo demonstra porque composição é geralmente preferível à herança:
 * 
 * PROBLEMAS DA HERANÇA:
 * 1. Herança é estática - não pode mudar em tempo de execução
 * 2. Acoplamento forte - mudanças na classe pai afetam todas as filhas
 * 3. Hierarquias profundas ficam complexas
 * 4. Dificuldade com herança múltipla
 * 5. Violação do encapsulamento - filhas dependem de detalhes da implementação do pai
 * 
 * VANTAGENS DA COMPOSIÇÃO:
 * 1. Flexibilidade - comportamentos podem ser trocados em tempo de execução
 * 2. Baixo acoplamento - componentes são independentes
 * 3. Reutilização sem hierarquia
 * 4. Testabilidade - fácil mockar componentes
 * 5. Cumpre SRP e DIP (princípios SOLID)
 */

// ============================================
//  ABORDAGEM RUIM: Usando Herança
// ============================================

/**
 * Problema: Queremos criar diferentes tipos de veículos.
 * Alguns vão na terra, alguns na água, alguns voam.
 * Com herança, ficamos presos em hierarquias rígidas.
 */

// Classe base
class VeiculoRuim {
    protected String modelo;
    protected double velocidade;
    
    public void mover() {
        System.out.println(modelo + " está se movendo");
    }
}

// Veículo terrestre
class CarroRuim extends VeiculoRuim {
    private int rodas = 4;
    
    @Override
    public void mover() {
        System.out.println(modelo + " está andando na estrada com " + rodas + " rodas");
    }
}

// Veículo aquático
class BarcoRuim extends VeiculoRuim {
    private double calado = 2.5;
    
    @Override
    public void mover() {
        System.out.println(modelo + " está navegando com calado de " + calado + "m");
    }
}

// PROBLEMA: E se quisermos um veículo anfíbio que anda e navega?
// Java não permite herança múltipla!
// Teríamos que duplicar código ou criar uma hierarquia complexa.
// class VeiculoAnfibioRuim extends CarroRuim { //  Não herda de BarcoRuim!
//     // Teríamos que copiar código do BarcoRuim
// }

// ============================================
//  ABORDAGEM BOA: Usando Composição
// ============================================

/**
 * Solução: Compor comportamentos através de interfaces.
 * Cada comportamento é uma estratégia que pode ser injetada.
 */

// Estratégias de movimento (comportamentos)
interface EstrategiaMovimento {
    void mover(String modelo);
}

// Movimento terrestre
class MovimentoTerrestre implements EstrategiaMovimento {
    private int rodas;
    private String terreno;
    
    public MovimentoTerrestre(int rodas, String terreno) {
        this.rodas = rodas;
        this.terreno = terreno;
    }
    
    @Override
    public void mover(String modelo) {
        System.out.println(modelo + " está andando no " + terreno + " com " + rodas + " rodas");
    }
}

// Movimento aquático
class MovimentoAquatico implements EstrategiaMovimento {
    private double calado;
    private String tipoCasco;
    
    public MovimentoAquatico(double calado, String tipoCasco) {
        this.calado = calado;
        this.tipoCasco = tipoCasco;
    }
    
    @Override
    public void mover(String modelo) {
        System.out.println(modelo + " está navegando com casco " + tipoCasco + " (calado: " + calado + "m)");
    }
}

// Movimento aéreo
class MovimentoAereo implements EstrategiaMovimento {
    private double altitudeMaxima;
    private String tipoAsa;
    
    public MovimentoAereo(double altitudeMaxima, String tipoAsa) {
        this.altitudeMaxima = altitudeMaxima;
        this.tipoAsa = tipoAsa;
    }
    
    @Override
    public void mover(String modelo) {
        System.out.println(modelo + " está voando com asas " + tipoAsa + " (altitude máx: " + altitudeMaxima + "m)");
    }
}

// Movimento combinado (anfíbio)
class MovimentoAnfibio implements EstrategiaMovimento {
    private EstrategiaMovimento terrestre;
    private EstrategiaMovimento aquatico;
    private boolean emTerra;
    
    public MovimentoAnfibio(EstrategiaMovimento terrestre, EstrategiaMovimento aquatico) {
        this.terrestre = terrestre;
        this.aquatico = aquatico;
        this.emTerra = true;
    }
    
    public void setEmTerra(boolean emTerra) {
        this.emTerra = emTerra;
    }
    
    @Override
    public void mover(String modelo) {
        if (emTerra) {
            terrestre.mover(modelo);
        } else {
            aquatico.mover(modelo);
        }
    }
}

// Veículo flexível usando composição
class Veiculo {
    private String modelo;
    private List<EstrategiaMovimento> estrategias = new ArrayList<>();
    
    public Veiculo(String modelo) {
        this.modelo = modelo;
    }
    
    // Adiciona comportamento dinamicamente
    public void adicionarEstrategia(EstrategiaMovimento estrategia) {
        estrategias.add(estrategia);
    }
    
    // Remove comportamento dinamicamente
    public void removerEstrategia(EstrategiaMovimento estrategia) {
        estrategias.remove(estrategia);
    }
    
    // Trocar estrategia em tempo de execução
    public void substituirEstrategia(EstrategiaMovimento antiga, EstrategiaMovimento nova) {
        int index = estrategias.indexOf(antiga);
        if (index >= 0) {
            estrategias.set(index, nova);
        }
    }
    
    public void mover() {
        if (estrategias.isEmpty()) {
            System.out.println(modelo + " não pode se mover (sem estratégia)");
        } else {
            for (EstrategiaMovimento estrategia : estrategias) {
                estrategia.mover(modelo);
            }
        }
    }
    
    public String getModelo() {
        return modelo;
    }
}

// ============================================
// 🎯 EXEMPLO PRÁTICO: Sistema de Notificações
// ============================================

/**
 * Demonstração real: Sistema de envio de notificações
 * Mostrando como composição permite combinar comportamentos facilmente
 */

// Comportamentos de envio
interface Sender {
    void enviar(String mensagem, String destinatario);
}

class EmailSender implements Sender {
    private String servidorSmtp;
    
    public EmailSender(String servidorSmtp) {
        this.servidorSmtp = servidorSmtp;
    }
    
    @Override
    public void enviar(String mensagem, String destinatario) {
        System.out.println("[EMAIL via " + servidorSmtp + "] Para: " + destinatario + " | " + mensagem);
    }
}

class SmsSender implements Sender {
    private String provedor;
    
    public SmsSender(String provedor) {
        this.provedor = provedor;
    }
    
    @Override
    public void enviar(String mensagem, String destinatario) {
        System.out.println("[SMS via " + provedor + "] Para: " + destinatario + " | " + mensagem);
    }
}

class PushSender implements Sender {
    private String plataforma;
    
    public PushSender(String plataforma) {
        this.plataforma = plataforma;
    }
    
    @Override
    public void enviar(String mensagem, String destinatario) {
        System.out.println("[PUSH via " + plataforma + "] Para: " + destinatario + " | " + mensagem);
    }
}

// Comportamentos de formatação
interface Formatter {
    String formatar(String mensagem);
}

class HtmlFormatter implements Formatter {
    @Override
    public String formatar(String mensagem) {
        return "<html><body><h1>" + mensagem + "</h1></body></html>";
    }
}

class MarkdownFormatter implements Formatter {
    @Override
    public String formatar(String mensagem) {
        return "# " + mensagem + "\n\n*Enviado via sistema*";
    }
}

class JsonFormatter implements Formatter {
    @Override
    public String formatar(String mensagem) {
        return "{\"mensagem\": \"" + mensagem + "\", \"tipo\": \"notificacao\"}";
    }
}

// Sistema de notificação usando composição
class NotificationService {
    private List<Sender> senders = new ArrayList<>();
    private Formatter formatter;
    
    public NotificationService(Formatter formatter) {
        this.formatter = formatter;
    }
    
    // Adicionar canais de envio dinamicamente
    public void addSender(Sender sender) {
        senders.add(sender);
    }
    
    // Trocar formatador em tempo de execução
    public void setFormatter(Formatter formatter) {
        this.formatter = formatter;
    }
    
    public void notificar(String mensagem, String destinatario) {
        String formatada = formatter.formatar(mensagem);
        
        for (Sender sender : senders) {
            sender.enviar(formatada, destinatario);
        }
    }
}

// ============================================
//  Comparação de Testabilidade
// ============================================

/**
 * Com composição, testar é muito mais fácil porque podemos
 * injetar mocks/fakes dos componentes.
 */

// Componente fake para testes
class FakeSender implements Sender {
    private List<String> mensagensEnviadas = new ArrayList<>();
    
    @Override
    public void enviar(String mensagem, String destinatario) {
        mensagensEnviadas.add("[" + destinatario + "] " + mensagem);
    }
    
    public List<String> getMensagens() {
        return mensagensEnviadas;
    }
    
    public boolean foiEnviado(String destinatario) {
        return mensagensEnviadas.stream()
            .anyMatch(m -> m.contains("[" + destinatario + "]"));
    }
}

// ============================================
//  Classe de Demonstração
// ============================================

public class ComposicaoVsHerancaExample {
    
    public static void demonstrar() {
        System.out.println("===  HERANÇA: Problemas ===\n");
        
        VeiculoRuim carro = new CarroRuim();
        carro.modelo = "Sedan";
        carro.mover();
        
        VeiculoRuim barco = new BarcoRuim();
        barco.modelo = "Iate";
        barco.mover();
        
        //  Não consigo criar um anfíbio sem duplicar código!
        
        System.out.println("\n===  COMPOSIÇÃO: Solução ===\n");
        
        // Veículo terrestre simples
        Veiculo carroComposicao = new Veiculo("Sedan 2024");
        carroComposicao.adicionarEstrategia(new MovimentoTerrestre(4, "asfalto"));
        carroComposicao.mover();
        
        System.out.println();
        
        // Veículo aquático
        Veiculo barcoComposicao = new Veiculo("Iate Luxo");
        barcoComposicao.adicionarEstrategia(new MovimentoAquatico(3.5, "fibra de vidro"));
        barcoComposicao.mover();
        
        System.out.println();
        
        //  Veículo anfíbio - combinando comportamentos!
        Veiculo anfibio = new Veiculo("Tanque Anfíbio");
        MovimentoAnfibio movimentoAnfibio = new MovimentoAnfibio(
            new MovimentoTerrestre(8, "terreno irregular"),
            new MovimentoAquatico(1.5, "blindado")
        );
        anfibio.adicionarEstrategia(movimentoAnfibio);
        
        System.out.println("Modo terrestre:");
        movimentoAnfibio.setEmTerra(true);
        anfibio.mover();
        
        System.out.println("Modo aquático:");
        movimentoAnfibio.setEmTerra(false);
        anfibio.mover();
        
        System.out.println();
        
        // Veículo voador
        Veiculo aviao = new Veiculo("Boeing 747");
        aviao.adicionarEstrategia(new MovimentoAereo(12000, "fixa"));
        aviao.mover();
        
        System.out.println();
        
        //  Trocando comportamento em tempo de execução!
        System.out.println("=== Trocando comportamento dinamicamente ===");
        Veiculo veiculoTransformavel = new Veiculo("Transformável");
        
        EstrategiaMovimento terrestre = new MovimentoTerrestre(4, "terra");
        veiculoTransformavel.adicionarEstrategia(terrestre);
        System.out.println("Original:");
        veiculoTransformavel.mover();
        
        // Troca para aquático!
        veiculoTransformavel.substituirEstrategia(terrestre, new MovimentoAquatico(2.0, "metal"));
        System.out.println("Após transformação:");
        veiculoTransformavel.mover();
        
        System.out.println("\n===  Sistema de Notificações ===\n");
        
        // Notificação apenas email
        NotificationService emailService = new NotificationService(new HtmlFormatter());
        emailService.addSender(new EmailSender("smtp.gmail.com"));
        emailService.notificar("Bem-vindo!", "usuario@email.com");
        
        System.out.println();
        
        // Notificação multi-canal
        NotificationService multiService = new NotificationService(new MarkdownFormatter());
        multiService.addSender(new EmailSender("smtp.empresa.com"));
        multiService.addSender(new SmsSender("Twilio"));
        multiService.addSender(new PushSender("Firebase"));
        multiService.notificar("Alerta importante!", "cliente@empresa.com");
        
        System.out.println();
        
        // Trocando formatador dinamicamente
        System.out.println("Trocando para JSON:");
        multiService.setFormatter(new JsonFormatter());
        multiService.notificar("Dados atualizados", "api@empresa.com");
    }
    
    public static void main(String[] args) {
        demonstrar();
    }
}
