package Model;

public class User {

    private int userID;
    private String user;
    private String password;
    private Boolean active;

    //Constructors
    public User() {
        userID = 0;
        user = null;
        password = null;
        active = false;
    }

    public User(int userID, String user, String password, Boolean active) {
        this.userID = userID;
        this.user = user;
        this.password = password;
        this.active = active;
    }

    //Getters and Setters
    public int getUserID() {
        return userID;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
