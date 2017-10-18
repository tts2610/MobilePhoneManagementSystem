/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inbound.model;

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
public class InboundProductDAOImpl implements IDAO<InboundProduct> {
    private CachedRowSet crs;  //CRS to update table
    private Inbound currentInbound;
    public void load(int proid) {
        // Chi thao tac voi product dang enable
        crs = getCRS("select i.ProID, ProName, ProQty ,ProCost from InDetails i Join Products p on p.ProID=i.ProID where InID=?",proid);
    }
    
    
    
    
    
    
    @Override
    public List<InboundProduct> getList() {
       List<InboundProduct> list = new ArrayList<>();
        try {
            if (crs != null && crs.first()) {
                do {
                    list.add(new InboundProduct(
                            crs.getInt(InboundProduct.COL_PROID),
                            crs.getString(InboundProduct.COL_PRONAME),
                            crs.getFloat(InboundProduct.COL_PROCOST),
                            crs.getInt(InboundProduct.COL_PROQTY)));
                } while (crs.next());
            }
        } catch (SQLException ex) {
            Logger.getLogger(InboundProductDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    @Override
    public boolean insert(InboundProduct model) {
        return false;
    }
    
 
    @Override
    public boolean update(InboundProduct model) {
        return false;
    }

    @Override
    public boolean delete(InboundProduct model) {
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
