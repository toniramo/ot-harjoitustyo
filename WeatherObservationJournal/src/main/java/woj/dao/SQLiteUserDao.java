/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package woj.dao;

import woj.domain.User;
import java.sql.*;
import java.util.Set;

/**
 *
 * @author toniramo
 */
public class SQLiteUserDao implements UserDao {

    Connection connection = null;
    PreparedStatement ps = null;
    ResultSet rs;

    private Connection getConnection() throws SQLException {
        Connection conn;
        conn = SQLiteConnectionFactory.getInstance().getConnection();
        return conn;
    }

    private void closeConnection() {
        try {
            if (ps != null) {
                ps.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (Exception e) {
            System.out.println("Exception " + e);
        }
    }

    public SQLiteUserDao() {
        try {
            connection = getConnection();
            ps = connection.prepareStatement("CREATE TABLE IF NOT EXISTS Users(id INTEGER PRIMARY KEY, username TEXT UNIQUE, name TEXT);");
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("Exception " + e);
        }

        closeConnection();
    }

    @Override
    public User getUserByUsername(String username) {

        User user = null;

        try {
            connection = getConnection();
            ps = connection.prepareStatement("SELECT * FROM Users WHERE username = ?;");
            ps.setString(1, username);
            rs = ps.executeQuery();

            if (rs.next()) {
                user = new User();
                user.setUsername(rs.getString("username"));
                user.setName(rs.getString("name"));
            }
        } catch (Exception e) {
            System.out.println("Exception " + e);
        }

        closeConnection();

        return user;

    }

    @Override
    public boolean createUser(String username, String name) {
        boolean userCreated = false;
        try {
            connection = getConnection();
            ps = connection.prepareStatement("INSERT INTO Users (username, name) VALUES (?,?);");
            ps.setString(1, username);
            ps.setString(2, name);
            ps.executeUpdate();
            //System.out.println("User " + name + " with username " + username + " added succesfully.");
            userCreated = true;

        } catch (Exception e) {
            System.out.println("Exception " + e);
        }

        closeConnection();

        return userCreated;
    }

//    public int getUserId(String username) {
//        User user = null;
//
//        try {
//            connection = getConnection();
//            ps = connection.prepareStatement("SELECT * FROM Users WHERE username = ?;");
//            ps.setString(1, username);
//            rs = ps.executeQuery();
//
//            if (rs.next()) {
//                user = new User();
//                user.setUsername(rs.getString("username"));
//                user.setName(rs.getString("name"));
//            }
//        } catch (Exception e) {
//            System.out.println("Exception " + e);
//        }
//
//        closeConnection();
//
//        return user;
//    }


    @Override
    public User getUser() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Set<User> getAllUsers() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean updateUser() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean deleteUser() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
