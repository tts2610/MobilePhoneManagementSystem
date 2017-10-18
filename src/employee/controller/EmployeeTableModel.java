package employee.controller;

import employee.model.Employee;
import employee.model.EmployeeDAOImpl;
import java.util.Date;

import utility.CustomizedTableModel;

/**
 * Data model for table Employee List
 *
 * @author BonBon
 */
public class EmployeeTableModel extends CustomizedTableModel<Employee> {

    public EmployeeTableModel() {
        super(new EmployeeDAOImpl(), new String[]{"ID", "Name", "Phone", "Birthday", "BasicSalary", "EmpDes",
            "WorkStart", "Bonus", "STT"});
    }

    public Employee getEmpAtIndex(int index) {
        return list.get(index);
    }

    @Override
    public Class<?> getColumnClass(int column) {
        Class[] columnClasses = {Integer.class, String.class, String.class, Date.class,
            Integer.class, String.class, Date.class, Integer.class, Boolean.class};
        return columnClasses[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        item = list.get(rowIndex);
        Object result = null;
        switch (columnIndex) {
            case 0:
                result = item.getEmpID();
                break;           
            case 1:
                result = item.getEmpName();
                break;
            case 2:
                result = item.getEmpPhone();
                break;
            case 3:
                result = item.getEmpBirthday();
                break;
            case 4:
                result = item.getEmpSalary();
                break;
            case 5:
                result = item.getEmpDes();
                break;
            case 6:
                result = item.getEmpStartDate();
                break;
            case 7:
                result = item.getEmpBonus();
                break;
            case 8:
                result = item.isEmpEnabled();
                break;
        }
        return result;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        Employee employee = list.get(rowIndex);
        switch (columnIndex) {
            case 0:
                employee.setEmpID((int) aValue);
                break;            
            case 1:
                employee.setEmpName((String) aValue);
                break;
            case 2:
                employee.setEmpPhone((String) aValue);
                break;
            case 3:
                employee.setEmpBirthday((Date) aValue);
                break;
            case 4:
                employee.setEmpSalary((int) aValue);
                break;
            case 5:
                employee.setEmpDes((String) aValue);
                break;
            case 6:
                employee.setEmpStartDate((Date) aValue);
                break;
            case 7:
                employee.setEmpBonus((int) aValue);
                break;
            case 8:
                employee.setEmpEnabled((boolean) aValue);
                break;
        }
        fireTableCellUpdated(rowIndex, columnIndex);
    }
}
