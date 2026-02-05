package com.avanade.curso.time;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Locale;
import java.util.Set;

/**
 * JAVA TIME API (java.time)
 * 
 * Introduzido no Java 8 para substituir as APIs legadas (Date, Calendar).
 * Imutável, thread-safe e muito mais fácil de usar.
 * 
 * Principais classes:
 * - LocalDate: Data (ano, mês, dia) sem fuso horário
 * - LocalTime: Horário (hora, minuto, segundo) sem fuso horário
 * - LocalDateTime: Data + Horário sem fuso horário
 * - ZonedDateTime: Data + Horário com fuso horário
 * - Instant: Timestamp em UTC (milissegundos desde 1970)
 * - Duration: Diferença entre dois tempos (horas, minutos, segundos)
 * - Period: Diferença entre duas datas (anos, meses, dias)
 * - DateTimeFormatter: Formatação e parsing de datas
 */

public class TimeExample {
    
    // ============================================
    // LOCALDATE - Data sem horário
    // ============================================
    
    public void demonstrarLocalDate() {
        // Data atual
        LocalDate hoje = LocalDate.now();
        System.out.println("Hoje: " + hoje);
        
        // Criar data específica
        LocalDate natal = LocalDate.of(2024, 12, 25);
        LocalDate anoNovo = LocalDate.parse("2025-01-01");
        
        System.out.println("Natal: " + natal);
        System.out.println("Ano Novo: " + anoNovo);
        
        // Extrair componentes
        System.out.println("Ano: " + hoje.getYear());
        System.out.println("Mês: " + hoje.getMonth()); // retorna DECEMBER (inglês)
        System.out.println("Mês (número): " + hoje.getMonthValue()); // 12
        System.out.println("Dia: " + hoje.getDayOfMonth());
        System.out.println("Dia da semana: " + hoje.getDayOfWeek()); // retorna THURSDAY (inglês)
        System.out.println("Dia do ano: " + hoje.getDayOfYear());
        
        // Manipulação (imutável - retorna nova instância)
        LocalDate amanha = hoje.plusDays(1);
        LocalDate mesPassado = hoje.minusMonths(1);
        LocalDate proximoAno = hoje.plusYears(1);
        
        System.out.println("Amanhã: " + amanha);
        System.out.println("Mês passado: " + mesPassado);
        System.out.println("Próximo ano: " + proximoAno);
        
        // Comparar datas
        System.out.println("Hoje é antes do Natal? " + hoje.isBefore(natal));
        System.out.println("Hoje é depois do Natal? " + hoje.isAfter(natal));
        System.out.println("É ano bissexto? " + hoje.isLeapYear());
        
        // Ajustes temporais
        LocalDate primeiroDiaDoMes = hoje.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate ultimoDiaDoMes = hoje.with(TemporalAdjusters.lastDayOfMonth());
        LocalDate proximaSegunda = hoje.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
        
        System.out.println("Primeiro dia do mês: " + primeiroDiaDoMes);
        System.out.println("Último dia do mês: " + ultimoDiaDoMes);
        System.out.println("Próxima segunda: " + proximaSegunda);
    }
    
    // ============================================
    // LOCALTIME - Horário sem data
    // ============================================
    
    public void demonstrarLocalTime() {
        // Horário atual
        LocalTime agora = LocalTime.now();
        System.out.println("Agora: " + agora);
        
        // Criar horário específico
        LocalTime almoco = LocalTime.of(12, 30);
        LocalTime meiaNoite = LocalTime.parse("00:00:00");
        LocalTime comSegundos = LocalTime.of(14, 30, 45, 123456789);
        
        System.out.println("Almoço: " + almoco);
        System.out.println("Meia-noite: " + meiaNoite);
        
        // Extrair componentes
        System.out.println("Hora: " + agora.getHour());
        System.out.println("Minuto: " + agora.getMinute());
        System.out.println("Segundo: " + agora.getSecond());
        System.out.println("Nano: " + agora.getNano());
        
        // Manipulação
        LocalTime daquiUmaHora = agora.plusHours(1);
        LocalTime daqui30Minutos = agora.plusMinutes(30);
        LocalTime horaAnterior = agora.minusHours(2);
        
        System.out.println("Daqui 1 hora: " + daquiUmaHora);
        System.out.println("Daqui 30 min: " + daqui30Minutos);
        
        // Comparar
        System.out.println("É meia-noite? " + agora.equals(LocalTime.MIDNIGHT));
        System.out.println("É meio-dia? " + agora.equals(LocalTime.NOON));
    }
    
    // ============================================
    // LOCALDATETIME - Data + Horário
    // ============================================
    
    public void demonstrarLocalDateTime() {
        // Data/hora atual
        LocalDateTime agora = LocalDateTime.now();
        System.out.println("Agora: " + agora);
        
        // Criar específico
        LocalDateTime reuniao = LocalDateTime.of(2024, 12, 25, 14, 30);
        LocalDateTime parseado = LocalDateTime.parse("2024-12-25T14:30:00");
        
        System.out.println("Reunião: " + reuniao);
        
        // Extrair LocalDate e LocalTime
        LocalDate data = agora.toLocalDate();
        LocalTime hora = agora.toLocalTime();
        
        System.out.println("Data: " + data);
        System.out.println("Hora: " + hora);
        
        // Combinar
        LocalDateTime combinado = data.atTime(hora);
        System.out.println("Combinado: " + combinado);
        
        // Manipulação
        LocalDateTime daquiUmaSemana = agora.plusWeeks(1);
        LocalDateTime inicioDoDia = agora.with(LocalTime.MIN);
        LocalDateTime fimDoDia = agora.with(LocalTime.MAX);
        
        System.out.println("Daqui uma semana: " + daquiUmaSemana);
        System.out.println("Início do dia: " + inicioDoDia);
        System.out.println("Fim do dia: " + fimDoDia);
    }
    
    // ============================================
    // ZONEDDATETIME - Com fuso horário
    // ============================================
    
    public void demonstrarZonedDateTime() {
        // Data/hora com fuso horário
        ZonedDateTime agoraSP = ZonedDateTime.now(ZoneId.of("America/Sao_Paulo"));
        ZonedDateTime agoraNY = ZonedDateTime.now(ZoneId.of("America/New_York"));
        ZonedDateTime agoraLondres = ZonedDateTime.now(ZoneId.of("Europe/London"));
        ZonedDateTime agoraToquio = ZonedDateTime.now(ZoneId.of("Asia/Tokyo"));
        
        System.out.println("São Paulo: " + agoraSP);
        System.out.println("New York: " + agoraNY);
        System.out.println("Londres: " + agoraLondres);
        System.out.println("Tóquio: " + agoraToquio);
        
        // Zonas disponíveis
        Set<String> zonas = ZoneId.getAvailableZoneIds();
        System.out.println("\nTotal de zonas: " + zonas.size());
        zonas.stream()
            .filter(z -> z.contains("America"))
            .limit(5)
            .forEach(System.out::println);
        
        // Converter entre fusos
        ZonedDateTime reuniaoSP = ZonedDateTime.of(
            LocalDateTime.of(2024, 12, 25, 14, 0),
            ZoneId.of("America/Sao_Paulo")
        );
        
        ZonedDateTime reuniaoNY = reuniaoSP.withZoneSameInstant(ZoneId.of("America/New_York"));
        System.out.println("\nReunião em SP: " + reuniaoSP);
        System.out.println("Reunião em NY: " + reuniaoNY);
        
        // Offset (diferença do UTC)
        ZoneOffset offset = agoraSP.getOffset();
        System.out.println("Offset SP: " + offset); // -03:00 ou -02:00 (DST)
    }
    
    // ============================================
    // INSTANT - Timestamp UTC
    // ============================================
    
    public void demonstrarInstant() {
        // Instant representa um ponto no tempo em UTC
        Instant agora = Instant.now();
        System.out.println("Instant agora: " + agora);
        
        // Epoch milli (milissegundos desde 1970-01-01T00:00:00Z)
        long epochMilli = agora.toEpochMilli();
        System.out.println("Epoch milli: " + epochMilli);
        
        // Criar de epoch
        Instant deEpoch = Instant.ofEpochMilli(1700000000000L);
        System.out.println("De epoch: " + deEpoch);
        
        // Manipulação
        Instant daqui5Minutos = agora.plus(5, ChronoUnit.MINUTES);
        Instant ha1Hora = agora.minus(1, ChronoUnit.HOURS);
        
        System.out.println("Daqui 5 min: " + daqui5Minutos);
        System.out.println("Há 1 hora: " + ha1Hora);
        
        // Converter para ZonedDateTime
        ZonedDateTime zdt = agora.atZone(ZoneId.of("America/Sao_Paulo"));
        System.out.println("Em SP: " + zdt);
    }
    
    // ============================================
    // PERIOD vs DURATION
    // ============================================
    
    public void demonstrarPeriodEDuration() {
        // Period - diferença entre datas (anos, meses, dias)
        LocalDate inicio = LocalDate.of(2020, 1, 1);
        LocalDate fim = LocalDate.of(2024, 12, 25);
        
        Period periodo = Period.between(inicio, fim);
        System.out.println("Período: " + periodo);
        System.out.println("Anos: " + periodo.getYears());
        System.out.println("Meses: " + periodo.getMonths());
        System.out.println("Dias: " + periodo.getDays());
        System.out.println("Total meses: " + periodo.toTotalMonths());
        
        // Criar period
        Period doisAnos = Period.ofYears(2);
        Period seisMeses = Period.ofMonths(6);
        Period composto = Period.of(1, 2, 15); // 1 ano, 2 meses, 15 dias
        
        LocalDate futuro = inicio.plus(doisAnos);
        System.out.println("Daqui 2 anos: " + futuro);
        
        // Duration - diferença entre tempos (horas, minutos, segundos, nanos)
        LocalTime hora1 = LocalTime.of(10, 0);
        LocalTime hora2 = LocalTime.of(14, 30, 45);
        
        Duration duracao = Duration.between(hora1, hora2);
        System.out.println("\nDuração: " + duracao);
        System.out.println("Horas: " + duracao.toHours());
        System.out.println("Minutos: " + duracao.toMinutes());
        System.out.println("Segundos: " + duracao.getSeconds());
        
        // Criar duration
        Duration duasHoras = Duration.ofHours(2);
        Duration noventaMinutos = Duration.ofMinutes(90);
        Duration trintaSegundos = Duration.ofSeconds(30);
        
        LocalTime maisTarde = hora1.plus(duasHoras);
        System.out.println("Daqui 2 horas: " + maisTarde);
        
        // ChronoUnit
        long diasEntre = ChronoUnit.DAYS.between(inicio, fim);
        long horasEntre = ChronoUnit.HOURS.between(hora1, hora2);
        
        System.out.println("\nDias entre: " + diasEntre);
        System.out.println("Horas entre: " + horasEntre);
    }
    
    // ============================================
    // FORMATANDO E PARSING
    // ============================================
    
    public void demonstrarFormatacao() {
        LocalDateTime agora = LocalDateTime.now();
        
        // Formatos predefinidos
        String iso = agora.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        String basico = agora.format(DateTimeFormatter.BASIC_ISO_DATE);
        
        System.out.println("ISO: " + iso);
        System.out.println("Básico: " + basico);
        
        // Formatos customizados
        DateTimeFormatter formatoBr = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        DateTimeFormatter formatoUs = DateTimeFormatter.ofPattern("MM-dd-yyyy hh:mm a");
        DateTimeFormatter formatoExtenso = DateTimeFormatter.ofPattern(
            "EEEE, d 'de' MMMM 'de' yyyy", 
            new Locale("pt", "BR")
        );
        
        String dataBr = agora.format(formatoBr);
        String dataUs = agora.format(formatoUs);
        String dataExtenso = agora.format(formatoExtenso);
        
        System.out.println("Formato BR: " + dataBr);
        System.out.println("Formato US: " + dataUs);
        System.out.println("Formato extenso: " + dataExtenso);
        
        // Parsing
        String dataString = "25/12/2024 14:30:00";
        LocalDateTime parseado = LocalDateTime.parse(dataString, formatoBr);
        System.out.println("\nParseado: " + parseado);
        
        // DateTimeFormatterBuilder para formatos complexos
        DateTimeFormatter complexo = new DateTimeFormatterBuilder()
            .appendPattern("dd/MM/yyyy")
            .appendLiteral(" às ")
            .appendPattern("HH:mm")
            .toFormatter(new Locale("pt", "BR"));
        
        System.out.println("Formato complexo: " + agora.format(complexo));
    }
    
    // ============================================
    // COMPARANDO COM LEGADO
    // ============================================
    
    public void compararComLegado() {
        //  Antigo (java.util.Date/Calendar) - mutável, problemático
        java.util.Date dateAntigo = new java.util.Date();
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.set(2024, 11, 25); // Mês começa em 0!
        
        //  Novo (java.time) - imutável, thread-safe, claro
        LocalDate dateNovo = LocalDate.of(2024, 12, 25); // Mês começa em 1!
        
        // Conversão legado -> novo
        java.util.Date legado = new java.util.Date();
        Instant instant = legado.toInstant();
        LocalDateTime novo = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        
        // Conversão novo -> legado (quando necessário)
        Instant deNovo = novo.atZone(ZoneId.systemDefault()).toInstant();
        java.util.Date paraLegado = java.util.Date.from(deNovo);
        
        System.out.println("Legado: " + legado);
        System.out.println("Novo: " + novo);
    }
    
    // ============================================
    // EXEMPLOS PRÁTICOS
    // ============================================
    
    public void exemplosPraticos() {
        // 1. Verificar se é horário comercial
        LocalTime hora = LocalTime.now();
        boolean horarioComercial = !hora.isBefore(LocalTime.of(9, 0)) && 
                                   !hora.isAfter(LocalTime.of(18, 0));
        System.out.println("É horário comercial? " + horarioComercial);
        
        // 2. Calcular idade
        LocalDate nascimento = LocalDate.of(1990, 5, 15);
        LocalDate hoje = LocalDate.now();
        int idade = Period.between(nascimento, hoje).getYears();
        System.out.println("Idade: " + idade);
        
        // 3. Próximo aniversário
        LocalDate proximoAniversario = nascimento.withYear(hoje.getYear());
        if (proximoAniversario.isBefore(hoje) || proximoAniversario.isEqual(hoje)) {
            proximoAniversario = proximoAniversario.plusYears(1);
        }
        long diasAteAniversario = ChronoUnit.DAYS.between(hoje, proximoAniversario);
        System.out.println("Dias até aniversário: " + diasAteAniversario);
        
        // 4. Último dia útil do mês
        LocalDate ultimoDia = hoje.with(TemporalAdjusters.lastDayOfMonth());
        while (ultimoDia.getDayOfWeek() == DayOfWeek.SATURDAY || 
               ultimoDia.getDayOfWeek() == DayOfWeek.SUNDAY) {
            ultimoDia = ultimoDia.minusDays(1);
        }
        System.out.println("Último dia útil: " + ultimoDia);
        
        // 5. Diferença de fusos
        ZonedDateTime sp = ZonedDateTime.now(ZoneId.of("America/Sao_Paulo"));
        ZonedDateTime toquio = ZonedDateTime.now(ZoneId.of("Asia/Tokyo"));
        long horasDiferenca = ChronoUnit.HOURS.between(sp, toquio);
        System.out.println("Diferença SP-Tóquio: " + horasDiferenca + " horas");
    }
    
    public static void main(String[] args) {
        TimeExample example = new TimeExample();
        
        System.out.println("=== LOCALDATE ===");
        example.demonstrarLocalDate();
        
        System.out.println("\n=== LOCALTIME ===");
        example.demonstrarLocalTime();
        
        System.out.println("\n=== LOCALDATETIME ===");
        example.demonstrarLocalDateTime();
        
        System.out.println("\n=== ZONEDDATETIME ===");
        example.demonstrarZonedDateTime();
        
        System.out.println("\n=== INSTANT ===");
        example.demonstrarInstant();
        
        System.out.println("\n=== PERIOD E DURATION ===");
        example.demonstrarPeriodEDuration();
        
        System.out.println("\n=== FORMATAÇÃO ===");
        example.demonstrarFormatacao();
        
        System.out.println("\n=== EXEMPLOS PRÁTICOS ===");
        example.exemplosPraticos();
    }
}
