/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package order.model;

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
public class OrderBranchDAOImpl implements IDAO<OrderBranch> {

    private CachedRowSet crs;

    public OrderBranchDAOImpl() {
        crs = getCRS("select * from Branches where BraEnabled=1");
    }

    @Override
    public List<OrderBranch> getList() {
        List<OrderBranch> list = new ArrayList<>();
        try {
            if (crs != null && crs.first()) {
                do {
                    list.add(new OrderBranch(
                            crs.getInt(OrderBranch.COL_ID),
                            crs.getString(OrderBranch.COL_NAME),
                            crs.getBoolean(OrderBranch.COL_ENABLED)));
                } while (crs.next());
            }
        } catch (SQLException ex) {
            Logger.getLogger(OrderCustomerDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    @Override
    public boolean insert(OrderBranch model) {
        return true;
    }

    @Override
    public boolean update(OrderBranch model) {
        return true;
    }

    @Override
    public boolean delete(OrderBranch model) {
        return true;
    }

    @Override
    public int getSelectingIndex(int idx) {
        return 0;
    }

    @Override
    public void setSelectingIndex(int idx) {
    }

}
