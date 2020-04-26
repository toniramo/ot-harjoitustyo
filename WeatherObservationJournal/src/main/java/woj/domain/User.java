package woj.domain;

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
    
}


