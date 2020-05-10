package woj.domain;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for testing User class
 */
public class UserTest {
    
    @Test
    public void twoUserObjectsWithSameUsernameAreEqual() {
        User user1 = new User("matti", "Meikäläinen");
        User user2 = new User("matti", "Mäkinen");
        assertTrue(user1.equals(user2));
        assertTrue(user2.equals(user1));
    }
    
    @Test
    public void twoUserObjectsWithDifferentUsernamesAreNotEqual() {
        User user1 = new User("matti1", "Meikäläinen");
        User user2 = new User("matti2", "Meikäläinen");
        assertFalse(user1.equals(user2));
        assertFalse(user2.equals(user1));
    }
    
    @Test
    public void nonUserObjectIsNotEqualWithUserObject() {
        User user1 = new User("matti", "Mäkinen");
        Object user2 = new Object();
        assertFalse(user1.equals(user2));
    }
    
}
