package com.avanade.curso.solid.dip;

/**
 * D - Dependency Inversion Principle (Princípio da Inversão de Dependência)
 * 
 * Módulos de alto nível não devem depender de módulos de baixo nível.
 * Ambos devem depender de abstrações.
 * Abstrações não devem depender de detalhes. Detalhes devem depender de abstrações.
 * 
 * ANTES (errado): Classe de alto nível depende diretamente de implementações concretas
 * DEPOIS (correto): Depende de abstrações (interfaces)
 */

//  ANTES: Serviço de alto nível depende diretamente de implementações
class MySQLDatabase {
    public void connect() {
        System.out.println("Conectando ao MySQL...");
    }
    
    public void query(String sql) {
        System.out.println("Executando query MySQL: " + sql);
    }
    
    public void close() {
        System.out.println("Fechando conexão MySQL");
    }
}

class ServicoUsuarioErrado {
    private MySQLDatabase database; //  Acoplamento direto!
    
    public ServicoUsuarioErrado() {
        this.database = new MySQLDatabase(); //  Criação direta = difícil testar
    }
    
    public void salvarUsuario(String nome) {
        database.connect();
        database.query("INSERT INTO usuarios VALUES ('" + nome + "')");
        database.close();
    }
}

//  DEPOIS: Depende de abstrações

// Abstração que define o contrato
interface Database {
    void connect();
    void query(String sql);
    void close();
}

// Implementações concretas dependem da abstração
class MySQLDatabaseImpl implements Database {
    private String host;
    private String database;
    
    public MySQLDatabaseImpl(String host, String database) {
        this.host = host;
        this.database = database;
    }
    
    @Override
    public void connect() {
        System.out.println("Conectando ao MySQL em " + host + "/" + database);
    }
    
    @Override
    public void query(String sql) {
        System.out.println("[MySQL] Executando: " + sql);
    }
    
    @Override
    public void close() {
        System.out.println("[MySQL] Conexão fechada");
    }
}

class PostgreSQLDatabaseImpl implements Database {
    private String host;
    private String database;
    
    public PostgreSQLDatabaseImpl(String host, String database) {
        this.host = host;
        this.database = database;
    }
    
    @Override
    public void connect() {
        System.out.println("Conectando ao PostgreSQL em " + host + "/" + database);
    }
    
    @Override
    public void query(String sql) {
        System.out.println("[PostgreSQL] Executando: " + sql);
    }
    
    @Override
    public void close() {
        System.out.println("[PostgreSQL] Conexão fechada");
    }
}

// Implementação fake para testes
class InMemoryDatabase implements Database {
    private java.util.List<String> queries = new java.util.ArrayList<>();
    
    @Override
    public void connect() {
        System.out.println("[TEST] Conectando ao banco em memória");
    }
    
    @Override
    public void query(String sql) {
        queries.add(sql);
        System.out.println("[TEST] Query registrada: " + sql);
    }
    
    @Override
    public void close() {
        System.out.println("[TEST] Conexão fechada");
    }
    
    public java.util.List<String> getQueries() {
        return queries;
    }
}

//  Serviço de alto nível depende da abstração (interface)
class ServicoUsuario {
    private final Database database; //  Depende de abstração
    
    //  Injeção de dependência via construtor
    public ServicoUsuario(Database database) {
        this.database = database;
    }
    
    public void salvarUsuario(String nome) {
        database.connect();
        database.query("INSERT INTO usuarios (nome) VALUES ('" + nome + "')");
        database.close();
    }
    
    public void buscarUsuario(String id) {
        database.connect();
        database.query("SELECT * FROM usuarios WHERE id = " + id);
        database.close();
    }
}

// Exemplo com Notificações

interface ProvedorEmail {
    void enviarEmail(String destinatario, String assunto, String corpo);
}

class SMTPEmailProvider implements ProvedorEmail {
    private String smtpServer;
    private int porta;
    
    public SMTPEmailProvider(String smtpServer, int porta) {
        this.smtpServer = smtpServer;
        this.porta = porta;
    }
    
    @Override
    public void enviarEmail(String destinatario, String assunto, String corpo) {
        System.out.printf("[SMTP - %s:%d] Para: %s | Assunto: %s%n", 
            smtpServer, porta, destinatario, assunto);
    }
}

class SendGridProvider implements ProvedorEmail {
    private String apiKey;
    
    public SendGridProvider(String apiKey) {
        this.apiKey = apiKey;
    }
    
    @Override
    public void enviarEmail(String destinatario, String assunto, String corpo) {
        System.out.println("[SendGrid API] Enviando email via API key: " + apiKey.substring(0, 8) + "...");
    }
}

// Fake para testes
class FakeEmailProvider implements ProvedorEmail {
    private java.util.List<String> emailsEnviados = new java.util.ArrayList<>();
    
    @Override
    public void enviarEmail(String destinatario, String assunto, String corpo) {
        String email = String.format("Para: %s | Assunto: %s", destinatario, assunto);
        emailsEnviados.add(email);
        System.out.println("[FAKE] Email registrado: " + email);
    }
    
    public java.util.List<String> getEmailsEnviados() {
        return emailsEnviados;
    }
    
    public boolean foiEnviadoPara(String destinatario) {
        return emailsEnviados.stream()
            .anyMatch(e -> e.contains(destinatario));
    }
}

class NotificacaoService {
    private final ProvedorEmail provedorEmail;
    
    public NotificacaoService(ProvedorEmail provedorEmail) {
        this.provedorEmail = provedorEmail;
    }
    
    public void notificarBoasVindas(String email) {
        provedorEmail.enviarEmail(
            email,
            "Bem-vindo!",
            "Obrigado por se cadastrar em nossa plataforma."
        );
    }
    
    public void notificarPromocao(String email, String promocao) {
        provedorEmail.enviarEmail(
            email,
            "Promoção especial!",
            "Aproveite: " + promocao
        );
    }
}

// Exemplo de uso com injeção de dependência
class Application {
    public static void main(String[] args) {
        // Ambiente de produção
        Database mysql = new MySQLDatabaseImpl("localhost", "prod_db");
        ServicoUsuario servicoProd = new ServicoUsuario(mysql);
        servicoProd.salvarUsuario("João");
        
        // Ambiente de teste
        Database memoryDb = new InMemoryDatabase();
        ServicoUsuario servicoTest = new ServicoUsuario(memoryDb);
        servicoTest.salvarUsuario("Maria");
        // Podemos verificar as queries executadas no teste
        
        // Email com SendGrid
        ProvedorEmail sendGrid = new SendGridProvider("SG.xxxxxxxx");
        NotificacaoService notificacaoProd = new NotificacaoService(sendGrid);
        notificacaoProd.notificarBoasVindas("cliente@empresa.com");
        
        // Email fake para testes
        ProvedorEmail fakeEmail = new FakeEmailProvider();
        NotificacaoService notificacaoTest = new NotificacaoService(fakeEmail);
        notificacaoTest.notificarBoasVindas("teste@teste.com");
        // Podemos verificar se o email foi "enviado" no teste
    }
}
