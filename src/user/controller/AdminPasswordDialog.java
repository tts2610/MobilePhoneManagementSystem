/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package user.controller;

import database.IDAO;
import employee.model.EmployeeSwingUtils;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import user.model.User;
import utility.SwingUtils;

/**
 *
 * @author BonBon
 */
public class AdminPasswordDialog extends javax.swing.JDialog implements IDAO<User> {

    User user;
    String newPass = "", reNewPass = "";
    public static final int MIN = 6;
    public static final int MAX = 30;
//<editor-fold defaultstate="collapsed" desc="Constructor">

    public AdminPasswordDialog(User user) {
        super((JFrame) null, true);
        initComponents();
        setLocationRelativeTo(null);
        this.user = user;
        btOK.setEnabled(false);

        SwingUtils.validateStringInput2(txtNew, MIN, MAX, EmployeeSwingUtils.PATTERN_NAMENOSPACE);
        txtNew.getDocument().addDocumentListener(
                new DocumentListener() {
            @Override
            public void changedUpdate(DocumentEvent e) {
                setButton();
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                setButton();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                setButton();
            }
        });
        txtReNew.getDocument().addDocumentListener(
                new DocumentListener() {
            @Override
            public void changedUpdate(DocumentEvent e) {
                setButton();
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                setButton();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                setButton();
            }
        });
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="method">    
    @Override
    public boolean update(User user) {
        boolean result = false;
        try {

            runPS("update Users set  UserPassword=? where UserID=?",
                    encryptPass(newPass),
                    user.getUserID()
            );
            result = true;
            dispose();

        } catch (SQLException ex) {
            Logger.getLogger(UserPasswordDialog.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    private void setButton() {
        newPass = String.valueOf(txtNew.getPassword()).trim();
        reNewPass = String.valueOf(txtReNew.getPassword()).trim();
        if (newPass.isEmpty() || reNewPass.isEmpty()) {
            btOK.setEnabled(false);
//            btCancel.setEnabled(false);
        } else {
            btOK.setEnabled(true);
            btCancel.setEnabled(true);
        }
    }

    public String encryptPass(String newPass) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(newPass.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        }
        return null;

    }

    private boolean checkLength() {
        boolean result = false;
        String newPw = new String(txtNew.getPassword()).trim();
        if (newPw.isEmpty()) {
            SwingUtils.showInfoDialog("New password is empty !");
            txtNew.requestFocus();
            txtNew.selectAll();
        } else if (newPw.length() < MIN) {
            SwingUtils.showInfoDialog("Minimum 6 characters !");
            txtNew.requestFocus();
            txtNew.selectAll();
        } else {
            result = true;
        }
        return result;
    }

    public boolean validateField() {
        if (!checkLength()) {
            return false;
        }
        boolean result = true;
//        if (!newPass.matches("[A-Za-z0-9]{6,30}")) {
//            result = false;
//            SwingUtils.showErrorDialog("Invalid format ! Only number and character, minimum 6 and maximum 30 characters !");
//            txtNew.requestFocus();
//        } else 

        if (!reNewPass.equals(newPass)) {
            result = false;
            SwingUtils.showErrorDialog("Re-new password does not matches!");
            txtReNew.requestFocus();
            txtReNew.selectAll();
        }

        return result;
    }

    @Override
    public List<User> getList() {
        return null;
    }

    @Override
    public boolean insert(User model) {
        return false;
    }

    @Override
    public boolean delete(User model) {
        return false;
    }

    @Override
    public int getSelectingIndex(int idx) {
        return 0;
    }

    @Override
    public void setSelectingIndex(int idx) {

    }
//</editor-fold>

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btOK = new javax.swing.JButton();
        btCancel = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtNew = new javax.swing.JPasswordField();
        txtReNew = new javax.swing.JPasswordField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Change Password");
        setMaximumSize(new java.awt.Dimension(338, 158));
        setMinimumSize(new java.awt.Dimension(338, 158));
        setResizable(false);

        btOK.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        btOK.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/order/OK2.png"))); // NOI18N
        btOK.setText("Save");
        btOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btOKActionPerformed(evt);
            }
        });

        btCancel.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        btCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/order/Cancel2.png"))); // NOI18N
        btCancel.setText("Cancel");
        btCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btCancelActionPerformed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 0, 51));
        jLabel9.setText("*");

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 0, 51));
        jLabel10.setText("*");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel2.setText("New password:");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel3.setText("Re-password:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(21, 21, 21)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtNew)
                    .addComponent(txtReNew, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btOK, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel3))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtNew, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtReNew, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btOK, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btCancelActionPerformed
        dispose();
    }//GEN-LAST:event_btCancelActionPerformed

    private void btOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btOKActionPerformed
        if (validateField() == true) {
            if (EmployeeSwingUtils.showConfirmDialog("Are you sure to update ?") == JOptionPane.NO_OPTION) {
                return;
            } else {
                boolean result = update(user);
                EmployeeSwingUtils.showInfoDialog(result ? EmployeeSwingUtils.UPDATE_SUCCESS : EmployeeSwingUtils.UPDATE_FAIL);
            }
        }
    }//GEN-LAST:event_btOKActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btCancel;
    private javax.swing.JButton btOK;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPasswordField txtNew;
    private javax.swing.JPasswordField txtReNew;
    // End of variables declaration//GEN-END:variables

}
