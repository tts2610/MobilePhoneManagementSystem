/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package user.model;

/**
 *
 * @author BonBon
 */
public class Permission {
    int userID;
    int functionID;
    
    public static final String COL_USERID = "UserID";
    public static final String COL_FUNCTION_ID = "FunctionID";
    public static final String COL_FUNCTION_GROUP = "FunctionGroup";
    public static final String COL_FUNCTION_NAME = "FunctionName";

    public Permission() {
    }

    public Permission(int userID, int functionID) {
        this.userID = userID;
        this.functionID = functionID;
    }

    

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getFunctionID() {
        return functionID;
    }

    public void setFunctionID(int functionID) {
        this.functionID = functionID;
    }

    @Override
    public String toString() {
        return "Permission{" + "userID=" + userID + ", functionID=" + functionID + '}';
    }

   
    
    
    
}
