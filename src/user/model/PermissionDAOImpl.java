/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package user.model;

import database.IDAO;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.rowset.CachedRowSet;
import main.controller.LoginFrame;
import utility.SwingUtils;

/**
 *
 * @author BonBon
 */
public class PermissionDAOImpl implements IDAO<Permission> {

    private CachedRowSet crs;   //CRS to update table
    int id;

    public PermissionDAOImpl(int id) {
        this.id = id;
        crs = getCRS("select p.UserID,p.FunctionID,f.FunctionGroup,f.FunctionName FROM Permission p JOIN Functions f ON p.FunctionID=f.FunctionID WHERE p.UserID=?", id);

    }

    @Override
    public List<Permission> getList() {
        List<Permission> perList = new ArrayList<>();
        try {
            if (crs.first()) {
                do {
                    perList.add(new Permission(
                            crs.getInt(Permission.COL_USERID),
                            crs.getInt(Permission.COL_FUNCTION_ID)
                    //                            crs.getString(Permission.COL_FUNCTION_GROUP),
                    //                            crs.getString(Permission.COL_FUNCTION_NAME)
                    ));
                } while (crs.next());
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return perList;
    }

    @Override
    public boolean insert(Permission permission) {
        boolean result = false;
        
        try {

            runPS("INSERT INTO Permission(UserID,FunctionID) values(?,?)",
                    permission.getUserID(),
                    permission.getFunctionID()
            );
            result = true;

        } catch (SQLException ex) {
            Logger.getLogger(PermissionDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public boolean update(Permission permission) {
        return false;
    }

    public boolean delete(User user) {
        boolean result = false;
        try {
            runPS("DELETE Permission WHERE UserID=?",
                    user.getUserID()
            );
            result = true;

        } catch (SQLException ex) {
            Logger.getLogger(PermissionDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public boolean checkRootPermission() {
//        config = (LoginConfig) IOUtils.readObject(getClass().getResource("config").getPath());
        String name = LoginFrame.config.userName;
        boolean result = false;
        CachedRowSet crs1 = getCRS("SELECT UserID from Users WHERE UserName=?",
                name
        );
        try {
            if (crs1.first()) {
                if (!(crs1.getInt(1) == 1)) {
                    result=false;
                } else {
                    
                    result = true;
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(PermissionDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int getSelectingIndex(int idx) {
        return 0;
    }

    @Override
    public void setSelectingIndex(int idx) {

    }

    @Override
    public boolean delete(Permission model) {
        return false;
    }

}
