/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package salesoff.model;

/**
 *
 * @author Hoang
 */
public class SalesOffProduct {

    private int proID;
    private String proName;
    private String braName;
    private boolean salesOff;
    private int saleID;

    public static final String COL_PROID = "ProID";
    public static final String COL_PRONAME = "ProName";
    public static final String COL_BRANAME = "BraName";
    public static final String COL_SALEID = "SalesOffID";

    public SalesOffProduct() {
    }

    public SalesOffProduct(int proID, String proName, String braName, boolean salesOff, int saleID) {
        this.proID = proID;
        this.proName = proName;
        this.braName = braName;
        this.salesOff = salesOff;
        this.saleID = saleID;
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

    public String getBraName() {
        return braName;
    }

    public void setBraName(String braName) {
        this.braName = braName;
    }

    public boolean isSalesOff() {
        return salesOff;
    }

    public void setSalesOff(boolean salesOff) {
        this.salesOff = salesOff;
    }

    public int getSaleID() {
        return saleID;
    }

    public void setSaleID(int saleID) {
        this.saleID = saleID;
    }
}
