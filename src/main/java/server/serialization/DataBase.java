package server.serialization;

import server.user.User;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;

public class DataBase {
    private Queue<User> usersQueue; // Queue of students.
    private List<User> users;       // All users which are known on the server.

    public DataBase(){
        users = Serialiser.DeserializeUsers();
        if (users == null)
            users = new LinkedList<>();

        usersQueue = Serialiser.DeserializeQueue();
        if (usersQueue == null)
            usersQueue = new LinkedList<>();
    }

    /**
     * Adds new user to a database.
     * @param user a user which we want to add.
     */
    public void addUser(User user){
        users.add(user);
        Serialiser.Serialize(users);
    }

    /**
     * Checking for duplicate username.
     * @param username username of a user
     * @return  False if such a username exists, else True.
     */
    public boolean isValidUsername(String username){
        return users
                .stream()
                .noneMatch(user -> user.getUsername().equals(username));
    }

    public boolean isValidUser(User user){
        return !users.contains(user);
    }

    /**
     * Searching for a user with identical username and a possword.
     * @param username
     * @param password
     * @return  a user with specified username and password. Null if not found.
     */
    public User signIn(String username, String password) {
        Optional<User> result = users.stream()
                .filter(user -> user.getUsername().equals(username) && user.getPassword().equals(password))
                .findAny();

        return result.orElse(null);
    }

    /**
     * Checks if a user is in a queue.
     * @param user  user to be checked
     * @return  True if in a queue, else False.
     */
    public boolean isInQueue(User user){
        return usersQueue.contains(user);
    }

    public void addToQueue(User user){
        usersQueue.add(user);
        Serialiser.Serialize(usersQueue);
    }

    public int getQueueSize(){
        return usersQueue.size();
    }

    /**
     * Finds out the user position in a queue.
     * @param user  user in a queue.
     * @return  position number.
     */
    public int getQueueNumber(User user){
        int i = 1;
        for (User u : usersQueue) {
            if (u.equals(user))
                break;
            i+=1;
        }
        return i;
    }

    /**
     * Removes the first user from a queue .
     * @return  removed user.
     */
    public User removeFromQueue(){
        User u = usersQueue.poll();
        Serialiser.Serialize(usersQueue);
        return u;
    }
}
