package database;

import com.sun.rowset.CachedRowSetImpl;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.rowset.CachedRowSet;

public class DBProvider {

    private final String dbType = "jdbc:sqlserver://";
    private String dbHost, dbPort, dbName, dbUsername, dbPassword;
    private Connection connection;
    private PreparedStatement preparedStatement;
    private CallableStatement callableStatement;
    private ResultSet resultSet;

    // Khai bao cac tri default
//    public static final String HOST = "10.211.55.7";
    public static final String HOST = "localhost";
    public static final String PORT = "1433";
    public static final String DBNAME = "F21508T1C_MobileShopSystemManage";
    public static final String NAME = "sa";
//    public static final String PASSWORD = "26101996";
//    public static final String PASSWORD = "1";
    public static final String PASSWORD = "111";
    

    public static void main(String[] args) throws SQLException {
        // Dung de test ket noi db
    }

    public DBProvider() {
        dbHost = HOST;
        dbPort = PORT;
        dbName = DBNAME;
        dbUsername = NAME;
        dbPassword = PASSWORD;
    }

    public DBProvider(String dbHost, String dbPort, String dbName, String dbUsername, String dbPassword) {
        this.dbHost = dbHost;
        this.dbPort = (dbPort == null || dbPort == "") ? "1433" : dbPort;
        this.dbName = dbName;
        this.dbUsername = dbUsername;
        this.dbPassword = dbPassword;
    }

    public boolean start() {
        boolean result = false;
        try {
            connection = DriverManager.getConnection(getDbUrl(), dbUsername, dbPassword);
            result = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void stop() {
        try {
            if (connection != null) {
                connection.close();
            }
            if (resultSet != null) {
                resultSet.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (callableStatement != null) {
                callableStatement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getDbUrl() {
        if (dbPort.isEmpty()) {
            return dbType + dbHost + ";DatabaseName=" + dbName;
        } else {
            return dbType + dbHost + ":" + dbPort + ";DatabaseName=" + dbName;
        }
    }

    public String getFullDbUrl() {
        if (dbPort.isEmpty()) {
            return dbType + dbHost + ";DatabaseName=" + dbName + ";UID=" + dbUsername + ";Password=" + dbPassword;
        } else {
            return dbType + dbHost + ":" + dbPort + ";DatabaseName=" + dbName + ";UID=" + dbUsername + ";Password=" + dbPassword;
        }
    }

    public String getDbHost() {
        return dbHost;
    }

    public void setDbHost(String dbHost) {
        this.dbHost = dbHost;
    }

    public String getDbPort() {
        return dbPort;
    }

    public void setDbPort(String dbPort) {
        this.dbPort = dbPort;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getDbUsername() {
        return dbUsername;
    }

    public void setDbUsername(String dbUsername) {
        this.dbUsername = dbUsername;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }

    public Connection getConnection() {
        return connection;
    }

    public ResultSet getResultSet(String query) {
        try {
            resultSet = connection.createStatement().executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public PreparedStatement getPreparedStatement(String query) {
        try {
            preparedStatement = connection.prepareStatement(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return preparedStatement;
    }

    public CallableStatement getCallableStatement(String query) {
        try {
            callableStatement = connection.prepareCall(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return callableStatement;
    }

    public CachedRowSet getCRS(String query) {
        CachedRowSet cachedRowSet = null;
        try {
            cachedRowSet = new CachedRowSetImpl();
            cachedRowSet.setUrl(getDbUrl());
            cachedRowSet.setUsername(dbUsername);
            cachedRowSet.setPassword(dbPassword);
            cachedRowSet.setCommand(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cachedRowSet;
    }
}
