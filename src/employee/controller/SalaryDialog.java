/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package employee.controller;

import com.toedter.calendar.JDateChooser;
import employee.model.IntegerCurrencyCellRenderer;
import employee.model.CurrencyDoubleCellRenderer;
import employee.model.Employee;
import employee.model.Salary;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.TableRowSorter;
import main.controller.LoginFrame;
import main.model.UserFunction;
import employee.model.EmployeeSwingUtils;
import employee.model.SalaryDAOImpl;
import javax.swing.JLabel;
import javax.swing.table.DefaultTableCellRenderer;
import utility.SpinnerCellEditor;
import utility.TableCellListener;

/**
 *
 * @author Hoang
 */
public class SalaryDialog extends javax.swing.JDialog {

    private JDateChooser dcFilter;
    private SalaryTableModel salaryTableModel;
    private TableRowSorter<SalaryTableModel> sorter;

    // Salary dang duoc chon trong table
    private Salary selectedSalary;
    private int selectedRowIndex = -1;
    private int backupRowCount;
    Employee employee;

// Define some column constants
    private static final int COL_SALID = 0;
    private static final int COL_EMPID = 1;
    private static final int COL_MONTH = 2;
    private static final int COL_PAYDAY = 3;
    private static final int COL_WORKDAYS = 4;
    private static final int COL_OFFDAYS = 5;
    private static final int COL_BONUS = 6;
    private static final int COL_BASICSALARY = 7;
    private static final int COL_TOTALSALARY = 8;
    private List<Salary> salary;

    public SalaryDialog(Employee employee) {
        //<editor-fold defaultstate="collapsed" desc="constructor">      
        super((JFrame) null, true);
        initComponents();
        this.employee = employee;
        setLocationRelativeTo(null);
        //Disable button khi moi khoi dong len
        setButtonEnabled(false);

        // Selecting customer in the table
        selectedSalary = new Salary();
        // Set data cho table
        salaryTableModel = new SalaryTableModel();

        tbSalaryList.setModel(salaryTableModel);
// Set sorter cho table
        sorter = new TableRowSorter<>(salaryTableModel);

        tbSalaryList.setRowSorter(sorter);
        // Set height cho table header
        tbSalaryList.getTableHeader().setPreferredSize(new Dimension(300, 30));
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tbSalaryList.getColumnModel().getColumn(COL_MONTH).setCellRenderer(centerRenderer);
        tbSalaryList.getColumnModel().getColumn(COL_PAYDAY).setCellRenderer(centerRenderer);
        tbSalaryList.getColumnModel().getColumn(COL_OFFDAYS).setCellRenderer(centerRenderer);
        tbSalaryList.getColumnModel().getColumn(COL_WORKDAYS).setCellRenderer(centerRenderer);

        // Col Ser ID (HIDDEN)
        tbSalaryList.getColumnModel().getColumn(COL_SALID).setMinWidth(0);
        tbSalaryList.getColumnModel().getColumn(COL_SALID).setMaxWidth(0);
        tbSalaryList.getColumnModel().getColumn(COL_EMPID).setMinWidth(0);
        tbSalaryList.getColumnModel().getColumn(COL_EMPID).setMaxWidth(0);
        // Col pro ID (HIDDEN)
        tbSalaryList.getColumnModel().getColumn(COL_MONTH).setMinWidth(100);
        tbSalaryList.getColumnModel().getColumn(COL_MONTH).setMaxWidth(100);
        // Col quantity
        tbSalaryList.getColumnModel().getColumn(COL_PAYDAY).setMinWidth(130);
        tbSalaryList.getColumnModel().getColumn(COL_PAYDAY).setMaxWidth(130);
//        tbSalaryList.getColumnModel().getColumn(COL_PAYDAY).setCellEditor(new DateCellWorkingDateEditor());

        // Col oderid
        tbSalaryList.getColumnModel().getColumn(COL_WORKDAYS).setMinWidth(70);
        tbSalaryList.getColumnModel().getColumn(COL_WORKDAYS).setMaxWidth(70);
        tbSalaryList.getColumnModel().getColumn(COL_WORKDAYS).setCellEditor(new SpinnerCellEditor(14, 28));
        // Col oderid
        tbSalaryList.getColumnModel().getColumn(COL_OFFDAYS).setMinWidth(60);
        tbSalaryList.getColumnModel().getColumn(COL_OFFDAYS).setMaxWidth(60);
        tbSalaryList.getColumnModel().getColumn(COL_OFFDAYS).setCellEditor(new SpinnerCellEditor(0, 7));
        tbSalaryList.getColumnModel().getColumn(COL_BONUS).setMinWidth(100);
        tbSalaryList.getColumnModel().getColumn(COL_BONUS).setCellRenderer(new IntegerCurrencyCellRenderer());
        tbSalaryList.getColumnModel().getColumn(COL_BASICSALARY).setMinWidth(100);
        tbSalaryList.getColumnModel().getColumn(COL_BASICSALARY).setCellRenderer(new IntegerCurrencyCellRenderer());
        tbSalaryList.getColumnModel().getColumn(COL_TOTALSALARY).setMinWidth(100);
        tbSalaryList.getColumnModel().getColumn(COL_TOTALSALARY).setCellRenderer(new CurrencyDoubleCellRenderer());

        // Bat su kien select row tren table product
        tbSalaryList.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
            DefaultListSelectionModel model = (DefaultListSelectionModel) e.getSource();
            if (!model.isSelectionEmpty()) {
                fetchAction();
                setButtonEnabled(true);
            } else {
                setButtonEnabled(false);
            }
        }
        );

        // Set data cho table chinh
        salaryTableModel.load(employee.getEmpID());
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Set cell listener cho updating">
        TableCellListener tcl = new TableCellListener(tbSalaryList, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TableCellListener tcl = (TableCellListener) e.getSource();
                Calendar cal = Calendar.getInstance();
                int yNow = cal.get(Calendar.YEAR);
                int mNow = cal.get(Calendar.MONTH);

                cal.setTime(selectedSalary.getPayDay());
                int yPay, mPay;
                switch (tcl.getColumn()) {

                    case COL_WORKDAYS:
                        cal.setTime(selectedSalary.getPayDay());
                        yPay = cal.get(Calendar.YEAR);
                        mPay = cal.get(Calendar.MONTH);
                        if (!(yPay == yNow && mPay == mNow)) {
                            EmployeeSwingUtils.showErrorDialog("Can't update workdays of the month before!");
                            refreshAction(false);
                            return;
                        } else {
                            selectedSalary.setWorkDays((int) tcl.getNewValue());
                        }
                        break;
                    case COL_OFFDAYS:
                        cal.setTime(selectedSalary.getPayDay());
                        yPay = cal.get(Calendar.YEAR);
                        mPay = cal.get(Calendar.MONTH);
                        if (!(yPay == yNow && mPay == mNow)) {
                            EmployeeSwingUtils.showErrorDialog("Can't update offdays of the month before!");
                            refreshAction(false);
                            return;
                        } else {
                            selectedSalary.setOffDays((int) tcl.getNewValue());
                        }
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

        if (!LoginFrame.checkPermission(
                new UserFunction(UserFunction.FG_SALARY, UserFunction.FN_UPDATE))) {
            tbSalaryList.setEnabled(false);
            btAdd.setEnabled(false);
            btRemove.setEnabled(false);
        }

        tbSalaryList.setAutoCreateColumnsFromModel(
                false);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tbSalaryList = new javax.swing.JTable();
        btAdd = new javax.swing.JButton();
        btRemove = new javax.swing.JButton();
        btRefresh = new javax.swing.JButton();
        btCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Salary");
        setMaximumSize(new java.awt.Dimension(738, 458));
        setMinimumSize(new java.awt.Dimension(738, 458));
        setModal(true);
        setResizable(false);

        jScrollPane1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Salary List"));

        tbSalaryList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "SalID", "Month", "Payday", "WorkDays", "OffDays"
            }
        ));
        tbSalaryList.setRowHeight(20);
        tbSalaryList.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(tbSalaryList);

        btAdd.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        btAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/order/Add3.png"))); // NOI18N
        btAdd.setText("Add");
        btAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btAddActionPerformed(evt);
            }
        });

        btRemove.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        btRemove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/order/Delete2.png"))); // NOI18N
        btRemove.setText("Remove");
        btRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btRemoveActionPerformed(evt);
            }
        });

        btRefresh.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        btRefresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/order/Refresh2.png"))); // NOI18N
        btRefresh.setText("Refresh");
        btRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btRefreshActionPerformed(evt);
            }
        });

        btCancel.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        btCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/order/Cancel2.png"))); // NOI18N
        btCancel.setText("Close");
        btCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btCancelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(146, Short.MAX_VALUE)
                .addComponent(btAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btRemove)
                .addGap(13, 13, 13)
                .addComponent(btRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(146, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 406, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btRemove, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btCancelActionPerformed
        dispose();
    }//GEN-LAST:event_btCancelActionPerformed

    private void btAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btAddActionPerformed
        insertAction();
    }//GEN-LAST:event_btAddActionPerformed

    private void btRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btRemoveActionPerformed

        deleteAction();
    }//GEN-LAST:event_btRemoveActionPerformed

    private void btRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btRefreshActionPerformed
        refreshAction(false);
    }//GEN-LAST:event_btRefreshActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btAdd;
    private javax.swing.JButton btCancel;
    private javax.swing.JButton btRefresh;
    private javax.swing.JButton btRemove;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tbSalaryList;
    // End of variables declaration//GEN-END:variables

    private void insertAction() {

        Salary details = new Salary();
        details.setWorkDays(22);
        details.setOffDays(0);
        Calendar calendar = Calendar.getInstance();
        int y = calendar.get(Calendar.YEAR);
        int m = calendar.get(Calendar.MONTH);
//        System.out.println(m);
        calendar.set(y, m, 5);
//        System.out.println(calendar.getTime());
        details.setPayDay(calendar.getTime());
//        details.setPayDay(new Date());
        details.setMonth(m);
        details.setEmpID(employee.getEmpID());
        details.setBasicSalary(employee.getEmpSalary());
        details.setBonus(employee.getEmpBonus());

//        System.out.println("salary:   " + details);
        SalaryDAOImpl sd = new SalaryDAOImpl();
        if (sd.checkPayDay(details) == true) {
            salaryTableModel.insert(details);
            refreshAction(false);
        } else {
            EmployeeSwingUtils.showErrorDialog("Salary of last month was paid !");
        }
        scrollToRow(tbSalaryList.getRowCount() - 1);
        //
    }

    private void deleteAction() {

        if (selectedRowIndex == -1) {
            EmployeeSwingUtils.showErrorDialog("Choose salary to delete !");
            return;
        } else {
            Calendar cal = Calendar.getInstance();
            int yNow = cal.get(Calendar.YEAR);
            int mNow = cal.get(Calendar.MONTH);

            cal.setTime(selectedSalary.getPayDay());
            int yPay = cal.get(Calendar.YEAR);
            int mPay = cal.get(Calendar.MONTH);
            if (!(yPay == yNow && mPay == mNow)) {
                EmployeeSwingUtils.showErrorDialog("Can't delete salary of the month before!");

            } else if (EmployeeSwingUtils.showConfirmDialog("Are you sure to delete ?") == JOptionPane.NO_OPTION) {
                return;
            } else if (salaryTableModel.delete(selectedSalary)) {
                EmployeeSwingUtils.showInfoDialog(EmployeeSwingUtils.DELETE_SUCCESS);

            } else {
                EmployeeSwingUtils.showInfoDialog(EmployeeSwingUtils.DELETE_FAIL);
            }
        }
        refreshAction(false);
//        System.out.println("RowCount: "+tbSalaryList.getRowCount());
        // Neu row xoa la row cuoi thi lui cursor ve
        // Neu row xoa la row khac cuoi thi tien cursor ve truoc
        selectedRowIndex = 0;
//        selectedRowIndex = (selectedRowIndex == tbSalaryList.getRowCount() ? tbSalaryList.getRowCount() - 1 : selectedRowIndex++);

        scrollToRow(selectedRowIndex);
        //
    }

    private void refreshAction(boolean mustInfo) {
        if (mustInfo) {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            // Refresh table
            salaryTableModel.load(employee.getEmpID());
//            salaryTableModel.refresh();

            setCursor(null);
            EmployeeSwingUtils.showInfoDialog(EmployeeSwingUtils.DB_REFRESH);
        } else {
            // Refresh table
            salaryTableModel.load(employee.getEmpID());
//            salaryTableModel.refresh();
        }
        //
//        scrollToRow(selectedRowIndex);

    }

    // Ham goi khi bam nut Save
    private void updateAction() {
        if (salaryTableModel.update(selectedSalary)) {
            EmployeeSwingUtils.showInfoDialog(EmployeeSwingUtils.UPDATE_SUCCESS);

        } else {
            EmployeeSwingUtils.showInfoDialog(EmployeeSwingUtils.UPDATE_FAIL);
        }
        refreshAction(false);
        //
    }

    private void cancelAction() {
        dispose();
    }

    private void scrollToRow(int row) {
        tbSalaryList.getSelectionModel().setSelectionInterval(row, row);
        tbSalaryList.scrollRectToVisible(new Rectangle(tbSalaryList.getCellRect(row, 0, true)));
    }

    private void fetchAction() {
        selectedRowIndex = tbSalaryList.getSelectedRow();
        selectedSalary.setSalID((int) tbSalaryList.getValueAt(selectedRowIndex, 0));
        selectedSalary.setEmpID((int) tbSalaryList.getValueAt(selectedRowIndex, 1));
        selectedSalary.setMonth((int) tbSalaryList.getValueAt(selectedRowIndex, 2));
        selectedSalary.setPayDay((Date) tbSalaryList.getValueAt(selectedRowIndex, 3));
        selectedSalary.setWorkDays((int) tbSalaryList.getValueAt(selectedRowIndex, 4));
        selectedSalary.setOffDays((int) tbSalaryList.getValueAt(selectedRowIndex, 5));
        selectedSalary.setBonus((int) tbSalaryList.getValueAt(selectedRowIndex, 6));
        selectedSalary.setBasicSalary((int) tbSalaryList.getValueAt(selectedRowIndex, 7));

//        if (selectedRowIndex >= 0) {
//            int idx = tbSalaryList.convertRowIndexToModel(selectedRowIndex);
//            selectedSalary = salaryTableModel.getSalaryFromIndex(idx);
//            salaryTableModel.setSelectingIndex(idx);
//        } else {
//            selectedSalary = null;
//            salaryTableModel.setSelectingIndex(-1);
//        }
    }

    private void setButtonEnabled(boolean enabled, JButton... exclude) {

        btRemove.setEnabled(enabled);
//        btAdd.setEnabled(enabled);

        // Ngoai tru may button nay luon luon enable
        if (exclude.length != 0) {
            Arrays.stream(exclude).forEach(b -> b.setEnabled(true));
        }
    }

}
