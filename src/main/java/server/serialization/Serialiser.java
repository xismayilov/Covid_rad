package server.serialization;

import server.user.User;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Queue;

/**
 * Serializer is responsible for write/reading users to/from a file.
 */
public class Serialiser {

    private static String USERS_FILE_NAME = "clients.out";
    private static String QUEUE_FILE_NAME = "clients.out";

    /**
     *  Saves users to a file.
     * @param users a list of users which are going to be saved to a file
     */
    public static void Serialize(List<User> users) {
        try {
            FileOutputStream file = new FileOutputStream(USERS_FILE_NAME);
            ObjectOutputStream out = new ObjectOutputStream(file);

            out.writeObject(users); //Saving of objects in a file

            out.close();
            file.close();

            System.out.println("LOG: Users have been serialized");
        }
        catch(Exception e) {
            System.out.println(e.toString());
        }
    }

    /**
     *  Saves queue of users to a file.
     * @param users a list of users which are going to be saved to a file
     */
    public static void Serialize(Queue<User> users) {
        try {
            FileOutputStream file = new FileOutputStream(QUEUE_FILE_NAME);
            ObjectOutputStream out = new ObjectOutputStream(file);

            out.writeObject(users); //Saving of objects in a file

            out.close();
            file.close();

            System.out.println("LOG: Users have been serialized");
        }
        catch(Exception e) {
            System.out.println(e.toString());
        }
    }

    /**
     *  Uploads users from a file.
     * @return a list of users which are uploaded from a file
     */
    public static List<User> DeserializeUsers() {
        try {
            FileInputStream file = new FileInputStream(USERS_FILE_NAME);
            ObjectInputStream in = new ObjectInputStream(file);

            // Method for deserialization of object
            List<User> users = (List<User>) in.readObject();
            in.close();
            file.close();

            System.out.println("LOG: Users have been deserialized");

            return users;
        }
        catch(Exception ex) {
            System.out.println(ex.toString());
            return null;
        }
    }

    /**
     *  Uploads users from a file.
     * @return a list of users which are uploaded from a file
     */
    public static Queue<User> DeserializeQueue() {
        try {
            FileInputStream file = new FileInputStream(QUEUE_FILE_NAME);
            ObjectInputStream in = new ObjectInputStream(file);

            // Method for deserialization of object
            Queue<User> users = (Queue<User>) in.readObject();
            in.close();
            file.close();

            System.out.println("LOG: Users have been deserialized");

            return users;
        }
        catch(Exception ex) {
            System.out.println(ex.toString());
            return null;
        }
    }

}
