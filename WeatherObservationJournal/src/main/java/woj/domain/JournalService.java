package woj.domain;

import woj.dao.SiteDao;
import woj.dao.UserDao;
import java.util.*;
import woj.dao.ObservationDao;

/**
 * Class responsible for application logic
 */
public class JournalService {

    private UserDao userDao;
    private SiteDao siteDao;
    private ObservationDao observationDao;
    private User loggedIn;

    /**
     * Create new JournalService
     *
     * @param userDao Data access object for accessing user related data
     * @param siteDao Data access object for accessing site related data
     * @param observationDao Data access object for accessing observation related data
     */
    public JournalService(UserDao userDao, SiteDao siteDao, ObservationDao observationDao) {
        this.userDao = userDao;
        this.siteDao = siteDao;
        this.observationDao = observationDao;
    }
    
    /**
     * Log in user with chosen username
     * 
     * @param username
     * @return true if login is successful, otherwise false
     */
    public boolean login(String username) {
        User user = userDao.getUserByUsername(username);
        if (user == null) {
            return false;
        }

        loggedIn = user;
        return true;
    }
    
    /**
     * Get logged user as User object
     * @return logged user
     */
    public User getLoggedUser() {
        return loggedIn;
    }
    
    /**
     * Logs out logged user
     */
    public void logout() {
        loggedIn = null;
    }
    
    /**
     * Creates new user
     * @param username unique username (e.g. "user123")
     * @param name actual name of the user (e.g. "Matti Meikäläinen")
     * @return true if user was created successfully, otherwise false
     */
    public boolean createUser(String username, String name) {
        if (userDao.getUserByUsername(username) != null) {
            return false;
        }

        try {
            userDao.createUser(username, name);
        } catch (Exception e) {
            System.out.println("Exception: " + e);
            return false;
        }
        return true;
    }

    /**
     * Create new observation site
     * @param site Site object
     * @return true if creating new site was successful, otherwise false
     */
    public boolean createSite(Site site) {
        site.setCreatedBy(loggedIn.getUsername());

        if (this.getSitesOfLoggedUser().contains(site)) {
            return false;
        }

        try {
            siteDao.createSite(site);
            return true;

        } catch (Exception e) {
            System.out.println("Exception " + e);

        }

        return false;
    }
    
    /**
     * Get sites of user that has logged in
     * @return List of sites that the logged user has created (empty list if there are no sites for the user in question)
     */
    public List<Site> getSitesOfLoggedUser() {
        List<Site> sites = siteDao.getSitesByUsername(loggedIn.getUsername());
        return sites;
    }
    
    /**
     * Create new observation for the user that is logged in
     * @param observation New observation having timestamp, measurements, description, comment and observation site
     * @return true if creating new observation was successful, otherwise false
     */
    public boolean createObservation(Observation observation) {
        if (observation.getObservationSite() != null && observation.getTimestamp() != null) {
            observation.setCreatedBy(loggedIn);
            try {
                observationDao.createObservation(observation);
                return true;
            } catch (Exception e) {
                System.out.println("Exception " + e);
            }
        }
        return false;

    }

    /**
     * Get observations of certain site of the user that is logged in
     * @param site Site object containing unique sitename, address and description
     * @return List of found sites, empty list if no sites was found
     */
    public List<Observation> getObservationsOfLoggedUserAndChosenSite(Site site) {
        List<Observation> observations = observationDao.getObservationsBySiteAndUser(site, loggedIn);
        return observations;

    }
}
