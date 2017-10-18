/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package outbound.model;


import database.DBProvider;
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
public class OutboundProductDAOImpl implements IDAO<OutboundProduct> {
    private CachedRowSet crs;  //CRS to update table
    
    public void load(int proid) {
        // Chi thao tac voi product dang enable
        crs = getCRS("select i.ProID, ProName, ProQty from OutDetails i Join Products p on p.ProID=i.ProID where OutID=?",proid);
    }
    
    
    
    
    
    
    @Override
    public List<OutboundProduct> getList() {
       List<OutboundProduct> list = new ArrayList<>();
        try {
            if (crs != null && crs.first()) {
                do {
                    list.add(new OutboundProduct(
                            crs.getInt(OutboundProduct.COL_PROID),
                            crs.getString(OutboundProduct.COL_PRONAME),
                            crs.getInt(OutboundProduct.COL_PROQTY)));
                } while (crs.next());
            }
        } catch (SQLException ex) {
            Logger.getLogger(OutboundProductDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    @Override
    public boolean insert(OutboundProduct model) {
        return false;
    }
    
 
    @Override
    public boolean update(OutboundProduct model) {
        return false;
    }

    @Override
    public boolean delete(OutboundProduct model) {
        return false;
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
