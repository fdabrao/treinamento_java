package com.avanade.curso.nio;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

/**
 * Testes para NIO.2 (New I/O)
 */
class NIOTest {
    
    private Path tempDir;
    
    @BeforeEach
    void setUp() throws IOException {
        tempDir = Files.createTempDirectory("nio-test-");
    }
    
    @AfterEach
    void tearDown() throws IOException {
        // Limpar diretório temporário
        if (Files.exists(tempDir)) {
            try (Stream<Path> stream = Files.walk(tempDir)) {
                stream.sorted(Comparator.reverseOrder())
                      .forEach(path -> {
                          try {
                              Files.deleteIfExists(path);
                          } catch (IOException e) {
                              // Ignora
                          }
                      });
            }
        }
    }
    
    // ============================================
    // Testes de Path
    // ============================================
    
    @Test
    @DisplayName("Path deve extrair componentes")
    void pathComponentes() {
        Path path = Paths.get("/home/usuario/documentos/arquivo.txt");
        
        assertEquals("arquivo.txt", path.getFileName().toString());
        assertEquals("/home/usuario/documentos", path.getParent().toString());
        assertEquals("/", path.getRoot().toString());
        assertEquals(4, path.getNameCount());
        assertEquals("home", path.getName(0).toString());
    }
    
    @Test
    @DisplayName("Path deve relativizar caminhos")
    void pathRelativizar() {
        Path base = Paths.get("/home/usuario");
        Path completo = Paths.get("/home/usuario/documentos/arquivo.txt");
        
        Path relativo = base.relativize(completo);
        
        assertEquals("documentos/arquivo.txt", relativo.toString());
    }
    
    @Test
    @DisplayName("Path deve resolver caminhos")
    void pathResolver() {
        Path base = Paths.get("/home/usuario");
        Path resolvido = base.resolve("projetos/codigo");
        
        assertEquals("/home/usuario/projetos/codigo", resolvido.toString());
    }
    
    @Test
    @DisplayName("Path deve normalizar caminhos")
    void pathNormalizar() {
        Path complicado = Paths.get("/home/usuario/../usuario/./documentos");
        Path normalizado = complicado.normalize();
        
        assertEquals("/home/usuario/documentos", normalizado.toString());
    }
    
    @Test
    @DisplayName("Path deve verificar se é absoluto")
    void pathAbsoluto() {
        Path absoluto = Paths.get("/home/usuario");
        Path relativo = Paths.get("documentos/arquivo.txt");
        
        assertTrue(absoluto.isAbsolute());
        assertFalse(relativo.isAbsolute());
    }
    
    // ============================================
    // Testes de Operações Básicas
    // ============================================
    
    @Test
    @DisplayName("Files deve criar diretório")
    void criarDiretorio() throws IOException {
        Path novoDir = tempDir.resolve("novo-diretorio");
        
        Files.createDirectories(novoDir);
        
        assertTrue(Files.exists(novoDir));
        assertTrue(Files.isDirectory(novoDir));
    }
    
    @Test
    @DisplayName("Files deve criar arquivo")
    void criarArquivo() throws IOException {
        Path arquivo = tempDir.resolve("teste.txt");
        
        Files.createFile(arquivo);
        
        assertTrue(Files.exists(arquivo));
        assertTrue(Files.isRegularFile(arquivo));
    }
    
    @Test
    @DisplayName("Files deve verificar propriedades")
    void verificarPropriedades() throws IOException {
        Path arquivo = tempDir.resolve("teste.txt");
        Files.createFile(arquivo);
        
        assertTrue(Files.isRegularFile(arquivo));
        assertTrue(Files.isReadable(arquivo));
        assertTrue(Files.isWritable(arquivo));
        assertFalse(Files.isDirectory(arquivo));
        assertFalse(Files.isHidden(arquivo));
    }
    
    @Test
    @DisplayName("Files deve copiar arquivo")
    void copiarArquivo() throws IOException {
        Path original = tempDir.resolve("original.txt");
        Path copia = tempDir.resolve("copia.txt");
        Files.createFile(original);
        Files.writeString(original, "Conteúdo");
        
        Files.copy(original, copia);
        
        assertTrue(Files.exists(copia));
        assertEquals("Conteúdo", Files.readString(copia));
    }
    
    @Test
    @DisplayName("Files deve mover arquivo")
    void moverArquivo() throws IOException {
        Path original = tempDir.resolve("original.txt");
        Path movido = tempDir.resolve("movido.txt");
        Files.createFile(original);
        
        Files.move(original, movido);
        
        assertFalse(Files.exists(original));
        assertTrue(Files.exists(movido));
    }
    
    @Test
    @DisplayName("Files deve deletar arquivo")
    void deletarArquivo() throws IOException {
        Path arquivo = tempDir.resolve("deletar.txt");
        Files.createFile(arquivo);
        assertTrue(Files.exists(arquivo));
        
        Files.delete(arquivo);
        
        assertFalse(Files.exists(arquivo));
    }
    
    @Test
    @DisplayName("Files deve deletar se existir")
    void deletarSeExistir() throws IOException {
        Path arquivo = tempDir.resolve("deletar.txt");
        Files.createFile(arquivo);
        
        Files.deleteIfExists(arquivo);
        
        assertFalse(Files.exists(arquivo));
        assertDoesNotThrow(() -> Files.deleteIfExists(arquivo)); // Não lança exceção
    }
    
    // ============================================
    // Testes de Leitura e Escrita
    // ============================================
    
    @Test
    @DisplayName("Files deve escrever e ler string")
    void escreverLerString() throws IOException {
        Path arquivo = tempDir.resolve("dados.txt");
        String conteudo = "Olá, NIO!";
        
        Files.writeString(arquivo, conteudo);
        String lido = Files.readString(arquivo);
        
        assertEquals(conteudo, lido);
    }
    
    @Test
    @DisplayName("Files deve escrever e ler bytes")
    void escreverLerBytes() throws IOException {
        Path arquivo = tempDir.resolve("bytes.bin");
        byte[] dados = "Teste".getBytes(StandardCharsets.UTF_8);
        
        Files.write(arquivo, dados);
        byte[] lidos = Files.readAllBytes(arquivo);
        
        assertArrayEquals(dados, lidos);
    }
    
    @Test
    @DisplayName("Files deve ler todas as linhas")
    void lerTodasLinhas() throws IOException {
        Path arquivo = tempDir.resolve("linhas.txt");
        Files.writeString(arquivo, "Linha 1\nLinha 2\nLinha 3\n");
        
        List<String> linhas = Files.readAllLines(arquivo);
        
        assertEquals(3, linhas.size());
        assertEquals("Linha 1", linhas.get(0));
        assertEquals("Linha 2", linhas.get(1));
        assertEquals("Linha 3", linhas.get(2));
    }
    
    @Test
    @DisplayName("Files deve ler linhas como stream")
    void lerLinhasStream() throws IOException {
        Path arquivo = tempDir.resolve("linhas.txt");
        Files.writeString(arquivo, "A\nB\nC\n");
        
        try (Stream<String> stream = Files.lines(arquivo)) {
            long count = stream.count();
            assertEquals(3, count);
        }
    }
    
    @Test
    @DisplayName("Files deve adicionar ao arquivo")
    void appendArquivo() throws IOException {
        Path arquivo = tempDir.resolve("append.txt");
        Files.writeString(arquivo, "Primeira linha\n");
        Files.writeString(arquivo, "Segunda linha\n", StandardOpenOption.APPEND);
        
        String conteudo = Files.readString(arquivo);
        assertTrue(conteudo.contains("Primeira linha"));
        assertTrue(conteudo.contains("Segunda linha"));
    }
    
    // ============================================
    // Testes de Navegação
    // ============================================
    
    @Test
    @DisplayName("Files deve listar diretório")
    void listarDiretorio() throws IOException {
        Path dir = tempDir.resolve("listar");
        Files.createDirectory(dir);
        Files.createFile(dir.resolve("arquivo1.txt"));
        Files.createFile(dir.resolve("arquivo2.txt"));
        Files.createDirectory(dir.resolve("subdiretorio"));
        
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
            int count = 0;
            for (Path entry : stream) {
                count++;
            }
            assertEquals(3, count);
        }
    }
    
    @Test
    @DisplayName("Files deve listar com filtro glob")
    void listarComFiltro() throws IOException {
        Path dir = tempDir.resolve("filtro");
        Files.createDirectory(dir);
        Files.createFile(dir.resolve("teste.txt"));
        Files.createFile(dir.resolve("teste.java"));
        Files.createFile(dir.resolve("outro.txt"));
        
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir, "*.txt")) {
            int count = 0;
            for (Path entry : stream) {
                count++;
                assertTrue(entry.toString().endsWith(".txt"));
            }
            assertEquals(2, count);
        }
    }
    
    @Test
    @DisplayName("Files deve percorrer recursivamente")
    void walkRecursivo() throws IOException {
        Path raiz = tempDir.resolve("walk");
        Files.createDirectories(raiz.resolve("pasta1/subpasta"));
        Files.createDirectories(raiz.resolve("pasta2"));
        Files.createFile(raiz.resolve("arquivo1.txt"));
        Files.createFile(raiz.resolve("pasta1/arquivo2.txt"));
        Files.createFile(raiz.resolve("pasta1/subpasta/arquivo3.txt"));
        
        try (Stream<Path> stream = Files.walk(raiz)) {
            long count = stream.count();
            assertTrue(count >= 6); // 3 diretórios + 3 arquivos
        }
    }
    
    @Test
    @DisplayName("Files deve encontrar arquivos com filtro")
    void findArquivos() throws IOException {
        Path raiz = tempDir.resolve("find");
        Files.createDirectories(raiz.resolve("pasta"));
        Files.createFile(raiz.resolve("arquivo.txt"));
        Files.createFile(raiz.resolve("pasta/outro.txt"));
        Files.createFile(raiz.resolve("arquivo.java"));
        
        try (Stream<Path> stream = Files.find(raiz, 10, 
                (path, attrs) -> path.toString().endsWith(".txt"))) {
            long count = stream.count();
            assertEquals(2, count);
        }
    }
    
    // ============================================
    // Testes de Arquivos Temporários
    // ============================================
    
    @Test
    @DisplayName("Files deve criar arquivo temporário")
    void arquivoTemporario() throws IOException {
        Path tempFile = Files.createTempFile("teste-", ".tmp");
        
        assertTrue(Files.exists(tempFile));
        assertTrue(tempFile.getFileName().toString().startsWith("teste-"));
        assertTrue(tempFile.getFileName().toString().endsWith(".tmp"));
        
        Files.deleteIfExists(tempFile);
    }
    
    @Test
    @DisplayName("Files deve criar diretório temporário")
    void diretorioTemporario() throws IOException {
        Path tempDir = Files.createTempDirectory("meu-app-");
        
        assertTrue(Files.exists(tempDir));
        assertTrue(Files.isDirectory(tempDir));
        assertTrue(tempDir.getFileName().toString().startsWith("meu-app-"));
        
        Files.deleteIfExists(tempDir);
    }
    
    // ============================================
    // Testes de Links
    // ============================================
    
    @Test
    @DisplayName("Files deve verificar se é link simbólico")
    void verificarLink() throws IOException {
        Path arquivo = tempDir.resolve("original.txt");
        Files.createFile(arquivo);
        
        assertFalse(Files.isSymbolicLink(arquivo));
    }
    
    @Test
    @DisplayName("Files deve verificar se é diretório")
    void verificarDiretorio() throws IOException {
        Path dir = tempDir.resolve("teste-dir");
        Path file = tempDir.resolve("teste-file.txt");
        Files.createDirectory(dir);
        Files.createFile(file);
        
        assertTrue(Files.isDirectory(dir));
        assertFalse(Files.isDirectory(file));
    }
    
    // ============================================
    // Testes de Tamanho e Timestamps
    // ============================================
    
    @Test
    @DisplayName("Files deve obter tamanho do arquivo")
    void obterTamanho() throws IOException {
        Path arquivo = tempDir.resolve("tamanho.txt");
        String conteudo = "Conteúdo de teste";
        Files.writeString(arquivo, conteudo);
        
        long tamanho = Files.size(arquivo);
        
        assertEquals(conteudo.getBytes(StandardCharsets.UTF_8).length, tamanho);
    }
    
    @Test
    @DisplayName("Files deve obter última modificação")
    void obterModificacao() throws IOException {
        Path arquivo = tempDir.resolve("modificacao.txt");
        Files.createFile(arquivo);
        
        var lastModified = Files.getLastModifiedTime(arquivo);
        
        assertNotNull(lastModified);
    }
}
