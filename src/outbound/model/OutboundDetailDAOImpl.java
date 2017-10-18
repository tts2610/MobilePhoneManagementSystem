/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package outbound.model;


import database.DBProvider;
import database.IDAO;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.rowset.CachedRowSet;
import product.model.Product;
import utility.SwingUtils;

/**
 *
 * @author tuan
 */
public class OutboundDetailDAOImpl implements IDAO<OutboundDetail>{
    private CachedRowSet crs;
    private Outbound currentOutbound;
    private int selectingIndex;
    private static List<OutboundDetail> listTemp = new ArrayList<>();
    
    public void load(int inID) {
        this.crs = getCRS("SELECT Products.ProID,ProName,ProQty from OutDetails join products on OutDetails.ProID = Products.proID where OutID=?",inID);
    }
    
    
    @Override
    public List<OutboundDetail> getList() {
        List<OutboundDetail> list = new ArrayList<>(); 
        try {
            if (crs != null &&crs.first()) {
                do {
                    list.add(new OutboundDetail(
                            
                            crs.getInt("ProID"), 
                            crs.getString("ProName"),
                            crs.getInt("ProQty")));
                    
                    listTemp.add(new OutboundDetail(
                            
                            crs.getInt("ProID"), 
                            crs.getString("ProName"),
                            crs.getInt("ProQty")));
                } while (crs.next());
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(OutboundDetailDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }
    

    @Override
    public boolean insert(OutboundDetail model) {
        boolean result = false;
        return result;
        
    }
    //dung de insert INBOUND that su(insert mode)
    public boolean insert(List<OutboundDetail> inDetail){
        boolean result = false;
        try {
            
            runPS("Insert into Outbounds values('06/06/2016',1,(select min(userid) from users),'AAAAA')");
            CachedRowSet crs2 = getCRS("Select Max(OutID) as Outid from Outbounds");
            crs2.next();
            
            for(int i = 0;i<inDetail.size();i++){
            runPS("Insert into OutDetails(Outid,ProID,ProQty) values(?,?,?)",
                    crs2.getInt("Outid"),
                    inDetail.get(i).getProID(),
                    inDetail.get(i).getProQty());
            }
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(OutboundDetailDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
    //dung de update THAT SU LAI INBOUND sau khi da INSERT bang default value(insert mode)
    public boolean update(Outbound inbound){
        boolean result = false;
        try {
            
            
            
                runPS("Update outbounds set OutDate=?,OutContent=?,UserID = (SELECT userid from users where username = ?) where OutID=(SELECT max(OutID) from outbounds)",
                    inbound.getOutDate(),
                    inbound.getOutContent(),
                    inbound.getUserName());
            
            result = true;
            
        } catch (SQLException ex) {
            Logger.getLogger(OutboundDetailDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
    
    //dung de update list inboundDetail (update mode)
    public boolean update(List<OutboundDetail> inDetail,Outbound inbound,Outbound currentOutbound){
        boolean result = false;
        try {
            
            for(int i =0;i<listTemp.size();i++){
            // Xoa het outbound details cu cua current outbound
            runPS("delete OutDetails where OutID =? and ProID=?", currentOutbound.getOutID(),listTemp.get(i).getProID());
            }
            
            
            //check truong hop list co them nhung san pham moi, neu co thi phai INSERT san pham moi trc khi update lai gia tri san pham cu
            for(int i = 0;i<inDetail.size();i++){
                CachedRowSet crs1 = getCRS("Select * from OutDetails where OutID=? and ProID=?",inbound.getOutID(),
                    inDetail.get(i).getProID());
                if(!crs1.first()){
                    runPS("Insert into OutDetails(OutID,ProID,ProQty) values(?,?,?)",
                    inbound.getOutID(),
                    inDetail.get(i).getProID(),
                    inDetail.get(i).getProQty());
                }
            }
            
            
            
            //update nhung san pham cu
            for(int i = 0;i<inDetail.size();i++){
            runPS("Update OutDetails set ProQty=? where OutID=? and ProID=?",
                    
                    inDetail.get(i).getProQty(),
                    inbound.getOutID(),
                    inDetail.get(i).getProID());
            }
            
            //update cho table inbound
            runPS("Update outbounds set OutContent=? where OutID=?",
                   
                    inbound.getOutContent(),
                    inbound.getOutID());
            
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(OutboundDetailDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
    
    
    
    @Override
    public boolean update(OutboundDetail model) {
        boolean result = false;
        return result;
    }
    
    public boolean update(){
        boolean result = false;
        return result;
    }

    @Override
    public boolean delete(OutboundDetail model) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    //dung de update THAT SU LAI INBOUND sau khi da INSERT bang default value
    public boolean delete(Outbound inbound){
        boolean result = false;
        try {
            
            runPS("Delete OutDetails where OutID=(SELECT Max(OutID) from outbounds)");
            runPS("Delete outbounds where OutID=(SELECT Max(OutID) from outbounds)");
            
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(OutboundDetailDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
    
    
    @Override
    public int getSelectingIndex(int idx) {
        return selectingIndex;
    }

    @Override
    public void setSelectingIndex(int idx) {
        selectingIndex = idx;
    }

    public Outbound getCurrentInbound() {
        return currentOutbound;
    }

    public void setCurrentInbound(Outbound currentInbound) {
        this.currentOutbound = currentInbound;
    }
    
}
