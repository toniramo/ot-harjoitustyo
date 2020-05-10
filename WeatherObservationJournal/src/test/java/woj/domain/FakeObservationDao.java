package woj.domain;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import woj.dao.ObservationDao;

/**
 * Class that enables testing JournalService in isolation from actual ObservationDao class
 */
public class FakeObservationDao implements ObservationDao {

    List<Observation> observations = new ArrayList<>();

    public FakeObservationDao() {

        User user = new User();
        user.setUsername("testikayttaja");
        user.setName("Taneli Tuulispaa");

        Site site = new Site();
        site.setSitename("testisaitti");
        site.setAddress("saittikatu123");
        site.setDescription("testing");
        site.setCreatedBy("testaaja2");

        Observation o = new Observation();
        o.setTimestamp(new Timestamp(1587482243));
        o.setTemperature(20.4);
        o.setRh(81.2);
        o.setRainfall(10.1);
        o.setDescription("Rainy");
        o.setComment("Normipäivä");
        o.setCreatedBy(user);
        o.setObservationSite(site);
    }

    @Override
    public boolean createObservation(Observation observation) {
        observations.add(observation);
        return true;
    }

    @Override
    public List<Observation> getObservationsBySiteAndUser(Site site, User user) {
        return observations.stream()
                .filter(o -> o.getCreatedBy().getUsername().equals(user.getUsername()) 
                          && o.getObservationSite().getSitename().equals(site.getSitename()))
                .collect(Collectors.toList());
    }
}
