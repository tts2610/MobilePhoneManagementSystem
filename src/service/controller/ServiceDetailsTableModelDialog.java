package service.controller;

import java.util.Date;
import order.controller.*;
import java.util.List;
import order.model.Order;
import order.model.OrderProduct;
import order.model.OrderProductDAOImpl;
import service.model.Service;
import service.model.ServiceDetails;
import service.model.ServiceDetailsDAOImpl;
import utility.CustomizedTableModel;

/**
 *
 * @author BonBon
 */
public class ServiceDetailsTableModelDialog extends CustomizedTableModel<ServiceDetails> {

    public ServiceDetailsTableModelDialog() {
        super(new ServiceDetailsDAOImpl(), new String[]{"ServiceID", "Product Name", "Branch", "Content", "Qty", "OrderID","Cost", "ProID", "BraID"});
    }

    public List<ServiceDetails> getList() {
        return list;
    }

    public ServiceDetails getOrderProductFromIndex(int index) {
        return list.get(index);
    }

    public void load(int serID) {
        ((ServiceDetailsDAOImpl) super.daoImpl).load(serID);
        list = daoImpl.getList();
        fireTableDataChanged();
    }

    public boolean insert(Service service) {
        ((ServiceDetailsDAOImpl) super.daoImpl).setCurrentService(service);
//        System.out.println("list: " + list);
        return ((ServiceDetailsDAOImpl) super.daoImpl).insert(list);
    }

    public int checkOrdID(int id) {
        return ((ServiceDetailsDAOImpl) super.daoImpl).checkOrdIDInTable(id);
    }
    
    @Override
    public boolean insert(ServiceDetails item) {
        list.add(item);
        fireTableRowsInserted(list.size() - 1, list.size() - 1);
        return true;
    }

    public boolean update(Service service) {
        ((ServiceDetailsDAOImpl) super.daoImpl).setCurrentService(service);
        return ((ServiceDetailsDAOImpl) super.daoImpl).update(list);
    }

    @Override
    public boolean update(ServiceDetails item) {
        return false;
    }

    @Override
    public boolean delete(ServiceDetails item) {
        list.remove(selectingIndex);
        fireTableRowsDeleted(selectingIndex, selectingIndex);
        return true;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 1 || columnIndex == 3 || columnIndex == 4 || columnIndex == 5 || columnIndex == 6; //Chi sua name va qty
    }

    @Override
    public Class<?> getColumnClass(int column) {
        Class[] columnClasses = {Integer.class, String.class, String.class, String.class, Integer.class, Integer.class, Integer.class, Integer.class, Integer.class};
        return columnClasses[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        item = list.get(rowIndex);
        Object result = null;
        if (item != null) {
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
                if (aValue != null) {
                    if (aValue instanceof ServiceDetails) {
                        item.setProName(((ServiceDetails) aValue).getProName());
                    } else {
                        item.setProName((String) aValue);
                    }
                } else {
                    item.setProName(OrderProduct.DEFAULT_PRONAME);
                }
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
