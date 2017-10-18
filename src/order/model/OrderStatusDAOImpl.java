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
public class OrderStatusDAOImpl implements IDAO<OrderStatus> {

    private CachedRowSet crs;  //CRS to update table

    public OrderStatusDAOImpl() {
        crs = getCRS("select * from Status where SttType like 'Order'");
    }

    @Override
    public List<OrderStatus> getList() {
        List<OrderStatus> list = new ArrayList<>();
        try {
            if (crs.first()) {
                do {
                    list.add(new OrderStatus(
                            crs.getInt(OrderStatus.COL_ID),
                            crs.getString(OrderStatus.COL_NAME),
                            crs.getString(OrderStatus.COL_TYPE)));
                } while (crs.next());
            }
        } catch (SQLException ex) {
            Logger.getLogger(OrderStatusDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    @Override
    public boolean insert(OrderStatus model) {
        return false;
    }

    @Override
    public boolean update(OrderStatus model) {
        return false;
    }

    @Override
    public boolean delete(OrderStatus model) {
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
