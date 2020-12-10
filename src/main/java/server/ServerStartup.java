package server;

public class ServerStartup {

    public static void startTheServer() throws InterruptedException {
        ConnectionThread connectionThread = new ConnectionThread();
        connectionThread.start();
        connectionThread.join();
    }

    public static void main(String[] args) {
        try {
            startTheServer();
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }
}
