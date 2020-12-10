package server.serialization;

import server.user.User;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

public class Serialiser {

    private static String FILE_NAME = "clients.out";

    public static void Serialize(List<User> users) {
        try {
            //Saving of object in a file
            FileOutputStream file = new FileOutputStream(FILE_NAME);
            ObjectOutputStream out = new ObjectOutputStream(file);

            // Method for serialization of object
            out.writeObject(users);

            out.close();
            file.close();

            System.out.println("LOG: Users have been serialized");
        }
        catch(Exception e) {
            System.out.println(e.toString());
        }
    }

    public static List<User> Deserialize() {
        try {
            // Reading the object from a file
            FileInputStream file = new FileInputStream(FILE_NAME);
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
        catch(NullPointerException ex) {
            System.out.println("It is not an object");
            return null;
        }
    }
}
