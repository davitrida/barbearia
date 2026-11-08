package br.ucb.barbearia.dao;

import br.ucb.barbearia.model.Cliente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO (Data Access Object) do Cliente.
 * É a ÚNICA classe que executa SQL na tabela "cliente".
 */
public class ClienteDAO {

    // ------------------------------------------------------------
    // C - CREATE (inserir)
    // ------------------------------------------------------------
    public void inserir(Cliente cliente) throws SQLException {
        String sql = "INSERT INTO cliente (nome, telefone, email) VALUES (?, ?, ?)";

        try (Connection con = ConexaoBD.getConexao();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, cliente.getNome());
            ps.setString(2, cliente.getTelefone());
            ps.setString(3, cliente.getEmail());

            ps.executeUpdate();

            // Pega o id que o AUTO_INCREMENT gerou e guarda no objeto
            try (ResultSet chaves = ps.getGeneratedKeys()) {
                if (chaves.next()) {
                    cliente.setIdCliente(chaves.getInt(1));
                }
            }
        }
    }

    // ------------------------------------------------------------
    // R - READ (listar)
    // ------------------------------------------------------------
    public List<Cliente> listar() throws SQLException {
        String sql = "SELECT id_cliente, nome, telefone, email FROM cliente ORDER BY nome";
        List<Cliente> clientes = new ArrayList<>();

        try (Connection con = ConexaoBD.getConexao();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Cliente cliente = new Cliente(
                    rs.getInt("id_cliente"),
                    rs.getString("nome"),
                    rs.getString("telefone"),
                    rs.getString("email")
                );
                clientes.add(cliente);
            }
        }

        return clientes;
    }

    // ------------------------------------------------------------
    // U - UPDATE (alterar)
    // ------------------------------------------------------------
    public void alterar(Cliente cliente) throws SQLException {
        String sql = "UPDATE cliente SET nome = ?, telefone = ?, email = ? WHERE id_cliente = ?";

        try (Connection con = ConexaoBD.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, cliente.getNome());
            ps.setString(2, cliente.getTelefone());
            ps.setString(3, cliente.getEmail());
            ps.setInt(4, cliente.getIdCliente());

            ps.executeUpdate();
        }
    }

    // ------------------------------------------------------------
    // D - DELETE (excluir)
    // ------------------------------------------------------------
    public void excluir(int idCliente) throws SQLException {
        String sql = "DELETE FROM cliente WHERE id_cliente = ?";

        try (Connection con = ConexaoBD.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idCliente);

            ps.executeUpdate();
        }
    }
}
