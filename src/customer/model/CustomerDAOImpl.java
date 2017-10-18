/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.model;

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
public class CustomerDAOImpl implements IDAO<Customer> {

    private CachedRowSet crs;   //CRS to update table

    public CustomerDAOImpl() {
        // Luu y Stt=2 co nghia la chi tinh cus paid cua cac order da thanh toan (status="Paid"), neu order chua thanh thoan thi cus paid la null (0).
        crs = getCRS("select a.CusID, t3.CusPaid, CusName, CusLevel, CusLevelName, CusPhone, CusAddress, CusEnabled, a.CusLevelID from Customers a join CustomerLevels b on a.CusLevelID=b.CusLevelID left join (select CusID, sum(OrdValue) CusPaid from Orders t1 left join (select OrdID, sum(OrdProQty*OrdProPrice) OrdValue from OrderDetails where OrdID in (select OrdID from Orders where SttID=2) group by OrdID) t2 on t1.OrdID=t2.OrdID group by CusID) t3 on a.CusID=t3.CusID");
    }

    @Override
    public List<Customer> getList() {
        List<Customer> customerList = new ArrayList<>();
        try {
            if (crs.first()) {
                do {
                    customerList.add(new Customer(
                            crs.getInt(Customer.COL_CUSID),
                            crs.getString(Customer.COL_CUSNAME),
                            crs.getFloat(Customer.COL_CUSPAID),
                            crs.getInt(Customer.COL_CUSLEVEL),
                            crs.getString(Customer.COL_CUSLEVELNAME),
                            crs.getString(Customer.COL_CUSPHONE),
                            crs.getString(Customer.COL_CUSADDRESS),
                            crs.getBoolean(Customer.COL_CUSENABLED),
                            crs.getInt(Customer.COL_CUSLEVELID)));
                } while (crs.next());
            }
        } catch (SQLException ex) {
            Logger.getLogger(CustomerDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return customerList;
    }

    @Override
    public boolean insert(Customer customer) {
        boolean result = false;

        try {
            runPS("insert into Customers(CusName, CusLevelID, CusPhone, CusAddress, CusEnabled) values(?,?,?,?,?)",
                    customer.getCusName(),
                    customer.getCusLevelID(),
                    customer.getCusPhone(),
                    customer.getCusAddress(),
                    customer.isCusEnabled()
            );

            // Refresh lai cachedrowset hien thi table
            crs.execute();
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(CustomerDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public boolean update(Customer customer) {
        boolean result = false;
        try {
            // Check cus phone khong duoc trung
            CachedRowSet crs2 = getCRS("select * from Customers where CusPhone like ? AND CusID!=?", customer.getCusPhone(), customer.getCusID());
            if (crs2.first()) {
                SwingUtils.showErrorDialog("Customer phone cannot be duplicated !");
            } else {
                runPS("update Customers set CusName=?, CusLevelID=?, CusPhone=?, CusAddress=?, CusEnabled=? where CusID=?",
                        customer.getCusName(),
                        customer.getCusLevelID(),
                        customer.getCusPhone(),
                        customer.getCusAddress(),
                        customer.isCusEnabled(),
                        customer.getCusID()
                );

                // Refresh lai cachedrowset hien thi table
                crs.execute();
                result = true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(CustomerDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public boolean delete(Customer customer) {
        boolean result = false;
        try {
            //Check customer co order hoac service khong, neu co thi khong cho delete
            CachedRowSet crs1 = getCRS("select * from Orders where CusID=?", customer.getCusID());
            CachedRowSet crs2 = getCRS("select * from Service where CusID=?", customer.getCusID());
            if (crs1.first()) {
                SwingUtils.showErrorDialog("Customer is now in ORDER !");
            } else if (crs2.first()) {
                SwingUtils.showErrorDialog("Customer is now in SERVICE !");
            } else {
                runPS("delete from Customers where CusID=?", customer.getCusID());

                // Refresh lai cachedrowset hien thi table
                crs.execute();
                result = true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(CustomerDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
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
