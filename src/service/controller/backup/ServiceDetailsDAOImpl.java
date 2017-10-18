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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.rowset.CachedRowSet;
import utility.SwingUtils;

/**
 *
 * @author BonBon
 */
public class ServiceDetailsDAOImpl implements IDAO<ServiceDetails> {

    private CachedRowSet crs;  //CRS to update table
    private int selectingIndex;
    private Service currentService;

    /**
     * Dung load du lieu theo yeu cau, khong tu dong load
     *
     * @param serID
     */
    public void load(int serID) {
//    public ServiceDetailsDAOImpl(){
        crs = getCRS("select a.ServiceID,p.ProName,b.BraName,a.ServiceContent,a.ProQty,a.OrdID,a.ServiceCost,a.ProID,p.BraID from ServiceDetails a join Products p on a.ProID=p.ProID left join Branches b on p.BraID=b.BraID WHERE a.ServiceID=?", serID);

    }

    @Override
    public List<ServiceDetails> getList() {
        List<ServiceDetails> list = new ArrayList<>();
        try {
            if (crs != null && crs.first()) { // Neu table co data (update mode)                

                do {
                    list.add(new ServiceDetails(
                            crs.getInt(ServiceDetails.COL_ID),
                            crs.getString(ServiceDetails.COL_PRONAME),
                            crs.getString(ServiceDetails.COL_BRANCH),
                            crs.getString(ServiceDetails.COL_CONTENT),
                            crs.getInt(ServiceDetails.COL_QUANTITY),
                            crs.getInt(ServiceDetails.COL_ORDERID),//!=0?crs.getInt(ServiceDetails.COL_ORDERID):0,
                            crs.getInt(ServiceDetails.COL_COST),
                            crs.getInt(ServiceDetails.COL_PROID),
                            crs.getInt(ServiceDetails.COL_BRAID)
                    ));
                } while (crs.next());
            } else { // Neu table khong co data (insert mode)
                ServiceDetails op = new ServiceDetails();
//                op.setBraName("");
                op.setProName(ServiceDetails.DEFAULT_PRONAME);
                op.setProQty(0);
                op.setSerCost(0);
                list.add(op);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServiceDetailsDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    @Override
    public boolean insert(ServiceDetails model) {

        return false;
    }

    public int checkOrdIDInTable(int id) {
        int result = 0;
        if (id == 0) { // oderID null
            return 0;
        } else {
            try {
                CachedRowSet crs3 = getCRS("select OrdID,OrdDate from Orders where OrdID=? AND SttID=2", id);
                if (crs3.first()) {
                    Date ordDate = crs3.getDate("OrdDate");
                    Date now = new Date();
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(ordDate);
                    cal.add(Calendar.YEAR, +1);
                    Date warranty = cal.getTime();
                    if (warranty.after(now)) {
                        result = 2;
                    } else {
                        result = 1;
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(ServiceDetailsDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        return result;
    }

    public boolean insert(List<ServiceDetails> list) {
        boolean result = false;
        if (currentService == null) { // Chua set current service cho DAO
            return false;
        }

        try {
//            System.out.println("CurentService: " + currentService.toString());
            // Insert table Service
            runPS("insert Service(UserID, ReceiveDate,ReturnDate, ServiceTypeID, SttID) values((select UserID from Users where UserName=?),?,?,?,?)", currentService.getUserName(), currentService.getReceiveDate(), currentService.getReturnDate(), currentService.getSerTypeID(), currentService.getSerStatusID());
            // Lay serID sau khi insert table Sevice
            CachedRowSet crs2 = getCRS("select top(1) ServiceID from Service order by ServiceID DESC");
            crs2.first();

            currentService.setSerID(crs2.getInt("ServiceID"));

            // Insert table ServiceDetails
            for (ServiceDetails op : list) {
//                System.out.println("ServiceID :" + currentService.getSerID());
//                System.out.println("LIST Insert: " + list.toString());
                if (op.getOrdID() == 0) {
                    runPS("insert ServiceDetails(ServiceID, ProID, ServiceContent,ProQty,ServiceCost) values(?,?,?,?,?)", currentService.getSerID(), op.getProID(), op.getSerContent(), op.getProQty(), op.getSerCost());
                } else {
                    runPS("insert into ServiceDetails(ServiceID, ProID, ServiceContent,ProQty, OrdID,ServiceCost) values(?,?,?,?,?,?)", currentService.getSerID(), op.getProID(), op.getSerContent(), op.getProQty(), op.getOrdID(), op.getSerCost());
                }
            }
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(ServiceDetailsDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public void setCurrentService(Service currentService) {
        this.currentService = currentService;
    }

    public boolean update(List<ServiceDetails> list) {
        boolean result = false;
        if (currentService == null) { // Chua set current order cho DAO
            return false;
        }

        try {
            // Update table Service
            runPS("update Service set UserID=(select UserID from Users where UserName=?), ReceiveDate=?,ReturnDate=?, ServiceTypeID=?, SttID=? where ServiceID=? ", currentService.getUserName(), currentService.getReceiveDate(), currentService.getReturnDate(), currentService.getSerTypeID(), currentService.getSerStatusID(), currentService.getSerID());
            // Xoa het ServiceDetails cu cua current service
            runPS("delete ServiceDetails where ServiceID =?", currentService.getSerID());
            // Update table ServiceDetails

            for (ServiceDetails op : list) {

                if (op.getOrdID() == 0) {
                    runPS("insert ServiceDetails(ServiceID, ProID, ServiceContent,ProQty,ServiceCost) values(?,?,?,?,?)", currentService.getSerID(), op.getProID(), op.getSerContent(), op.getProQty(), op.getSerCost());
                } else {
                    runPS("insert into ServiceDetails(ServiceID, ProID, ServiceContent,ProQty, OrdID,ServiceCost) values(?,?,?,?,?,?)", currentService.getSerID(), op.getProID(), op.getSerContent(), op.getProQty(), op.getOrdID(), op.getSerCost());
                }
            }
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(ServiceDetailsDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result;
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
        return 0;
    }

    @Override
    public void setSelectingIndex(int idx) {

    }

}
