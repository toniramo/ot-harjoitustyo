/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package woj.domain;

import woj.dao.SiteDao;
import woj.dao.UserDao;
import java.util.*;
import woj.dao.ObservationDao;

/**
 *
 * @author toniramo
 */
public class JournalService {

    private UserDao userDao;
    private SiteDao siteDao;
    private ObservationDao observationDao;
    private User loggedIn;

    public JournalService(UserDao userDao, SiteDao siteDao, ObservationDao observationDao) {
        this.userDao = userDao;
        this.siteDao = siteDao;
        this.observationDao = observationDao;
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

    public List<Site> getSitesOfLoggedUser() {
        List<Site> sites = siteDao.getSitesByUsername(loggedIn.getUsername());
        return sites;
    }

    public boolean createObservation(Observation observation) {
        observation.setCreatedBy(loggedIn);

        try {
            observationDao.createObservation(observation);
            return true;
        } catch (Exception e) {
            System.out.println("Exception " + e);
        }

        return false;

    }

    public List<Observation> getObservationsOfChosenSite(Site site) {
        List<Observation> observations = observationDao.getObservationsBySiteAndUser(site, loggedIn);
        return observations;

    }
}
