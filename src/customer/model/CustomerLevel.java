/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.model;

/**
 *
 * @author Hoang
 */
public class CustomerLevel {

    private int cusLevelID;
    private int cusLevel;
    private String cusLevelName;
    private float cusDiscount;

    public static final String COL_CUSLEVELID = "CusLevelID";
    public static final String COL_CUSLEVEL = "CusLevel";
    public static final String COL_CUSLEVELNAME = "CusLevelName";
    public static final String COL_CUSDISCOUNT = "CusDiscount";
    
    public static final int MIN_DISCOUNT = 0;
    public static final int MAX_DISCOUNT = 50;
    public static final int MIN_LEVEL = 1;
    public static final int MAX_LEVEL = 10;

    public CustomerLevel() {
    }

    public CustomerLevel(int cusLevelID, int cusLevel, String cusLevelName, float cusDiscount) {
        this.cusLevelID = cusLevelID;
        this.cusLevel = cusLevel;
        this.cusLevelName = cusLevelName;
        this.cusDiscount = cusDiscount;
    }

    public int getCusLevelID() {
        return cusLevelID;
    }

    public void setCusLevelID(int CusLevelID) {
        this.cusLevelID = CusLevelID;
    }

    public int getCusLevel() {
        return cusLevel;
    }

    public void setCusLevel(int CusLevel) {
        this.cusLevel = CusLevel;
    }

    public String getCusLevelName() {
        return cusLevelName;
    }

    public void setCusLevelName(String CusLevelName) {
        this.cusLevelName = CusLevelName;
    }

    public float getCusDiscount() {
        return cusDiscount;
    }

    public void setCusDiscount(float cusDiscount) {
        this.cusDiscount = cusDiscount;
    }

    @Override
    public String toString() {
        return "CusLevelID: "+cusLevelID+", CusLevel: "+cusLevel;
    }
}
