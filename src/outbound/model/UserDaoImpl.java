/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package outbound.model;

import inbound.model.*;
import database.IDAO;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.rowset.CachedRowSet;

/**
 *
 * @author tuan
 */
public class UserDaoImpl implements IDAO<User> {
    private CachedRowSet crs;  //CRS to update table
    public UserDaoImpl() {
        crs = getCRS("select UserID,UserName,UserEnabled from Users");
    }

    
    @Override
    public List<User> getList() {
        List<User> list = new ArrayList<>();
        try {
            
            
            if (crs.first()) {
                do {
                    list.add(new User(
                            crs.getInt(User.COL_ID),
                            crs.getString(User.COL_NAME),
                            crs.getBoolean(User.COL_Status)));
                } while (crs.next());
            }
            
            
        } catch (SQLException ex) {
            Logger.getLogger(UserDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    @Override
    public boolean insert(User model) {
        return false;    
    }

    @Override
    public boolean update(User model) {
        return false;
    }

    @Override
    public boolean delete(User model) {
        return false;
    }

    @Override
    public int getSelectingIndex(int idx) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setSelectingIndex(int idx) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
