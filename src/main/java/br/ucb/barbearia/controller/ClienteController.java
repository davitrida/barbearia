package br.ucb.barbearia.controller;

import br.ucb.barbearia.dao.ClienteDAO;
import br.ucb.barbearia.model.Cliente;
import java.sql.SQLException;
import java.util.List;

/**
 * Controller do Cliente.
 * Recebe os dados da tela, valida, monta o objeto Cliente e chama o DAO.
 * Também transforma os erros do banco em mensagens que o usuário entende.
 */
public class ClienteController {

    // Código de erro do MySQL para "registro em uso por uma chave estrangeira"
    private static final int ERRO_FK_EM_USO = 1451;

    private final ClienteDAO dao = new ClienteDAO();

    // ------------------------------------------------------------
    // C - salvar (cadastrar novo)
    // ------------------------------------------------------------
    public void salvar(String nome, String telefone, String email) throws Exception {
        Cliente cliente = validarEMontar(0, nome, telefone, email);

        try {
            dao.inserir(cliente);
        } catch (SQLException e) {
            throw new Exception("Erro ao salvar o cliente: " + e.getMessage());
        }
    }

    // ------------------------------------------------------------
    // R - listar
    // ------------------------------------------------------------
    public List<Cliente> listar() throws Exception {
        try {
            return dao.listar();
        } catch (SQLException e) {
            throw new Exception("Erro ao listar os clientes: " + e.getMessage());
        }
    }

    // ------------------------------------------------------------
    // U - alterar
    // ------------------------------------------------------------
    public void alterar(int idCliente, String nome, String telefone, String email) throws Exception {
        if (idCliente <= 0) {
            throw new Exception("Selecione um cliente na tabela para alterar.");
        }

        Cliente cliente = validarEMontar(idCliente, nome, telefone, email);

        try {
            dao.alterar(cliente);
        } catch (SQLException e) {
            throw new Exception("Erro ao alterar o cliente: " + e.getMessage());
        }
    }

    // ------------------------------------------------------------
    // D - excluir
    // ------------------------------------------------------------
    public void excluir(int idCliente) throws Exception {
        if (idCliente <= 0) {
            throw new Exception("Selecione um cliente na tabela para excluir.");
        }

        try {
            dao.excluir(idCliente);
        } catch (SQLException e) {
            if (e.getErrorCode() == ERRO_FK_EM_USO) {
                throw new Exception("Este cliente possui agendamentos e não pode ser excluído.");
            }
            throw new Exception("Erro ao excluir o cliente: " + e.getMessage());
        }
    }

    // ------------------------------------------------------------
    // Validações (usadas pelo salvar e pelo alterar)
    // ------------------------------------------------------------
    private Cliente validarEMontar(int idCliente, String nome, String telefone, String email)
            throws Exception {

        nome = limpar(nome);
        telefone = limpar(telefone);
        email = limpar(email);

        if (nome == null) {
            throw new Exception("O nome é obrigatório.");
        }
        if (nome.length() > 100) {
            throw new Exception("O nome pode ter no máximo 100 caracteres.");
        }
        if (telefone == null) {
            throw new Exception("O telefone é obrigatório.");
        }
        if (telefone.length() > 20) {
            throw new Exception("O telefone pode ter no máximo 20 caracteres.");
        }
        if (email != null && !email.contains("@")) {
            throw new Exception("O e-mail informado não é válido.");
        }
        if (email != null && email.length() > 100) {
            throw new Exception("O e-mail pode ter no máximo 100 caracteres.");
        }

        return new Cliente(idCliente, nome, telefone, email);
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
