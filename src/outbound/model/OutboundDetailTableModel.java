/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package outbound.model;


import java.util.List;


import utility.CustomizedTableModel;
import utility.SwingUtils;

/**
 *
 * @author tuan
 */
public class OutboundDetailTableModel extends CustomizedTableModel<OutboundDetail> {

    public OutboundDetailTableModel() {
        super(new OutboundDetailDAOImpl(), new String[]{"ProID","ProName","ProQty"});
    }
    public List<OutboundDetail> getList() {
        return list;
    }
    
    public OutboundDetail getInboundDetailFromIndex(int index) {
        return list.get(index);
    }
    public void load(int inID) {
        ((OutboundDetailDAOImpl) super.daoImpl).load(inID);
        list = daoImpl.getList();
        fireTableDataChanged();
    }
    @Override
    public Class<?> getColumnClass(int columnIndex) {
         Class[] columnClass = {Integer.class,String.class,Float.class,Integer.class};
         return columnClass[columnIndex];
    }

    
    
    @Override
    public boolean insert(OutboundDetail item) {
         if(checkDuplicateInbound(item)){
            SwingUtils.showInfoDialog("Dupplicate product list!");
             return false;
         }
         
         else if(item.getProQty()==0){
             SwingUtils.showInfoDialog("Cannot deliver product due to 0 quantity!");
             return false;
         }
         else{
             item.setProQty(1);//set lai proQty neu proQty k bang 0
             list.add(item);
            fireTableRowsInserted(list.size() - 1, list.size() - 1);
             
         }
         return true;
    }
    
    public boolean checkDuplicateInbound(OutboundDetail inbound){
        for(OutboundDetail i : list){
            if(i.getProID()==inbound.getProID())
                return true;
        }
        return false;
    }

    @Override
    public boolean delete(OutboundDetail item) {
        
         list.remove(selectingIndex);
         
         fireTableRowsDeleted(selectingIndex, selectingIndex);
        return true;
    }

    @Override
    public boolean update(OutboundDetail item) {
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
                case 2:result = item.getProQty();break;
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
             case 2: item.setProQty((int) aValue);break;
             
         }
         fireTableCellUpdated(rowIndex, columnIndex);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
         if(columnIndex==1||columnIndex==0)
             return false;
         return true;
    }
    
    
}
