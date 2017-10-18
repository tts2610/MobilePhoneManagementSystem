/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.controller;

import main.model.Login;
import java.awt.Color;
import javax.swing.JTextField;
import org.jdesktop.xswingx.PromptSupport;
import database.DBProvider;
import java.awt.Container;
import java.awt.Cursor;
import utility.SwingUtils;

/**
 *
 * @author Hoang
 */
public class ConfigPanel extends javax.swing.JPanel {

    private LoginFrame parent;
    private Login config;

    /**
     * Creates new form LoginPanel
     * @param parent
     */
    public ConfigPanel(Container parent) {
        this.parent = (LoginFrame) parent;
        this.config = LoginFrame.config;

        initComponents();
        setBackground(new Color(0, 255, 0, 0));
        tfHost.setBackground(new Color(255, 255, 255, 150));
        tfHost.setHorizontalAlignment(JTextField.CENTER);
        tfPort.setBackground(new Color(255, 255, 255, 150));
        tfPort.setHorizontalAlignment(JTextField.CENTER);
        tfDBName.setBackground(new Color(255, 255, 255, 150));
        tfDBName.setHorizontalAlignment(JTextField.CENTER);
        tfName.setBackground(new Color(255, 255, 255, 150));
        tfName.setHorizontalAlignment(JTextField.CENTER);
        tfPassword.setBackground(new Color(255, 255, 255, 150));
        tfPassword.setHorizontalAlignment(JTextField.CENTER);

        // Set place holder
        PromptSupport.setForeground(Color.RED, tfHost);
        PromptSupport.setForeground(Color.RED, tfPort);
        PromptSupport.setForeground(Color.RED, tfDBName);
        PromptSupport.setForeground(Color.RED, tfName);
        PromptSupport.setForeground(Color.RED, tfPassword);
        PromptSupport.setPrompt("Host...", tfHost);
        PromptSupport.setPrompt("Port...", tfPort);
        PromptSupport.setPrompt("Database...", tfDBName);
        PromptSupport.setPrompt("Name...", tfName);
        PromptSupport.setPrompt("Password...", tfPassword);

        // Validate input
        SwingUtils.validateStringInput(tfHost, 9, 30, SwingUtils.PATTERN_HOST);
        SwingUtils.validateIntegerInput(tfPort);
        SwingUtils.validateStringInput(tfDBName, 3, 50, SwingUtils.PATTERN_DBNAME);
        SwingUtils.validateStringInput(tfName, 2, 30, SwingUtils.PATTERN_NAMENOSPACE);
        SwingUtils.validateStringInput(tfPassword, 1, 30, SwingUtils.PATTERN_NAMENOSPACE);

        // Khoi tao tri mac dinh
        reset();
    }

//<editor-fold defaultstate="collapsed" desc="Code tu phat sinh">
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tfHost = new javax.swing.JTextField();
        tfPort = new javax.swing.JTextField();
        tfDBName = new javax.swing.JTextField();
        tfName = new javax.swing.JTextField();
        tfPassword = new javax.swing.JPasswordField();
        btOK = new javax.swing.JButton();
        btReset = new javax.swing.JButton();
        btCancel = new javax.swing.JButton();
        lbBg = new javax.swing.JLabel();

        setPreferredSize(new java.awt.Dimension(270, 554));
        setLayout(null);

        tfHost.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        tfHost.setForeground(new java.awt.Color(153, 0, 153));
        tfHost.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tfHostFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                tfHostFocusLost(evt);
            }
        });
        add(tfHost);
        tfHost.setBounds(60, 120, 150, 40);

        tfPort.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        tfPort.setForeground(new java.awt.Color(153, 0, 153));
        tfPort.setToolTipText("Port can be ignored !");
        tfPort.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tfPortFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                tfPortFocusLost(evt);
            }
        });
        add(tfPort);
        tfPort.setBounds(60, 170, 150, 40);

        tfDBName.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        tfDBName.setForeground(new java.awt.Color(153, 0, 153));
        tfDBName.setEnabled(false);
        tfDBName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tfDBNameFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                tfDBNameFocusLost(evt);
            }
        });
        add(tfDBName);
        tfDBName.setBounds(60, 220, 150, 40);

        tfName.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        tfName.setForeground(new java.awt.Color(153, 0, 153));
        tfName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tfNameFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                tfNameFocusLost(evt);
            }
        });
        add(tfName);
        tfName.setBounds(60, 270, 150, 40);

        tfPassword.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        tfPassword.setForeground(new java.awt.Color(153, 0, 153));
        tfPassword.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tfPasswordFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                tfPasswordFocusLost(evt);
            }
        });
        add(tfPassword);
        tfPassword.setBounds(60, 320, 150, 40);

        btOK.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/main/OK.png"))); // NOI18N
        btOK.setBorderPainted(false);
        btOK.setContentAreaFilled(false);
        btOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btOKActionPerformed(evt);
            }
        });
        add(btOK);
        btOK.setBounds(30, 470, 50, 50);

        btReset.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/main/Connect.png"))); // NOI18N
        btReset.setBorderPainted(false);
        btReset.setContentAreaFilled(false);
        btReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btResetActionPerformed(evt);
            }
        });
        add(btReset);
        btReset.setBounds(110, 470, 50, 50);

        btCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/main/Cancel.png"))); // NOI18N
        btCancel.setBorderPainted(false);
        btCancel.setContentAreaFilled(false);
        btCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btCancelActionPerformed(evt);
            }
        });
        add(btCancel);
        btCancel.setBounds(190, 470, 50, 50);

        lbBg.setBackground(new java.awt.Color(255, 255, 255));
        lbBg.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/main/Login3.jpg"))); // NOI18N
        add(lbBg);
        lbBg.setBounds(0, 0, 324, 556);
    }// </editor-fold>//GEN-END:initComponents

    private void tfHostFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tfHostFocusGained
        tfHost.setBackground(new Color(255, 255, 255, 255));
        tfHost.selectAll();
    }//GEN-LAST:event_tfHostFocusGained

    private void tfHostFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tfHostFocusLost
        tfHost.setBackground(new Color(255, 255, 255, 150));
    }//GEN-LAST:event_tfHostFocusLost

    private void tfPasswordFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tfPasswordFocusGained
        tfPassword.setBackground(new Color(255, 255, 255, 255));
        tfPassword.selectAll();
    }//GEN-LAST:event_tfPasswordFocusGained

    private void tfPasswordFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tfPasswordFocusLost
        tfPassword.setBackground(new Color(255, 255, 255, 150));
    }//GEN-LAST:event_tfPasswordFocusLost

    private void btCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btCancelActionPerformed
        parent.reloadContentPanel(LoginFrame.LOGIN_PANEL);
    }//GEN-LAST:event_btCancelActionPerformed

    private void tfPortFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tfPortFocusGained
        tfPort.setBackground(new Color(255, 255, 255, 255));
        tfPort.selectAll();
    }//GEN-LAST:event_tfPortFocusGained

    private void tfPortFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tfPortFocusLost
        tfPort.setBackground(new Color(255, 255, 255, 150));
    }//GEN-LAST:event_tfPortFocusLost

    private void tfDBNameFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tfDBNameFocusGained
        tfDBName.setBackground(new Color(255, 255, 255, 255));
        tfDBName.selectAll();
    }//GEN-LAST:event_tfDBNameFocusGained

    private void tfDBNameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tfDBNameFocusLost
        tfDBName.setBackground(new Color(255, 255, 255, 150));
    }//GEN-LAST:event_tfDBNameFocusLost

    private void tfNameFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tfNameFocusGained
        tfName.setBackground(new Color(255, 255, 255, 255));
        tfName.selectAll();
    }//GEN-LAST:event_tfNameFocusGained

    private void tfNameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tfNameFocusLost
        tfName.setBackground(new Color(255, 255, 255, 150));
    }//GEN-LAST:event_tfNameFocusLost

    private void btOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btOKActionPerformed
        checkConnection();
    }//GEN-LAST:event_btOKActionPerformed

    private void btResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btResetActionPerformed
        reset();
    }//GEN-LAST:event_btResetActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btCancel;
    private javax.swing.JButton btOK;
    private javax.swing.JButton btReset;
    private javax.swing.JLabel lbBg;
    public javax.swing.JTextField tfDBName;
    public javax.swing.JTextField tfHost;
    public javax.swing.JTextField tfName;
    public javax.swing.JPasswordField tfPassword;
    public javax.swing.JTextField tfPort;
    // End of variables declaration//GEN-END:variables
//</editor-fold>

    private void reset() {
        // Khoi tao tri mac dinh
        tfHost.setText(config.host);
        tfPort.setText(config.port);
        tfDBName.setText(config.DBName);
        tfName.setText(config.name);
        tfPassword.setText(config.password);

        // Set focus khi khoi tao
        tfHost.requestFocus();
    }

    private void checkConnection() {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        DBProvider db = new DBProvider(tfHost.getText().trim(), tfPort.getText().trim(), tfDBName.getText().trim(), tfName.getText().trim(), new String(tfPassword.getPassword()).trim());
        if (!db.start()) { //Ket noi database that bai
            setCursor(null);
            SwingUtils.showErrorDialog("Error: cannot connect database !");
            SwingUtils.showInfoDialog("Data will be reverted back !");
            reset();
        } else { // Ket noi database thanh cong
            setCursor(null);
            SwingUtils.showInfoDialog("Connected database successfully !");
            db.stop();
            config.host = tfHost.getText().trim();
            config.port = tfPort.getText().trim();
            config.DBName = tfDBName.getText().trim();
            config.name = tfName.getText().trim();
            config.password = new String(tfPassword.getPassword()).trim();

            // Quay ve panel login
            parent.reloadContentPanel(LoginFrame.LOGIN_PANEL);
        }
    }
}
