package com.avanade.curso.nio;

import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.nio.file.attribute.*;
import java.util.*;
import java.util.stream.*;

/**
 * JAVA NIO.2 (New I/O) - API Moderna de Arquivos
 * 
 * Introduzida no Java 7 (java.nio.file).
 * Substitui as APIs legadas (java.io.File).
 * 
 * Principais classes:
 * - Path: Representa um caminho no sistema de arquivos
 * - Paths: Factory para criar Path
 * - Files: Utilitários para operações em arquivos
 * - FileSystem: Representa o sistema de arquivos
 * 
 * Vantagens sobre File:
 * - Melhor tratamento de exceções
 * - Suporte a links simbólicos
 * - Melhor performance em operações recursivas
 * - WatchService para monitorar mudanças
 * - Suporte a atributos de arquivo avançados
 */

public class NIOExample {
    
    // ============================================
    // PATH - Representação de caminhos
    // ============================================
    
    public void demonstrarPath() {
        // Criar Path
        Path path1 = Paths.get("/home/usuario/documentos/arquivo.txt");
        Path path2 = Path.of("/home/usuario/documentos/arquivo.txt"); // Java 11+
        
        System.out.println("Path: " + path1);
        System.out.println("File name: " + path1.getFileName());
        System.out.println("Parent: " + path1.getParent());
        System.out.println("Root: " + path1.getRoot());
        System.out.println("Name count: " + path1.getNameCount());
        
        // Navegar pelo path
        for (int i = 0; i < path1.getNameCount(); i++) {
            System.out.println("  Name[" + i + "]: " + path1.getName(i));
        }
        
        // Relativizar
        Path base = Paths.get("/home/usuario");
        Path relativo = base.relativize(path1);
        System.out.println("Relativo: " + relativo);
        
        // Resolver (juntar)
        Path completo = base.resolve("projetos/codigo");
        System.out.println("Resolvido: " + completo);
        
        // Normalizar
        Path complicado = Paths.get("/home/usuario/../usuario/./documentos");
        Path normalizado = complicado.normalize();
        System.out.println("Normalizado: " + normalizado);
        
        // Verificações
        System.out.println("É absoluto? " + path1.isAbsolute());
        System.out.println("Termina com .txt? " + path1.endsWith(".txt"));
    }
    
    // ============================================
    // OPERAÇÕES BÁSICAS COM FILES
    // ============================================
    
    public void demonstrarOperacoesBasicas() throws IOException {
        Path tempDir = Paths.get(System.getProperty("java.io.tmpdir"), "nio-teste");
        
        // Criar diretório
        if (!Files.exists(tempDir)) {
            Files.createDirectories(tempDir);
            System.out.println("Diretório criado: " + tempDir);
        }
        
        // Criar arquivo
        Path arquivo = tempDir.resolve("teste.txt");
        if (!Files.exists(arquivo)) {
            Files.createFile(arquivo);
            System.out.println("Arquivo criado: " + arquivo);
        }
        
        // Verificar existência e propriedades
        System.out.println("Existe? " + Files.exists(arquivo));
        System.out.println("É arquivo? " + Files.isRegularFile(arquivo));
        System.out.println("É diretório? " + Files.isDirectory(arquivo));
        System.out.println("É legível? " + Files.isReadable(arquivo));
        System.out.println("É gravável? " + Files.isWritable(arquivo));
        System.out.println("É executável? " + Files.isExecutable(arquivo));
        System.out.println("É oculto? " + Files.isHidden(arquivo));
        
        // Tamanho e timestamps
        System.out.println("Tamanho: " + Files.size(arquivo) + " bytes");
        System.out.println("Última modificação: " + Files.getLastModifiedTime(arquivo));
        
        // Copiar
        Path copia = tempDir.resolve("teste-copia.txt");
        Files.copy(arquivo, copia, StandardCopyOption.REPLACE_EXISTING);
        System.out.println("Arquivo copiado para: " + copia);
        
        // Mover
        Path movido = tempDir.resolve("teste-movido.txt");
        Files.move(copia, movido, StandardCopyOption.REPLACE_EXISTING);
        System.out.println("Arquivo movido para: " + movido);
        
        // Deletar
        Files.deleteIfExists(movido);
        System.out.println("Arquivo deletado");
    }
    
    // ============================================
    // LEITURA E ESCRITA
    // ============================================
    
    public void demonstrarLeituraEscrita() throws IOException {
        Path tempDir = Paths.get(System.getProperty("java.io.tmpdir"), "nio-teste");
        Files.createDirectories(tempDir);
        
        Path arquivo = tempDir.resolve("dados.txt");
        
        // Escrever texto
        String conteudo = "Linha 1\nLinha 2\nLinha 3\n";
        Files.writeString(arquivo, conteudo); // Java 11+
        System.out.println("Arquivo escrito");
        
        // Ler texto completo
        String lido = Files.readString(arquivo);
        System.out.println("Conteúdo lido:\n" + lido);
        
        // Escrever bytes
        byte[] bytes = "Texto em bytes".getBytes(StandardCharsets.UTF_8);
        Files.write(arquivo, bytes);
        
        // Ler bytes
        byte[] lidos = Files.readAllBytes(arquivo);
        System.out.println("Bytes lidos: " + new String(lidos, StandardCharsets.UTF_8));
        
        // Ler todas as linhas
        List<String> linhas = Files.readAllLines(arquivo, StandardCharsets.UTF_8);
        System.out.println("Linhas: " + linhas);
        
        // Ler com Stream (melhor para arquivos grandes)
        try (Stream<String> stream = Files.lines(arquivo)) {
            long count = stream.count();
            System.out.println("Total de linhas: " + count);
        }
        
        // Processar linhas
        try (Stream<String> stream = Files.lines(arquivo)) {
            stream.filter(line -> !line.isEmpty())
                  .map(String::toUpperCase)
                  .forEach(System.out::println);
        }
        
        // Adicionar ao arquivo (append)
        Files.writeString(arquivo, "Linha adicionada\n", 
            StandardOpenOption.APPEND);
        System.out.println("Texto adicionado");
    }
    
    // ============================================
    // NAVEGAÇÃO DE DIRETÓRIOS
    // ============================================
    
    public void demonstrarNavegacao() throws IOException {
        Path tempDir = Paths.get(System.getProperty("java.io.tmpdir"), "nio-teste");
        
        // Criar estrutura de teste
        Files.createDirectories(tempDir.resolve("pasta1/subpasta"));
        Files.createDirectories(tempDir.resolve("pasta2"));
        Files.createFile(tempDir.resolve("arquivo1.txt"));
        Files.createFile(tempDir.resolve("pasta1/arquivo2.txt"));
        Files.createFile(tempDir.resolve("pasta1/subpasta/arquivo3.txt"));
        
        // Listar conteúdo de diretório
        System.out.println("Conteúdo de " + tempDir + ":");
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(tempDir)) {
            for (Path entry : stream) {
                System.out.println("  " + (Files.isDirectory(entry) ? "[D] " : "[A] ") + entry.getFileName());
            }
        }
        
        // Listar com filtro glob
        System.out.println("\nArquivos .txt:");
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(tempDir, "*.txt")) {
            for (Path entry : stream) {
                System.out.println("  " + entry.getFileName());
            }
        }
        
        // Walk (percorrer recursivamente)
        System.out.println("\nÁrvore completa:");
        try (Stream<Path> stream = Files.walk(tempDir)) {
            stream.forEach(path -> {
                int depth = tempDir.relativize(path).getNameCount();
                String indent = "  ".repeat(depth);
                System.out.println(indent + path.getFileName());
            });
        }
        
        // Find (buscar com filtro)
        System.out.println("\nBuscando arquivos .txt:");
        try (Stream<Path> stream = Files.find(tempDir, 10, 
                (path, attrs) -> path.toString().endsWith(".txt"))) {
            stream.forEach(System.out::println);
        }
        
        // Contar arquivos
        long totalArquivos = Files.walk(tempDir)
            .filter(Files::isRegularFile)
            .count();
        System.out.println("\nTotal de arquivos: " + totalArquivos);
    }
    
    // ============================================
    // TEMPORARY FILES
    // ============================================
    
    public void demonstrarArquivosTemporarios() throws IOException {
        // Criar arquivo temporário
        Path tempFile = Files.createTempFile("prefixo-", ".tmp");
        System.out.println("Arquivo temporário: " + tempFile);
        
        Files.writeString(tempFile, "Dados temporários");
        
        // Usar e deletar
        String conteudo = Files.readString(tempFile);
        System.out.println("Conteúdo: " + conteudo);
        
        Files.deleteIfExists(tempFile);
        System.out.println("Arquivo temporário deletado");
        
        // Diretório temporário
        Path tempDir = Files.createTempDirectory("meu-app-");
        System.out.println("Diretório temporário: " + tempDir);
        Files.deleteIfExists(tempDir);
    }
    
    // ============================================
    // TRY-WITH-RESOURCES MELHORADO
    // ============================================
    
    public void demonstrarTryWithResources() {
        Path arquivo = Paths.get(System.getProperty("java.io.tmpdir"), "nio-teste", "dados.txt");
        
        // BufferedReader com Path
        try (BufferedReader reader = Files.newBufferedReader(arquivo)) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                System.out.println("Lido: " + linha);
            }
        } catch (IOException e) {
            System.err.println("Erro na leitura: " + e.getMessage());
        }
        
        // BufferedWriter com Path
        Path output = arquivo.getParent().resolve("output.txt");
        try (BufferedWriter writer = Files.newBufferedWriter(output)) {
            writer.write("Linha 1");
            writer.newLine();
            writer.write("Linha 2");
        } catch (IOException e) {
            System.err.println("Erro na escrita: " + e.getMessage());
        }
    }
    
    // ============================================
    // BOAS PRÁTICAS
    // ============================================
    
    public void boasPraticas() throws IOException {
        //  Usar Path em vez de String/File
        Path path = Paths.get("/caminho/para/arquivo.txt");
        
        //  Verificar existência antes de operações críticas
        if (Files.exists(path)) {
            // Operação segura
        }
        
        //  Usar try-with-resources para streams
        try (Stream<Path> stream = Files.walk(path.getParent())) {
            // Processar
        }
        
        //  Preferir métodos atômicos
        Files.move(path, path.resolveSibling("novo-nome.txt"), 
            StandardCopyOption.ATOMIC_MOVE);
        
        //  Usar StandardCharsets
        Files.readString(path, StandardCharsets.UTF_8);
    }
    
    // ============================================
    // LIMPEZA
    // ============================================
    
    public void limparEstruturaTeste() throws IOException {
        Path tempDir = Paths.get(System.getProperty("java.io.tmpdir"), "nio-teste");
        
        if (Files.exists(tempDir)) {
            // Deletar recursivamente
            try (Stream<Path> stream = Files.walk(tempDir)) {
                stream.sorted(Comparator.reverseOrder())
                      .forEach(path -> {
                          try {
                              Files.deleteIfExists(path);
                          } catch (IOException e) {
                              System.err.println("Erro ao deletar " + path + ": " + e.getMessage());
                          }
                      });
            }
            System.out.println("\nEstrutura de teste limpa");
        }
    }
    
    public static void main(String[] args) throws Exception {
        NIOExample example = new NIOExample();
        
        System.out.println("=== PATH ===");
        example.demonstrarPath();
        
        System.out.println("\n=== OPERAÇÕES BÁSICAS ===");
        example.demonstrarOperacoesBasicas();
        
        System.out.println("\n=== LEITURA E ESCRITA ===");
        example.demonstrarLeituraEscrita();
        
        System.out.println("\n=== NAVEGAÇÃO ===");
        example.demonstrarNavegacao();
        
        System.out.println("\n=== ARQUIVOS TEMPORÁRIOS ===");
        example.demonstrarArquivosTemporarios();
        
        System.out.println("\n=== TRY-WITH-RESOURCES ===");
        example.demonstrarTryWithResources();
        
        example.limparEstruturaTeste();
    }
}
