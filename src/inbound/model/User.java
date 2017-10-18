/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inbound.model;

/**
 *
 * @author tuan
 */
public class User {
    private int userid;
    private String username;
    private Boolean userEnabled;
    
    public static final String COL_ID = "UserID";
    public static final String COL_NAME="UserName";
    public static final String COL_Status="UserEnabled";

    public User() {
    }

    public User(int userid, String username, Boolean userEnabled) {
        this.userid = userid;
        this.username = username;
        this.userEnabled = userEnabled;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Boolean getUserEnabled() {
        return userEnabled;
    }

    public void setUserEnabled(Boolean userEnabled) {
        this.userEnabled = userEnabled;
    }
    
}
