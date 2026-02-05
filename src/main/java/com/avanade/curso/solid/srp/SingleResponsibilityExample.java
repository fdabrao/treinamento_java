package com.avanade.curso.solid.srp;

/**
 * S - Single Responsibility Principle (Princípio da Responsabilidade Única)
 * 
 * Uma classe deve ter apenas um motivo para mudar.
 * Cada classe deve ter apenas uma responsabilidade.
 * 
 * ANTES (errado): Uma classe fazendo muitas coisas
 * DEPOIS (correto): Classes separadas por responsabilidade
 */

//  ANTES: Classe violando SRP - faz muitas coisas
class FuncionarioErrado {
    private String nome;
    private double salario;
    
    public double calcularSalario() {
        return salario * 0.9; // 10% de impostos
    }
    
    public void enviarEmail(String mensagem) {
        // Lógica de envio de email
        System.out.println("Enviando email para: " + nome);
    }
    
    public void salvarNoBanco() {
        // Lógica de persistência
        System.out.println("Salvando no banco...");
    }
    
    public void gerarRelatorio() {
        // Lógica de relatório
        System.out.println("Gerando relatório...");
    }
}

//  DEPOIS: Classes seguindo SRP

// Responsabilidade: Representar dados do funcionário
class Funcionario {
    private String nome;
    private String cargo;
    private double salarioBase;
    
    public Funcionario(String nome, String cargo, double salarioBase) {
        this.nome = nome;
        this.cargo = cargo;
        this.salarioBase = salarioBase;
    }
    
    public String getNome() { return nome; }
    public String getCargo() { return cargo; }
    public double getSalarioBase() { return salarioBase; }
}

// Responsabilidade: Calcular salários
class CalculadoraSalario {
    public double calcular(Funcionario funcionario) {
        double salarioBase = funcionario.getSalarioBase();
        return switch (funcionario.getCargo()) {
            case "DESENVOLVEDOR" -> salarioBase * 0.8; // 20% desconto
            case "GERENTE" -> salarioBase * 0.75; // 25% desconto
            default -> salarioBase * 0.9; // 10% desconto
        };
    }
}

// Responsabilidade: Enviar notificações
class ServicoEmail {
    public void enviarEmail(Funcionario funcionario, String assunto, String mensagem) {
        System.out.printf("Email para %s - Assunto: %s%n", funcionario.getNome(), assunto);
        System.out.println("Mensagem: " + mensagem);
    }
}

// Responsabilidade: Persistir dados
class FuncionarioRepository {
    public void salvar(Funcionario funcionario) {
        System.out.println("Funcionário salvo: " + funcionario.getNome());
    }
    
    public Funcionario buscarPorNome(String nome) {
        // Simulação de busca no banco
        return new Funcionario(nome, "DESENVOLVEDOR", 5000.0);
    }
}

// Responsabilidade: Gerar relatórios
class GeradorRelatorio {
    public String gerarRelatorioPagamento(Funcionario funcionario, double salarioLiquido) {
        return String.format("""
            === RELATÓRIO DE PAGAMENTO ===
            Funcionário: %s
            Cargo: %s
            Salário Base: R$ %.2f
            Salário Líquido: R$ %.2f
            ==============================
            """, 
            funcionario.getNome(), 
            funcionario.getCargo(),
            funcionario.getSalarioBase(),
            salarioLiquido
        );
    }
}
