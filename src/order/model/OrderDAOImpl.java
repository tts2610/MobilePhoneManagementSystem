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
import utility.SwingUtils;

/**
 *
 * @author Hoang
 */
public class OrderDAOImpl implements IDAO<Order> {

    private CachedRowSet crs;  //CRS to update table

    public OrderDAOImpl() {
        crs = getCRS("select o.OrdID, u.UserName, c.CusName, OrdDate, OrdCusDiscount, s.SttName, o.UserID, o.CusID, o.SttID, tmp*(1-OrdCusDiscount) OrdValue from Orders o join Users u on o.UserID=u.UserID join Customers c on o.CusID=c.CusID join Status s on o.SttID=s.SttID left join (select OrdID, sum(OrdProQty*OrdProPrice) tmp from OrderDetails group by OrdID) tmp on o.OrdID=tmp.OrdID");
    }

    @Override
    public List<Order> getList() {
        List<Order> list = new ArrayList<>();
        try {
            if (crs.first()) {
                do {
                    list.add(new Order(
                            crs.getInt(Order.COL_ORDID),
                            crs.getString(Order.COL_USERNAME),
                            crs.getString(Order.COL_CUSNAME),
                            crs.getDate(Order.COL_ORDDATE),
                            crs.getFloat(Order.COL_CUSDISCOUNT),
                            crs.getString(Order.COL_ORDSTATUS),
                            crs.getInt(Order.COL_USERID),
                            crs.getInt(Order.COL_CUSID),
                            crs.getInt(Order.COL_ORDSTATUSID),
                            crs.getFloat(Order.COL_ORDVALUE)));
                } while (crs.next());
            }
        } catch (SQLException ex) {
            Logger.getLogger(OrderStatusDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    @Override
    public boolean insert(Order model) {
        return false;
    }

    @Override
    public boolean update(Order model) {
        return false;

    }

    @Override
    public boolean delete(Order model) {
        boolean result = false;
        try {
            // Check trang thai order, neu la "Done" thi khong cho xoa
            if(model.getOrdStatus().equals("Done")){
                SwingUtils.showErrorDialog("Cannot delete order which has been done !");
                return result;
            }
            
            // Check neu order dang trong service thi khong cho xoa
            CachedRowSet crs2 = getCRS("select OrdID from ServiceDetails where OrdID=?", model.getOrdID());
            if(crs2.first()){
                 SwingUtils.showErrorDialog("Order is in Service !");
                return result;               
            }
            
            // Xoa data trong table OrderDetails
            runPS("delete OrderDetails where OrdID=?", model.getOrdID());
            // Xoa data trong table Orders
            runPS("delete Orders where OrdID=?", model.getOrdID());
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(OrderDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;

    }

    @Override
    public int getSelectingIndex(int idx) {
        return 0;
    }

    @Override
    public void setSelectingIndex(int idx) {
    }

}
