/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package woj.dao;

import woj.domain.Site;
import java.util.*;

/**
 *
 * @author toniramo
 */
public interface SiteDao {
    Site getSite();
    //List<Site> getAllSites();
    List<Site> getSitesByUsername(String username);
    boolean createSite(Site site);
    boolean updateSite();
    boolean deleteSite();    
}
