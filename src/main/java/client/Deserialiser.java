package client;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Deserialiser {

    String filename = "clients.out";


    public void Serialize(Client client) throws NullPointerException {
        try {
            //Saving of object in a file
            FileOutputStream file = new FileOutputStream(filename);
            ObjectOutputStream out = new ObjectOutputStream(file);

            // Method for serialization of object
            out.writeObject(client);

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



    public Client Deserialize(String username, String password, int ID)  throws IOException,ClassNotFoundException{
        try {
            // Reading the object from a file
            FileInputStream file = new FileInputStream(filename);
            ObjectInputStream in = new ObjectInputStream(file);

            // Method for deserialization of object
            Client client = (Client) in.readObject();
            in.close();
            file.close();

            if(client.getName().equals(username) && client.getPassword().equals(password) && client.getID() == ID){
                return client;
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
