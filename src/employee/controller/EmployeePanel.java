package employee.controller;

//import utility.ComboBoxCellEditor;
import com.toedter.calendar.JDateChooser;
import employee.model.Employee;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
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

import employee.model.DateCellBirthDayEditor;
import employee.model.DateCellWorkingDateEditor;
import employee.model.EmployeeDAOImpl;
import utility.IntegerCellEditor;
import utility.StringCellEditor;
import utility.TableCellListener;
import employee.model.EmployeeSwingUtils;
import employee.model.IntegerCurrencyCellRenderer;
import javax.swing.JLabel;
import javax.swing.table.DefaultTableCellRenderer;
import utility.SwingUtils;

/**
 *
 * @author BonBon
 */
public class EmployeePanel extends javax.swing.JPanel {

    private JDateChooser dcFilter;
    private JDateChooser dcFilter1;
    private EmployeeTableModel employeeTableModel;
    private TableRowSorter<EmployeeTableModel> sorter;

    // Employee dang duoc chon trong table
    private Employee selectedEmployee;
    private int selectedRowIndex;

    // Define some column constants
    private static final int COL_EMPID = 0;
    private static final int COL_EMPNAME = 1;
    private static final int COL_EMPPHONE = 2;
    private static final int COL_EMPBIRTH = 3;
    private static final int COL_SALARY = 4;
    private static final int COL_EMPDES = 5;
    private static final int COL_WORKSTART = 6;
    private static final int COL_BONUS = 7;
    private static final int COL_STATUS = 8;

    //<editor-fold defaultstate="collapsed" desc="Constructor">
    /**
     * Creates new form EmployeePanel
     */
    public EmployeePanel() {
        initComponents();
// Set date picker len giao dien
        Calendar c = Calendar.getInstance();
        //birth day
        dcFilter = new JDateChooser();
        dcFilter.setBounds(0, 0, 110, 20);
        dcFilter.setDateFormatString("MMM dd, yyyy");
        dcFilter.setDate(null);
        c.set(1944, 11, 31);
        dcFilter.getJCalendar().setMinSelectableDate(c.getTime());
        Date today=new Date();
        Calendar c1=Calendar.getInstance();
        c1.setTime(today);
        int year=c1.get(Calendar.YEAR);
        int month=c1.get(Calendar.MONTH);
        int day=c1.get(Calendar.DATE);
        c1.set(year-18,month,day-1);        
        dcFilter.getJCalendar().setMaxSelectableDate(c1.getTime());
        pnBirthday.add(dcFilter);
        dcFilter.getDateEditor().setEnabled(false);
        //work day
        dcFilter1 = new JDateChooser();
        dcFilter1.setBounds(0, 0, 110, 20);
        dcFilter1.setDateFormatString("MMM dd, yyyy");
        dcFilter1.setDate(null);
        c.set(2010, 0, 1);
        dcFilter1.getJCalendar().setMinSelectableDate(c.getTime());
        Calendar c2 = Calendar.getInstance();
        dcFilter1.getJCalendar().setMaxSelectableDate(c2.getTime());
        pnStartDate.add(dcFilter1);
        dcFilter1.getDateEditor().setEnabled(false);
        //Disable button khi moi khoi dong len
        setButtonEnabled(false);

        // Selecting customer in the table
        selectedEmployee = new Employee();

        // Set data cho table
        employeeTableModel = new EmployeeTableModel();
        tbEmpployeeList.setModel(employeeTableModel);

        // Set sorter cho table
        sorter = new TableRowSorter<>(employeeTableModel);
        tbEmpployeeList.setRowSorter(sorter);

        //Set auto define column from model to false to stop create column again
        tbEmpployeeList.setAutoCreateColumnsFromModel(false);

        // Set height cho table header
        tbEmpployeeList.getTableHeader().setPreferredSize(new Dimension(100, 30));
        //Set CellEditor cho table
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tbEmpployeeList.getColumnModel().getColumn(COL_EMPID).setCellRenderer(centerRenderer);

        //col empid
        tbEmpployeeList.getColumnModel().getColumn(COL_EMPID).setMaxWidth(30);

        tbEmpployeeList.getColumnModel().getColumn(COL_STATUS).setMinWidth(30);
        tbEmpployeeList.getColumnModel().getColumn(COL_STATUS).setMaxWidth(30);

        // Col emp name
        tbEmpployeeList.getColumnModel().getColumn(COL_EMPNAME).setMinWidth(150);
        tbEmpployeeList.getColumnModel().getColumn(COL_EMPNAME).setMaxWidth(300);
        tbEmpployeeList.getColumnModel().getColumn(COL_EMPNAME).setCellEditor(new StringCellEditor(5, 50, EmployeeSwingUtils.PATTERN_EMPNAME));
        

        tbEmpployeeList.getColumnModel().getColumn(COL_EMPDES).setMinWidth(130);
        tbEmpployeeList.getColumnModel().getColumn(COL_EMPDES).setCellEditor(new StringCellEditor(5, 50, EmployeeSwingUtils.PATTERN_EMPNAME));

        // Col emp phone        
//        tbEmpployeeList.getColumnModel().getColumn(COL_EMPPHONE).setMinWidth(100);
//        
//        tbEmpployeeList.getColumnModel().getColumn(COL_EMPPHONE).setCellEditor(new StringCellEditor(1, 20, SwingUtils.PATTERN_NUMBER));
        // Col cus phone    
        tbEmpployeeList.getColumnModel().getColumn(COL_EMPPHONE).setMinWidth(100);
        tbEmpployeeList.getColumnModel().getColumn(COL_EMPPHONE).setMaxWidth(100);
        tbEmpployeeList.getColumnModel().getColumn(COL_EMPPHONE).setCellEditor(new StringCellEditor(10,50, SwingUtils.PATTERN_NUMBER));
        
        //col bonus
        tbEmpployeeList.getColumnModel().getColumn(COL_BONUS).setMinWidth(80);
        tbEmpployeeList.getColumnModel().getColumn(COL_BONUS).setMaxWidth(80);
        tbEmpployeeList.getColumnModel().getColumn(COL_BONUS).setCellEditor(new IntegerCellEditor(0, 20000000));
        tbEmpployeeList.getColumnModel().getColumn(COL_BONUS).setCellRenderer(new IntegerCurrencyCellRenderer());

        //col salary
        tbEmpployeeList.getColumnModel().getColumn(COL_SALARY).setMinWidth(80);
        tbEmpployeeList.getColumnModel().getColumn(COL_SALARY).setMaxWidth(80);
        tbEmpployeeList.getColumnModel().getColumn(COL_SALARY).setCellEditor(new IntegerCellEditor(5000000, 20000000));
        tbEmpployeeList.getColumnModel().getColumn(COL_SALARY).setCellRenderer(new IntegerCurrencyCellRenderer());
        
        // Col birth date
        tbEmpployeeList.getColumnModel().getColumn(COL_EMPBIRTH).setMinWidth(90);
        tbEmpployeeList.getColumnModel().getColumn(COL_EMPBIRTH).setMaxWidth(90);
        tbEmpployeeList.getColumnModel().getColumn(COL_EMPBIRTH).setCellEditor(new DateCellBirthDayEditor());
        
        // Col work start date
        tbEmpployeeList.getColumnModel().getColumn(COL_WORKSTART).setMinWidth(90);
        tbEmpployeeList.getColumnModel().getColumn(COL_WORKSTART).setMaxWidth(90);
        tbEmpployeeList.getColumnModel().getColumn(COL_WORKSTART).setCellEditor(new DateCellWorkingDateEditor());        

        // Bat su kien select row tren table
        tbEmpployeeList.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
            DefaultListSelectionModel model = (DefaultListSelectionModel) e.getSource();
            if (!model.isSelectionEmpty()) {
                fetchAction();
                setButtonEnabled(true);
            } else {
                setButtonEnabled(false);
            }
        });

        // Check permission employee
        if (!LoginFrame.checkPermission(new UserFunction(UserFunction.FG_EMPLOYEE, UserFunction.FN_UPDATE))) {
            tbEmpployeeList.setEnabled(false);
            btAdd.setEnabled(false);
            btRemove.setEnabled(false);
        }
        // Check permission salary
        if (!LoginFrame.checkPermission(new UserFunction(UserFunction.FG_SALARY, UserFunction.FN_VIEW))) {
            btSalary.setEnabled(false);
        }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Set cell listener cho updating">
        TableCellListener tcl = new TableCellListener(tbEmpployeeList, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TableCellListener tcl = (TableCellListener) e.getSource();
                switch (tcl.getColumn()) {
                    case COL_EMPNAME:
                        selectedEmployee.setEmpName((String) tcl.getNewValue());
                        break;
                    case COL_EMPPHONE:
                        selectedEmployee.setEmpPhone((String) tcl.getNewValue());
                        break;
                    case COL_EMPBIRTH:
                        selectedEmployee.setEmpBirthday((Date) tcl.getNewValue());
                        break;
                    case COL_SALARY:
                        selectedEmployee.setEmpSalary((int) tcl.getNewValue());
                        break;
                    case COL_EMPDES:
                        selectedEmployee.setEmpDes((String) tcl.getNewValue());
                        break;
                    case COL_WORKSTART:
                        selectedEmployee.setEmpStartDate((Date) tcl.getNewValue());
                        break;
                    case COL_BONUS:
                        selectedEmployee.setEmpBonus((int) tcl.getNewValue());
                        break;
                    case COL_STATUS:
                        selectedEmployee.setEmpEnabled((boolean) tcl.getNewValue());
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
        tfBonusFilter.getDocument().addDocumentListener(
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
        tfEmpPhoneFilter.getDocument().addDocumentListener(
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
        tfEmpDes.getDocument().addDocumentListener(
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
        dcFilter1.getDateEditor().addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                doFilter();
            }
        });
//</editor-fold>

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
        tfEmpNameFilter = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        cbStatusFilter = new javax.swing.JComboBox<>();
        tfEmpPhoneFilter = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        pnBirthday = new javax.swing.JPanel();
        pnStartDate = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        tfEmpDes = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        tfBonusFilter = new javax.swing.JTextField();
        btRemove = new javax.swing.JButton();
        btAdd = new javax.swing.JButton();
        btRefresh = new javax.swing.JButton();
        pnTitle = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        btSalary = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbEmpployeeList = new javax.swing.JTable();

        setPreferredSize(new java.awt.Dimension(790, 640));

        pnFilter.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Filter"));

        jLabel2.setText("ID:");

        jLabel3.setText("Name:");
        jLabel3.setPreferredSize(new java.awt.Dimension(55, 16));

        jLabel6.setText("Status:");

        cbStatusFilter.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All", "Enabled", "Disabled" }));
        cbStatusFilter.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbStatusFilterItemStateChanged(evt);
            }
        });

        jLabel5.setText("Phone:");

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/order/Refresh2.png"))); // NOI18N
        jButton1.setBorderPainted(false);
        jButton1.setContentAreaFilled(false);
        jButton1.setFocusPainted(false);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel7.setText("Birthday");
        jLabel7.setPreferredSize(new java.awt.Dimension(55, 16));

        jLabel8.setText("WorkStartDate");

        pnBirthday.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout pnBirthdayLayout = new javax.swing.GroupLayout(pnBirthday);
        pnBirthday.setLayout(pnBirthdayLayout);
        pnBirthdayLayout.setHorizontalGroup(
            pnBirthdayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 106, Short.MAX_VALUE)
        );
        pnBirthdayLayout.setVerticalGroup(
            pnBirthdayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        pnStartDate.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout pnStartDateLayout = new javax.swing.GroupLayout(pnStartDate);
        pnStartDate.setLayout(pnStartDateLayout);
        pnStartDateLayout.setHorizontalGroup(
            pnStartDateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        pnStartDateLayout.setVerticalGroup(
            pnStartDateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 18, Short.MAX_VALUE)
        );

        jLabel9.setText("Designation:");
        jLabel9.setPreferredSize(new java.awt.Dimension(43, 16));

        jLabel4.setText("Bonus:");

        javax.swing.GroupLayout pnFilterLayout = new javax.swing.GroupLayout(pnFilter);
        pnFilter.setLayout(pnFilterLayout);
        pnFilterLayout.setHorizontalGroup(
            pnFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnFilterLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(tfBonusFilter, javax.swing.GroupLayout.DEFAULT_SIZE, 47, Short.MAX_VALUE)
                    .addComponent(tfIdFilter))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pnFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(tfEmpPhoneFilter, javax.swing.GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE)
                    .addComponent(tfEmpNameFilter))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pnFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pnBirthday, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnStartDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, Short.MAX_VALUE)
                .addGroup(pnFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, 73, Short.MAX_VALUE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cbStatusFilter, 0, 80, Short.MAX_VALUE)
                    .addComponent(tfEmpDes))
                .addGap(17, 17, 17)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        pnFilterLayout.setVerticalGroup(
            pnFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnFilterLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jButton1))
            .addGroup(pnFilterLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(pnFilterLayout.createSequentialGroup()
                        .addGroup(pnFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cbStatusFilter))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(pnFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(tfEmpDes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(pnFilterLayout.createSequentialGroup()
                        .addGroup(pnFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(pnFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel2)
                                .addComponent(tfIdFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(tfEmpNameFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(pnBirthday, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(pnFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(pnStartDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(pnFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel4)
                                .addComponent(tfBonusFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pnFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel5)
                                .addComponent(tfEmpPhoneFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel8))))))
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
        btAdd.setText("Add New");
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
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/main/Employee.png"))); // NOI18N
        jLabel1.setText("<html><u><i><font color='red'>E</font>mployee <font color='red'>M</font>anagement</i></u></html>");

        btSalary.setFont(new java.awt.Font("Lucida Grande", 3, 14)); // NOI18N
        btSalary.setForeground(new java.awt.Color(255, 153, 0));
        btSalary.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/user/rsz_salary32.png"))); // NOI18N
        btSalary.setText("<html><u>Salary...</u></html>");
        btSalary.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btSalaryActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnTitleLayout = new javax.swing.GroupLayout(pnTitle);
        pnTitle.setLayout(pnTitleLayout);
        pnTitleLayout.setHorizontalGroup(
            pnTitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnTitleLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 450, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btSalary, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        pnTitleLayout.setVerticalGroup(
            pnTitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnTitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 61, Short.MAX_VALUE)
                .addComponent(btSalary, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jScrollPane2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Employee List"));

        tbEmpployeeList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Name", "Phone", "Birthday", "BasicSalary", "EmpDes", "StartDate", "Bonus", "Enabled"
            }
        ));
        tbEmpployeeList.setFillsViewportHeight(true);
        tbEmpployeeList.setRowHeight(25);
        tbEmpployeeList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tbEmpployeeList.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(tbEmpployeeList);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(164, Short.MAX_VALUE)
                .addComponent(btAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btRemove, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(164, Short.MAX_VALUE))
            .addComponent(jScrollPane2)
            .addComponent(pnFilter, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pnTitle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnTitle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 406, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btRemove, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    // <editor-fold defaultstate="collapsed" desc="Khai bao event">    
    private void btAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btAddActionPerformed
        insertAction();
    }//GEN-LAST:event_btAddActionPerformed

    private void btSalaryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btSalaryActionPerformed
        if (selectedEmployee.getEmpID() == 0) {
            EmployeeDAOImpl e = new EmployeeDAOImpl();
            Employee emp = e.getEmpFromUserName(LoginFrame.config.userName);
            SalaryDialog sd = new SalaryDialog(emp);

            sd.setTitle("Salary Details of     " + emp.getEmpName() + "     Working with Account     " + LoginFrame.config.userName);
            sd.setVisible(true);
        } else {
            EmployeeDAOImpl e = new EmployeeDAOImpl();
            SalaryDialog sd = new SalaryDialog(selectedEmployee);
            if (e.getUserNameFromEmpID(selectedEmployee.getEmpID()).isEmpty() == false) {
                sd.setTitle("Salary Details of     " + selectedEmployee.getEmpName() + "     working with account     " + e.getUserNameFromEmpID(selectedEmployee.getEmpID()));
            } else {
                sd.setTitle("Salary Details of     " + selectedEmployee.getEmpName());
            }
            sd.setVisible(true);
        }
    }//GEN-LAST:event_btSalaryActionPerformed

    private void btRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btRefreshActionPerformed
        refreshAction(true);
    }//GEN-LAST:event_btRefreshActionPerformed

    private void btRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btRemoveActionPerformed
        if (EmployeeSwingUtils.showConfirmDialog("Are you sure to delete ?") == JOptionPane.NO_OPTION) {
                    return;
                } else {
                    deleteAction();
                }        
    }//GEN-LAST:event_btRemoveActionPerformed

    private void cbStatusFilterItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbStatusFilterItemStateChanged
        doFilter();
    }//GEN-LAST:event_cbStatusFilterItemStateChanged

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        clearFilter();
    }//GEN-LAST:event_jButton1ActionPerformed
    //// </editor-fold>
    //<editor-fold defaultstate="collapsed" desc="khai bao component">
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btAdd;
    private javax.swing.JButton btRefresh;
    private javax.swing.JButton btRemove;
    private javax.swing.JButton btSalary;
    private javax.swing.JComboBox<String> cbStatusFilter;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPanel pnBirthday;
    private javax.swing.JPanel pnFilter;
    private javax.swing.JPanel pnStartDate;
    private javax.swing.JPanel pnTitle;
    private javax.swing.JTable tbEmpployeeList;
    private javax.swing.JTextField tfBonusFilter;
    private javax.swing.JTextField tfEmpDes;
    private javax.swing.JTextField tfEmpNameFilter;
    private javax.swing.JTextField tfEmpPhoneFilter;
    private javax.swing.JTextField tfIdFilter;
    // End of variables declaration//GEN-END:variables
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="doFilter">
    private void doFilter() {
        RowFilter<EmployeeTableModel, Object> rf = null;

        //Filter theo regex, neu parse bi loi thi khong filter
        try {
            List<RowFilter<EmployeeTableModel, Object>> filters = new ArrayList<>();
            filters.add(RowFilter.regexFilter("^" + tfIdFilter.getText(), 0));

            filters.add(RowFilter.regexFilter("^" + tfEmpNameFilter.getText(), 1));
            filters.add(RowFilter.regexFilter("^" + tfEmpPhoneFilter.getText(), 2));
            // Chi filter date khi date khac null
            if (dcFilter.getDate() != null) {
                // dcFilter khac voi date cua table o GIO, PHUT, GIAY nen phai dung Calendar de so sanh chi nam, thang, ngay.... oh vai~.
                // Magic here....
                RowFilter<EmployeeTableModel, Object> dateFilter = new RowFilter<EmployeeTableModel, Object>() {
                    @Override
                    public boolean include(RowFilter.Entry<? extends EmployeeTableModel, ? extends Object> entry) {
                        EmployeeTableModel model = entry.getModel();
                        Employee o = model.getEmpAtIndex((Integer) entry.getIdentifier());

                        Calendar origin = Calendar.getInstance();
                        origin.setTime(o.getEmpBirthday());

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
            //filter workdate
            if (dcFilter1.getDate() != null) {
                // dcFilter khac voi date cua table o GIO, PHUT, GIAY nen phai dung Calendar de so sanh chi nam, thang, ngay.... oh vai~.
                // Magic here....
                RowFilter<EmployeeTableModel, Object> dateFilter = new RowFilter<EmployeeTableModel, Object>() {
                    @Override
                    public boolean include(RowFilter.Entry<? extends EmployeeTableModel, ? extends Object> entry) {
                        EmployeeTableModel model = entry.getModel();
                        Employee o = model.getEmpAtIndex((Integer) entry.getIdentifier());

                        Calendar origin = Calendar.getInstance();
                        origin.setTime(o.getEmpStartDate());

                        Calendar filter = Calendar.getInstance();
                        filter.setTime(dcFilter1.getDate());

                        if (origin.get(Calendar.YEAR) == filter.get(Calendar.YEAR) && origin.get(Calendar.MONTH) == filter.get(Calendar.MONTH) && origin.get(Calendar.DATE) == filter.get(Calendar.DATE)) {
                            return true;
                        }

                        return false;
                    }
                };

                filters.add(dateFilter);
            }
            // Neu co chon user name thi moi filter username

            filters.add(RowFilter.regexFilter("^" + tfBonusFilter.getText(), 7));
            filters.add(RowFilter.regexFilter("^" + tfEmpDes.getText(), 5));

            // Neu status khac "All" thi moi filter
            String statusFilter = cbStatusFilter.getSelectedItem().toString();
            if (!statusFilter.equals("All")) {
                filters.add(RowFilter.regexFilter(
                        statusFilter.equals("Enabled") ? "t" : "f", 8));
            }

            rf = RowFilter.andFilter(filters);
        } catch (java.util.regex.PatternSyntaxException e) {
            return;
        }
        sorter.setRowFilter(rf);
    }
//</editor-fold>

    // Test commit ti
    private void clearFilter() {
        tfIdFilter.setText(null);
        tfBonusFilter.setText(null);
        tfEmpPhoneFilter.setText(null);
        tfEmpNameFilter.setText(null);
        tfEmpDes.setText(null);
        dcFilter.setDate(null);
        dcFilter1.setDate(null);
        cbStatusFilter.setSelectedIndex(0);
    }

    private void fetchAction() {
        selectedRowIndex = tbEmpployeeList.getSelectedRow();
        selectedEmployee.setEmpID((int) tbEmpployeeList.getValueAt(selectedRowIndex, 0));
        selectedEmployee.setEmpName((String) tbEmpployeeList.getValueAt(selectedRowIndex, 1));
        selectedEmployee.setEmpPhone((String) tbEmpployeeList.getValueAt(selectedRowIndex, 2));
        selectedEmployee.setEmpBirthday((Date) tbEmpployeeList.getValueAt(selectedRowIndex, 3));
        selectedEmployee.setEmpSalary((int) tbEmpployeeList.getValueAt(selectedRowIndex, 4));
        selectedEmployee.setEmpDes((String) tbEmpployeeList.getValueAt(selectedRowIndex, 5));
        selectedEmployee.setEmpStartDate((Date) tbEmpployeeList.getValueAt(selectedRowIndex, 6));
        selectedEmployee.setEmpBonus((int) tbEmpployeeList.getValueAt(selectedRowIndex, 7));
        selectedEmployee.setEmpEnabled((boolean) tbEmpployeeList.getValueAt(selectedRowIndex, 8));
//        System.out.println("fetch: "+selectedEmployee.toString());
    }

    private void refreshAction(boolean mustInfo) {
        if (mustInfo) {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            // Refresh table
            employeeTableModel.refresh();
            
            setCursor(null);
            EmployeeSwingUtils.showInfoDialog(EmployeeSwingUtils.DB_REFRESH);
        } else {
            // Refresh table
            employeeTableModel.refresh();

        }
        scrollToRow(selectedRowIndex);
    }

    private void insertAction() {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        boolean result = employeeTableModel.insert(new Employee());
        setCursor(null);
        EmployeeSwingUtils.showInfoDialog(result ? EmployeeSwingUtils.INSERT_SUCCESS : EmployeeSwingUtils.INSERT_FAIL);
        // Select row vua insert vao
        selectedRowIndex = tbEmpployeeList.getRowCount() - 1;
        scrollToRow(selectedRowIndex);
        tbEmpployeeList.editCellAt(tbEmpployeeList.getSelectedRow(), 1);
        tbEmpployeeList.getEditorComponent().requestFocus();
    }

    // comment thu de push
    private void updateAction() {
//        System.out.println("Updateaction: "+selectedEmployee.toString());
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        boolean result = employeeTableModel.update(selectedEmployee);
        refreshAction(false);
        setCursor(null);
        EmployeeSwingUtils.showInfoDialog(result ? EmployeeSwingUtils.UPDATE_SUCCESS : EmployeeSwingUtils.UPDATE_FAIL);
        scrollToRow(selectedRowIndex);
    }

    private void deleteAction() {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        boolean result = employeeTableModel.delete(selectedEmployee);
        setCursor(null);
        EmployeeSwingUtils.showInfoDialog(result ? EmployeeSwingUtils.DELETE_SUCCESS : EmployeeSwingUtils.DELETE_FAIL);

        // Neu row xoa la row cuoi thi lui cursor ve
        // Neu row xoa la row khac cuoi thi tien cursor ve truoc
        selectedRowIndex = (selectedRowIndex == tbEmpployeeList.getRowCount() ? tbEmpployeeList.getRowCount() - 1 : selectedRowIndex++);
        scrollToRow(selectedRowIndex);
    }

    private void scrollToRow(int row) {
        tbEmpployeeList.getSelectionModel().setSelectionInterval(row, row);
        tbEmpployeeList.scrollRectToVisible(new Rectangle(tbEmpployeeList.getCellRect(row, 0, true)));
    }

    private void setButtonEnabled(boolean enabled, JButton... exclude) {
        btRemove.setEnabled(enabled);
//        btAdd.setEnabled(enabled);
//        btSalary.setEnabled(enabled);

        // Ngoai tru may button nay luon luon enable
        if (exclude.length != 0) {
            Arrays.stream(exclude).forEach(b -> b.setEnabled(true));
        }
    }
}
