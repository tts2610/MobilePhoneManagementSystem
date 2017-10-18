package salesoff.controller;

import salesoff.model.SalesOffProduct;
import salesoff.model.SalesOffProductDAOImpl;
import utility.CustomizedTableModel;

/**
 *
 * @author Hoang
 */
public class SalesOffProductTableModel extends CustomizedTableModel<SalesOffProduct> {

    public SalesOffProductTableModel() {
        super(new SalesOffProductDAOImpl(), new String[]{"ID", "Product Name", "Branch", "SalesOff"});
    }
    
    public void load(int salesOffID){
        ((SalesOffProductDAOImpl) super.daoImpl).load(salesOffID);
        list = daoImpl.getList();
        fireTableDataChanged();
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        // Chi cho sua column SalesOff
        if (columnIndex==3) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Class<?> getColumnClass(int column) {
        Class[] columnClasses = {Integer.class, String.class, String.class, Boolean.class};
        return columnClasses[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        item = list.get(rowIndex);
        Object result = null;
        switch (columnIndex) {
            case 0:
                result = item.getProID();
                break;
            case 1:
                result = item.getProName();
                break;
            case 2:
                result = item.getBraName();
                break;
            case 3:
                result = item.isSalesOff();
                break;
        }
        return result;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        item = list.get(rowIndex);
        switch (columnIndex) {
            case 0:
                item.setProID((int) aValue);
                break;
            case 1:
                item.setProName((String) aValue);
                break;
            case 2:
                item.setBraName((String) aValue);
                break;
            case 3:
                item.setSalesOff((boolean) aValue);
                break;
        }
        fireTableCellUpdated(rowIndex, columnIndex);
    }
}
