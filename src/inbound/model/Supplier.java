/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inbound.model;

import supplier.model.*;

/**
 *
 * @author tuan
 */
public class Supplier {
    private int supID;
    private String supName;
    private String supAddress;
    private boolean supStatus;
    
    public static final String COL_ID = "SupID";
    public static final String COL_Name = "SupName";
    public static final String COL_Address = "SupAddress";
    public static final String COL_Status = "SupEnabled";
    
    public static final String Query_Show = "select SupID,SupName,SupAddress,SupEnabled from suppliers where SupEnabled = 1";
    public static final String Query_Insert = "insert into suppliers values(?,?,?)";
    public static final String Query_Update = "update suppliers set "+COL_Name+"=?,"+COL_Address+"=?,"+COL_Status+"=?"+" where "+COL_ID+"=?";
    public static final String Query_Delete = "delete suppliers where "+COL_ID+"=?";

    public Supplier(int supID, String supName, String supAddress, boolean supStatus) {
        this.supID = supID;
        this.supName = supName;
        this.supAddress = supAddress;
        this.supStatus = supStatus;
    }
    
    public Supplier(){
        
    }

    public int getSupID() {
        return supID;
    }

    public void setSupID(int supID) {
        this.supID = supID;
    }

    public String getSupName() {
        return supName;
    }
    
    public boolean getSupStatus(){
        return supStatus;
    }

    public void setSupName(String supName) {
        this.supName = supName;
    }

    public String getSupAddress() {
        return supAddress;
    }

    public void setSupAddress(String supAddress) {
        this.supAddress = supAddress;
    }

    public boolean isSupStatus() {
        return supStatus;
    }

    public void setSupStatus(boolean supStatus) {
        this.supStatus = supStatus;
    }

    @Override
    public String toString() {
        return "Supplier{" + "supID=" + supID + ", supName=" + supName + ", supAddress=" + supAddress + ", supStatus=" + supStatus + '}';
    }
    
    
    
}
