package woj.domain;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class to test Site class
 */
public class SiteTest {

    @Test
    public void siteObjectIsEqualWithItself() {
        Site site1 = new Site("test", "123" , "");
        assertTrue(site1.equals(site1));
    }

    @Test
    public void twoSiteObjectsWithSameSitenameAreEqual() {
        Site site1 = new Site("testSite", "Katu 123", "Kuvaus");
        Site site2 = new Site("testSite", "Katu 321", "Suavuk");
        assertTrue(site1.equals(site2));
        assertTrue(site2.equals(site1));
    }

    @Test
    public void twoSiteObjectsWithDifferentSitenamesAreNotEqual() {
        Site site1 = new Site("testSite", "Katu 123", "Kuvaus");
        Site site2 = new Site("siteTest", "Katu 123", "Kuvaus");
        assertFalse(site1.equals(site2));
        assertFalse(site2.equals(site1));
    }

    @Test
    public void nonSiteObjectIsNotEqualWithUserObject() {
        Site site1 = new Site("testSite", "Katu 123", "Kuvaus");
        Object site2 = new Object();
        assertFalse(site1.equals(site2));
    }

}
