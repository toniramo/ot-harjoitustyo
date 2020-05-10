package woj.domain;

import woj.dao.SiteDao;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Class that enables testing JournalService in isolation from actual SiteDao class
 */
public class FakeSiteDao implements SiteDao {
    List<Site> sites = new ArrayList<>();
    
    public FakeSiteDao() {
        Site site = new Site();
        site.setSitename("testisaitti");
        site.setAddress("saittikatu123");
        site.setDescription("testing");
        site.setCreatedBy("testaaja2");
        sites.add(site); 
    }

    @Override
    public List<Site> getSitesByUsername(String username) {
        return sites.stream().filter(s->s.getCreatedBy().equals(username)).collect(Collectors.toList());
    }

    @Override
    public boolean createSite(Site site) {
        sites.add(site);
        return true;
    }
}
