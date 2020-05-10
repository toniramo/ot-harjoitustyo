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
