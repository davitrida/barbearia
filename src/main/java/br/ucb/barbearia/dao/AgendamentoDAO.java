package br.ucb.barbearia.dao;

import br.ucb.barbearia.model.Agendamento;
import br.ucb.barbearia.model.Cliente;
import br.ucb.barbearia.model.FormaPagamento;
import br.ucb.barbearia.model.Profissional;
import br.ucb.barbearia.model.Servico;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO do Agendamento.
 * É a ÚNICA classe que executa SQL na tabela "agendamento".
 */
public class AgendamentoDAO {

    // ------------------------------------------------------------
    // C - CREATE
    // ------------------------------------------------------------
    public void inserir(Agendamento a) throws SQLException {
        String sql = "INSERT INTO agendamento (id_cliente, id_profissional, id_servico, "
                   + "id_forma_pagamento, data_agendamento, hora, valor, status, observacao) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = ConexaoBD.getConexao();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            preencherParametros(ps, a);
            ps.executeUpdate();

            try (ResultSet chaves = ps.getGeneratedKeys()) {
                if (chaves.next()) {
                    a.setIdAgendamento(chaves.getInt(1));
                }
            }
        }
    }

    // ------------------------------------------------------------
    // R - READ: CONSULTA COM JOIN
    // Junta o agendamento com cliente, profissional, serviço e forma de
    // pagamento para mostrar NOMES em vez de códigos.
    // LEFT JOIN no pagamento: agendamentos ainda não pagos (NULL) também aparecem.
    // ------------------------------------------------------------
    public List<Agendamento> listar() throws SQLException {
        String sql = "SELECT a.id_agendamento, a.data_agendamento, a.hora, a.valor, "
                   + "       a.status, a.observacao, "
                   + "       c.id_cliente,         c.nome      AS nome_cliente, "
                   + "       p.id_profissional,    p.nome      AS nome_profissional, "
                   + "       s.id_servico,         s.descricao AS descricao_servico, "
                   + "       f.id_forma_pagamento, f.descricao AS descricao_pagamento "
                   + "FROM agendamento a "
                   + "INNER JOIN cliente      c ON c.id_cliente      = a.id_cliente "
                   + "INNER JOIN profissional p ON p.id_profissional = a.id_profissional "
                   + "INNER JOIN servico      s ON s.id_servico      = a.id_servico "
                   + "LEFT  JOIN forma_pagamento f ON f.id_forma_pagamento = a.id_forma_pagamento "
                   + "ORDER BY a.data_agendamento, a.hora";

        List<Agendamento> agendamentos = new ArrayList<>();

        try (Connection con = ConexaoBD.getConexao();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                agendamentos.add(montarAgendamento(rs));
            }
        }

        return agendamentos;
    }

    // ------------------------------------------------------------
    // U - UPDATE
    // ------------------------------------------------------------
    public void alterar(Agendamento a) throws SQLException {
        String sql = "UPDATE agendamento SET id_cliente = ?, id_profissional = ?, id_servico = ?, "
                   + "id_forma_pagamento = ?, data_agendamento = ?, hora = ?, valor = ?, "
                   + "status = ?, observacao = ? WHERE id_agendamento = ?";

        try (Connection con = ConexaoBD.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {

            preencherParametros(ps, a);
            ps.setInt(10, a.getIdAgendamento());
            ps.executeUpdate();
        }
    }

    // ------------------------------------------------------------
    // D - DELETE
    // ------------------------------------------------------------
    public void excluir(int idAgendamento) throws SQLException {
        String sql = "DELETE FROM agendamento WHERE id_agendamento = ?";

        try (Connection con = ConexaoBD.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idAgendamento);
            ps.executeUpdate();
        }
    }

    // ------------------------------------------------------------
    // Usado pela REGRA DE NEGÓCIO do Controller:
    // existe outro agendamento ativo do mesmo profissional, no mesmo dia e hora?
    // ------------------------------------------------------------
    public boolean existeConflito(int idProfissional, LocalDate data, LocalTime hora,
                                  int idIgnorar) throws SQLException {
        String sql = "SELECT COUNT(*) FROM agendamento "
                   + "WHERE id_profissional = ? AND data_agendamento = ? AND hora = ? "
                   + "AND status <> 'CANCELADO' AND id_agendamento <> ?";

        try (Connection con = ConexaoBD.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idProfissional);
            ps.setObject(2, data);
            ps.setObject(3, hora);
            ps.setInt(4, idIgnorar);   // ao alterar, ignora o próprio registro

            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1) > 0;
            }
        }
    }

    // ------------------------------------------------------------
    // Auxiliares
    // ------------------------------------------------------------

    /** Preenche os 9 primeiros "?" do INSERT e do UPDATE (estão na mesma ordem). */
    private void preencherParametros(PreparedStatement ps, Agendamento a) throws SQLException {
        ps.setInt(1, a.getCliente().getIdCliente());
        ps.setInt(2, a.getProfissional().getIdProfissional());
        ps.setInt(3, a.getServico().getIdServico());

        if (a.getFormaPagamento() == null) {
            ps.setNull(4, Types.INTEGER);          // ainda não pago
        } else {
            ps.setInt(4, a.getFormaPagamento().getIdFormaPagamento());
        }

        ps.setObject(5, a.getData());
        ps.setObject(6, a.getHora());
        ps.setBigDecimal(7, a.getValor());
        ps.setString(8, a.getStatus());
        ps.setString(9, a.getObservacao());
    }

    /** Transforma uma linha do JOIN num objeto Agendamento com os objetos relacionados. */
    private Agendamento montarAgendamento(ResultSet rs) throws SQLException {
        Cliente cliente = new Cliente(
            rs.getInt("id_cliente"), rs.getString("nome_cliente"), null, null);

        Profissional profissional = new Profissional(
            rs.getInt("id_profissional"), rs.getString("nome_profissional"), null, null, true);

        Servico servico = new Servico(
            rs.getInt("id_servico"), rs.getString("descricao_servico"), null, 0);

        FormaPagamento forma = null;
        int idForma = rs.getInt("id_forma_pagamento");
        if (!rs.wasNull()) {                       // só cria se não for NULL
            forma = new FormaPagamento(idForma, rs.getString("descricao_pagamento"));
        }

        return new Agendamento(
            rs.getInt("id_agendamento"),
            cliente,
            profissional,
            servico,
            forma,
            rs.getObject("data_agendamento", LocalDate.class),
            rs.getObject("hora", LocalTime.class),
            rs.getBigDecimal("valor"),
            rs.getString("status"),
            rs.getString("observacao")
        );
    }
}
