package com.avanade.curso.oop.heranca;

/**
 * HERANÇA
 * 
 * A herança permite que uma classe (filha) herde atributos e métodos de outra
 * classe (pai), promovendo reuso de código e estabelecendo relações "é um".
 * 
 * Benefícios:
 * - Reuso de código comum
 * - Especialização de comportamentos
 * - Hierarquia de classes lógica
 * - Extensibilidade do sistema
 * 
 * Veja a classe Funcionario (pai) e suas especializações (filhas).
 */
public class Funcionario {
    
    protected String nome;
    protected String cpf;
    protected double salarioBase;
    protected int horasTrabalhadas;
    
    public Funcionario(String nome, String cpf, double salarioBase) {
        this.nome = nome;
        this.cpf = cpf;
        this.salarioBase = salarioBase;
        this.horasTrabalhadas = 0;
    }
    
    // Método que pode ser sobrescrito pelas subclasses (polimorfismo)
    public double calcularSalario() {
        return salarioBase + calcularBonus();
    }
    
    // Método que pode ser sobrescrito
    protected double calcularBonus() {
        return 0; // Funcionario comum não tem bônus base
    }
    
    public void registrarHoras(int horas) {
        if (horas < 0 || horas > 24) {
            throw new IllegalArgumentException("Horas inválidas");
        }
        this.horasTrabalhadas += horas;
    }
    
    public String getNome() {
        return nome;
    }
    
    public String getCpf() {
        return cpf;
    }
    
    public double getSalarioBase() {
        return salarioBase;
    }
    
    public int getHorasTrabalhadas() {
        return horasTrabalhadas;
    }
    
    public String obterDescricaoCargo() {
        return "Funcionário";
    }
    
    @Override
    public String toString() {
        return String.format("Funcionario{nome='%s', cargo='%s', salario=R$ %.2f}",
                nome, obterDescricaoCargo(), calcularSalario());
    }
}
