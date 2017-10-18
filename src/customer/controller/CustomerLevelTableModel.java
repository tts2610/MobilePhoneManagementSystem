/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.controller;

import customer.model.CustomerLevelDAOImpl;
import customer.model.CustomerLevel;
import utility.CustomizedTableModel;

/**
 * Data model for table CustomerLevel List
 *
 * @author Hoang
 */
public class CustomerLevelTableModel extends CustomizedTableModel<CustomerLevel> {
    
    public CustomerLevelTableModel() {
        super(new CustomerLevelDAOImpl(), new String[]{"ID", "Level", "Level Name", "Discount (%)"});
    }
    
    @Override
    public Class<?> getColumnClass(int column) {
        Class[] columnClasses = {Integer.class, Integer.class, String.class, Float.class};
        return columnClasses[column];
    }
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        item = list.get(rowIndex);
        Object result = null;
        switch (columnIndex) {
            case 0:
                result = item.getCusLevelID();
                break;
            case 1:
                result = item.getCusLevel();
                break;
            case 2:
                result = item.getCusLevelName();
                break;
            case 3:
                result = item.getCusDiscount() * 100;
                break;
        }
        return result;
    }
    
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        item = list.get(rowIndex);
        switch (columnIndex) {
            case 0:
                item.setCusLevelID((int) aValue);
                break;
            case 1:
                item.setCusLevel((int) aValue);
                break;
            case 2:
                item.setCusLevelName((String) aValue);
                break;
            case 3:
                if (aValue instanceof Integer) {
                    item.setCusDiscount(((int) aValue) / 100f);
                } else {
                    item.setCusDiscount((float) aValue / 100);
                }
                break;
        }
        fireTableCellUpdated(rowIndex, columnIndex);
    }
}
