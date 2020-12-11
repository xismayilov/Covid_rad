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
import java.util.Scanner;

import static common.Constants.FAILURE_MSG;
import static common.Constants.SUCCESS_MSG;

public class ServerCommunicationThread extends Thread {
    private int port;
    private boolean stop = false;
    private Socket connectionSocket;
    private ServerSocket serverSocket;
    private Scanner scanner;
    private PrintWriter serverPrintOut;
    private DataBase db;
    private User signedInUser;

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
            connectionSocket = serverSocket.accept();
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
            String message = scanner.nextLine();
            System.out.println("Received: " + message);

            performCommand(message);
        }

        closeSockets();
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

        if (signedInUser != null)
            serverPrintOut.println(SUCCESS_MSG);
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
}
