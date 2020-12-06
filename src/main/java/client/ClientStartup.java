package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;

import static common.Constants.CONNECTION_PORT;

public class ClientStartup {

    public static void connectToServer() {
        Socket socket = null;

        try {
            socket = new Socket("localhost", CONNECTION_PORT);

            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String time = reader.readLine();

            System.out.println(time);

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        finally {
            if(socket != null) {
                try {
                    socket.close();
                } catch(Exception ignored) {}
            }
        }
    }

    public static void main(String[] args) {
        connectToServer();
    }
}
