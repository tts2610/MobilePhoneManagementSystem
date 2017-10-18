/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package product.dao;


import database.DBProvider;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.rowset.CachedRowSet;
import javax.swing.DefaultComboBoxModel;
import utility.CustomizedComboBoxModel;
/**
 *
 * @author tuan
 */
public class BranchNameComboBoxModel extends CustomizedComboBoxModel<Branch> {
    private CachedRowSet crs;

    public BranchNameComboBoxModel() {
        super(new BranchDAOImpl());
//        crs = new DBProvider().getCRS("select * from Branches");
//        try {
//            if (crs.first()) {
//                do {
//                    addElement(new Branch(
//                            crs.getInt(Branch.COL_ID), 
//                            
//                            crs.getString(Branch.COL_Name),
//                            crs.getBoolean(Branch.COL_Status)));
//                } while (crs.next());
//            }
//        } catch (SQLException ex) {
//            Logger.getLogger(BranchNameComboBoxModel.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }
    
    public int getIndexOfBranch(int branch){
        int result = -1;
        
        for (int i=0; i< crs.size(); i++) {
            if(getElementAt(i).getBraID()==branch){
                result = i;
                break;
            }
        }
        return result;
    }
    
    public Branch getBranchFromValue(String braName) {
        Branch result = null;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getBraName().equals(braName)) {
                result = list.get(i);
                break;
            }
        }
        return result;
    }
    
}
