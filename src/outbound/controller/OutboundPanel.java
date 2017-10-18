/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package outbound.controller;


import com.toedter.calendar.JDateChooser;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;
import org.jdesktop.xswingx.PromptSupport;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.RowFilter;
import javax.swing.RowFilter.ComparisonType;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.RowSorterEvent;
import javax.swing.event.RowSorterListener;
import javax.swing.table.TableColumnModel;
import main.controller.LoginFrame;
import main.model.UserFunction;
import outbound.model.Outbound;
import outbound.model.OutboundDAOImpl;
import outbound.model.OutboundProductTableModel;
import outbound.model.User;
import outbound.model.UserComboboxModel;
import outbound.model.UserNameComboboxRenderer;
import product.controller.ProductPanel;
import utility.SwingUtils;
import utility.TableCellListener;
/**
 *
 * @author tuan
 */
public class OutboundPanel extends javax.swing.JPanel {
    private JDateChooser dcFilter;

    // Khai bao 2 cai table model
    private OutboundTableModel outboundTableModel;
    private OutboundProductTableModel outboundProductTableModel;
    
    //khai bao user combobbox
    private UserComboboxModel userComboBoxModel;
    private UserComboboxModel userComboBoxModel1;
    private UserNameComboboxRenderer userNameComboBoxRenderer;
    
    
    
    
    //row sorter
    private TableRowSorter<OutboundTableModel> sorter;
    
    
    // Order dang duoc chon trong table order list
    private Outbound selectedOutbound;
    private int selectedRowIndex;
    private User filterUser;
    
    
    
    //format ngay
    SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy");
    JDateChooser jdcFromDate = new JDateChooser();
    JDateChooser jdcToDate = new JDateChooser();
    
    /*Outbound Table*/
    private static final int COL_InID = 0;
    private static final int COL_InDate = 1;
    private static final int COL_UserName = 3;
     private static final int COL_Content = 2;
    /*Product Table*/
    private static final int COL_PROID = 0;
    private static final int COL_PRONAME = 1;
    private static final int COL_PROQTY = 2;
    public OutboundPanel(){
        initComponents();
        // Selecting order in the table
        selectedOutbound = new Outbound();
        
        setting();
        // Set data cho combobox user filter
        userComboBoxModel = new UserComboboxModel();
        filterUser = new User(0, "All",true);
        userComboBoxModel.addElement(filterUser);

        // Set data cho column user combobox
        userComboBoxModel1 = new UserComboboxModel();

        // Set data cho combobox user update
        userNameComboBoxRenderer = new UserNameComboboxRenderer();
        cbUserFilter.setModel(userComboBoxModel);
        cbUserFilter.setRenderer(userNameComboBoxRenderer);
        

        
        
        // Set sorter cho table
        sorter = new TableRowSorter<>(outboundTableModel);
        tbOutboundList.setRowSorter(sorter);
        
        //event trong truong hop k co record trong table
        sorter.addRowSorterListener(new RowSorterListener() {

            @Override
            public void sorterChanged(RowSorterEvent e) {
                if (tbOutboundList.getRowCount() == 0||tbOutboundList.getSelectedRow()==-1) {
                    btRemove.setEnabled(false);
                    btUpdate.setEnabled(false);
                } else {
                    btRemove.setEnabled(true);
                    btUpdate.setEnabled(true);
                }
            }
        });
        
        
        // Select mac dinh cho level filter
        cbUserFilter.setSelectedIndex(cbUserFilter.getItemCount() - 1);
        
        formatTable();
        
       btUpdate.setEnabled(false);
       btRemove.setEnabled(false);
        
        // Check permission inbound
        if (!LoginFrame.checkPermission(new UserFunction(UserFunction.FG_OUTBOUND, UserFunction.FN_UPDATE))) {
            btAdd.setEnabled(false);
            btUpdate.setEnabled(false);
            btRemove.setEnabled(false);
        }
    }
    
    public void setting(){
        // Add prompt/hint to textfield
        PromptSupport.setPrompt("Find OutID", tfIdFilter);

        PromptSupport.setFocusBehavior(PromptSupport.FocusBehavior.SHOW_PROMPT, tfIdFilter);
        
        
        // Setup button state
       
        
        // ---------- Settup Jdatechooser
        jdcFromDate.setBounds(0, 0, 120, 20);
        jdcFromDate.setDateFormatString("MMM dd, yyyy");
        jdcToDate.setBounds(0, 0, 120, 20);
        jdcToDate.setDateFormatString("MMM dd, yyyy");
        jdcFromDate.getDateEditor().setEnabled(false);
        jdcToDate.getDateEditor().setEnabled(false);
        pnlFrom.add(jdcFromDate);
        pnlTo.add(jdcToDate);
        
        

        // Add listener for date selection change
        jdcFromDate.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                String crs = new OutboundDAOImpl().mainCRS;
                new OutboundDAOImpl().filterDate(jdcFromDate.getDate(),jdcToDate.getDate());
                outboundTableModel.refresh();
                new OutboundDAOImpl().setMainCRS(crs);
            }
        });
        jdcToDate.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                String crs = new OutboundDAOImpl().mainCRS;
                new OutboundDAOImpl().filterDate(jdcFromDate.getDate(),jdcToDate.getDate());
                outboundTableModel.refresh();
                new OutboundDAOImpl().setMainCRS(crs);
            }
        });

        tfIdFilter.getDocument().addDocumentListener(new DocumentListener() {
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
        
        

        // Start editing
        tfIdFilter.requestFocus();
        
        // Set data cho table
        outboundTableModel = new OutboundTableModel();
        outboundProductTableModel = new OutboundProductTableModel();
        tbOutboundList.setModel(outboundTableModel);
        tbProductList.setModel(outboundProductTableModel);
        
        // Bat su kien select row tren table sales off
        tbOutboundList.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
            DefaultListSelectionModel model = (DefaultListSelectionModel) e.getSource();
            if (!model.isSelectionEmpty()) {
                fetchAction();
                btUpdate.setEnabled(true);
                btRemove.setEnabled(true);
               
            }
        });
        
        
//        // Set table cell listener to update table sales off
//        TableCellListener tcl1 = new TableCellListener(tbOutboundList, new AbstractAction() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                TableCellListener tcl = (TableCellListener) e.getSource();
//                switch (tcl.getColumn()) {
//                    case COL_InDate:
//                        selectedInbound.setInDate((Date) tcl.getNewValue());
//                        break;
//                    case COL_SupName:
//                        selectedInbound.setSupName((String) tcl.getNewValue());
//                        break;
//                    case COL_SupInID:
//                        selectedInbound.setSupInvoiceID((String) tcl.getNewValue());
//                        break;
//                    case COL_UserName:
//                        selectedInbound.setUserName((String) tcl.getNewValue());
//                        break;
//                }
//                updateAction();
//            }
//        });
    }
    
    private void doFilter() {
        RowFilter<OutboundTableModel, Object> rf = null;

        //Filter theo regex, neu parse bi loi thi khong filter
        try {
            List<RowFilter<OutboundTableModel, Object>> filters = new ArrayList<>();
            filters.add(RowFilter.regexFilter("^" + tfIdFilter.getText(), 0));
            
            
            // Neu co chon cus level thi moi filter level
            if (cbUserFilter.getSelectedIndex() != cbUserFilter.getItemCount() - 1) {
                filters.add(RowFilter.regexFilter("^" + ((User) cbUserFilter.getSelectedItem()).getUsername(), 3));
            }
            
            
//            if(cbTarget.getSelectedItem()!=null){
//            if (cbTarget.getSelectedIndex() != cbTarget.getItemCount() - 1) {
//                
//                filters.add(RowFilter.regexFilter("^" + (cbTarget.getSelectedItem()), 2));
//            }
//            }

            rf = RowFilter.andFilter(filters);
        } catch (java.util.regex.PatternSyntaxException e) {
            return;
        }
        sorter.setRowFilter(rf);
    }
    
    private void fetchAction() {
        selectedRowIndex = tbOutboundList.getSelectedRow();
        selectedOutbound.setOutID((int) tbOutboundList.getValueAt(selectedRowIndex, 0));
        selectedOutbound.setOutDate((Date) tbOutboundList.getValueAt(selectedRowIndex, 1));
        selectedOutbound.setUserName((String) tbOutboundList.getValueAt(selectedRowIndex, 3));
        selectedOutbound.setOutContent((String) tbOutboundList.getValueAt(selectedRowIndex, 2));
        // Reload table product list voi Order moi chon
        outboundProductTableModel.load(selectedOutbound.getOutID());
    }
    

    
    

    

    
    
    public void formatTable(){
        tbOutboundList.getTableHeader().setPreferredSize(new Dimension(300, 30));
        tbProductList.getTableHeader().setPreferredSize(new Dimension(300, 30));
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        //id
        tbOutboundList.getColumnModel().getColumn(COL_InID).setMinWidth(70);
//        tbOutboundList.getColumnModel().getColumn(COL_InID).setMaxWidth(70);
        tbOutboundList.getColumnModel().getColumn(COL_InID).setCellRenderer(centerRenderer);
        
        tbOutboundList.getColumnModel().getColumn(COL_InDate).setMinWidth(150);
//        tbOutboundList.getColumnModel().getColumn(COL_InDate).setMaxWidth(150);
        tbOutboundList.getColumnModel().getColumn(COL_InDate).setCellRenderer(centerRenderer);
        
        
////        tbOutboundList.getColumnModel().getColumn(COL_Target).setMinWidth(150);
////        tbOutboundList.getColumnModel().getColumn(COL_Target).setMaxWidth(150);
////        tbOutboundList.getColumnModel().getColumn(COL_Target).setCellRenderer(centerRenderer);
//        
//        
      
        
        
        tbOutboundList.getColumnModel().getColumn(COL_Content).setMinWidth(475);
//        tbOutboundList.getColumnModel().getColumn(COL_Content).setMaxWidth(475);
        tbOutboundList.getColumnModel().getColumn(COL_Content).setCellRenderer(centerRenderer); 
        
          tbOutboundList.getColumnModel().getColumn(COL_UserName).setMinWidth(105);
//        tbOutboundList.getColumnModel().getColumn(COL_UserName).setMaxWidth(105);
        tbOutboundList.getColumnModel().getColumn(COL_UserName).setCellRenderer(centerRenderer);
        
        
        
         // Col pro ID
        tbProductList.getColumnModel().getColumn(COL_PROID).setMinWidth(70);
        tbProductList.getColumnModel().getColumn(COL_PROID).setMaxWidth(70);
        tbProductList.getColumnModel().getColumn(COL_PROID).setCellRenderer(centerRenderer);
        // Col pro name
        tbProductList.getColumnModel().getColumn(COL_PRONAME).setMinWidth(150);
        tbProductList.getColumnModel().getColumn(COL_PRONAME).setCellRenderer(centerRenderer);
        // Col quantity
        tbProductList.getColumnModel().getColumn(COL_PROQTY).setMinWidth(120);
        tbProductList.getColumnModel().getColumn(COL_PROQTY).setMaxWidth(120);
        tbProductList.getColumnModel().getColumn(COL_PROQTY).setCellRenderer(centerRenderer);
        
       
    }
   
    

    // Handler for list selection changes  
   

    
    
    public void resetFilter(){
        tfIdFilter.setText(null);
        jdcFromDate.setCalendar(null);
        
        jdcToDate.setCalendar(null);
        
//        cbTarget.setSelectedIndex(cbTarget.getItemCount() - 1);
        cbUserFilter.setSelectedIndex(cbUserFilter.getItemCount() - 1);
        
        
        
    }
    
   
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlSearch = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        tfIdFilter = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jButton5 = new javax.swing.JButton();
        cbUserFilter = new javax.swing.JComboBox<>();
        pnlFrom = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        pnlTo = new javax.swing.JPanel();
        tableScrollPane = new javax.swing.JScrollPane();
        tbOutboundList = new javax.swing.JTable();
        jScroll = new javax.swing.JScrollPane();
        tbProductList = new javax.swing.JTable();
        btAdd = new javax.swing.JButton();
        btUpdate = new javax.swing.JButton();
        btRemove = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();

        setPreferredSize(new java.awt.Dimension(810, 680));

        pnlSearch.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Filter", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 18), new java.awt.Color(255, 153, 0))); // NOI18N

        jLabel1.setText("Outbound ID:");

        tfIdFilter.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                tfIdFilterCaretUpdate(evt);
            }
        });
        tfIdFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tfIdFilterActionPerformed(evt);
            }
        });

        jLabel4.setText("To Date:");

        jLabel6.setText("User name:");

        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/product/refresh_1.png"))); // NOI18N
        jButton5.setBorderPainted(false);
        jButton5.setContentAreaFilled(false);
        jButton5.setFocusPainted(false);
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        cbUserFilter.setPreferredSize(new java.awt.Dimension(31, 32));
        cbUserFilter.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbUserFilterItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout pnlFromLayout = new javax.swing.GroupLayout(pnlFrom);
        pnlFrom.setLayout(pnlFromLayout);
        pnlFromLayout.setHorizontalGroup(
            pnlFromLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 123, Short.MAX_VALUE)
        );
        pnlFromLayout.setVerticalGroup(
            pnlFromLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jLabel3.setText("From Date:");

        javax.swing.GroupLayout pnlToLayout = new javax.swing.GroupLayout(pnlTo);
        pnlTo.setLayout(pnlToLayout);
        pnlToLayout.setHorizontalGroup(
            pnlToLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 120, Short.MAX_VALUE)
        );
        pnlToLayout.setVerticalGroup(
            pnlToLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout pnlSearchLayout = new javax.swing.GroupLayout(pnlSearch);
        pnlSearch.setLayout(pnlSearchLayout);
        pnlSearchLayout.setHorizontalGroup(
            pnlSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSearchLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tfIdFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbUserFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlFrom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlTo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        pnlSearchLayout.setVerticalGroup(
            pnlSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSearchLayout.createSequentialGroup()
                .addGroup(pnlSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(pnlSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(tfIdFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel3)
                        .addComponent(jLabel6)
                        .addComponent(cbUserFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(pnlFrom, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel4)
                    .addComponent(pnlTo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        tableScrollPane.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Outbound Note", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 18), new java.awt.Color(255, 153, 0))); // NOI18N
        tableScrollPane.setAutoscrolls(true);

        tbOutboundList.setAutoCreateRowSorter(true);
        tbOutboundList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tbOutboundList.setFillsViewportHeight(true);
        tbOutboundList.setRowHeight(25);
        tbOutboundList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbOutboundListMouseClicked(evt);
            }
        });
        tableScrollPane.setViewportView(tbOutboundList);

        jScroll.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Outbound Note Details", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 18), new java.awt.Color(255, 153, 0))); // NOI18N

        tbProductList.setAutoCreateRowSorter(true);
        tbProductList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tbProductList.setFillsViewportHeight(true);
        tbProductList.setRowHeight(25);
        jScroll.setViewportView(tbProductList);

        btAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/product/Add.png"))); // NOI18N
        btAdd.setText("Add New...");
        btAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btAddActionPerformed(evt);
            }
        });

        btUpdate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/product/Save.png"))); // NOI18N
        btUpdate.setText("Update...");
        btUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btUpdateActionPerformed(evt);
            }
        });

        btRemove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/product/Delete.png"))); // NOI18N
        btRemove.setText("Remove");
        btRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btRemoveActionPerformed(evt);
            }
        });

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/product/Refresh.png"))); // NOI18N
        jButton1.setText("Refresh");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel7.setFont(new java.awt.Font("Lucida Sans", 1, 24)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 255, 255));
        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/main/Outbound.png"))); // NOI18N
        jLabel7.setText("<html><u><i><font color='red'>O</font>utbound <font color='red'>M</font>anagement</i></u></html>");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel7)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(100, Short.MAX_VALUE)
                .addComponent(btAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btRemove, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(99, Short.MAX_VALUE))
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlSearch, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(tableScrollPane)
                    .addComponent(jScroll))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(pnlSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(tableScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 252, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScroll, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btAdd)
                    .addComponent(btUpdate)
                    .addComponent(btRemove)
                    .addComponent(jButton1))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btAddActionPerformed
            
            new OutboundDialog(null).setVisible(true);
            refreshAction(false);
            new ProductPanel().getProductTableModel().refresh();
            scrollToRow(selectedRowIndex);
       
    }//GEN-LAST:event_btAddActionPerformed

    private void tbOutboundListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbOutboundListMouseClicked
        
    }//GEN-LAST:event_tbOutboundListMouseClicked

    private void cbUserFilterItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbUserFilterItemStateChanged
        doFilter();
    }//GEN-LAST:event_cbUserFilterItemStateChanged

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        resetFilter();
        
    }//GEN-LAST:event_jButton5ActionPerformed

    private void tfIdFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfIdFilterActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tfIdFilterActionPerformed

    private void tfIdFilterCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_tfIdFilterCaretUpdate
        //        search();        // TODO add your handling code here:
    }//GEN-LAST:event_tfIdFilterCaretUpdate

    private void btRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btRemoveActionPerformed
      
      //neu khong co row nao duoc chon, k cho xoa  
      int selectedRowIndex = tbOutboundList.getSelectedRow();
      if(selectedRowIndex<0){
          JOptionPane.showMessageDialog(this, "Choose inbound to delete!");
          return;
      }
      int ans =  JOptionPane.showConfirmDialog(this, "Are you sure to delete?", "Confirm", JOptionPane.YES_NO_OPTION);
      if(ans==JOptionPane.YES_OPTION){
        deleteAction();
        refreshAction(true);
        formatTable();
      }
    }//GEN-LAST:event_btRemoveActionPerformed

    private void btUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btUpdateActionPerformed
        new OutboundDialog(selectedOutbound).setVisible(true);
        refreshAction(false);
        scrollToRow(selectedRowIndex);
    }//GEN-LAST:event_btUpdateActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        refreshAction(true);
        SwingUtils.showInfoDialog(SwingUtils.DB_REFRESH);
    }//GEN-LAST:event_jButton1ActionPerformed
    public void setTableColumnType(JTable tbl, Integer colID, Class type) {
        TableColumn col = tbl.getColumnModel().getColumn(colID);
        col.setCellEditor(tbl.getDefaultEditor(type));
        col.setCellRenderer(tbl.getDefaultRenderer(type));
    }
    private void refreshAction(boolean mustInfo) {
        if (mustInfo) {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            // Refresh table
            outboundTableModel.refresh();

            

            // Refresh combobox column table
//            branchNameComboBoxModel1.refresh();
            setCursor(null);
            
        } else {
            // Refresh table
            outboundTableModel.refresh();

           
        }
        scrollToRow(selectedRowIndex);
    }
    
    private void scrollToRow(int row) {
        tbOutboundList.getSelectionModel().setSelectionInterval(row, row);
        tbOutboundList.scrollRectToVisible(new Rectangle(tbOutboundList.getCellRect(row, 0, true)));
    }
   private void deleteAction() {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        boolean result = outboundTableModel.delete(selectedOutbound);
        setCursor(null);
        

        // Neu row xoa la row cuoi thi lui cursor ve
        // Neu row xoa la row khac cuoi thi tien cursor ve truoc
        selectedRowIndex = (selectedRowIndex == tbProductList.getRowCount() ? tbProductList.getRowCount() - 1 : selectedRowIndex++);
        scrollToRow(selectedRowIndex);
        JOptionPane.showMessageDialog(this, SwingUtils.DELETE_SUCCESS);
    }
   
   private void updateAction() {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        boolean result = outboundTableModel.update(selectedOutbound);
        refreshAction(false);
        setCursor(null);
        formatTable();
        SwingUtils.showInfoDialog(result ? SwingUtils.UPDATE_SUCCESS : SwingUtils.UPDATE_FAIL);
    }
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btAdd;
    private javax.swing.JButton btRemove;
    private javax.swing.JButton btUpdate;
    private javax.swing.JComboBox<User> cbUserFilter;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScroll;
    private javax.swing.JPanel pnlFrom;
    private javax.swing.JPanel pnlSearch;
    private javax.swing.JPanel pnlTo;
    private javax.swing.JScrollPane tableScrollPane;
    private javax.swing.JTable tbOutboundList;
    private javax.swing.JTable tbProductList;
    private javax.swing.JTextField tfIdFilter;
    // End of variables declaration//GEN-END:variables
}
