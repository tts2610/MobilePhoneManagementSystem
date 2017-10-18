/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package supplier.model;

/**
 *
 * @author tuan
 */
public class SupplierBranch {
    private int braID;
    private String braName;
    private boolean braStatus;
    private String supName;
    private int supID;
    
    public static final String COL_ID = "BraID";
    public static final String COL_Name = "BraName";
    public static final String COL_Status = "BraEnabled";
    public static final String COL_SupName = "SupName";
    public static final String COL_SupID = "SupID";
    
    
    public static final String Query_Show = "select braid,braname,braEnabled,suppliers.supname,suppliers.supid from branches join suppliers on suppliers.supid=branches.supid order by braid desc";

    public SupplierBranch() {
    }

    public SupplierBranch(int braID, String braName, boolean braStatus, int supID) {
        this.braID = braID;
        this.braName = braName;
        this.braStatus = braStatus;
        this.supID = supID;
    }

    public int getBraID() {
        return braID;
    }

    public void setBraID(int braID) {
        this.braID = braID;
    }

    public String getBraName() {
        return braName;
    }

    public void setBraName(String braName) {
        this.braName = braName;
    }

    public boolean isBraStatus() {
        return braStatus;
    }

    public void setBraStatus(boolean braStatus) {
        this.braStatus = braStatus;
    }

    public String getSupName() {
        return supName;
    }

    public void setSupName(String supName) {
        this.supName = supName;
    }

    public int getSupID() {
        return supID;
    }

    public void setSupID(int supID) {
        this.supID = supID;
    }
    
}
