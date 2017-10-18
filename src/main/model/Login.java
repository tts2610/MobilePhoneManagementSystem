package main.model;

import database.DBProvider;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Hoang
 */
public class Login implements Serializable {
    private static final long serialVersionUID = 1L;
    // Khai bao cac tri default
    public static final String HOST = DBProvider.HOST;
    public static final String PORT = DBProvider.PORT;
    public static final String DBNAME = DBProvider.DBNAME;
    public static final String NAME = DBProvider.NAME;
    public static final String PASSWORD = DBProvider.PASSWORD;
    public static final String USER_NAME = "root";
    public static final String USER_PASSWORD = "123123";
    public static final List<UserFunction> USER_FUNCTIONS = Arrays.asList(
            new UserFunction(UserFunction.FG_USER, UserFunction.FN_VIEW),
            new UserFunction(UserFunction.FG_USER, UserFunction.FN_UPDATE),
            new UserFunction(UserFunction.FG_PERMISSION, UserFunction.FN_VIEW),
            new UserFunction(UserFunction.FG_PERMISSION, UserFunction.FN_UPDATE),
            new UserFunction(UserFunction.FG_PRODUCT, UserFunction.FN_VIEW),
            new UserFunction(UserFunction.FG_PRODUCT, UserFunction.FN_UPDATE),
            new UserFunction(UserFunction.FG_BRANCH, UserFunction.FN_VIEW),
            new UserFunction(UserFunction.FG_BRANCH, UserFunction.FN_UPDATE),
            new UserFunction(UserFunction.FG_INBOUND, UserFunction.FN_VIEW),
            new UserFunction(UserFunction.FG_INBOUND, UserFunction.FN_UPDATE),
            new UserFunction(UserFunction.FG_OUTBOUND, UserFunction.FN_VIEW),
            new UserFunction(UserFunction.FG_OUTBOUND, UserFunction.FN_UPDATE),
            new UserFunction(UserFunction.FG_SUPPLIER, UserFunction.FN_VIEW),
            new UserFunction(UserFunction.FG_SUPPLIER, UserFunction.FN_UPDATE),
            new UserFunction(UserFunction.FG_ORDER, UserFunction.FN_VIEW),
            new UserFunction(UserFunction.FG_ORDER, UserFunction.FN_UPDATE),
            new UserFunction(UserFunction.FG_SALESOFF, UserFunction.FN_VIEW),
            new UserFunction(UserFunction.FG_SALESOFF, UserFunction.FN_UPDATE),
            new UserFunction(UserFunction.FG_CUSTOMER, UserFunction.FN_VIEW),
            new UserFunction(UserFunction.FG_CUSTOMER, UserFunction.FN_UPDATE),
            new UserFunction(UserFunction.FG_CUSTOMERLEVEL, UserFunction.FN_VIEW),
            new UserFunction(UserFunction.FG_CUSTOMERLEVEL, UserFunction.FN_UPDATE),
            new UserFunction(UserFunction.FG_SERVICE, UserFunction.FN_VIEW),
            new UserFunction(UserFunction.FG_SERVICE, UserFunction.FN_UPDATE),
            new UserFunction(UserFunction.FG_EMPLOYEE, UserFunction.FN_VIEW),
            new UserFunction(UserFunction.FG_EMPLOYEE, UserFunction.FN_UPDATE),
            new UserFunction(UserFunction.FG_SALARY, UserFunction.FN_VIEW),
            new UserFunction(UserFunction.FG_SALARY, UserFunction.FN_UPDATE)
            );

    public String host;
    public String port;
    public String DBName;
    public String name;
    public String password;
    public String userName;
    public String userPassword;
    public List<UserFunction> userFunctions;

    public Login() {
    }

    public Login(String host, String port, String DBName, String name, String password, String userName, String userPassword, List<UserFunction> userFunctions) {
        this.host = host;
        this.port = port;
        this.DBName = DBName;
        this.name = name;
        this.password = password;
        this.userName = userName;
        this.userPassword = userPassword;
        this.userFunctions = userFunctions;
    }

    @Override
    public String toString() {
        return "Login{" + "host=" + host + ", port=" + port + ", DBName=" + DBName + ", name=" + name + ", password=" + password + ", userName=" + userName + ", userPassword=" + userPassword + ", userFunctions=" + userFunctions + '}';
    }
}
