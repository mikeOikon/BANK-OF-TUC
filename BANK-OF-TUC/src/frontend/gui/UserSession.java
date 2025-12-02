package frontend.gui;

public class UserSession {
    private static UserSession instance;
    private String userId;
    private String username;
    private String email;

    private UserSession() {}

    public static UserSession getInstance() {
        if (instance == null) instance = new UserSession();
        return instance;
    }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public void clear() {
        userId = null; username = null; email = null;
    }
}
