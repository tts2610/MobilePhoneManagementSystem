package order.model;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Define order class model
 *
 * @author Hoang
 */
public class Order implements Cloneable {

    private int ordID;
    private String userName;
    private String cusName;
    private Date ordDate;
    private float cusDiscount;
    private String ordStatus;
    private int userID;
    private int cusID;
    private int ordStatusID;
    private float ordValue;
    
    public static final String COL_ORDID = "OrdID";
    public static final String COL_USERNAME = "UserName";
    public static final String COL_CUSNAME = "CusName";
    public static final String COL_ORDDATE = "OrdDate";
    public static final String COL_CUSDISCOUNT = "OrdCusDiscount";
    public static final String COL_ORDSTATUS = "SttName";
    public static final String COL_USERID = "UserID";
    public static final String COL_CUSID = "CusID";
    public static final String COL_ORDSTATUSID = "SttID";
    public static final String COL_ORDVALUE = "OrdValue";

    public Order() {
        
    }

    public Order(int ordID, String userName, String cusName, Date ordDate, float cusDiscount, String ordStatus, int userID, int cusID, int ordStatusID, float ordValue) {
        this.ordID = ordID;
        this.userName = userName;
        this.cusName = cusName;
        this.ordDate = ordDate;
        this.cusDiscount = cusDiscount;
        this.ordStatus = ordStatus;
        this.userID = userID;
        this.cusID = cusID;
        this.ordStatusID = ordStatusID;
        this.ordValue = ordValue;
    }

    public int getOrdID() {
        return ordID;
    }

    public void setOrdID(int ordID) {
        this.ordID = ordID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCusName() {
        return cusName;
    }

    public void setCusName(String cusName) {
        this.cusName = cusName;
    }

    public Date getOrdDate() {
        return ordDate;
    }

    public void setOrdDate(Date ordDate) {
        this.ordDate = ordDate;
    }

    public float getCusDiscount() {
        return cusDiscount;
    }

    public void setCusDiscount(float cusDiscount) {
        this.cusDiscount = cusDiscount;
    }

    public String getOrdStatus() {
        return ordStatus;
    }

    public void setOrdStatus(String ordStatus) {
        this.ordStatus = ordStatus;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getCusID() {
        return cusID;
    }

    public void setCusID(int cusID) {
        this.cusID = cusID;
    }

    public int getOrdStatusID() {
        return ordStatusID;
    }

    public void setOrdStatusID(int ordStatusID) {
        this.ordStatusID = ordStatusID;
    }

    public float getOrdValue() {
        return ordValue;
    }

    public void setOrdValue(float ordValue) {
        this.ordValue = ordValue;
    }

    @Override
    public Order clone() {
        Order result = null;
        try {
            result = (Order) super.clone();
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(Order.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result; 
    }
}
