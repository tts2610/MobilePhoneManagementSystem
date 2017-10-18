/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package branch.model;


import database.IDAO;
import utility.CustomizedComboBoxModel;

/**
 *
 * @author tuan
 */
public class SupplierComboboxModel extends CustomizedComboBoxModel<Supplier> {

    public SupplierComboboxModel() {
        super(new SupplierDAOImpl());
    }
    
    public Supplier getSupplierFromID(int supID) {
        Supplier result = null;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getSupID()==supID) {
                result = list.get(i);
                break;
            }
        }
        return result;
    }
    
    public Supplier getSupplierFromValue(String supName) {
        Supplier result = null;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getSupName().equals(supName)) {
                result = list.get(i);
                break;
            }
        }
        return result;
    }
    
}
