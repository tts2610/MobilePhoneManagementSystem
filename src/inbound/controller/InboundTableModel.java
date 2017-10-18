/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inbound.controller;

import database.IDAO;
import inbound.model.Inbound;
import inbound.model.InboundDAOImpl;
import java.util.Date;
import supplier.model.Supplier;
import utility.CustomizedTableModel;

/**
 *
 * @author tuan
 */
public class InboundTableModel extends CustomizedTableModel<Inbound> {

    public InboundTableModel() {
        super(new InboundDAOImpl(), new String[]{"ID","Date","Supplier Name","Supplier Invoice ID","UserName"});
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
      Inbound inbound = list.get(rowIndex);
        switch (columnIndex) {
            case 0:
                inbound.setInID((int) aValue);
                break;
            case 1:
                inbound.setInDate((Date) aValue);
                break;
            case 2:
                inbound.setSupName(((Supplier) aValue).getSupName());
                break;
            case 3:
                inbound.setSupInvoiceID((String) aValue);
                break;
            case 4:
                inbound.setUserID((int) aValue);
                break;
            
        }
        fireTableCellUpdated(rowIndex, columnIndex);
    }
    

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        Class[] columnClasses = {Integer.class, Date.class, String.class, String.class, Integer.class};
        return columnClasses[columnIndex];
    }
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        item = list.get(rowIndex);
        Object result = null;
        switch (columnIndex) {
            case 0:
                result = item.getInID();
                break;
            case 1:
                result = item.getInDate();
                break;
            case 2:
                result = item.getSupName();
                break;
            case 3:
                result = item.getSupInvoiceID();
                break;
            case 4:
                result = item.getUserName();
                break;
            
        }
        return result;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
         return false;
    }
    
     public Inbound getInboundAtIndex(int index) {
        return list.get(index);
    }
}
