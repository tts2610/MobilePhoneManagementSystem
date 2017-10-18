package employee.controller;

import employee.model.Salary;
import employee.model.SalaryDAOImpl;
import java.util.Date;
import service.model.ServiceDetails;
import service.model.ServiceDetailsDAOImpl;
import utility.CustomizedTableModel;

/**
 *
 * @author BonBon
 */
public class SalaryTableModel extends CustomizedTableModel<Salary> {

    public SalaryTableModel() {
        super(new SalaryDAOImpl(), new String[]{"ID", "EmpID", "Month Work ", "PayDay", "WorkDays", "OffDays","Bonus","Basic Salary","Total Salary"});
    }

    public Salary getSalaryFromIndex(int index) {
        return list.get(index);
    }

    public void load(int empID) {
        ((SalaryDAOImpl) super.daoImpl).load(empID);
        list = daoImpl.getList();
        fireTableDataChanged();
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 4 || columnIndex == 5;
    }

    @Override
    public Class<?> getColumnClass(int column) {
        Class[] columnClasses = {Integer.class, Integer.class, Integer.class, Date.class, Integer.class, Integer.class, Integer.class, Integer.class, Double.class};
        return columnClasses[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        item = list.get(rowIndex);
        Object result = null;
        switch (columnIndex) {
            case 0:
                result = item.getSalID();
                break;
            case 1:
                result = item.getEmpID();
                break;
            case 2:
                result = item.getMonth();
                break;
            case 3:
                result = item.getPayDay();
                break;
            case 4:
                result = item.getWorkDays();
                break;
            case 5:
                result = item.getOffDays();
                break;
            case 6:
                result = item.getBonus();
                break;
            case 7:
                result = item.getBasicSalary();
                break;
            case 8:
                result = item.getTotal();
                break;

        }
        return result;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        item = list.get(rowIndex);
        switch (columnIndex) {
            case 0:
                item.setSalID((int) aValue);
                break;
            case 1:
                item.setEmpID((int) aValue);
                break;
            case 2:
                item.setMonth((int) aValue);
                break;
            case 3:
                item.setPayDay((Date) aValue);
                break;
            case 4:
                item.setWorkDays((int) aValue);
                break;
            case 5:
                item.setOffDays((int) aValue);
                break;
            case 6:
                item.setBonus((int) aValue);
                break;
            case 7:
                item.setBasicSalary((int) aValue);
                break;
            case 8:
                item.setTotal((double) aValue);
                break;
        }
        fireTableCellUpdated(rowIndex, columnIndex);
    }
}
