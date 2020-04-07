/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package woj.dao;

import woj.domain.Observation;
import woj.domain.User;
import woj.domain.Site;
import java.util.Set;

/**
 *
 * @author toniramo
 */
public interface ObservationDao {
    Observation getObservation();
    Set<Observation> getAllObservations();
    boolean createObservation(Site site, User user, Observation observation);
    boolean updateSite();
    boolean deleteSite();
}
