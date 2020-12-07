package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

import static common.Constants.CONNECTION_PORT;
import static server.SocketHelper.createServerSocket;

public class ConnectionThread extends Thread{
    private ArrayList<Integer> usedPorts = new ArrayList<Integer>();
    private ServerSocket serverSocket;

    @Override
    public void run() {

        serverSocket = createServerSocket(CONNECTION_PORT);
        if (serverSocket == null)
            return;

        while (true){
            Socket connectionSocket = acceptClient(serverSocket);

            OutputStreamWriter osw;
            Scanner scanner = null;
            PrintWriter serverPrintOut = null;
            try {
                osw = new OutputStreamWriter(connectionSocket.getOutputStream(), "UTF-8");
                scanner = new Scanner(connectionSocket.getInputStream(), "UTF-8");
                serverPrintOut = new PrintWriter(osw, true);
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }

            serverPrintOut.println("Hello World! Enter Peace to exit.");
            try {
                connectionSocket.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }

//            boolean stop = false;
//            while(!stop && scanner.hasNextLine()) {
//                String message = scanner.nextLine();
//                serverPrintOut.println("Server received the message: " + message);
//                if(message.equals("stop"))
//                    stop = true;
//            }
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        serverSocket.close();
    }

    private int generatePort(){
        return 0;
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