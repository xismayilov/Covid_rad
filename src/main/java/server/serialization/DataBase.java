package server.serialization;

import server.user.User;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;

public class DataBase {
    private Queue<User> usersQueue;
    private List<User> users;

    public DataBase(){
        users = Serialiser.Deserialize();
        if (users == null)
            users = new LinkedList<>();

        usersQueue = new LinkedList<>();
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

    public User signIn(String username, String password) {
        Optional<User> result = users.stream()
                .filter(user -> user.getUsername().equals(username) && user.getPassword().equals(password))
                .findAny();

        return result.orElse(null);
    }

    public boolean isInQueue(User user){
        return usersQueue.contains(user);
    }

    public void addToQueue(User user){
        usersQueue.add(user);
    }

    public int getQueueSize(){
        return usersQueue.size();
    }

    public int getQueueNumber(User user){
        int i = 1;
        for (User u : usersQueue) {
            if (u.equals(user))
                break;
            i+=1;
        }
        return i;
    }

    public User removeFromQueue(){
        return usersQueue.poll();
    }
}
