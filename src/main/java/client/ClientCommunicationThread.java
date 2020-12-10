package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import static common.SocketHelper.createClientSocket;

public class ClientCommunicationThread extends Thread {
    private int communicationPort;

    public ClientCommunicationThread(int communicationPort){
        this.communicationPort = communicationPort;
    }

    @Override
    public void run() {
        BufferedReader reader = null;
        Socket socket = createClientSocket(communicationPort);
        if (socket == null)
            return;

        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String data = reader.readLine();
            System.out.println(data);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return;
        }

        try {
            reader.close();
            socket.close();
        } catch (Exception ignored) { }
    }
}
