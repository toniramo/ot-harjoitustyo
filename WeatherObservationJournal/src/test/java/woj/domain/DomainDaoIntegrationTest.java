package woj.domain;

import java.io.File;
import java.sql.Timestamp;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import woj.dao.*;

/**
 * Test class to integration test domain and dao classes together
 */
public class DomainDaoIntegrationTest {

    UserDao userDao;
    SiteDao siteDao;
    ObservationDao observationDao;
    String testUrl = "jdbc:sqlite:wojTest.db";

    JournalService service;
    
    Site site;
    Observation o;

    public DomainDaoIntegrationTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        userDao = new SQLiteUserDao(testUrl);
        siteDao = new SQLiteSiteDao(testUrl);
        observationDao = new SQLiteObservationDao(testUrl);
        service = new JournalService(userDao, siteDao, observationDao);
        
        userDao.createUser("matti", "Meikäläinen");

        site = new Site();
        site.setSitename("mattisSite");
        site.setAddress("Mettistreet 321");
        site.setDescription("10th floor");
        site.setCreatedBy("matti");
        siteDao.createSite(site);
        
        o = new Observation();
        o.setTimestamp(new Timestamp(System.currentTimeMillis()));
        o.setTemperature(10);
        o.setRh(20);
        o.setRainfall(30);
        o.setPressure(1000);
        o.setObservationSite(site);
    }

    @Test
    public void creatingNonExistingNewUserReturnsTrue() {
        assertTrue(service.createUser("newUser", "New User"));

    }

    @Test
    public void creatingNewUserWithExistingUsernameReturnsFalse() {
        assertFalse(service.createUser("matti", "New User 2"));
    }

    @Test
    public void loggingInWithExistingUsernameReturnsTrue() {
        assertTrue(service.login("matti"));
    }

    @Test
    public void loggingInWithNonExistingUsernameReturnsFalse() {
        assertFalse(service.login("newUser"));
    }

    @Test
    public void creatingNewSiteWithNonExistingSitenameReturnsTrue() {
        service.login("matti");
        Site testSite = new Site();
        testSite.setSitename("newSite");
        testSite.setAddress("New Site Street 123");
        assertTrue(service.createSite(testSite));
    }

    @Test
    public void creatingSiteWithExistingSitenameReturnsFalse() {
        service.login("matti");
        Site testSite = new Site();
        testSite.setSitename("mattisSite");
        testSite.setAddress("New Site Street 123");
        assertFalse(service.createSite(testSite));
    }
    
    @Test
    public void gettingSitesOfUserWithOneSiteReturnsThatSite() {
        service.login("matti");
        assertTrue(service.getSitesOfLoggedUser().contains(site));
    }
    
    @Test
    public void creatingNewObservationReturnsTrue() {
        service.login("matti");
        assertTrue(service.createObservation(o));
    }
    
    @Test
    public void afterAddingNewObservationsReturnsTheSameObservation() {
        service.login("matti");
        service.createObservation(o);
        o.setCreatedBy(service.getLoggedUser());
        assertTrue(service.getObservationsOfLoggedUserAndChosenSite(site).contains(o));
    }
    
    @Test
    public void gettingListOfObservationsDoesNotContainNonExistingObservation() {
        service.login("matti");
        o.setCreatedBy(service.getLoggedUser());
        assertFalse(service.getObservationsOfLoggedUserAndChosenSite(site).contains(o));
    }
    

    @After
    public void tearDown() {
        try {
            File db = new File("wojTest.db");
            db.delete();
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }
    }
}
