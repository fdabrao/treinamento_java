package com.avanade.curso.excecoes;

import java.io.*;
import java.sql.*;
import java.util.Scanner;

/**
 * TRATAMENTO DE EXCEÇÕES EM JAVA
 * 
 * Exceções são eventos que interrompem o fluxo normal de execução do programa.
 * Java divide exceções em duas categorias principais:
 * 
 * 1. CHECKED EXCEPTIONS: Obrigatórias de tratar ou declarar
 *    - Herdam de Exception (mas não de RuntimeException)
 *    - Ex: IOException, SQLException, ClassNotFoundException
 *    - Representam condições recuperáveis que o programa deve antecipar
 * 
 * 2. UNCHECKED EXCEPTIONS: Não obrigatórias de tratar
 *    - Herdam de RuntimeException
 *    - Ex: NullPointerException, IllegalArgumentException, ArrayIndexOutOfBoundsException
 *    - Representam bugs de programação
 * 
 * 3. ERRORS: Problemas graves do sistema, não devem ser capturados
 *    - Ex: OutOfMemoryError, StackOverflowError
 */

// ============================================
// EXCEÇÕES CUSTOMIZADAS
// ============================================

/**
 * Exceção checked - exige tratamento obrigatório
 * Usada para erros de negócio previsíveis
 */
class ContaNaoEncontradaException extends Exception {
    private final String numeroConta;
    
    public ContaNaoEncontradaException(String numeroConta) {
        super("Conta não encontrada: " + numeroConta);
        this.numeroConta = numeroConta;
    }
    
    public ContaNaoEncontradaException(String numeroConta, Throwable causa) {
        super("Conta não encontrada: " + numeroConta, causa);
        this.numeroConta = numeroConta;
    }
    
    public String getNumeroConta() {
        return numeroConta;
    }
}

/**
 * Exceção unchecked - não exige tratamento obrigatório
 * Usada para erros de validação
 */
class ValorInvalidoException extends RuntimeException {
    private final double valor;
    private final double minimo;
    private final double maximo;
    
    public ValorInvalidoException(double valor, double minimo, double maximo) {
        super(String.format("Valor %.2f inválido. Deve estar entre %.2f e %.2f", valor, minimo, maximo));
        this.valor = valor;
        this.minimo = minimo;
        this.maximo = maximo;
    }
    
    public double getValor() { return valor; }
    public double getMinimo() { return minimo; }
    public double getMaximo() { return maximo; }
}

/**
 * Exceção de validação de dados
 */
class ValidacaoException extends RuntimeException {
    private final String campo;
    
    public ValidacaoException(String campo, String mensagem) {
        super(String.format("Erro no campo '%s': %s", campo, mensagem));
        this.campo = campo;
    }
    
    public String getCampo() {
        return campo;
    }
}

// ============================================
// CLASSE DE SERVIÇO COM EXCEÇÕES
// ============================================

class ContaBancariaService {
    private double saldo = 1000.0;
    
    /**
     * Método que lança exceção checked
     * Obriga o chamador a tratar ou declarar
     */
    public void transferir(String contaOrigem, String contaDestino, double valor) 
            throws ContaNaoEncontradaException, SQLException {
        
        if (contaDestino == null || contaDestino.isEmpty()) {
            throw new ContaNaoEncontradaException(contaDestino);
        }
        
        if (valor <= 0) {
            throw new IllegalArgumentException("Valor deve ser positivo");
        }
        
        if (valor > saldo) {
            throw new IllegalStateException("Saldo insuficiente");
        }
        
        // Simula erro de banco de dados
        if (contaDestino.equals("ERRO_DB")) {
            throw new SQLException("Erro ao acessar banco de dados");
        }
        
        saldo -= valor;
        System.out.printf("Transferido R$ %.2f da conta %s para %s%n", valor, contaOrigem, contaDestino);
    }
    
    /**
     * Método que lança exceção unchecked
     * Não obriga tratamento, mas pode ser capturada
     */
    public void depositar(double valor) {
        if (valor <= 0) {
            throw new ValorInvalidoException(valor, 0.01, Double.MAX_VALUE);
        }
        saldo += valor;
    }
    
    /**
     * Validação que acumula erros antes de lançar exceção
     */
    public void validarCadastro(String nome, String email, int idade) {
        StringBuilder erros = new StringBuilder();
        
        if (nome == null || nome.trim().isEmpty()) {
            erros.append("Nome é obrigatório\n");
        }
        
        if (email == null || !email.contains("@")) {
            erros.append("Email inválido\n");
        }
        
        if (idade < 18) {
            erros.append("Deve ter pelo menos 18 anos\n");
        }
        
        if (erros.length() > 0) {
            throw new IllegalArgumentException("Erros de validação:\n" + erros.toString());
        }
    }
    
    public double getSaldo() {
        return saldo;
    }
}

// ============================================
// EXEMPLOS DE TRATAMENTO
// ============================================

public class ExcecoesExample {
    private ContaBancariaService service = new ContaBancariaService();
    
    /**
     * TRY-CATCH BÁSICO
     * Captura e trata exceção específica
     */
    public void exemploTryCatchBasico() {
        try {
            service.transferir("123", "456", 500);
        } catch (ContaNaoEncontradaException e) {
            System.out.println("Erro: " + e.getMessage());
            System.out.println("Conta: " + e.getNumeroConta());
        } catch (SQLException e) {
            System.out.println("Erro de banco: " + e.getMessage());
        }
    }
    
    /**
     * MULTI-CATCH (Java 7+)
     * Captura múltiplas exceções em um bloco
     */
    public void exemploMultiCatch() {
        try {
            service.transferir("123", "456", 500);
        } catch (ContaNaoEncontradaException | SQLException e) {
            // 'e' é final e do tipo mais genérico possível (Exception)
            System.out.println("Erro na transferência: " + e.getMessage());
        }
    }
    
    /**
     * CATCH COM HIERARQUIA
     * Ordem importa: mais específico primeiro!
     */
    public void exemploCatchHierarquia() {
        try {
            // Código que pode lançar várias exceções
            Object obj = null;
            obj.toString(); // NullPointerException
        } catch (NullPointerException e) {
            // Captura específica
            System.out.println("Null pointer: " + e.getMessage());
        } catch (RuntimeException e) {
            // Captura mais genérica
            System.out.println("Runtime: " + e.getMessage());
        } catch (Exception e) {
            // Captura ainda mais genérica
            System.out.println("Exception: " + e.getMessage());
        }
    }
    
    /**
     * FINALLY
     * Sempre executado, mesmo se exceção ocorrer
     * Útil para liberar recursos
     */
    public void exemploFinally() {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("arquivo.txt"));
            String linha = reader.readLine();
            System.out.println(linha);
        } catch (IOException e) {
            System.out.println("Erro ao ler: " + e.getMessage());
        } finally {
            // Sempre executado!
            System.out.println("Fechando recursos...");
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    System.out.println("Erro ao fechar: " + e.getMessage());
                }
            }
        }
    }
    
    /**
     * TRY-WITH-RESOURCES (Java 7+)
 * Fecha recursos automaticamente
     * Recurso deve implementar AutoCloseable
     */
    public void exemploTryWithResources() {
        // Recursos são fechados automaticamente no final
        try (BufferedReader reader = new BufferedReader(new FileReader("arquivo.txt"));
             Scanner scanner = new Scanner(System.in)) {
            
            String linha = reader.readLine();
            System.out.println("Conteúdo: " + linha);
            
        } catch (IOException e) {
            System.out.println("Erro: " + e.getMessage());
        }
        // reader e scanner são fechados automaticamente aqui!
    }
    
    /**
     * MÚLTIPLOS RECURSOS NO TRY-WITH-RESOURCES
     */
    public void exemploMultiplosRecursos() throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader("entrada.txt"));
             BufferedWriter writer = new BufferedWriter(new FileWriter("saida.txt"))) {
            
            String linha;
            while ((linha = reader.readLine()) != null) {
                writer.write(linha.toUpperCase());
                writer.newLine();
            }
        }
        // Ambos fechados automaticamente em ordem reversa (writer, depois reader)
    }
    
    /**
     * PROPAGAÇÃO DE EXCEÇÕES
     * Throws declara que método pode lançar exceção
     */
    public void metodoQuePropaga() throws ContaNaoEncontradaException, SQLException {
        // Não trata, apenas declara que pode lançar
        service.transferir("123", "456", 500);
    }
    
    /**
     * WRAP DE EXCEÇÕES
     * Converter checked em unchecked
     */
    public void exemploWrapException() {
        try {
            service.transferir("123", "456", 500);
        } catch (ContaNaoEncontradaException | SQLException e) {
            // Wrap em RuntimeException para não propagar checked
            throw new RuntimeException("Erro na operação bancária", e);
        }
    }
    
    /**
     * RE-THROW COM REFINAMENTO DE TIPO (Java 7+)
     */
    public void exemploReThrowRefinado() throws ContaNaoEncontradaException, SQLException {
        try {
            service.transferir("123", "ERRO_DB", 500);
        } catch (Exception e) {
            // Pode re-lançar como os tipos específicos declarados no throws
            System.out.println("Erro: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * BLOCO FINALLY COM RETURN
     * Cuidado: finally executa mesmo com return no try/catch
     */
    public int exemploFinallyComReturn() {
        try {
            return 1; // Tentativa de retorno
        } catch (Exception e) {
            return 2;
        } finally {
            // Isso executa ANTES do método retornar!
            System.out.println("Finally executando...");
            // Se houver return aqui, sobrescreve os anteriores!
        }
    }
    
    /**
     * SUPRESSÃO DE EXCEÇÕES
     * No try-with-resources, exceção de close pode suprimir a principal
     */
    public void exemploSupressao() {
        try (RecursoProblematico recurso = new RecursoProblematico()) {
            throw new RuntimeException("Erro principal no try");
        } catch (RuntimeException e) {
            System.out.println("Principal: " + e.getMessage());
            // Pode haver exceções suprimidas
            for (Throwable suprimida : e.getSuppressed()) {
                System.out.println("Suprimida: " + suprimida.getMessage());
            }
        }
    }
    
    /**
     * TRATAMENTO COM ASSERT
     * Para validações internas (não para input de usuário)
     * Desativado por padrão em produção (-ea para ativar)
     */
    public void exemploAssert(int valor) {
        // Só lança AssertionError se -ea estiver ativado
        assert valor > 0 : "Valor deve ser positivo: " + valor;
        System.out.println("Valor válido: " + valor);
    }
    
    /**
     * BOAS PRÁTICAS: NÃO CAPTURAR EXCEPTION GENERICA
     */
    public void exemploMausHabitos() {
        //  RUIM: Captura tudo, esconde bugs
        try {
            // código
        } catch (Exception e) {
            // Ignora silenciosamente
        }
        
        //  BOM: Captura específica
        try {
            service.transferir("123", "456", 500);
        } catch (ContaNaoEncontradaException e) {
            // Tratamento específico para conta não encontrada
            System.out.println("Conta não existe: " + e.getNumeroConta());
        } catch (SQLException e) {
            // Tratamento específico para erro de banco
            System.out.println("Erro de banco: " + e.getMessage());
        }
    }
    
    // Classe para demonstrar supressão
    static class RecursoProblematico implements AutoCloseable {
        @Override
        public void close() {
            throw new RuntimeException("Erro ao fechar recurso");
        }
    }
}

// ============================================
// UTILITÁRIO DE EXCEÇÕES
// ============================================

/**
 * Classe utilitária para operações seguras
 */
class ExceptionUtils {
    
    /**
     * Executa operação ignorando exceções
     * Útil para limpeza ou logging
     */
    public static void executarIgnorandoExcecoes(Runnable operacao) {
        try {
            operacao.run();
        } catch (Exception e) {
            // Ignora silenciosamente (use com cautela!)
        }
    }
    
    /**
     * Executa operação com valor padrão em caso de erro
     */
    public static <T> T executarComPadrao(SupplierThrows<T> operacao, T padrao) {
        try {
            return operacao.get();
        } catch (Exception e) {
            return padrao;
        }
    }
    
    /**
     * Interface funcional que permite lançar exceções
     */
    @FunctionalInterface
    interface SupplierThrows<T> {
        T get() throws Exception;
    }
}
