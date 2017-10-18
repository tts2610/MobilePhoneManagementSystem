/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.model;

import database.IDAO;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.rowset.CachedRowSet;

/**
 *
 * @author Hoang
 */
public class LoginDAOImpl implements IDAO<Login> {

    private CachedRowSet crs1;
    private CachedRowSet crs2;

    public LoginDAOImpl(String userName) {
        // Chi lay cac user duoc enable
        crs1 = getCRS("select * from Users where UserName=? AND UserEnabled=1", userName);
        crs2 = getCRS("select FunctionGroup, FunctionName from Users u join Permission p on u.UserID=p.UserID join Functions f on p.FunctionID=f.FunctionID where u.UserName=? AND u.UserEnabled=1 order by f.FunctionID", userName);
    }

    @Override
    public List<Login> getList() {
        List<Login> list1 = new ArrayList<>();
        List<UserFunction> list2 = new ArrayList<>();
        try {
            // Load functions of user
            if (crs2.first()) {
                do {
                    list2.add(new UserFunction(
                            crs2.getString(UserFunction.COL_FGROUP),
                            crs2.getString(UserFunction.COL_FNAME)));
                } while (crs2.next());
            }

            // Load user info
            if (crs1.first()) {
                do {
                    list1.add(new Login(
                            Login.HOST,
                            Login.PORT,
                            Login.DBNAME,
                            Login.NAME,
                            Login.PASSWORD,
                            crs1.getString("UserName"),
                            crs1.getString("UserPassword"),
                            list2));
                } while (crs1.next());
            }
        } catch (SQLException ex) {
            Logger.getLogger(LoginDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list1;
    }

    @Override
    public boolean insert(Login model) {
        return false;
    }

    @Override
    public boolean update(Login model) {
        return false;
    }

    @Override
    public boolean delete(Login model) {
        return false;
    }

    @Override
    public int getSelectingIndex(int idx) {
        return 0;
    }

    @Override
    public void setSelectingIndex(int idx) {
    }

}
