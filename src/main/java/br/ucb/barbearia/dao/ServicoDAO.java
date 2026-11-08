package br.ucb.barbearia.dao;

import br.ucb.barbearia.model.Servico;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO do Serviço.
 * É a ÚNICA classe que executa SQL na tabela "servico".
 */
public class ServicoDAO {

    // C - CREATE
    public void inserir(Servico servico) throws SQLException {
        String sql = "INSERT INTO servico (descricao, preco, duracao_min) VALUES (?, ?, ?)";

        try (Connection con = ConexaoBD.getConexao();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, servico.getDescricao());
            ps.setBigDecimal(2, servico.getPreco());
            ps.setInt(3, servico.getDuracaoMin());
            ps.executeUpdate();

            try (ResultSet chaves = ps.getGeneratedKeys()) {
                if (chaves.next()) {
                    servico.setIdServico(chaves.getInt(1));
                }
            }
        }
    }

    // R - READ
    public List<Servico> listar() throws SQLException {
        String sql = "SELECT id_servico, descricao, preco, duracao_min FROM servico ORDER BY descricao";
        List<Servico> servicos = new ArrayList<>();

        try (Connection con = ConexaoBD.getConexao();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                servicos.add(new Servico(
                    rs.getInt("id_servico"),
                    rs.getString("descricao"),
                    rs.getBigDecimal("preco"),
                    rs.getInt("duracao_min")
                ));
            }
        }

        return servicos;
    }

    // U - UPDATE
    public void alterar(Servico servico) throws SQLException {
        String sql = "UPDATE servico SET descricao = ?, preco = ?, duracao_min = ? WHERE id_servico = ?";

        try (Connection con = ConexaoBD.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, servico.getDescricao());
            ps.setBigDecimal(2, servico.getPreco());
            ps.setInt(3, servico.getDuracaoMin());
            ps.setInt(4, servico.getIdServico());
            ps.executeUpdate();
        }
    }

    // D - DELETE
    public void excluir(int idServico) throws SQLException {
        String sql = "DELETE FROM servico WHERE id_servico = ?";

        try (Connection con = ConexaoBD.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idServico);
            ps.executeUpdate();
        }
    }
}
