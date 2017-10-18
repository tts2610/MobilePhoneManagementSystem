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
 * Dung cho COMBOBOX cua column product trong table product list cua 
 * OrderDialog.
 *
 * @author Hoang
 */
public class OrderProductDAOImplComboBox implements IDAO<OrderProduct> {

    private CachedRowSet crs;  //CRS to update table
    private int selectingIndex;

    public OrderProductDAOImplComboBox() {
        // Chi thao tac voi product dang enable
        crs = getCRS("select ProID, p.BraID, BraName, ProName, ProStock, ProPrice, SalesOffAmount from Products p join Branches b on p.BraID=b.BraID left join SalesOff s on p.SalesOffID=s.SalesOffID where ProEnabled=1 order by BraName");
    }

    @Override
    public List<OrderProduct> getList() {
        List<OrderProduct> list = new ArrayList<>();
        try {
            if (crs != null && crs.first()) {
                do {
                    list.add(new OrderProduct(
                            crs.getInt(OrderProduct.COL_PROID),
                            crs.getString(OrderProduct.COL_PRONAME),
                            0,
                            crs.getFloat(OrderProduct.COL_PROPRICE1),
                            crs.getFloat(OrderProduct.COL_SALEAMOUNT),
                            // Price2 = Price1*(1-SalesOff)                           
                            crs.getFloat(OrderProduct.COL_PROPRICE1) * (1 - crs.getFloat(OrderProduct.COL_SALEAMOUNT)),
                            0,
                            0,
                            crs.getString(OrderProduct.COL_BRANAME),
                            crs.getInt(OrderProduct.COL_BRAID),
                            crs.getInt(OrderProduct.COL_PROSTOCK)));
                } while (crs.next());
            }
        } catch (SQLException ex) {
            Logger.getLogger(OrderProductDAOImplComboBox.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    @Override
    public boolean insert(OrderProduct model) {
        return false;

    }

    @Override
    public boolean update(OrderProduct model) {
        return false;

    }

    @Override
    public boolean delete(OrderProduct model) {
        return false;
    }

    @Override
    public int getSelectingIndex(int idx) {
        return selectingIndex;
    }

    @Override
    public void setSelectingIndex(int idx) {
        selectingIndex = idx;
    }

}
