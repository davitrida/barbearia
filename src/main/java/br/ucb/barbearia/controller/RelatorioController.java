package br.ucb.barbearia.controller;

import br.ucb.barbearia.dao.RelatorioDAO;
import br.ucb.barbearia.model.Agendamento;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Controller do Relatório de atendimentos por período.
 * Valida o período, busca os dados e calcula os totais do resumo.
 */
public class RelatorioController {

    public static final String TODOS = "TODOS";

    private final RelatorioDAO dao = new RelatorioDAO();

    /** Busca os agendamentos do período; status "TODOS" = sem filtro. */
    public List<Agendamento> gerar(String dataInicial, String dataFinal, String status)
            throws Exception {

        LocalDate inicio = converterData(dataInicial, "inicial");
        LocalDate fim = converterData(dataFinal, "final");

        if (fim.isBefore(inicio)) {
            throw new Exception("A data final não pode ser anterior à data inicial.");
        }

        String filtroStatus = (status == null || TODOS.equals(status)) ? null : status;

        try {
            return dao.listarPorPeriodo(inicio, fim, filtroStatus);
        } catch (SQLException e) {
            throw new Exception("Erro ao gerar o relatório: " + e.getMessage());
        }
    }

    /** Quantos agendamentos da lista têm o status informado. */
    public int contarPorStatus(List<Agendamento> lista, String status) {
        int total = 0;
        for (Agendamento a : lista) {
            if (a.getStatus().equals(status)) {
                total++;
            }
        }
        return total;
    }

    /** Soma o valor dos atendimentos CONCLUIDOS (o que de fato foi faturado). */
    public BigDecimal somarFaturado(List<Agendamento> lista) {
        BigDecimal total = BigDecimal.ZERO;
        for (Agendamento a : lista) {
            if (Agendamento.CONCLUIDO.equals(a.getStatus())) {
                total = total.add(a.getValor());
            }
        }
        return total;
    }

    private LocalDate converterData(String data, String qual) throws Exception {
        if (data == null || data.trim().isEmpty()) {
            throw new Exception("Informe a data " + qual + ".");
        }
        try {
            return LocalDate.parse(data.trim(), AgendamentoController.FORMATO_DATA);
        } catch (DateTimeParseException e) {
            throw new Exception("Data " + qual + " inválida. Use o formato dd/mm/aaaa.");
        }
    }
}
