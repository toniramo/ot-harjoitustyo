package woj.dao;

import woj.domain.User;

/**
 * Interface of data access object for application user related data.
 */
public interface UserDao {
    User getUserByUsername(String username);
    boolean createUser(String username, String name);
}
