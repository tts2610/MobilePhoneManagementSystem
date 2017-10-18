/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package outbound.controller;


import database.IDAO;

import java.util.Date;
import outbound.model.Outbound;
import outbound.model.OutboundDAOImpl;
import supplier.model.Supplier;
import utility.CustomizedTableModel;

/**
 *
 * @author tuan
 */
public class OutboundTableModel extends CustomizedTableModel<Outbound> {

    public OutboundTableModel() {
        super(new OutboundDAOImpl(), new String[]{"ID","Date","Content","UserName"});
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
      Outbound inbound = list.get(rowIndex);
        switch (columnIndex) {
            case 0:
                inbound.setOutID((int) aValue);
                break;
            case 1:
                inbound.setOutDate((Date) aValue);
                break;
            case 2:
                inbound.setOutContent((String) aValue);
                break;
            
            case 3:
                inbound.setUserID((int) aValue);
                break;
            
        }
        fireTableCellUpdated(rowIndex, columnIndex);
    }
    

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        Class[] columnClasses = {Integer.class, Date.class,String.class,  Integer.class};
        return columnClasses[columnIndex];
    }
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        item = list.get(rowIndex);
        Object result = null;
        switch (columnIndex) {
            case 0:
                result = item.getOutID();
                break;
            case 1:
                result = item.getOutDate();
                break;
            case 2:
                result = item.getOutContent();
                break;
            case 3:
                result = item.getUserName();
                break;
            
        }
        return result;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
         return false;
    }
    
}
