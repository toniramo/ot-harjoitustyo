package woj.domain;

import java.sql.Timestamp;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class to test Observation class
 */
public class ObservationTest {

    @Test
    public void siteObjectIsEqualWithItself() {
        Observation o1 = new Observation();
        o1.setTimestamp(new Timestamp(System.currentTimeMillis()));
        o1.setTemperature(-20);
        o1.setRh(5);
        o1.setRainfall(0);
        o1.setPressure(1010);
        o1.setDescription("Sunny");
        o1.setComment("Kommentti");

        assertTrue(o1.equals(o1));
    }

    @Test
    public void twoObservationsWithSameTimestampUserSiteAndMeasurementsAreEqual() {
        Timestamp time = new Timestamp(System.currentTimeMillis());
        Site site = new Site("site", "Address", "Description");
        User user = new User("user", "Name");

        Observation o1 = new Observation();
        o1.setTimestamp(time);
        o1.setTemperature(-20);
        o1.setRh(5);
        o1.setRainfall(0);
        o1.setPressure(1010);
        o1.setDescription("Sunny");
        o1.setComment("Kommentti");
        o1.setObservationSite(site);
        o1.setCreatedBy(user);

        Observation o2 = new Observation();
        o2.setTimestamp(time);
        o2.setTemperature(-20);
        o2.setRh(5);
        o2.setRainfall(0);
        o2.setPressure(1010);
        o2.setDescription("Sunny");
        o2.setComment("Kommentti2");
        o2.setObservationSite(site);
        o2.setCreatedBy(user);
        
        assertTrue(o1.equals(o2));
        assertTrue(o2.equals(o1));
    }

    @Test
    public void twoObservationsWithDifferentSitesAreNotEqual() {
        Timestamp time = new Timestamp(System.currentTimeMillis());
        Site site1 = new Site("site", "Address", "Description");
        Site site2 = new Site("site2", "123" , "abc");
        User user = new User("user", "Name");

        Observation o1 = new Observation();
        o1.setTimestamp(time);
        o1.setTemperature(-20);
        o1.setRh(5);
        o1.setRainfall(0);
        o1.setPressure(1010);
        o1.setDescription("Sunny");
        o1.setComment("Kommentti");
        o1.setObservationSite(site1);
        o1.setCreatedBy(user);

        Observation o2 = new Observation();
        o2.setTimestamp(time);
        o2.setTemperature(-20);
        o2.setRh(5);
        o2.setRainfall(0);
        o2.setPressure(1010);
        o2.setDescription("Sunny");
        o2.setComment("Kommentti2");
        o2.setObservationSite(site2);
        o2.setCreatedBy(user);
        
        assertFalse(o1.equals(o2));
        assertFalse(o2.equals(o1));
    }
    
    @Test
    public void twoObservationsWithDifferentUsersAreNotEqual() {
        Timestamp time = new Timestamp(System.currentTimeMillis());
        Site site = new Site("site", "Address", "Description");
        User user = new User("user", "Name");

        Observation o1 = new Observation();
        o1.setTimestamp(time);
        o1.setTemperature(-20);
        o1.setRh(5);
        o1.setRainfall(0);
        o1.setPressure(1010);
        o1.setDescription("Sunny");
        o1.setComment("Kommentti");
        o1.setObservationSite(site);
        o1.setCreatedBy(user);

        Observation o2 = new Observation();
        o2.setTimestamp(time);
        o2.setTemperature(-20);
        o2.setRh(5);
        o2.setRainfall(0);
        o2.setPressure(1010);
        o2.setDescription("Sunny");
        o2.setComment("Kommentti2");
        o2.setObservationSite(site);
        o2.setCreatedBy(new User("user2", "Name"));
        
        assertFalse(o1.equals(o2));
        assertFalse(o2.equals(o1));
    }

    @Test
    public void nonSiteObjectIsNotEqualWithUserObject() {
        Timestamp time = new Timestamp(System.currentTimeMillis());
        Site site = new Site("site", "Address", "Description");
        User user = new User("user", "Name");

        Observation o1 = new Observation();
        o1.setTimestamp(time);
        o1.setTemperature(-20);
        o1.setRh(5);
        o1.setRainfall(0);
        o1.setPressure(1010);
        o1.setDescription("Sunny");
        o1.setComment("Kommentti");
        o1.setObservationSite(site);
        o1.setCreatedBy(user);
        
        Object o2 = new Object();
        
        assertFalse(o1.equals(o2));
    }
}
