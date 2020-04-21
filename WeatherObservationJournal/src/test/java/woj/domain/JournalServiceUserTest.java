/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package woj.domain;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;


/**
 *
 * @author toniramo
 */
public class JournalServiceUserTest {

    FakeUserDao userDao;
    FakeSiteDao siteDao;
    FakeObservationDao observationDao;
    JournalService service;

    @Before
    public void setUp() {
        userDao = new FakeUserDao();
        siteDao = new FakeSiteDao();
        observationDao = new FakeObservationDao();
        service = new JournalService(userDao, siteDao, observationDao);

    }

    @Test
    public void loggingInAsExistingUserReturnsTrue() {
        boolean result = service.login("testikayttaja");
        assertTrue(result);
    }

    @Test
    public void exstingUserCanLogIn() {
        service.login("testikayttaja");
        assertEquals("Taneli Tuulispaa", service.getLoggedUser().getName());
    }

    @Test
    public void loggingInAsNonExistingUserReturnsFalse() {
        boolean result = service.login("nonExistingUser");
        assertFalse(result);
    }

    @Test
    public void nonExistingUserCantLogIn() {
        service.login("nonExistingUser");
        assertEquals(null, service.getLoggedUser());
    }


    @Test
    public void loggedInUserCanLogOut() {
        service.login("testikayttaja");
        service.logout();
        assertEquals(null, service.getLoggedUser());
    }

    @Test
    public void creatingUserWithUniqueNameReturnsTrue() {
        boolean result = service.createUser("uniqueuser", "Name of Unique User");
        assertTrue(result);
    }

    @Test
    public void creatingUserWithNonUniqueNameReturnFalse() {
        boolean result = service.createUser("testikayttaja", "Ei Uniikki Kayttaja");
        assertFalse(result);
    }
}
