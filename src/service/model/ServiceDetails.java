/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service.model;

import java.util.Date;

/**
 *
 * @author BonBon
 */
public class ServiceDetails {

    private int serID;
    private String proName;
    private String braName;
    private String serContent;
    private int proQty;
    private int ordID;
    private int serCost;
    private int proID;
    private int BraID;

    public static final String COL_ID = "ServiceID";
    public static final String COL_PRONAME = "ProName";
    public static final String COL_BRANCH = "BraName";
    public static final String COL_CONTENT = "ServiceContent";
    public static final String COL_QUANTITY = "ProQty";
    public static final String COL_ORDERID = "OrdID";
    public static final String COL_COST = "ServiceCost";
    public static final String COL_PROID = "ProID";
    public static final String COL_BRAID = "BraID";
    
    public static final String DEFAULT_PRONAME = "<html><font color='red'>Please choose item...</font></html>";
    public ServiceDetails() {
    }

    public ServiceDetails(int serID, String proName, String braName, String serContent, int proQty, int ordID,int serCost,  int proID, int BraID) {
        this.serID = serID;
        this.proName = proName;
        this.braName = braName;
        this.serContent = serContent;
        this.proQty = proQty;
        this.ordID = ordID;
        this.serCost=serCost;
        this.proID = proID;
        this.BraID = BraID;
    }

    public int getSerCost() {
        return serCost;
    }

    public void setSerCost(int serCost) {
        this.serCost = serCost;
    }

   
    public String getSerContent() {
        return serContent;
    }

    public void setSerContent(String serContent) {
        this.serContent = serContent;
    }

    public int getSerID() {
        return serID;
    }

    public void setSerID(int serID) {
        this.serID = serID;
    }

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }

    public String getBraName() {
        return braName;
    }

    public void setBraName(String braName) {
        this.braName = braName;
    }

    public int getProQty() {
        return proQty;
    }

    public void setProQty(int proQty) {
        this.proQty = proQty;
    }

    public int getOrdID() {
        return ordID;
    }

    public void setOrdID(int ordID) {
        this.ordID = ordID;
    }

    public int getProID() {
        return proID;
    }

    public void setProID(int proID) {
        this.proID = proID;
    }

    public int getBraID() {
        return BraID;
    }

    public void setBraID(int BraID) {
        this.BraID = BraID;
    }

    @Override
    public String toString() {
        return "ServiceDetails{" + "serID=" + serID + ", proName=" + proName + ", braName=" + braName + ", serContent=" + serContent + ", proQty=" + proQty + ", ordID=" + ordID + ", serCost=" + serCost + ", proID=" + proID + ", BraID=" + BraID + '}';
    }

    
    
}
