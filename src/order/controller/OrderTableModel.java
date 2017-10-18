package order.controller;

import java.util.Date;
import order.model.Order;
import order.model.OrderDAOImpl;
import utility.CustomizedTableModel;
import utility.SwingUtils;
import utility.SwingUtils.FormatType;

/**
 *
 * @author Hoang
 */
public class OrderTableModel extends CustomizedTableModel<Order> {

    public OrderTableModel() {
        super(new OrderDAOImpl(), new String[]{"ID","User Name", "Cus. Name", "Date", "Status", "Discount", "Value"});
    }

    public Order getOrderAtIndex(int index) {
        return list.get(index);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public Class<?> getColumnClass(int column) {
        Class[] columnClasses = {Integer.class, String.class, String.class, Date.class, String.class, Float.class, Float.class};
        return columnClasses[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        item = list.get(rowIndex);
        Object result = null;
        switch (columnIndex) {
            case OrderPanel.COL_ORDID:
                result = item.getOrdID();
                break;
            case OrderPanel.COL_ORDDATE:
                result = item.getOrdDate();
                break;
            case OrderPanel.COL_ORDVALUE:
                result = item.getOrdValue();
                break;
            case OrderPanel.COL_STATUS:
                result = item.getOrdStatus();
                break;
            case OrderPanel.COL_DISCOUNT:
                result = item.getCusDiscount();
                break;
            case OrderPanel.COL_CUSNAME:
                result = item.getCusName();
                break;
            case OrderPanel.COL_USERNAME:
                result = item.getUserName();
                break;
        }
        return result;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        item = list.get(rowIndex);
        switch (columnIndex) {
            case OrderPanel.COL_ORDID:
                item.setOrdID((int) aValue);
                break;
            case OrderPanel.COL_ORDDATE:
                item.setOrdDate((Date) aValue);
                break;
            case OrderPanel.COL_ORDVALUE:
                item.setOrdValue((float) SwingUtils.unFormatString((String) aValue, FormatType.CURRENCY));
                break;
            case OrderPanel.COL_STATUS:
                item.setOrdStatus((String) aValue);
                break;
            case OrderPanel.COL_DISCOUNT:
                item.setCusDiscount((float) SwingUtils.unFormatString((String) aValue, FormatType.PERCENT));
                break;
            case OrderPanel.COL_CUSNAME:
                item.setCusName((String) aValue);
                break;
            case OrderPanel.COL_USERNAME:
                item.setUserName((String) aValue);
                break;
        }
        fireTableCellUpdated(rowIndex, columnIndex);
    }
}
