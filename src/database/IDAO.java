/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.rowset.CachedRowSet;
import main.controller.LoginFrame;

/**
 * Apply DAO design pattern
 *
 * @author Hoang
 * @param <T>
 */
public interface IDAO<T> {

    List<T> getList();

    boolean insert(T model);

    boolean update(T model);

    boolean delete(T model);

    int getSelectingIndex(int idx);

    void setSelectingIndex(int idx);

    default DBProvider createDB() {
        DBProvider db = new DBProvider();
        db.setDbHost(LoginFrame.config.host);
        db.setDbPort(LoginFrame.config.port);
        db.setDbName(LoginFrame.config.DBName);
        db.setDbUsername(LoginFrame.config.name);
        db.setDbPassword(LoginFrame.config.password);
        return db;
    }

    // Chay CRS de lay ve ket qua query select
    default CachedRowSet getCRS(String query, Object... args) {
        CachedRowSet crs = null;
        try {
            crs = createDB().getCRS(query);
            if (args.length > 0) {
                for (int i = 0; i < args.length; i++) {
                    if (args[i].getClass() == Integer.class) {
                        crs.setInt(i + 1, (int) args[i]);
                    }

                    if (args[i].getClass() == Float.class) {
                        crs.setFloat(i + 1, (float) args[i]);
                    }

                    if (args[i].getClass() == Double.class) {
                        crs.setDouble(i + 1, (double) args[i]);
                    }

                    if (args[i].getClass() == String.class) {
                        crs.setString(i + 1, (String) args[i]);
                    }

                    if (args[i].getClass() == Boolean.class) {
                        crs.setBoolean(i + 1, (boolean) args[i]);
                    }

                    if (args[i].getClass() == Date.class) {
                        crs.setDate(i + 1, (java.sql.Date) args[i]);
                    }
                }
            }
            crs.execute();
        } catch (SQLException ex) {
            Logger.getLogger(IDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return crs;
    }

    // Chay prepared statement de insert, update, delete
    default void runPS(String query, Object... args) throws SQLException {
        DBProvider db = createDB();
        db.start();
        PreparedStatement ps = db.getPreparedStatement(query);
        if (args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                if (args[i].getClass() == Integer.class) {
                    ps.setInt(i + 1, (int) args[i]);
                }

                if (args[i].getClass() == Float.class) {
                    ps.setFloat(i + 1, (float) args[i]);
                }

                if (args[i].getClass() == Double.class) {
                    ps.setDouble(i + 1, (double) args[i]);
                }

                if (args[i].getClass() == String.class) {
                    ps.setString(i + 1, (String) args[i]);
                }

                if (args[i].getClass() == Boolean.class) {
                    ps.setBoolean(i + 1, (boolean) args[i]);
                }

                if (args[i].getClass() == java.util.Date.class) {
                    java.util.Date origin = (java.util.Date) args[i];
                    java.sql.Date convert = new java.sql.Date(origin.getTime());
                    ps.setDate(i + 1, convert);
                }

                if (args[i].getClass() == java.sql.Date.class) {
                    ps.setDate(i + 1, (java.sql.Date) args[i]);
                }
            }
        }
        ps.execute();
        db.stop();
    }
}
