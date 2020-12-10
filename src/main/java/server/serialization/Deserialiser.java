package server.serialization;

import server.user.User;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Deserialiser {

    String filename = "clients.out";

    public void Serialize(User user) throws NullPointerException {
        try {
            //Saving of object in a file
            FileOutputStream file = new FileOutputStream(filename);
            ObjectOutputStream out = new ObjectOutputStream(file);

            // Method for serialization of object
            out.writeObject(user);

            out.close();
            file.close();

            System.out.println("Object has been serialized");
        }

        catch(IOException ex) {
            System.out.println("IOException is caught");
        }
        catch(NullPointerException ex) {
            System.out.println("It is not an object");
        }
    }

    public User Deserialize(String username, String password, int ID)  throws IOException,ClassNotFoundException{
        try {
            // Reading the object from a file
            FileInputStream file = new FileInputStream(filename);
            ObjectInputStream in = new ObjectInputStream(file);

            // Method for deserialization of object
            User user = (User) in.readObject();
            in.close();
            file.close();

            if(user.getName().equals(username) && user.getPassword().equals(password) && user.getID() == ID){
                return user;
            }

            else {
                return null;
            }
        }

        catch(IOException ex) {
            System.out.println("IOException is caught");
            return null;
        }

        catch(ClassNotFoundException ex) {
            System.out.println("ClassNotFoundException is caught");
            return null;
        }
        catch(NullPointerException ex) {
            System.out.println("It is not an object");
            return null;
        }
    }
}
