package server;

import java.io.IOException;
import java.net.ServerSocket;

public class SocketHelper {

    /**
     *  Creates a server socket according to the port parameter.
     *  It will return null if creation is not possible.
     */
    public static ServerSocket createServerSocket(int port){
        ServerSocket serverSocket;

        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
        }

        return serverSocket;
    }

}
