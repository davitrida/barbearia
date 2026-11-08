package br.ucb.barbearia.model;

import java.math.BigDecimal;

/**
 * Representa um serviço da barbearia (Corte, Barba...).
 * Cada objeto desta classe corresponde a uma linha da tabela "servico".
 */
public class Servico {

    private int idServico;
    private String descricao;
    private BigDecimal preco;      // BigDecimal: valor exato, ideal para dinheiro
    private int duracaoMin;

    public Servico() {
    }

    /** Usado ao CADASTRAR: o id ainda não existe, quem gera é o banco. */
    public Servico(String descricao, BigDecimal preco, int duracaoMin) {
        this.descricao = descricao;
        this.preco = preco;
        this.duracaoMin = duracaoMin;
    }

    /** Usado ao LER do banco ou ALTERAR: o serviço já tem id. */
    public Servico(int idServico, String descricao, BigDecimal preco, int duracaoMin) {
        this.idServico = idServico;
        this.descricao = descricao;
        this.preco = preco;
        this.duracaoMin = duracaoMin;
    }

    public int getIdServico() {
        return idServico;
    }

    public void setIdServico(int idServico) {
        this.idServico = idServico;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public int getDuracaoMin() {
        return duracaoMin;
    }

    public void setDuracaoMin(int duracaoMin) {
        this.duracaoMin = duracaoMin;
    }

    @Override
    public String toString() {
        return descricao;
    }
}
