/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inbound.model;

/**
 *
 * @author tuan
 */
public class InboundProduct {
    private int proID;
    private String proName;
    private float proCost;
    private int proQty;

    
    public static final String COL_PROID = "ProID";
    public static final String COL_PRONAME = "ProName";
    public static final String COL_PROQTY = "ProQty";
    public static final String COL_PROCOST = "ProCost";
    public InboundProduct() {
    }

    public InboundProduct(int proID, String proName, float proCost) {
        this.proID = proID;
        this.proName = proName;
        this.proCost = proCost;
    }

    public InboundProduct(int proID, String proName, float proCost, int proQty) {
        this.proID = proID;
        this.proName = proName;
        this.proCost = proCost;
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

    public float getProPrice() {
        return proCost;
    }

    public void setProPrice(float proPrice) {
        this.proCost = proPrice;
    }

    public int getProQty() {
        return proQty;
    }

    public void setProQty(int proQty) {
        this.proQty = proQty;
    }
    
    
}
