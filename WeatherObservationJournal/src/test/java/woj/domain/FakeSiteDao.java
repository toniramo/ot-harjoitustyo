/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package woj.domain;

import woj.domain.Site;
import woj.dao.SiteDao;
import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * @author toniramo
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

    @Override
    public Site getSite() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean updateSite() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean deleteSite() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
