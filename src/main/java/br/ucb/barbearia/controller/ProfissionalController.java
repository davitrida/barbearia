package br.ucb.barbearia.controller;

import br.ucb.barbearia.dao.ProfissionalDAO;
import br.ucb.barbearia.model.Profissional;
import java.sql.SQLException;
import java.util.List;

/**
 * Controller do Profissional.
 * Valida os dados, chama o DAO e traduz os erros do banco para o usuário.
 */
public class ProfissionalController {

    private static final int ERRO_FK_EM_USO = 1451;

    private final ProfissionalDAO dao = new ProfissionalDAO();

    // C - salvar
    public void salvar(String nome, String telefone, String especialidade, boolean ativo)
            throws Exception {
        Profissional p = validarEMontar(0, nome, telefone, especialidade, ativo);

        try {
            dao.inserir(p);
        } catch (SQLException e) {
            throw new Exception("Erro ao salvar o profissional: " + e.getMessage());
        }
    }

    // R - listar todos
    public List<Profissional> listar() throws Exception {
        try {
            return dao.listar();
        } catch (SQLException e) {
            throw new Exception("Erro ao listar os profissionais: " + e.getMessage());
        }
    }

    // R - listar só os ativos (usado no Agendamento)
    public List<Profissional> listarAtivos() throws Exception {
        try {
            return dao.listarAtivos();
        } catch (SQLException e) {
            throw new Exception("Erro ao listar os profissionais ativos: " + e.getMessage());
        }
    }

    // U - alterar
    public void alterar(int idProfissional, String nome, String telefone,
                        String especialidade, boolean ativo) throws Exception {
        if (idProfissional <= 0) {
            throw new Exception("Selecione um profissional na tabela para alterar.");
        }

        Profissional p = validarEMontar(idProfissional, nome, telefone, especialidade, ativo);

        try {
            dao.alterar(p);
        } catch (SQLException e) {
            throw new Exception("Erro ao alterar o profissional: " + e.getMessage());
        }
    }

    // D - excluir
    public void excluir(int idProfissional) throws Exception {
        if (idProfissional <= 0) {
            throw new Exception("Selecione um profissional na tabela para excluir.");
        }

        try {
            dao.excluir(idProfissional);
        } catch (SQLException e) {
            if (e.getErrorCode() == ERRO_FK_EM_USO) {
                throw new Exception("Este profissional possui agendamentos e não pode ser excluído.\n"
                        + "Se ele não trabalha mais na barbearia, desmarque \"Ativo\" e clique em Alterar.");
            }
            throw new Exception("Erro ao excluir o profissional: " + e.getMessage());
        }
    }

    // ------------------------------------------------------------
    // Validações
    // ------------------------------------------------------------
    private Profissional validarEMontar(int id, String nome, String telefone,
                                        String especialidade, boolean ativo) throws Exception {
        nome = limpar(nome);
        telefone = limpar(telefone);
        especialidade = limpar(especialidade);

        if (nome == null) {
            throw new Exception("O nome é obrigatório.");
        }
        if (nome.length() > 100) {
            throw new Exception("O nome pode ter no máximo 100 caracteres.");
        }
        if (telefone != null && telefone.length() > 20) {
            throw new Exception("O telefone pode ter no máximo 20 caracteres.");
        }
        if (especialidade != null && especialidade.length() > 60) {
            throw new Exception("A especialidade pode ter no máximo 60 caracteres.");
        }

        return new Profissional(id, nome, telefone, especialidade, ativo);
    }

    /** Tira espaços das pontas; se sobrar texto vazio, devolve null. */
    private String limpar(String texto) {
        if (texto == null) {
            return null;
        }
        texto = texto.trim();
        return texto.isEmpty() ? null : texto;
    }
}
