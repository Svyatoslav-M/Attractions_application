package Account;

public class User {
    private String user_name;
    private String email;

    public User(String user_name, String email) {
        this.user_name = user_name;
        this.email = email;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getEmail() {
        return email;
    }
}
