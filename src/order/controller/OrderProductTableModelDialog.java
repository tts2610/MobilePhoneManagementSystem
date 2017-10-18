package order.controller;

import java.util.List;
import order.model.Order;
import order.model.OrderProduct;
import order.model.OrderProductDAOImpl;
import utility.CustomizedTableModel;

/**
 *
 * @author Hoang
 */
public class OrderProductTableModelDialog extends CustomizedTableModel<OrderProduct> {

    public OrderProductTableModelDialog() {
        super(new OrderProductDAOImpl(), new String[]{"No.", "Product Name", "Qty", "Price 1", "SalesOff", "Price 2", "ID"});
    }

    public List<OrderProduct> getList() {
        return list;
    }

    public OrderProduct getOrderProductFromIndex(int index) {
        return list.get(index);
    }

    public void load(int ordID) {
        ((OrderProductDAOImpl) super.daoImpl).load(ordID);
        list = daoImpl.getList();
        fireTableDataChanged();
    }

    public boolean insert(Order order) {
        ((OrderProductDAOImpl) super.daoImpl).setCurrentOrder(order);
        return ((OrderProductDAOImpl) super.daoImpl).insert(list);
    }

    @Override
    public boolean insert(OrderProduct item) {
        list.add(item);
        fireTableRowsInserted(list.size() - 1, list.size() - 1);
        return true;
    }

    public boolean update(Order order) {
        ((OrderProductDAOImpl) super.daoImpl).setCurrentOrder(order);
        return ((OrderProductDAOImpl) super.daoImpl).update(list);
    }

    @Override
    public boolean update(OrderProduct item) {
        return false;
    }

    @Override
    public boolean delete(OrderProduct item) {
        list.remove(selectingIndex);
        fireTableRowsDeleted(selectingIndex, selectingIndex);
        return true;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 1 || columnIndex == 2; //Chi sua name va qty
    }

    @Override
    public Class<?> getColumnClass(int column) {
        Class[] columnClasses = {Integer.class, String.class, Integer.class, Float.class, Float.class, Float.class, Integer.class};
        return columnClasses[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        item = list.get(rowIndex);
        Object result = null;
        if (item != null) {
            switch (columnIndex) {
                case 0:
                    result = item.getProNo();
                    break;
                case 1:
                    result = item.getProName();
                    break;
                case 2:
                    result = item.getProQty();
                    break;
                case 3:
                    result = item.getProPrice1();
                    break;
                case 4:
                    result = item.getSalesOffAmount();
                    break;
                case 5:
                    result = item.getProPrice2();
                    break;
                case 6:
                    result = item.getProID();
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
                item.setProNo((int) aValue);
                break;
            case 1:
                if (aValue != null) {
                    if (aValue instanceof OrderProduct) {
                        item.setProName(((OrderProduct) aValue).getProName());
                    } else {
                        item.setProName((String) aValue);
                    }
                } else {
                    item.setProName(OrderProduct.DEFAULT_PRONAME);
                }
                break;
            case 2:
                item.setProQty((int) aValue);
                break;
            case 3:
                item.setProPrice1((float) aValue);
                break;
            case 4:
                item.setSalesOffAmount((float) aValue);
                break;
            case 5:
                item.setProPrice2((float) aValue);
                break;
            case 6:
                item.setProID((int) aValue);
                break;
        }
        fireTableCellUpdated(rowIndex, columnIndex);
    }
}
