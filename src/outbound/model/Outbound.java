/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package outbound.model;


import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author tuan
 */
public class Outbound implements Cloneable {
        private int OutID;
	private Date OutDate;
	private int TargetID;
	private String OutContent;
        private String UserName;
        
        
	private int UserID;
        
        public static final String COL_OutID = "OutID";
        public static final String COL_OutDate = "OutDate";
        public static final String COL_OutContent = "OutContent";
        public static final String COL_UserName = "UserName";
        public static final String COL_TargetID = "TargetID";
        public static final String COL_UserID = "UserID";

    public Outbound() {
    }

    public Outbound(int OutID, Date OutDate, String OutContent, String UserName, int UserID) {
        this.OutID = OutID;
        this.OutDate = OutDate;
//        this.TargetID = TargetID;
        this.OutContent = OutContent;
        this.UserName = UserName;
        this.UserID = UserID;
    }

    public int getOutID() {
        return OutID;
    }

    public void setOutID(int OutID) {
        this.OutID = OutID;
    }

    public Date getOutDate() {
        return OutDate;
    }

    public void setOutDate(Date OutDate) {
        this.OutDate = OutDate;
    }

    public int getTargetID() {
        return TargetID;
    }

    public void setTargetID(int TargetID) {
        this.TargetID = TargetID;
    }

    public String getOutContent() {
        return OutContent;
    }

    public void setOutContent(String OutContent) {
        this.OutContent = OutContent;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String UserName) {
        this.UserName = UserName;
    }

    

    public int getUserID() {
        return UserID;
    }

    public void setUserID(int UserID) {
        this.UserID = UserID;
    }

    @Override
    public Outbound clone(){
        Outbound result = null;
        
            try {
                result = (Outbound)super.clone();
            } catch (CloneNotSupportedException ex) {
                Logger.getLogger(Outbound.class.getName()).log(Level.SEVERE, null, ex);
            }
       
        return result; 
    }
        
        
}
