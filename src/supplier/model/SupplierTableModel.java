/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package supplier.model;

import java.util.List;
import supplier.dao.SupplierDAOImpl;
import utility.CustomizedTableModel;

/**
 *
 * @author tuan
 */
public class SupplierTableModel extends CustomizedTableModel<Supplier> {
    
    

    public SupplierTableModel() {
        super(new SupplierDAOImpl(),new String[]{"Supplier ID","Supplier Name","Supplier Address","Status"});
        
    }
//    public boolean insert(Supplier supplier) {
//        boolean result = false;
//        if (supplierDAOImpl.insert(supplier)) {
//            supplierList = supplierDAOImpl.getList();
//            fireTableRowsInserted(supplierList.indexOf(supplier), supplierList.indexOf(supplier));
//            result = true;
//        }
//        return result;
//    }
//    
//    public boolean update(Supplier supplier) {
//        boolean result = false;
//        if (supplierDAOImpl.update(supplier)) {
//            supplierList = supplierDAOImpl.getList();
//            
//            result = true;
//        }
//        return result;
//    }
//    
//    public boolean delete(Supplier supplier) {
//        boolean result = false;
//        if (supplierDAOImpl.delete(supplier)) {
//            supplierList = supplierDAOImpl.getList();
//            fireTableRowsDeleted(supplierList.indexOf(supplier), supplierList.indexOf(supplier));
//            result = true;
//        }
//        return result;
//    }

   

    @Override
    public Class<?> getColumnClass(int columnIndex) {
         Class[] columnClasses = {Integer.class, String.class, String.class, Boolean.class};
        return columnClasses[columnIndex];
    }
    
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Supplier supplier = list.get(rowIndex);
         switch(columnIndex){
             case 0 : return supplier.getSupID();
             case 1 : return supplier.getSupName();
             case 2 : return supplier.getSupAddress();
             case 3 : return supplier.getSupStatus();
         }
         return null;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
         Supplier supplier = list.get(rowIndex);
         switch(columnIndex){
             case 0 : supplier.setSupID((int) aValue); break;
             case 1 : supplier.setSupName((String) aValue); break;
             case 2 : supplier.setSupAddress((String) aValue); break;
             case 3 : supplier.setSupStatus((boolean) aValue); break;
         }
         fireTableCellUpdated(rowIndex, columnIndex);
    }
    
}
