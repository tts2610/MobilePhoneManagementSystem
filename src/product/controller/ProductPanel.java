/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package product.controller;


import database.DBProvider;
import inbound.controller.FloatEditor;
import inbound.model.CurrencyCellRenderer;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.DefaultListCellRenderer;

import javax.swing.DefaultListSelectionModel;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.RowSorterEvent;
import javax.swing.event.RowSorterListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;
import main.controller.LoginFrame;
import main.model.UserFunction;
import org.jdesktop.xswingx.PromptSupport;
import product.dao.Branch;
import product.dao.BranchNameComboBoxModel;
import product.dao.BranchNameComboBoxRender;
import product.dao.ProductDAOImpl;
import product.model.ImageRenderer;
import product.model.Product;
import product.model.ProductTableModel;
import utility.IntegerCellEditor;
import utility.StringCellEditor;
import utility.SwingUtils;
import utility.TableCellListener;

/**
 *
 * @author tuan
 */
public class ProductPanel extends javax.swing.JPanel {

    //chon hinh
    String s;

    private ProductTableModel productTableModel;
    private BranchNameComboBoxModel branchNameComboBoxModel;

    private final BranchNameComboBoxRender branchNameComboBoxRenderer;
    private final TableRowSorter<ProductTableModel> sorter;

    // Branch dang duoc chon trong table
    private Product selectedProduct;
    private int selectedRowIndex;
    private Branch filterBranch;

    /*Product table*/
    private static int COL_ID = 0;
    private static int COL_BraName = 1;
    private static int COL_ProName = 2;
    private static int COL_ProStock = 3;
    private static int COL_ProPrice = 4;
    private static int COL_ProDescr = 5;
    private static int COL_ProEnable = 6;
    private static int COL_SaleOff = 7;
    private static int COL_Img = 8;

    /* gia tri cu cua product name*/
    Object oldValue = null;

    public ProductPanel() {
        initComponents();
        PromptSupport.setPrompt("Find ID", tfIdFilter);
        PromptSupport.setPrompt("Find Name", tfNameFilter);
        PromptSupport.setPrompt("Find Price", tfPriceFilter);
        PromptSupport.setPrompt("Stock", tfStockFilter);

        PromptSupport.setFocusBehavior(PromptSupport.FocusBehavior.SHOW_PROMPT, tfIdFilter);
        PromptSupport.setFocusBehavior(PromptSupport.FocusBehavior.SHOW_PROMPT, tfNameFilter);
        PromptSupport.setFocusBehavior(PromptSupport.FocusBehavior.SHOW_PROMPT, tfPriceFilter);
        PromptSupport.setFocusBehavior(PromptSupport.FocusBehavior.SHOW_PROMPT, tfStockFilter);
        //disable nut browse
        btBrowse.setEnabled(false);

        selectedProduct = new Product();

        // Set data cho combobox level filter
        branchNameComboBoxModel = new BranchNameComboBoxModel();
        filterBranch = new Branch(0, "All", true, "default", 0);
        branchNameComboBoxModel.addElement(filterBranch);

        branchNameComboBoxRenderer = new BranchNameComboBoxRender();
        cbBranchName.setModel(branchNameComboBoxModel);
        cbBranchName.setRenderer(branchNameComboBoxRenderer);

        //set data cho table
        productTableModel = new ProductTableModel();
        tbProductList.setModel(productTableModel);
        tbProductList.setRowSelectionAllowed(true);

        // Set sorter cho table
        sorter = new TableRowSorter<>(productTableModel);
        tbProductList.setRowSorter(sorter);

        //set default option cho cb
        cbBranchName.setSelectedIndex(cbBranchName.getItemCount() - 1);
        formatTable();
        tbProductList.setRowHeight(120);

        // Bat su kien select row tren table
        tbProductList.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
            DefaultListSelectionModel model = (DefaultListSelectionModel) e.getSource();
            if (!model.isSelectionEmpty()) {
                fetchProductDetails();
                btRemove.setEnabled(true);
                btBrowse.setEnabled(true);//enable khi bam chon vao table
                tbProductList.setSurrendersFocusOnKeystroke(false);
            }
            else{
                btRemove.setEnabled(false);
                btBrowse.setEnabled(false);//enable khi bam chon vao table
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

        TableCellListener tcl = new TableCellListener(tbProductList, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TableCellListener tcl = (TableCellListener) e.getSource();
                

                switch (tcl.getColumn()) {
                    case 0:
                        selectedProduct.setProName((String) tcl.getNewValue());
                        oldValue = tcl.getNewValue();//de tra ve gia tri cu neu sai
                        break;
                    case 1:
                        selectedProduct.setBraname((String) tcl.getNewValue());
                        break;
                    case 2:
                        selectedProduct.setProName((String) tcl.getNewValue());
                        break;
                    case 4:
                        selectedProduct.setProPrice((float) tcl.getNewValue());
                        break;
                    case 3:
                        selectedProduct.setProStock((int) tcl.getNewValue());
                        break;
                    case 5:
                        selectedProduct.setProDesc((String) tcl.getNewValue());
                        break;
                    case 7:
                        selectedProduct.setSaleofid((int) tcl.getNewValue());
                        break;
                    case 6:
                        selectedProduct.setProEnabled((boolean) tcl.getNewValue());
                        break;
                }
                int ans = SwingUtils.showConfirmDialog("Are you sure to update?");
                if(ans==JOptionPane.YES_OPTION)
                    updateAction();
                else
                    refreshAction(false);
            }
        });
        
        //event trong truong hop k co record trong table
        sorter.addRowSorterListener(new RowSorterListener() {

            @Override
            public void sorterChanged(RowSorterEvent e) {
                if (tbProductList.getRowCount() == 0) {
                    btRemove.setEnabled(false);
                } else {
                    btRemove.setEnabled(true);
                }
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
        tfPriceFilter.getDocument().addDocumentListener(
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
        tfStockFilter.getDocument().addDocumentListener(
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

        btRemove.setEnabled(false);
        // Check permission product
        if (!LoginFrame.checkPermission(new UserFunction(UserFunction.FG_PRODUCT, UserFunction.FN_UPDATE))) {
            btAdd.setEnabled(false);
            btRemove.setEnabled(false);
        }

        // Check permission branch
        if (!LoginFrame.checkPermission(new UserFunction(UserFunction.FG_BRANCH, UserFunction.FN_VIEW))) {
            btBranch.setEnabled(false);
        }

    }

    private void fetchProductDetails() {
        selectedRowIndex = tbProductList.getSelectedRow();
        selectedProduct.setProId((int) tbProductList.getValueAt(selectedRowIndex, 0));
        selectedProduct.setBraname((String) tbProductList.getValueAt(selectedRowIndex, 1));
        selectedProduct.setProName((String) tbProductList.getValueAt(selectedRowIndex, 2));
        selectedProduct.setProStock((int) tbProductList.getValueAt(selectedRowIndex, 3));
        selectedProduct.setProPrice((float) tbProductList.getValueAt(selectedRowIndex, 4));
        selectedProduct.setProDesc((String) tbProductList.getValueAt(selectedRowIndex, 5));
        selectedProduct.setProEnabled((boolean) tbProductList.getValueAt(selectedRowIndex, 6));
        //cot sale off k cho update

        //cot image
//        //cot braid bi an di
//        selectedProduct.setBraId((int) tbProductList.getModel().getValueAt(selectedRowIndex, 9));
    }

    private void updateAction() {
        if (productTableModel.update(selectedProduct)) {
            
            JOptionPane.showMessageDialog(this, "Update successfully");
        } else {
            //refresh
            refreshAction(true);
            
            formatTable();

        }
        formatTable();
        tbProductList.setRowHeight(120);
        productTableModel.refresh();
    }

    private void refreshAction() {

        //refresh filter
        tfIdFilter.setText("");
        tfNameFilter.setText("");
        tfPriceFilter.setText("");
        tfStockFilter.setText("");
        cbBranchName.setSelectedIndex(cbBranchName.getItemCount() - 1);
        cbStatusFilter.setSelectedIndex(0);
        tbProductList.getSelectionModel().clearSelection();

    }

    public void formatTable() {
        //alignment component
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        //id
        tbProductList.getColumnModel().getColumn(COL_ID).setMinWidth(30);
        tbProductList.getColumnModel().getColumn(COL_ID).setMaxWidth(50);
        tbProductList.getColumnModel().getColumn(COL_ID).setCellRenderer(centerRenderer);
        //branch 
        tbProductList.getColumnModel().getColumn(COL_BraName).setMinWidth(80);
//        tbProductList.getColumnModel().getColumn(COL_BraName).setMaxWidth(80);
        tbProductList.getColumnModel().getColumn(COL_BraName).setCellRenderer(centerRenderer);
        tbProductList.getColumnModel().getColumn(COL_BraName).setCellEditor(new product.dao.ComboBoxCellEditor(new BranchNameComboBoxModel()));

        //name
        tbProductList.getColumnModel().getColumn(COL_ProName).setMinWidth(150);
        tbProductList.getColumnModel().getColumn(COL_ProName).setCellRenderer(centerRenderer);
        tbProductList.getColumnModel().getColumn(COL_ProName).setCellEditor(new StringCellEditor(1, 70,SwingUtils.PATTERN_NAMEWITHSPACE));
        //stock
        tbProductList.getColumnModel().getColumn(COL_ProStock).setMinWidth(30);
        tbProductList.getColumnModel().getColumn(COL_ProStock).setMaxWidth(50);
        tbProductList.getColumnModel().getColumn(COL_ProStock).setCellRenderer(centerRenderer);

        //price
        tbProductList.getColumnModel().getColumn(COL_ProPrice).setMinWidth(100);
        tbProductList.getColumnModel().getColumn(COL_ProPrice).setCellRenderer(new CurrencyCellRenderer());

        //desc
        tbProductList.getColumnModel().getColumn(COL_ProDescr).setMinWidth(150);
        tbProductList.getColumnModel().getColumn(COL_ProDescr).setCellRenderer(centerRenderer);
        tbProductList.getColumnModel().getColumn(COL_ProDescr).setCellEditor(new StringCellEditor(1, 100, SwingUtils.PATTERN_ADDRESS));

        //enable
        tbProductList.getColumnModel().getColumn(COL_ProEnable).setMinWidth(25);
//        tbProductList.getColumnModel().getColumn(COL_ProEnable).setMaxWidth(45);

        //saleoff
        tbProductList.getColumnModel().getColumn(COL_SaleOff).setMinWidth(100);
//        tbProductList.getColumnModel().getColumn(COL_SaleOff).setMaxWidth(100);
        tbProductList.getColumnModel().getColumn(COL_SaleOff).setCellRenderer(centerRenderer);

        //image
        tbProductList.getColumnModel().getColumn(COL_Img).setMinWidth(100);
//        tbProductList.getColumnModel().getColumn(COL_Img).setMaxWidth(100);
//        tbProductList.getColumnModel().getColumn(COL_Img).setCellRenderer(new ImageRenderer(selectedProduct));

        tbProductList.setDefaultEditor(Float.class, new FloatEditor(1000000, 30000000));

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTextField2 = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        buttonGroup1 = new javax.swing.ButtonGroup();
        jButton10 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        btBrowse = new javax.swing.JButton();
        btAdd = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        tfNameFilter = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        tfStockFilter = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        cbStatusFilter = new javax.swing.JComboBox<>();
        tfPriceFilter = new javax.swing.JTextField();
        tfIdFilter = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        cbBranchName = new javax.swing.JComboBox<>();
        btRemove = new javax.swing.JButton();
        jrefreshFilter = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbProductList = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        btBranch = new javax.swing.JButton();

        jTextField2.setText("jTextField2");

        jLabel8.setText("jLabel8");

        jButton10.setText("jButton10");

        setPreferredSize(new java.awt.Dimension(810, 680));

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Update", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 18), new java.awt.Color(255, 153, 0))); // NOI18N

        jLabel9.setText("Image");

        jLabel10.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));

        btBrowse.setText("Browse");
        btBrowse.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btBrowseMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btBrowse, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9)
                    .addComponent(btBrowse, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        btAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/product/Add.png"))); // NOI18N
        btAdd.setText("Add New");
        btAdd.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btAddMousePressed(evt);
            }
        });
        btAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btAddActionPerformed(evt);
            }
        });

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Filter", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 18), new java.awt.Color(255, 153, 0))); // NOI18N

        jLabel3.setText("ID:");

        jLabel12.setText("Name:");

        jLabel13.setText("Stock:");

        jLabel14.setText("Price:");

        jLabel15.setText("Status");

        cbStatusFilter.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All", "Enabled", "Disabled" }));
        cbStatusFilter.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbStatusFilterItemStateChanged(evt);
            }
        });

        jLabel11.setText("Branch:");

        cbBranchName.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbBranchNameItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(tfStockFilter, javax.swing.GroupLayout.DEFAULT_SIZE, 64, Short.MAX_VALUE)
                    .addComponent(tfIdFilter))
                .addGap(18, 18, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(tfNameFilter, javax.swing.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE)
                    .addComponent(tfPriceFilter))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cbBranchName, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cbStatusFilter, 0, 145, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(18, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel12)
                    .addComponent(tfNameFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15)
                    .addComponent(cbStatusFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tfIdFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(tfStockFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14)
                    .addComponent(tfPriceFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11)
                    .addComponent(cbBranchName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        btRemove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/product/Delete.png"))); // NOI18N
        btRemove.setText("Remove");
        btRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btRemoveActionPerformed(evt);
            }
        });

        jrefreshFilter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/product/Refresh.png"))); // NOI18N
        jrefreshFilter.setText("Refresh");
        jrefreshFilter.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jrefreshFilterMouseClicked(evt);
            }
        });

        tbProductList.setAutoCreateRowSorter(true);
        tbProductList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4", "Title 5", "Title 6", "Title 7", "Title 8", "Title 9"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Float.class, java.lang.String.class, java.lang.Boolean.class, java.lang.Integer.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tbProductList.setFillsViewportHeight(true);
        tbProductList.setRowHeight(25);
        tbProductList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tbProductList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbProductListMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbProductList);

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setFont(new java.awt.Font("Lucida Sans", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 255, 255));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/main/Product.png"))); // NOI18N
        jLabel1.setText("<html><u><i><font color='red'>P</font>roduct <font color='red'>M</font>anagement</i></u></html>");

        btBranch.setFont(new java.awt.Font("Lucida Sans", 3, 14)); // NOI18N
        btBranch.setForeground(new java.awt.Color(255, 153, 0));
        btBranch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/product/folder-open.png"))); // NOI18N
        btBranch.setText("<html><u>Branch...</u></html>");
        btBranch.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btBranchMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btBranch, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(38, 38, 38))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btBranch))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(263, Short.MAX_VALUE)
                .addComponent(btAdd)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 68, Short.MAX_VALUE)
                .addComponent(btRemove)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 68, Short.MAX_VALUE)
                .addComponent(jrefreshFilter)
                .addGap(0, 64, Short.MAX_VALUE))
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jrefreshFilter)
                            .addComponent(btRemove)
                            .addComponent(btAdd))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 379, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void cbBranchNameItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbBranchNameItemStateChanged
        doFilter();
    }//GEN-LAST:event_cbBranchNameItemStateChanged

    private void cbStatusFilterItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbStatusFilterItemStateChanged
        doFilter();
    }//GEN-LAST:event_cbStatusFilterItemStateChanged

    private void jrefreshFilterMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jrefreshFilterMouseClicked
        refreshAction();

        refreshAction(false);
        formatTable();
        scrollToRow(selectedRowIndex);
        SwingUtils.showInfoDialog(SwingUtils.DB_REFRESH);
    }//GEN-LAST:event_jrefreshFilterMouseClicked

    private void btBrowseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btBrowseMouseClicked

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("IMAGE 150x120");
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.name")));
        FileNameExtensionFilter filter = new FileNameExtensionFilter("*.IMAGE", "jpg", "gif", "png");
        fileChooser.addChoosableFileFilter(filter);
        int result = fileChooser.showSaveDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String path = selectedFile.getAbsolutePath();
            ImageIcon MyImage = new ImageIcon(path);
            Image img = MyImage.getImage();
            Image newImage = img.getScaledInstance(jLabel10.getWidth(), jLabel10.getHeight(), Image.SCALE_SMOOTH);
            ImageIcon image = new ImageIcon(newImage);
            jLabel10.setIcon(image);
            s = path;

            try {
                InputStream is = new FileInputStream(new File(s));
                new ProductDAOImpl().updateImage(selectedProduct, is);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(ProductPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
            refreshAction(true);
            return;
        } else if (result == JFileChooser.CANCEL_OPTION) {
            System.out.println("No data");
            return;
        }

    }//GEN-LAST:event_btBrowseMouseClicked

    private void btAddMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btAddMousePressed


    }//GEN-LAST:event_btAddMousePressed

    private void btAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btAddActionPerformed
        insertAction();
        refreshAction(true);
        productTableModel.refresh();
        formatTable();

        // Select row vua insert vao
        selectedRowIndex = 0;
        scrollToRow(selectedRowIndex);
        tbProductList.editCellAt(tbProductList.getSelectedRow(), 2);
        tbProductList.getEditorComponent().requestFocus();
    }//GEN-LAST:event_btAddActionPerformed

    private void btBranchMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btBranchMouseClicked
        new branch.controller.BranchDialog().setVisible(true);
        refreshAction(true);
        formatTable();
    }//GEN-LAST:event_btBranchMouseClicked

    private void btRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btRemoveActionPerformed
        deleteAction();
        refreshAction(true);
        productTableModel.refresh();
        formatTable();
    }//GEN-LAST:event_btRemoveActionPerformed

    private void tbProductListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbProductListMouseClicked

        ImageIcon image1;
        image1 = (ImageIcon) tbProductList.getValueAt(selectedRowIndex, 8);

        Image image2 = image1.getImage().getScaledInstance(jLabel10.getWidth(), jLabel10.getHeight(), Image.SCALE_SMOOTH);

        ImageIcon image3 = new ImageIcon(image2);

        jLabel10.setIcon(image3);
    }//GEN-LAST:event_tbProductListMouseClicked

    private void refreshAction(boolean mustInfo) {
        if (mustInfo) {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            // Refresh table
            productTableModel.refresh();

            // Refresh combobox filter
            branchNameComboBoxModel.refresh();
            branchNameComboBoxModel.addElement(filterBranch);
            // Refresh combobox column table
//            branchNameComboBoxModel1.refresh();
            setCursor(null);

        } else {
            // Refresh table
            productTableModel.refresh();
        }

    }

    private void scrollToRow(int row) {
        tbProductList.getSelectionModel().setSelectionInterval(row, row);
        tbProductList.scrollRectToVisible(new Rectangle(tbProductList.getCellRect(row, 0, true)));
    }

    private void deleteAction() {
        int ans = SwingUtils.showConfirmDialog("Are you sure to delete?");
        if (ans == JOptionPane.YES_OPTION) {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            boolean result = productTableModel.delete(selectedProduct);
            setCursor(null);
            if (result) {
                SwingUtils.showInfoDialog(SwingUtils.DELETE_SUCCESS);
            }

            // Neu row xoa la row cuoi thi lui cursor ve
            // Neu row xoa la row khac cuoi thi tien cursor ve truoc
            selectedRowIndex = (selectedRowIndex == tbProductList.getRowCount() ? tbProductList.getRowCount() - 1 : selectedRowIndex++);
            scrollToRow(selectedRowIndex);

        }
    }

    private void insertAction() {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        boolean result = productTableModel.insert(new Product());
        setCursor(null);
        SwingUtils.showInfoDialog(result ? SwingUtils.INSERT_SUCCESS : SwingUtils.INSERT_FAIL);

    }

    private void doFilter() {
        RowFilter<ProductTableModel, Object> rf = null;

        //Filter theo regex, neu parse bi loi thi khong filter
        try {
            List<RowFilter<ProductTableModel, Object>> filters = new ArrayList<>();

            filters.add(RowFilter.regexFilter("^" + tfIdFilter.getText(), 0));
            // Neu co chon cus level thi moi filter level
            if (cbBranchName.getSelectedIndex() != cbBranchName.getItemCount() - 1) {
                filters.add(RowFilter.regexFilter("^" + ((Branch) cbBranchName.getSelectedItem()).getBraName(), 1));
            }
            filters.add(RowFilter.regexFilter("^" + tfNameFilter.getText(), 2));

            filters.add(RowFilter.regexFilter("^" + tfStockFilter.getText(), 3));
            filters.add(RowFilter.regexFilter("^" + tfPriceFilter.getText(), 4));

            // Neu status khac "All" thi moi filter
            String statusFilter = cbStatusFilter.getSelectedItem().toString();
            if (!statusFilter.equals("All")) {
                filters.add(RowFilter.regexFilter(
                        statusFilter.equals("Enabled") ? "t" : "f", 6));
            }

            rf = RowFilter.andFilter(filters);
        } catch (java.util.regex.PatternSyntaxException e) {
            return;
        }
        sorter.setRowFilter(rf);
    }

    //de refresh table tu panel khac
    public ProductTableModel getProductTableModel() {
        return productTableModel;
    }

    public void setProductTableModel(ProductTableModel productTableModel) {
        this.productTableModel = productTableModel;
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btAdd;
    private javax.swing.JButton btBranch;
    private javax.swing.JButton btBrowse;
    private javax.swing.JButton btRemove;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox<Branch> cbBranchName;
    private javax.swing.JComboBox<String> cbStatusFilter;
    private javax.swing.JButton jButton10;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JButton jrefreshFilter;
    private javax.swing.JTable tbProductList;
    private javax.swing.JTextField tfIdFilter;
    private javax.swing.JTextField tfNameFilter;
    private javax.swing.JTextField tfPriceFilter;
    private javax.swing.JTextField tfStockFilter;
    // End of variables declaration//GEN-END:variables
}
