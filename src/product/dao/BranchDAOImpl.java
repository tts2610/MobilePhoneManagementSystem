/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package product.dao;


import database.DBProvider;
import database.IDAO;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.rowset.CachedRowSet;
import utility.SwingUtils;

/**
 *
 * @author tuan
 */
public class BranchDAOImpl implements IDAO<Branch> {
    private CachedRowSet dbCrs;           //CRS for update database
    private final CachedRowSet tableCrs;  //CRS for update table
    public BranchDAOImpl(){
        tableCrs = getCRS(Branch.Query_Show);
    }

    @Override
    public List<Branch> getList() {
        List<Branch> branchList = new ArrayList<>();
        try {
            
            if(tableCrs.first()){
                do{
                branchList.add(new Branch(
                tableCrs.getInt(Branch.COL_ID),
                tableCrs.getString(Branch.COL_Name),
                tableCrs.getBoolean(Branch.COL_Status),
                tableCrs.getString(Branch.COL_SupName),
                tableCrs.getInt(Branch.COL_SupID)
                ));
                }while(tableCrs.next());
            }
        } catch (SQLException ex) {
            Logger.getLogger(BranchDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return branchList;
    }

   

    @Override
    public boolean insert(Branch branch) {
        boolean result = false;
        
        try {
            CachedRowSet crs1 = getCRS("Select min(supid) as supid from suppliers");
        if(!crs1.first()){
            SwingUtils.showErrorDialog("Input at least 1 supplier!");
            return false;
        }
            
            runPS("Insert into branches(braname,braEnabled,SupID) values(?,?,?)",branch.getBraName(),branch.getBraStatus(),crs1.getInt("supid"));
           
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(BranchDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public boolean update(Branch branch) {
        boolean result = false;
        
        try {
            CachedRowSet crs1 = getCRS("SELECT * from Branches where braName = "+"'"+branch.getBraName()+"'"+"and braID !="+branch.getBraID());
            if (crs1.first()) {
                SwingUtils.showErrorDialog("Branch name cannot be duplicated !");
                return false;
            }
            System.err.println("eeee");
            runPS("Update Branches set BraName = "+"'"+branch.getBraName()+"'"+",BraEnabled = "+((branch.getBraStatus())?1:0)+",SupID = (select supid from Suppliers where supname="+"'"+branch.getSupName()+"'"+") where BraId = "+branch.getBraID());
            
            
            result = true;
            } catch (SQLException ex) {
            Logger.getLogger(BranchDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        return result;
    }
    

    @Override
    public boolean delete(Branch branch) {
        boolean result = false;
        
        try {
            CachedRowSet crs1 = getCRS("SELECT * from Products where braID ="+branch.getBraID());
            if (crs1.first()) {
                SwingUtils.showErrorDialog("Branch is now in Product table!");
                return false;
            }
            runPS("delete branches where braid=?",branch.getBraID());
            
                result = true;
            
        } catch (SQLException ex) {
            Logger.getLogger(BranchDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
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
