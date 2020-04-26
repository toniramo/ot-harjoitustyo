package woj.dao;

import woj.domain.Observation;
import woj.domain.User;
import woj.domain.Site;
import java.util.List;

/**
 * Interface of data access object for observation related data.
 */
public interface ObservationDao {
    
    /**
     * Get observations of chosen user and site.
     * @param site Site object of chosen observation site
     * @param user User object of chosen application user
     * @return list of observations of chosen user and site (empty list if no observations were found)
     */
    List<Observation> getObservationsBySiteAndUser(Site site, User user);
    
    /**
     * Create new observation and save it for later access
     * @param observation Observation object of the new observation
     * @return true if creating and saving new observation was successful, otherwise false
     */
    boolean createObservation(Observation observation);
}
