/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service.model;

import database.IDAO;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.rowset.CachedRowSet;

/**
 *
 * @author BonBon
 */
public class ServiceTypeDAOImpl implements IDAO<ServiceType> {

    private CachedRowSet crs;  //CRS to update table

    public ServiceTypeDAOImpl() {
        crs = getCRS("select * from ServiceTypes ");
    }

    @Override
    public List<ServiceType> getList() {
        List<ServiceType> list = new ArrayList<>();
        try {
            if (crs.first()) {
                do {
                    list.add(new ServiceType(
                            crs.getInt(ServiceType.COL_ID),
                            crs.getString(ServiceType.COL_NAME)));
                } while (crs.next());
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServiceTypeDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    @Override
    public boolean insert(ServiceType model) {
        return false;
    }

    @Override
    public boolean update(ServiceType model) {
        return false;
    }

    @Override
    public boolean delete(ServiceType model) {
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
