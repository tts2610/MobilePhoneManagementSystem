/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package salesoff.controller;

import java.util.Date;
import salesoff.model.SalesOff;
import salesoff.model.SalesOffDAOImpl;
import utility.CustomizedTableModel;

/**
 *
 * @author Hoang
 */
public class SalesOffTableModel extends CustomizedTableModel<SalesOff> {

    private boolean updatable;

    public SalesOffTableModel(boolean updatable) {
        super(new SalesOffDAOImpl(), new String[]{"ID", "Name", "Start Date", "End Date", "Amount (%)"});
        this.updatable = updatable;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        if (updatable) {
            return super.isCellEditable(rowIndex, columnIndex);
        } else {
            return false;
        }
    }

    @Override
    public Class<?> getColumnClass(int column) {
        Class[] columnClasses = {Integer.class, String.class, Date.class, Date.class, Float.class};
        return columnClasses[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        item = list.get(rowIndex);
        Object result = null;
        switch (columnIndex) {
            case 0:
                result = item.getSaleID();
                break;
            case 1:
                result = item.getSaleName();
                break;
            case 2:
                result = item.getSaleStartDate();
                break;
            case 3:
                result = item.getSaleEndDate();
                break;
            case 4:
                result = item.getSaleAmount() * 100;
                break;
        }
        return result;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        item = list.get(rowIndex);
        switch (columnIndex) {
            case 0:
                item.setSaleID((int) aValue);
                break;
            case 1:
                item.setSaleName((String) aValue);
                break;
            case 2:
                item.setSaleStartDate((Date) aValue);
                break;
            case 3:
                item.setSaleEndDate((Date) aValue);
                break;
            case 4:
                if (aValue instanceof Integer) {
                    item.setSaleAmount(((int) aValue) / 100f);
                } else {
                    item.setSaleAmount((float) aValue / 100);
                }
                break;
        }
        fireTableCellUpdated(rowIndex, columnIndex);
    }
}
