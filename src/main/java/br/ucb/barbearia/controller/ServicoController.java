package br.ucb.barbearia.controller;

import br.ucb.barbearia.dao.ServicoDAO;
import br.ucb.barbearia.model.Servico;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

/**
 * Controller do Serviço.
 * Valida os dados (inclusive converte os textos digitados em números),
 * chama o DAO e traduz os erros do banco para o usuário.
 */
public class ServicoController {

    private static final int ERRO_FK_EM_USO = 1451;

    // Maior valor que cabe em DECIMAL(8,2): 6 dígitos antes da vírgula
    private static final BigDecimal PRECO_MAXIMO = new BigDecimal("999999.99");

    private final ServicoDAO dao = new ServicoDAO();

    // C - salvar
    public void salvar(String descricao, String preco, String duracao) throws Exception {
        Servico servico = validarEMontar(0, descricao, preco, duracao);

        try {
            dao.inserir(servico);
        } catch (SQLException e) {
            throw new Exception("Erro ao salvar o serviço: " + e.getMessage());
        }
    }

    // R - listar
    public List<Servico> listar() throws Exception {
        try {
            return dao.listar();
        } catch (SQLException e) {
            throw new Exception("Erro ao listar os serviços: " + e.getMessage());
        }
    }

    // U - alterar
    public void alterar(int idServico, String descricao, String preco, String duracao) throws Exception {
        if (idServico <= 0) {
            throw new Exception("Selecione um serviço na tabela para alterar.");
        }

        Servico servico = validarEMontar(idServico, descricao, preco, duracao);

        try {
            dao.alterar(servico);
        } catch (SQLException e) {
            throw new Exception("Erro ao alterar o serviço: " + e.getMessage());
        }
    }

    // D - excluir
    public void excluir(int idServico) throws Exception {
        if (idServico <= 0) {
            throw new Exception("Selecione um serviço na tabela para excluir.");
        }

        try {
            dao.excluir(idServico);
        } catch (SQLException e) {
            if (e.getErrorCode() == ERRO_FK_EM_USO) {
                throw new Exception("Este serviço já foi usado em agendamentos e não pode ser excluído.");
            }
            throw new Exception("Erro ao excluir o serviço: " + e.getMessage());
        }
    }

    // ------------------------------------------------------------
    // Validações
    // ------------------------------------------------------------
    private Servico validarEMontar(int idServico, String descricao, String preco, String duracao)
            throws Exception {

        descricao = (descricao == null) ? "" : descricao.trim();
        if (descricao.isEmpty()) {
            throw new Exception("A descrição é obrigatória.");
        }
        if (descricao.length() > 80) {
            throw new Exception("A descrição pode ter no máximo 80 caracteres.");
        }

        BigDecimal valorPreco = converterPreco(preco);
        int valorDuracao = converterDuracao(duracao);

        return new Servico(idServico, descricao, valorPreco, valorDuracao);
    }

    /** Converte o texto digitado (ex.: "40", "40,00" ou "40.50") em BigDecimal. */
    private BigDecimal converterPreco(String preco) throws Exception {
        preco = (preco == null) ? "" : preco.trim().replace(",", ".");
        if (preco.isEmpty()) {
            throw new Exception("O preço é obrigatório.");
        }

        BigDecimal valor;
        try {
            valor = new BigDecimal(preco);
        } catch (NumberFormatException e) {
            throw new Exception("Preço inválido. Use apenas números, por exemplo: 40,00");
        }

        if (valor.compareTo(BigDecimal.ZERO) < 0) {
            throw new Exception("O preço não pode ser negativo.");
        }
        if (valor.scale() > 2) {
            throw new Exception("O preço pode ter no máximo 2 casas decimais.");
        }
        if (valor.compareTo(PRECO_MAXIMO) > 0) {
            throw new Exception("O preço informado é alto demais.");
        }

        return valor;
    }

    /** Converte o texto digitado em número inteiro de minutos. */
    private int converterDuracao(String duracao) throws Exception {
        duracao = (duracao == null) ? "" : duracao.trim();
        if (duracao.isEmpty()) {
            throw new Exception("A duração é obrigatória.");
        }

        int valor;
        try {
            valor = Integer.parseInt(duracao);
        } catch (NumberFormatException e) {
            throw new Exception("Duração inválida. Informe os minutos como número inteiro, por exemplo: 30");
        }

        if (valor <= 0) {
            throw new Exception("A duração deve ser maior que zero.");
        }
        if (valor > 600) {
            throw new Exception("A duração pode ser de no máximo 600 minutos.");
        }

        return valor;
    }
}
