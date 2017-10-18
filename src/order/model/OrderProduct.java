/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package order.model;

/**
 * Define order product class model
 *
 * @author Hoang
 */
public class OrderProduct {

    private int proID;
    private String proName;
    private int proQty;
    private float proPrice1;
    private float salesOffAmount;
    private float proPrice2;
    private int salesOffID;
    private int proNo;
    private String braName;
    private int braID;
    private int proStock;

    public static final String COL_PROID = "ProID";
    public static final String COL_PRONAME = "ProName";
    public static final String COL_PROQTY = "OrdProQty";
    public static final String COL_PROPRICE1 = "ProPrice";
    public static final String COL_SALEAMOUNT = "SalesOffAmount";
    public static final String COL_PROPRICE2 = "OrdProPrice";
    public static final String COL_SALEID = "SalesOffID";
    public static final String COL_PRONO = "ProNo";
    public static final String COL_BRANAME = "BraName";
    public static final String COL_BRAID = "BraID";
    public static final String COL_PROSTOCK = "ProStock";
    
    public static final String DEFAULT_PRONAME = "<html><font color='red'>Please choose item...</font></html>";

    public OrderProduct() {

    }

    public OrderProduct(int proID, String proName, int proQty, float proPrice1, float salesOffAmount, float proPrice2, int salesOffID, int proNo, String braName, int braID, int proStock) {
        this.proID = proID;
        this.proName = proName;
        this.proQty = proQty;
        this.proPrice1 = proPrice1;
        this.salesOffAmount = salesOffAmount;
        this.proPrice2 = proPrice2;
        this.salesOffID = salesOffID;
        this.proNo = proNo;
        this.braName = braName;
        this.braID = braID;
        this.proStock = proStock;
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

    public float getProPrice1() {
        return proPrice1;
    }

    public void setProPrice1(float proPrice1) {
        this.proPrice1 = proPrice1;
    }

    public float getSalesOffAmount() {
        return salesOffAmount;
    }

    public void setSalesOffAmount(float salesOffAmount) {
        this.salesOffAmount = salesOffAmount;
    }

    public float getProPrice2() {
        return proPrice2;
    }

    public void setProPrice2(float proPrice2) {
        this.proPrice2 = proPrice2;
    }

    public int getSalesOffID() {
        return salesOffID;
    }

    public void setSalesOffID(int salesOffID) {
        this.salesOffID = salesOffID;
    }

    public int getProNo() {
        return proNo;
    }

    public void setProNo(int proNo) {
        this.proNo = proNo;
    }

    public String getBraName() {
        return braName;
    }

    public void setBraName(String braName) {
        this.braName = braName;
    }

    public int getBraID() {
        return braID;
    }

    public void setBraID(int braID) {
        this.braID = braID;
    }

    public int getProStock() {
        return proStock;
    }

    public void setProStock(int proStock) {
        this.proStock = proStock;
    }

    @Override
    public String toString() {
        return "OrderProduct{" + "proID=" + proID + ", proName=" + proName + ", proQty=" + proQty + ", proPrice1=" + proPrice1 + ", salesOffAmount=" + salesOffAmount + ", proPrice2=" + proPrice2 + ", salesOffID=" + salesOffID + ", proNo=" + proNo + ", braName=" + braName + ", braID=" + braID + ", proStock=" + proStock + '}';
    }

}
