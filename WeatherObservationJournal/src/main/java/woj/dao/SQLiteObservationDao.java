/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package woj.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import woj.domain.Observation;
import woj.domain.Site;
import woj.domain.User;

/**
 *
 * @author toniramo
 */
public class SQLiteObservationDao implements ObservationDao {

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

    public SQLiteObservationDao() {
        try {
            connection = getConnection();
            ps = connection.prepareStatement(""
                    + "CREATE TABLE IF NOT EXISTS Observations("
                    + "timestamp DATETIME, "
                    + "temperature REAL, "
                    + "rh REAL, "
                    + "rainfall REAL, "
                    + "pressure REAL, "
                    + "description TEXT, "
                    + "comment TEXT, "
                    + "site_id INTEGER REFERENCES Sites,"
                    + "user_id INTEGER REFERENCES Users);");
            ps.executeUpdate();

        } catch (Exception e) {
            System.out.println("Exception " + e);
        }
        closeConnection();
    }

    @Override
    public boolean createObservation(Observation observation) {
        boolean observationCreated = false;

        try {
            connection = getConnection();

            ps = connection.prepareStatement("SELECT Users.id as user_id, Sites.id as site_id FROM Users, Sites WHERE username = ? AND sitename = ?;");
            ps.setString(1, observation.getCreatedBy().getUsername());
            ps.setString(2, observation.getObservationSite().getSitename());
            rs = ps.executeQuery();
            int userId = rs.getInt("user_id");
            int siteId = rs.getInt("site_id");

            ps = connection.prepareStatement("INSERT INTO Observations (timestamp, temperature, rh, rainfall, pressure, description, comment, site_id, user_id) VALUES (?,?,?,?,?,?,?,?,?);");
            ps.setTimestamp(1, observation.getTimestamp());
            ps.setDouble(2, observation.getTemperature());
            ps.setDouble(3, observation.getRh());
            ps.setDouble(4, observation.getRainfall());
            ps.setDouble(5, observation.getPressure());
            ps.setString(6, observation.getDescription());
            ps.setString(7, observation.getComment());
            ps.setInt(8, siteId);
            ps.setInt(9, userId);

            ps.executeUpdate();

            observationCreated = true;

        } catch (Exception e) {
            System.out.println("Exception " + e);
        }

        closeConnection();

        return observationCreated;
    }

    @Override
    public List<Observation> getObservationsBySiteAndUser(Site site, User user) {
        
        Observation observation;
        List<Observation> observations = new ArrayList();

        try {
            connection = getConnection();
            ps = connection.prepareStatement("SELECT * FROM Observations WHERE user_id = (SELECT id FROM Users WHERE username = ?) AND site_id = (SELECT id FROM Sites WHERE sitename = ?);");
            ps.setString(1, user.getUsername());
            ps.setString(2, site.getSitename());
            rs = ps.executeQuery();

            while (rs.next()) {
                observation = new Observation();
                observation.setTimestamp(rs.getTimestamp("timestamp"));
                observation.setTemperature(rs.getDouble("temperature"));
                observation.setRh(rs.getDouble("rh"));
                observation.setRainfall(rs.getDouble("rainfall"));
                observation.setPressure(rs.getDouble("pressure"));
                observation.setDescription("description");
                observation.setComment("comment");
                observation.setObservationSite(site);
                observation.setCreatedBy(user);
                observations.add(observation);
            }

        } catch (Exception e) {
            System.out.println("Exception " + e);
        }
        closeConnection();
        
        return observations;
    }

    @Override
    public List<Observation> getAllObservations() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Observation> getObservationsBySite(Site site) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Observation> getObservationsByUser(User user) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean updateObservation(Observation observation) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean deleteObservation(Observation observation) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
