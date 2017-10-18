/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package branch.model;

import branch.dao.BranchDAOImpl;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import utility.CustomizedTableModel;

/**
 *
 * @author tuan
 */
public class BranchTableModel extends CustomizedTableModel<Branch> {
    

    
    public BranchTableModel(){
        super(new BranchDAOImpl(),new String[]{"ID","Branch Name","Status","Supplier name"});
        
    }
    
//    public boolean insert(Branch branch) {
//        boolean result = false;
//        if (branchDAOImpl.insert(branch)) {
//            branchList = branchDAOImpl.getList();
//            fireTableRowsInserted(branchList.indexOf(branch), branchList.indexOf(branch));
//            result = true;
//        }
//        return result;
//    }
//    
//     public boolean update(Branch branch) {
//        boolean result = false;
//        if (branchDAOImpl.update(branch)) {
//            branchList = branchDAOImpl.getList();
//            
//            result = true;
//        }
//        return result;
//    }
//    
//    public boolean delete(Branch branch) {
//        boolean result = false;
//        if (branchDAOImpl.delete(branch)) {
//            branchList = branchDAOImpl.getList();
//            fireTableRowsDeleted(branchList.indexOf(branch), branchList.indexOf(branch));
//            result = true;
//        }
//        return result;
//    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
         if (columnIndex == 0) { //Khong cho sua column ID
            return false;
        } else {
            return true;
        }
    }

   

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        Class[] columnClasses = {Integer.class, String.class, Boolean.class,String.class};
        return columnClasses[columnIndex];
    }
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
         Branch branch = list.get(rowIndex);
         switch(columnIndex){
             case 0 : return branch.getBraID();
             case 1 : return branch.getBraName();
             case 2 : return branch.getBraStatus();
             case 3 : return branch.getSupName();
             case 4 : return branch.getSupID();
         }
         return null;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
         Branch branch = list.get(rowIndex);
         switch(columnIndex){
             case 0 : branch.setBraID((int) aValue); break;
             case 1 : branch.setBraName((String) aValue); break;
             case 2 : branch.setBraStatus((boolean) aValue); break;
             case 3 : branch.setSupName(((Supplier) aValue).getSupName());break;
         }
         fireTableCellUpdated(rowIndex, columnIndex);
    }
    
    
    
}
