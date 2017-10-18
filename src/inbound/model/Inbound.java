/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inbound.model;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author tuan
 */
public class Inbound implements Cloneable {
        private int InID;
	private Date InDate;
	private String SupName;
	private String SupInvoiceID;
        private String UserName;
        
        private int SupID;
	private int UserID;
        
        public static final String COL_InID = "InID";
        public static final String COL_InDate = "InDate";
        public static final String COL_SupName = "SupName";
        public static final String COL_SupInID = "SupInvoiceID";
        public static final String COL_UserName = "UserName";
        public static final String COL_SupID = "SupID";
        public static final String COL_UserID = "UserID";

    public Inbound() {
    }

    public Inbound(int InID, Date InDate, String SupName, String SupInvoiceID, String UserName, int SupID, int UserID) {
        this.InID = InID;
        this.InDate = InDate;
        this.SupName = SupName;
        this.SupInvoiceID = SupInvoiceID;
        this.UserName = UserName;
        this.SupID = SupID;
        this.UserID = UserID;
    }

    public int getInID() {
        return InID;
    }

    public void setInID(int InID) {
        this.InID = InID;
    }

    public Date getInDate() {
        return InDate;
    }

    public void setInDate(Date InDate) {
        this.InDate = InDate;
    }

    public String getSupName() {
        return SupName;
    }

    public void setSupName(String SupName) {
        this.SupName = SupName;
    }

    public String getSupInvoiceID() {
        return SupInvoiceID;
    }

    public void setSupInvoiceID(String SupInvoiceID) {
        this.SupInvoiceID = SupInvoiceID;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String UserName) {
        this.UserName = UserName;
    }

    public int getSupID() {
        return SupID;
    }

    public void setSupID(int SupID) {
        this.SupID = SupID;
    }

    public int getUserID() {
        return UserID;
    }

    public void setUserID(int UserID) {
        this.UserID = UserID;
    }

    @Override
    public Inbound clone(){
        Inbound result = null;
        
            try {
                result = (Inbound)super.clone();
            } catch (CloneNotSupportedException ex) {
                Logger.getLogger(Inbound.class.getName()).log(Level.SEVERE, null, ex);
            }
       
        return result; 
    }
        
        
}
