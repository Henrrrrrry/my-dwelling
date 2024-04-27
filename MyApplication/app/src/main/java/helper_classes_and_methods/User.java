package helper_classes_and_methods;

public class User {
    private String username;
    private String password;
    private boolean isMaintainer;

    // Constructor
    public User(String username, String password, boolean isMaintainer) {
        this.username = username;
        this.password = password;
        this.isMaintainer = isMaintainer;
    }

    // Getters
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean isMaintainer() {
        return isMaintainer;
    }

    // Setters
    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setMaintainer(boolean isMaintainer) {
        this.isMaintainer = isMaintainer;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", isMaintainer=" + isMaintainer +
                '}';
    }
}


