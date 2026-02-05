package com.avanade.curso.oop.heranca;

/**
 * Desenvolvedor HERDA de Funcionario
 * 
 * Desenvolvedor "é um" Funcionario com características específicas:
 * - Tem nível de senioridade
 * - Recebe bônus por tecnologia
 * - Tem certificações
 */
public class Desenvolvedor extends Funcionario {
    
    public enum Nivel {
        JUNIOR, PLENO, SENIOR, ESPECIALISTA
    }
    
    private Nivel nivel;
    private String tecnologiaPrincipal;
    private int certificacoes;
    
    private static final double BONUS_BASE_DEV = 500.0;
    
    public Desenvolvedor(String nome, String cpf, double salarioBase, 
                        Nivel nivel, String tecnologiaPrincipal) {
        super(nome, cpf, salarioBase);
        this.nivel = nivel;
        this.tecnologiaPrincipal = tecnologiaPrincipal;
        this.certificacoes = 0;
    }
    
    @Override
    protected double calcularBonus() {
        double bonusNivel = switch (nivel) {
            case JUNIOR -> BONUS_BASE_DEV * 0.5;
            case PLENO -> BONUS_BASE_DEV;
            case SENIOR -> BONUS_BASE_DEV * 2;
            case ESPECIALISTA -> BONUS_BASE_DEV * 3;
        };
        
        // Bônus adicional por certificações
        double bonusCertificacoes = certificacoes * 200;
        
        return bonusNivel + bonusCertificacoes;
    }
    
    // Método específico de Desenvolvedor
    public void adicionarCertificacao() {
        this.certificacoes++;
    }
    
    public void promover() {
        switch (nivel) {
            case JUNIOR -> this.nivel = Nivel.PLENO;
            case PLENO -> this.nivel = Nivel.SENIOR;
            case SENIOR -> this.nivel = Nivel.ESPECIALISTA;
            // ESPECIALISTA não pode ser promovido mais
        }
    }
    
    public Nivel getNivel() {
        return nivel;
    }
    
    public String getTecnologiaPrincipal() {
        return tecnologiaPrincipal;
    }
    
    public int getCertificacoes() {
        return certificacoes;
    }
    
    @Override
    public String obterDescricaoCargo() {
        return String.format("Desenvolvedor %s - %s", nivel, tecnologiaPrincipal);
    }
    
    @Override
    public String toString() {
        return String.format("Desenvolvedor{nome='%s', nivel=%s, tech='%s', certs=%d, salario=R$ %.2f}",
                nome, nivel, tecnologiaPrincipal, certificacoes, calcularSalario());
    }
}
