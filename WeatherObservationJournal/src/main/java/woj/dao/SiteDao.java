package woj.dao;

import woj.domain.Site;
import java.util.*;

/**
 * Interface of data access object for site related data.
 */
public interface SiteDao {
    
    /**
     * Get sites of chosen user by username
     * @param username username of the chosen user
     * @return list of sites of user in question (empty list if no sites found)
     */
    List<Site> getSitesByUsername(String username);
    
    /**
     * Create new site for chosen user
     * @param site Site object of the new site
     * @return true if new site was created successfully, otherwise false
     */
    boolean createSite(Site site);
}
