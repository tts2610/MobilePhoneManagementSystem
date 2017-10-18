/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package branch.model;



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
        
        return result;
    }

    @Override
    public boolean update(Supplier supplier) {
        boolean result = false;
        
        
        return result;
    }

    @Override
    public boolean delete(Supplier model) {
        boolean result = false;
        
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
