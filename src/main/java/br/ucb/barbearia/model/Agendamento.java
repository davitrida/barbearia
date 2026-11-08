package br.ucb.barbearia.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Representa um agendamento de atendimento: a ROTINA PRINCIPAL do sistema.
 *
 * Em vez de guardar só os números das chaves estrangeiras (id_cliente etc.),
 * a classe guarda os OBJETOS relacionados. Assim, a FK do banco vira,
 * no Java, um objeto dentro de outro objeto (associação).
 */
public class Agendamento {

    // Valores possíveis do status (iguais ao ENUM da tabela)
    public static final String AGENDADO = "AGENDADO";
    public static final String CONCLUIDO = "CONCLUIDO";
    public static final String CANCELADO = "CANCELADO";

    private int idAgendamento;
    private Cliente cliente;
    private Profissional profissional;
    private Servico servico;
    private FormaPagamento formaPagamento;   // pode ser null até o atendimento ser concluído
    private LocalDate data;
    private LocalTime hora;
    private BigDecimal valor;                // preço "congelado" no dia do agendamento
    private String status;
    private String observacao;

    public Agendamento() {
    }

    public Agendamento(int idAgendamento, Cliente cliente, Profissional profissional,
                       Servico servico, FormaPagamento formaPagamento, LocalDate data,
                       LocalTime hora, BigDecimal valor, String status, String observacao) {
        this.idAgendamento = idAgendamento;
        this.cliente = cliente;
        this.profissional = profissional;
        this.servico = servico;
        this.formaPagamento = formaPagamento;
        this.data = data;
        this.hora = hora;
        this.valor = valor;
        this.status = status;
        this.observacao = observacao;
    }

    public int getIdAgendamento() {
        return idAgendamento;
    }

    public void setIdAgendamento(int idAgendamento) {
        this.idAgendamento = idAgendamento;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Profissional getProfissional() {
        return profissional;
    }

    public void setProfissional(Profissional profissional) {
        this.profissional = profissional;
    }

    public Servico getServico() {
        return servico;
    }

    public void setServico(Servico servico) {
        this.servico = servico;
    }

    public FormaPagamento getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(FormaPagamento formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }
}
