package order.controller;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import main.controller.LoginFrame;
import order.model.Order;
import order.model.OrderBranch;
import order.model.OrderCustomer;
import order.model.OrderProduct;
import order.model.OrderStatus;
import utility.CurrencyCellRenderer;
import utility.PercentCellRenderer;
import utility.SpinnerCellEditor;
import utility.SwingUtils;
import utility.SwingUtils.FormatType;
import utility.TableCellListener;

/**
 *
 * @author Hoang
 */
public class OrderDialog extends javax.swing.JDialog implements ItemListener {

//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(new Runnable() {
//            @Override
//            public void run() {
//                new OrderDialog(null).setVisible(true);
//            }
//        });
//    }

    private Order backup;
    private Order order;
    private int backupRowCount;

    // Khai bao model
    private OrderProductTableModelDialog orderProductTableModelDialog;
    private OrderStatusComboBoxModel orderStatusComboBoxModel;
    private OrderStatusComboBoxRenderer orderStatusComboBoxRenderer;
    private OrderCustomerComboBoxModel orderCustomerComboBoxModel;
    private OrderCustomerComboBoxRenderer orderCustomerComboBoxRenderer;
    private OrderProductComboBoxModel orderProductComboBoxModel;

    // Product dang duoc chon trong table product list
    private OrderProduct selectedProduct;
    private int selectedRowIndex = -1;

    public static final int COL_PRONO = 0;
    public static final int COL_PRONAME = 1;
    public static final int COL_PROQTY = 2;
    public static final int COL_PROPRICE1 = 3;
    public static final int COL_SALEAMOUNT = 4;
    public static final int COL_PROPRICE2 = 5;
    public static final int COL_PROID = 6;

    // Two mode: insert va update
    private boolean insertMode;

    // Flag de theo doi co thay doi noi dung gi khong
    private boolean trackChanges;

    public OrderDialog(Order order) {
        super((JFrame) null, true);
        initComponents();
        setLocationRelativeTo(null);
        insertMode = order == null;

        // Set data cho combobox status
        orderStatusComboBoxModel = new OrderStatusComboBoxModel();
        orderStatusComboBoxRenderer = new OrderStatusComboBoxRenderer();
        cbStatus.setModel(orderStatusComboBoxModel);
        cbStatus.setRenderer(orderStatusComboBoxRenderer);
        cbStatus.addItemListener(this);

        // Set data cho combobox customer
        orderCustomerComboBoxModel = new OrderCustomerComboBoxModel();
        orderCustomerComboBoxRenderer = new OrderCustomerComboBoxRenderer();
        cbCustomer.setModel(orderCustomerComboBoxModel);
        cbCustomer.setRenderer(orderCustomerComboBoxRenderer);
        cbCustomer.addItemListener(this);

        // Set data cho combobox product name
        orderProductComboBoxModel = new OrderProductComboBoxModel();

        // Set data cho table
        orderProductTableModelDialog = new OrderProductTableModelDialog();
        tbProduct.setModel(orderProductTableModelDialog);

        // Set height cho table header
        tbProduct.getTableHeader().setPreferredSize(new Dimension(300, 30));

        // Col pro ID (HIDDEN)
        tbProduct.getColumnModel().getColumn(COL_PROID).setMinWidth(0);
        tbProduct.getColumnModel().getColumn(COL_PROID).setMaxWidth(0);
        // Col pro No
        tbProduct.getColumnModel().getColumn(COL_PRONO).setMinWidth(40);
        tbProduct.getColumnModel().getColumn(COL_PRONO).setMaxWidth(50);

        // Col pro name
        tbProduct.getColumnModel().getColumn(COL_PRONAME).setMinWidth(450);
        tbProduct.getColumnModel().getColumn(COL_PRONAME).setCellEditor(new OrderProductComboBoxCellEditor(orderProductComboBoxModel));

        // Col quantity
        tbProduct.getColumnModel().getColumn(COL_PROQTY).setMinWidth(50);
        tbProduct.getColumnModel().getColumn(COL_PROQTY).setCellEditor(new SpinnerCellEditor(1, 10));

        // Col price 1
        tbProduct.getColumnModel().getColumn(COL_PROPRICE1).setMinWidth(100);
        tbProduct.getColumnModel().getColumn(COL_PROPRICE1).setCellRenderer(new CurrencyCellRenderer());

        // Col salesoff
        tbProduct.getColumnModel().getColumn(COL_SALEAMOUNT).setMinWidth(100);
        tbProduct.getColumnModel().getColumn(COL_SALEAMOUNT).setCellRenderer(new PercentCellRenderer());

        // Col price 2
        tbProduct.getColumnModel().getColumn(COL_PROPRICE2).setMinWidth(110);
        tbProduct.getColumnModel().getColumn(COL_PROPRICE2).setCellRenderer(new CurrencyCellRenderer());

        // Bat su kien select row tren table product
        tbProduct.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
            DefaultListSelectionModel model = (DefaultListSelectionModel) e.getSource();
            if (!model.isSelectionEmpty()) {
                fetchAction();
                if (orderProductTableModelDialog.getRowCount() > 1) {
                    btRemove.setEnabled(true);
                }
            } else {
                btRemove.setEnabled(false);
            }
        });

        //<editor-fold defaultstate="collapsed" desc="Set cell listener cho updating">
        TableCellListener tcl = new TableCellListener(tbProduct, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TableCellListener tcl = (TableCellListener) e.getSource();
                switch (tcl.getColumn()) {
                    case COL_PRONAME:
                        String oldValue = (String) tcl.getOldValue();
                        String newValue = (String) tcl.getNewValue();
                        if (!newValue.equals(OrderProduct.DEFAULT_PRONAME)) {
                            // Check duplicate product
                            if (checkDuplicate((String) tcl.getNewValue())) {
                                SwingUtils.showErrorDialog("Duplicated item is not allowed in an order !");
                                tbProduct.setValueAt(oldValue, tbProduct.getSelectedRow(), COL_PRONAME);
                            } else if (checkStockZero((String) tcl.getNewValue())) {
                                SwingUtils.showErrorDialog("This product is out of stock.\nPlease choose another !");
                                tbProduct.setValueAt(oldValue, tbProduct.getSelectedRow(), COL_PRONAME);
                            } else {
                                // Lay product moi tu combo box gan cho product
                                // trong row cua table
                                selectedProduct = orderProductComboBoxModel.getOrderProductFromName((String) tcl.getNewValue());
                                // Gan quantity moi cho product hien tai
                                selectedProduct.setProQty((int) tbProduct.getValueAt(tbProduct.getSelectedRow(), COL_PROQTY));
                                // Update cac thuoc tinh cua product moi vao table
                                tbProduct.setValueAt(selectedProduct.getProPrice1(), tbProduct.getSelectedRow(), COL_PROPRICE1);
                                tbProduct.setValueAt(selectedProduct.getSalesOffAmount(), tbProduct.getSelectedRow(), COL_SALEAMOUNT);
                                tbProduct.setValueAt(selectedProduct.getProPrice1() * (1 - selectedProduct.getSalesOffAmount()), tbProduct.getSelectedRow(), COL_PROPRICE2);
                                tbProduct.setValueAt(selectedProduct.getProID(), tbProduct.getSelectedRow(), COL_PROID);
                                // Update label
                                updateTotalLabel();
                                if (!checkProductEmpty()) {
                                    setTrackChanges(true);
                                }
                            }
                        } else if (!newValue.equals(oldValue)) {
                            tbProduct.setValueAt(oldValue, tbProduct.getSelectedRow(), COL_PRONAME);
                        }
                        break;
                    case COL_PROQTY:
                        // Gan quantity moi cho product tren row
                        selectedProduct.setProQty((int) tbProduct.getValueAt(tbProduct.getSelectedRow(), COL_PROQTY));
                        // Update label
                        updateItemsLabel();
                        if (!checkProductEmpty()) {
                            setTrackChanges(true);
                        }
                        break;
                }
            }
        });

//</editor-fold>
        // Xu ly mode
        if (insertMode) { // Mode insert
            setTitle("New Order");
            this.order = new Order();
            this.order.setOrdID(-1);
            this.order.setOrdStatusID(1);
//            this.order.setOrdStatus("Waiting");
//            this.order.setCusID(orderCustomerComboBoxModel.getElementAt(orderCustomerComboBoxModel.getSize() - 1).getCusID());
//            this.order.setCusDiscount(orderCustomerComboBoxModel.getElementAt(orderCustomerComboBoxModel.getSize() - 1).getCusDiscount());
//            this.order.setUserID(1);
            this.order.setUserName(LoginFrame.config.userName);
            this.order.setOrdDate(new Date());
            backup = this.order.clone(); // Backup
            tfID.setText("New");
            tfDate.setText(SwingUtils.formatString(new Date(), FormatType.DATE));
            tfUser.setText(LoginFrame.config.userName);
            cbStatus.setSelectedIndex(0);
            cbCustomer.setSelectedIndex(cbCustomer.getItemCount() - 1);
            setTrackChanges(false);
        } else { // Mode update
            setTitle("Update Order");
            this.order = order.clone();
            backup = this.order.clone(); // Backup
            tfID.setText(this.order.getOrdID() + "");
            tfDate.setText(SwingUtils.formatString(this.order.getOrdDate(), FormatType.DATE));
            tfUser.setText(this.order.getUserName());
            cbStatus.setSelectedItem(orderStatusComboBoxModel.getStatusFromValue(this.order.getOrdStatus()));
            cbCustomer.setSelectedItem(orderCustomerComboBoxModel.getCustomerFromID(this.order.getCusID()));
            setTrackChanges(false);

            // Xac nhan update discount khi discount cua customer bi thay doi sau order
            if (backup.getCusDiscount() != orderCustomerComboBoxModel.getCustomerFromID(this.order.getCusID()).getCusDiscount()) {
                if (SwingUtils.showConfirmDialog("Discount of this order has changed !\nWould you like to update ?") == JOptionPane.NO_OPTION) {
                    this.order.setCusDiscount(backup.getCusDiscount());
                } else {
                    setTrackChanges(true);
                }
            }
        }

        // Set data cho table chinh
        orderProductTableModelDialog.load(this.order.getOrdID());    //Emply list neu o mode insert
        backupRowCount = orderProductTableModelDialog.getRowCount(); //Backup so row ban dau

        // Set data cho cac label
        updateItemsLabel();
        updateDiscountLabel();

//<editor-fold defaultstate="collapsed" desc="xu ly cho vung filter">
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
                orderProductComboBoxModel.setBraList((List<OrderBranch>) list.getSelectedValuesList());
                orderProductComboBoxModel.filter();
            }
        });

// Su kien cho filter name
        tfNameFilter.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                orderProductComboBoxModel.setProName(tfNameFilter.getText().trim());
                orderProductComboBoxModel.filter();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                orderProductComboBoxModel.setProName(tfNameFilter.getText().trim());
                orderProductComboBoxModel.filter();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                orderProductComboBoxModel.setProName(tfNameFilter.getText().trim());
                orderProductComboBoxModel.filter();
            }
        });
//</editor-fold>
    }

//<editor-fold defaultstate="collapsed" desc="Bat su kien">
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        tfDate = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        cbCustomer = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        cbStatus = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        tfUser = new javax.swing.JTextField();
        tfID = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        btAdd = new javax.swing.JButton();
        btRemove = new javax.swing.JButton();
        btReset = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbProduct = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        btClear = new javax.swing.JButton();
        tfNameFilter = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        list = new javax.swing.JList<>();
        btSave = new javax.swing.JButton();
        btCancel = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        lbItems = new javax.swing.JLabel();
        lbTotal = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        lbDiscount = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Add Order");
        setMaximumSize(new java.awt.Dimension(9999, 9999));
        setMinimumSize(new java.awt.Dimension(900, 700));
        setPreferredSize(new java.awt.Dimension(900, 700));
        setSize(new java.awt.Dimension(900, 700));

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Order Details"));

        jLabel3.setText("Date:");

        tfDate.setEditable(false);
        tfDate.setEnabled(false);
        tfDate.setFocusable(false);

        jLabel4.setText("Customer:");

        cbCustomer.setPreferredSize(new java.awt.Dimension(800, 30));

        jLabel6.setText("Status:");

        jLabel7.setText("Seller:");

        tfUser.setEditable(false);
        tfUser.setEnabled(false);
        tfUser.setFocusable(false);

        tfID.setEditable(false);
        tfID.setEnabled(false);
        tfID.setFocusable(false);

        jLabel8.setText("ID:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(17, 17, 17)
                        .addComponent(cbCustomer, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(17, 17, 17)
                        .addComponent(tfID, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, Short.MAX_VALUE)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tfDate, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, Short.MAX_VALUE)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(tfUser, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfDate, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel7)
                    .addComponent(tfUser, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(cbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tfID, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Product Details"));

        btAdd.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        btAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/order/Add3.png"))); // NOI18N
        btAdd.setText("Add");
        btAdd.setFocusPainted(false);
        btAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btAddActionPerformed(evt);
            }
        });

        btRemove.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        btRemove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/order/Delete2.png"))); // NOI18N
        btRemove.setText("Remove");
        btRemove.setEnabled(false);
        btRemove.setFocusPainted(false);
        btRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btRemoveActionPerformed(evt);
            }
        });

        btReset.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        btReset.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/order/Refresh2.png"))); // NOI18N
        btReset.setText("Reset");
        btReset.setFocusPainted(false);
        btReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btResetActionPerformed(evt);
            }
        });

        jScrollPane2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Order Details"));

        tbProduct.setAutoCreateRowSorter(true);
        tbProduct.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No.", "Product Name", "Quantity", "Price 1", "SalesOff", "Price 2"
            }
        ));
        tbProduct.setFillsViewportHeight(true);
        tbProduct.setRowHeight(25);
        tbProduct.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tbProduct.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(tbProduct);

        jLabel1.setText("Name:");
        jLabel1.setPreferredSize(new java.awt.Dimension(95, 20));

        btClear.setFont(new java.awt.Font("Lucida Grande", 0, 12)); // NOI18N
        btClear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/order/trash_full.png"))); // NOI18N
        btClear.setText("Clear Filter");
        btClear.setFocusPainted(false);
        btClear.setPreferredSize(new java.awt.Dimension(100, 20));
        btClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btClearActionPerformed(evt);
            }
        });

        tfNameFilter.setPreferredSize(new java.awt.Dimension(250, 20));

        jScrollPane3.setMinimumSize(new java.awt.Dimension(200, 30));
        jScrollPane3.setPreferredSize(new java.awt.Dimension(604, 82));

        list.setBackground(new java.awt.Color(51, 51, 51));
        list.setLayoutOrientation(javax.swing.JList.HORIZONTAL_WRAP);
        list.setMaximumSize(new java.awt.Dimension(99999, 9999));
        list.setMinimumSize(new java.awt.Dimension(600, 30));
        list.setPreferredSize(new java.awt.Dimension(600, 600));
        list.setVisibleRowCount(-1);
        jScrollPane3.setViewportView(list);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tfNameFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btClear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btRemove, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btReset, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btRemove, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btReset, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(tfNameFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btClear, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 305, Short.MAX_VALUE))
        );

        btSave.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        btSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/order/OK2.png"))); // NOI18N
        btSave.setText("Save");
        btSave.setEnabled(false);
        btSave.setFocusPainted(false);
        btSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btSaveActionPerformed(evt);
            }
        });

        btCancel.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        btCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/order/Cancel2.png"))); // NOI18N
        btCancel.setText("Cancel");
        btCancel.setFocusPainted(false);
        btCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btCancelActionPerformed(evt);
            }
        });

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Summary"));

        jLabel5.setText("Total items:");

        lbItems.setText("10");

        lbTotal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbTotal.setText("10,000,000 VND");

        jLabel10.setText("Total money:");

        lbDiscount.setText("2%");

        jLabel12.setText("Discount:");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbItems, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbDiscount, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel5)
                        .addComponent(lbItems))
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel12)
                        .addComponent(lbDiscount))
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel10)
                        .addComponent(lbTotal)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btSave, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btSave, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btCancelActionPerformed
        cancelAction();
    }//GEN-LAST:event_btCancelActionPerformed

    private void btAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btAddActionPerformed
        insertAction();
    }//GEN-LAST:event_btAddActionPerformed

    private void btRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btRemoveActionPerformed
        deleteAction();
    }//GEN-LAST:event_btRemoveActionPerformed

    private void btResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btResetActionPerformed
        resetAction(true);
    }//GEN-LAST:event_btResetActionPerformed

    private void btSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btSaveActionPerformed
        updateAction();
    }//GEN-LAST:event_btSaveActionPerformed

    private void btClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btClearActionPerformed
        clearFilter();
    }//GEN-LAST:event_btClearActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btAdd;
    private javax.swing.JButton btCancel;
    private javax.swing.JButton btClear;
    private javax.swing.JButton btRemove;
    private javax.swing.JButton btReset;
    private javax.swing.JButton btSave;
    private javax.swing.JComboBox<OrderCustomer> cbCustomer;
    private javax.swing.JComboBox<order.model.OrderStatus> cbStatus;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lbDiscount;
    private javax.swing.JLabel lbItems;
    private javax.swing.JLabel lbTotal;
    private javax.swing.JList<OrderBranch> list;
    private javax.swing.JTable tbProduct;
    private javax.swing.JTextField tfDate;
    private javax.swing.JTextField tfID;
    private javax.swing.JTextField tfNameFilter;
    private javax.swing.JTextField tfUser;
    // End of variables declaration//GEN-END:variables
//</editor-fold>

    private void insertAction() {
        // Toi da 20 items
        if (orderProductTableModelDialog.getRowCount() == 19) {
            SwingUtils.showInfoDialog("Maximum 20 item in 01 order !");
            btAdd.setEnabled(false);
        }
        OrderProduct product = new OrderProduct();
        product.setProNo(orderProductTableModelDialog.getRowCount() + 1);
        product.setProQty(1);
        product.setProName(OrderProduct.DEFAULT_PRONAME);
        orderProductTableModelDialog.addElement(product);
        updateItemsLabel();
        btSave.setEnabled(false);
    }

    private void deleteAction() {
        btAdd.setEnabled(true);

        // Toi thieu 01 item
        if (orderProductTableModelDialog.getRowCount() == 2) {
            SwingUtils.showInfoDialog("At least 01 item in 01 order !");
            btRemove.setEnabled(false);
        }

        orderProductTableModelDialog.delete(selectedProduct);
        // Neu row xoa la row cuoi thi lui cursor ve
        // Neu row xoa la row khac cuoi thi tien cursor ve truoc
        selectedRowIndex = (selectedRowIndex == tbProduct.getRowCount() ? tbProduct.getRowCount() - 1 : selectedRowIndex++);
        scrollToRow(selectedRowIndex);
        updateItemsLabel();
        // Check neu product ko empty va so row khac voi so row ban dau thi moi bat trackchange
        if (!checkProductEmpty() && orderProductTableModelDialog.getRowCount() != backupRowCount) {
            setTrackChanges(true);
        }
    }

    // Ham goi khi bam nut Save
    private void updateAction() {
        // Check da co product chua va product da chon het chua
        if (checkProductEmpty()) {
            SwingUtils.showInfoDialog("Please choose product name !");
            return;
        }

        if (order.getOrdID() == -1) { // Insert mode
            if (orderProductTableModelDialog.insert(order)) {
                SwingUtils.showInfoDialog(SwingUtils.INSERT_SUCCESS);
                dispose(); //Tat dialog sau khi da update db
            } else {
                SwingUtils.showInfoDialog(SwingUtils.INSERT_FAIL);
            }
        } else if (orderProductTableModelDialog.update(order)) { // Update mode
            SwingUtils.showInfoDialog(SwingUtils.UPDATE_SUCCESS);
            dispose(); //Tat dialog sau khi da update db
        } else {
            SwingUtils.showInfoDialog(SwingUtils.UPDATE_FAIL);
        }
    }

    private void cancelAction() {
        if (trackChanges) {
            if (SwingUtils.showConfirmDialog("Discard change(s) and quit ?") == JOptionPane.YES_OPTION) {
                dispose();
            }
        } else {
            dispose();
        }
    }

    private void fetchAction() {
        selectedRowIndex = tbProduct.getSelectedRow();
        if (selectedRowIndex >= 0) {
            int idx = tbProduct.convertRowIndexToModel(selectedRowIndex);
            selectedProduct = orderProductTableModelDialog.getOrderProductFromIndex(idx);
            orderProductTableModelDialog.setSelectingIndex(idx);
        } else {
            selectedProduct = null;
            orderProductTableModelDialog.setSelectingIndex(-1);
        }
    }

    private void resetAction(boolean mustInfo) {
        // Load lai vung customer info
        cbStatus.setSelectedItem(orderStatusComboBoxModel.getStatusFromValue(backup.getOrdStatus()));
        cbCustomer.setSelectedItem(orderCustomerComboBoxModel.getCustomerFromID(backup.getCusID()));

        // Phai load lai discount cua backup ma ko lay discount cua customer de phong truong hop customer thay doi discount sau order
        order.setCusDiscount(backup.getCusDiscount());

        // Load lai table product
        orderProductTableModelDialog.load(order.getOrdID());
        // Load lai may cai label
        updateItemsLabel();
        updateDiscountLabel();

        if (mustInfo) {
            SwingUtils.showInfoDialog(SwingUtils.DB_RESET);
        }
        setTrackChanges(false);
    }

    private void updateItemsLabel() {
        int sum = 0;
        if (orderProductTableModelDialog.getRowCount() > 0) {
            for (int i = 0; i < orderProductTableModelDialog.getRowCount(); i++) {
                sum += (int) orderProductTableModelDialog.getValueAt(i, COL_PROQTY);
            }
        }
        lbItems.setText(sum > 0 ? String.format("%02d", sum) : "0");
        updateTotalLabel();
    }

    private void updateDiscountLabel() {
        NumberFormat format = NumberFormat.getPercentInstance();
        lbDiscount.setText(format.format(order.getCusDiscount()));
        updateTotalLabel();
    }

    private void updateTotalLabel() {
        float sum = 0;
        if (orderProductTableModelDialog.getRowCount() > 0) {
            for (int i = 0; i < orderProductTableModelDialog.getRowCount(); i++) {
                sum += (float) orderProductTableModelDialog.getValueAt(i, COL_PROPRICE2) * (int) orderProductTableModelDialog.getValueAt(i, COL_PROQTY);
            }
            String dis = lbDiscount.getText().split("%")[0];
            float discount = Float.parseFloat(dis) / 100;
            sum = sum * (1 - discount);
        }
        lbTotal.setText(String.format("%,.0f Đ", (float) sum));
    }

    /**
     * Kiem tra da chon product name day du het chua
     *
     * @return true neu da chon product name day du
     */
    private boolean checkProductEmpty() {
        List<OrderProduct> tmp = new ArrayList();
        orderProductTableModelDialog.getList().stream().filter(op -> op.getProName().equals(OrderProduct.DEFAULT_PRONAME)).forEach(op -> tmp.add(op));
        return tmp.size() > 0; //Da chon product day du
    }

    /**
     * Kiem tra so luong product co con ton kho hay khong
     *
     * @param proName
     * @return true neu het hang
     */
    private boolean checkStockZero(String proName) {
        return orderProductComboBoxModel.getOrderProductFromName(proName).getProStock() == 0;
    }

    /**
     * Kiem tra duplicate product trong order
     *
     * @param proName
     * @return true if duplicated
     */
    private boolean checkDuplicate(String proName) {
        List<OrderProduct> tmp = new ArrayList();
        orderProductTableModelDialog.getList().stream().filter(op -> op.getProName().equals(proName)).forEach(op -> tmp.add(op));
        return tmp.size() > 1; //Mang co 2 phan tu tuc la duplicate
    }

    public void setTrackChanges(boolean trackChanges) {
        this.trackChanges = trackChanges;
        btSave.setEnabled(trackChanges);
    }

    private void clearFilter() {
        list.getSelectionModel().clearSelection();
        tfNameFilter.setText(null);
    }

    private void scrollToRow(int row) {
        tbProduct.getSelectionModel().setSelectionInterval(row, row);
        tbProduct.scrollRectToVisible(new Rectangle(tbProduct.getCellRect(row, 0, true)));
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() == cbCustomer) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                OrderCustomer oc = (OrderCustomer) e.getItem();
                order.setCusID(oc.getCusID());
                order.setCusName(oc.getCusName());
                order.setCusDiscount(oc.getCusDiscount());
                updateDiscountLabel();
                if (!checkProductEmpty()) {
                    setTrackChanges(true);
                }
            }
        }

        if (e.getSource() == cbStatus) {
            OrderStatus os = (OrderStatus) e.getItem();
            order.setOrdStatusID(os.getSttID());
            order.setOrdStatus(os.getSttName());
            if (!checkProductEmpty()) {
                setTrackChanges(true);
            }
        }
    }
}
