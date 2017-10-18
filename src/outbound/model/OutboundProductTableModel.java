package outbound.model;


import inbound.model.*;
import utility.CustomizedTableModel;

/**
 *
 * @author Hoang
 */
public class OutboundProductTableModel extends CustomizedTableModel<OutboundProduct> {
    
    public OutboundProductTableModel() {
        super(new OutboundProductDAOImpl(), new String[]{"ID", "Product Name", "Quantity"});
    }
    
    public OutboundProduct getOrderProductFromIndex(int index){
        return list.get(index);
    }

    public  void load(int proID) {
        ((OutboundProductDAOImpl) super.daoImpl).load(proID);
        list = daoImpl.getList();
        fireTableDataChanged();
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public Class<?> getColumnClass(int column) {
        Class[] columnClasses = {Integer.class, String.class, Integer.class};
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
                result = item.getProQty();
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
                item.setProQty((int) aValue);
                break;
            
            
        }
        fireTableCellUpdated(rowIndex, columnIndex);
    }
}
