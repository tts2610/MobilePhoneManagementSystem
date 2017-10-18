/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package order.model;

/**
 *
 * @author Hoang
 */
public class OrderCustomer {
    private int cusID;
    private String cusName;
    private String cusPhone;
    private String cusAddress;
    private float cusDiscount;
    private int cusLevelID;

    public static final String COL_ID = "CusID";
    public static final String COL_NAME = "CusName";
    public static final String COL_PHONE = "CusPhone";
    public static final String COL_ADDRESS = "CusAddress";
    public static final String COL_DISCOUNT = "CusDiscount";
    public static final String COL_LEVELID = "CusLevelID";

    public OrderCustomer() {
    }

    public OrderCustomer(int cusID, String cusName, String cusPhone, String cusAddress, float cusDiscount, int cusLevelID) {
        this.cusID = cusID;
        this.cusName = cusName;
        this.cusPhone = cusPhone;
        this.cusAddress = cusAddress;
        this.cusDiscount = cusDiscount;
        this.cusLevelID = cusLevelID;
    }

    public int getCusID() {
        return cusID;
    }

    public void setCusID(int cusID) {
        this.cusID = cusID;
    }

    public String getCusName() {
        return cusName;
    }

    public void setCusName(String cusName) {
        this.cusName = cusName;
    }

    public String getCusPhone() {
        return cusPhone;
    }

    public void setCusPhone(String cusPhone) {
        this.cusPhone = cusPhone;
    }

    public String getCusAddress() {
        return cusAddress;
    }

    public void setCusAddress(String cusAddress) {
        this.cusAddress = cusAddress;
    }

    public float getCusDiscount() {
        return cusDiscount;
    }

    public void setCusDiscount(float cusDiscount) {
        this.cusDiscount = cusDiscount;
    }

    public int getCusLevelID() {
        return cusLevelID;
    }

    public void setCusLevelID(int cusLevelID) {
        this.cusLevelID = cusLevelID;
    }

    @Override
    public String toString() {
        return "OrderCustomer{" + "cusID=" + cusID + ", cusName=" + cusName + ", cusPhone=" + cusPhone + ", cusAddress=" + cusAddress + ", cusDiscount=" + cusDiscount + ", cusLevelID=" + cusLevelID + '}';
    }
}
