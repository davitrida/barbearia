package br.ucb.barbearia;

import br.ucb.barbearia.dao.ConexaoBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Classe TEMPORÁRIA, só para testar a conexão com o banco.
 * Pode ser apagada depois que a conexão estiver funcionando.
 */
public class TesteConexao {

    public static void main(String[] args) {
        String sql = "SELECT COUNT(*) AS total FROM cliente";

        try (Connection con = ConexaoBD.getConexao();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.println("Conectado ao banco com sucesso!");

            if (rs.next()) {
                int total = rs.getInt("total");
                System.out.println("Clientes cadastrados: " + total);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao conectar: " + e.getMessage());
        }
    }
}
