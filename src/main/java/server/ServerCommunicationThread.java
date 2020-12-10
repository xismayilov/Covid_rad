package server;

import common.SocketHelper;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class ServerCommunicationThread extends Thread {
    private int port;
    Socket connectionSocket;
    ServerSocket serverSocket;

    public ServerCommunicationThread(int port){
        this.port = port;
    }

    @Override
    public void run() {
        serverSocket = SocketHelper.createServerSocket(port);

        if (serverSocket == null)
            return;

        try {
            connectionSocket = serverSocket.accept();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return;
        }

        Scanner scanner;
        OutputStreamWriter osw;
        PrintWriter serverPrintOut;
        try {
            osw = new OutputStreamWriter(connectionSocket.getOutputStream(), "UTF-8");
            scanner = new Scanner(connectionSocket.getInputStream(), "UTF-8");
            serverPrintOut = new PrintWriter(osw, true);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            closeSockets();
            return;
        }

        serverPrintOut.println("Hello User!");

        boolean stop = false;
        while(!stop && scanner.hasNextLine()) {
            String message = scanner.nextLine();
            serverPrintOut.println("Server received the message: " + message);
            if(message.equals("logout"))
                stop = true;
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        closeSockets();
    }

    private void closeSockets(){
        try{
            serverSocket.close();
            connectionSocket.close();
        } catch (Exception ignored){ }
    }
}
