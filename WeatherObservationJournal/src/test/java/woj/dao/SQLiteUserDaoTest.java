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

/**
 *
 * @author toniramo
 */
public class SQLiteUserDaoTest {
    UserDao userDao;
    String testUrl = "jdbc:sqlite:wojTest.db";
    
    public SQLiteUserDaoTest() {
    }
    
    
    @Before
    public void setUp() {
        userDao = new SQLiteUserDao(testUrl);
    }
    
    @Test
    public void creatingNewUserReturnsTrue() {
        assertTrue(userDao.createUser("testaaja", "Timo Torniainen"));
    }
    
    @Test
    public void creatingSameUserTwiceReturnsFalseOnSecondAttempt() {
        userDao.createUser("testaaja", "Timo Torniainen");
        assertFalse(userDao.createUser("testaaja", "Timo Torniainen"));
    }
    
    @Test
    public void gettingNonExistingUserReturnsNull() {
        assertEquals(null,userDao.getUserByUsername("testaaja"));
    }
    
    @Test
    public void gettingUserByUsernameAfterBeingAddedResultsToSameUsername() {
        userDao.createUser("testaaja", "Taina Tiistai");
        assertEquals("testaaja", userDao.getUserByUsername("testaaja").getUsername());
    }
    
   @Test
    public void gettingUserAfterBeingAddedResultsToSameName() {
        userDao.createUser("testaaja5", "Taina Tiistai");
        assertEquals("Taina Tiistai", userDao.getUserByUsername("testaaja5").getName());
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
