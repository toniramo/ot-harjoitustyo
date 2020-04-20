/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package woj.domain;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import woj.dao.ObservationDao;
import woj.dao.SQLiteObservationDao;

/**
 *
 * @author toniramo
 */
public class JournalServiceSiteTest {

    FakeUserDao userDao;
    FakeSiteDao siteDao;
    ObservationDao observationDao;
    JournalService service;

    @Before
    public void setUp() {
        userDao = new FakeUserDao();
        siteDao = new FakeSiteDao();
        observationDao = new SQLiteObservationDao();
        service = new JournalService(userDao, siteDao, observationDao);

    }

    @Test
    public void creatingUniqueSiteReturnsTrue() {
        service.login("testaaja2");
        boolean siteCreated = service.createSite(new Site("testisaitti2", "testaajankatu 987", "parveke"));
        assertTrue(siteCreated);
    }

    @Test
    public void creatingNonUniqueSiteReturnsFalse() {
        service.login("testaaja2");
        boolean siteCreated = service.createSite(new Site("testisaitti", "testitie 8", "satunnainen kommentti"));
        assertFalse(siteCreated);
    }

    @Test
    public void gettingSitesOfUserWithoutSitesReturnsEmptyList() {
        service.login("testikayttaja");
        assertTrue(service.getSitesOfLoggedUser().isEmpty());
    }

    @Test
    public void gettingSitesOfUserWithOneSiteReturnListWithOneSite() {
        service.login("testaaja2");
        assertTrue(service.getSitesOfLoggedUser().size() == 1);
    }

    @Test
    public void addingNewSiteIncreasesUserListByOne() {
        service.login("testaaja2");
        int initialListSize = service.getSitesOfLoggedUser().size();
        boolean siteCreated = service.createSite(new Site("testisaitti5", "testaajankatu 1", "kellari"));
        assertTrue(siteCreated);
        int finalListSize = service.getSitesOfLoggedUser().size();
        assertTrue(finalListSize - initialListSize == 1);
    }

    @Test
    public void addedSiteCanBeFoundFromUserSitesAfterBeingAdded() {
        service.login("testaaja2");

        Site site1 = new Site("testisaitti2", "testitie 8", "satunnainen kommentti");
        Site site2 = new Site("testisaitti5", "testaajankatu 1", "kellari");

        service.createSite(site1);
        assertTrue(service.getSitesOfLoggedUser().contains(site1));

        service.createSite(site2);
        assertTrue(service.getSitesOfLoggedUser().contains(site2));
    }

    @Test
    public void userSiteListDoesNotContainSiteBeforeItIsAdded() {
        service.login("testaaja2");

        Site site1 = new Site("testisaitti2", "testitie 8", "satunnainen kommentti");
        Site site2 = new Site("testisaitti5", "testaajankatu 1", "kellari");
        
        assertFalse(service.getSitesOfLoggedUser().contains(site1));
        service.createSite(site1);

        assertFalse(service.getSitesOfLoggedUser().contains(site2));

    }
}
