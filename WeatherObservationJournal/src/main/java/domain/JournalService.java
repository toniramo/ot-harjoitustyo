/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import dao.*;
import java.util.*;

/**
 *
 * @author toniramo
 */
public class JournalService {

    private UserDao userDao;
    private SiteDao siteDao;
    private User loggedIn;

    public JournalService(UserDao userDao, SiteDao siteDao) {
        this.userDao = userDao;
        this.siteDao = siteDao;
    }

    public boolean login(String username) {
        User user = userDao.getUserByUsername(username);
        if (user == null) {
            return false;
        }

        loggedIn = user;
        return true;
    }

    public User getLoggedUser() {
        return loggedIn;
    }

    public void logout() {
        loggedIn = null;
    }

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

    public boolean createSite(Site site) {

        try {
            siteDao.createSite(site);
            return true;

        } catch (Exception e) {
            System.out.println("Exception " + e);

        }

        return false;
    }

    public List<Site> getSitesOfLoggedUser() {
        List<Site> sites = siteDao.getSitesByUsername(loggedIn.getUsername());
        return sites;
    }
}
