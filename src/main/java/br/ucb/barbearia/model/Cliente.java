package br.ucb.barbearia.model;

/**
 * Representa um cliente da barbearia.
 * Cada objeto desta classe corresponde a uma linha da tabela "cliente".
 */
public class Cliente {

    // ------------------------------------------------------------
    // Atributos (as colunas da tabela)
    // ------------------------------------------------------------
    private int idCliente;
    private String nome;
    private String telefone;
    private String email;

    // ------------------------------------------------------------
    // Construtores
    // ------------------------------------------------------------

    /** Construtor vazio: cria um cliente sem dados (preenchido depois pelos setters). */
    public Cliente() {
    }

    /** Usado ao CADASTRAR: o id ainda não existe, quem gera é o banco. */
    public Cliente(String nome, String telefone, String email) {
        this.nome = nome;
        this.telefone = telefone;
        this.email = email;
    }

    /** Usado ao LER do banco ou ALTERAR: o cliente já tem id. */
    public Cliente(int idCliente, String nome, String telefone, String email) {
        this.idCliente = idCliente;
        this.nome = nome;
        this.telefone = telefone;
        this.email = email;
    }

    // ------------------------------------------------------------
    // Getters e Setters
    // ------------------------------------------------------------
    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // ------------------------------------------------------------
    // toString: como o objeto aparece quando vira texto
    // ------------------------------------------------------------
    @Override
    public String toString() {
        return nome;
    }
}
