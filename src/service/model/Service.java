/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service.model;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import order.model.Order;

/**
 *
 * @author BonBon
 */
public class Service implements Cloneable {

    private int serID;
    private String userName;
    private String cusPhone;
    private Date receiveDate;
    private Date returnDate;
    private String serTypeName;
    private String serStatus;

    private int userID;
    private int serStatusID;
    private int serTypeID;

    public static final String COL_SERID = "ServiceID";
    public static final String COL_USERNAME = "UserName";
    public static final String COL_CUSPHONE = "CusPhone";
    public static final String COL_RECEIVEDATE = "ReceiveDate";
    public static final String COL_RETURNDATE = "ReturnDate";
    public static final String COL_SERTYPENAME = "ServiceTypeName";
    public static final String COL_SERSTATUS = "SttName";

    public static final String COL_USERID = "UserID";
    public static final String COL_SERTYPEID = "ServiceTypeID";
    public static final String COL_SERSTATUSID = "SttID";

    public Service() {
    }

    public Service(int serID, String userName, String cusPhone, Date receiveDate, Date returnDate, String serTypeName, String serStatus, int userID, int serStatusID, int serTypeID) {
        this.serID = serID;
        this.userName = userName;
        this.cusPhone = cusPhone;
        this.receiveDate = receiveDate;
        this.returnDate = returnDate;
        this.serTypeName = serTypeName;
        this.serStatus = serStatus;

        this.userID = userID;
        this.serTypeID = serTypeID;
        this.serStatusID = serStatusID;
    }

    @Override
    public String toString() {
        return "Service{" + "serID=" + serID + ", userName=" + userName + ", receiveDate=" + receiveDate + ", returnDate=" + returnDate + ", serTypeName=" + serTypeName + ", serStatus=" + serStatus + ", userID=" + userID + ", serStatusID=" + serStatusID + ", serTypeID=" + serTypeID + '}';
    }

    public int getSerID() {
        return serID;
    }

    public void setSerID(int serID) {
        this.serID = serID;
    }

    public String getCusPhone() {
        return cusPhone;
    }

    public void setCusPhone(String cusPhone) {
        this.cusPhone = cusPhone;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(Date receiveDate) {
        this.receiveDate = receiveDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public String getSerTypeName() {
        return serTypeName;
    }

    public void setSerTypeName(String serTypeName) {
        this.serTypeName = serTypeName;
    }

    public String getSerStatus() {
        return serStatus;
    }

    public void setSerStatus(String serStatus) {
        this.serStatus = serStatus;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getSerStatusID() {
        return serStatusID;
    }

    public void setSerStatusID(int serStatusID) {
        this.serStatusID = serStatusID;
    }

    public int getSerTypeID() {
        return serTypeID;
    }

    public void setSerTypeID(int serTypeID) {
        this.serTypeID = serTypeID;
    }

    @Override
    public Service clone() {
        Service result = null;
        try {
            result = (Service) super.clone();
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
}
