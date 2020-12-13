package server;

import server.serialization.DataBase;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

import static common.Constants.CONNECTION_PORT;
import static common.SocketHelper.createServerSocket;

/**
 * ConnectionThread is responsible for connection establishment between a client and the server.
 * Connection is performed on the port 8888.
 */
public class ConnectionThread extends Thread{
    private ArrayList<Integer> usedPorts = new ArrayList<>();
    private ServerSocket serverSocket;

    public ConnectionThread(){
        usedPorts.add(CONNECTION_PORT);
    }

    @Override
    public void run() {
        DataBase db = new DataBase();
        serverSocket = createServerSocket(CONNECTION_PORT);
        if (serverSocket == null)
            return;

        while (true){
            Socket connectionSocket = acceptClient(serverSocket);   // Waiting for a client to accept

            OutputStreamWriter osw;
            PrintWriter serverPrintOut;
            try {
                osw = new OutputStreamWriter(connectionSocket.getOutputStream(), "UTF-8");
                serverPrintOut = new PrintWriter(osw, true);
            } catch (IOException e) {
                System.out.println(e.getMessage());
                continue;
            }

            int port = generatePort();
            ServerCommunicationThread commThread = new ServerCommunicationThread(port, db); // create new thread for communication between a client and the server
            commThread.start();

            serverPrintOut.println(port);   // Send generated port to a client

            try {
                connectionSocket.close();
                serverPrintOut.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        serverSocket.close();
    }

    /**
     *  Generates new port.
     * @return random port which is greater 1024
     */
    private int generatePort(){
        Random random = new Random();

        while (true){
            int newPort = random.nextInt(65000);
            if (newPort < 1024)
                newPort+=1024;  // Ports 0-1024 are usually reserved.

            if (usedPorts.contains(newPort))
                continue;

            usedPorts.add(newPort);
            return newPort;
        }
    }

    private Socket acceptClient(ServerSocket socket){
        if(socket == null)
            return null;

        Socket connectionSocket = null;
        try {
            connectionSocket = socket.accept();
            System.out.println("LOG: Connection accepted " + connectionSocket.getInetAddress().getHostAddress());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return connectionSocket;
    }

}
