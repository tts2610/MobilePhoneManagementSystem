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

/**
 *
 * @author BonBon
 */
public class NewUserEmployeeDAOImpl implements IDAO<UserEmployee> {

    private CachedRowSet crs;  //CRS to update table

    public NewUserEmployeeDAOImpl() {
        crs = getCRS("select EmpID,EmpName from Employees WHERE  EmpID NOT IN (select u.EmpID from  Users u ) ");
//        crs = getCRS("select EmpID,EmpName from Employees");
    }

    public boolean isZeroEmployee() {
        boolean result = false;
        try {
            if (!crs.first()) {
                result = true;
            } else {
                result = false;
            }
        } catch (SQLException ex) {
            Logger.getLogger(NewUserEmployeeDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public List<UserEmployee> getList() {
        List<UserEmployee> list = new ArrayList<>();
        try {
            if (crs.first()) {
                do {
                    list.add(new UserEmployee(
                            crs.getInt(UserEmployee.COL_ID),
                            crs.getString(UserEmployee.COL_NAME)));
                } while (crs.next());
            }
        } catch (SQLException ex) {
            Logger.getLogger(NewUserEmployeeDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    @Override
    public boolean insert(UserEmployee model) {
        return false;
    }

    @Override
    public boolean update(UserEmployee model) {
        return false;
    }

    @Override
    public boolean delete(UserEmployee model) {
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
