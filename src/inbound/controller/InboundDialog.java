/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inbound.controller;

import com.toedter.calendar.JDateChooser;
import inbound.model.CurrencyCellRenderer;
import inbound.model.Inbound;
import inbound.model.InboundDetail;
import inbound.model.InboundDetailDAOImpl;
import inbound.model.InboundDetailTableModel;
import inbound.model.Product;
import inbound.model.ProductTableModel;
import inbound.model.SupplierComboboxModel;
import inbound.model.SupplierComboboxRenderer;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Rectangle;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;
import order.controller.OrderBranchListCellRenderer;
import order.controller.OrderBranchListModel;
import order.controller.OrderProductComboBoxModel;
import order.model.OrderBranch;
import org.jdesktop.xswingx.PromptSupport;

import inbound.model.Supplier;
import inbound.model.SupplierDAOImpl;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.sql.rowset.CachedRowSet;
import javax.swing.AbstractAction;
import javax.swing.event.RowSorterEvent;
import javax.swing.event.RowSorterListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import main.controller.LoginFrame;
import main.model.Login;
import utility.SpinnerCellEditor;
import utility.SwingUtils;
import utility.TableCellListener;

/**
 *
 * @author tuan
 */
public class InboundDialog extends javax.swing.JDialog implements ItemListener {

    private Inbound backup;
    private Inbound inbound;

    ProductTableModel productTableModel;
    InboundDetailTableModel inboundDetailTableModel;
    SupplierComboboxModel supplierComboboxModel;
    InDetailSupplierComboBoxRenderer inDetailSupplierComoboxRenderer;
//    SupplierComboboxRenderer supplierComboboxRenderer;
    private OrderProductComboBoxModel orderProductComboBoxModel;
    JDateChooser jdcDate = new JDateChooser();

    private Product selectedProduct;
    private int selectedRowIndex;
    private int selectedRowIndex1;
    private InboundDetail selectedInDetail;
    private Supplier selectedSupplier;
    
    //row sorter
    private TableRowSorter<ProductTableModel> sorter;

    //inbound list de add moi vao inbound
    List<InboundDetail> listIn = new ArrayList<>();

    // Two mode: insert va update
    private boolean insertMode;

    //flag de cho biet co thay doi noi dung gi k
    private boolean trackChanges;

    /*Product table*/
    private static int COL_ID = 0;
    private static int COL_BraName = 1;
    private static int COL_ProName = 2;
    private static int COL_ProStock = 3;
    private static int COL_ProPrice = 4;
    private static int COL_ProDescr = 5;
    private static int COL_ProEnable = 6;

    private static int COL_InDID = 0;
    private static int COL_InDName = 1;
    private static int COL_InDCost = 2;
    private static int COL_InDQTy = 3;

    public InboundDialog(Inbound inbound) {
        super((JFrame) null, true);
        insertMode = inbound == null;
        initComponents();
        setLocationRelativeTo(null);
        
        setting();
        btDelete.setEnabled(false);
        btnAdd.setEnabled(false);
        btReset.setEnabled(false);
        if (insertMode) {
            setTitle("New Inbound");
            this.inbound = new Inbound();
            this.inbound.setInID(-1);
            this.inbound.setInDate(new Date());
            this.inbound.setSupID(1);
            this.inbound.setSupInvoiceID("");
//            this.inbound.setUserName(user);
            backup = this.inbound.clone();
            setTrackChanges(false);
            txtInvoice.setEditable(true);
            txtUser.setText(LoginFrame.config.userName);
            tfDate.setText(SwingUtils.formatString(new Date(), SwingUtils.FormatType.DATE));
            tbProductList.setEnabled(false);
        } else {
            this.inbound = inbound.clone();
            backup = this.inbound.clone();
            cbSupplier.setSelectedItem(supplierComboboxModel.getSupplierFromValue(this.inbound.getSupName()));//set data cho combobox
            doFilter();
            tfDate.setText(SwingUtils.formatString(this.inbound.getInDate(), SwingUtils.FormatType.DATE));
            txtInvoice.setText(this.inbound.getSupInvoiceID());
            setTrackChanges(false);
            txtInvoice.setEditable(false);
            cbSupplier.setEnabled(false);
            txtUser.setText(this.inbound.getUserName());
        }

        inboundDetailTableModel.load(this.inbound.getInID());//null neu insert mode

        //update lai list
        listIn = inboundDetailTableModel.getList();//null neu insert mode
        
        
    }

    public void setting() {

        selectedProduct = new Product();
        selectedInDetail = new InboundDetail();
        selectedSupplier = new Supplier();
        //set data cho table product
        productTableModel = new ProductTableModel();
        tbProductList.setModel(productTableModel);

        // Set sorter cho table
        sorter = new TableRowSorter<>(productTableModel);
        tbProductList.setRowSorter(sorter);

        //hide column hinh
        TableColumnModel tcm = tbProductList.getColumnModel();
        tcm.removeColumn(tcm.getColumn(8));

        //hide column saleoff
        tcm.removeColumn(tcm.getColumn(7));

        //hide column enable
        tcm.removeColumn(tcm.getColumn(6));

        // Set data cho combobox product name
        orderProductComboBoxModel = new OrderProductComboBoxModel();

        //set data cho table InDetail
        inboundDetailTableModel = new InboundDetailTableModel();
        tbInDetail.setModel(inboundDetailTableModel);
        tbInDetail.setRowSelectionAllowed(true);

        // Set data cho combobox supplier
        supplierComboboxModel = new SupplierComboboxModel();
        inDetailSupplierComoboxRenderer = new InDetailSupplierComboBoxRenderer();
        cbSupplier.setModel(supplierComboboxModel);
        cbSupplier.setRenderer(inDetailSupplierComoboxRenderer);
        cbSupplier.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                btnAdd.setEnabled(false);
                tbProductList.setEnabled(true);
   
                if(tbInDetail.getRowCount()!=0&& supplierComboboxModel.getSelectedItem().getSupID()!=selectedSupplier.getSupID()){ //chon lai supplier nhung khac voi supplier cu
                    int ans = SwingUtils.showConfirmDialog("Discard changes and choose another suppliers?");
                    if(ans==JOptionPane.YES_OPTION&&insertMode){//insert mode
                        inboundDetailTableModel.load(backup.getInID());
                        doFilter();
                    }
                    else{
                        supplierComboboxModel.setSelectedItem(selectedSupplier);
                    }
                    
                    
                }
                else{
                    
                    doFilter();
                }
                
                
            }

        });

        //get current user name
        txtUser.setText(main.model.Login.USER_NAME);

        // Set promt text
        PromptSupport.setPrompt("Input Invoice", txtInvoice);

        PromptSupport.setFocusBehavior(PromptSupport.FocusBehavior.SHOW_PROMPT, txtInvoice);

        // ---------- Settup Jdatechooser
//        jdcDate.setBounds(0, 0, 130, 20);
//        jdcDate.setDateFormatString("MMM dd, yyyy");
//        jdcDate.getDateEditor().setEnabled(false);
//        pnlDate.add(jdcDate);
//        //Lay ngay hien tai
//        java.util.Date today = new java.util.Date();
//        jdcDate.setDate(today);

        //bat su kien sua thong tin table InDetail
        TableCellListener tcl = new TableCellListener(tbInDetail, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TableCellListener tcl = (TableCellListener) e.getSource();
                switch (tcl.getColumn()) {
                    case 2:
                        setTrackChanges(true);
                        break;//co sua cot cost
                    case 3:
                        setTrackChanges(true);
                        break;//co sua cot quantity
                }
            }
        });
        //bat su kien click vao table product
        tbProductList.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
            DefaultListSelectionModel model = (DefaultListSelectionModel) e.getSource();
            if (!model.isSelectionEmpty()) {
                fetchProductDetails();
                btnAdd.setEnabled(true);
                tbProductList.setSurrendersFocusOnKeystroke(false);
            }

        });

        //event trong truong hop k co record trong table
        sorter.addRowSorterListener(new RowSorterListener() {

            @Override
            public void sorterChanged(RowSorterEvent e) {
                if (tbProductList.getRowCount() == 0||tbInDetail.getRowCount()==0) {
                    btnAdd.setEnabled(false);
                } 
                fetchSupplier();//fetch supplier de biet supplier co thay doi hay k trong truong hop da chon san pham nhung muon doi supplier
            }
        });
        txtInvoice.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                btnSave.setEnabled(true);
            }

            public void removeUpdate(DocumentEvent e) {
                btnSave.setEnabled(true);
            }

            public void insertUpdate(DocumentEvent e) {
                btnSave.setEnabled(true);
            }
        });

        // Bat su kien select row tren table Indetail
        tbInDetail.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
            DefaultListSelectionModel model = (DefaultListSelectionModel) e.getSource();
            if (!model.isSelectionEmpty()) {
                fetchAction();
                btDelete.setEnabled(true);
            }
            else
                btDelete.setEnabled(false);
        });

        tbInDetail.setDefaultEditor(Float.class, new FloatEditor(1000000, 30000000));

        tbInDetail.getColumnModel().getColumn(3).setCellEditor(new SpinnerCellEditor(1, 10));

//        // Set data cho list filter
//        list.setModel(new OrderBranchListModel());
//        list.setCellRenderer(new OrderBranchListCellRenderer());
//        list.setSelectionModel(new DefaultListSelectionModel() {
//            @Override
//            public void setSelectionInterval(int index0, int index1) {
//                if (super.isSelectedIndex(index0)) {
//                    super.removeSelectionInterval(index0, index1);
//                } else {
//                    super.addSelectionInterval(index0, index1);
//                }
//            }
//
//        });
//        list.addListSelectionListener(new ListSelectionListener() {
//            @Override
//            public void valueChanged(ListSelectionEvent e) {
//                if (list.isSelectionEmpty()) {
//
//                    refreshAction(false);
//                    productTableModel.refresh();
//                    doFilter();
//                    return;
//                } else {
//                    doFilter();
//                }
//
//            }
//        });
//        
        formatTable();
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

        //name
        tbProductList.getColumnModel().getColumn(COL_ProName).setMinWidth(80);
        tbProductList.getColumnModel().getColumn(COL_ProName).setCellRenderer(centerRenderer);

        //stock
        tbProductList.getColumnModel().getColumn(COL_ProStock).setMinWidth(35);
//        tbProductList.getColumnModel().getColumn(COL_ProStock).setMaxWidth(50);
        tbProductList.getColumnModel().getColumn(COL_ProStock).setCellRenderer(centerRenderer);

        //price
        tbProductList.getColumnModel().getColumn(COL_ProPrice).setMinWidth(80);
        tbProductList.getColumnModel().getColumn(COL_ProPrice).setCellRenderer(new CurrencyCellRenderer());

        //desc
        tbProductList.getColumnModel().getColumn(COL_ProDescr).setMinWidth(50);
        tbProductList.getColumnModel().getColumn(COL_ProDescr).setCellRenderer(centerRenderer);

        //id
        tbInDetail.getColumnModel().getColumn(COL_InDID).setMinWidth(35);
        tbInDetail.getColumnModel().getColumn(COL_InDID).setMaxWidth(70);
        tbInDetail.getColumnModel().getColumn(COL_InDID).setCellRenderer(centerRenderer);
        //InDetail Name
        tbInDetail.getColumnModel().getColumn(COL_InDName).setMinWidth(400);
        tbInDetail.getColumnModel().getColumn(COL_InDName).setMaxWidth(400);
        tbInDetail.getColumnModel().getColumn(COL_InDName).setCellRenderer(centerRenderer);

        //InDetail Cost
        tbInDetail.getColumnModel().getColumn(COL_InDCost).setMinWidth(50);
        tbInDetail.getColumnModel().getColumn(COL_InDCost).setCellRenderer(new CurrencyCellRenderer());

        //stock
        tbInDetail.getColumnModel().getColumn(COL_InDQTy).setMinWidth(35);
        tbInDetail.getColumnModel().getColumn(COL_InDQTy).setMaxWidth(70);
        tbInDetail.getColumnModel().getColumn(COL_InDQTy).setCellRenderer(centerRenderer);

    }

    private void resetAction(boolean mustInfo) {
        // Load lai vung supplier
        if (!insertMode) {
            cbSupplier.setSelectedItem(supplierComboboxModel.getSupplierFromValue(this.inbound.getSupName()));
        } else {
//            cbSupplier.setSelectedIndex();
        }
        jdcDate.setDate(new Date());

        // Load lai table product
        inboundDetailTableModel.load(backup.getInID());
        // Load lai Supplier invoice id
        txtInvoice.setText(backup.getSupInvoiceID());

        if (mustInfo) {
            SwingUtils.showInfoDialog(SwingUtils.DB_RESET);
        }
        setTrackChanges(false);
    }

    private void refreshAction(boolean mustInfo) {
        if (mustInfo) {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            // Refresh table
            productTableModel.refresh();

            // Refresh combobox column table
//            branchNameComboBoxModel1.refresh();
            setCursor(null);

        } else {
            // Refresh table
            productTableModel.refresh();

        }

    }

    public void setTrackChanges(boolean trackChanges) {
        this.trackChanges = trackChanges;
        btnSave.setEnabled(trackChanges);
        btReset.setEnabled(trackChanges);
    }

    private void refreshAction2(boolean mustInfo) {
        if (mustInfo) {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            // Refresh table
            inboundDetailTableModel.refresh();

            // Refresh combobox column table
//            branchNameComboBoxModel1.refresh();
            setCursor(null);

        } else {
            // Refresh table
            inboundDetailTableModel.refresh();

        }

    }

    private void scrollToRow(int row) {
        tbProductList.getSelectionModel().setSelectionInterval(row, row);
        tbProductList.scrollRectToVisible(new Rectangle(tbProductList.getCellRect(row, 0, true)));
    }

    private void doFilter() {
        RowFilter<ProductTableModel, Object> rf = null;

        List<RowFilter<ProductTableModel, Object>> filters = new ArrayList<>();

//        // Get the index of all the selected items
//        int[] selectedIx = list.getSelectedIndices();
//
//        // Get all the selected items using the indices
//        for (int i = 0; i < selectedIx.length; i++) {
//            Object sel = list.getModel().getElementAt(selectedIx[i]).getBraName();
//            System.err.println(sel);
        //lay supid de filter braname
        ArrayList arr = new ArrayList<String>();
        SupplierDAOImpl sdi = new SupplierDAOImpl();
        arr = sdi.getBranchFromValue(supplierComboboxModel.getSelectedItem());

        for (Object item : arr) {
            filters.add(RowFilter.regexFilter("^" + item.toString(), 1));
            rf = RowFilter.orFilter(filters);
        }

        sorter.setRowFilter(rf);
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
        jLabel4 = new javax.swing.JLabel();
        txtInvoice = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtUser = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        tfDate = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbInDetail = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbProductList = new javax.swing.JTable();
        btnSave = new javax.swing.JButton();
        btnAdd = new javax.swing.JButton();
        btDelete = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        btReset = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        cbSupplier = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(730, 627));
        setResizable(false);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Inbound", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 13), new java.awt.Color(255, 153, 0))); // NOI18N

        jLabel4.setText("Suppplier Invoice:");

        jLabel5.setText("User:");

        txtUser.setEnabled(false);
        txtUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtUserActionPerformed(evt);
            }
        });

        jLabel2.setText("Date:");

        tfDate.setEditable(false);
        tfDate.setEnabled(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tfDate, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtInvoice, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel5)
                .addGap(18, 18, 18)
                .addComponent(txtUser)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtInvoice)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel4)
                        .addComponent(jLabel5)
                        .addComponent(txtUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2)
                        .addComponent(tfDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Inbound Details", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 13), new java.awt.Color(255, 153, 0))); // NOI18N

        tbInDetail.setAutoCreateRowSorter(true);
        tbInDetail.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tbInDetail.setFillsViewportHeight(true);
        tbInDetail.setRowHeight(25);
        tbInDetail.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tbInDetail.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbInDetailMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbInDetail);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE)
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Product", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 13), new java.awt.Color(255, 153, 0))); // NOI18N

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
        tbProductList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tbProductList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbProductListMouseClicked(evt);
            }
        });
        tbProductList.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                tbProductListPropertyChange(evt);
            }
        });
        jScrollPane2.setViewportView(tbProductList);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/order/OK2.png"))); // NOI18N
        btnSave.setText("Save all");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        btnAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/product/add2.png"))); // NOI18N
        btnAdd.setText("Add");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        btDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/order/crossout.png"))); // NOI18N
        btDelete.setText("Remove");
        btDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btDeleteActionPerformed(evt);
            }
        });

        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/order/Cancel2.png"))); // NOI18N
        jButton5.setText("Discard");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        btReset.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/product/refresh_1.png"))); // NOI18N
        btReset.setText("Reset Data");
        btReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btResetActionPerformed(evt);
            }
        });

        jLabel3.setText("Supplier:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, 650, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btReset, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnSave, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btReset, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton5)))
                .addGap(16, 16, 16))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtUserActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtUserActionPerformed

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed

        
        
        InboundDetail inbound = new InboundDetail();
        inbound.setInID(0);
        inbound.setProID(selectedProduct.getProId());
        inbound.setProName(selectedProduct.getProName());

        inbound.setProCost(1000000);
        inbound.setProQty(1);
        inboundDetailTableModel.insert(inbound);
        listIn = inboundDetailTableModel.getList();
        setTrackChanges(true);

    }//GEN-LAST:event_btnAddActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        if (insertMode)//insert mode
        {
            insertAction();
        } else {

            updateAction();
        }


    }//GEN-LAST:event_btnSaveActionPerformed

    private void tbInDetailMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbInDetailMouseClicked

    }//GEN-LAST:event_tbInDetailMouseClicked

    private void tbProductListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbProductListMouseClicked

        tbProductList.setRowSelectionAllowed(true);
    }//GEN-LAST:event_tbProductListMouseClicked

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        cancelAction();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void btDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btDeleteActionPerformed
        deleteAction();
    }//GEN-LAST:event_btDeleteActionPerformed

    private void btResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btResetActionPerformed
        
        int ans = SwingUtils.showConfirmDialog("Are you sure to reset data to initial value?");
        if(ans==JOptionPane.YES_OPTION)
            resetAction(true);
    
    }//GEN-LAST:event_btResetActionPerformed

    private void tbProductListPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_tbProductListPropertyChange

    }//GEN-LAST:event_tbProductListPropertyChange
    public void deleteAction() {
        int ans = SwingUtils.showConfirmDialog("Are you sure to delete? Quantity of such product will deduct in stock");
        if(ans==JOptionPane.YES_OPTION){
        inboundDetailTableModel.delete(selectedInDetail);
        selectedRowIndex = (selectedRowIndex == tbInDetail.getRowCount() ? tbInDetail.getRowCount() - 1 : selectedRowIndex++);
        // scrollToRow(selectedRowIndex);
        //refresh table
        //refreshAction2(false);
        //inboundDetailTableModel.refresh();
        setTrackChanges(true);
        }
    }

    public void cancelAction() {
        if (trackChanges) {
            if (SwingUtils.showConfirmDialog("Discard change(s) and quit ?") == JOptionPane.YES_OPTION) {
                dispose();
            }
        } else {
            dispose();
        }
    }

    public void insertAction() {

        if (txtInvoice.getText().equals("")) {
            SwingUtils.showInfoDialog("You have not input invoice id");
            txtInvoice.requestFocus();
            return;
        } else if (!txtInvoice.getText().matches("[a-zA-Z0-9 ]+")) {
            SwingUtils.showInfoDialog("Invalid input");
            txtInvoice.requestFocus();
            return;
        }
        String temp = txtInvoice.getText();//check dupplicate sup invoice id
        if (new InboundDetailDAOImpl().insert(listIn, temp)) { //insert inbound moi voi gia tri mac dinh

            //update lai nhung gia tri duoc sua moi
            Inbound ib = new Inbound();
            ib.setInDate(jdcDate.getDate());
            System.err.println(jdcDate.getDate());
            if (cbSupplier.getSelectedIndex() == -1) {
                ib.setSupName(supplierComboboxModel.getElementAt(0).getSupName());
            } else {
                ib.setSupName(supplierComboboxModel.getSelectedItem().getSupName());
            }
            ib.setSupInvoiceID(txtInvoice.getText());
            ib.setUserName(LoginFrame.config.userName);
            //update lai database
            if (new InboundDetailDAOImpl().update(ib)) {
                SwingUtils.showInfoDialog(SwingUtils.INSERT_SUCCESS);
                //tat dialog
                dispose();
            } else {//neu invoice id bi trung
                
                //delete inbound moi them vao
                new InboundDetailDAOImpl().delete(ib);
                txtInvoice.requestFocus();
                txtInvoice.selectAll();
            }

        } else {
            SwingUtils.showInfoDialog(SwingUtils.INSERT_FAIL);
            txtInvoice.requestFocus();
            txtInvoice.selectAll();
            return;
        }

    }

    public void updateAction() {

        if (txtInvoice.getText().equals("")) {
            SwingUtils.showInfoDialog("You have not input invoice id");
            txtInvoice.requestFocus();
            return;
        } else if (!txtInvoice.getText().matches("[a-zA-Z0-9 ]+")) {
            SwingUtils.showInfoDialog("Invalid input");
            txtInvoice.requestFocus();
            return;
        } else {

            //update lai nhung gia tri moi
            Inbound ib = new Inbound();
            ib.setInID(this.inbound.getInID());

            if (cbSupplier.getSelectedIndex() == -1) {
                ib.setSupName(supplierComboboxModel.getElementAt(0).getSupName());
            } else {
                ib.setSupName(supplierComboboxModel.getSelectedItem().getSupName());
            }

            ib.setSupInvoiceID(txtInvoice.getText());

            if (new InboundDetailDAOImpl().update(listIn, ib, inbound)) {
                SwingUtils.showInfoDialog(SwingUtils.UPDATE_SUCCESS);//update thanh cong
                //tat dialog
                dispose();
            } else {
                SwingUtils.showInfoDialog(SwingUtils.UPDATE_FAIL);//update that bai
            }
        }
    }

    /**
     * @param args the command line arguments
     */
    private void fetchProductDetails() {

        selectedRowIndex = tbProductList.getSelectedRow();

        selectedProduct.setProId((int) tbProductList.getValueAt(selectedRowIndex, 0));

        selectedProduct.setBraname((String) tbProductList.getValueAt(selectedRowIndex, 1));
        selectedProduct.setProName((String) tbProductList.getValueAt(selectedRowIndex, 2));
        selectedProduct.setProStock((int) tbProductList.getValueAt(selectedRowIndex, 3));
        selectedProduct.setProPrice((float) tbProductList.getValueAt(selectedRowIndex, 4));
        selectedProduct.setProDesc((String) tbProductList.getValueAt(selectedRowIndex, 5));
//        selectedProduct.setProEnabled((boolean) tbProductList.getValueAt(selectedRowIndex, 6));
        //cot sale off k cho update

        //cot image
//        //cot braid bi an di
//        selectedProduct.setBraId((int) tbProductList.getModel().getValueAt(selectedRowIndex, 9));
    }

    private void fetchAction() {

//        selectedRowIndex = tbInDetail.getSelectedRow();
//       selectedInDetail.setProID((int) tbInDetail.getValueAt(selectedRowIndex, 0));
//       selectedInDetail.setProName((String) tbInDetail.getValueAt(selectedRowIndex, 1));
//       selectedInDetail.setProCost((float) tbInDetail.getValueAt(selectedRowIndex, 2));
//       selectedInDetail.setProQty((int) tbInDetail.getValueAt(selectedRowIndex, 3));
//       System.err.println("indetail selected "+selectedInDetail);
        selectedRowIndex1 = tbInDetail.getSelectedRow();
        System.err.println(selectedRowIndex1);
        if (selectedRowIndex1 >= 0) {
            int idx = tbInDetail.convertRowIndexToModel(selectedRowIndex1);
            selectedInDetail = inboundDetailTableModel.getInboundDetailFromIndex(idx);
            inboundDetailTableModel.setSelectingIndex(idx);
        } else {
            selectedProduct = null;
            inboundDetailTableModel.setSelectingIndex(-1);
        }
    }

    private void fetchSupplier(){
        selectedSupplier=(Supplier) cbSupplier.getSelectedItem();
        
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btDelete;
    private javax.swing.JButton btReset;
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnSave;
    private javax.swing.JComboBox<Supplier> cbSupplier;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable tbInDetail;
    private javax.swing.JTable tbProductList;
    private javax.swing.JTextField tfDate;
    private javax.swing.JTextField txtInvoice;
    private javax.swing.JTextField txtUser;
    // End of variables declaration//GEN-END:variables

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() == cbSupplier) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                setTrackChanges(true);
            }
        }
    }
}
