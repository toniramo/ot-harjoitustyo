package woj.dao;

import woj.domain.User;
import java.sql.*;

/**
 * Class to be used as data access object for user data stored in SQLite
 * database.
 */
public class SQLiteUserDao implements UserDao {

    Connection connection = null;
    PreparedStatement ps = null;
    ResultSet rs;
    String url;

    private Connection getConnection() throws SQLException {
        Connection conn;
        conn = SQLiteConnectionFactory.getInstance(url).getConnection();
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

    /**
     * Create new SQLiteUserDao with chosen database url. Either connection is
     * established to the existing SQLite database (if found via url) or new
     * database is created based on the given url.
     *
     * @param url url of the database (starting with "jdbc:")
     */
    public SQLiteUserDao(String url) {
        this.url = url;
        try {
            connection = getConnection();
            ps = connection.prepareStatement("CREATE TABLE IF NOT EXISTS Users(id INTEGER PRIMARY KEY, username TEXT UNIQUE, name TEXT);");
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("Exception " + e);
        }

        closeConnection();
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
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
}
