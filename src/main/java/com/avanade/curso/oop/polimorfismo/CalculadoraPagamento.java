package com.avanade.curso.oop.polimorfismo;

/**
 * POLIMORFISMO
 * 
 * Polimorfismo significa "muitas formas". É a capacidade de um objeto
 * se comportar de diferentes formas dependendo do contexto.
 * 
 * Tipos de polimorfismo demonstrados aqui:
 * 1. Sobrescrita (Override) - mesmo método, comportamento diferente
 * 2. Sobrecarga (Overload) - métodos com mesmo nome, parâmetros diferentes
 * 3. Polimorfismo de inclusão - tratando objetos filhos como o pai
 * 
 * Benefícios:
 * - Código mais flexível e extensível
 * - Tratamento uniforme de objetos diferentes
 * - Facilidade para adicionar novos tipos
 */
public class CalculadoraPagamento {
    
    /**
     * Método polimórfico - trabalha com a interface/tipo genérico
     * Não precisa saber qual tipo específico de pagamento está processando
     */
    public ResultadoPagamento processarPagamento(Pagamento pagamento) {
        // Chama o método apropriado baseado no tipo real do objeto
        boolean sucesso = pagamento.processar();
        double taxa = pagamento.calcularTaxa();
        String metodo = pagamento.obterDescricao();
        
        return new ResultadoPagamento(sucesso, metodo, pagamento.getValor(), taxa);
    }
    
    /**
     * SOBRECARGA (Overload) - mesmo nome, parâmetros diferentes
     * Permite processar múltiplos pagamentos de formas diferentes
     */
    public ResultadoPagamento[] processarPagamentos(Pagamento... pagamentos) {
        ResultadoPagamento[] resultados = new ResultadoPagamento[pagamentos.length];
        for (int i = 0; i < pagamentos.length; i++) {
            resultados[i] = processarPagamento(pagamentos[i]);
        }
        return resultados;
    }
    
    public ResultadoPagamento[] processarPagamentos(java.util.List<Pagamento> pagamentos) {
        return processarPagamentos(pagamentos.toArray(new Pagamento[0]));
    }
    
    /**
     * Calcula o total de taxas para uma lista de pagamentos
     * Demonstra polimorfismo de inclusão
     */
    public double calcularTotalTaxas(java.util.List<Pagamento> pagamentos) {
        double total = 0;
        for (Pagamento p : pagamentos) {
            total += p.calcularTaxa();
        }
        return total;
    }
    
    /**
     * Processa reembolso - comportamento polimórfico
     */
    public boolean processarReembolso(Pagamento pagamento) {
        return pagamento.estornar();
    }
    
    // Classe interna para resultado
    public record ResultadoPagamento(
        boolean sucesso,
        String metodo,
        double valor,
        double taxa
    ) {
        public double getValorTotal() {
            return valor + taxa;
        }
    }
}
