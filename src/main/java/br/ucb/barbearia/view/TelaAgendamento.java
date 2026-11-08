/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package br.ucb.barbearia.view;
import br.ucb.barbearia.controller.AgendamentoController;
import br.ucb.barbearia.model.Agendamento;
import br.ucb.barbearia.model.Cliente;
import br.ucb.barbearia.model.FormaPagamento;
import br.ucb.barbearia.model.Profissional;
import br.ucb.barbearia.model.Servico;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author davitrida
 */
public class TelaAgendamento extends javax.swing.JFrame {
    private final AgendamentoController controller = new AgendamentoController();
    private DefaultTableModel modeloTabela;
    private List<Agendamento> agendamentos = new ArrayList<>();
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(TelaAgendamento.class.getName());

    /**
     * Creates new form TelaAgendamento
     */
    public TelaAgendamento() {
        initComponents();
        configurarTabela();
        carregarCombos();
        carregarTabela();
        limparCampos();
        setLocationRelativeTo(null);
    }
    
        private void configurarTabela() {
        modeloTabela = new DefaultTableModel(new Object[]{
            "ID", "Data", "Hora", "Cliente", "Profissional", "Serviço", "Valor (R$)", "Status", "Pagamento"}, 0) {
            @Override
            public boolean isCellEditable(int linha, int coluna) {
                return false;
            }
        };
        tblAgendamentos.setModel(modeloTabela);
        tblAgendamentos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblAgendamentos.getColumnModel().getColumn(0).setMaxWidth(50);
    }

    private void carregarCombos() {
        try {
            cmbCliente.setModel(new DefaultComboBoxModel<>(
                    controller.listarClientes().toArray(new Cliente[0])));
            cmbProfissional.setModel(new DefaultComboBoxModel<>(
                    controller.listarProfissionais().toArray(new Profissional[0])));
            cmbServico.setModel(new DefaultComboBoxModel<>(
                    controller.listarServicos().toArray(new Servico[0])));

            DefaultComboBoxModel<FormaPagamento> formas = new DefaultComboBoxModel<>();
            formas.addElement(null);   // primeira opção vazia = "ainda não pago"
            for (FormaPagamento f : controller.listarFormasPagamento()) {
                formas.addElement(f);
            }
            cmbPagamento.setModel(formas);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void carregarTabela() {
        modeloTabela.setRowCount(0);
        try {
            agendamentos = controller.listar();
            for (Agendamento a : agendamentos) {
                modeloTabela.addRow(new Object[]{
                    a.getIdAgendamento(),
                    a.getData().format(AgendamentoController.FORMATO_DATA),
                    a.getHora().format(AgendamentoController.FORMATO_HORA),
                    a.getCliente().getNome(),
                    a.getProfissional().getNome(),
                    a.getServico().getDescricao(),
                    a.getValor().toString().replace(".", ","),
                    a.getStatus(),
                    a.getFormaPagamento() == null ? "-" : a.getFormaPagamento().getDescricao()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limparCampos() {
        txtId.setText("");
        cmbCliente.setSelectedIndex(-1);
        cmbProfissional.setSelectedIndex(-1);
        cmbServico.setSelectedIndex(-1);
        txtValor.setText("");
        txtData.setText("");
        txtHora.setText("");
        cmbStatus.setSelectedItem(Agendamento.AGENDADO);
        cmbPagamento.setSelectedIndex(0);
        txtObservacao.setText("");
        tblAgendamentos.clearSelection();
    }

    private int getIdSelecionado() {
        String id = txtId.getText().trim();
        return id.isEmpty() ? 0 : Integer.parseInt(id);
    }

    // ---- Selecionar nos combos o item com o mesmo id ----
    private void selecionarCliente(int id) {
        for (int i = 0; i < cmbCliente.getItemCount(); i++) {
            if (cmbCliente.getItemAt(i).getIdCliente() == id) {
                cmbCliente.setSelectedIndex(i);
                return;
            }
        }
    }

    private void selecionarProfissional(Profissional p) {
        for (int i = 0; i < cmbProfissional.getItemCount(); i++) {
            if (cmbProfissional.getItemAt(i).getIdProfissional() == p.getIdProfissional()) {
                cmbProfissional.setSelectedIndex(i);
                return;
            }
        }
        // Profissional desativado não está no combo: adiciona só para mostrar
        cmbProfissional.addItem(p);
        cmbProfissional.setSelectedItem(p);
    }

    private void selecionarServico(int id) {
        for (int i = 0; i < cmbServico.getItemCount(); i++) {
            if (cmbServico.getItemAt(i).getIdServico() == id) {
                cmbServico.setSelectedIndex(i);
                return;
            }
        }
    }

    private void selecionarPagamento(FormaPagamento f) {
        if (f == null) {
            cmbPagamento.setSelectedIndex(0);
            return;
        }
        for (int i = 0; i < cmbPagamento.getItemCount(); i++) {
            FormaPagamento item = cmbPagamento.getItemAt(i);
            if (item != null && item.getIdFormaPagamento() == f.getIdFormaPagamento()) {
                cmbPagamento.setSelectedIndex(i);
                return;
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        txtId = new javax.swing.JTextField();
        txtValor = new javax.swing.JTextField();
        txtData = new javax.swing.JTextField();
        txtHora = new javax.swing.JTextField();
        txtObservacao = new javax.swing.JTextField();
        cmbCliente = new javax.swing.JComboBox<>();
        cmbProfissional = new javax.swing.JComboBox<>();
        cmbServico = new javax.swing.JComboBox<>();
        cmbStatus = new javax.swing.JComboBox<>();
        cmbPagamento = new javax.swing.JComboBox<>();
        jLabel11 = new javax.swing.JLabel();
        btnSalvar = new javax.swing.JButton();
        btnAlterar = new javax.swing.JButton();
        btnExcluir = new javax.swing.JButton();
        btnLimpar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblAgendamentos = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Agendamentos");

        jLabel1.setText("ID:");

        jLabel2.setText("Cliente:");

        jLabel3.setText("Profissional:");

        jLabel4.setText("Serviço:");

        jLabel5.setText("Data (dd/mm/aaaa):");

        jLabel6.setText("Status:");

        jLabel7.setText("Pagamento:");

        jLabel9.setText("Observação:");

        jLabel10.setText("Hora (hh:mm):");

        txtId.setEditable(false);
        txtId.setColumns(5);
        txtId.addActionListener(this::txtIdActionPerformed);

        txtValor.setEditable(false);
        txtValor.setColumns(8);

        txtData.setColumns(10);

        txtHora.setColumns(5);

        txtObservacao.setColumns(30);

        cmbServico.addActionListener(this::cmbServicoActionPerformed);

        cmbStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "AGENDADO", "CONCLUIDO", "CANCELADO" }));

        jLabel11.setText("Valor (R$):");

        btnSalvar.setText("Salvar");
        btnSalvar.addActionListener(this::btnSalvarActionPerformed);

        btnAlterar.setText("Alterar");
        btnAlterar.addActionListener(this::btnAlterarActionPerformed);

        btnExcluir.setText("Excluir");
        btnExcluir.addActionListener(this::btnExcluirActionPerformed);

        btnLimpar.setText("Limpar");
        btnLimpar.addActionListener(this::btnLimparActionPerformed);

        tblAgendamentos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblAgendamentos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblAgendamentosMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblAgendamentos);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGap(106, 106, 106)
                        .addComponent(jLabel8))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGap(65, 65, 65)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1)))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel10)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7)
                            .addComponent(jLabel9)
                            .addComponent(jLabel11)
                            .addComponent(jLabel4))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnSalvar)
                        .addGap(18, 18, 18)
                        .addComponent(btnAlterar)
                        .addGap(18, 18, 18)
                        .addComponent(btnExcluir)
                        .addGap(18, 18, 18)
                        .addComponent(btnLimpar))
                    .addComponent(txtValor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtData, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtHora, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbProfissional, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbServico, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbPagamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtObservacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(318, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(cmbCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(cmbProfissional, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(cmbServico, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtValor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtData, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(9, 9, 9)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(txtHora, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(cmbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(9, 9, 9)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(cmbPagamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(txtObservacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(75, 75, 75)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSalvar)
                    .addComponent(btnAlterar)
                    .addComponent(btnExcluir)
                    .addComponent(btnLimpar))
                .addGap(20, 20, 20)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8)
                .addContainerGap(277, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalvarActionPerformed
                if (getIdSelecionado() > 0) {
            JOptionPane.showMessageDialog(this,
                    "Este agendamento já está cadastrado. Use Alterar, ou clique em Limpar para cadastrar um novo.");
            return;
        }
        try {
            controller.salvar(
                    (Cliente) cmbCliente.getSelectedItem(),
                    (Profissional) cmbProfissional.getSelectedItem(),
                    (Servico) cmbServico.getSelectedItem(),
                    (FormaPagamento) cmbPagamento.getSelectedItem(),
                    txtData.getText(), txtHora.getText(), txtValor.getText(),
                    (String) cmbStatus.getSelectedItem(), txtObservacao.getText());
            JOptionPane.showMessageDialog(this, "Agendamento cadastrado com sucesso!");
            carregarTabela();
            limparCampos();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Atenção", JOptionPane.WARNING_MESSAGE);
        }// TODO add your handling code here:
    }//GEN-LAST:event_btnSalvarActionPerformed

    private void txtIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIdActionPerformed

    private void btnAlterarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAlterarActionPerformed
                try {
            controller.alterar(getIdSelecionado(),
                    (Cliente) cmbCliente.getSelectedItem(),
                    (Profissional) cmbProfissional.getSelectedItem(),
                    (Servico) cmbServico.getSelectedItem(),
                    (FormaPagamento) cmbPagamento.getSelectedItem(),
                    txtData.getText(), txtHora.getText(), txtValor.getText(),
                    (String) cmbStatus.getSelectedItem(), txtObservacao.getText());
            JOptionPane.showMessageDialog(this, "Agendamento alterado com sucesso!");
            carregarTabela();
            limparCampos();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Atenção", JOptionPane.WARNING_MESSAGE);
        }// TODO add your handling code here:
    }//GEN-LAST:event_btnAlterarActionPerformed

    private void btnExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcluirActionPerformed
                int id = getIdSelecionado();
        if (id > 0 && JOptionPane.showConfirmDialog(this,
                "Deseja realmente excluir este agendamento?", "Confirmar exclusão",
                JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            controller.excluir(id);
            JOptionPane.showMessageDialog(this, "Agendamento excluído com sucesso!");
            carregarTabela();
            limparCampos();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Atenção", JOptionPane.WARNING_MESSAGE);
        }// TODO add your handling code here:
    }//GEN-LAST:event_btnExcluirActionPerformed

    private void btnLimparActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimparActionPerformed
                limparCampos();// TODO add your handling code here:
    }//GEN-LAST:event_btnLimparActionPerformed

    private void cmbServicoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbServicoActionPerformed
                Servico s = (Servico) cmbServico.getSelectedItem();
        txtValor.setText(s == null ? "" : s.getPreco().toString().replace(".", ","));// TODO add your handling code here:
    }//GEN-LAST:event_cmbServicoActionPerformed

    private void tblAgendamentosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblAgendamentosMouseClicked
                int linha = tblAgendamentos.getSelectedRow();
        if (linha >= 0) {
            Agendamento a = agendamentos.get(linha);
            txtId.setText(String.valueOf(a.getIdAgendamento()));
            selecionarCliente(a.getCliente().getIdCliente());
            selecionarProfissional(a.getProfissional());
            selecionarServico(a.getServico().getIdServico());
            selecionarPagamento(a.getFormaPagamento());
            cmbStatus.setSelectedItem(a.getStatus());
            txtData.setText(a.getData().format(AgendamentoController.FORMATO_DATA));
            txtHora.setText(a.getHora().format(AgendamentoController.FORMATO_HORA));
            // depois do combo de serviço, para manter o valor "congelado" do agendamento
            txtValor.setText(a.getValor().toString().replace(".", ","));
            txtObservacao.setText(a.getObservacao() == null ? "" : a.getObservacao());
        }// TODO add your handling code here:
    }//GEN-LAST:event_tblAgendamentosMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new TelaAgendamento().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAlterar;
    private javax.swing.JButton btnExcluir;
    private javax.swing.JButton btnLimpar;
    private javax.swing.JButton btnSalvar;
    private javax.swing.JComboBox<Cliente> cmbCliente;
    private javax.swing.JComboBox<FormaPagamento> cmbPagamento;
    private javax.swing.JComboBox<Profissional> cmbProfissional;
    private javax.swing.JComboBox<Servico> cmbServico;
    private javax.swing.JComboBox<String> cmbStatus;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblAgendamentos;
    private javax.swing.JTextField txtData;
    private javax.swing.JTextField txtHora;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtObservacao;
    private javax.swing.JTextField txtValor;
    // End of variables declaration//GEN-END:variables
}
