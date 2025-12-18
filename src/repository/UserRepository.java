package repository;

import model.User;
import java.util.*;

public class UserRepository {
    private Map<String, User> users;
    private Map<String, User> usersByEmail;
    
    public UserRepository() {
        this.users = new HashMap<>();
        this.usersByEmail = new HashMap<>();
    }
    
    public void addUser(User user) {
        users.put(user.getId(), user);
        usersByEmail.put(user.getEmail().toLowerCase(), user);
    }
    
    public User getUser(String id) {
        return users.get(id);
    }
    
    public User getUserByEmail(String email) {
        return usersByEmail.get(email.toLowerCase());
    }
    
    public boolean userExists(String email) {
        return usersByEmail.containsKey(email.toLowerCase());
    }
}