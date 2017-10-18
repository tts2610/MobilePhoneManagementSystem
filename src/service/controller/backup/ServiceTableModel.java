package service.controller.backup;

import service.controller.*;
import java.util.Date;
import service.model.Service;
import service.model.ServiceDAOImpl;
import utility.CustomizedTableModel;

/**
 *
 * @author BonBon
 */
public class ServiceTableModel extends CustomizedTableModel<Service> {

    public ServiceTableModel() {
        super(new ServiceDAOImpl(), new String[]{"ID", "User Name", "Receive Date", "Return Date", "Service Type", "Status"});
    }
    
    public Service getServiceAtIndex(int index){
        return list.get(index);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }
    
    @Override
    public Class<?> getColumnClass(int column) {
        Class[] columnClasses = {Integer.class, String.class,  Date.class, Date.class, String.class, String.class};
        return columnClasses[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        item = list.get(rowIndex);
        Object result = null;
        switch (columnIndex) {
            case 0:
                result = item.getSerID();
                break;
            case 1:
                result = item.getUserName();
                break;
            case 2:
                result = item.getReceiveDate();
                break;
            case 3:
                result = item.getReturnDate();
                break;
            case 4:
                result = item.getSerTypeName();
                break;
            case 5:
                result = item.getSerStatus();
                break;
        }
        return result;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        item = list.get(rowIndex);
        switch (columnIndex) {
            case 0:
                item.setSerID((int) aValue);
                break;
            case 1:
                item.setUserName((String) aValue);
                break;
            case 2:
                item.setReceiveDate((Date) aValue);
                break;
            case 3:
                item.setReturnDate((Date) aValue);
                break;
            case 4:
                item.setSerTypeName((String) aValue);
                break;
            case 5:
                item.setSerStatus((String) aValue);
                break;
        }
        fireTableCellUpdated(rowIndex, columnIndex);
    }
}
