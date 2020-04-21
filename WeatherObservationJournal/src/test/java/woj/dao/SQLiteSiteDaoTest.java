/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package woj.dao;

import java.io.File;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import woj.domain.Site;

/**
 *
 * @author toniramo
 */
public class SQLiteSiteDaoTest {

    SiteDao siteDao;
    UserDao userDao;
    String testUrl = "jdbc:sqlite:wojTest.db";

    public SQLiteSiteDaoTest() {

    }

    @Before
    public void setUp() {
        siteDao = new SQLiteSiteDao(testUrl);
        userDao = new SQLiteUserDao(testUrl);

        userDao.createUser("testaaja", "Testi Käyttäjä");
    }

    @Test
    public void creatingSiteToExistingUserReturnsTrue() {
        Site site = new Site();
        site.setSitename("testisaitti");
        site.setAddress("Testiosoite 123");
        site.setDescription("Parvekkeella");
        site.setCreatedBy("testaaja");
        assertTrue(siteDao.createSite(site));
    }

    @Test
    public void creatingSiteToNonExistingUserReturnsFalse() {
        Site site = new Site();
        site.setSitename("testisaitti");
        site.setAddress("Testiosoite 123");
        site.setDescription("Parvekkeella");
        site.setCreatedBy("nonexistinguser");
        assertFalse(siteDao.createSite(site));
    }

    @Test
    public void gettingSitesOfUserWithoutSitesReturnsEmptyList() {
        assertTrue(siteDao.getSitesByUsername("testaaja").isEmpty());
    }

    @Test
    public void afterAddingOneSiteToUserGettingSitesReturnsListContainingOneSite() {
        Site site = new Site();
        site.setSitename("testisaitti");
        site.setAddress("Testiosoite 123");
        site.setDescription("Parvekkeella");
        site.setCreatedBy("testaaja");
        siteDao.createSite(site);
        assertEquals(1, siteDao.getSitesByUsername("testaaja").size());
    }

    @Test
    public void addingOneSiteToOneUserDoesNotIncreaseAmountOfOtherUsersSites() {
        userDao.createUser("testaaja2", "Toinen Käyttäjä");
        Site site = new Site();
        site.setSitename("testisaitti");
        site.setAddress("Testiosoite 123");
        site.setDescription("Parvekkeella");
        site.setCreatedBy("testaaja2");
        siteDao.createSite(site);
        assertTrue(siteDao.getSitesByUsername("testaaja").isEmpty());
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
