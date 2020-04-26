/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package woj.dao;

import java.io.File;
import java.sql.Timestamp;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import woj.domain.Observation;
import woj.domain.Site;
import woj.domain.User;

/**
 *
 * @author toniramo
 */
public class SQLiteObservationTest {

    SiteDao siteDao;
    UserDao userDao;
    ObservationDao observationDao;
    String testUrl = "jdbc:sqlite:wojTest.db";

    User user;
    Site site;
    Observation o;

    public SQLiteObservationTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        siteDao = new SQLiteSiteDao(testUrl);
        userDao = new SQLiteUserDao(testUrl);
        observationDao = new SQLiteObservationDao(testUrl);

        user = new User("testaaja", "Testi Käyttäjä");
        userDao.createUser(user.getUsername(), user.getName());

        site = new Site();
        site.setSitename("testisaitti");
        site.setAddress("Testiosoite 123");
        site.setDescription("Parvekkeella");
        site.setCreatedBy("testaaja");
        siteDao.createSite(site);
    }

    @Test
    public void gettingObservationsOfSiteAndUserWithoutObservationsReturnsEmptyList() {
        assertTrue(observationDao.getObservationsBySiteAndUser(site, user).isEmpty());
    }

    @Test
    public void gettingObservationsOfSiteAndUserWithOneObservationReturnsListWithOneItem() {
        o = new Observation();
        Timestamp timestamp = new Timestamp(1587427200);
        o.setTimestamp(timestamp);
        o.setTemperature(10);
        o.setRh(89.9);
        o.setRainfall(18.1);
        o.setPressure(1000);
        o.setDescription("Storm");
        o.setComment("Pahapäivä");
        o.setObservationSite(site);
        o.setCreatedBy(user);
        observationDao.createObservation(o);

        assertTrue(observationDao.getObservationsBySiteAndUser(site, user).size() == 1);
    }
    
    @Test
    public void addingObservationToOneSiteAndGettingObservationsOfAnoutherSiteReturnEmptyList() {
        Site anotherSite = new Site();
        anotherSite.setSitename("testisaitti2");
        anotherSite.setAddress("Testiosoite 321");
        anotherSite.setDescription("Terassi");
        anotherSite.setCreatedBy("testaaja");
        siteDao.createSite(anotherSite);
        
        o = new Observation();
        Timestamp timestamp = new Timestamp(1587427200);
        o.setTimestamp(timestamp);
        o.setTemperature(10);
        o.setRh(89.9);
        o.setRainfall(18.1);
        o.setPressure(1000);
        o.setDescription("Storm");
        o.setComment("Melkoinen myräkkä");
        o.setObservationSite(anotherSite);
        o.setCreatedBy(user);
        observationDao.createObservation(o);

        assertTrue(observationDao.getObservationsBySiteAndUser(site, user).isEmpty());
    }

    @After
    public void tearDown() {
        try {
            File db = new File("wojTest.db");
            db.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
