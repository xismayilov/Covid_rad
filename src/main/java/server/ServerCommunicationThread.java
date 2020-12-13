package server;

import common.Constants;
import common.SocketHelper;
import server.serialization.DataBase;
import server.user.User;
import server.user.UserReferent;
import server.user.UserStudent;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

import static common.Constants.FAILURE_MSG;
import static common.Constants.SUCCESS_MSG;

/**
 * ServerCommunicationThread is responsible for performing commands which are sent from a client to a server.
 */
public class ServerCommunicationThread extends Thread implements Observer {
    private int port;
    private boolean stop = false;
    private Socket connectionSocket;
    private ServerSocket serverSocket;
    private Scanner scanner;
    private PrintWriter serverPrintOut;
    private DataBase db;
    private User signedInUser;

    /**
     *
     * @param port  port on which communication will be performed.
     * @param db    database with users.
     */
    public ServerCommunicationThread(int port, DataBase db){
        this.port = port;
        this.db = db;
    }

    @Override
    public void run() {
        serverSocket = SocketHelper.createServerSocket(port);

        if (serverSocket == null)
            return;

        try {
            connectionSocket = serverSocket.accept();   // waiting for a client ot be connected.
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return;
        }

        OutputStreamWriter osw;
        try {
            osw = new OutputStreamWriter(connectionSocket.getOutputStream(), "UTF-8");
            scanner = new Scanner(connectionSocket.getInputStream(), "UTF-8");
            serverPrintOut = new PrintWriter(osw, true);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            closeSockets();
            return;
        }

        serverPrintOut.println("Hello!\nWelcome to the queue system. Print \"help\" for a list of commands.");

        while(!stop && scanner.hasNextLine()) {
            String message = scanner.nextLine();        // Wait for a command from a client
            System.out.println("Received: " + message);

            performCommand(message);
        }

        closeSockets();
    }

    private void performCommand(String command) {
        switch (command.trim().toLowerCase()){
            case "logout":
                stop = true;
                break;
            case "register":
                performRegistration(scanner, serverPrintOut);
                break;
            case "sign_in":
                performSignIn();
                break;
            case "wait_in_queue":
                waitInQueue();
                break;
            case "get_queue_number":
                getQueueNumber();
                break;
            case "call_next_one":
                callNextOne();
                break;
        }
    }

    private void performRegistration(Scanner scanner, PrintWriter printWriter) {
        String username = scanner.nextLine().trim();
        String password = scanner.nextLine().trim();
        String email = scanner.nextLine().trim();
        String type = scanner.nextLine().trim();

        User user = null;
        switch (type){
            case Constants.REFERENT:
                user = new UserReferent(email, password, username);
                break;
            case Constants.STUDENT:
                user = new UserStudent(email, password, username);
                break;
        }

        if (!db.isValidUsername(username)) {
            printWriter.println("Such a user exists");
            return;
        }

        db.addUser(user);
        printWriter.println(SUCCESS_MSG);
    }

    private void performSignIn(){
        String username = scanner.nextLine().trim();
        String password = scanner.nextLine().trim();
        signedInUser = db.signIn(username, password);

        if (signedInUser != null) {
            serverPrintOut.println(SUCCESS_MSG);
            String className = signedInUser.getClass().getCanonicalName();

            String welcomeMessage = "Welcome to our waiting system Covid Rad! ";
            if (signedInUser.getClass().equals(UserStudent.class)){
                // Checks whether a referent has called the student.
                UserStudent student = (UserStudent) signedInUser;
                if (student.getIsCalled()) {
                    welcomeMessage += "Referent has called you. You can visit the study department.";
                    student.setIsCalled(false);
                }
            }

            serverPrintOut.println(welcomeMessage);
            System.out.println("LOG: Signed in new " + className);
            serverPrintOut.println(className);
        }
        else
            serverPrintOut.println(FAILURE_MSG);
    }

    private void waitInQueue(){
        if (db.isInQueue(signedInUser)) {
            serverPrintOut.println("You are already in a queue.");
            return;
        }

        db.addToQueue(signedInUser);
        int queueNum = db.getQueueSize();
        serverPrintOut.println(String.format("You have number %d.", queueNum));
    }

    private void getQueueNumber(){
        if (!db.isInQueue(signedInUser)) {
            serverPrintOut.println("You are not in a queue.");
            return;
        }

        int queueNum = db.getQueueNumber(signedInUser);
        serverPrintOut.println(String.format("You have number %d.", queueNum));
    }

    private void callNextOne(){
        User user = db.removeFromQueue();
        if (user == null){
            serverPrintOut.println("There are no waiting students.");
            return;
        }

        serverPrintOut.println("The next student has been called. It is " + user.getUsername());
        user.notifyObservers();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        closeSockets();
    }

    private void closeSockets(){
        try{
            serverSocket.close();
        } catch (Exception ignored){ }
        try{
            connectionSocket.close();
        } catch (Exception ignored){ }
    }

    @Override
    public void update(Observable o, Object arg) {
        ((UserStudent) o).setIsCalled(false);
        serverPrintOut.println("Referent has called you. You can visit the study department.");
    }
}
