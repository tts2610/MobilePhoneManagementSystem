/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package supplier.model;

import database.IDAO;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.rowset.CachedRowSet;

/**
 *
 * @author tuan
 */
public class SupplierBranchDAOImpl implements IDAO<SupplierBranch> {
    private CachedRowSet crs;
    
    public void load(int supid){
        crs = getCRS("SELECT * from branches where supid=? order by BraID DESC", supid);
    }
    @Override
    public List<SupplierBranch> getList() {
       List<SupplierBranch> branchList = new ArrayList<>();
        try {
            
            if (crs != null && crs.first()){
                do{
                branchList.add(new SupplierBranch(
                crs.getInt(SupplierBranch.COL_ID),
                crs.getString(SupplierBranch.COL_Name),
                crs.getBoolean(SupplierBranch.COL_Status),
                crs.getInt(SupplierBranch.COL_SupID)
                ));
                }while(crs.next());
            }
        } catch (SQLException ex) {
            Logger.getLogger(SupplierBranchDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return branchList;
    }

    @Override
    public boolean insert(SupplierBranch model) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean update(SupplierBranch model) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean delete(SupplierBranch model) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getSelectingIndex(int idx) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setSelectingIndex(int idx) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
