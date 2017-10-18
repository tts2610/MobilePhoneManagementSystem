/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service.controller.backup;

import service.controller.*;
import com.toedter.calendar.JDateChooser;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.TableRowSorter;
import main.controller.LoginFrame;
import main.model.UserFunction;
import service.model.Service;
import service.model.ServiceStatus;
import service.model.ServiceType;
import employee.model.IntegerCurrencyCellRenderer;
import employee.model.DateCellWorkingDateEditor;
import employee.model.EmployeeSwingUtils;
import javax.swing.JLabel;
import javax.swing.table.DefaultTableCellRenderer;
import utility.SwingUtils;
import utility.TableCellListener;

/**
 *
 * @author BonBon
 */
public class ServicePanel extends javax.swing.JPanel {

    private JDateChooser dcFilter;
    private JDateChooser dcFilter1;
    // Khai bao 2 cai table model
    private ServiceTableModel serviceTableModel;
    private ServiceDetailsTableModel serviceDetailsTableModel;
    private ServiceStatusComboBoxModel serviceStatusComboBoxModel;
    private ServiceStatusComboBoxRenderer serviceStatusComboBoxRenderer;
    private ServiceTypeComboBoxModel serviceTypeComboBoxModel;
    private ServiceTypeComboBoxRenderer serviceTypeComboBoxRenderer;
    private TableRowSorter<ServiceTableModel> sorter;

    // Order dang duoc chon trong table order list
    private Service selectedService;
    private int selectedRowIndex;
    private ServiceStatus filterStatus;
    private ServiceType filterType;

    private static final int COL_SERID = 0;
    private static final int COL_USERNAME = 1;
    private static final int COL_RECEIVEDATE = 2;
    private static final int COL_RETURNDATE = 3;
    private static final int COL_SERTYPENAME = 4;
    private static final int COL_STATUS = 5;

    private static final int COL_PROID = 0;
    private static final int COL_PRONAME = 1;
    private static final int COL_BRANAME = 2;
    private static final int COL_CONTENT = 3;
    private static final int COL_PROQTY = 4;
    private static final int COL_ODERID = 5;
    private static final int COL_COST = 6;
    //hidden
    private static final int COL_SERVICEID = 7;
    private static final int COL_BRAID = 8;

//<editor-fold defaultstate="collapsed" desc="constructor">
    public ServicePanel() {
        initComponents();

        // Set date picker len giao dien
        Calendar c = Calendar.getInstance();
        dcFilter = new JDateChooser();
        dcFilter.setBounds(0, 0, 120, 20);
        dcFilter.setDateFormatString("MMM dd, yyyy");
        dcFilter.setDate(null);
        c.set(2010, 0, 1);
        dcFilter.setMinSelectableDate(c.getTime());
        Calendar c1 = Calendar.getInstance();
        dcFilter.setMaxSelectableDate(c1.getTime());
        pnReceiveDate.add(dcFilter);
        dcFilter.getDateEditor().setEnabled(false);
        // Set date picker1 len giao dien
        dcFilter1 = new JDateChooser();
        dcFilter1.setBounds(0, 0, 120, 20);
        dcFilter1.setDateFormatString("MMM dd, yyyy");
        dcFilter1.setDate(null);
        dcFilter1.getJCalendar().setMinSelectableDate(c.getTime());
        c1.add(Calendar.DATE, +30);
        dcFilter1.getJCalendar().setMaxSelectableDate(c1.getTime());
        pnReturnDate.add(dcFilter1);
        dcFilter1.getDateEditor().setEnabled(false);
        //Disable button khi moi khoi dong len
        setButtonEnabled(false);

        // Selecting service in the table
        selectedService = new Service();

        // Set data cho combobox status filter
        serviceStatusComboBoxModel = new ServiceStatusComboBoxModel();
        filterStatus = new ServiceStatus(0, "All", "Service");
        serviceStatusComboBoxModel.addElement(filterStatus);
        cbStatusFilter.setModel(serviceStatusComboBoxModel);
        serviceStatusComboBoxRenderer = new ServiceStatusComboBoxRenderer();
        cbStatusFilter.setRenderer(serviceStatusComboBoxRenderer);

        // Set data cho combobox service type filter
        serviceTypeComboBoxModel = new ServiceTypeComboBoxModel();
        filterType = new ServiceType(0, "All");
        serviceTypeComboBoxModel.addElement(filterType);
        cbTypeFilter.setModel(serviceTypeComboBoxModel);
        serviceTypeComboBoxRenderer = new ServiceTypeComboBoxRenderer();
        cbTypeFilter.setRenderer(serviceTypeComboBoxRenderer);

        // Set data cho table
        serviceTableModel = new ServiceTableModel();
        serviceDetailsTableModel = new ServiceDetailsTableModel();
        tbServiceList.setModel(serviceTableModel);
        tbDetailsList.setModel(serviceDetailsTableModel);

        // Set sorter cho table
        sorter = new TableRowSorter<>(serviceTableModel);
        tbServiceList.setRowSorter(sorter);

        // Select mac dinh cho level filter
        cbStatusFilter.setSelectedIndex(cbStatusFilter.getItemCount() - 1);
        cbTypeFilter.setSelectedIndex(cbTypeFilter.getItemCount() - 1);
        // Set auto define column from model to false to stop create column again
        tbServiceList.setAutoCreateColumnsFromModel(false);
        tbDetailsList.setAutoCreateColumnsFromModel(false);

        // Set height cho table header
        tbServiceList.getTableHeader().setPreferredSize(new Dimension(300, 30));
        tbDetailsList.getTableHeader().setPreferredSize(new Dimension(300, 30));
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tbDetailsList.getColumnModel().getColumn(COL_ODERID).setCellRenderer(centerRenderer);
        tbServiceList.getColumnModel().getColumn(COL_SERID).setCellRenderer(centerRenderer);
        tbDetailsList.getColumnModel().getColumn(COL_PROID).setCellRenderer(centerRenderer);
        tbDetailsList.getColumnModel().getColumn(COL_PROQTY).setCellRenderer(centerRenderer);
// Col order ID
        tbServiceList.getColumnModel().getColumn(COL_SERID).setMinWidth(30);
        tbServiceList.getColumnModel().getColumn(COL_SERID).setMaxWidth(50);
        tbServiceList.getColumnModel().getColumn(COL_SERID).sizeWidthToFit();
        // Col user name
        tbServiceList.getColumnModel().getColumn(COL_USERNAME).setMinWidth(120);
//        tbServiceList.getColumnModel().getColumn(COL_USERNAME).setMaxWidth(120);

        // Col receivedate
        tbServiceList.getColumnModel().getColumn(COL_RECEIVEDATE).setCellEditor(new DateCellWorkingDateEditor());
        tbServiceList.getColumnModel().getColumn(COL_RECEIVEDATE).setMinWidth(120);
//        tbServiceList.getColumnModel().getColumn(COL_RECEIVEDATE).setMaxWidth(120);
        // Col receive date
        tbServiceList.getColumnModel().getColumn(COL_RETURNDATE).setCellEditor(new DateCellWorkingDateEditor());
        tbServiceList.getColumnModel().getColumn(COL_RETURNDATE).setMinWidth(120);
//        tbServiceList.getColumnModel().getColumn(COL_RETURNDATE).setMaxWidth(120);
        // Col type
        tbServiceList.getColumnModel().getColumn(COL_SERTYPENAME).setMinWidth(90);
//        tbServiceList.getColumnModel().getColumn(COL_SERTYPENAME).setMaxWidth(90);
        // Col status
        tbServiceList.getColumnModel().getColumn(COL_STATUS).setMinWidth(90);

////    table detailslist
        tbDetailsList.getColumnModel().getColumn(COL_PROID).setMinWidth(50);
        tbDetailsList.getColumnModel().getColumn(COL_PROID).setMaxWidth(60);

        // Col pro name
        tbDetailsList.getColumnModel().getColumn(COL_PRONAME).setMinWidth(250);

        // Col braname
        tbDetailsList.getColumnModel().getColumn(COL_BRANAME).setMinWidth(100);

        // Col content
        tbDetailsList.getColumnModel().getColumn(COL_CONTENT).setMinWidth(150);
//        tbProduct.getColumnModel().getColumn(COL_CONTENT).setCellRenderer(new DefaultTableCellRenderer());

        // Col quantity
        tbDetailsList.getColumnModel().getColumn(COL_PROQTY).setMinWidth(50);
//        tbDetailsList.getColumnModel().getColumn(COL_PROQTY).setCellEditor(new SpinnerCellEditor(1, 10));

        // Col oderid
        tbDetailsList.getColumnModel().getColumn(COL_ODERID).setMinWidth(60);
        tbDetailsList.getColumnModel().getColumn(COL_ODERID).setMaxWidth(70);
//        tbDetailsList.getColumnModel().getColumn(COL_ODERID).setCellEditor(new SpinnerCellEditor(1, 10000));
        tbDetailsList.getColumnModel().getColumn(COL_COST).setCellRenderer(new IntegerCurrencyCellRenderer());
        tbDetailsList.getColumnModel().getColumn(COL_COST).setMinWidth(100);
        // Col SER ID (HIDDEN)
        tbDetailsList.getColumnModel().getColumn(COL_SERVICEID).setMinWidth(0);
        tbDetailsList.getColumnModel().getColumn(COL_SERVICEID).setMaxWidth(0);
        // Col bra ID (HIDDEN)
        tbDetailsList.getColumnModel().getColumn(COL_BRAID).setMinWidth(0);
        tbDetailsList.getColumnModel().getColumn(COL_BRAID).setMaxWidth(0);
        // Bat su kien select row tren table service list
        tbServiceList.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
            DefaultListSelectionModel model = (DefaultListSelectionModel) e.getSource();

            if (!model.isSelectionEmpty()) {
                fetchAction();
                setButtonEnabled(true);
                if (!this.selectedService.getSerStatus().equals("Done")) {
                    setButtonRemoveEnabled(true);
                } else {
                    setButtonRemoveEnabled(false);
                }
                if (!LoginFrame.checkPermission(new UserFunction(UserFunction.FG_SERVICE, UserFunction.FN_UPDATE))) {
                    btAdd.setEnabled(false);
                    btUpdate.setEnabled(false);
                    btRemove.setEnabled(false);
                }
            } else {
                setButtonEnabled(false);
            }

        });

//<editor-fold defaultstate="collapsed" desc="Set cell listener cho updating">
        TableCellListener tcl = new TableCellListener(tbServiceList, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TableCellListener tcl = (TableCellListener) e.getSource();

                switch (tcl.getColumn()) {

                }
            }
        });
//</editor-fold>

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

        dcFilter.getDateEditor().addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                doFilter();
            }
        });
        dcFilter1.getDateEditor().addPropertyChangeListener(new PropertyChangeListener() {
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
        cbTypeFilter.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                doFilter();
            }
        });
//</editor-fold>
//check 
        if (!LoginFrame.checkPermission(new UserFunction(UserFunction.FG_SERVICE, UserFunction.FN_UPDATE))) {
            btAdd.setEnabled(false);
            btUpdate.setEnabled(false);
            btRemove.setEnabled(false);
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
        lbOrderDate = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        cbStatusFilter = new javax.swing.JComboBox<>();
        pnReceiveDate = new javax.swing.JPanel();
        btClear = new javax.swing.JButton();
        lbOrderDate1 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        cbTypeFilter = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        tfUserFilter = new javax.swing.JTextField();
        pnReturnDate = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbServiceList = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbDetailsList = new javax.swing.JTable();
        btRemove = new javax.swing.JButton();
        btUpdate = new javax.swing.JButton();
        btAdd = new javax.swing.JButton();
        btRefresh = new javax.swing.JButton();
        pnTitle = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();

        setPreferredSize(new java.awt.Dimension(790, 640));

        pnFilter.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Filter"));
        pnFilter.setPreferredSize(new java.awt.Dimension(770, 66));

        jLabel2.setText("ID:");

        lbOrderDate.setText("Receive Date");

        jLabel6.setText("Status");
        jLabel6.setPreferredSize(new java.awt.Dimension(80, 16));

        cbStatusFilter.setPreferredSize(new java.awt.Dimension(160, 27));

        pnReceiveDate.setPreferredSize(new java.awt.Dimension(130, 20));

        javax.swing.GroupLayout pnReceiveDateLayout = new javax.swing.GroupLayout(pnReceiveDate);
        pnReceiveDate.setLayout(pnReceiveDateLayout);
        pnReceiveDateLayout.setHorizontalGroup(
            pnReceiveDateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 130, Short.MAX_VALUE)
        );
        pnReceiveDateLayout.setVerticalGroup(
            pnReceiveDateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        btClear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/order/Refresh2.png"))); // NOI18N
        btClear.setBorderPainted(false);
        btClear.setContentAreaFilled(false);
        btClear.setFocusPainted(false);
        btClear.setPreferredSize(new java.awt.Dimension(20, 28));
        btClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btClearActionPerformed(evt);
            }
        });

        lbOrderDate1.setText("Return Date");

        jLabel7.setText("Type");
        jLabel7.setPreferredSize(new java.awt.Dimension(80, 16));

        cbTypeFilter.setPreferredSize(new java.awt.Dimension(160, 27));

        jLabel3.setText("User Name");

        pnReturnDate.setPreferredSize(new java.awt.Dimension(130, 20));

        javax.swing.GroupLayout pnReturnDateLayout = new javax.swing.GroupLayout(pnReturnDate);
        pnReturnDate.setLayout(pnReturnDateLayout);
        pnReturnDateLayout.setHorizontalGroup(
            pnReturnDateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 130, Short.MAX_VALUE)
        );
        pnReturnDateLayout.setVerticalGroup(
            pnReturnDateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout pnFilterLayout = new javax.swing.GroupLayout(pnFilter);
        pnFilter.setLayout(pnFilterLayout);
        pnFilterLayout.setHorizontalGroup(
            pnFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnFilterLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(pnFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(tfIdFilter, javax.swing.GroupLayout.DEFAULT_SIZE, 143, Short.MAX_VALUE)
                    .addComponent(tfUserFilter))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 44, Short.MAX_VALUE)
                .addGroup(pnFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lbOrderDate, javax.swing.GroupLayout.DEFAULT_SIZE, 77, Short.MAX_VALUE)
                    .addComponent(lbOrderDate1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(5, 5, 5)
                .addGroup(pnFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnReceiveDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pnReturnDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 45, Short.MAX_VALUE)
                .addGroup(pnFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cbTypeFilter, 0, 140, Short.MAX_VALUE)
                    .addComponent(cbStatusFilter, 0, 1, Short.MAX_VALUE))
                .addGap(30, 30, 30)
                .addComponent(btClear, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12))
        );
        pnFilterLayout.setVerticalGroup(
            pnFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnFilterLayout.createSequentialGroup()
                .addGroup(pnFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(pnFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2)
                        .addComponent(tfIdFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cbTypeFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(pnReceiveDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lbOrderDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cbStatusFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(tfUserFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel3)
                        .addComponent(lbOrderDate1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(pnReturnDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
            .addGroup(pnFilterLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btClear, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jScrollPane1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Service List"));

        tbServiceList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "User Name", "Receive Date", "Return Date", "Service Type", "Status"
            }
        ));
        tbServiceList.setFillsViewportHeight(true);
        tbServiceList.setRowHeight(25);
        tbServiceList.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(tbServiceList);

        jScrollPane2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Service Details"));

        tbDetailsList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Product Name", "Branch", "Content", "Quantity", "Oder ID"
            }
        ));
        tbDetailsList.setFillsViewportHeight(true);
        tbDetailsList.setRowHeight(25);
        tbDetailsList.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(tbDetailsList);

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
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/main/Service.png"))); // NOI18N
        jLabel1.setText("<html><u><i><font color='red'>S</font>ervice <font color='red'>M</font>anagement</i></u></html>");

        javax.swing.GroupLayout pnTitleLayout = new javax.swing.GroupLayout(pnTitle);
        pnTitle.setLayout(pnTitleLayout);
        pnTitleLayout.setHorizontalGroup(
            pnTitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnTitleLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 488, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnTitleLayout.setVerticalGroup(
            pnTitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(86, Short.MAX_VALUE)
                .addComponent(btAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btRemove, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(86, Short.MAX_VALUE))
            .addComponent(pnTitle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pnFilter, javax.swing.GroupLayout.DEFAULT_SIZE, 790, Short.MAX_VALUE)
            .addComponent(jScrollPane1)
            .addComponent(jScrollPane2)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnTitle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btRemove, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
//<editor-fold defaultstate="collapsed" desc="Bat su kien">
    private void btAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btAddActionPerformed
        new ServiceDialog(null).setVisible(true);
        refreshAction(false);
        scrollToRow(tbServiceList.getRowCount() - 1);
    }//GEN-LAST:event_btAddActionPerformed

    private void btUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btUpdateActionPerformed
        new ServiceDialog(selectedService).setVisible(true);
        refreshAction(false);
    }//GEN-LAST:event_btUpdateActionPerformed

    private void btRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btRemoveActionPerformed
        if (EmployeeSwingUtils.showConfirmDialog("Are you sure to delete ?") == JOptionPane.NO_OPTION) {
            return;
        } else {
            deleteAction();
        }
    }//GEN-LAST:event_btRemoveActionPerformed

    private void btRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btRefreshActionPerformed
        refreshAction(true);
    }//GEN-LAST:event_btRefreshActionPerformed

    private void btClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btClearActionPerformed
        clearFilter();
    }//GEN-LAST:event_btClearActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btAdd;
    private javax.swing.JButton btClear;
    private javax.swing.JButton btRefresh;
    private javax.swing.JButton btRemove;
    private javax.swing.JButton btUpdate;
    private javax.swing.JComboBox<service.model.ServiceStatus> cbStatusFilter;
    private javax.swing.JComboBox<service.model.ServiceType> cbTypeFilter;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lbOrderDate;
    private javax.swing.JLabel lbOrderDate1;
    private javax.swing.JPanel pnFilter;
    private javax.swing.JPanel pnReceiveDate;
    private javax.swing.JPanel pnReturnDate;
    private javax.swing.JPanel pnTitle;
    private javax.swing.JTable tbDetailsList;
    private javax.swing.JTable tbServiceList;
    private javax.swing.JTextField tfIdFilter;
    private javax.swing.JTextField tfUserFilter;
    // End of variables declaration//GEN-END:variables
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="doFilter">
    private void doFilter() {
        RowFilter<ServiceTableModel, Object> rf = null;
        //Filter theo regex, neu parse bi loi thi khong filter
        try {
            List<RowFilter<ServiceTableModel, Object>> filters = new ArrayList<>();
            filters.add(RowFilter.regexFilter("^" + tfIdFilter.getText(), 0));
            filters.add(RowFilter.regexFilter("^" + tfUserFilter.getText(), 1));

            // Chi filter date khi date khac null
            if (dcFilter.getDate() != null) {
                // dcFilter khac voi date cua table o GIO, PHUT, GIAY nen phai dung Calendar de so sanh chi nam, thang, ngay.... oh vai~.
                // Magic here....
                RowFilter<ServiceTableModel, Object> dateFilter = new RowFilter<ServiceTableModel, Object>() {
                    @Override
                    public boolean include(RowFilter.Entry<? extends ServiceTableModel, ? extends Object> entry) {
                        ServiceTableModel model = entry.getModel();
                        Service o = model.getServiceAtIndex((Integer) entry.getIdentifier());

                        Calendar origin = Calendar.getInstance();
                        origin.setTime(o.getReceiveDate());

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
            // Chi filter date khi date khac null
            if (dcFilter1.getDate() != null) {
                // dcFilter khac voi date cua table o GIO, PHUT, GIAY nen phai dung Calendar de so sanh chi nam, thang, ngay.... oh vai~.
                // Magic here....
                RowFilter<ServiceTableModel, Object> dateFilter1 = new RowFilter<ServiceTableModel, Object>() {
                    @Override
                    public boolean include(RowFilter.Entry<? extends ServiceTableModel, ? extends Object> entry) {
                        ServiceTableModel model = entry.getModel();
                        Service o = model.getServiceAtIndex((Integer) entry.getIdentifier());

                        Calendar origin1 = Calendar.getInstance();
                        origin1.setTime(o.getReturnDate());

                        Calendar filter1 = Calendar.getInstance();
                        filter1.setTime(dcFilter1.getDate());

                        if (origin1.get(Calendar.YEAR) == filter1.get(Calendar.YEAR) && origin1.get(Calendar.MONTH) == filter1.get(Calendar.MONTH) && origin1.get(Calendar.DATE) == filter1.get(Calendar.DATE)) {
                            return true;
                        }

                        return false;
                    }
                };

                filters.add(dateFilter1);
            }
            //filter type khi khac All
            String stt1 = ((ServiceType) cbTypeFilter.getSelectedItem()).getSerTypeName();
            if (!stt1.equals("All")) {
                filters.add(RowFilter.regexFilter("^" + stt1, 4));
            }
            // Chi filter status khi status khac "All"
            String stt = ((ServiceStatus) cbStatusFilter.getSelectedItem()).getSttName();
            if (!stt.equals("All")) {
                filters.add(RowFilter.regexFilter("^" + stt, 5));
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
        tfUserFilter.setText(null);
        dcFilter.setDate(null);
        dcFilter1.setDate(null);
        cbStatusFilter.setSelectedIndex(cbStatusFilter.getItemCount() - 1);
        cbTypeFilter.setSelectedIndex(cbTypeFilter.getItemCount() - 1);
    }

    //<editor-fold defaultstate="collapsed" desc="xu ly cho table service">
    private void fetchAction() {
        selectedRowIndex = tbServiceList.convertRowIndexToModel(tbServiceList.getSelectedRow());
        selectedService = serviceTableModel.getServiceAtIndex(selectedRowIndex);
        //Reload table product list voi Order moi chon
        serviceDetailsTableModel.load(selectedService.getSerID());
    }

    private void deleteAction() {
        if (EmployeeSwingUtils.showConfirmDialog("Are you sure to delete this service ?") == JOptionPane.NO_OPTION) {
            return;
        }
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        boolean result = serviceTableModel.delete(selectedService);
        setCursor(null);
        EmployeeSwingUtils.showInfoDialog(result ? EmployeeSwingUtils.DELETE_SUCCESS : EmployeeSwingUtils.DELETE_FAIL);

        // Neu row xoa la row cuoi thi lui cursor ve
        // Neu row xoa la row khac cuoi thi tien cursor ve truoc
        selectedRowIndex = (selectedRowIndex == tbServiceList.getRowCount() ? tbServiceList.getRowCount() - 1 : selectedRowIndex++);
        scrollToRow(selectedRowIndex);
        refreshAction(false);
    }
//</editor-fold>

    private void refreshAction(boolean mustInfo) {
        if (mustInfo) {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            serviceTableModel.refresh();
            setCursor(null);
            EmployeeSwingUtils.showInfoDialog(EmployeeSwingUtils.DB_REFRESH);
        } else {
            serviceTableModel.refresh();
        }
        scrollToRow(selectedRowIndex);
    }

    private void scrollToRow(int row) {
        tbServiceList.getSelectionModel().setSelectionInterval(row, row);
        tbServiceList.scrollRectToVisible(new Rectangle(tbServiceList.getCellRect(row, 0, true)));
    }

    private void setButtonEnabled(boolean enabled, JButton... exclude) {
        btUpdate.setEnabled(enabled);
        btRemove.setEnabled(enabled);

        // Ngoai tru may button nay luon luon enable
        if (exclude.length != 0) {
            Arrays.stream(exclude).forEach(b -> b.setEnabled(true));
        }
    }

    private void setButtonRemoveEnabled(boolean enabled, JButton... exclude) {
        btRemove.setEnabled(enabled);

        // Ngoai tru may button nay luon luon enable
        if (exclude.length != 0) {
            Arrays.stream(exclude).forEach(b -> b.setEnabled(true));
        }
    }
}
