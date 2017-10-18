/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package user.controller;

//import utility.ComboBoxCellEditor;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.TableRowSorter;
import main.controller.LoginFrame;
import main.model.UserFunction;

import user.model.User;
import user.model.UserDAOImpl;
import user.model.UserEmployee;
import utility.StringCellEditor;
import utility.TableCellListener;
import employee.model.EmployeeSwingUtils;
import javax.swing.JOptionPane;
import user.model.NewUserEmployeeDAOImpl;
//test push
/**
 *
 * @author BonBon
 */
public class UserPanel extends javax.swing.JPanel {

    private UserTableModel userTableModel;

    private TableRowSorter<UserTableModel> sorter;

    // Customer dang duoc chon trong table
    private User selectedUser;
    private int selectedRowIndex = -1;
    private UserEmployeeComboBoxModel employeeComboBoxModel1;
    UserEmployee filterEmployee;

// Define some column constants
    private static final int COL_USERID = 0;
    private static final int COL_USERNAME = 1;
    private static final int COL_EMPNAME = 2;
    private static final int COL_STATUS = 3;
    private static final int COL_PASS = 4;
    private static final int COL_EMPID = 5;

    private String userName = LoginFrame.config.userName;

    //<editor-fold defaultstate="collapsed" desc="Constructor">
    /**
     * Creates new form UserPanel
     */
    public UserPanel() {
        initComponents();
        // Disable button khi moi khoi dong len
        setButtonEnabled(false);

        // Selecting user in the table
        selectedUser = new User();

        // Set data cho column employee Username combobox
        employeeComboBoxModel1 = new UserEmployeeComboBoxModel();

        // Set data cho table
        userTableModel = new UserTableModel();
        tbUserList.setModel(userTableModel);

        // Set sorter cho table
        sorter = new TableRowSorter<>(userTableModel);
        tbUserList.setRowSorter(sorter);

        // Set auto define column from model to false to stop create column again
        tbUserList.setAutoCreateColumnsFromModel(false);

        // Set height cho table header
        tbUserList.getTableHeader().setPreferredSize(new Dimension(100, 30));

        // Col cus id
        tbUserList.getColumnModel().getColumn(COL_USERID).setMinWidth(40);
        tbUserList.getColumnModel().getColumn(COL_USERID).setMaxWidth(60);

        // Col emp name
        tbUserList.getColumnModel().getColumn(COL_EMPNAME).setMinWidth(150);
        tbUserList.getColumnModel().getColumn(COL_EMPNAME).setCellEditor(new UserEmployeeComboBoxCellEditor(employeeComboBoxModel1));
        // Col status
        tbUserList.getColumnModel().getColumn(COL_STATUS).setMinWidth(70);
        tbUserList.getColumnModel().getColumn(COL_STATUS).setMaxWidth(70);
//        // Col pass
        tbUserList.getColumnModel().getColumn(COL_PASS).setMinWidth(0);
        tbUserList.getColumnModel().getColumn(COL_PASS).setMaxWidth(0);
        //Col EmpID
        tbUserList.getColumnModel().getColumn(COL_EMPID).setMinWidth(0);
        tbUserList.getColumnModel().getColumn(COL_EMPID).setMaxWidth(0);

        // Bat su kien select row tren table
        tbUserList.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
            DefaultListSelectionModel model = (DefaultListSelectionModel) e.getSource();
            if (!model.isSelectionEmpty()) {
                fetchAction();
                // Check permission Permission (chi check view)
                setButtonEnabled(true);
                if (!LoginFrame.checkPermission(new UserFunction(UserFunction.FG_PERMISSION, UserFunction.FN_VIEW))) {
                    btPermission.setEnabled(false);
                } else {
//                    setButtonPermissionEnabled(checkChangePass());
                }
                setButtonEnabled(checkUpdate());
                setButtonChangePassEnabled(checkChangePass());
            } else {
                setButtonEnabled(false);
            }
        });

//</editor-fold>
//<editor-fold defaultstate="collapsed" desc="Set cell listener cho updating">
        TableCellListener tcl = new TableCellListener(tbUserList, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TableCellListener tcl = (TableCellListener) e.getSource();

                switch (tcl.getColumn()) {
                    case COL_EMPNAME:                        
                        selectedUser.setEmpName((String) tcl.getNewValue());
                        selectedUser.setEmpID(employeeComboBoxModel1.getUserEmployeeNameFromValue((String) tcl.getNewValue()).getEmpID());
                        employeeComboBoxModel1.refresh();
                        break;
                    case COL_STATUS:
                        selectedUser.setUserEnable((boolean) tcl.getNewValue());
                        break;
                }
                if (EmployeeSwingUtils.showConfirmDialog("Are you sure to update ?") == JOptionPane.NO_OPTION) {
                    refreshAction(false);
                } else {
                    updateAction();
                }
            }
        });
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Bat su kien cho vung filter">
        tfIdFilter.getDocument().addDocumentListener(
                new DocumentListener() {
            @Override
            public void changedUpdate(DocumentEvent e) {
                doFilter();
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                doFilter();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                doFilter();
            }
        });
        tfUserNameFilter.getDocument().addDocumentListener(
                new DocumentListener() {
            @Override
            public void changedUpdate(DocumentEvent e) {
                doFilter();
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                doFilter();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                doFilter();
            }
        });
        tfEmpNameFilter.getDocument().addDocumentListener(
                new DocumentListener() {
            @Override
            public void changedUpdate(DocumentEvent e) {
                doFilter();
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                doFilter();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                doFilter();
            }
        });

//</editor-fold>
// Check permission order
        if (!LoginFrame.checkPermission(new UserFunction(UserFunction.FG_USER, UserFunction.FN_UPDATE))) {
            tbUserList.setEnabled(false);
            btAdd.setEnabled(false);
//            btChangePass.setEnabled(false);
            btRemove.setEnabled(false);
        }
        // Check permission Permission (chi check view)
        if (!LoginFrame.checkPermission(new UserFunction(UserFunction.FG_PERMISSION, UserFunction.FN_VIEW))) {
            btPermission.setEnabled(false);
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

        pnFilter = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        tfIdFilter = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        tfUserNameFilter = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        cbStatusFilter = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        tfEmpNameFilter = new javax.swing.JTextField();
        btRemove = new javax.swing.JButton();
        btAdd = new javax.swing.JButton();
        btRefresh = new javax.swing.JButton();
        pnTitle = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        btPermission = new javax.swing.JButton();
        btChangePass = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbUserList = new javax.swing.JTable();

        setPreferredSize(new java.awt.Dimension(790, 640));

        pnFilter.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Filter"));

        jLabel2.setText("ID:");

        jLabel3.setText("UserName:");
        jLabel3.setPreferredSize(new java.awt.Dimension(55, 16));

        jLabel6.setText("Status:");

        cbStatusFilter.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All", "Enabled", "Disabled" }));
        cbStatusFilter.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbStatusFilterItemStateChanged(evt);
            }
        });

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/order/Refresh2.png"))); // NOI18N
        jButton1.setBorderPainted(false);
        jButton1.setContentAreaFilled(false);
        jButton1.setFocusPainted(false);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel4.setText("EmpName:");

        javax.swing.GroupLayout pnFilterLayout = new javax.swing.GroupLayout(pnFilter);
        pnFilter.setLayout(pnFilterLayout);
        pnFilterLayout.setHorizontalGroup(
            pnFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnFilterLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(tfIdFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 39, Short.MAX_VALUE)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(tfUserNameFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 39, Short.MAX_VALUE)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(tfEmpNameFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 41, Short.MAX_VALUE)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbStatusFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        pnFilterLayout.setVerticalGroup(
            pnFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnFilterLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel6)
                        .addComponent(cbStatusFilter)
                        .addComponent(tfEmpNameFilter))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(tfUserNameFilter)
                        .addComponent(jLabel4))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2)
                        .addComponent(tfIdFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(17, 17, 17))
        );

        btRemove.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        btRemove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/order/Delete.png"))); // NOI18N
        btRemove.setText("Remove");
        btRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btRemoveActionPerformed(evt);
            }
        });

        btAdd.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        btAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/order/Add.png"))); // NOI18N
        btAdd.setText("Add New...");
        btAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btAddActionPerformed(evt);
            }
        });

        btRefresh.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        btRefresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/order/Refresh.png"))); // NOI18N
        btRefresh.setText("Refresh");
        btRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btRefreshActionPerformed(evt);
            }
        });

        pnTitle.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setFont(new java.awt.Font("Lucida Grande", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 255, 255));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/main/User.png"))); // NOI18N
        jLabel1.setText("<html><u><i><font color='red'>U</font>ser <font color='red'>M</font>anagement</i></u></html>");

        btPermission.setFont(new java.awt.Font("Lucida Grande", 3, 14)); // NOI18N
        btPermission.setForeground(new java.awt.Color(255, 153, 0));
        btPermission.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/user/rsz_permission.png"))); // NOI18N
        btPermission.setText("<html><u>Permission...</u></html>");
        btPermission.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btPermissionActionPerformed(evt);
            }
        });

        btChangePass.setFont(new java.awt.Font("Lucida Grande", 3, 14)); // NOI18N
        btChangePass.setForeground(new java.awt.Color(255, 153, 0));
        btChangePass.setText("<html><u>Change Password...</u></html>");
        btChangePass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btChangePassActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnTitleLayout = new javax.swing.GroupLayout(pnTitle);
        pnTitle.setLayout(pnTitleLayout);
        pnTitleLayout.setHorizontalGroup(
            pnTitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnTitleLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 322, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btChangePass, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btPermission, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        pnTitleLayout.setVerticalGroup(
            pnTitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnTitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 61, Short.MAX_VALUE)
                .addComponent(btPermission, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(btChangePass, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jScrollPane2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "User List"));

        tbUserList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null}
            },
            new String [] {
                "ID", "User Name", "Emp Name", "Status"
            }
        ));
        tbUserList.setFillsViewportHeight(true);
        tbUserList.setRowHeight(25);
        tbUserList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tbUserList.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(tbUserList);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnFilter, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pnTitle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btRemove, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jScrollPane2)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnTitle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 425, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btRemove, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    // <editor-fold defaultstate="collapsed" desc="Khai bao event">    
    private void btAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btAddActionPerformed
        NewUserEmployeeDAOImpl nu = new NewUserEmployeeDAOImpl();
        if (nu.isZeroEmployee() == false) {
            new AddNewUser().setVisible(true);
        } else {
            EmployeeSwingUtils.showErrorDialog("All employee have account, please insert more employee before !");
        }
        refreshAction(false);
    }//GEN-LAST:event_btAddActionPerformed

    private void btPermissionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btPermissionActionPerformed

        if (selectedUser.getUserID() == 0) {
            selectedUser = new UserDAOImpl().getUserFromName(LoginFrame.config.userName);
        }
        new PermissionDialog(selectedUser).setVisible(true);


    }//GEN-LAST:event_btPermissionActionPerformed

    private void btRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btRefreshActionPerformed
        refreshAction(true);
    }//GEN-LAST:event_btRefreshActionPerformed

    private void btRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btRemoveActionPerformed
        if (EmployeeSwingUtils.showConfirmDialog("Are you sure to delete ?") == JOptionPane.NO_OPTION) {
            return;
        } else {
            deleteAction();
            refreshAction(false);
        }
    }//GEN-LAST:event_btRemoveActionPerformed

    private void cbStatusFilterItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbStatusFilterItemStateChanged
        doFilter();
    }//GEN-LAST:event_cbStatusFilterItemStateChanged

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        clearFilter();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void btChangePassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btChangePassActionPerformed
        if (selectedUser.getUserID() == 0 || selectedUser.getUserName().equals(LoginFrame.config.userName)) {
            selectedUser = new UserDAOImpl().getUserFromName(LoginFrame.config.userName);
            UserPasswordDialog upw = new UserPasswordDialog(selectedUser);
            upw.setTitle("Change your password !");
            upw.setVisible(true);
        } else {
            AdminPasswordDialog pw = new AdminPasswordDialog(selectedUser);
            pw.setTitle("Change password for user:    " + selectedUser.getUserName());
            pw.setVisible(true);
            refreshAction(false);
        }

    }//GEN-LAST:event_btChangePassActionPerformed
    //// </editor-fold>
    //<editor-fold defaultstate="collapsed" desc="khai bao component">
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btAdd;
    private javax.swing.JButton btChangePass;
    private javax.swing.JButton btPermission;
    private javax.swing.JButton btRefresh;
    private javax.swing.JButton btRemove;
    private javax.swing.JComboBox<String> cbStatusFilter;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPanel pnFilter;
    private javax.swing.JPanel pnTitle;
    private javax.swing.JTable tbUserList;
    private javax.swing.JTextField tfEmpNameFilter;
    private javax.swing.JTextField tfIdFilter;
    private javax.swing.JTextField tfUserNameFilter;
    // End of variables declaration//GEN-END:variables
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="doFilter">
    private void doFilter() {
        RowFilter<UserTableModel, Object> rf = null;

        //Filter theo regex, neu parse bi loi thi khong filter
        try {
            List<RowFilter<UserTableModel, Object>> filters = new ArrayList<>();
            filters.add(RowFilter.regexFilter("^" + tfIdFilter.getText(), 0));
            filters.add(RowFilter.regexFilter("^" + tfUserNameFilter.getText(), 1));
            filters.add(RowFilter.regexFilter("^" + tfEmpNameFilter.getText(), 2));
            // Neu status khac "All" thi moi filter
            String statusFilter = cbStatusFilter.getSelectedItem().toString();
            if (!statusFilter.equals("All")) {
                filters.add(RowFilter.regexFilter(
                        statusFilter.equals("Enabled") ? "t" : "f", 3));
            }

            rf = RowFilter.andFilter(filters);
        } catch (java.util.regex.PatternSyntaxException e) {
            return;
        }
        sorter.setRowFilter(rf);
    }//filter
//</editor-fold>

    private void clearFilter() {
        tfIdFilter.setText("");
        tfEmpNameFilter.setText("");
        tfUserNameFilter.setText("");
        cbStatusFilter.setSelectedIndex(0);
    }

    private void fetchAction() {
        selectedRowIndex = tbUserList.getSelectedRow();
        selectedUser.setUserID((int) tbUserList.getValueAt(selectedRowIndex, 0));
        selectedUser.setUserName((String) tbUserList.getValueAt(selectedRowIndex, 1));
        selectedUser.setEmpName((String) tbUserList.getValueAt(selectedRowIndex, 2));
        selectedUser.setUserEnable((boolean) tbUserList.getValueAt(selectedRowIndex, 3));
        selectedUser.setPassword((String) tbUserList.getValueAt(selectedRowIndex, 4));
        selectedUser.setEmpID((int) tbUserList.getValueAt(selectedRowIndex, 5));
    }

    private void refreshAction(boolean mustInfo) {
        if (mustInfo) {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            // Refresh table
            userTableModel.refresh();
            employeeComboBoxModel1.refresh();
            setCursor(null);
            EmployeeSwingUtils.showInfoDialog(EmployeeSwingUtils.DB_REFRESH);
        } else {
            // Refresh table
            userTableModel.refresh();
            employeeComboBoxModel1.refresh();
        }
        scrollToRow(selectedRowIndex);
    }

    private boolean checkRoot() {
        UserDAOImpl u = new UserDAOImpl();
        boolean result = u.checkRoot(userName);
        return result;
    }

    private boolean checkPermissionUpdate() {
        UserDAOImpl u = new UserDAOImpl();
        boolean result = u.checkPermissionUpdate(userName);
        return result;
    }

    private boolean checkChangePass() {
        UserDAOImpl u = new UserDAOImpl();
        boolean result = u.checkChangePassForAdmin(userName, selectedUser);
        return result;
    }

    private boolean checkUpdate() {
        UserDAOImpl u = new UserDAOImpl();
        boolean result = u.checkUpdateRecord(userName, selectedUser);
        return result;
    }

    private void insertAction() {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        boolean result = userTableModel.insert(new User());
        setCursor(null);
        EmployeeSwingUtils.showInfoDialog(result ? EmployeeSwingUtils.INSERT_SUCCESS : EmployeeSwingUtils.INSERT_FAIL);
        // Select row vua insert vao
        selectedRowIndex = tbUserList.getRowCount() - 1;
        scrollToRow(selectedRowIndex);
        tbUserList.editCellAt(tbUserList.getSelectedRow(), 1);
    }

    private void updateAction() {
        if (checkUpdate() == true) {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            boolean result = userTableModel.update(selectedUser);
            refreshAction(false);
            setCursor(null);
            EmployeeSwingUtils.showInfoDialog(result ? EmployeeSwingUtils.UPDATE_SUCCESS : EmployeeSwingUtils.UPDATE_FAIL);
            scrollToRow(selectedRowIndex);
        } else {
            EmployeeSwingUtils.showErrorDialog("You can't update this user !");
            refreshAction(false);
        }

    }

    private void deleteAction() {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        boolean result = userTableModel.delete(selectedUser);
        setCursor(null);
        EmployeeSwingUtils.showInfoDialog(result ? EmployeeSwingUtils.DELETE_SUCCESS : EmployeeSwingUtils.DELETE_FAIL);

        // Neu row xoa la row cuoi thi lui cursor ve
        // Neu row xoa la row khac cuoi thi tien cursor ve truoc
        selectedRowIndex = (selectedRowIndex == tbUserList.getRowCount() ? tbUserList.getRowCount() - 1 : selectedRowIndex++);
        scrollToRow(selectedRowIndex);
    }

    private void scrollToRow(int row) {
        tbUserList.getSelectionModel().setSelectionInterval(row, row);
        tbUserList.scrollRectToVisible(new Rectangle(tbUserList.getCellRect(row, 0, true)));
    }

    private void setButtonEnabled(boolean enabled, JButton... exclude) {
        btRemove.setEnabled(enabled);
//        btAdd.setEnabled(enabled);
//        btChangePass.setEnabled(enabled);
//        btPermission.setEnabled(enabled);

        // Ngoai tru may button nay luon luon enable
        if (exclude.length != 0) {
            Arrays.stream(exclude).forEach(b -> b.setEnabled(true));
        }
    }

    private void setButtonChangePassEnabled(boolean enabled, JButton... exclude) {

        btChangePass.setEnabled(enabled);
//        btRemove.setEnabled(enabled);
        // Ngoai tru may button nay luon luon enable
        if (exclude.length != 0) {
            Arrays.stream(exclude).forEach(b -> b.setEnabled(true));
        }
    }

    private void setButtonPermissionEnabled(boolean enabled, JButton... exclude) {

        btPermission.setEnabled(enabled);
        // Ngoai tru may button nay luon luon enable
        if (exclude.length != 0) {
            Arrays.stream(exclude).forEach(b -> b.setEnabled(true));
        }
    }
}
