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
public class OutboundProduct {
    private int proID;
    private String proName;
    private int proQty;

    
    public static final String COL_PROID = "ProID";
    public static final String COL_PRONAME = "ProName";
    public static final String COL_PROQTY = "ProQty";

    public OutboundProduct() {
    }

   

    public OutboundProduct(int proID, String proName,int proQty) {
        this.proID = proID;
        this.proName = proName;
        this.proQty = proQty;
    }
    

    public int getProID() {
        return proID;
    }

    public void setProID(int proID) {
        this.proID = proID;
    }

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }

    public int getProQty() {
        return proQty;
    }

    public void setProQty(int proQty) {
        this.proQty = proQty;
    }
    
    
}
