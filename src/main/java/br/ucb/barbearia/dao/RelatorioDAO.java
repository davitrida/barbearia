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
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO do Relatório.
 * Consulta os agendamentos (rotina principal) de um período, com JOIN
 * para mostrar os nomes, e filtro opcional por status.
 */
public class RelatorioDAO {

    public List<Agendamento> listarPorPeriodo(LocalDate inicio, LocalDate fim, String status)
            throws SQLException {

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
                   + "WHERE a.data_agendamento BETWEEN ? AND ? ";

        // Filtro de status só entra no SQL se foi escolhido um status específico
        if (status != null) {
            sql += "AND a.status = ? ";
        }
        sql += "ORDER BY a.data_agendamento, a.hora";

        List<Agendamento> lista = new ArrayList<>();

        try (Connection con = ConexaoBD.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setObject(1, inicio);
            ps.setObject(2, fim);
            if (status != null) {
                ps.setString(3, status);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(montarAgendamento(rs));
                }
            }
        }

        return lista;
    }

    private Agendamento montarAgendamento(ResultSet rs) throws SQLException {
        Cliente cliente = new Cliente(
            rs.getInt("id_cliente"), rs.getString("nome_cliente"), null, null);

        Profissional profissional = new Profissional(
            rs.getInt("id_profissional"), rs.getString("nome_profissional"), null, null, true);

        Servico servico = new Servico(
            rs.getInt("id_servico"), rs.getString("descricao_servico"), null, 0);

        FormaPagamento forma = null;
        int idForma = rs.getInt("id_forma_pagamento");
        if (!rs.wasNull()) {
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
