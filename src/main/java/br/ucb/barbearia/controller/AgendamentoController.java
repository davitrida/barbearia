package br.ucb.barbearia.controller;

import br.ucb.barbearia.dao.AgendamentoDAO;
import br.ucb.barbearia.dao.ClienteDAO;
import br.ucb.barbearia.dao.FormaPagamentoDAO;
import br.ucb.barbearia.dao.ProfissionalDAO;
import br.ucb.barbearia.dao.ServicoDAO;
import br.ucb.barbearia.model.Agendamento;
import br.ucb.barbearia.model.Cliente;
import br.ucb.barbearia.model.FormaPagamento;
import br.ucb.barbearia.model.Profissional;
import br.ucb.barbearia.model.Servico;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.List;

/**
 * Controller do Agendamento: onde ficam as REGRAS DE NEGÓCIO da rotina principal.
 *
 * Regras:
 *  1. Cliente, profissional e serviço são obrigatórios.
 *  2. Data e hora precisam ser válidas.
 *  3. Agendamento com status AGENDADO não pode ser para data/hora que já passou.
 *  4. O mesmo profissional não pode ter dois agendamentos ativos no mesmo dia e hora.
 *  5. Para marcar como CONCLUIDO, a forma de pagamento é obrigatória.
 *  6. O valor vem do preço do serviço e fica "congelado" no agendamento.
 */
public class AgendamentoController {

    public static final DateTimeFormatter FORMATO_DATA =
            DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);
    public static final DateTimeFormatter FORMATO_HORA =
            DateTimeFormatter.ofPattern("HH:mm").withResolverStyle(ResolverStyle.STRICT);

    private final AgendamentoDAO dao = new AgendamentoDAO();
    private final ClienteDAO clienteDAO = new ClienteDAO();
    private final ProfissionalDAO profissionalDAO = new ProfissionalDAO();
    private final ServicoDAO servicoDAO = new ServicoDAO();
    private final FormaPagamentoDAO formaPagamentoDAO = new FormaPagamentoDAO();

    // ------------------------------------------------------------
    // Listas para preencher os combos da tela
    // ------------------------------------------------------------
    public List<Cliente> listarClientes() throws Exception {
        try {
            return clienteDAO.listar();
        } catch (SQLException e) {
            throw new Exception("Erro ao carregar os clientes: " + e.getMessage());
        }
    }

    public List<Profissional> listarProfissionais() throws Exception {
        try {
            return profissionalDAO.listarAtivos();   // só quem está ativo pode ser agendado
        } catch (SQLException e) {
            throw new Exception("Erro ao carregar os profissionais: " + e.getMessage());
        }
    }

    public List<Servico> listarServicos() throws Exception {
        try {
            return servicoDAO.listar();
        } catch (SQLException e) {
            throw new Exception("Erro ao carregar os serviços: " + e.getMessage());
        }
    }

    public List<FormaPagamento> listarFormasPagamento() throws Exception {
        try {
            return formaPagamentoDAO.listar();
        } catch (SQLException e) {
            throw new Exception("Erro ao carregar as formas de pagamento: " + e.getMessage());
        }
    }

    // ------------------------------------------------------------
    // CRUD
    // ------------------------------------------------------------
    public void salvar(Cliente cliente, Profissional profissional, Servico servico,
                       FormaPagamento forma, String data, String hora, String valor,
                       String status, String observacao) throws Exception {

        Agendamento a = validarEMontar(0, cliente, profissional, servico, forma,
                data, hora, valor, status, observacao);
        try {
            dao.inserir(a);
        } catch (SQLException e) {
            throw new Exception("Erro ao salvar o agendamento: " + e.getMessage());
        }
    }

    public List<Agendamento> listar() throws Exception {
        try {
            return dao.listar();
        } catch (SQLException e) {
            throw new Exception("Erro ao listar os agendamentos: " + e.getMessage());
        }
    }

    public void alterar(int idAgendamento, Cliente cliente, Profissional profissional,
                        Servico servico, FormaPagamento forma, String data, String hora,
                        String valor, String status, String observacao) throws Exception {

        if (idAgendamento <= 0) {
            throw new Exception("Selecione um agendamento na tabela para alterar.");
        }

        Agendamento a = validarEMontar(idAgendamento, cliente, profissional, servico, forma,
                data, hora, valor, status, observacao);
        try {
            dao.alterar(a);
        } catch (SQLException e) {
            throw new Exception("Erro ao alterar o agendamento: " + e.getMessage());
        }
    }

    public void excluir(int idAgendamento) throws Exception {
        if (idAgendamento <= 0) {
            throw new Exception("Selecione um agendamento na tabela para excluir.");
        }
        try {
            dao.excluir(idAgendamento);
        } catch (SQLException e) {
            throw new Exception("Erro ao excluir o agendamento: " + e.getMessage());
        }
    }

    // ------------------------------------------------------------
    // REGRAS DE NEGÓCIO
    // ------------------------------------------------------------
    private Agendamento validarEMontar(int id, Cliente cliente, Profissional profissional,
                                       Servico servico, FormaPagamento forma, String data,
                                       String hora, String valor, String status,
                                       String observacao) throws Exception {
        // Regra 1 - campos obrigatórios
        if (cliente == null) {
            throw new Exception("Selecione o cliente.");
        }
        if (profissional == null) {
            throw new Exception("Selecione o profissional.");
        }
        if (servico == null) {
            throw new Exception("Selecione o serviço.");
        }
        if (status == null) {
            status = Agendamento.AGENDADO;
        }

        // Regra 2 - data e hora válidas
        LocalDate dataConvertida = converterData(data);
        LocalTime horaConvertida = converterHora(hora);

        // Regra 3 - não agendar no passado
        if (Agendamento.AGENDADO.equals(status)
                && LocalDateTime.of(dataConvertida, horaConvertida).isBefore(LocalDateTime.now())) {
            throw new Exception("Não é possível deixar como AGENDADO um horário que já passou.\n"
                    + "Escolha uma data futura, ou mude o status para CONCLUIDO ou CANCELADO.");
        }

        // Regra 5 - concluído exige forma de pagamento
        if (Agendamento.CONCLUIDO.equals(status) && forma == null) {
            throw new Exception("Para marcar como CONCLUIDO, informe a forma de pagamento.");
        }

        // Regra 6 - valor (vem do serviço; se o campo estiver vazio, usa o preço atual)
        BigDecimal valorConvertido = converterValor(valor, servico);

        // Observação opcional
        observacao = (observacao == null) ? null : observacao.trim();
        if (observacao != null && observacao.isEmpty()) {
            observacao = null;
        }
        if (observacao != null && observacao.length() > 255) {
            throw new Exception("A observação pode ter no máximo 255 caracteres.");
        }

        // Regra 4 - conflito de horário (cancelados não contam)
        if (!Agendamento.CANCELADO.equals(status)) {
            try {
                if (dao.existeConflito(profissional.getIdProfissional(),
                        dataConvertida, horaConvertida, id)) {
                    throw new Exception(profissional.getNome()
                            + " já tem um agendamento em " + data.trim() + " às " + hora.trim() + ".");
                }
            } catch (SQLException e) {
                throw new Exception("Erro ao verificar a agenda do profissional: " + e.getMessage());
            }
        }

        return new Agendamento(id, cliente, profissional, servico, forma,
                dataConvertida, horaConvertida, valorConvertido, status, observacao);
    }

    private LocalDate converterData(String data) throws Exception {
        if (data == null || data.trim().isEmpty()) {
            throw new Exception("A data é obrigatória.");
        }
        try {
            return LocalDate.parse(data.trim(), FORMATO_DATA);
        } catch (DateTimeParseException e) {
            throw new Exception("Data inválida. Use o formato dd/mm/aaaa, por exemplo: 20/11/2026");
        }
    }

    private LocalTime converterHora(String hora) throws Exception {
        if (hora == null || hora.trim().isEmpty()) {
            throw new Exception("A hora é obrigatória.");
        }
        try {
            return LocalTime.parse(hora.trim(), FORMATO_HORA);
        } catch (DateTimeParseException e) {
            throw new Exception("Hora inválida. Use o formato hh:mm, por exemplo: 09:30");
        }
    }

    private BigDecimal converterValor(String valor, Servico servico) throws Exception {
        if (valor == null || valor.trim().isEmpty()) {
            return servico.getPreco();
        }
        try {
            BigDecimal v = new BigDecimal(valor.trim().replace(",", "."));
            if (v.compareTo(BigDecimal.ZERO) < 0) {
                throw new Exception("O valor não pode ser negativo.");
            }
            return v;
        } catch (NumberFormatException e) {
            throw new Exception("Valor inválido.");
        }
    }
}
