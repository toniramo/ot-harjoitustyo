/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package woj.domain;

/**
 *
 * @author toniramo
 */

public class User {
    private String username;
    private String name;
    //private String password; // not supported yet

    public User() {
        
    }
    
    public User(String username, String name) {
        this.username = username;
        this.name = name;
    }
    
//    public User(String username, String name, String password) {
//        this.username = username;
//        this.name = name;
//        this.password = password;
//    }
    

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


