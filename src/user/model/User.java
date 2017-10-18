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
public class User {
    private int userID;
    private String userName;
    private String password;
    private String empName;
    private int empID;
    private boolean userEnable;
    
    public static final String COL_USERID = "UserID";
    public static final String COL_USERNAME = "UserName";
    public static final String COL_PASSWORD = "UserPassword";
    public static final String COL_EMPNAME = "EmpName";
    public static final String COL_EMPID = "EmpID";
    public static final String COL_USERENABLE = "UserEnabled";

    public User() {
    }

    public User(int userID, String userName, String password, String empName, int empID, boolean userEnable) {
        this.userID = userID;
        this.userName = userName;
        this.password = password;
        this.empName = empName;
        this.empID = empID;
        this.userEnable = userEnable;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public int getEmpID() {
        return empID;
    }

    public void setEmpID(int empID) {
        this.empID = empID;
    }

    public boolean isUserEnable() {
        return userEnable;
    }

    public void setUserEnable(boolean userEnable) {
        this.userEnable = userEnable;
    }

    @Override
    public String toString() {
        return "User{" + "userID=" + userID + ", userName=" + userName + ", password=" + password + ", empName=" + empName + ", empID=" + empID + ", userEnable=" + userEnable + '}';
    }

    
}
