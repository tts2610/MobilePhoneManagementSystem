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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.rowset.CachedRowSet;
import main.controller.LoginFrame;
import utility.SwingUtils;

/**
 *
 * @author BonBon
 */
public class EmployeeDAOImpl implements IDAO<Employee> {

    private CachedRowSet crs;   //CRS to update table

    public EmployeeDAOImpl() {
        crs = getCRS("select EmpID, EmpName, EmpPhone, Birthday, BasicSalary,Designation, WorkingStartDate,Bonus, EmpEnabled from Employees ");
        //where EmpID<>1

    }

    @Override
    public List<Employee> getList() {
        List<Employee> employeeList = new ArrayList<>();
        try {
            if (crs.first()) {
                do {
                    employeeList.add(new Employee(
                            crs.getInt(Employee.COL_EMPID),
                            crs.getString(Employee.COL_EMPNAME),
                            crs.getString(Employee.COL_EMPPHONE),
                            crs.getDate(Employee.COL_EMPBIRTHDAY),
                            crs.getInt(Employee.COL_EMPSALARY),
                            crs.getString(Employee.COL_EMPDESIGNATION),
                            crs.getDate(Employee.COL_EMPWORKSTARTDATE),
                            crs.getInt(Employee.COL_EMPBONUS),
                            crs.getBoolean(Employee.COL_EMPENABLED)));
                } while (crs.next());
            }
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return employeeList;
    }

    @Override
    public boolean insert(Employee employee) {
        boolean result = false;

        // Khoi tao tri default de insert vao db
        employee.setEmpName("New Employee");
        employee.setEmpPhone(System.currentTimeMillis() + "");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1);
        employee.setEmpBirthday(calendar.getTime());
        employee.setEmpSalary(0);
        employee.setEmpDes("New Employee");
        employee.setEmpStartDate(calendar.getTime());
        employee.setEmpBonus(0);
        employee.setEmpEnabled(true);
        try {
            runPS("insert into Employees(EmpName, EmpPhone, Birthday,BasicSalary,Designation,WorkingStartDate,"
                    + "Bonus, EmpEnabled) values(?,?,?,?,?,?,?,?)",
                    employee.getEmpName(),
                    employee.getEmpPhone(),
                    employee.getEmpBirthday(),
                    employee.getEmpSalary(),
                    employee.getEmpDes(),
                    employee.getEmpStartDate(),
                    employee.getEmpBonus(),
                    employee.isEmpEnabled()
            );

            // Refresh lai cachedrowset hien thi table
            crs.execute();
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public boolean update(Employee empl) {
//        System.out.println("Deo VO: "+empl.toString());
        boolean result = false;
        try {
            // Check cus phone khong duoc trung            
            CachedRowSet crs1 = getCRS("select * from Employees "
                    + "where EmpPhone = ? AND EmpID <>?", empl.getEmpPhone(), empl.getEmpID());
            CachedRowSet crs2 = getCRS("select UserID from Users where EmpID=?", empl.getEmpID());
            if (crs2.first()&&crs2.getInt("UserID") == 1 && empl.isEmpEnabled() == false) {                
                    SwingUtils.showErrorDialog("Can't disable employee with permission ROOT !"); 
                
            } else if (crs1.first()) {
                SwingUtils.showErrorDialog("Phone cannot be duplicated !");
            } else {
                runPS("update Employees set EmpName=?, EmpPhone=?,"
                        + " Birthday=?,BasicSalary=?,Designation=?,WorkingStartDate = ?,Bonus=?, EmpEnabled=? where EmpID=?",
                        empl.getEmpName(),
                        empl.getEmpPhone(),
                        empl.getEmpBirthday(),
                        empl.getEmpSalary(),
                        empl.getEmpDes(),
                        empl.getEmpStartDate(),
                        empl.getEmpBonus(),
                        empl.isEmpEnabled(),
                        empl.getEmpID()
                );
                if (empl.isEmpEnabled() == false) {
//                CachedRowSet crs11=getCRS("select UserID ",);
                    runPS("update Users set UserEnabled=? where EmpID =?", 0, empl.getEmpID());
                } else {
                    runPS("update Users set UserEnabled=? where EmpID =?", 1, empl.getEmpID());
                }
                // Refresh lai cachedrowset hien thi table                
                crs.execute();
                result = true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public boolean delete(Employee employee
    ) {
        boolean result = false;
        try {
            //Check emp co salary khong, neu co thi khong cho delete
            CachedRowSet crs1 = getCRS("select * from Salaries  where EmpID=?", employee.getEmpID());
            CachedRowSet crs2 = getCRS("select * from Users  where EmpID=?", employee.getEmpID());
            if (crs1.first()) {
                SwingUtils.showErrorDialog("Empployee have salary !");
            } else if (crs2.first()) {
                SwingUtils.showErrorDialog("Empployee have an account user !");
            } else {
                runPS("delete from Employees where EmpID=?", employee.getEmpID());

                // Refresh lai cachedrowset hien thi table
                crs.execute();
                result = true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public String getUserNameFromEmpID(int id) {
        String username = "";
        CachedRowSet crs = getCRS("select UserName from Users where EmpID = ?", id);
        try {
            if (crs.first()) {
                username = crs.getString("UserName");
            }
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return username;
    }

    public Employee getEmpFromUserName(String userName) {
        Employee emp = new Employee(0, "", "", new Date(), 0, "", new Date(), 0, false);
        CachedRowSet crs = getCRS("select EmpID,EmpName,EmpPhone,Birthday,BasicSalary,Designation,WorkingStartDate,EmpEnabled,Bonus from Employees where EmpID=(select EmpID from Users where UserName=?)", userName);
        try {
            if (crs.first()) {
                emp.setEmpID(crs.getInt(Employee.COL_EMPID));
                emp.setEmpName(crs.getString(Employee.COL_EMPNAME));
                emp.setEmpPhone(crs.getString(Employee.COL_EMPPHONE));
                emp.setEmpBirthday(crs.getDate(Employee.COL_EMPBIRTHDAY));
                emp.setEmpSalary(crs.getInt(Employee.COL_EMPSALARY));
                emp.setEmpDes(crs.getString(Employee.COL_EMPDESIGNATION));
                emp.setEmpStartDate(crs.getDate(Employee.COL_EMPWORKSTARTDATE));
                emp.setEmpEnabled(crs.getBoolean(Employee.COL_EMPENABLED));
                emp.setEmpBonus(crs.getInt(Employee.COL_EMPBONUS));
            }
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return emp;
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
