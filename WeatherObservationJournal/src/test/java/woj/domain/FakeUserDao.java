package woj.domain;

import woj.dao.UserDao;
import java.util.*;

/**
 * Class that enables testing JournalService in isolation from actual UserDao class
 */
public class FakeUserDao implements UserDao {
    List<User> users = new ArrayList<>();
    
    public FakeUserDao() {
        users.add(new User("testikayttaja", "Taneli Tuulispaa"));
        users.add(new User("testaaja2", "Tiina Testikäyttäjä"));
    }

    @Override
    public User getUserByUsername(String username) {
        return users.stream().filter(u->u.getUsername().equals(username)).findFirst().orElse(null);
    }

    @Override
    public boolean createUser(String username, String name) {
        User user = new User(username, name);
        users.add(user);
        return true;
    }
}
