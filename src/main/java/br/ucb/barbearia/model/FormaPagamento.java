package br.ucb.barbearia.model;

/**
 * Representa uma forma de pagamento (Dinheiro, Pix, Cartão...).
 * Cada objeto desta classe corresponde a uma linha da tabela "forma_pagamento".
 */
public class FormaPagamento {

    private int idFormaPagamento;
    private String descricao;

    public FormaPagamento() {
    }

    /** Usado ao CADASTRAR: o id ainda não existe, quem gera é o banco. */
    public FormaPagamento(String descricao) {
        this.descricao = descricao;
    }

    /** Usado ao LER do banco ou ALTERAR: a forma de pagamento já tem id. */
    public FormaPagamento(int idFormaPagamento, String descricao) {
        this.idFormaPagamento = idFormaPagamento;
        this.descricao = descricao;
    }

    public int getIdFormaPagamento() {
        return idFormaPagamento;
    }

    public void setIdFormaPagamento(int idFormaPagamento) {
        this.idFormaPagamento = idFormaPagamento;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return descricao;
    }
}
