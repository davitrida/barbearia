package br.ucb.barbearia.dao;

import br.ucb.barbearia.model.Profissional;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO do Profissional.
 * É a ÚNICA classe que executa SQL na tabela "profissional".
 */
public class ProfissionalDAO {

    // C - CREATE
    public void inserir(Profissional p) throws SQLException {
        String sql = "INSERT INTO profissional (nome, telefone, especialidade, ativo) VALUES (?, ?, ?, ?)";

        try (Connection con = ConexaoBD.getConexao();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, p.getNome());
            ps.setString(2, p.getTelefone());
            ps.setString(3, p.getEspecialidade());
            ps.setBoolean(4, p.isAtivo());
            ps.executeUpdate();

            try (ResultSet chaves = ps.getGeneratedKeys()) {
                if (chaves.next()) {
                    p.setIdProfissional(chaves.getInt(1));
                }
            }
        }
    }

    // R - READ (todos, para a tela de cadastro)
    public List<Profissional> listar() throws SQLException {
        String sql = "SELECT id_profissional, nome, telefone, especialidade, ativo "
                   + "FROM profissional ORDER BY nome";
        return executarConsulta(sql);
    }

    // R - READ (só os ativos, para o combo da tela de Agendamento)
    public List<Profissional> listarAtivos() throws SQLException {
        String sql = "SELECT id_profissional, nome, telefone, especialidade, ativo "
                   + "FROM profissional WHERE ativo = TRUE ORDER BY nome";
        return executarConsulta(sql);
    }

    // U - UPDATE
    public void alterar(Profissional p) throws SQLException {
        String sql = "UPDATE profissional SET nome = ?, telefone = ?, especialidade = ?, ativo = ? "
                   + "WHERE id_profissional = ?";

        try (Connection con = ConexaoBD.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, p.getNome());
            ps.setString(2, p.getTelefone());
            ps.setString(3, p.getEspecialidade());
            ps.setBoolean(4, p.isAtivo());
            ps.setInt(5, p.getIdProfissional());
            ps.executeUpdate();
        }
    }

    // D - DELETE
    public void excluir(int idProfissional) throws SQLException {
        String sql = "DELETE FROM profissional WHERE id_profissional = ?";

        try (Connection con = ConexaoBD.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idProfissional);
            ps.executeUpdate();
        }
    }

    // Executa um SELECT e transforma cada linha num objeto Profissional
    private List<Profissional> executarConsulta(String sql) throws SQLException {
        List<Profissional> profissionais = new ArrayList<>();

        try (Connection con = ConexaoBD.getConexao();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                profissionais.add(new Profissional(
                    rs.getInt("id_profissional"),
                    rs.getString("nome"),
                    rs.getString("telefone"),
                    rs.getString("especialidade"),
                    rs.getBoolean("ativo")
                ));
            }
        }

        return profissionais;
    }
}
