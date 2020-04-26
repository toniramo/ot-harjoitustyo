package woj.domain;

import java.util.Objects;

/**
 * Class representing observation site of application user.
 * Site has a sitename, address, description and user created the site (createdBy).
 * These can be accessed with getters and setters.
 */
public class Site {
    private String sitename;
    private String address;
    private String description;
    private String createdBy;

    public Site() {
        
    }
    
    public Site(String sitename, String address, String description) {
        this.sitename = sitename;
        this.address = address;
        this.description = description;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

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
        final Site other = (Site) obj;
        if (!Objects.equals(this.sitename, other.sitename)) {
            return false;
        }

        return true;
    }
    
    public String getSitename() {
        return sitename;
    }

    public void setSitename(String sitename) {
        this.sitename = sitename;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
    
    @Override
    public String toString() {
        return "Sitename: " + sitename + ", address: " + address + ", description: " + description + ", user: " + createdBy;
    }
    
    
}
