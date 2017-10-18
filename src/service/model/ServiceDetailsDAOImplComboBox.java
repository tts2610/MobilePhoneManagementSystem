/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service.model;

import order.model.*;
import database.IDAO;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.rowset.CachedRowSet;

/**
 *
 * @author BonBon
 */
public class ServiceDetailsDAOImplComboBox implements IDAO<ServiceDetails> {

    private CachedRowSet crs;  //CRS to update table
    private int selectingIndex;

    public ServiceDetailsDAOImplComboBox() {
        // Chi thao tac voi product dang enable
        crs = getCRS("select p.ProID,p.ProName,b.BraName,b.BraID from Products p join Branches b on b.BraID=p.BraID where ProEnabled=1 ORDER BY b.BraName");
    }

    @Override
    public List<ServiceDetails> getList() {
        List<ServiceDetails> list = new ArrayList<>();
        try {
            if (crs != null && crs.first()) {
                do {
                   
                    list.add(new ServiceDetails(
                            1,
                            crs.getString(ServiceDetails.COL_PRONAME),
                            crs.getString(ServiceDetails.COL_BRANCH),
                            "NewDetails",                            
                            1,
                            0,
                            0,
                            crs.getInt(ServiceDetails.COL_PROID),
                            crs.getInt(ServiceDetails.COL_BRAID)
                    ));
//                    System.out.println(list.get(0).toString());
                } while (crs.next());
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServiceDetailsDAOImplComboBox.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    @Override
    public boolean insert(ServiceDetails model) {
        return false;

    }

    @Override
    public boolean update(ServiceDetails model) {
        return false;

    }

    @Override
    public boolean delete(ServiceDetails model) {
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
