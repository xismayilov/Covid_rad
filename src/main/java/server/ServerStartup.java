package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import static common.Constants.CONNECTION_PORT;

public class ServerStartup {

    public static void startTheServer() {
        try{
            ServerSocket serverSocket = new ServerSocket(CONNECTION_PORT);
            Socket connectionSocket = serverSocket.accept();
            System.out.println("LOG: Connection accepted " + connectionSocket.getInetAddress().getHostAddress());

            InputStream inputStream = connectionSocket.getInputStream();
            OutputStream outputStream = connectionSocket.getOutputStream();
            Scanner scanner = new Scanner(inputStream, "UTF-8");
            PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(outputStream, "UTF-8"), true);
            printWriter.println("Hello Client!");

            boolean stop = false;
            while(!stop && scanner.hasNextLine()) {
                String message = scanner.nextLine();
                printWriter.println("Server received the message: " + message);
                if(message.equals("stop"))
                    stop = true;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        startTheServer();
    }
}
