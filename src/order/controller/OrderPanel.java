package order.controller;

import com.toedter.calendar.JDateChooser;
import database.DBProvider;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Rectangle;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.InputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.TableRowSorter;
import main.controller.LoginFrame;
import main.model.UserFunction;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import order.model.Order;
import order.model.OrderStatus;
import salesoff.controller.SalesOffDialog;
import utility.CurrencyCellRenderer;
import utility.PercentCellRenderer;
import utility.SwingUtils;

/**
 *
 * @author Hoang
 */
public class OrderPanel extends javax.swing.JPanel {

    private JDateChooser dcFilter;

    // Khai bao 2 cai table model
    private OrderTableModel orderTableModel;
    private OrderProductTableModel orderProductTableModel;
    private OrderStatusComboBoxModel orderStatusComboBoxModel;
    private OrderStatusComboBoxRenderer orderStatusComboBoxRenderer;
    private TableRowSorter<OrderTableModel> sorter;

    // Order dang duoc chon trong table order list
    private Order selectedOrder;
    private int selectedRowIndex;
    private OrderStatus filterStatus;

    public static final int COL_ORDID = 0;
    public static final int COL_USERNAME = 1;
    public static final int COL_CUSNAME = 2;
    public static final int COL_ORDDATE = 3;
    public static final int COL_STATUS = 4;
    public static final int COL_DISCOUNT = 5;
    public static final int COL_ORDVALUE = 6;

    public static final int COL_PROID = 0;
    public static final int COL_PRONAME = 1;
    public static final int COL_PROQTY = 2;
    public static final int COL_PROPRICE1 = 3;
    public static final int COL_SALEAMOUNT = 4;
    public static final int COL_PROPRICE2 = 5;

//<editor-fold defaultstate="collapsed" desc="constructor">
    public OrderPanel() {
        initComponents();

        // Set date picker len giao dien
        dcFilter = new JDateChooser();
        dcFilter.setBounds(0, 0, 130, 20);
        dcFilter.setDateFormatString("dd/MM/yyyy");
        dcFilter.setDate(null);
        pnDateChooser.add(dcFilter);

        //Disable button khi moi khoi dong len
        setButtonEnabled(false);

        // Selecting order in the table
        selectedOrder = new Order();

        // Set data cho combobox level filter
        orderStatusComboBoxModel = new OrderStatusComboBoxModel();
        filterStatus = new OrderStatus(0, "All", "Order");
        orderStatusComboBoxModel.addElement(filterStatus);
        orderStatusComboBoxRenderer = new OrderStatusComboBoxRenderer();
        cbStatusFilter.setModel(orderStatusComboBoxModel);
        cbStatusFilter.setRenderer(orderStatusComboBoxRenderer);

        // Set data cho 2 table
        orderTableModel = new OrderTableModel();
        orderProductTableModel = new OrderProductTableModel();
        tbOrderList.setModel(orderTableModel);
        tbProductList.setModel(orderProductTableModel);

        // Set sorter cho table
        sorter = new TableRowSorter<>(orderTableModel);
        tbOrderList.setRowSorter(sorter);

        // Select mac dinh cho level filter
        cbStatusFilter.setSelectedIndex(cbStatusFilter.getItemCount() - 1);

        // Set auto define column from model to false to stop create column again
        tbOrderList.setAutoCreateColumnsFromModel(false);
        tbProductList.setAutoCreateColumnsFromModel(false);

        // Set height cho table header
        tbOrderList.getTableHeader().setPreferredSize(new Dimension(300, 30));
        tbProductList.getTableHeader().setPreferredSize(new Dimension(300, 30));

        // Col order ID
        tbOrderList.getColumnModel().getColumn(COL_ORDID).setMinWidth(40);
        tbOrderList.getColumnModel().getColumn(COL_ORDID).setMaxWidth(40);

        // Col order value
        tbOrderList.getColumnModel().getColumn(COL_ORDVALUE).setMinWidth(50);
        tbOrderList.getColumnModel().getColumn(COL_ORDVALUE).setCellRenderer(new CurrencyCellRenderer());

        // Col user name
        tbOrderList.getColumnModel().getColumn(COL_USERNAME).setMinWidth(50);

        // Col cus name
        tbOrderList.getColumnModel().getColumn(COL_CUSNAME).setMinWidth(50);

        // Col order date
        tbOrderList.getColumnModel().getColumn(COL_ORDDATE).setMinWidth(120);
        tbOrderList.getColumnModel().getColumn(COL_ORDDATE).setMaxWidth(120);

        // Col discount
        tbOrderList.getColumnModel().getColumn(COL_DISCOUNT).setMinWidth(120);
        tbOrderList.getColumnModel().getColumn(COL_DISCOUNT).setMaxWidth(120);
        tbOrderList.getColumnModel().getColumn(COL_DISCOUNT).setCellRenderer(new PercentCellRenderer());

        // Col status
        tbOrderList.getColumnModel().getColumn(COL_STATUS).setMinWidth(40);

        // Col pro ID
        tbProductList.getColumnModel().getColumn(COL_PROID).setMinWidth(40);
        tbProductList.getColumnModel().getColumn(COL_PROID).setMaxWidth(40);

        // Col pro name
        tbProductList.getColumnModel().getColumn(COL_PRONAME).setMinWidth(150);

        // Col quantity
        tbProductList.getColumnModel().getColumn(COL_PROQTY).setMinWidth(120);
        tbProductList.getColumnModel().getColumn(COL_PROQTY).setMaxWidth(120);

        // Col price 1
        tbProductList.getColumnModel().getColumn(COL_PROPRICE1).setMinWidth(125);
        tbProductList.getColumnModel().getColumn(COL_PROPRICE1).setCellRenderer(new CurrencyCellRenderer());

        // Col salesoff
        tbProductList.getColumnModel().getColumn(COL_SALEAMOUNT).setMinWidth(125);
        tbProductList.getColumnModel().getColumn(COL_SALEAMOUNT).setCellRenderer(new PercentCellRenderer());

        // Col price 2
        tbProductList.getColumnModel().getColumn(COL_PROPRICE2).setMinWidth(125);
        tbProductList.getColumnModel().getColumn(COL_PROPRICE2).setCellRenderer(new CurrencyCellRenderer());

        // Bat su kien select row tren table sales off
        tbOrderList.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
            DefaultListSelectionModel model = (DefaultListSelectionModel) e.getSource();
            if (!model.isSelectionEmpty()) {
                fetchAction();
                if (LoginFrame.checkPermission(new UserFunction(UserFunction.FG_ORDER, UserFunction.FN_UPDATE))) {
                    setButtonEnabled(true);
                    // Khong cho xoa order da done
                    if (((String) tbOrderList.getValueAt(tbOrderList.getSelectedRow(), COL_STATUS)).equalsIgnoreCase("Done")) {
                        btRemove.setEnabled(false);
                    }
                }
            } else {
                setButtonEnabled(false);
            }
        });

        //<editor-fold defaultstate="collapsed" desc="Bat su kien cho vung filter">
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
        tfUserFilter.getDocument().addDocumentListener(new DocumentListener() {
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
        tfCusFilter.getDocument().addDocumentListener(
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
        dcFilter.getDateEditor().addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                doFilter();
            }
        });
        cbStatusFilter.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                doFilter();
            }
        });
        cbValueFilter.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                doFilter();
            }
        });
//</editor-fold>

        // Check permission order
        if (!LoginFrame.checkPermission(new UserFunction(UserFunction.FG_ORDER, UserFunction.FN_UPDATE))) {
            btAdd.setEnabled(false);
            btUpdate.setEnabled(false);
            btRemove.setEnabled(false);
        }

        // Check permission sales off
        if (!LoginFrame.checkPermission(new UserFunction(UserFunction.FG_SALESOFF, UserFunction.FN_VIEW))) {
            btSalesOff.setEnabled(false);
        }
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

        pnFilter = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        tfIdFilter = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        tfUserFilter = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        tfCusFilter = new javax.swing.JTextField();
        lbOrderDate = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        cbStatusFilter = new javax.swing.JComboBox<>();
        pnDateChooser = new javax.swing.JPanel();
        btClear = new javax.swing.JButton();
        lbOrderDate1 = new javax.swing.JLabel();
        cbValueFilter = new javax.swing.JComboBox<>();
        btRemove = new javax.swing.JButton();
        btUpdate = new javax.swing.JButton();
        btAdd = new javax.swing.JButton();
        btRefresh = new javax.swing.JButton();
        pnTitle = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        btSalesOff = new javax.swing.JButton();
        jSplitPane1 = new javax.swing.JSplitPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbProductList = new javax.swing.JTable();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbOrderList = new javax.swing.JTable();

        setPreferredSize(new java.awt.Dimension(790, 640));

        pnFilter.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Filter"));
        pnFilter.setPreferredSize(new java.awt.Dimension(770, 66));

        jLabel2.setText("ID:");

        jLabel3.setText("Sell. User:");

        jLabel4.setText("Cus. Name:");
        jLabel4.setPreferredSize(new java.awt.Dimension(80, 16));

        tfCusFilter.setPreferredSize(new java.awt.Dimension(160, 26));

        lbOrderDate.setText("Order Date:");

        jLabel6.setText("Status:");
        jLabel6.setPreferredSize(new java.awt.Dimension(80, 16));

        cbStatusFilter.setPreferredSize(new java.awt.Dimension(160, 27));

        pnDateChooser.setPreferredSize(new java.awt.Dimension(130, 20));

        javax.swing.GroupLayout pnDateChooserLayout = new javax.swing.GroupLayout(pnDateChooser);
        pnDateChooser.setLayout(pnDateChooserLayout);
        pnDateChooserLayout.setHorizontalGroup(
            pnDateChooserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 130, Short.MAX_VALUE)
        );
        pnDateChooserLayout.setVerticalGroup(
            pnDateChooserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );

        btClear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/order/Delete2.png"))); // NOI18N
        btClear.setBorderPainted(false);
        btClear.setContentAreaFilled(false);
        btClear.setFocusPainted(false);
        btClear.setPreferredSize(new java.awt.Dimension(20, 28));
        btClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btClearActionPerformed(evt);
            }
        });

        lbOrderDate1.setText("Order Value:");

        cbValueFilter.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "~1.000.000", "1.000.000-10.000.000", "10.000.000-20.000.000", "20.000.000~", "All" }));
        cbValueFilter.setSelectedItem("All");

        javax.swing.GroupLayout pnFilterLayout = new javax.swing.GroupLayout(pnFilter);
        pnFilter.setLayout(pnFilterLayout);
        pnFilterLayout.setHorizontalGroup(
            pnFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnFilterLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lbOrderDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pnDateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tfIdFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, Short.MAX_VALUE)
                .addGroup(pnFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lbOrderDate1)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cbValueFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tfUserFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, Short.MAX_VALUE)
                .addGroup(pnFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnFilterLayout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tfCusFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnFilterLayout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbStatusFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btClear, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12))
        );
        pnFilterLayout.setVerticalGroup(
            pnFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnFilterLayout.createSequentialGroup()
                .addGroup(pnFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnFilterLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(pnFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(tfIdFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3)
                            .addComponent(tfUserFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tfCusFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(pnDateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbOrderDate)
                            .addGroup(pnFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(cbStatusFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lbOrderDate1)
                                .addComponent(cbValueFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(pnFilterLayout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(btClear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        btRemove.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        btRemove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/order/Delete.png"))); // NOI18N
        btRemove.setText("Remove");
        btRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btRemoveActionPerformed(evt);
            }
        });

        btUpdate.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        btUpdate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/order/Save.png"))); // NOI18N
        btUpdate.setText("Update...");
        btUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btUpdateActionPerformed(evt);
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
        pnTitle.setPreferredSize(new java.awt.Dimension(790, 65));

        jLabel1.setFont(new java.awt.Font("Lucida Grande", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 255, 255));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/main/Order.png"))); // NOI18N
        jLabel1.setText("<html><u><i><font color='red'>O</font>rder <font color='red'>M</font>anagement</i></u></html>");

        btSalesOff.setFont(new java.awt.Font("Lucida Grande", 3, 14)); // NOI18N
        btSalesOff.setForeground(new java.awt.Color(255, 153, 0));
        btSalesOff.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/order/SalesOff2.png"))); // NOI18N
        btSalesOff.setText("<html><u>SalesOff...</u></html>");
        btSalesOff.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btSalesOffActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnTitleLayout = new javax.swing.GroupLayout(pnTitle);
        pnTitle.setLayout(pnTitleLayout);
        pnTitleLayout.setHorizontalGroup(
            pnTitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnTitleLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btSalesOff, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        pnTitleLayout.setVerticalGroup(
            pnTitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnTitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 61, Short.MAX_VALUE)
                .addComponent(btSalesOff, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jSplitPane1.setDividerLocation(200);
        jSplitPane1.setDividerSize(10);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane1.setContinuousLayout(true);
        jSplitPane1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jSplitPane1.setOneTouchExpandable(true);

        jScrollPane2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Order Details"));
        jScrollPane2.setMinimumSize(new java.awt.Dimension(31, 150));
        jScrollPane2.setPreferredSize(new java.awt.Dimension(462, 200));

        tbProductList.setAutoCreateRowSorter(true);
        tbProductList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Product Name", "Quantity", "Sold Price"
            }
        ));
        tbProductList.setFillsViewportHeight(true);
        tbProductList.setRowHeight(25);
        tbProductList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tbProductList.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(tbProductList);

        jSplitPane1.setBottomComponent(jScrollPane2);

        jScrollPane1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Order List"));
        jScrollPane1.setMinimumSize(new java.awt.Dimension(31, 150));

        tbOrderList.setAutoCreateRowSorter(true);
        tbOrderList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Order Date", "Value", "Status", "Discount", "User Name", "Cus. Name"
            }
        ));
        tbOrderList.setFillsViewportHeight(true);
        tbOrderList.setRowHeight(25);
        tbOrderList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tbOrderList.getTableHeader().setReorderingAllowed(false);
        tbOrderList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbOrderListMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbOrderList);

        jSplitPane1.setTopComponent(jScrollPane1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btRemove, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(pnTitle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pnFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 790, Short.MAX_VALUE)
            .addComponent(jSplitPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnTitle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 429, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btRemove, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
//<editor-fold defaultstate="collapsed" desc="Bat su kien">
    private void btAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btAddActionPerformed
        new OrderDialog(null).setVisible(true);
        refreshAction(false);
        if (orderTableModel.getRowCount() > 0) {
            scrollToRow(selectedRowIndex);
        }
    }//GEN-LAST:event_btAddActionPerformed

    private void btSalesOffActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btSalesOffActionPerformed
        new SalesOffDialog().setVisible(true);
    }//GEN-LAST:event_btSalesOffActionPerformed

    private void btUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btUpdateActionPerformed
        new OrderDialog(selectedOrder).setVisible(true);
        refreshAction(false);
        scrollToRow(selectedRowIndex);
    }//GEN-LAST:event_btUpdateActionPerformed

    private void btRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btRemoveActionPerformed
        deleteAction();
    }//GEN-LAST:event_btRemoveActionPerformed

    private void btRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btRefreshActionPerformed
        refreshAction(true);
    }//GEN-LAST:event_btRefreshActionPerformed

    private void btClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btClearActionPerformed
        clearFilter();
    }//GEN-LAST:event_btClearActionPerformed

    private void tbOrderListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbOrderListMouseClicked
        if (evt.getButton() == 1 && evt.getClickCount() == 2) {
            JTable table = (JTable) evt.getSource();
            int row = table.rowAtPoint(evt.getPoint());
            if (row != -1) { // Khong double click trung row hoac khong co row nao
                viewReport((int) table.getValueAt(row, COL_ORDID));
            }
//            System.out.println("click: " + table.rowAtPoint(evt.getPoint()));
//            System.out.println("click: " + table.columnAtPoint(evt.getPoint()));
        }
    }//GEN-LAST:event_tbOrderListMouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btAdd;
    private javax.swing.JButton btClear;
    private javax.swing.JButton btRefresh;
    private javax.swing.JButton btRemove;
    private javax.swing.JButton btSalesOff;
    private javax.swing.JButton btUpdate;
    private javax.swing.JComboBox<order.model.OrderStatus> cbStatusFilter;
    private javax.swing.JComboBox<String> cbValueFilter;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JLabel lbOrderDate;
    private javax.swing.JLabel lbOrderDate1;
    private javax.swing.JPanel pnDateChooser;
    private javax.swing.JPanel pnFilter;
    private javax.swing.JPanel pnTitle;
    private javax.swing.JTable tbOrderList;
    private javax.swing.JTable tbProductList;
    private javax.swing.JTextField tfCusFilter;
    private javax.swing.JTextField tfIdFilter;
    private javax.swing.JTextField tfUserFilter;
    // End of variables declaration//GEN-END:variables
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="doFilter">
    private void doFilter() {
        RowFilter<OrderTableModel, Integer> rf = null;
        //Filter theo regex, neu parse bi loi thi khong filter
        try {
            List<RowFilter<OrderTableModel, Integer>> filters = new ArrayList<>();
            // Filter ID
            filters.add(RowFilter.regexFilter("^" + tfIdFilter.getText(), COL_ORDID));

            // Filter User Name khi khac rong
            if (!tfUserFilter.getText().isEmpty()) {
                RowFilter<OrderTableModel, Integer> userFilter = new RowFilter<OrderTableModel, Integer>() {
                    @Override
                    public boolean include(RowFilter.Entry<? extends OrderTableModel, ? extends Integer> entry) {
                        OrderTableModel model = entry.getModel();
                        Order o = model.getOrderAtIndex(entry.getIdentifier());
                        if (o.getUserName().toUpperCase().contains(tfUserFilter.getText().toUpperCase())) {
                            return true;
                        }
                        return false;
                    }
                };
                filters.add(userFilter);
            }

            // Filter Cus Name khi khac rong
            if (!tfCusFilter.getText().isEmpty()) {
                RowFilter<OrderTableModel, Integer> cusFilter = new RowFilter<OrderTableModel, Integer>() {
                    @Override
                    public boolean include(RowFilter.Entry<? extends OrderTableModel, ? extends Integer> entry) {
                        OrderTableModel model = entry.getModel();
                        Order o = model.getOrderAtIndex(entry.getIdentifier());
                        if (o.getCusName().toUpperCase().contains(tfCusFilter.getText().toUpperCase())) {
                            return true;
                        }
                        return false;
                    }
                };
                filters.add(cusFilter);
            }

            // Chi filter date khi date khac null
            if (dcFilter.getDate() != null) {
                // dcFilter khac voi date cua table o GIO, PHUT, GIAY nen phai dung Calendar de so sanh chi nam, thang, ngay.... oh vai~.
                // Magic here....
                RowFilter<OrderTableModel, Integer> dateFilter = new RowFilter<OrderTableModel, Integer>() {
                    @Override
                    public boolean include(RowFilter.Entry<? extends OrderTableModel, ? extends Integer> entry) {
                        OrderTableModel model = entry.getModel();
                        Order o = model.getOrderAtIndex(entry.getIdentifier());

                        Calendar origin = Calendar.getInstance();
                        origin.setTime(o.getOrdDate());

                        Calendar filter = Calendar.getInstance();
                        filter.setTime(dcFilter.getDate());

                        if (origin.get(Calendar.YEAR) == filter.get(Calendar.YEAR) && origin.get(Calendar.MONTH) == filter.get(Calendar.MONTH) && origin.get(Calendar.DATE) == filter.get(Calendar.DATE)) {
                            return true;
                        }

                        return false;
                    }
                };

                filters.add(dateFilter);
            }

            // Value filter
            RowFilter<OrderTableModel, Integer> valueFilter = new RowFilter<OrderTableModel, Integer>() {
                @Override
                public boolean include(RowFilter.Entry<? extends OrderTableModel, ? extends Integer> entry) {
                    OrderTableModel model = entry.getModel();
                    Order o = model.getOrderAtIndex(entry.getIdentifier());
                    switch (cbValueFilter.getSelectedIndex()) {
                        case 0: // ~1.000.000
                            return o.getOrdValue() <= 1000000;
                        case 1: // 1.000.000-10.000.000
                            return 1000000 <= o.getOrdValue() && o.getOrdValue() <= 10000000;
                        case 2: // 10.000.000-20.000.000
                            return 10000000 <= o.getOrdValue() && o.getOrdValue() <= 20000000;
                        case 3: // 20.000.000~
                            return 20000000 <= o.getOrdValue();
                        case 4:  // All
                            return true;
                    }
                    return false;
                }
            };

            filters.add(valueFilter);

            // Chi filter status khi status khac "All"
            String stt = ((OrderStatus) cbStatusFilter.getSelectedItem()).getSttName();
            if (!stt.equals("All")) {
                filters.add(RowFilter.regexFilter("^" + stt, COL_STATUS));
            }

            rf = RowFilter.andFilter(filters);
        } catch (java.util.regex.PatternSyntaxException e) {
            return;
        }
        sorter.setRowFilter(rf);
    }
//</editor-fold>

    private void clearFilter() {
        tfIdFilter.setText(null);
        dcFilter.setDate(null);
        tfUserFilter.setText(null);
        cbValueFilter.setSelectedIndex(cbValueFilter.getItemCount() - 1);
        tfCusFilter.setText(null);
        cbStatusFilter.setSelectedIndex(cbStatusFilter.getItemCount() - 1);
    }

    private void fetchAction() {
        selectedRowIndex = tbOrderList.getSelectedRow();
        selectedOrder = orderTableModel.getOrderAtIndex(tbOrderList.convertRowIndexToModel(selectedRowIndex));
        // Reload table product list voi Order moi chon
        orderProductTableModel.load(selectedOrder.getOrdID());
    }

    private void deleteAction() {
        if (SwingUtils.showConfirmDialog("Are you sure to delete this order ?") == JOptionPane.NO_OPTION) {
            return;
        }
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        boolean result = orderTableModel.delete(selectedOrder);
        setCursor(null);
        SwingUtils.showInfoDialog(result ? SwingUtils.DELETE_SUCCESS : SwingUtils.DELETE_FAIL);

        // Neu row xoa la row cuoi thi lui cursor ve
        // Neu row xoa la row khac cuoi thi tien cursor ve truoc
        selectedRowIndex = (selectedRowIndex == tbOrderList.getRowCount() ? tbOrderList.getRowCount() - 1 : selectedRowIndex++);
        scrollToRow(selectedRowIndex);

        // Them refresh do cai table order khong tu dong update sau khi delete nhu cac table khac
        refreshAction(false);
    }

    private void refreshAction(boolean mustInfo) {
        if (mustInfo) {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            orderTableModel.refresh();
            setCursor(null);
            SwingUtils.showInfoDialog(SwingUtils.DB_REFRESH);
        } else {
            orderTableModel.refresh();
        }
    }

    private void scrollToRow(int row) {
        tbOrderList.getSelectionModel().setSelectionInterval(row, row);
        tbOrderList.scrollRectToVisible(new Rectangle(tbOrderList.getCellRect(row, 0, true)));
    }

    private void setButtonEnabled(boolean enabled, JButton... exclude) {
        btUpdate.setEnabled(enabled);
        btRemove.setEnabled(enabled);

        // Ngoai tru may button nay luon luon enable
        if (exclude.length != 0) {
            Arrays.stream(exclude).forEach(b -> b.setEnabled(true));
        }
    }

    // Print report
    private void viewReport(int OrdID) {
        try {
            // Access xml file
//            String reportPath = getClass().getResource("bill.jrxml").getPath();
            InputStream in = getClass().getResourceAsStream("bill.jrxml");
            JasperDesign jasperDesign = JRXmlLoader.load(in);

            // Compile xml file to .jasper file
//            JasperReport jasperReport = JasperCompileManager.compileReport(reportPath);
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

            // Create connection
            DBProvider db = new DBProvider();
            db.setDbHost(LoginFrame.config.host);
            db.setDbPort(LoginFrame.config.port);
            db.setDbName(LoginFrame.config.DBName);
            db.setDbUsername(LoginFrame.config.name);
            db.setDbPassword(LoginFrame.config.password);
            db.start();
            Connection connection = db.getConnection();
            // Pass parameters
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("OrdIDPara", OrdID);
            // Create .jpprint file
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);
            // View report in the JasperViewer
            JasperViewer jv = new JasperViewer(jasperPrint, false);
            jv.setZoomRatio(0.75f);
            jv.setExtendedState(Frame.MAXIMIZED_BOTH);
            jv.setVisible(true);
            db.stop();
        } catch (JRException ex) {
            Logger.getLogger(OrderPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
