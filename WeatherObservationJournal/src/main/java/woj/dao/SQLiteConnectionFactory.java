package woj.dao;

import java.sql.*;

/**
 * Class to establish connection to the SQLite database
 */
public class SQLiteConnectionFactory {

    String driverClassName = "org.sqlite.JDBC";
    String url;

    private static SQLiteConnectionFactory connectionFactory = null;

    private SQLiteConnectionFactory(String url) {
        this.url = url;
        try {
            Class.forName(driverClassName);
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }
    }

    public Connection getConnection() throws SQLException {
        Connection connection;
        connection = DriverManager.getConnection(url);
        return connection;
    }
    
    public static SQLiteConnectionFactory getInstance(String url) {
        
        if (connectionFactory == null) {  
            connectionFactory = new SQLiteConnectionFactory(url);
        }
        
        return connectionFactory;
    }

}
