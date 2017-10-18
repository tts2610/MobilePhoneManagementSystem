/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service.controller.backup;

import service.model.*;
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
public class ServiceDAOImpl implements IDAO<Service> {
    private CachedRowSet crs;  //CRS to update table
    private int selectingIndex;

    public  ServiceDAOImpl() {
        crs = getCRS("select a.ServiceID, b.UserName, a.ReceiveDate, a.ReturnDate, c.ServiceTypeName,s.SttName,a.UserID,a.ServiceTypeID,a.SttID from Service a join Users b on a.UserID=b.UserID join ServiceTypes c on a.ServiceTypeID=c.ServiceTypeID join Status s on a.SttID=s.SttID ORDER BY a.ReceiveDate DESC");
    }
    @Override
    public List<Service> getList() {
List<Service> list = new ArrayList<>();
        try {
            if (crs.first()) {               
                do {
                    list.add(new Service(
                            crs.getInt(Service.COL_SERID),
                            crs.getString(Service.COL_USERNAME),                            
                            crs.getDate(Service.COL_RECEIVEDATE),
                            crs.getDate(Service.COL_RETURNDATE),
                            crs.getString(Service.COL_SERTYPENAME),
                            crs.getString(Service.COL_SERSTATUS),
                            
                            crs.getInt(Service.COL_USERID),
                            crs.getInt(Service.COL_SERTYPEID),
                            crs.getInt(Service.COL_SERSTATUSID)
                    
                    ));
                } while (crs.next());
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServiceDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    @Override
    public boolean insert(Service model) {
        return false;
    }

    @Override
    public boolean update(Service model) {
        return false;
    }

    @Override
    public boolean delete(Service model) {
        boolean result = false;
        try {
            // Xoa data trong table ServiceDetails
            runPS("delete ServiceDetails where ServiceID=?", model.getSerID());
            // Xoa data trong table Service
            runPS("delete Service where ServiceID=?", model.getSerID());
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(ServiceDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
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
