package user.model;


/**
 *
 * @author BonBon
 */
public class UserEmployee {
    private int EmpID;
    private String EmpName;
    
    
    public static final String COL_ID = "EmpID";
    public static final String COL_NAME = "EmpName";    

    public UserEmployee() {
    }

    public UserEmployee(int EmpID, String EmpName) {
        this.EmpID = EmpID;
        this.EmpName = EmpName;
    }

    public int getEmpID() {
        return EmpID;
    }

    public void setEmpID(int EmpID) {
        this.EmpID = EmpID;
    }

    public String getEmpName() {
        return EmpName;
    }

    public void setEmpName(String EmpName) {
        this.EmpName = EmpName;
    }

    
}
