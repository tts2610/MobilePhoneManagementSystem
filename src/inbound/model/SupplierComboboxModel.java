/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inbound.model;

import branch.model.Branch;
import database.IDAO;
import inbound.model.SupplierDAOImpl;
import inbound.model.Supplier;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.rowset.CachedRowSet;
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
