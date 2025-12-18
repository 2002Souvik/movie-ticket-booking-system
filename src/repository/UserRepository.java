package repository;

import model.User;
import util.CsvUtil;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class UserRepository {
    private Map<String, User> users;
    private Map<String, User> usersByEmail;
    private final String CSV_PATH = "data/users.csv";

    public UserRepository() {
        this.users = new HashMap<>();
        this.usersByEmail = new HashMap<>();
        // ensure CSV has header and load existing users
        CsvUtil.ensureHeaderExists(CSV_PATH, "userId,name,email,phone");
        loadFromCsv();
    }

    public void addUser(User user) {
        users.put(user.getId(), user);
        usersByEmail.put(user.getEmail().toLowerCase(), user);
        // append to CSV
        String line = String.join(",",
                CsvUtil.escape(user.getId()),
                CsvUtil.escape(user.getName()),
                CsvUtil.escape(user.getEmail()),
                CsvUtil.escape(user.getPhone())
        );
        CsvUtil.appendLine(CSV_PATH, line);
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

    private void loadFromCsv() {
        Path p = Paths.get(CSV_PATH);
        if (!Files.exists(p)) return;
        try {
            List<String> lines = Files.readAllLines(p);
            boolean first = true;
            for (String line : lines) {
                if (first) { first = false; continue; }
                if (line.trim().isEmpty()) continue;
                // simple split (doesn't handle quoted commas fully)
                String[] parts = line.split(",");
                if (parts.length < 4) continue;
                String id = parts[0].replaceAll("^\"|\"$", "");
                String name = parts[1].replaceAll("^\"|\"$", "");
                String email = parts[2].replaceAll("^\"|\"$", "");
                String phone = parts[3].replaceAll("^\"|\"$", "");
                User u = new User(id, name, email, phone);
                users.put(id, u);
                usersByEmail.put(email.toLowerCase(), u);
            }
        } catch (IOException e) {
            System.err.println("Failed to load users from CSV: " + e.getMessage());
        }
    }
}