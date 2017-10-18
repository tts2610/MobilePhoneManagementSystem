/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package outbound.model;


import database.DBProvider;
import database.IDAO;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.rowset.CachedRowSet;
import utility.SwingUtils;

/**
 *
 * @author tuan
 */
public class OutboundDAOImpl implements IDAO<Outbound> {
    private CachedRowSet crs;
    public static String mainCRS = "SELECT OutID,OutDate,OutContent,UserName,u.UserID from Outbounds o join Users u on u.UserID=o.UserID order by OutID desc";
    private static List<OutboundDetail> listTemp = new ArrayList<>();
    public OutboundDAOImpl() {
        this.crs = getCRS(mainCRS);
        
    }
    
    
    @Override
    public List<Outbound> getList() {
        List<Outbound> inboundList = new ArrayList<>();
        try {
            if (crs.first()) {
                do {
                    inboundList.add(new Outbound(
                            crs.getInt(Outbound.COL_OutID),
                            crs.getDate(Outbound.COL_OutDate),
                            crs.getString(Outbound.COL_OutContent),
                            crs.getString(Outbound.COL_UserName),
                            crs.getInt(Outbound.COL_UserID)));
                } while (crs.next());
            }
        } catch (SQLException ex) {
            Logger.getLogger(OutboundDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return inboundList;
    }

    
    @Override
    public boolean update(Outbound outbound) {
       boolean result = false;
//        try {
//            
//                runPS("update Inbounds set InDate=?, SupID=(Select SupID from Suppliers where supName=?),SupInvoiceID=?,UserID=(SELECT UserID from users where userName=?) where InID=?",
//                        inbound.getInDate(),
//                        inbound.getSupName(),
//                        inbound.getSupInvoiceID(),
//                        inbound.getUserName(),
//                        inbound.getInID()
//                );
//
//                // Refresh lai cachedrowset hien thi table
//                crs.execute();
//                result = true;
//            
//        } catch (SQLException ex) {
//            Logger.getLogger(OutboundDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
//        }
        return result;
    }

    @Override
    public boolean delete(Outbound outbound) {
        boolean result = false;
        OutboundDetailDAOImpl od = new OutboundDetailDAOImpl();
        od.load(outbound.getOutID());
        listTemp = od.getList();
        try {
            for(int i =0;i<listTemp.size();i++){
            
            runPS("Delete from OutDetails where OutID=? and ProID=?",outbound.getOutID(),listTemp.get(i).getProID());
            }
            runPS("delete from Outbounds where OutID=?",outbound.getOutID() );
            crs.execute();
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(OutboundDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public void filterDate(Date fromDate,Date toDate){
        if(fromDate!=null&& toDate !=null){
        java.sql.Date sqlFromDate = new java.sql.Date(fromDate.getTime());
        java.sql.Date sqlToDate = new java.sql.Date(toDate.getTime());
        mainCRS ="SELECT OutID,OutDate,OutContent,UserName,u.UserID from Outbounds o join Users u on u.UserID=o.UserID where OutDate >= "+"'"+sqlFromDate+"'"+"and OutDate <="+"'"+sqlToDate+"'"+" order by OutID DESC";
        }
        
        else{
            if(fromDate!=null&&toDate==null){
                java.sql.Date sqlFromDate = new java.sql.Date(fromDate.getTime());
                mainCRS="SELECT OutID,OutDate,OutContent,UserName,u.UserID from Outbounds o join Users u on u.UserID=o.UserID  where OutDate >= "+"'"+sqlFromDate+"'"+" order by OutID DESC";
            }
            if(fromDate==null&&toDate!=null){
                java.sql.Date sqlToDate = new java.sql.Date(toDate.getTime());
                mainCRS="SELECT OutID,OutDate,OutContent,UserName,u.UserID from Outbounds o join Users u on u.UserID=o.UserID  where OutDate <= "+"'"+sqlToDate+"'"+" order by OutID DESC";
            }
          
        }
        
        
    }
    @Override
    public boolean insert(Outbound model) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getSelectingIndex(int idx) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setSelectingIndex(int idx) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public static String getMainCRS() {
        return mainCRS;
    }

    public static void setMainCRS(String mainCRS) {
        OutboundDAOImpl.mainCRS = mainCRS;
    }
    
}
