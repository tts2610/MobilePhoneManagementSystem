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
import javax.sql.rowset.CachedRowSet;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import user.model.User;
import user.model.UserEmployee;
import utility.SwingUtils;

/**
 *
 * @author BonBon
 */
public class AddNewUser extends javax.swing.JDialog implements IDAO<User> {

//    private AddNewUser newUser;
    User user;
    private NewUserComboBoxModel employeeComboBoxModel;
    private NewUserComboBoxRenderer employeeComboBoxRenderer;
    UserEmployee cbbEmp;
    int empID;
    String pass = "", repass = "", userName = "";
    public static final int MINUSER = 4;
    public static final int MIN = 6;
    public static final int MAX = 30;

    public AddNewUser() {
        super((JFrame) null, true);
        initComponents();
        setLocationRelativeTo(null);
        btCancel.setEnabled(false);
        btOK.setEnabled(false);

        employeeComboBoxModel = new NewUserComboBoxModel();
        cbbEmp = new UserEmployee(0, "All");
        employeeComboBoxModel.addElement(cbbEmp);
        cbEmployee.setModel(employeeComboBoxModel);
        employeeComboBoxRenderer = new NewUserComboBoxRenderer();
        cbEmployee.setRenderer(employeeComboBoxRenderer);
        cbEmployee.setSelectedIndex(cbEmployee.getItemCount() - 1);
        txtUserName.setText("");
        txtNew.setText("");
        SwingUtils.validateStringInput2(txtUserName, MINUSER, MAX, SwingUtils.PATTERN_NAMENOSPACE);
        SwingUtils.validateStringInput2(txtNew, MIN, MAX, SwingUtils.PATTERN_NAMENOSPACE);

        //<editor-fold defaultstate="collapsed" desc="event listener">
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
        txtUserName.getDocument().addDocumentListener(
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
//</editor-fold>
    }

    //<editor-fold defaultstate="collapsed" desc="method">
    private void setButton() {
        pass = String.valueOf(txtNew.getPassword()).trim();
        repass = String.valueOf(txtReNew.getPassword()).trim();
        userName = txtUserName.getText().trim();
        if (pass.isEmpty() || repass.isEmpty() || userName.isEmpty()) {
            btOK.setEnabled(false);
            btCancel.setEnabled(false);
        } else {
            btOK.setEnabled(true);
            btCancel.setEnabled(true);
        }
    }

    private boolean checkLength() {
        boolean result = false;
        String oldPw = new String(txtUserName.getText()).trim();
        String newPw = new String(txtNew.getPassword()).trim();
        if (oldPw.isEmpty()) {
            SwingUtils.showInfoDialog("User name is empty !");
            txtUserName.requestFocus();
            txtUserName.selectAll();
        } else if (oldPw.length() < MINUSER) {
            SwingUtils.showInfoDialog("Minimum 4 characters !");
            txtUserName.requestFocus();
            txtUserName.selectAll();
        } else if (newPw.isEmpty()) {
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
        boolean result = true;
        empID = ((UserEmployee) cbEmployee.getSelectedItem()).getEmpID();

        if (empID == 0) {
            result = false;
            SwingUtils.showErrorDialog("Choose employee !");
        } else if (!checkLength()) {
            result = false;
        } else if (!repass.equals(pass)) {
            result = false;
            SwingUtils.showErrorDialog("Re-password does not matches!");
            txtReNew.requestFocus();
            txtReNew.selectAll();
        } else {
            try {
                CachedRowSet crs = getCRS("select * from Users where UserName=?", userName);
                if (crs.first()) {
                    result = false;
                    SwingUtils.showErrorDialog("Username can't be duplicate !");
                    txtUserName.requestFocus();
                }
            } catch (SQLException ex) {
                Logger.getLogger(AddNewUser.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }

    public boolean insert() {
        boolean result = false;

        try {
            runPS("insert into Users values(?,?,?,?)", userName, encryptPass(pass), 1, empID);
            result = true;
            dispose();
        } catch (SQLException ex) {
            Logger.getLogger(AddNewUser.class.getName()).log(Level.SEVERE, null, ex);
        }

//        System.out.println(pass + "  \t\tMD5:  " + encryptPass(pass));
        return result;
    }

    public String encryptPass(String pass) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(pass.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        }
        return null;

    }

    private void refreshField() {
        txtNew.setText("");
        txtReNew.setText("");
        txtUserName.setText("");
        cbEmployee.setSelectedIndex(cbEmployee.getItemCount() - 1);
    }

    @Override
    public List<User> getList() {
        return null;
    }

    @Override
    public boolean update(User model) {
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

    @Override
    public boolean insert(User model) {
        return false;
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
        jLabel11 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        btClose = new javax.swing.JButton();
        txtUserName = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        cbEmployee = new javax.swing.JComboBox<>();
        jLabel12 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("NewUser");
        setMinimumSize(new java.awt.Dimension(400, 180));
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

        txtNew.setMinimumSize(new java.awt.Dimension(6, 6));

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 0, 51));
        jLabel11.setText("*");

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel4.setText("Username:");

        btClose.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        btClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/order/Delete2.png"))); // NOI18N
        btClose.setText("Close");
        btClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btCloseActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel5.setText("Employee:");

        jLabel12.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 0, 51));
        jLabel12.setText("*");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(36, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btOK, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btClose, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(42, 42, 42)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(cbEmployee, 0, 157, Short.MAX_VALUE)
                            .addComponent(txtNew, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtReNew, javax.swing.GroupLayout.DEFAULT_SIZE, 157, Short.MAX_VALUE)
                            .addComponent(txtUserName))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(36, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtUserName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cbEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel5)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel3))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtNew, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9))
                        .addGap(20, 20, 20)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtReNew, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10))))
                .addGap(23, 23, 23)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btOK, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btClose, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btCancelActionPerformed
        refreshField();
    }//GEN-LAST:event_btCancelActionPerformed

    private void btOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btOKActionPerformed
        if (validateField() == true) {
            if (EmployeeSwingUtils.showConfirmDialog("Are you sure to insert ?") == JOptionPane.NO_OPTION) {
                return;
            } else {
                boolean result = insert();
                EmployeeSwingUtils.showInfoDialog(result ? EmployeeSwingUtils.INSERT_SUCCESS : EmployeeSwingUtils.INSERT_FAIL);
//                if (result == true) {
//                    refreshField();
//                }
            }
        }
    }//GEN-LAST:event_btOKActionPerformed

    private void btCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btCloseActionPerformed
        dispose();
    }//GEN-LAST:event_btCloseActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btCancel;
    private javax.swing.JButton btClose;
    private javax.swing.JButton btOK;
    private javax.swing.JComboBox<user.model.UserEmployee> cbEmployee;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPasswordField txtNew;
    private javax.swing.JPasswordField txtReNew;
    private javax.swing.JTextField txtUserName;
    // End of variables declaration//GEN-END:variables

}
