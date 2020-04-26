/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package woj.dao;

import woj.domain.User;
import java.util.Set;

/**
 * Interface of data access object for application user related data.
 */
public interface UserDao {
    User getUserByUsername(String username);
    boolean createUser(String username, String name);
}
