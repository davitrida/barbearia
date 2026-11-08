package br.ucb.barbearia.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Responsável por abrir conexões com o banco de dados MySQL.
 *
 * Os dados de acesso (URL, usuário e senha) são lidos do arquivo
 * db.properties, que fica em src/main/resources e não é versionado.
 */
public class ConexaoBD {

    private static final String ARQUIVO_CONFIG = "/db.properties";

    private static final String URL;
    private static final String USUARIO;
    private static final String SENHA;

    // Bloco estático: roda uma única vez, quando a classe é usada pela primeira vez
    static {
        Properties config = new Properties();

        try (InputStream entrada = ConexaoBD.class.getResourceAsStream(ARQUIVO_CONFIG)) {
            if (entrada == null) {
                throw new IllegalStateException(
                    "Arquivo db.properties não encontrado em src/main/resources. "
                    + "Copie o db.properties.exemplo e preencha sua senha.");
            }
            config.load(entrada);
        } catch (IOException e) {
            throw new IllegalStateException("Erro ao ler o arquivo db.properties.", e);
        }

        URL = config.getProperty("db.url");
        USUARIO = config.getProperty("db.usuario");
        SENHA = config.getProperty("db.senha");
    }

    // Construtor privado: ninguém precisa criar objetos desta classe
    private ConexaoBD() {
    }

    /**
     * Abre e devolve uma nova conexão com o banco.
     * Quem chama é responsável por fechá-la (de preferência com try-with-resources).
     */
    public static Connection getConexao() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, SENHA);
    }
}
