package server;

import server.serialization.DataBase;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

import static common.Constants.CONNECTION_PORT;
import static common.SocketHelper.createServerSocket;

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
            Socket connectionSocket = acceptClient(serverSocket);

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
            ServerCommunicationThread commThread = new ServerCommunicationThread(port, db);
            commThread.start();

            serverPrintOut.println(port);

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
