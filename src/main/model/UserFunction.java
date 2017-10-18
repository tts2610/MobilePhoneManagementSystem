package main.model;

import java.io.Serializable;

/**
 *
 * @author Hoang
 */
public class UserFunction implements Serializable {

    private static final long serialVersionUID = 2L;

    public String FunctionGroup;
    public String FunctionName;

    public static final String COL_FGROUP = "FunctionGroup";
    public static final String COL_FNAME = "FunctionName";

    // Some constants in database
    public static final String FG_USER = "User";
    public static final String FG_PERMISSION = "Permission";
    public static final String FG_PRODUCT = "Product";
    public static final String FG_BRANCH = "Branch";
    public static final String FG_INBOUND = "Inbound";
    public static final String FG_OUTBOUND = "Outbound";
    public static final String FG_SUPPLIER = "Supplier";
    public static final String FG_ORDER = "Order";
    public static final String FG_SALESOFF = "SalesOff";
    public static final String FG_CUSTOMER = "Customer";
    public static final String FG_CUSTOMERLEVEL = "CustomerLevel";
    public static final String FG_SERVICE = "Service";
    public static final String FG_EMPLOYEE = "Employee";
    public static final String FG_SALARY = "Salary";    //13 features
    public static final String FN_VIEW = "View";
    public static final String FN_UPDATE = "Update";

    public UserFunction(String FunctionGroup, String FunctionName) {
        this.FunctionGroup = FunctionGroup;
        this.FunctionName = FunctionName;
    }

    @Override
    public String toString() {
        return "UserFunction{" + "FunctionGroup=" + FunctionGroup + ", FunctionName=" + FunctionName + '}';
    }
}
