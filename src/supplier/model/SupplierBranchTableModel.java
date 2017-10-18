/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package supplier.model;

import database.IDAO;
import utility.CustomizedTableModel;

/**
 *
 * @author tuan
 */
public class SupplierBranchTableModel extends CustomizedTableModel<SupplierBranch> {

    public SupplierBranchTableModel() {
        super(new SupplierBranchDAOImpl(), new String[]{"ID","Branch Name","Status"});
    }

    public void load(int proID) {
        ((SupplierBranchDAOImpl) super.daoImpl).load(proID);
        list = daoImpl.getList();
        fireTableDataChanged();
    }
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }
    
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        SupplierBranch branch = list.get(rowIndex);
         switch(columnIndex){
             case 0 : return branch.getBraID();
             case 1 : return branch.getBraName();
             case 2 : return branch.isBraStatus();
             case 3 : return branch.getSupName();
             case 4 : return branch.getSupID();
         }
         return null;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
       SupplierBranch branch = list.get(rowIndex);
         switch(columnIndex){
             case 0 : branch.setBraID((int) aValue); break;
             case 1 : branch.setBraName((String) aValue); break;
             case 2 : branch.setBraStatus((boolean) aValue); break;
             case 3 : branch.setSupName((String) aValue);break;
             case 4 : branch.setSupID((int) aValue);break;
         }
         fireTableCellUpdated(rowIndex, columnIndex);
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        Class[] columnClasses = {Integer.class, String.class, Boolean.class};
        return columnClasses[columnIndex];
    }
    
}
