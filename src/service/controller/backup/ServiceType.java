/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service.controller.backup;

import service.model.*;

/**
 *
 * @author BonBon
 */
public class ServiceType {

    private int serTypeID;
    private String serTypeName;    

    public static final String COL_ID = "ServiceTypeID";
    public static final String COL_NAME = "ServiceTypeName";

    public ServiceType() {
    }

    public ServiceType(int serTypeID, String serTypeName) {
        this.serTypeID = serTypeID;
        this.serTypeName = serTypeName;
    }

    public int getSerTypeID() {
        return serTypeID;
    }

    public void setSerTypeID(int serTypeID) {
        this.serTypeID = serTypeID;
    }

    public String getSerTypeName() {
        return serTypeName;
    }

    public void setSerTypeName(String serTypeName) {
        this.serTypeName = serTypeName;
    }

    @Override
    public String toString() {
        return "ServiceType{" + "serTypeID=" + serTypeID + ", serTypeName=" + serTypeName + '}';
    }
       
}
