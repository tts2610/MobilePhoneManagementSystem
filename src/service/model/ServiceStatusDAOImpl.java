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
public class ServiceStatusDAOImpl implements IDAO<ServiceStatus> {

    private CachedRowSet crs;  //CRS to update table

    public ServiceStatusDAOImpl() {
        crs = getCRS("select * from Status where SttType like 'Service'");
    }

    @Override
    public List<ServiceStatus> getList() {
        List<ServiceStatus> list = new ArrayList<>();
        try {
            if (crs.first()) {
                do {
                    list.add(new ServiceStatus(
                            crs.getInt(ServiceStatus.COL_ID),
                            crs.getString(ServiceStatus.COL_NAME),
                            crs.getString(ServiceStatus.COL_TYPE)));
                } while (crs.next());
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServiceStatusDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    @Override
    public boolean insert(ServiceStatus model) {
        return false;
    }

    @Override
    public boolean update(ServiceStatus model) {
        return false;
    }

    @Override
    public boolean delete(ServiceStatus model) {
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
