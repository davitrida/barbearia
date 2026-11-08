package br.ucb.barbearia.model;

/**
 * Representa um profissional (barbeiro) da barbearia.
 * Cada objeto desta classe corresponde a uma linha da tabela "profissional".
 */
public class Profissional {

    private int idProfissional;
    private String nome;
    private String telefone;
    private String especialidade;
    private boolean ativo;

    public Profissional() {
    }

    /** Usado ao CADASTRAR: o id ainda não existe, quem gera é o banco. */
    public Profissional(String nome, String telefone, String especialidade, boolean ativo) {
        this.nome = nome;
        this.telefone = telefone;
        this.especialidade = especialidade;
        this.ativo = ativo;
    }

    /** Usado ao LER do banco ou ALTERAR: o profissional já tem id. */
    public Profissional(int idProfissional, String nome, String telefone,
                        String especialidade, boolean ativo) {
        this.idProfissional = idProfissional;
        this.nome = nome;
        this.telefone = telefone;
        this.especialidade = especialidade;
        this.ativo = ativo;
    }

    public int getIdProfissional() {
        return idProfissional;
    }

    public void setIdProfissional(int idProfissional) {
        this.idProfissional = idProfissional;
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

    public String getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    @Override
    public String toString() {
        return nome;
    }
}
