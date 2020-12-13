package server;

/**
 * Class that starts the server application.
 *
 * Interface between a server and a client:
 * Communication is based on TCP using sockets.
 * Before main communication a client needs to connect to the port 8888 to get new port for future communication.
 * After connection establishment, the client can send specified commands to the server.
 * Those commands are:
 * <ul>
 *   <li>register - register new user</li>
 *   <li>sign_in - sign in existing user</li>
 *   <li>wait_in_queue - begin waiting in a queue</li>
 *   <li>get_queue_number - get a number which represent your position in a queue</li>
 *   <li>call_next_one - notifies the first student in a queue that he/she can visit a study department.</li>
 *   <li>logout - exit the system</li>
 *  </ul>
 */
public class ServerStartup {

    /**
     * Creates new thread which is correspondent for connection of a client to the server.
     * @throws InterruptedException
     */
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
