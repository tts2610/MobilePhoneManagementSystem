/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package employee.model;

import java.util.Date;

/**
 *
 * @author BonBon
 */
public class Employee {

    private int empID;   
    private String empName;
    private String empPhone;
    private Date empBirthday;
    private int empSalary;
    private String empDes;
    private Date empStartDate;
    private int empBonus;
    private boolean empEnabled;

    //Khai bao ten column de hien thi tren table
    public static final String COL_EMPID = "EmpID";    
    public static final String COL_EMPNAME = "EmpName";
    public static final String COL_EMPPHONE = "EmpPhone";
    public static final String COL_EMPSALARY = "BasicSalary";
    public static final String COL_EMPBIRTHDAY = "Birthday";
    public static final String COL_EMPDESIGNATION = "Designation";
    public static final String COL_EMPWORKSTARTDATE = "WorkingStartDate";
    public static final String COL_EMPBONUS = "Bonus";
    public static final String COL_EMPENABLED = "EmpEnabled";

    public Employee() {

    }

    public Employee(String empDes) {
        this.empDes = empDes;
    }

    public Employee(int empID,  String empName, String empPhone, Date empBirthday, int empSalary, String empDes, Date empStartDate, int empBonus, boolean empEnabled) {
        this.empID = empID;        
        this.empName = empName;
        this.empPhone = empPhone;
        this.empBirthday = empBirthday;
        this.empSalary = empSalary;
        this.empDes = empDes;
        this.empStartDate = empStartDate;
        this.empBonus = empBonus;
        this.empEnabled = empEnabled;
    }

    public int getEmpID() {
        return empID;
    }

    public void setEmpID(int empID) {
        this.empID = empID;
    }
    
    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getEmpPhone() {
        return empPhone;
    }

    public void setEmpPhone(String empPhone) {
        this.empPhone = empPhone;
    }

    public int getEmpSalary() {
        return empSalary;
    }

    public void setEmpSalary(int empSalary) {
        this.empSalary = empSalary;
    }

    public Date getEmpBirthday() {
        return empBirthday;
    }

    public void setEmpBirthday(Date empBirthday) {
        this.empBirthday = empBirthday;
    }

    public String getEmpDes() {
        return empDes;
    }

    public void setEmpDes(String empDes) {
        this.empDes = empDes;
    }

    public Date getEmpStartDate() {
        return empStartDate;
    }

    public void setEmpStartDate(Date empStartDate) {
        this.empStartDate = empStartDate;
    }

    public int getEmpBonus() {
        return empBonus;
    }

    public void setEmpBonus(int empBonus) {
        this.empBonus = empBonus;
    }

    public boolean isEmpEnabled() {
        return empEnabled;
    }

    public void setEmpEnabled(boolean empEnabled) {
        this.empEnabled = empEnabled;
    }

    @Override
    public String toString() {
        return "Employee{" + "empID=" + empID + ", empName=" + empName + ", empPhone=" + empPhone + ", empBirthday=" + empBirthday + ", empSalary=" + empSalary + ", empDes=" + empDes + ", empStartDate=" + empStartDate + ", empBonus=" + empBonus + ", empEnabled=" + empEnabled + '}';
    }

}
