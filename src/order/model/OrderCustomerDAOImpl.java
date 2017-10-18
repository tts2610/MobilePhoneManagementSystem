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
public class OrderCustomerDAOImpl implements IDAO<OrderCustomer> {

    private CachedRowSet crs;  //CRS to update table
    private int selectingIndex;

    public OrderCustomerDAOImpl() {
        // Chi load customer dang duoc enable
        crs = getCRS("select a.CusID, CusName, CusPhone, CusAddress, CusDiscount, a.CusLevelID from Customers a join CustomerLevels b on a.CusLevelID=b.CusLevelID where CusEnabled=1");
    }

    @Override
    public List<OrderCustomer> getList() {
        List<OrderCustomer> list = new ArrayList<>();
        try {
            if (crs != null && crs.first()) {
                do {
                    list.add(new OrderCustomer(
                            crs.getInt(OrderCustomer.COL_ID),
                            crs.getString(OrderCustomer.COL_NAME),
                            crs.getString(OrderCustomer.COL_PHONE),
                            crs.getString(OrderCustomer.COL_ADDRESS),
                            crs.getFloat(OrderCustomer.COL_DISCOUNT),
                            crs.getInt(OrderCustomer.COL_LEVELID)));
                } while (crs.next());
            }
        } catch (SQLException ex) {
            Logger.getLogger(OrderCustomerDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    @Override
    public boolean insert(OrderCustomer model) {
        return false;
    }

    @Override
    public boolean update(OrderCustomer model) {
        return false;
    }

    @Override
    public boolean delete(OrderCustomer model) {
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
