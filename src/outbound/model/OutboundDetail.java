/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package outbound.model;



/**
 *
 * @author tuan
 */
public class OutboundDetail {
    private int outID;
    private String proName;
    private int proID;
    private int proQty;

    public OutboundDetail(int proID,String proName, int proQty) {
        this.proID = proID;
        this.proName = proName;
        
        
        this.proQty = proQty;
    }

    public OutboundDetail() {
    }

    public int getOutID() {
        return outID;
    }

    public void setOutID(int outID) {
        this.outID = outID;
    }

    

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }

    public int getProID() {
        return proID;
    }

    public void setProID(int proID) {
        this.proID = proID;
    }

    

    public int getProQty() {
        return proQty;
    }

    public void setProQty(int proQty) {
        this.proQty = proQty;
    }
    
    
}
