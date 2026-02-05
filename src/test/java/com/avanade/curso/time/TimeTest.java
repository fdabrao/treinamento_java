package com.avanade.curso.time;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Locale;

/**
 * Testes para Java Time API
 */
class TimeTest {
    
    // ============================================
    // Testes de LocalDate
    // ============================================
    
    @Test
    @DisplayName("LocalDate deve criar data específica")
    void localDateCriacao() {
        LocalDate natal = LocalDate.of(2024, 12, 25);
        
        assertEquals(2024, natal.getYear());
        assertEquals(Month.DECEMBER, natal.getMonth());
        assertEquals(12, natal.getMonthValue());
        assertEquals(25, natal.getDayOfMonth());
    }
    
    @Test
    @DisplayName("LocalDate deve extrair componentes")
    void localDateComponentes() {
        LocalDate hoje = LocalDate.of(2024, 6, 15);
        
        assertEquals(2024, hoje.getYear());
        assertEquals(6, hoje.getMonthValue());
        assertEquals(15, hoje.getDayOfMonth());
        assertEquals(DayOfWeek.SATURDAY, hoje.getDayOfWeek());
    }
    
    @Test
    @DisplayName("LocalDate deve ser imutável")
    void localDateImutavel() {
        LocalDate original = LocalDate.of(2024, 6, 15);
        LocalDate amanha = original.plusDays(1);
        LocalDate proximoMes = original.plusMonths(1);
        LocalDate proximoAno = original.plusYears(1);
        
        // Original não muda
        assertEquals(15, original.getDayOfMonth());
        assertEquals(6, original.getMonthValue());
        
        // Novas instâncias têm os valores modificados
        assertEquals(16, amanha.getDayOfMonth());
        assertEquals(7, proximoMes.getMonthValue());
        assertEquals(2025, proximoAno.getYear());
    }
    
    @Test
    @DisplayName("LocalDate deve comparar datas corretamente")
    void localDateComparacao() {
        LocalDate data1 = LocalDate.of(2024, 6, 15);
        LocalDate data2 = LocalDate.of(2024, 6, 20);
        LocalDate data3 = LocalDate.of(2024, 6, 15);
        
        assertTrue(data1.isBefore(data2));
        assertTrue(data2.isAfter(data1));
        assertTrue(data1.isEqual(data3));
        assertFalse(data1.isEqual(data2));
    }
    
    @Test
    @DisplayName("LocalDate deve detectar ano bissexto")
    void anoBissexto() {
        LocalDate bissexto = LocalDate.of(2024, 1, 1);
        LocalDate naoBissexto = LocalDate.of(2023, 1, 1);
        
        assertTrue(bissexto.isLeapYear());
        assertFalse(naoBissexto.isLeapYear());
    }
    
    @Test
    @DisplayName("LocalDate deve usar TemporalAdjusters")
    void temporalAdjusters() {
        LocalDate hoje = LocalDate.of(2024, 6, 15);
        
        LocalDate primeiroDia = hoje.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate ultimoDia = hoje.with(TemporalAdjusters.lastDayOfMonth());
        LocalDate proximaSegunda = hoje.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
        
        assertEquals(1, primeiroDia.getDayOfMonth());
        assertEquals(30, ultimoDia.getDayOfMonth()); // Junho tem 30 dias
        assertEquals(DayOfWeek.MONDAY, proximaSegunda.getDayOfWeek());
    }
    
    // ============================================
    // Testes de LocalTime
    // ============================================
    
    @Test
    @DisplayName("LocalTime deve criar horário específico")
    void localTimeCriacao() {
        LocalTime hora = LocalTime.of(14, 30, 45);
        
        assertEquals(14, hora.getHour());
        assertEquals(30, hora.getMinute());
        assertEquals(45, hora.getSecond());
    }
    
    @Test
    @DisplayName("LocalTime deve manipular horas")
    void localTimeManipulacao() {
        LocalTime hora = LocalTime.of(10, 0);
        
        LocalTime maisUmaHora = hora.plusHours(1);
        LocalTime mais30Minutos = hora.plusMinutes(30);
        LocalTime menos2Horas = hora.minusHours(2);
        
        assertEquals(11, maisUmaHora.getHour());
        assertEquals(30, mais30Minutos.getMinute());
        assertEquals(8, menos2Horas.getHour());
    }
    
    // ============================================
    // Testes de LocalDateTime
    // ============================================
    
    @Test
    @DisplayName("LocalDateTime deve combinar data e hora")
    void localDateTimeCombinacao() {
        LocalDateTime dataHora = LocalDateTime.of(2024, 6, 15, 14, 30);
        
        assertEquals(2024, dataHora.getYear());
        assertEquals(6, dataHora.getMonthValue());
        assertEquals(15, dataHora.getDayOfMonth());
        assertEquals(14, dataHora.getHour());
        assertEquals(30, dataHora.getMinute());
    }
    
    @Test
    @DisplayName("LocalDateTime deve extrair LocalDate e LocalTime")
    void localDateTimeExtracao() {
        LocalDateTime dataHora = LocalDateTime.of(2024, 6, 15, 14, 30);
        
        LocalDate data = dataHora.toLocalDate();
        LocalTime hora = dataHora.toLocalTime();
        
        assertEquals(LocalDate.of(2024, 6, 15), data);
        assertEquals(LocalTime.of(14, 30), hora);
    }
    
    // ============================================
    // Testes de ZonedDateTime
    // ============================================
    
    @Test
    @DisplayName("ZonedDateTime deve criar com fuso horário")
    void zonedDateTimeFuso() {
        ZonedDateTime sp = ZonedDateTime.of(
            LocalDateTime.of(2024, 6, 15, 14, 0),
            ZoneId.of("America/Sao_Paulo")
        );
        
        assertEquals(ZoneId.of("America/Sao_Paulo"), sp.getZone());
        assertEquals(2024, sp.getYear());
        assertEquals(14, sp.getHour());
    }
    
    @Test
    @DisplayName("ZonedDateTime deve converter entre fusos")
    void zonedDateTimeConversao() {
        ZonedDateTime sp = ZonedDateTime.of(
            LocalDateTime.of(2024, 6, 15, 14, 0),
            ZoneId.of("America/Sao_Paulo")
        );
        
        ZonedDateTime ny = sp.withZoneSameInstant(ZoneId.of("America/New_York"));
        
        // NY está 1 hora atrás de SP (sem DST)
        // ou 2 horas (com DST)
        assertEquals(ZoneId.of("America/New_York"), ny.getZone());
    }
    
    // ============================================
    // Testes de Instant
    // ============================================
    
    @Test
    @DisplayName("Instant deve representar timestamp UTC")
    void instantBasico() {
        Instant agora = Instant.now();
        
        assertNotNull(agora);
        assertTrue(agora.toEpochMilli() > 0);
    }
    
    @Test
    @DisplayName("Instant deve criar de epoch")
    void instantDeEpoch() {
        Instant instant = Instant.ofEpochMilli(1700000000000L);
        
        assertEquals(1700000000000L, instant.toEpochMilli());
    }
    
    @Test
    @DisplayName("Instant deve converter para ZonedDateTime")
    void instantConversao() {
        Instant instant = Instant.now();
        ZonedDateTime sp = instant.atZone(ZoneId.of("America/Sao_Paulo"));
        
        assertNotNull(sp);
        assertEquals(ZoneId.of("America/Sao_Paulo"), sp.getZone());
    }
    
    // ============================================
    // Testes de Period
    // ============================================
    
    @Test
    @DisplayName("Period deve calcular diferença entre datas")
    void periodDiferenca() {
        LocalDate inicio = LocalDate.of(2020, 1, 1);
        LocalDate fim = LocalDate.of(2024, 6, 15);
        
        Period periodo = Period.between(inicio, fim);
        
        assertEquals(4, periodo.getYears());
        assertEquals(5, periodo.getMonths());
        assertEquals(14, periodo.getDays());
    }
    
    @Test
    @DisplayName("Period deve somar a data")
    void periodSoma() {
        LocalDate data = LocalDate.of(2024, 1, 1);
        Period doisAnos = Period.ofYears(2);
        
        LocalDate futuro = data.plus(doisAnos);
        
        assertEquals(2026, futuro.getYear());
    }
    
    // ============================================
    // Testes de Duration
    // ============================================
    
    @Test
    @DisplayName("Duration deve calcular diferença entre horários")
    void durationDiferenca() {
        LocalTime inicio = LocalTime.of(10, 0);
        LocalTime fim = LocalTime.of(14, 30, 45);
        
        Duration duracao = Duration.between(inicio, fim);
        
        assertEquals(4, duracao.toHours());
        assertEquals(270, duracao.toMinutes()); // 4h30m = 270min
    }
    
    @Test
    @DisplayName("Duration deve manipular tempo")
    void durationManipulacao() {
        LocalTime hora = LocalTime.of(10, 0);
        Duration duasHoras = Duration.ofHours(2);
        
        LocalTime futuro = hora.plus(duasHoras);
        
        assertEquals(12, futuro.getHour());
    }
    
    // ============================================
    // Testes de ChronoUnit
    // ============================================
    
    @Test
    @DisplayName("ChronoUnit deve calcular diferença em dias")
    void chronoUnitDias() {
        LocalDate inicio = LocalDate.of(2024, 1, 1);
        LocalDate fim = LocalDate.of(2024, 1, 15);
        
        long dias = ChronoUnit.DAYS.between(inicio, fim);
        
        assertEquals(14, dias);
    }
    
    @Test
    @DisplayName("ChronoUnit deve calcular diferença em horas")
    void chronoUnitHoras() {
        LocalTime inicio = LocalTime.of(10, 0);
        LocalTime fim = LocalTime.of(14, 0);
        
        long horas = ChronoUnit.HOURS.between(inicio, fim);
        
        assertEquals(4, horas);
    }
    
    // ============================================
    // Testes de DateTimeFormatter
    // ============================================
    
    @Test
    @DisplayName("DateTimeFormatter deve formatar data")
    void formatarData() {
        LocalDateTime dataHora = LocalDateTime.of(2024, 6, 15, 14, 30, 0);
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        
        String formatado = dataHora.format(formato);
        
        assertEquals("15/06/2024 14:30:00", formatado);
    }
    
    @Test
    @DisplayName("DateTimeFormatter deve fazer parsing de data")
    void parsingData() {
        String dataString = "15/06/2024 14:30:00";
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        
        LocalDateTime parseado = LocalDateTime.parse(dataString, formato);
        
        assertEquals(2024, parseado.getYear());
        assertEquals(6, parseado.getMonthValue());
        assertEquals(15, parseado.getDayOfMonth());
        assertEquals(14, parseado.getHour());
        assertEquals(30, parseado.getMinute());
    }
    
    @Test
    @DisplayName("DateTimeFormatter deve formatar com locale")
    void formatarComLocale() {
        LocalDateTime dataHora = LocalDateTime.of(2024, 6, 15, 14, 30);
        DateTimeFormatter formato = DateTimeFormatter.ofPattern(
            "EEEE, d 'de' MMMM 'de' yyyy",
            new Locale("pt", "BR")
        );
        
        String formatado = dataHora.format(formato);
        
        assertTrue(formatado.contains("2024"));
        assertTrue(formatado.toLowerCase().contains("junho"));
    }
    
    // ============================================
    // Testes de Exemplos Práticos
    // ============================================
    
    @Test
    @DisplayName("Deve verificar se é horário comercial")
    void horarioComercial() {
        LocalTime hora = LocalTime.of(14, 0);
        
        boolean comercial = !hora.isBefore(LocalTime.of(9, 0)) && 
                           !hora.isAfter(LocalTime.of(18, 0));
        
        assertTrue(comercial);
    }
    
    @Test
    @DisplayName("Deve calcular idade")
    void calcularIdade() {
        LocalDate nascimento = LocalDate.of(1990, 5, 15);
        LocalDate hoje = LocalDate.of(2024, 6, 15);
        
        int idade = Period.between(nascimento, hoje).getYears();
        
        assertEquals(34, idade);
    }
    
    @Test
    @DisplayName("Deve calcular dias até aniversário")
    void diasAteAniversario() {
        LocalDate hoje = LocalDate.of(2024, 1, 1);
        LocalDate nascimento = LocalDate.of(1990, 6, 15);
        
        LocalDate proximoAniversario = nascimento.withYear(hoje.getYear());
        if (proximoAniversario.isBefore(hoje) || proximoAniversario.isEqual(hoje)) {
            proximoAniversario = proximoAniversario.plusYears(1);
        }
        
        long dias = ChronoUnit.DAYS.between(hoje, proximoAniversario);
        
        assertTrue(dias > 0);
        assertEquals(166, dias); // De 01/01 até 15/06 (2024 é bissexto)
    }
    
    @Test
    @DisplayName("Deve encontrar último dia útil do mês")
    void ultimoDiaUtil() {
        LocalDate hoje = LocalDate.of(2024, 6, 15);
        LocalDate ultimoDia = hoje.with(TemporalAdjusters.lastDayOfMonth());
        
        // Voltar até encontrar dia útil
        while (ultimoDia.getDayOfWeek() == DayOfWeek.SATURDAY || 
               ultimoDia.getDayOfWeek() == DayOfWeek.SUNDAY) {
            ultimoDia = ultimoDia.minusDays(1);
        }
        
        assertNotEquals(DayOfWeek.SATURDAY, ultimoDia.getDayOfWeek());
        assertNotEquals(DayOfWeek.SUNDAY, ultimoDia.getDayOfWeek());
    }
    
    @Test
    @DisplayName("Deve converter entre legado e novo")
    void conversaoLegado() {
        // Legado -> Novo
        java.util.Date legado = new java.util.Date();
        Instant instant = legado.toInstant();
        LocalDateTime novo = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        
        assertNotNull(novo);
        
        // Novo -> Legado
        Instant deNovo = novo.atZone(ZoneId.systemDefault()).toInstant();
        java.util.Date paraLegado = java.util.Date.from(deNovo);
        
        assertNotNull(paraLegado);
    }
    
    @Test
    @DisplayName("LocalDate deve ser imutável - mês começa em 1")
    void mesComecaEm1() {
        // No java.time, mês começa em 1 (Janeiro)
        LocalDate data = LocalDate.of(2024, 1, 1);
        
        assertEquals(Month.JANUARY, data.getMonth());
        assertEquals(1, data.getMonthValue());
    }
}
