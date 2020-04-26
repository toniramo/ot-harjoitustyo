package woj.domain;

import java.sql.Timestamp;

/**
 * Class for representing user made observation.
 * Stores observation date (timestamp) and site (observationSite), 
 * user that has made the observation (createdBy), 
 * measurements - temperature, relative humidity (rh), rainfall, pressure -
 * weather description and optional comment. 
 * These can be accessed with getters and setters.
 */
public class Observation {
    private Site observationSite;
    private User createdBy;
    private double temperature;
    private double rh;
    private double rainfall;
    private double pressure;
    private String description;
    private String comment;
    private Timestamp timestamp;

    public Site getObservationSite() {
        return observationSite;
    }

    public void setObservationSite(Site observationSite) {
        this.observationSite = observationSite;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getRh() {
        return rh;
    }

    public void setRh(double rh) {
        this.rh = rh;
    }

    public double getRainfall() {
        return rainfall;
    }

    public void setRainfall(double rainfall) {
        this.rainfall = rainfall;
    }

    public double getPressure() {
        return pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
    
    public Timestamp getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
    
}
