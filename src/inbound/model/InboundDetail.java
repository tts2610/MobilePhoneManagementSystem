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
public class InboundDetail {
    private int inID;
    private String proName;
    private int proID;
    private float proCost;
    private int proQty;

    public InboundDetail(int proID,String proName, float proCost, int proQty) {
        this.proID = proID;
        this.proName = proName;
        
        this.proCost = proCost;
        this.proQty = proQty;
    }

    public InboundDetail() {
    }

    public int getInID() {
        return inID;
    }

    public void setInID(int inID) {
        this.inID = inID;
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

    public float getProCost() {
        return proCost;
    }

    public void setProCost(float proCost) {
        this.proCost = proCost;
    }

    public int getProQty() {
        return proQty;
    }

    public void setProQty(int proQty) {
        this.proQty = proQty;
    }
    
    
}
