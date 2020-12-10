package common;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

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

    /**
     *  Creates a client socket according to the port parameter.
     *  It will return null if creation is not possible.
     */
    public static Socket createClientSocket(int port){
        Socket socket;

        try {
            socket = new Socket("localhost", port);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
        }

        return socket;
    }

}
