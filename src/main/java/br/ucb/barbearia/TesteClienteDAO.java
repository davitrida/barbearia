package br.ucb.barbearia;

import br.ucb.barbearia.dao.ClienteDAO;
import br.ucb.barbearia.model.Cliente;
import java.sql.SQLException;
import java.util.List;

/**
 * Classe TEMPORÁRIA, só para testar o ClienteDAO pelo console.
 * Testa as 4 operações e, no final, apaga o cliente de teste,
 * deixando o banco como estava.
 */
public class TesteClienteDAO {

    public static void main(String[] args) {
        ClienteDAO dao = new ClienteDAO();

        try {
            // R - listar
            System.out.println("=== 1. LISTAR (antes) ===");
            mostrar(dao.listar());

            // C - inserir
            System.out.println("\n=== 2. INSERIR ===");
            Cliente novo = new Cliente("Cliente Teste", "(61) 90000-0000", "teste@email.com");
            dao.inserir(novo);
            System.out.println("Inserido com id: " + novo.getIdCliente());

            // U - alterar
            System.out.println("\n=== 3. ALTERAR ===");
            novo.setNome("Cliente Teste ALTERADO");
            dao.alterar(novo);
            System.out.println("Nome alterado para: " + novo.getNome());

            System.out.println("\n=== 4. LISTAR (com o cliente de teste) ===");
            mostrar(dao.listar());

            // D - excluir
            System.out.println("\n=== 5. EXCLUIR ===");
            dao.excluir(novo.getIdCliente());
            System.out.println("Cliente " + novo.getIdCliente() + " excluído.");

            System.out.println("\n=== 6. LISTAR (depois) ===");
            mostrar(dao.listar());

        } catch (SQLException e) {
            System.out.println("Erro no banco: " + e.getMessage());
        }
    }

    private static void mostrar(List<Cliente> clientes) {
        for (Cliente c : clientes) {
            System.out.println(c.getIdCliente() + " | " + c.getNome() + " | "
                    + c.getTelefone() + " | " + c.getEmail());
        }
    }
}
