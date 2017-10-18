/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package outbound.controller;


import com.toedter.calendar.JDateChooser;
import database.DBProvider;
import database.IDAO;
import inbound.model.CurrencyCellRenderer;

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
import org.jdesktop.xswingx.PromptSupport;

import inbound.model.Supplier;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.rowset.CachedRowSet;
import javax.swing.AbstractAction;
import javax.swing.event.RowSorterEvent;
import javax.swing.event.RowSorterListener;
import javax.swing.table.DefaultTableCellRenderer;
import main.controller.LoginFrame;
import order.controller.OrderBranchListCellRenderer;
import order.controller.OrderBranchListModel;
import order.model.OrderBranch;
import outbound.model.Outbound;
import outbound.model.OutboundDetail;
import outbound.model.OutboundDetailDAOImpl;
import outbound.model.OutboundDetailTableModel;
import outbound.model.Product;
import outbound.model.ProductTableModel;

import utility.SpinnerCellEditor;
import utility.SwingUtils;
import utility.TableCellListener;

/**
 *
 * @author tuan
 */
public class OutboundDialog extends javax.swing.JDialog implements ItemListener ,IDAO<OutboundDetail> {

    private Outbound backup;
    private Outbound outbound;
    
    private OutboundDetail outboundForDelete;

    ProductTableModel productTableModel;
    OutboundDetailTableModel outboundDetailTableModel;

    JDateChooser jdcDate = new JDateChooser();

    private Product selectedProduct;
    private int selectedRowIndex;
    private int selectedRowIndex1;
    private OutboundDetail selectedInDetail;
    //row sorter
    private TableRowSorter<ProductTableModel> sorter;

    //inbound list de add moi vao outbound
    List<OutboundDetail> listOut = new ArrayList<>();
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
    
    //Outbound Detail Table
    private static int COL_OutDID = 0;
    private static int COL_OutDName = 1;
    private static int COL_OutDQTy = 2;
    
    
    public OutboundDialog(Outbound outbound) {
        super((JFrame) null, true);
        insertMode = outbound == null;
        initComponents();
        setLocationRelativeTo(null);

        setting();
        btDelete.setEnabled(false);
        btReset.setEnabled(false);
        if (insertMode) {
            setTitle("New Outbound");
            this.outbound = new Outbound();
            this.outbound.setOutID(-1);
            this.outbound.setOutDate(new Date());
            
            this.outbound.setUserID(1);
            this.outbound.setOutContent("");
            backup = this.outbound.clone();
            setTrackChanges(false);
            txtUser.setText(LoginFrame.config.userName);
        } else {
            this.outbound = outbound.clone();
            backup = this.outbound.clone();
            
            jdcDate.setDate(this.outbound.getOutDate());//set data cho ngay
            jdcDate.setEnabled(false);//disable ngay,k cho update
            txtContent.setText(this.outbound.getOutContent());
            txtUser.setText(this.outbound.getUserName());
            setTrackChanges(false);
        }
        
        outboundDetailTableModel.load(this.outbound.getOutID());//null neu insert mode
        
        //update lai list
       listOut = outboundDetailTableModel.getList();//null neu insert mode
       
       btnAdd.setEnabled(false);
    }

    

    public void setting() {

        selectedProduct = new Product();
        selectedInDetail = new OutboundDetail();
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

        //set data cho table InDetail
        outboundDetailTableModel = new OutboundDetailTableModel();
        tbInDetail.setModel(outboundDetailTableModel);
        tbInDetail.setRowSelectionAllowed(true);

        // Set data cho combobox supplier
        
//        cbTarget.addActionListener (new ActionListener () {
//            public void actionPerformed(ActionEvent e) {
//                setTrackChanges(true);
//            }
//        });

        //get current user name
        txtUser.setText(main.model.Login.USER_NAME);

        // Set promt text
        PromptSupport.setPrompt("Input Content", txtContent);

        PromptSupport.setFocusBehavior(PromptSupport.FocusBehavior.SHOW_PROMPT, txtContent);

        // ---------- Settup Jdatechooser
        jdcDate.setBounds(0, 0, 130, 20);
        jdcDate.setDateFormatString("MMM dd, yyyy");
        jdcDate.getDateEditor().setEnabled(false);
        pnlDate.add(jdcDate);
        //Lay ngay hien tai
        java.util.Date today = new java.util.Date();
        jdcDate.setDate(today);
        
        //bat su kien sua thong tin table InDetail
        TableCellListener tcl = new TableCellListener(tbInDetail, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TableCellListener tcl = (TableCellListener) e.getSource();
                switch (tcl.getColumn()){
                    case 2: setTrackChanges(true);break;//co sua cot quantity
                }
                }
        });
        //bat su kien click vao table product
        tbProductList.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
            DefaultListSelectionModel model = (DefaultListSelectionModel) e.getSource();
            if (!model.isSelectionEmpty()) {
                fetchProductDetails();
                btnAdd.setEnabled(true);
                
                if(selectedProduct.getProStock()>0)
                //spinner cho table, lay maximum la so luong ton kho
                tbInDetail.getColumnModel().getColumn(2).setCellEditor(new SpinnerCellEditor(1, selectedProduct.getProStock()));
                
                tbProductList.setSurrendersFocusOnKeystroke(false);
            }
        });
        
        txtContent.getDocument().addDocumentListener(new DocumentListener() {
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

        //event trong truong hop k co record trong table
        sorter.addRowSorterListener(new RowSorterListener() {
            @Override
            public void sorterChanged(RowSorterEvent e) {
               if(tbInDetail.getRowCount()==0)
                   btnAdd.setEnabled(false);
               else
                   btnAdd.setEnabled(true);
            }
        });
        // Bat su kien select row tren table Indetail
        tbInDetail.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
            DefaultListSelectionModel model = (DefaultListSelectionModel) e.getSource();
            if (!model.isSelectionEmpty()) {
                fetchAction();
                btDelete.setEnabled(true);
                if(selectedInDetail.getProQty()>returnProstockByProID(selectedInDetail.getProID())){//neu so luong trong stock lon hon so luong thuc trong outbound khi update thi max cua spinner la 1
                    tbInDetail.getColumnModel().getColumn(2).setCellEditor(new SpinnerCellEditor(1,1));
                }
                else//neu stock <= so luong thuc trong outbound thi max cua spinner la stock
                    tbInDetail.getColumnModel().getColumn(2).setCellEditor(new SpinnerCellEditor(1, returnProstockByProID(selectedInDetail.getProID())));
                
            }
            else
                btDelete.setEnabled(false);
        });

        tbInDetail.setDefaultEditor(Float.class, new FloatEditor(1000000, 30000000));
        
        tbInDetail.getColumnModel().getColumn(2).setCellEditor(new SpinnerCellEditor(1, 10));
        

        
        // Set data cho list filter
        list.setModel(new OrderBranchListModel());
        list.setCellRenderer(new OrderBranchListCellRenderer());
        list.setSelectionModel(new DefaultListSelectionModel() {
            @Override
            public void setSelectionInterval(int index0, int index1) {
                if (super.isSelectedIndex(index0)) {
                    super.removeSelectionInterval(index0, index1);
                } else {
                    super.addSelectionInterval(index0, index1);
                }
            }

        });
        list.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (list.isSelectionEmpty()) {

                    refreshAction(false);
                    productTableModel.refresh();
                    doFilter();
                    return;
                } else {
                    doFilter();
                }

            }
        });
        
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
        tbProductList.getColumnModel().getColumn(COL_BraName).setMaxWidth(80);
        tbProductList.getColumnModel().getColumn(COL_BraName).setCellRenderer(centerRenderer);
        
        
        //name
        tbProductList.getColumnModel().getColumn(COL_ProName).setMinWidth(80);
        tbProductList.getColumnModel().getColumn(COL_ProName).setCellRenderer(centerRenderer);
        
        //stock
        tbProductList.getColumnModel().getColumn(COL_ProStock).setMinWidth(35);
        tbProductList.getColumnModel().getColumn(COL_ProStock).setMaxWidth(50);
        tbProductList.getColumnModel().getColumn(COL_ProStock).setCellRenderer(centerRenderer);

        //price
        tbProductList.getColumnModel().getColumn(COL_ProPrice).setMinWidth(80);
        tbProductList.getColumnModel().getColumn(COL_ProPrice).setCellRenderer(new CurrencyCellRenderer());

        //desc
        tbProductList.getColumnModel().getColumn(COL_ProDescr).setMinWidth(50);
        tbProductList.getColumnModel().getColumn(COL_ProDescr).setCellRenderer(centerRenderer);
        
        
        //id
        tbInDetail.getColumnModel().getColumn(COL_OutDID).setMinWidth(35);
        tbInDetail.getColumnModel().getColumn(COL_OutDID).setMaxWidth(70);
        tbInDetail.getColumnModel().getColumn(COL_OutDID).setCellRenderer(centerRenderer);
        
        
        //InDetail Name
        tbInDetail.getColumnModel().getColumn(COL_OutDName).setMinWidth(500);
        tbInDetail.getColumnModel().getColumn(COL_OutDName).setMaxWidth(500);
        tbInDetail.getColumnModel().getColumn(COL_OutDName).setCellRenderer(centerRenderer);
        
        
        //stock
        tbInDetail.getColumnModel().getColumn(COL_OutDQTy).setMinWidth(35);
        tbInDetail.getColumnModel().getColumn(COL_OutDQTy).setMaxWidth(150);
        tbInDetail.getColumnModel().getColumn(COL_OutDQTy).setCellRenderer(centerRenderer);

    }
    public int returnProstockByProID(int proid){
        CachedRowSet crs2 = getCRS("SELECT prostock from products where proid=?",proid);
        int result = 0;
        try {
            
            crs2.next();
            result = crs2.getInt("prostock");
        } catch (SQLException ex) {
            Logger.getLogger(OutboundDialog.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
        
    }
    private void resetAction(boolean mustInfo) {
        // Load lai vung supplier
//        cbTarget.setSelectedItem(backup.getTargetID());
        

        
        // Load lai table product
        outboundDetailTableModel.load(backup.getOutID());
        // Load lai Supplier invoice id
        txtContent.setText(backup.getOutContent());

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
            outboundDetailTableModel.refresh();

            // Refresh combobox column table
//            branchNameComboBoxModel1.refresh();
            setCursor(null);

        } else {
            // Refresh table
            outboundDetailTableModel.refresh();

        }

    }

    private void scrollToRow(int row) {
        tbProductList.getSelectionModel().setSelectionInterval(row, row);
        tbProductList.scrollRectToVisible(new Rectangle(tbProductList.getCellRect(row, 0, true)));
    }

    private void doFilter() {
        RowFilter<ProductTableModel, Object> rf = null;

        List<RowFilter<ProductTableModel, Object>> filters = new ArrayList<>();

        // Get the index of all the selected items
        int[] selectedIx = list.getSelectedIndices();

        // Get all the selected items using the indices
        for (int i = 0; i < selectedIx.length; i++) {
            Object sel = list.getModel().getElementAt(selectedIx[i]).getBraName();
            filters.add(RowFilter.regexFilter("^" + sel, 1));
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
        pnlDate = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtUser = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        txtContent = new javax.swing.JTextArea();
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
        jScrollPane3 = new javax.swing.JScrollPane();
        list = new javax.swing.JList<>();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(730, 627));
        setResizable(false);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Outbound", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 13), new java.awt.Color(255, 153, 0))); // NOI18N

        pnlDate.setEnabled(false);

        javax.swing.GroupLayout pnlDateLayout = new javax.swing.GroupLayout(pnlDate);
        pnlDate.setLayout(pnlDateLayout);
        pnlDateLayout.setHorizontalGroup(
            pnlDateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 187, Short.MAX_VALUE)
        );
        pnlDateLayout.setVerticalGroup(
            pnlDateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jLabel4.setText("Content");

        jLabel5.setText("User:");

        txtUser.setEnabled(false);
        txtUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtUserActionPerformed(evt);
            }
        });

        jLabel2.setText("Date:");

        txtContent.setColumns(20);
        txtContent.setRows(5);
        jScrollPane4.setViewportView(txtContent);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(38, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtUser, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(137, 137, 137)
                        .addComponent(jLabel4))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(pnlDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(txtUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(8, 8, 8))
                            .addComponent(pnlDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Outbound Details", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 13), new java.awt.Color(255, 153, 0))); // NOI18N

        jScrollPane1.setAutoscrolls(true);

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
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
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
        jScrollPane2.setViewportView(tbProductList);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 206, Short.MAX_VALUE)
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

        list.setLayoutOrientation(javax.swing.JList.HORIZONTAL_WRAP);
        list.setVisibleRowCount(-1);
        jScrollPane3.setViewportView(list);

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/product/refresh_1.png"))); // NOI18N
        jButton2.setBorderPainted(false);
        jButton2.setContentAreaFilled(false);
        jButton2.setFocusPainted(false);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2))
            .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btReset, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton2)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btReset, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnSave)
                        .addComponent(jButton5)))
                .addGap(10, 10, 10))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtUserActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtUserActionPerformed

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed

        OutboundDetail inbound = new OutboundDetail();
        inbound.setOutID(0);
        inbound.setProID(selectedProduct.getProId());
        inbound.setProName(selectedProduct.getProName());
        inbound.setProQty(selectedProduct.getProStock());
        outboundDetailTableModel.insert(inbound);
        listOut = outboundDetailTableModel.getList();
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
        int ans = SwingUtils.showConfirmDialog("Are you sure to delete? Quantity of such product will add back to stock");
        if(ans==JOptionPane.YES_OPTION)
        deleteAction();
    }//GEN-LAST:event_btDeleteActionPerformed

    private void btResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btResetActionPerformed
        int ans = SwingUtils.showConfirmDialog("Are you sure to reset data to initial value?");
        if(ans == JOptionPane.YES_OPTION)
        resetAction(true);
    }//GEN-LAST:event_btResetActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
       resetAction();
    }//GEN-LAST:event_jButton2ActionPerformed
    
    public void resetAction(){
        list.clearSelection();
    }
    
    
    
    public void deleteAction() {

        outboundDetailTableModel.delete(selectedInDetail);
        selectedRowIndex = (selectedRowIndex == tbInDetail.getRowCount() ? tbInDetail.getRowCount() - 1 : selectedRowIndex++);
//         scrollToRow(selectedRowIndex);
//        refresh table
//        refreshAction2(false);
//        inboundDetailTableModel.refresh();



        
        setTrackChanges(true);
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

        
        
        if (new OutboundDetailDAOImpl().insert(listOut)) { //insert inbound moi voi gia tri mac dinh

            //update lai nhung gia tri duoc sua moi
            Outbound ib = new Outbound();
            ib.setOutDate(jdcDate.getDate());
           
//            if (cbTarget.getSelectedIndex() == -1) {
//                 ib.setTargetID(1);//order
//            } else {
//                ib.setTargetID(changeTargetID(cbTarget.getSelectedItem().toString()));
//            }
             
            ib.setOutContent(txtContent.getText());
            ib.setUserName(LoginFrame.config.userName);
            //update lai database
            if (new OutboundDetailDAOImpl().update(ib)) {
                SwingUtils.showInfoDialog(SwingUtils.INSERT_SUCCESS);
                //tat dialog
                dispose();
            } 

        } else {
            SwingUtils.showInfoDialog(SwingUtils.INSERT_FAIL);
            return;
        }

    }
    
    
    public int changeTargetID(String target){
        
       if(target.equals("Order"))
           return 1;
       else if(target.equals("Service"))
           return 2;
       else
           return 3;
    }
    public void updateAction() {
            //update lai nhung gia tri moi
            Outbound ib = new Outbound();
            ib.setOutID(this.outbound.getOutID());
            
//            if (cbTarget.getSelectedIndex() == -1) {
//                 ib.setTargetID(1);// target order
//            } else {
//                ib.setTargetID(changeTargetID(cbTarget.getSelectedItem().toString()));
//            }
                
            ib.setOutContent(txtContent.getText());
             
        
//            if(tbInDetail.getRowCount()==0){//neu co 0 record
//                listOut.add(new OutboundDetail(selectedInDetail.getProID(), selectedInDetail.getProName(), selectedInDetail.getProQty()));
//            }

             
             if (new OutboundDetailDAOImpl().update(listOut,ib,outbound)){
                 SwingUtils.showInfoDialog(SwingUtils.UPDATE_SUCCESS);//update thanh cong
                 //tat dialog
                 dispose();
             }
             else{
                 SwingUtils.showInfoDialog(SwingUtils.UPDATE_FAIL);//update that bai
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
        if (selectedRowIndex1 >= 0) {
            int idx = tbInDetail.convertRowIndexToModel(selectedRowIndex1);
            selectedInDetail = outboundDetailTableModel.getInboundDetailFromIndex(idx);
            outboundDetailTableModel.setSelectingIndex(idx);
        } else {
            selectedProduct = null;
            outboundDetailTableModel.setSelectingIndex(-1);
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btDelete;
    private javax.swing.JButton btReset;
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JList<OrderBranch> list;
    private javax.swing.JPanel pnlDate;
    private javax.swing.JTable tbInDetail;
    private javax.swing.JTable tbProductList;
    private javax.swing.JTextArea txtContent;
    private javax.swing.JTextField txtUser;
    // End of variables declaration//GEN-END:variables

    @Override
    public void itemStateChanged(ItemEvent e) {
//        if (e.getSource() == cbTarget) {
//            if (e.getStateChange() == ItemEvent.SELECTED) {
//                setTrackChanges(true);
//            }
//    }
    }

    @Override
    public List<OutboundDetail> getList() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean insert(OutboundDetail model) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean update(OutboundDetail model) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean delete(OutboundDetail model) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getSelectingIndex(int idx) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setSelectingIndex(int idx) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
