package client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

import static common.SocketHelper.createClientSocket;

public class ClientCommunicationThread extends Thread {
    private int communicationPort;
    BufferedReader reader;
    Socket socket;
    OutputStreamWriter osw;

    public ClientCommunicationThread(int communicationPort){
        this.communicationPort = communicationPort;
    }

    @Override
    public void run() {
        socket = createClientSocket(communicationPort);
        if (socket == null)
            return;

        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String data = reader.readLine();
            System.out.println(data);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            closeEverything();
            return;
        }

        try {
            osw = new OutputStreamWriter(socket.getOutputStream(), "UTF-8");
        } catch (IOException e) {
            System.out.println(e.getMessage());
            closeEverything();
            return;
        }

        PrintWriter printWriter = new PrintWriter(osw, true);
        Scanner stdinScanner = new Scanner(System.in);
        while (true) {

            String userInput = stdinScanner.nextLine();

            printWriter.println(userInput);

            boolean isError = printWriter.checkError();
            if (isError) {
                System.out.println("Connection closed.");
                break;
            }
        }

        printWriter.close();
        closeEverything();
    }

    private void closeEverything(){
        try {
            reader.close();
        } catch (Exception ignored) { }
        try {
            socket.close();
        } catch (Exception ignored) { }
        try {
            osw.close();
        } catch (Exception ignored) { }
    }
}
