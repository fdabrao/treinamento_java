package com.avanade.curso.oop.heranca;

/**
 * Gerente HERDA de Funcionario
 * 
 * Gerente "é um" Funcionario, mas com características específicas:
 * - Tem bônus adicional
 * - Tem departamento
 * - Pode aprovar despesas
 */
public class Gerente extends Funcionario {
    
    private String departamento;
    private int equipeSize;
    private static final double BONUS_GERENTE = 1500.0;
    private static final double LIMITE_APROVACAO = 5000.0;
    
    public Gerente(String nome, String cpf, double salarioBase, String departamento, int equipeSize) {
        // Chama construtor da classe pai
        super(nome, cpf, salarioBase);
        this.departamento = departamento;
        this.equipeSize = equipeSize;
    }
    
    // Sobrescreve método da classe pai para adicionar comportamento específico
    @Override
    protected double calcularBonus() {
        // Bônus base de gerente + bônus por tamanho da equipe
        return BONUS_GERENTE + (equipeSize * 100);
    }
    
    // Método específico de Gerente
    public boolean podeAprovarDespesa(double valor) {
        return valor <= LIMITE_APROVACAO;
    }
    
    public String getDepartamento() {
        return departamento;
    }
    
    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }
    
    public int getEquipeSize() {
        return equipeSize;
    }
    
    public void setEquipeSize(int equipeSize) {
        this.equipeSize = equipeSize;
    }
    
    @Override
    public String obterDescricaoCargo() {
        return "Gerente de " + departamento;
    }
    
    @Override
    public String toString() {
        return String.format("Gerente{nome='%s', departamento='%s', equipe=%d, salario=R$ %.2f}",
                nome, departamento, equipeSize, calcularSalario());
    }
}
