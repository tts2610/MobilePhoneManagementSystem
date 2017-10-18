/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inbound.model;

import database.IDAO;
import java.util.List;
import inbound.model.InboundDetail;
import inbound.model.InboundDetailDAOImpl;

import utility.CustomizedTableModel;
import utility.SwingUtils;

/**
 *
 * @author tuan
 */
public class InboundDetailTableModel extends CustomizedTableModel<InboundDetail>{

    public InboundDetailTableModel() {
        super(new InboundDetailDAOImpl(), new String[]{"ProID","ProName","ProCost","ProQty"});
    }
    public List<InboundDetail> getList() {
        return list;
    }
    
    public InboundDetail getInboundDetailFromIndex(int index) {
        return list.get(index);
    }
    public void load(int inID) {
        ((InboundDetailDAOImpl) super.daoImpl).load(inID);
        list = daoImpl.getList();
        fireTableDataChanged();
    }
    @Override
    public Class<?> getColumnClass(int columnIndex) {
         Class[] columnClass = {Integer.class,String.class,Float.class,Integer.class};
         return columnClass[columnIndex];
    }

    
    
    @Override
    public boolean insert(InboundDetail item) {
         if(!checkDuplicateInbound(item)){
            list.add(item);
            fireTableRowsInserted(list.size() - 1, list.size() - 1);
         }
         else{
             SwingUtils.showInfoDialog("Dupplicate product list!");
             return false;
         }
         return true;
    }
    
    public boolean checkDuplicateInbound(InboundDetail inbound){
        for(InboundDetail i : list){
            if(i.getProID()==inbound.getProID())
                return true;
        }
        return false;
    }

    @Override
    public boolean delete(InboundDetail item) {
         list.remove(selectingIndex);
//         new InboundDetailDAOImpl().delete(item);
         fireTableRowsDeleted(selectingIndex, selectingIndex);
        return true;
    }

    @Override
    public boolean update(InboundDetail item) {
        return super.update(item); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        item = list.get(rowIndex);
        Object result = null;
        if (item != null) {
            switch(columnIndex){
                case 0:result = item.getProID();break;
                case 1:result = item.getProName();break;
                case 2:result = item.getProCost();break;
                case 3:result = item.getProQty();break;
            }
        }
        return result;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
         item = list.get(rowIndex);
         switch(columnIndex){
             case 0: item.setProID((int) aValue);break;
             case 1: item.setProName((String) aValue);break;
             case 2: item.setProCost((float) aValue);break;
             case 3: item.setProQty((int) aValue);break;
             
         }
         fireTableCellUpdated(rowIndex, columnIndex);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
         if(columnIndex==2||columnIndex==3)
             return true;
         return false;
    }

   
    
}
