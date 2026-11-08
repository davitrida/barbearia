package br.ucb.barbearia.dao;

import br.ucb.barbearia.model.FormaPagamento;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO da Forma de Pagamento.
 * É a ÚNICA classe que executa SQL na tabela "forma_pagamento".
 */
public class FormaPagamentoDAO {

    // C - CREATE
    public void inserir(FormaPagamento forma) throws SQLException {
        String sql = "INSERT INTO forma_pagamento (descricao) VALUES (?)";

        try (Connection con = ConexaoBD.getConexao();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, forma.getDescricao());
            ps.executeUpdate();

            try (ResultSet chaves = ps.getGeneratedKeys()) {
                if (chaves.next()) {
                    forma.setIdFormaPagamento(chaves.getInt(1));
                }
            }
        }
    }

    // R - READ
    public List<FormaPagamento> listar() throws SQLException {
        String sql = "SELECT id_forma_pagamento, descricao FROM forma_pagamento ORDER BY descricao";
        List<FormaPagamento> formas = new ArrayList<>();

        try (Connection con = ConexaoBD.getConexao();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                formas.add(new FormaPagamento(
                    rs.getInt("id_forma_pagamento"),
                    rs.getString("descricao")
                ));
            }
        }

        return formas;
    }

    // U - UPDATE
    public void alterar(FormaPagamento forma) throws SQLException {
        String sql = "UPDATE forma_pagamento SET descricao = ? WHERE id_forma_pagamento = ?";

        try (Connection con = ConexaoBD.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, forma.getDescricao());
            ps.setInt(2, forma.getIdFormaPagamento());
            ps.executeUpdate();
        }
    }

    // D - DELETE
    public void excluir(int idFormaPagamento) throws SQLException {
        String sql = "DELETE FROM forma_pagamento WHERE id_forma_pagamento = ?";

        try (Connection con = ConexaoBD.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idFormaPagamento);
            ps.executeUpdate();
        }
    }
}
