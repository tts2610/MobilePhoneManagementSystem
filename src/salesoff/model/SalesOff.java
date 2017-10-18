/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package salesoff.model;

import java.util.Date;

/**
 *
 * @author Hoang
 */
public class SalesOff {
    private int saleID;
    private String saleName;
    private Date saleStartDate;
    private Date saleEndDate;
    private float saleAmount;

    public static final String COL_SALEID = "SalesOffID";
    public static final String COL_SALENAME = "SalesOffName";
    public static final String COL_SALESTART = "SalesOffStartDate";
    public static final String COL_SALEEND = "SalesOffEndDate";
    public static final String COL_SALEAMOUNT = "SalesOffAmount";
    
    public static final int MIN_SALE = 1;
    public static final int MAX_SALE = 20;
    
    public SalesOff() {
    }
    
    public SalesOff(int saleID, String saleName, Date saleStartDate, Date saleEndDate, float saleAmount) {
        this.saleID = saleID;
        this.saleName = saleName;
        this.saleStartDate = saleStartDate;
        this.saleEndDate = saleEndDate;
        this.saleAmount = saleAmount;
    }
    
    // Getters
    public int getSaleID() {
        return saleID;
    }

    public String getSaleName() {
        return saleName;
    }

    public Date getSaleStartDate() {
        return saleStartDate;
    }

    public Date getSaleEndDate() {
        return saleEndDate;
    }

    public float getSaleAmount() {
        return saleAmount;
    }
    
    // Setters

    public void setSaleID(int saleID) {
        this.saleID = saleID;
    }

    public void setSaleName(String saleName) {
        this.saleName = saleName;
    }

    public void setSaleStartDate(Date saleStartDate) {
        this.saleStartDate = saleStartDate;
    }

    public void setSaleEndDate(Date saleEndDate) {
        this.saleEndDate = saleEndDate;
    }

    public void setSaleAmount(float saleAmount) {
        this.saleAmount = saleAmount;
    }
}
