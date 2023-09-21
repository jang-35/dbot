package com.dbot;

import java.sql.*;

public class DBDriver {
    private static DBDriver dbd;
    private Connection connection = null;
    private Statement statement = null;

    private DBDriver() {
    }

    public static DBDriver getInstance() {
        if (dbd == null) {
            dbd = new DBDriver();
        }
        return dbd;
    }

    public void connect(String db) throws SQLException {
        connection = DriverManager.getConnection(db);
        statement = connection.createStatement();
        statement.setQueryTimeout(20);
    }

    public void dc(String db) throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

    public void update(String update) throws SQLException {
        statement.executeUpdate(update);
    }

    public ResultSet query(String query) throws SQLException {
        if (statement == null) {
            return null;
        }
        return statement.executeQuery(query);
    }

}