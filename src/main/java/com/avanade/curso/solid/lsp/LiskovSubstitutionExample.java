package com.avanade.curso.solid.lsp;

/**
 * L - Liskov Substitution Principle (Princípio da Substituição de Liskov)
 * 
 * Classes filhas devem poder substituir classes pai sem alterar o comportamento.
 * A subclasse deve respeitar o contrato da superclasse.
 * 
 * ANTES (errado): Subclasse quebra o contrato da classe pai
 * DEPOIS (correto): Subclasse mantém o comportamento esperado
 */

//  ANTES: Subclasse viola o contrato da classe pai
class RetanguloErrado {
    protected double largura;
    protected double altura;
    
    public void setLargura(double largura) {
        this.largura = largura;
    }
    
    public void setAltura(double altura) {
        this.altura = altura;
    }
    
    public double getLargura() { return largura; }
    public double getAltura() { return altura; }
    
    public double calcularArea() {
        return largura * altura;
    }
}

//  Quadrado quebra o comportamento esperado de Retangulo
class QuadradoErrado extends RetanguloErrado {
    @Override
    public void setLargura(double largura) {
        this.largura = largura;
        this.altura = largura; // Força altura = largura
    }
    
    @Override
    public void setAltura(double altura) {
        this.altura = altura;
        this.largura = altura; // Força largura = altura
    }
}

//  DEPOIS: Usar composição ou abstração correta

// Abstração que define o contrato
interface FormaGeometrica {
    double calcularArea();
    double calcularPerimetro();
}

// Cada classe implementa seu comportamento corretamente
class Retangulo implements FormaGeometrica {
    private final double largura;
    private final double altura;
    
    public Retangulo(double largura, double altura) {
        if (largura <= 0 || altura <= 0) {
            throw new IllegalArgumentException("Dimensões devem ser positivas");
        }
        this.largura = largura;
        this.altura = altura;
    }
    
    @Override
    public double calcularArea() {
        return largura * altura;
    }
    
    @Override
    public double calcularPerimetro() {
        return 2 * (largura + altura);
    }
    
    public double getLargura() { return largura; }
    public double getAltura() { return altura; }
}

class Quadrado implements FormaGeometrica {
    private final double lado;
    
    public Quadrado(double lado) {
        if (lado <= 0) {
            throw new IllegalArgumentException("Lado deve ser positivo");
        }
        this.lado = lado;
    }
    
    @Override
    public double calcularArea() {
        return lado * lado;
    }
    
    @Override
    public double calcularPerimetro() {
        return 4 * lado;
    }
    
    public double getLado() { return lado; }
}

// Exemplo com funcionários
abstract class Funcionario {
    protected String nome;
    protected double salarioBase;
    
    public Funcionario(String nome, double salarioBase) {
        this.nome = nome;
        this.salarioBase = salarioBase;
    }
    
    // Contrato: deve calcular salário corretamente
    public abstract double calcularSalario();
    
    public String getNome() { return nome; }
}

//  Respeita o contrato: calcula salário normalmente
class Desenvolvedor extends Funcionario {
    private double bonusPorProjeto;
    
    public Desenvolvedor(String nome, double salarioBase, double bonusPorProjeto) {
        super(nome, salarioBase);
        this.bonusPorProjeto = bonusPorProjeto;
    }
    
    @Override
    public double calcularSalario() {
        return salarioBase + bonusPorProjeto;
    }
}

//  Respeita o contrato: calcula salário normalmente
class Estagiario extends Funcionario {
    private int horasTrabalhadas;
    private double valorHora;
    
    public Estagiario(String nome, double valorHora, int horasTrabalhadas) {
        super(nome, 0); // Estagiário não tem salário base fixo
        this.valorHora = valorHora;
        this.horasTrabalhadas = horasTrabalhadas;
    }
    
    @Override
    public double calcularSalario() {
        return valorHora * horasTrabalhadas;
    }
}

//  VIOLA LSP: altera comportamento inesperadamente
class FuncionarioVoluntarioErrado extends Funcionario {
    public FuncionarioVoluntarioErrado(String nome) {
        super(nome, 0);
    }
    
    @Override
    public double calcularSalario() {
        throw new UnsupportedOperationException("Voluntários não recebem salário");
        //  Quebra o contrato! Espera-se que retorne um valor
    }
}

//  Respeita LSP: mesmo não recebendo, retorna valor válido
class Voluntario extends Funcionario {
    public Voluntario(String nome) {
        super(nome, 0);
    }
    
    @Override
    public double calcularSalario() {
        return 0; //  Contrato mantido: retorna um valor (zero)
    }
    
    public String getBeneficios() {
        return "Certificado de participação";
    }
}

// Classe que usa polimorfismo (funciona com QUALQUER Funcionario)
class FolhaPagamento {
    public double calcularTotal(Funcionario[] funcionarios) {
        double total = 0;
        for (Funcionario f : funcionarios) {
            total += f.calcularSalario(); // Funciona para todos!
        }
        return total;
    }
}
