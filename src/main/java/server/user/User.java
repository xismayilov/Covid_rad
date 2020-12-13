package server.user;

import java.io.Serializable;
import java.util.Objects;
import java.util.Observable;

/**
 * Common class for users which can be connected to a server.
 */
public abstract class User extends Observable implements Serializable {

    private String email;
    private String password;
    private String username;

    public User(String email, String password, String username) {
        this.email = email;
        this.password = password;
        this.username = username;
    }

    public abstract String getAvailableCommands();
    public abstract boolean isAvailableCommand(String command);

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Creates new user according to specified by a canonical name of a class.
     * @param type  type of a user.
     * @return  new empty user.
     */
    public static User getUserByType(String type) {
        if (type.equals(UserReferent.class.getCanonicalName()))
            return new UserReferent("", "", "");
        else if (type.equals(UserStudent.class.getCanonicalName()))
            return new UserStudent("", "", "");

        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;
        return username.equals(user.username) &&
                password.equals(user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, password, username);
    }
}
