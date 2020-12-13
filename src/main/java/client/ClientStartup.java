package client;

import common.Constants;
import common.SocketHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import static common.Constants.CONNECTION_PORT;
import static common.Constants.INVALID_PORT;

/**
 * Entry point for the client application.
 */
public class ClientStartup {

    /**
     *  Connects to a server on the port 8888 and receives a port for future communication.
     * @return received port for communication with a server.
     */
    public static int getCommunicationPortFromServer() {
        int communicationPort = Constants.INVALID_PORT;
        Socket socket = SocketHelper.createClientSocket(CONNECTION_PORT);

        if (socket == null)
            return communicationPort;

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String readData = reader.readLine();
            communicationPort = Integer.parseInt(readData);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        finally {
            try {
                socket.close();
            } catch(Exception ignored) { }
        }

        return communicationPort;
    }

    public static void main(String[] args) {
        int port = getCommunicationPortFromServer();

        if (port == INVALID_PORT){
            System.out.println("Invalid port");
            return;
        }

        ClientCommunicationThread communicationThread = new ClientCommunicationThread(port);
        communicationThread.start();

        try {
            communicationThread.join();
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }
}
