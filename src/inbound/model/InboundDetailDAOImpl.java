/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inbound.model;

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
public class InboundDetailDAOImpl implements IDAO<InboundDetail>{
    private CachedRowSet crs;
    private Inbound currentInbound;
    private int selectingIndex;
    private static List<InboundDetail> listTemp = new ArrayList<>();
    public void load(int inID) {
        this.crs = getCRS("SELECT Products.ProID,ProName,ProCost,ProQty from InDetails join products on InDetails.ProID = Products.proID where InID=?",inID);
    }
    
    
    @Override
    public List<InboundDetail> getList() {
        List<InboundDetail> list = new ArrayList<>(); 
        try {
            if (crs != null &&crs.first()) {
                do {
                    list.add(new InboundDetail(
                            
                            crs.getInt("ProID"), 
                            crs.getString("ProName"),
                            crs.getFloat("ProCost"), 
                            crs.getInt("ProQty")));
                    
                    //list de xoa lan luot
                    listTemp.add(new InboundDetail(
                            
                            crs.getInt("ProID"), 
                            crs.getString("ProName"),
                            crs.getFloat("ProCost"), 
                            crs.getInt("ProQty")));        
                } while (crs.next());
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(InboundDetailDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    @Override
    public boolean insert(InboundDetail model) {
        boolean result = false;
        return result;
        
    }
    //dung de insert INBOUND that su(insert mode)
    public boolean insert(List<InboundDetail> inDetail,String supid){
        boolean result = false;
        try {
            
            //insert record mac dinh
            runPS("Insert into Inbounds values('06/06/2016',(select min(supid) from suppliers),'AAAAA',(select min(userid) from users))");
            CachedRowSet crs2 = getCRS("Select Max(InID) as Inid from Inbounds");
            crs2.next();
            
            //check duplicate
            CachedRowSet crs1 = getCRS("Select * from inbounds where supInvoiceID = ? and InID!=(SELECT max(InID) from inbounds)",supid);
            if(crs1.first()){
                SwingUtils.showInfoDialog("Duplicate Supplier Invoice ID!");
                return false;
            }
            
            //insert indetail
            for(int i = 0;i<inDetail.size();i++){
            runPS("Insert into InDetails(InID,ProID,ProCost,ProQty) values(?,?,?,?)",
                    crs2.getInt("InId"),
                    inDetail.get(i).getProID(),
                    inDetail.get(i).getProCost(),
                    inDetail.get(i).getProQty());
            }
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(InboundDetailDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
    //dung de update THAT SU LAI INBOUND sau khi da INSERT bang default value(insert mode)
    public boolean update(Inbound inbound){
        boolean result = false;
        try {
            
            
            
                runPS("Update inbounds set InDate=?,SupID = (SELECT supid from suppliers where supname=?),SupInvoiceID=?,UserID = (SELECT UserID from Users where UserName=?) where InID=(SELECT max(InID) from inbounds)",
                    inbound.getInDate(),
                    inbound.getSupName(),
                    inbound.getSupInvoiceID(),
                    inbound.getUserName());
            
            result = true;
            
        } catch (SQLException ex) {
            Logger.getLogger(InboundDetailDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
    
    //dung de update list inboundDetail (update mode)
    public boolean update(List<InboundDetail> inDetail,Inbound inbound,Inbound currentInbound){
        boolean result = false;
        try {
            for(int i =0;i<listTemp.size();i++){
            // Xoa het outbound details cu cua current outbound
            runPS("delete InDetails where InID =? and proID=?", currentInbound.getInID(),listTemp.get(i).getProID());
            }
            
            //check truong hop list co them nhung san pham moi, neu co thi phai INSERT san pham moi trc khi update lai gia tri san pham cu
            for(int i = 0;i<inDetail.size();i++){
                CachedRowSet crs1 = getCRS("Select * from InDetails where InID=? and ProID=?",inbound.getInID(),
                    inDetail.get(i).getProID());
                if(!crs1.first()){
                    runPS("Insert into InDetails(InID,ProID,ProCost,ProQty) values(?,?,?,?)",
                    inbound.getInID(),
                    inDetail.get(i).getProID(),
                    inDetail.get(i).getProCost(),
                    inDetail.get(i).getProQty());
                }
            }
            
            //update nhung san pham cu
            for(int i = 0;i<inDetail.size();i++){
            runPS("Update InDetails set ProCost=?,ProQty=? where InID=? and ProID=?",
                    
                    inDetail.get(i).getProCost(),
                    inDetail.get(i).getProQty(),
                    inbound.getInID(),
                    inDetail.get(i).getProID());
            }
            
            //update cho table inbound
            runPS("Update inbounds set SupID = (SELECT supid from suppliers where supname=?),SupInvoiceID=? where InID=?",
                    
                    inbound.getSupName(),
                    inbound.getSupInvoiceID(),
                    inbound.getInID());
            
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(InboundDetailDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
    
    
    
    @Override
    public boolean update(InboundDetail model) {
        boolean result = false;
        return result;
    }
    
    public boolean update(){
        boolean result = false;
        return result;
    }

    @Override
    public boolean delete(InboundDetail model) {
       boolean result = false;
        try {
            runPS("DELETE InDetails where InID=?", model.getInID());
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(InboundDetailDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
    //dung de update THAT SU LAI INBOUND sau khi da INSERT bang default value
    public boolean delete(Inbound inbound){
        boolean result = false;
        try {
            
            runPS("Delete InDetails where InID=(SELECT Max(InID) from inbounds)");
            runPS("Delete inbounds where InID=(SELECT Max(InID) from inbounds)");
            
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(InboundDetailDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
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

    public Inbound getCurrentInbound() {
        return currentInbound;
    }

    public void setCurrentInbound(Inbound currentInbound) {
        this.currentInbound = currentInbound;
    }
    
}
