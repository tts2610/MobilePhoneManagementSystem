/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package supplier.controller;

import database.DBProvider;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.AbstractAction;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import static javax.swing.SwingConstants.CENTER;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.RowSorterEvent;
import javax.swing.event.RowSorterListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableRowSorter;
import main.controller.LoginFrame;
import main.model.UserFunction;
import org.jdesktop.xswingx.PromptSupport;
import supplier.model.Supplier;
import supplier.model.SupplierBranchTableModel;
import supplier.model.SupplierTableModel;
import utility.StringCellEditor;
import utility.SwingUtils;
import utility.TableCellListener;

/**
 *
 * @author tuan
 */
public class SupplierPanel extends javax.swing.JPanel {
    
     private SupplierTableModel supplierTableModel;
     private SupplierBranchTableModel supplierBranchTableModel;
   private final TableRowSorter<SupplierTableModel> sorter;
   
   // Branch dang duoc chon trong table
    private Supplier selectedSupplier;
    private int selectedRowIndex;
    
    
     private static int COL_ID = 0;
    private static int COL_SupName = 1;
    private static int COL_Status = 3;
    private static int COL_SupAddress = 2;
    public SupplierPanel() {
        initComponents();
        selectedSupplier = new Supplier();
        
        //set data cho table
        supplierTableModel = new SupplierTableModel();
        tbSupplierList.setModel(supplierTableModel);
        supplierBranchTableModel = new SupplierBranchTableModel();
        tbBranchList.setModel(supplierBranchTableModel);
        
        // Set sorter cho table
        sorter = new TableRowSorter<>(supplierTableModel);
        tbSupplierList.setRowSorter(sorter);
        
        //bat su kien cho sorter
        sorter.addRowSorterListener(new RowSorterListener() {

            @Override
            public void sorterChanged(RowSorterEvent e) {
                if (tbSupplierList.getRowCount() == 0|| tbSupplierList.getSelectedRow()==-1) {
                    btRemove.setEnabled(false);
                    
                } else {
                    btRemove.setEnabled(true);
                    
                }
            }
        });
        // Bat su kien select row tren table
        tbSupplierList.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
            
            DefaultListSelectionModel model = (DefaultListSelectionModel) e.getSource();
            if (!model.isSelectionEmpty()) {
                fetchSupplierDetails();
                tbSupplierList.setSurrendersFocusOnKeystroke(false);
                btRemove.setEnabled(true);
            } 
        });
        //customize combobox status
        cbStatusFilter.setRenderer(new DefaultListCellRenderer() {
            JTextField jtf = new JTextField();

            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                if (isSelected) {
                    jtf.setBackground(Color.ORANGE);
                    jtf.setForeground(Color.BLACK);

                } else {
                    jtf.setBackground(new java.awt.Color(51, 51, 51));
                    jtf.setForeground(list.getForeground());
                }
                jtf.setText((String) value);
                jtf.setBorder(null);
                jtf.setHorizontalAlignment(CENTER);
                return jtf;
            }

        });
        
        
        //bat su kien update cho table
        TableCellListener tcl = new TableCellListener(tbSupplierList, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TableCellListener tcl = (TableCellListener) e.getSource();
//                System.out.println("Row   : " + tcl.getRow());
//                System.out.println("Column: " + tcl.getColumn());
//                System.out.println("Old   : " + tcl.getOldValue());
//                System.out.println("New   : " + tcl.getNewValue());

                switch (tcl.getColumn()) {
                    case 1:
                        selectedSupplier.setSupName((String) tcl.getNewValue());
                        
                        break;
                    case 2:
                        selectedSupplier.setSupAddress((String) tcl.getNewValue());
                        break;
                    case 3:
                        selectedSupplier.setSupStatus((boolean) tcl.getNewValue());
                        break;
                    }
                int ans = SwingUtils.showConfirmDialog("Are you sure to update?");
                if(ans==JOptionPane.YES_OPTION)
                    updateAction();
                else
                    supplierTableModel.refresh();
            }
        });
        
        // Bat su kien cho vung filter
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
        tfNameFilter.getDocument().addDocumentListener(
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
        
        tfAddressFilter.getDocument().addDocumentListener(
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
        
        formatTable();
        btRemove.setEnabled(false);
        // Check permission inbound
        if (!LoginFrame.checkPermission(new UserFunction(UserFunction.FG_SUPPLIER, UserFunction.FN_UPDATE))) {
            btAdd.setEnabled(false);
            btRemove.setEnabled(false);
        }
        
        PromptSupport.setPrompt("Find ID", tfIdFilter);
        PromptSupport.setPrompt("Find Name", tfNameFilter);
        PromptSupport.setPrompt("Find Address", tfAddressFilter);
        
        PromptSupport.setFocusBehavior(PromptSupport.FocusBehavior.SHOW_PROMPT, tfIdFilter);
        PromptSupport.setFocusBehavior(PromptSupport.FocusBehavior.SHOW_PROMPT, tfNameFilter);
        PromptSupport.setFocusBehavior(PromptSupport.FocusBehavior.SHOW_PROMPT, tfAddressFilter);
    }
    public void formatTable(){
        tbSupplierList.getTableHeader().setPreferredSize(new Dimension(300, 30));
        
         //alignment component
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        //id
        tbSupplierList.getColumnModel().getColumn(COL_ID).setMinWidth(73);
        tbSupplierList.getColumnModel().getColumn(COL_ID).setMaxWidth(73);
        tbSupplierList.getColumnModel().getColumn(COL_ID).setCellRenderer(centerRenderer);
        //name
//        tbSupplierList.getColumnModel().getColumn(COL_SupName).setMinWidth(100);
//        tbSupplierList.getColumnModel().getColumn(COL_SupName).setMaxWidth(100);
        tbSupplierList.getColumnModel().getColumn(COL_SupName).setCellRenderer(centerRenderer);
        tbSupplierList.getColumnModel().getColumn(COL_SupName).setCellEditor(new StringCellEditor(1, 50, SwingUtils.PATTERN_NAMEWITHSPACE));
        
         //address
//        tbSupplierList.getColumnModel().getColumn(COL_SupAddress).setMinWidth(200);
//        tbSupplierList.getColumnModel().getColumn(COL_SupAddress).setMaxWidth(200);
        tbSupplierList.getColumnModel().getColumn(COL_SupAddress).setCellRenderer(centerRenderer);
        tbSupplierList.getColumnModel().getColumn(COL_SupAddress).setCellEditor(new StringCellEditor(1, 300, SwingUtils.PATTERN_ADDRESS));

        //status
        tbSupplierList.getColumnModel().getColumn(COL_Status).setMinWidth(73);
        tbSupplierList.getColumnModel().getColumn(COL_Status).setMaxWidth(73);
        
       
        //id
        tbBranchList.getColumnModel().getColumn(COL_ID).setMinWidth(73);
        tbBranchList.getColumnModel().getColumn(COL_ID).setMaxWidth(73);
        tbBranchList.getColumnModel().getColumn(COL_ID).setCellRenderer(centerRenderer);
        //name
        tbBranchList.getColumnModel().getColumn(COL_SupName).setCellRenderer(centerRenderer);
        
        //status
        tbBranchList.getColumnModel().getColumn(2).setMinWidth(73);
        tbBranchList.getColumnModel().getColumn(2).setMaxWidth(73);
        
    }
    private void doFilter() {
        RowFilter<SupplierTableModel, Object> rf = null;

        //Filter theo regex, neu parse bi loi thi khong filter
        try {
            List<RowFilter<SupplierTableModel, Object>> filters = new ArrayList<>();
            filters.add(RowFilter.regexFilter("^" + tfIdFilter.getText(), 0));
            filters.add(RowFilter.regexFilter("^" + tfNameFilter.getText(), 1));
            filters.add(RowFilter.regexFilter("^" + tfAddressFilter.getText(), 2));
            
            // Neu status khac "Choose" thi moi filter
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
    }
    private void fetchSupplierDetails() {
        selectedRowIndex = tbSupplierList.getSelectedRow();
        selectedSupplier.setSupID((int) tbSupplierList.getValueAt(selectedRowIndex, 0));
        selectedSupplier.setSupName((String) tbSupplierList.getValueAt(selectedRowIndex, 1));
        selectedSupplier.setSupAddress((String) tbSupplierList.getValueAt(selectedRowIndex, 2));
        selectedSupplier.setSupStatus((boolean) tbSupplierList.getValueAt(selectedRowIndex, 3));
        supplierBranchTableModel.load(selectedSupplier.getSupID());
    }
    private void updateAction() {
        if (supplierTableModel.update(selectedSupplier)) {
            JOptionPane.showMessageDialog(this, "Update successfully");
        } else {
            JOptionPane.showMessageDialog(this, "Update failed");
        }
        supplierTableModel.refresh();
        formatTable();
    }
    
    private void insertAction() {
        Supplier supplier = new Supplier();
        
        supplier.setSupName("Default Name "+ System.currentTimeMillis());
        supplier.setSupAddress("Default address "+ System.currentTimeMillis());
        supplier.setSupStatus(true);
        if (supplierTableModel.insert(supplier)) {
            JOptionPane.showMessageDialog(this, "Insert successfully");

          supplierTableModel.refresh();
        } else {
            JOptionPane.showMessageDialog(this, "Insert failed");
        }
        formatTable();
    }
    
    private void moveScrollToRow(int row) {
        tbSupplierList.getSelectionModel().setSelectionInterval(row, row);
        tbSupplierList.scrollRectToVisible(new Rectangle(tbSupplierList.getCellRect(row, 0, true)));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        tfIdFilter = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        tfNameFilter = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        cbStatusFilter = new javax.swing.JComboBox<>();
        tfAddressFilter = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        btAdd = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbSupplierList = new javax.swing.JTable();
        btRefresh = new javax.swing.JButton();
        btRemove = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbBranchList = new javax.swing.JTable();

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Filter", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 18), new java.awt.Color(255, 153, 0))); // NOI18N

        jLabel1.setText("Supplier ID:");

        jLabel2.setText("Supplier Name:");

        jLabel3.setText("Supplier Address:");

        jLabel4.setText("Status:");

        cbStatusFilter.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All", "Enabled", "Disabled" }));
        cbStatusFilter.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbStatusFilterItemStateChanged(evt);
            }
        });

        tfAddressFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tfAddressFilterActionPerformed(evt);
            }
        });

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/product/refresh_1.png"))); // NOI18N
        jButton1.setBorderPainted(false);
        jButton1.setContentAreaFilled(false);
        jButton1.setFocusPainted(false);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tfIdFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbStatusFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tfNameFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tfAddressFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(tfIdFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel1)
                .addComponent(jLabel4)
                .addComponent(cbStatusFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel2)
                .addComponent(tfNameFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel3)
                .addComponent(tfAddressFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(jButton1)
        );

        btAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/product/Add.png"))); // NOI18N
        btAdd.setText("Add New");
        btAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btAddActionPerformed(evt);
            }
        });

        jScrollPane2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Suppliers", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 18), new java.awt.Color(255, 153, 0))); // NOI18N

        tbSupplierList.setAutoCreateRowSorter(true);
        tbSupplierList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Supplier ID", "Supplier Name", "Supplier Address", "Status"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Boolean.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tbSupplierList.setFillsViewportHeight(true);
        tbSupplierList.setRowHeight(25);
        tbSupplierList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane2.setViewportView(tbSupplierList);

        btRefresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/product/Refresh.png"))); // NOI18N
        btRefresh.setText("Refresh");
        btRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btRefreshActionPerformed(evt);
            }
        });

        btRemove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/product/Delete.png"))); // NOI18N
        btRemove.setText("Remove");
        btRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btRemoveActionPerformed(evt);
            }
        });

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel5.setFont(new java.awt.Font("Lucida Sans", 1, 24)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 255, 255));
        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/main/Supplier.png"))); // NOI18N
        jLabel5.setText("<html><u><i><font color='red'>S</font>upplier <font color='red'>M</font>anagement</i></u></html>");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tbBranchList.setAutoCreateRowSorter(true);
        tbBranchList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tbBranchList.setFillsViewportHeight(true);
        tbBranchList.setRowHeight(25);
        jScrollPane1.setViewportView(tbBranchList);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(201, Short.MAX_VALUE)
                .addComponent(btAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(btRemove, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(181, Short.MAX_VALUE))
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane2)
            .addComponent(jScrollPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btAdd)
                    .addComponent(btRemove)
                    .addComponent(btRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(32, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void cbStatusFilterItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbStatusFilterItemStateChanged
        doFilter();
    }//GEN-LAST:event_cbStatusFilterItemStateChanged

    private void btAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btAddActionPerformed
        insertAction();
        // Select row vua insert vao
        selectedRowIndex = 0;
        scrollToRow(selectedRowIndex);
        tbSupplierList.editCellAt(tbSupplierList.getSelectedRow(), 1);
        tbSupplierList.getEditorComponent().requestFocus();
    }//GEN-LAST:event_btAddActionPerformed

    private void btRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btRefreshActionPerformed
        tfIdFilter.setText("");
        tfAddressFilter.setText("");
        tfNameFilter.setText("");
        cbStatusFilter.setSelectedIndex(0);
        supplierTableModel.refresh();
        SwingUtils.showInfoDialog(SwingUtils.DB_REFRESH);
    }//GEN-LAST:event_btRefreshActionPerformed

    private void tfAddressFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfAddressFilterActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tfAddressFilterActionPerformed

    private void btRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btRemoveActionPerformed
        deleteAction();
    }//GEN-LAST:event_btRemoveActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
       resetAction();
    }//GEN-LAST:event_jButton1ActionPerformed
    private void resetAction(){
        tfIdFilter.setText("");
        tfNameFilter.setText("");
        tfAddressFilter.setText("");
        cbStatusFilter.setSelectedIndex(0);
        
    }
    
    
    
    private void deleteAction() {
        int ans = SwingUtils.showConfirmDialog("Are you sure to delete?");
        if(ans==JOptionPane.YES_OPTION){
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        boolean result = supplierTableModel.delete(selectedSupplier);
        setCursor(null);
        if(result)
            SwingUtils.showInfoDialog(SwingUtils.DELETE_SUCCESS);
        else
            SwingUtils.showInfoDialog(SwingUtils.DELETE_FAIL);

        // Neu row xoa la row cuoi thi lui cursor ve
        // Neu row xoa la row khac cuoi thi tien cursor ve truoc
        selectedRowIndex = (selectedRowIndex == tbSupplierList.getRowCount() ? tbSupplierList.getRowCount() - 1 : selectedRowIndex++);
        scrollToRow(selectedRowIndex);
        formatTable();
        supplierTableModel.refresh();
        }
    }
 private void scrollToRow(int row) {
        tbSupplierList.getSelectionModel().setSelectionInterval(row, row);
        tbSupplierList.scrollRectToVisible(new Rectangle(tbSupplierList.getCellRect(row, 0, true)));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btAdd;
    private javax.swing.JButton btRefresh;
    private javax.swing.JButton btRemove;
    private javax.swing.JComboBox<String> cbStatusFilter;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable tbBranchList;
    private javax.swing.JTable tbSupplierList;
    private javax.swing.JTextField tfAddressFilter;
    private javax.swing.JTextField tfIdFilter;
    private javax.swing.JTextField tfNameFilter;
    // End of variables declaration//GEN-END:variables
}
