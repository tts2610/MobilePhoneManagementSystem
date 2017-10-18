/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this tuserate file, choose Tools | Tuserates
 * and open the tuserate in the editor.
 */
package user.model;

import database.IDAO;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.rowset.CachedRowSet;
import utility.SwingUtils;

/**
 *
 * @author BonBon
 */
public class UserDAOImpl implements IDAO<User> {

    private CachedRowSet crs;   //CRS to update table

    public UserDAOImpl() {
        crs = getCRS("select u.UserID,u.UserName,u.UserPassword,e.EmpName,u.EmpID,u.UserEnabled from Users u join Employees e on u.EmpID=e.EmpID"); 
        //where UserID<>1
    }

    public User getUserFromName(String name) {
        User result = null;
        CachedRowSet crs4;
        try {

            crs4 = getCRS("select u.UserID,u.UserName,u.UserPassword,e.EmpName,u.EmpID,u.UserEnabled from Users u join Employees e on u.EmpID=e.EmpID where UserName = ?", name);
            if (crs4.first()) {
                result = new User(
                        crs4.getInt(User.COL_USERID),
                        crs4.getString(User.COL_USERNAME),
                        crs4.getString(User.COL_PASSWORD),
                        crs4.getString(User.COL_EMPNAME),
                        crs4.getInt(User.COL_EMPID),
                        crs4.getBoolean(User.COL_USERENABLE));
            }

        } catch (SQLException ex) {
            Logger.getLogger(UserDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public boolean checkRoot(String name) {
        CachedRowSet crs2;
        boolean result = false;
        crs2 = getCRS("select UserID from Users where UserName=?", name);
        try {
            if (crs2.first() && (crs2.getInt(1) == 1)) {
                result = true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public boolean checkChangePassForAdmin(String userName, User selectedUser) {
        boolean result;
        CachedRowSet crs1, crs2;
        int funtionIDOnline = 0, funtionIDSelected = 0;
        //functionID cua row selected
        crs1 = getCRS("select FunctionID from Permission where UserID = ?", selectedUser.getUserID());
        //funtionID cua user dang online
        crs2 = getCRS("select FunctionID from Permission where UserID=(select UserID from Users where UserName=?)", userName);
        try {
            if (crs2.first()) {
                do {
                    if (crs2.getInt("FunctionID") == 2) {
                        funtionIDOnline = 2;
                    }
                } while (crs2.next());
            }
            if (crs1.first()) {
                do {
                    if (crs1.getInt("FunctionID") == 2) {
                        funtionIDSelected = 2;
                    }
                } while (crs1.next());
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (!userName.equals("root")) {
            if (funtionIDOnline == 2 && funtionIDSelected != 2) {
                result = true;
            } else if (funtionIDOnline == 2 && userName.equals(selectedUser.getUserName())) {
                result = true;
            } else if (funtionIDOnline != 2) {
                return true;
            } else {
                return false;
            }
        } else {
            result = true;
        }
        return result;
    }

    public boolean checkPermissionUpdate(String name) {
        CachedRowSet crs1;
        boolean result = false;
        try {

            crs1 = getCRS("select FunctionID from Permission where UserID=(select UserID from Users where UserName=?)", name);
            if (crs1.first()) {
                do {
                    if (crs1.getInt("FunctionID") == 2) {
                        result = true;
                        System.out.println("Co quyen update user");
                    }

                } while (crs1.next());

            }

        } catch (SQLException ex) {
            Logger.getLogger(UserDAOImpl.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public boolean checkUpdateRecord(String userName, User selectedUser) {
        boolean result = false;
        CachedRowSet crs1, crs2;
        int funtionIDOnline = 0, funtionIDSelected = 0;
        //functionID cua row selected
        crs1 = getCRS("select FunctionID from Permission where UserID = ?", selectedUser.getUserID());
        //funtionID cua user dang online
        crs2 = getCRS("select FunctionID from Permission where UserID=(select UserID from Users where UserName=?)", userName);
        try {
            if (crs2.first()) {
                do {
                    if (crs2.getInt("FunctionID") == 2) {
                        funtionIDOnline = 2;
                    }
                } while (crs2.next());
            }
            if (crs1.first()) {
                do {
                    if (crs1.getInt("FunctionID") == 2) {
                        funtionIDSelected = 2;
                    }
                } while (crs1.next());
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (funtionIDOnline == 2) {
            if (userName.equals("root")) {
                result = true;
            } else if (funtionIDSelected != 2) {
                result = true;
            } else if (userName.equals(selectedUser.getUserName())) {
                result = false;
            } else {
                result = false;
            }
        } else if (funtionIDOnline != 2) {
            result = false;
        }
        return result;
    }

    @Override
    public List<User> getList() {
        List<User> userList = new ArrayList<>();
        try {
            if (crs.first()) {
                do {
                    userList.add(new User(
                            crs.getInt(User.COL_USERID),
                            crs.getString(User.COL_USERNAME),
                            crs.getString(User.COL_PASSWORD),
                            crs.getString(User.COL_EMPNAME),
                            crs.getInt(User.COL_EMPID),
                            crs.getBoolean(User.COL_USERENABLE)));
                } while (crs.next());

            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDAOImpl.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return userList;
    }

    @Override
    public boolean insert(User user) {
        boolean result = false;
        //Lay EmpID va EmpName chua co trong user
        CachedRowSet crs1 = getCRS("select EmpID,EmpName from Employees WHERE  EmpID NOT IN (select u.EmpID from  Users u)");
        List<UserEmployee> list = new ArrayList<>();
//       

        try {
            if (!crs1.first()) {
                SwingUtils.showErrorDialog("All employees have account, can't insert user more ! Please insert more employee before");
            } else {
//                System.out.println("Co gia tri: ");
                do {
                    list.add(new UserEmployee(
                            crs1.getInt(UserEmployee.COL_ID),
                            crs1.getString(UserEmployee.COL_NAME)));
                } while (crs1.next());                
                // Khoi tao tri default de insert vao db
                user.setUserName(System.currentTimeMillis() + "");
                user.setPassword("1");
                user.setEmpID(list.get(0).getEmpID());
                user.setEmpName(list.get(0).getEmpName());
                user.setUserEnable(true);
                try {
                    runPS("insert into Users(UserName,UserPassword,EmpID,UserEnabled) values(?,?,?,?)",
                            user.getUserName(),
                            user.getPassword(),
                            user.getEmpID(),
                            user.isUserEnable()
                    );

                    // Refresh lai cachedrowset hien thi table
                    crs.execute();
                    result = true;

                } catch (SQLException ex) {
                    Logger.getLogger(UserDAOImpl.class
                            .getName()).log(Level.SEVERE, null, ex);

                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDAOImpl.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

        return result;
    }

    @Override
    public boolean update(User user
    ) {
        boolean result = false;
        try {
            // Check cus phone khong duoc trung            
            CachedRowSet crs1 = getCRS("select * from Users "
                    + "where UserName = ? AND UserID <>?", user.getUserName(), user.getUserID());
            CachedRowSet crs2 = getCRS("select * from Users "
                    + "where EmpID = ? AND UserID <>?", user.getEmpID(), user.getUserID());

            if (user.getUserID() == 1) {
                SwingUtils.showErrorDialog("ADMIN ROOT can't be update !");
            } else if (crs1.first()) {
                SwingUtils.showErrorDialog("UserName cannot be duplicated !");
            } else if (crs2.first()) {
                SwingUtils.showErrorDialog("EmpID cannot be duplicated !");
            } else {
//                System.out.println(user.toString());
                runPS("update Users set UserName=?, "
                        + " EmpID=?, UserEnabled=? where UserID=?",
                        user.getUserName(),
                        //                        user.getPassword(),
                        user.getEmpID(),
                        user.isUserEnable(),
                        user.getUserID()
                );

                // Refresh lai cachedrowset hien thi table                
                crs.execute();
                result = true;

            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDAOImpl.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public boolean delete(User user
    ) {
        boolean result = false;
        try {
            //Check user co o table khac khong, neu co thi khong cho delete            
            CachedRowSet crs3 = getCRS("select * from Orders Where UserID=?", user.getUserID());
            CachedRowSet crs4 = getCRS("select * from Service Where UserID=?", user.getUserID());
            CachedRowSet crs5 = getCRS("select * from Inbounds Where UserID=?", user.getUserID());
            CachedRowSet crs6 = getCRS("select * from Outbounds Where UserID=?", user.getUserID());
            CachedRowSet crs7 = getCRS("select * from Permission Where UserID=?", user.getUserID());
            if (user.getUserID() == 1) {
                SwingUtils.showErrorDialog("ADMIN ROOT can't be delete !");
            } else if (crs3.first()) {
                SwingUtils.showErrorDialog("This user is working at Order !");
            } else if (crs4.first()) {
                SwingUtils.showErrorDialog("This user is working at Service !");
            } else if (crs5.first()) {
                SwingUtils.showErrorDialog("This user is working at Inbounds !");
            } else if (crs6.first()) {
                SwingUtils.showErrorDialog("This user is working at Outbounds !");
            } else if (crs7.first()) {
                SwingUtils.showErrorDialog("Please disable all permission of this user first !");
            } else {
                runPS("delete from Users where UserID=?", user.getUserID());

                // Refresh lai cachedrowset hien thi table
                crs.execute();
                result = true;

            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDAOImpl.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return result;

    }

    @Override
    public int getSelectingIndex(int idx
    ) {
        return 0;
    }

    @Override
    public void setSelectingIndex(int idx
    ) {

    }

}
