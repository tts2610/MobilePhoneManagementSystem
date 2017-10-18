/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package employee.model;

import database.IDAO;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.rowset.CachedRowSet;

/**
 *
 * @author Hoang
 */
public class SalaryDAOImpl implements IDAO<Salary> {

    private CachedRowSet crs;  //CRS to update table
    private int selectingIndex;
    private Salary currentOrder;

    /**
     * Dung load du lieu theo yeu cau, khong tu dong load
     *
     * @param ordID
     */
    public void load(int empID) {
        crs = getCRS("select SalaryID,EmpID,PayDay,WorkDays,OffDays,BonusNow,BasicSalaryNow from Salaries WHERE EmpID=? ", empID);
    }

    @Override
    public List<Salary> getList() {
        List<Salary> list = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        try {
            if (crs != null && crs.first()) {
                do {
                    calendar.setTime(crs.getDate(Salary.COL_PAYDAY));
                    int month=calendar.get(Calendar.MONTH);
                    list.add(new Salary(
                            crs.getInt(Salary.COL_SALID),
                            crs.getInt(Salary.COL_EMPID),
                            month,
                            crs.getDate(Salary.COL_PAYDAY),
                            crs.getInt(Salary.COL_WORKDAYS),
                            crs.getInt(Salary.COL_OFFDAYS),
                            crs.getInt(Salary.COL_BONUSNOW),
                            crs.getInt(Salary.COL_SALARYNOW),
                            1
                    ));
                } while (crs.next());
            }
        } catch (SQLException ex) {
            Logger.getLogger(SalaryDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public boolean checkPayDay(Salary s) {
        boolean result=true;
        CachedRowSet crs = getCRS("select PayDay from Salaries where EmpID=?", s.getEmpID());
        try {
            if (crs.first()) {
                do {
                    Date d = crs.getDate("PayDay");
                    Calendar c = Calendar.getInstance();
                    Calendar c1 = Calendar.getInstance();
                    c.setTime(d);
                    int y = c.get(Calendar.YEAR);
                    int m = c.get(Calendar.MONTH);
                    c.setTime(s.getPayDay());
                    int y1 = c1.get(Calendar.YEAR);
                    int m1 = c1.get(Calendar.MONTH);
                    if (y1 == y && m1 == m) {
                        result = false;
                    }
                } while (crs.next());
            }else{
                result = true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(SalaryDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public boolean insert(Salary salary) {
        boolean result = false;
        try {

            runPS("insert into Salaries(EmpID,PayDay,WorkDays,OffDays,BonusNow,BasicSalaryNow) values(?,?,?,?,?,?)", salary.getEmpID(), salary.getPayDay(), salary.getWorkDays(), salary.getOffDays(), salary.getBonus(), salary.getBasicSalary());

            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(SalaryDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public boolean update(Salary salary) {
        boolean result = false;
        try {
//            System.out.println("Run update: "+salary.toString());

            runPS("update Salaries set PayDay=?,WorkDays=?,OffDays=?,BonusNow=?,BasicSalaryNow=? WHERE SalaryID=?", salary.getPayDay(), salary.getWorkDays(), salary.getOffDays(), salary.getBonus(), salary.getBasicSalary(), salary.getSalID());

            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(SalaryDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public boolean update(List<Salary> list) {
        boolean result = false;
        if (currentOrder == null) { // Chua set current order cho DAO
            return false;
        }

        return result;
    }

    @Override
    public boolean delete(Salary salary) {
        boolean result = false;
        try {
            runPS("delete Salaries  WHERE SalaryID=?", salary.getSalID());

            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(SalaryDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public Vector getTotalItemSale(int empID, int month) {
        Vector list = new Vector<>();
        boolean result = false;
        int soluong = 0;
        float total = 0;
        float discount = 0;
        float price = 0;
        int monthSale;
        try {

            CachedRowSet crs1, crs2, crs3;
            crs1 = getCRS("select UserID from Users WHERE EmpID=?", empID);
            if (crs1.first()) {
                int ordID = crs1.getInt("UserID");
                crs2 = getCRS("select OrdID,OrdCusDiscount,OrdDate from Orders where UserID=?", ordID);
                discount = crs2.getFloat("OrdCusDiscount");
                monthSale = crs2.getDate("OrdDate").getMonth() + 1;
                if (crs2.first()) {
                    do {
                        if (month == monthSale) {
                            crs3 = getCRS("select OrdProQty from OrderDetails where OrdID=?", crs2.getInt("OrdID"));
                            if (crs3.first()) {

                                do {
                                    int x = crs3.getInt("OrdProQty");
                                    soluong += x;
                                    float i = (float) x * discount;
                                    total += i;
                                } while (crs2.next());
                                result = true;
                            }
                        }
                    } while (crs2.first());
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(SalaryDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        list.add(result);
        list.add(soluong);
        list.add(total);
        return list;
    }

    @Override
    public int getSelectingIndex(int idx) {
        return 0;
    }

    @Override
    public void setSelectingIndex(int idx) {
        return;
    }

}
