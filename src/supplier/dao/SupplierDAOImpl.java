/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package supplier.dao;

import database.IDAO;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.rowset.CachedRowSet;
import supplier.model.Supplier;
import static supplier.model.Supplier.COL_Address;
import static supplier.model.Supplier.COL_ID;
import static supplier.model.Supplier.COL_Name;
import static supplier.model.Supplier.COL_Status;
import utility.SwingUtils;

/**
 *
 * @author tuan
 */
public class SupplierDAOImpl implements IDAO<Supplier> {
    private CachedRowSet crs;           //CRS for update database
    

    public SupplierDAOImpl() {
        this.crs =  getCRS(Supplier.Query_Show);
    }
    
    @Override
    public List<Supplier> getList() {
         List<Supplier> supplierList = new ArrayList<>();
        try {
            
            if(crs.first()){
                do{
                supplierList.add(new Supplier(
                crs.getInt(Supplier.COL_ID),
                crs.getString(Supplier.COL_Name),
                crs.getString(Supplier.COL_Address),
                crs.getBoolean(Supplier.COL_Status)
                ));
                }while(crs.next());
            }
        } catch (SQLException ex) {
            Logger.getLogger(SupplierDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return supplierList;
    }

    

    @Override
    public boolean insert(Supplier supplier) {
        boolean result = false;
        
        try {
            runPS("insert into suppliers values(?,?,?)", supplier.getSupName(),supplier.getSupAddress(), supplier.getSupStatus());
           
            
            // Refresh lai cachedrowset hien thi table
            crs.execute();
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(SupplierDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public boolean update(Supplier supplier) {
        boolean result = false;
       
        try {
            CachedRowSet crs1 = getCRS("SELECT * from suppliers where supname=? and supid!=?", supplier.getSupName(),supplier.getSupID());
            if(crs1.first()){
                SwingUtils.showInfoDialog("Dupplicate supplier name!");
                return false;
            }
            CachedRowSet crs2 = getCRS("SELECT * from suppliers where supaddress=? and supid!=?",supplier.getSupAddress(),supplier.getSupID());
            if(crs2.first()){
                SwingUtils.showInfoDialog("Dupplicate supplier address!");
                return false;
            }
            
           
            
            runPS("Update Suppliers set SupName=?,SupAddress=?,SupEnabled=? where SupID=?", supplier.getSupName(),supplier.getSupAddress(),supplier.getSupStatus(),supplier.getSupID());
            
            result = true;
            } catch (SQLException ex) {
            Logger.getLogger(SupplierDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        return result;
    }

    @Override
    public boolean delete(Supplier model) {
        boolean result = false;
        
        try {
            CachedRowSet crs1 = getCRS("SELECT * from branches where supid=?", model.getSupID());
            if(crs1.first()){
                SwingUtils.showInfoDialog("Supplier now associate with branch!");
                return false;
            }
            CachedRowSet crs2 = getCRS("SELECT * from inbounds where supid=?", model.getSupID());
            if(crs2.first()){
                SwingUtils.showInfoDialog("Supplier now associate with inbound!");
                return false;
            }
            runPS("Delete suppliers where supid=?",model.getSupID());
                result = true;
            
        } catch (SQLException ex) {
            Logger.getLogger(SupplierDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
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
