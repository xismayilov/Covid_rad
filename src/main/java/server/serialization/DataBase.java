package server.serialization;

import server.user.User;

import java.util.LinkedList;
import java.util.List;

public class DataBase {
    private List<User> users;

    public DataBase(){
        users = Serialiser.Deserialize();
        if (users == null)
            users = new LinkedList<>();
    }

    public void addUser(User user){
        users.add(user);
        Serialiser.Serialize(users);
    }

    public boolean isValidUsername(String username){
        return users
                .stream()
                .noneMatch(user -> user.getUsername().equals(username));
    }

    public boolean isValidUser(User user){
        return !users.contains(user);
    }
}
