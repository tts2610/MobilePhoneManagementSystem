package customer.controller;

import customer.model.CustomerDAOImpl;
import customer.model.Customer;
import customer.model.CustomerLevel;
import java.util.ArrayList;
import java.util.List;
import utility.CustomizedTableModel;

/**
 * Data model for table Customer List
 *
 * @author Hoang
 */
public class CustomerTableModel extends CustomizedTableModel<Customer> {

    public CustomerTableModel() {
        super(new CustomerDAOImpl(), new String[]{"ID", "Name", "Paid", "Level", "Phone", "Address", "Status"});
    }
    
    public Customer getCustomerFromID(int id){
        List<Customer> tmp = new ArrayList();
        list.stream().filter(c->c.getCusID()==id).forEach(c->tmp.add(c));
        return tmp.size()>0?tmp.get(0):null;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex!=0 && columnIndex!=2; // Khong sua ID va paid
    }

    @Override
    public Class<?> getColumnClass(int column) {
        Class[] columnClasses = {Integer.class, String.class, Float.class, String.class, String.class, String.class, Boolean.class};
        return columnClasses[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        item = list.get(rowIndex);
        Object result = null;
        switch (columnIndex) {
            case CustomerPanel.COL_CUSID:
                result = item.getCusID();
                break;
            case CustomerPanel.COL_CUSNAME:
                result = item.getCusName();
                break;
            case CustomerPanel.COL_CUSPAID:
                result = item.getCusPaid();
                break;
            case CustomerPanel.COL_CUSLEVELNAME:
                result = item.getCusLevelName();
                break;
            case CustomerPanel.COL_CUSPHONE:
                result = item.getCusPhone();
                break;
            case CustomerPanel.COL_CUSADDRESS:
                result = item.getCusAddress();
                break;
            case CustomerPanel.COL_STATUS:
                result = item.isCusEnabled();
                break;
        }
        return result;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        Customer customer = list.get(rowIndex);
        switch (columnIndex) {
            case CustomerPanel.COL_CUSID:
                customer.setCusID((int) aValue);
                break;
            case CustomerPanel.COL_CUSNAME:
                customer.setCusName((String) aValue);
                break;
            case CustomerPanel.COL_CUSPAID:
                customer.setCusPaid((float) aValue);
                break;
            case CustomerPanel.COL_CUSLEVELNAME:
                customer.setCusLevelName(((CustomerLevel) aValue).getCusLevelName());
                break;
            case CustomerPanel.COL_CUSPHONE:
                customer.setCusPhone((String) aValue);
                break;
            case CustomerPanel.COL_CUSADDRESS:
                customer.setCusAddress((String) aValue);
                break;
            case CustomerPanel.COL_STATUS:
                customer.setCusEnabled((boolean) aValue);
                break;
        }
        fireTableCellUpdated(rowIndex, columnIndex);
    }
}
