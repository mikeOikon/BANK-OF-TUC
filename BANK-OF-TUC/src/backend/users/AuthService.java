package backend.users;

import java.util.HashMap;
import java.util.Map;

public class AuthService {

    private static final int MAX_ATTEMPTS = 3;
    private static final long LOCK_MS = 60_000L; // 1 minute

    private final Map<String, Integer> failedAttempts = new HashMap<>();
    private final Map<String, Long> lockUntil = new HashMap<>();

    /**
     * @return the logged-in User if success, otherwise null
     */
    public User authenticate(String username, String password) {
        String key = username == null ? "" : username.trim();
        long now = System.currentTimeMillis();

        Long until = lockUntil.get(key);
        if (until != null && now < until) {
            return null; // locked
        }

        User user = findUserByUsername(key); // <-- θα το συνδέσεις με το δικό σου DAO / list
        if (user == null) {
            // προαιρετικά: μην μετράς attempts για "username not found"
            return null;
        }

        boolean ok = user.login(key, password);
        if (ok) {
            failedAttempts.remove(key);
            lockUntil.remove(key);
            return user;
        }

        int attempts = failedAttempts.getOrDefault(key, 0) + 1;
        failedAttempts.put(key, attempts);

        if (attempts >= MAX_ATTEMPTS) {
            lockUntil.put(key, now + LOCK_MS);
            failedAttempts.put(key, 0);
        }

        return null;
    }

    public boolean isLocked(String username) {
        long now = System.currentTimeMillis();
        Long until = lockUntil.get(username);
        return until != null && now < until;
    }

    public long remainingLockSeconds(String username) {
        long now = System.currentTimeMillis();
        Long until = lockUntil.get(username);
        if (until == null || now >= until) return 0;
        return (until - now + 999) / 1000;
    }

    // TODO: εδώ κουμπώνεις το project σου:
    private User findUserByUsername(String username) {
        // π.χ. return UserService.getInstance().findByUsername(username);
        return null;
    }
}
