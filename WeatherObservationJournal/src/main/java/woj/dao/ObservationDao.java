/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package woj.dao;

import woj.domain.Observation;
import woj.domain.User;
import woj.domain.Site;
import java.util.List;

/**
 *
 * @author toniramo
 */
public interface ObservationDao {
    List<Observation> getAllObservations();
    List<Observation> getObservationsBySite(Site site);
    List<Observation> getObservationsByUser(User user);
    List<Observation> getObservationsBySiteAndUser(Site site, User user);
    boolean createObservation(Observation observation);
    boolean updateObservation(Observation observation);
    boolean deleteObservation(Observation observation);
}
