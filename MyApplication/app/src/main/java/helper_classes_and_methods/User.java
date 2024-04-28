package helper_classes_and_methods;

public class User implements Observer {
    private String username;
    private String password;
    private boolean isMaintainer;
    private String userID;

    // Constructor


    public User(String username, String password, boolean isMaintainer, String userID) {
        this.username = username;
        this.password = password;
        this.isMaintainer = isMaintainer;
        this.userID = userID;
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

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", isMaintainer=" + isMaintainer +
                '}';
    }

    @Override
    public void update(String location) {
        System.out.println("Hi,Dear "+userID+ ", there's an emergency at your followed address"+location+".");
    }

    @Override
    public void maintainUpdate(String location) {
        System.out.println("Hi,Dear "+userID+ ", the building you are responsible for: "+location+" need repairs.");
    }
}


