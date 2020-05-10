package woj.domain;

import java.sql.Timestamp;
import java.util.Objects;

/**
 * Class for representing user made observation. Stores observation date
 * (timestamp) and site (observationSite), user that has made the observation
 * (createdBy), measurements - temperature, relative humidity (rh), rainfall,
 * pressure - weather description and optional comment. These can be accessed
 * with getters and setters.
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

    /**
     * Compare if two objects are equal Observation objects
     * @param obj object to be compared with
     * @return true if object are same, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Observation other = (Observation) obj;
        if (Double.doubleToLongBits(this.temperature) != Double.doubleToLongBits(other.temperature)) {
            return false;
        }
        if (Double.doubleToLongBits(this.rh) != Double.doubleToLongBits(other.rh)) {
            return false;
        }
        if (Double.doubleToLongBits(this.rainfall) != Double.doubleToLongBits(other.rainfall)) {
            return false;
        }
        if (Double.doubleToLongBits(this.pressure) != Double.doubleToLongBits(other.pressure)) {
            return false;
        }
        if (!Objects.equals(this.description, other.description)) {
            return false;
        }
        if (!Objects.equals(this.observationSite, other.observationSite)) {
            return false;
        }
        if (!Objects.equals(this.createdBy, other.createdBy)) {
            return false;
        }
        return Objects.equals(this.timestamp, other.timestamp);
    }
}
