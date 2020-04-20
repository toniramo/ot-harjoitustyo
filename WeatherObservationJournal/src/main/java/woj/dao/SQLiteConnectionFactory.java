package woj.dao;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author toniramo
 */

import java.sql.*;


public class SQLiteConnectionFactory {

    String driverClassName = "org.sqlite.JDBC";
    String url = "jdbc:sqlite:woj.db"; // TODO read from configuration file

    private static SQLiteConnectionFactory connectionFactory = null;

    private SQLiteConnectionFactory() {
        try {
            Class.forName(driverClassName);
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }
    }

    public Connection getConnection() throws SQLException {
        Connection connection = null;
        connection = DriverManager.getConnection(url);
        return connection;
    }
    
    public static SQLiteConnectionFactory getInstance() {
        
        if (connectionFactory == null) {
            connectionFactory = new SQLiteConnectionFactory();
        }
        
        return connectionFactory;
    }

}
