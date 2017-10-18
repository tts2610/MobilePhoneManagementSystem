/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inbound.model;

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
import outbound.model.OutboundDetail;
import utility.SwingUtils;

/**
 *
 * @author tuan
 */
public class InboundDAOImpl implements IDAO<Inbound> {
    private CachedRowSet crs;
    public static String mainCRS="SELECT InID,InDate,SupName,SupInvoiceID,UserName,s.SupID,u.UserID from Inbounds i join Suppliers s on i.SupID=s.SupID join Users u on u.UserID=i.UserID order by InID desc";
    private static List<InboundDetail> listTemp = new ArrayList<>();
    public InboundDAOImpl() {
        this.crs = getCRS(mainCRS);
        
       
    }
    
    
    
    
    
    
    
    
    
    @Override
    public List<Inbound> getList() {
        List<Inbound> inboundList = new ArrayList<>();
        try {
            if (crs.first()) {
                do {
                    inboundList.add(new Inbound(
                            crs.getInt(Inbound.COL_InID),
                            crs.getDate(Inbound.COL_InDate),
                            crs.getString(Inbound.COL_SupName),
                            crs.getString(Inbound.COL_SupInID),
                            crs.getString(Inbound.COL_UserName),
                            crs.getInt(Inbound.COL_SupID),
                            crs.getInt(Inbound.COL_UserID)));
                } while (crs.next());
            }
        } catch (SQLException ex) {
            Logger.getLogger(InboundDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return inboundList;
    }

    
    @Override
    public boolean update(Inbound inbound) {
       boolean result = false;
        try {
            // Check cus phone khong duoc trung
            CachedRowSet crs2 = getCRS("select * from Inbounds where SupInvoiceID = ? AND InID!=?",inbound.getSupInvoiceID(),inbound.getInID());
            if (crs2.first()) {
                SwingUtils.showErrorDialog("Supplier Invoice ID cannot be duplicated !");
            } else {
                runPS("update Inbounds set InDate=?, SupID=(Select SupID from Suppliers where supName=?),SupInvoiceID=?,UserID=(SELECT UserID from users where userName=?) where InID=?",
                        inbound.getInDate(),
                        inbound.getSupName(),
                        inbound.getSupInvoiceID(),
                        inbound.getUserName(),
                        inbound.getInID()
                );

                // Refresh lai cachedrowset hien thi table
                crs.execute();
                result = true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(InboundDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public boolean delete(Inbound inbound) {
        boolean result = false;
        InboundDetailDAOImpl id = new InboundDetailDAOImpl();
        id.load(inbound.getInID());
        listTemp = id.getList();
        try {
            for(int i =0;i<listTemp.size();i++){
                runPS("Delete from InDetails where InID=? and ProID=?",inbound.getInID(),listTemp.get(i).getProID());
            }
            runPS("delete from Inbounds where InID=?", inbound.getInID());
            crs.execute();
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(InboundDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public boolean insert(Inbound model) {
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
    
    public void filterDate(Date fromDate,Date toDate){
        if(fromDate!=null&& toDate !=null){
        java.sql.Date sqlFromDate = new java.sql.Date(fromDate.getTime());
        java.sql.Date sqlToDate = new java.sql.Date(toDate.getTime());
        mainCRS ="SELECT InID,InDate,SupName,SupInvoiceID,UserName,s.SupID,u.UserID from Inbounds i join Suppliers s on i.SupID=s.SupID join Users u on u.UserID=i.UserID  where InDate >= "+"'"+sqlFromDate+"'"+"and InDate <="+"'"+sqlToDate+"'"+" order by InID DESC";
        }
        
        else{
            if(fromDate!=null&&toDate==null){
                java.sql.Date sqlFromDate = new java.sql.Date(fromDate.getTime());
                mainCRS="SELECT InID,InDate,SupName,SupInvoiceID,UserName,s.SupID,u.UserID from Inbounds i join Suppliers s on i.SupID=s.SupID join Users u on u.UserID=i.UserID  where InDate >= "+"'"+sqlFromDate+"'"+" order by InID DESC";
            }
            if(fromDate==null&&toDate!=null){
                java.sql.Date sqlToDate = new java.sql.Date(toDate.getTime());
                mainCRS="SELECT InID,InDate,SupName,SupInvoiceID,UserName,s.SupID,u.UserID from Inbounds i join Suppliers s on i.SupID=s.SupID join Users u on u.UserID=i.UserID  where InDate <= "+"'"+sqlToDate+"'"+" order by InID DESC";
            }
          
        }
        
        
    }
    
   

    public static String getMainCRS() {
        return mainCRS;
    }

    public static void setMainCRS(String mainCRS) {
        InboundDAOImpl.mainCRS = mainCRS;
    }
    
}
