/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package woj.domain;

import java.sql.Timestamp;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author toniramo
 */
public class JournalServiceObservationTest {

    FakeUserDao userDao;
    FakeSiteDao siteDao;
    FakeObservationDao observationDao;
    JournalService service;

    String testUsername;
    Site site;
    Observation o;
    Observation o2;
    Observation o3;
    Timestamp timestamp;
    Timestamp timestamp2;

    @Before
    public void setUp() {
        userDao = new FakeUserDao();
        siteDao = new FakeSiteDao();
        observationDao = new FakeObservationDao();
        service = new JournalService(userDao, siteDao, observationDao);

        //Test user
        testUsername = "kayttaja";
        service.createUser(testUsername, "Kalle Käyttäjä");
        service.login(testUsername);

        //Test site
        site = new Site();
        site.setSitename("kayttajansaitti");
        site.setAddress("Kayttajan osoite");
        service.createSite(site);

        //Observation already added for user
        o = new Observation();
        timestamp = new Timestamp(1587427200);
        o.setTimestamp(timestamp);
        o.setTemperature(10);
        o.setRh(89.9);
        o.setRainfall(18.1);
        o.setPressure(1000);
        o.setDescription("Storm");
        o.setComment("Pahapäivä");
        o.setObservationSite(site);
        service.createObservation(o);

        //Observation not yet added for user
        o2 = new Observation();
        timestamp2 = new Timestamp(1587427200);
        o2.setTimestamp(timestamp);
        o2.setTemperature(32.1);
        o2.setRh(31.9);
        o2.setRainfall(0);
        o.setPressure(1003);
        o2.setDescription("Sunny");
        o2.setComment("Kaunis sää!");
        o2.setObservationSite(site);

        //Invalid observation (missing site and timestamp)
        o3 = new Observation();
        o3.setTemperature(59);
        o3.setRh(1);
        o3.setRainfall(100);
        o.setPressure(998);
        o3.setDescription("Cloudy");
        o3.setComment("Epävalidi merkintä.");

    }

    @Test
    public void creatingValidObservationReturnTrue() {
        assertTrue(service.createObservation(o2));
    }

    @Test
    public void creatingInvalidObservationReturnsFalse() {
        assertFalse(service.createObservation(o3));
    }

    @Test
    public void gettingObservationsOfUserWithoutObservationsReturnEmptyList() {
        service.logout();

        service.createUser("UserWithNoSites", "Test User 9123");
        service.login("UserWithNoSites");

        assertTrue(service.getObservationsOfLoggedUserAndChosenSite(site).isEmpty());
    }

    @Test
    public void gettingObservationsOfUserAndSiteWithObservationReturnNonEmptyList() {
        assertTrue(!service.getObservationsOfLoggedUserAndChosenSite(site).isEmpty());
    }

    @Test
    public void addingObservationToCertainUserAndSiteIncreasesListOfObservationsByOne() {
        int countInitially = service.getObservationsOfLoggedUserAndChosenSite(site).size();
        service.createObservation(o2);
        int countAfter = service.getObservationsOfLoggedUserAndChosenSite(site).size();

        assertEquals(1, countAfter - countInitially);
    }

    @Test
    public void addingObservationToOneSiteDoesNotIncreaseNumberOfObservationsInOtherSite() {
        Site newSite = new Site();
        newSite.setSitename("NewSite");
        newSite.setAddress("Address of new site");
        newSite.setCreatedBy(testUsername);
        service.createSite(newSite);

        int countInitially = service.getObservationsOfLoggedUserAndChosenSite(site).size();
        o2.setObservationSite(newSite);
        service.createObservation(o2);
        int countAfter = service.getObservationsOfLoggedUserAndChosenSite(site).size();

        assertEquals(0, countAfter - countInitially);
    }

}
