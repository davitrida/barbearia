package br.ucb.barbearia.controller;

import br.ucb.barbearia.dao.FormaPagamentoDAO;
import br.ucb.barbearia.model.FormaPagamento;
import java.sql.SQLException;
import java.util.List;

/**
 * Controller da Forma de Pagamento.
 * Valida os dados, chama o DAO e traduz os erros do banco para o usuário.
 */
public class FormaPagamentoController {

    // Códigos de erro do MySQL
    private static final int ERRO_DUPLICADO = 1062;  // violou UNIQUE
    private static final int ERRO_FK_EM_USO = 1451;  // registro usado por chave estrangeira

    private final FormaPagamentoDAO dao = new FormaPagamentoDAO();

    // C - salvar
    public void salvar(String descricao) throws Exception {
        FormaPagamento forma = validarEMontar(0, descricao);

        try {
            dao.inserir(forma);
        } catch (SQLException e) {
            throw traduzirErro(e, "salvar");
        }
    }

    // R - listar
    public List<FormaPagamento> listar() throws Exception {
        try {
            return dao.listar();
        } catch (SQLException e) {
            throw new Exception("Erro ao listar as formas de pagamento: " + e.getMessage());
        }
    }

    // U - alterar
    public void alterar(int idFormaPagamento, String descricao) throws Exception {
        if (idFormaPagamento <= 0) {
            throw new Exception("Selecione uma forma de pagamento na tabela para alterar.");
        }

        FormaPagamento forma = validarEMontar(idFormaPagamento, descricao);

        try {
            dao.alterar(forma);
        } catch (SQLException e) {
            throw traduzirErro(e, "alterar");
        }
    }

    // D - excluir
    public void excluir(int idFormaPagamento) throws Exception {
        if (idFormaPagamento <= 0) {
            throw new Exception("Selecione uma forma de pagamento na tabela para excluir.");
        }

        try {
            dao.excluir(idFormaPagamento);
        } catch (SQLException e) {
            throw traduzirErro(e, "excluir");
        }
    }

    // ------------------------------------------------------------
    // Validações
    // ------------------------------------------------------------
    private FormaPagamento validarEMontar(int idFormaPagamento, String descricao) throws Exception {
        descricao = (descricao == null) ? "" : descricao.trim();

        if (descricao.isEmpty()) {
            throw new Exception("A descrição é obrigatória.");
        }
        if (descricao.length() > 40) {
            throw new Exception("A descrição pode ter no máximo 40 caracteres.");
        }

        return new FormaPagamento(idFormaPagamento, descricao);
    }

    /** Transforma o erro técnico do banco numa mensagem que o usuário entende. */
    private Exception traduzirErro(SQLException e, String acao) {
        if (e.getErrorCode() == ERRO_DUPLICADO) {
            return new Exception("Já existe uma forma de pagamento com essa descrição.");
        }
        if (e.getErrorCode() == ERRO_FK_EM_USO) {
            return new Exception("Esta forma de pagamento já foi usada em agendamentos e não pode ser excluída.");
        }
        return new Exception("Erro ao " + acao + " a forma de pagamento: " + e.getMessage());
    }
}
