package woj.domain;

import java.util.Objects;

/**
 * Class representing the application user with individual username and actual name.
 * These can be accessed with getters and setters.
 */

public class User {
    private String username;
    private String name;

    public User() {
        
    }
    
    public User(String username, String name) {
        this.username = username;
        this.name = name;
    }
    

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * Compare if two objects are equal User objects (i.e. they have same username)
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
        final User other = (User) obj;
        return Objects.equals(this.username, other.username);
    }
}


