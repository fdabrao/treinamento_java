package com.avanade.curso.solid.isp;

/**
 * I - Interface Segregation Principle (Princípio da Segregação de Interfaces)
 * 
 * Uma classe não deve ser forçada a implementar interfaces que não usa.
 * Prefira interfaces pequenas e específicas a interfaces grandes.
 * 
 * ANTES (errado): Interface grande obriga classes a implementar métodos desnecessários
 * DEPOIS (correto): Interfaces pequenas que classes implementam conforme necessidade
 */

//  ANTES: Interface "gorda" que força implementações desnecessárias
interface TrabalhadorErrado {
    void trabalhar();
    void comer();
    void dormir();
    void fazerReuniao();
    void programar();
    void atenderCliente();
}

//  Robô é forçado a implementar métodos que não faz sentido
class RoboErrado implements TrabalhadorErrado {
    @Override
    public void trabalhar() {
        System.out.println("Robô trabalhando...");
    }
    
    @Override
    public void comer() {
        throw new UnsupportedOperationException("Robôs não comem!");
    }
    
    @Override
    public void dormir() {
        throw new UnsupportedOperationException("Robôs não dormem!");
    }
    
    @Override
    public void fazerReuniao() {
        System.out.println("Robô na reunião...");
    }
    
    @Override
    public void programar() {
        throw new UnsupportedOperationException("Este robô não programa!");
    }
    
    @Override
    public void atenderCliente() {
        throw new UnsupportedOperationException("Este robô não atende cliente!");
    }
}

//  DEPOIS: Interfaces pequenas e específicas

// Interface para qualquer trabalhador
interface Trabalhador {
    void trabalhar();
}

// Interface apenas para seres vivos (precisam comer/dormir)
interface SerVivo {
    void comer();
    void dormir();
}

// Interface para quem programa
interface Programador {
    void programar();
    void revisarCodigo();
}

// Interface para quem atende clientes
interface Atendente {
    void atenderCliente();
    void resolverProblema(String problema);
}

// Interface para quem participa de reuniões
interface Reuniao {
    void fazerReuniao();
    void apresentarRelatorio();
}

//  Desenvolvedor implementa apenas o que precisa
class Desenvolvedor implements Trabalhador, SerVivo, Programador, Reuniao {
    private String nome;
    
    public Desenvolvedor(String nome) {
        this.nome = nome;
    }
    
    @Override
    public void trabalhar() {
        System.out.println(nome + " está trabalhando no código");
    }
    
    @Override
    public void comer() {
        System.out.println(nome + " está almoçando");
    }
    
    @Override
    public void dormir() {
        System.out.println(nome + " está descansando");
    }
    
    @Override
    public void programar() {
        System.out.println(nome + " está programando em Java");
    }
    
    @Override
    public void revisarCodigo() {
        System.out.println(nome + " está revisando código");
    }
    
    @Override
    public void fazerReuniao() {
        System.out.println(nome + " está na daily");
    }
    
    @Override
    public void apresentarRelatorio() {
        System.out.println(nome + " está apresentando sprint");
    }
}

//  Robô implementa apenas o que faz sentido
class Robo implements Trabalhador {
    private String modelo;
    
    public Robo(String modelo) {
        this.modelo = modelo;
    }
    
    @Override
    public void trabalhar() {
        System.out.println("Robô " + modelo + " trabalhando 24/7 sem parar");
    }
}

//  Atendente implementa apenas o que precisa
class AtendenteCliente implements Trabalhador, SerVivo, Atendente {
    private String nome;
    
    public AtendenteCliente(String nome) {
        this.nome = nome;
    }
    
    @Override
    public void trabalhar() {
        System.out.println(nome + " está atendendo clientes");
    }
    
    @Override
    public void comer() {
        System.out.println(nome + " está no intervalo");
    }
    
    @Override
    public void dormir() {
        System.out.println(nome + " está descansando em casa");
    }
    
    @Override
    public void atenderCliente() {
        System.out.println(nome + " atendendo chamada");
    }
    
    @Override
    public void resolverProblema(String problema) {
        System.out.println(nome + " resolvendo: " + problema);
    }
}

//  Robô Programador implementa apenas o que faz sentido
class RoboProgramador implements Trabalhador, Programador {
    private String versao;
    
    public RoboProgramador(String versao) {
        this.versao = versao;
    }
    
    @Override
    public void trabalhar() {
        System.out.println("Copilot v" + versao + " trabalhando");
    }
    
    @Override
    public void programar() {
        System.out.println("Copilot sugerindo código...");
    }
    
    @Override
    public void revisarCodigo() {
        System.out.println("Copilot analisando código...");
    }
}

// Uso polimórfico
class Empresa {
    public void iniciarTrabalho(Trabalhador[] trabalhadores) {
        for (Trabalhador t : trabalhadores) {
            t.trabalhar();
        }
    }
    
    public void horaDoAlmoço(SerVivo[] seres) {
        for (SerVivo s : seres) {
            s.comer();
        }
    }
    
    public void codeReview(Programador[] programadores) {
        for (Programador p : programadores) {
            p.revisarCodigo();
        }
    }
}
