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
public class Customer {

    private int cusID;
    private String cusName;
    private float cusPaid;
    private int cusLevel;
    private String cusLevelName;
    private String cusPhone;
    private String cusAddress;
    private boolean cusEnabled;
    private int cusLevelID;

    //Khai bao ten column de hien thi tren table
    public static final String COL_CUSID = "CusID";
    public static final String COL_CUSNAME = "CusName";
    public static final String COL_CUSPAID = "CusPaid";
    public static final String COL_CUSLEVEL = "CusLevel";
    public static final String COL_CUSLEVELNAME = "CusLevelName";
    public static final String COL_CUSPHONE = "CusPhone";
    public static final String COL_CUSADDRESS = "CusAddress";
    public static final String COL_CUSENABLED = "CusEnabled";
    public static final String COL_CUSLEVELID = "CusLevelID";

    public Customer() {

    }

    public Customer(int cusID, String cusName, float cusPaid, int cusLevel, String cusLevelName, String cusPhone, String cusAddress, boolean cusEnabled, int cusLevelID) {
        this.cusID = cusID;
        this.cusName = cusName;
        this.cusPaid = cusPaid;
        this.cusLevel = cusLevel;
        this.cusLevelName = cusLevelName;
        this.cusPhone = cusPhone;
        this.cusAddress = cusAddress;
        this.cusEnabled = cusEnabled;
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

    public float getCusPaid() {
        return cusPaid;
    }

    public void setCusPaid(float cusPaid) {
        this.cusPaid = cusPaid;
    }

    public int getCusLevel() {
        return cusLevel;
    }

    public void setCusLevel(int cusLevel) {
        this.cusLevel = cusLevel;
    }

    public String getCusLevelName() {
        return cusLevelName;
    }

    public void setCusLevelName(String cusLevelName) {
        this.cusLevelName = cusLevelName;
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

    public boolean isCusEnabled() {
        return cusEnabled;
    }

    public void setCusEnabled(boolean cusEnabled) {
        this.cusEnabled = cusEnabled;
    }

    public int getCusLevelID() {
        return cusLevelID;
    }

    public void setCusLevelID(int cusLevelID) {
        this.cusLevelID = cusLevelID;
    }


}
