package client;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class Deserialiser {

    String filename = "clients.out";


    public void Serialization (Client client) throws NullPointerException {
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
}
