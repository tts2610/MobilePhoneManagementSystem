/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package salesoff.model;

import database.IDAO;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.rowset.CachedRowSet;

/**
 *
 * @author Hoang
 */
public class SalesOffProductDAOImpl implements IDAO<SalesOffProduct> {

    private CachedRowSet crs;  //CRS to update table

    /**
     * Dung load du lieu theo yeu cau, khong tu dong load
     *
     * @param salesOffID
     */
    public void load(int salesOffID) {
        // Chi thao tac voi product dang enable
        crs = getCRS("select ProID, ProName, BraName, SalesOffID from Products a join Branches b on a.BraID=b.BraID where ProEnabled=1 AND (SalesOffID is null OR SalesOffID=?)", salesOffID);
    }

    @Override
    public List<SalesOffProduct> getList() {
        List<SalesOffProduct> list = new ArrayList<>();
        try {
            if (crs != null && crs.first()) {
                do {
                    list.add(new SalesOffProduct(
                            crs.getInt(SalesOffProduct.COL_PROID),
                            crs.getString(SalesOffProduct.COL_PRONAME),
                            crs.getString(SalesOffProduct.COL_BRANAME),
                            //Lay kieu boolean cho column SalesOff
                            crs.getInt(SalesOffProduct.COL_SALEID) != 0,
                            crs.getInt(SalesOffProduct.COL_SALEID)));
                } while (crs.next());
            }
        } catch (SQLException ex) {
            Logger.getLogger(SalesOffProductDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    @Override
    public boolean insert(SalesOffProduct salesOffProduct) {
        return false;
    }

    @Override
    public boolean update(SalesOffProduct salesOffProduct) {
        boolean result = false;

        try {
            // Neu co check vao o sales off
            if (salesOffProduct.getSaleID() != 0) {
                runPS("update Products set SalesOffID=? where ProID=?", salesOffProduct.getSaleID(), salesOffProduct.getProID());
            } else { // Neu khong check, quat null
                runPS("update Products set SalesOffID=null where ProID=?", salesOffProduct.getProID());
            }

            // Refresh lai cachedrowset hien thi table
            crs.execute();
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(SalesOffProductDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result;
    }

    @Override
    public boolean delete(SalesOffProduct salesOffProduct) {
        return false;
    }

    @Override
    public int getSelectingIndex(int idx) {
        return 0;
    }

    @Override
    public void setSelectingIndex(int idx) {
    }
}
