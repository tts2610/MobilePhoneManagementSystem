package service.controller.backup;


import service.controller.*;
import service.model.ServiceDetails;
import service.model.ServiceDetailsDAOImpl;
import utility.CustomizedTableModel;
/**
 *
 * @author BonBon
 */
public class ServiceDetailsTableModel extends CustomizedTableModel<ServiceDetails> {

    public ServiceDetailsTableModel() {
        super(new ServiceDetailsDAOImpl(), new String[]{"ProID","Product Name","Branch","Content", "Qty", "OrderID","Cost","SerID","BraID"});
    }
    
    public ServiceDetails getServiceDetailsFromIndex(int index){
        return list.get(index);
    }

    public void load(int serID) {
        ((ServiceDetailsDAOImpl) super.daoImpl).load(serID);
        list = daoImpl.getList();
        fireTableDataChanged();
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public Class<?> getColumnClass(int column) {
        Class[] columnClasses = {Integer.class, String.class, String.class, String.class, Integer.class,Integer.class,Integer.class,Integer.class, Integer.class};
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
                result = item.getSerContent();
                break;
            case 4:
                result = item.getProQty();
                break;
            case 5:
                result = item.getOrdID();
                break;
            case 6:
                result = item.getSerCost();
                break;    
            case 7:
                result = item.getSerID();
                break;
            case 8:
                result = item.getBraID();
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
                item.setSerContent((String) aValue);
                break;
            case 4:
                item.setProQty((int) aValue);
                break;
            case 5:
                item.setOrdID((int) aValue);
                break;
            case 6:
                item.setSerCost((int) aValue);
                break;  
            case 7:
                item.setSerID((int) aValue);
                break;
            case 8:
                item.setBraID((int) aValue);
                break;
        }
        fireTableCellUpdated(rowIndex, columnIndex);
    }
}
