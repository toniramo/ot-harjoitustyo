/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import domain.*;
import java.sql.*;
import java.util.*;

/**
 *
 * @author toniramo
 */
public class SQLiteSiteDao implements SiteDao {

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

    public SQLiteSiteDao() {
        try {
            connection = getConnection();
            ps = connection.prepareStatement("CREATE TABLE IF NOT EXISTS Sites(id INTEGER PRIMARY KEY, sitename TEXT UNIQUE, address TEXT, description TEXT, user_id INTEGER REFERENCES Users);");
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("Exception " + e);
        }
        closeConnection();
    }

    @Override
    public List<Site> getSitesByUsername(String username) {
        Site site;
        List<Site> sites = new ArrayList();

        try {
            connection = getConnection();
            ps = connection.prepareStatement("SELECT * FROM Sites WHERE user_id = (SELECT id FROM Users WHERE username = ?);");
            ps.setString(1, username);
            rs = ps.executeQuery();

            while (rs.next()) {
                site = new Site();
                site.setSitename(rs.getString("sitename"));
                site.setAddress(rs.getString("address"));
                site.setDescription(rs.getString("description"));
                site.setCreatedBy(username);
                sites.add(site);
            }

        } catch (Exception e) {
            System.out.println("Exception " + e);
        }
        closeConnection();
        
        return sites;
    }

    @Override
    public boolean createSite(Site site) {
        boolean siteCreated = false;

        try {
            connection = getConnection();
            
            ps = connection.prepareStatement("SELECT id FROM Users WHERE username = ?;");
            ps.setString(1, site.getCreatedBy());
            rs = ps.executeQuery();
            int userId = rs.getInt("id");
            
            ps = connection.prepareStatement("INSERT INTO Sites (sitename, address, description, user_id) VALUES (?,?,?,?);");
            ps.setString(1, site.getSitename());
            ps.setString(2, site.getAddress());
            ps.setString(3, site.getDescription());
            ps.setInt(4, userId);
            ps.executeUpdate();

            siteCreated = true;

        } catch (Exception e) {
            System.out.println("Exception " + e);
        }

        closeConnection();

        return siteCreated;
    }


    @Override
    public boolean updateSite() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean deleteSite() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Site getSite() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
