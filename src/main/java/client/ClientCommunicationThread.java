package client;

import server.user.User;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

import static common.Constants.*;
import static common.SocketHelper.createClientSocket;

/**
 * ClientCommunicationThread is responsible for input of commands by a user which are sent afterwards  a server.
 */
public class ClientCommunicationThread extends Thread {
    private int communicationPort;
    private boolean stop = false;
    private boolean isSignedIn = false;
    private BufferedReader reader;
    private Socket socket;
    private OutputStreamWriter osw;
    private PrintWriter printWriter;
    private Scanner stdinScanner;
    private User currentUser;

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
            System.out.println(data);           // Welcome message 1 part
            data = reader.readLine();
            System.out.println(data);           // Welcome message 2 part
        } catch (IOException e) {
            System.err.println(e.getMessage());
            closeEverything();
            return;
        }

        try {
            osw = new OutputStreamWriter(socket.getOutputStream(), "UTF-8");
        } catch (IOException e) {
            System.err.println(e.getMessage());
            closeEverything();
            return;
        }

        printWriter = new PrintWriter(osw, true);

        // Reader from standard input
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        stdinScanner = new Scanner(System.in);

        /*Waiting for signing in*/
        while (!isSignedIn){
            String userInput = stdinScanner.nextLine().trim().toLowerCase();

            switch (userInput){
                case "help":
                    System.out.println("Commands:\n" +
                            "register - sign up a new user\n" +
                            "sign in - sign in to the system\n");
                    break;
                case "sign in" :
                case "register" :
                    performCommand(userInput);
                    break;
                default:
                    System.out.println("Unknown command");
            }
        }
        // Signed in successfully

        while (!stop) {
            try {
                if (reader.ready()){    // If server sent something, print it out
                    String result = reader.readLine();
                    System.out.println(result);
                }

                if (!bufferedReader.ready()) {  // If user has not entered something
                    waitSecond();
                    continue;
                }
            } catch (IOException e) {
                System.err.println(e.toString());
            }

            String userInput = stdinScanner.nextLine();
            if (!currentUser.isAvailableCommand(userInput))
                userInput = "";

            performCommand(userInput);

            boolean isError = printWriter.checkError();
            if (isError) {
                System.out.println("Connection closed.");
                break;
            }
        }

        closeEverything();
    }

    private void waitSecond(){
        try {
            sleep(1000);
        } catch (InterruptedException e) {
            System.err.println(e.toString());
        }
    }

    private void performCommand(String command) {
        switch (command.trim().toLowerCase()){
            case "logout":
                stop = true;
                printWriter.println("logout");
                break;
            case "register":
                performRegistration();
                break;
            case "sign in":
                performSignIn();
                break;
            case "help":
                System.out.println(currentUser.getAvailableCommands());
                break;
            case "wait in queue":
                startWaiting();
                break;
            case "get number":
                getQueueNumber();
                break;
            case "call next one":
                callNextStudent();
                break;
            default:
                System.out.println("Unknown command");
        }
    }

    private void performRegistration(){
        printWriter.println("register");

        System.out.println("Registration started:\n" +
                "Print username:");
        String username = stdinScanner.nextLine();
        printWriter.println(username.trim());

        System.out.println("Print password:");
        String password = stdinScanner.nextLine();
        printWriter.println(password.trim());

        System.out.println("Print email:");
        String email = stdinScanner.nextLine();
        printWriter.println(email.trim());

        String type = "";
        boolean gotType = false;
        while (!gotType){
            System.out.println("Are you a " + REFERENT + " or a " + STUDENT + ":");
            type = stdinScanner.nextLine();
            if (type.equals(REFERENT) || type.equals(STUDENT))
                gotType = true;
        }

        printWriter.println(type.trim());

        String result = readFromServer();
        System.out.println(result);
    }

    private void performSignIn(){
        printWriter.println("sign_in");

        System.out.println("Print username:");
        String username = stdinScanner.nextLine();
        printWriter.println(username.trim());

        System.out.println("Print password:");
        String password = stdinScanner.nextLine();
        printWriter.println(password.trim());

        String result = readFromServer();
        if (result.equals(SUCCESS_MSG)) {
            String welcomeMessage = readFromServer();
            System.out.println(welcomeMessage);
            String type = readFromServer();
            currentUser = User.getUserByType(type);
            isSignedIn = true;
        }
        else {
            System.out.println("Wrong username or password");
        }
    }

    private void startWaiting(){
        sendCommandAndPrintResult("wait_in_queue");
    }

    private void getQueueNumber(){
        sendCommandAndPrintResult("get_queue_number");
    }

    private void callNextStudent(){
        sendCommandAndPrintResult("call_next_one");
    }

    private void sendCommandAndPrintResult(String command){
        printWriter.println(command);
        String result = readFromServer();
        System.out.println(result);
    }

    private String readFromServer(){
        try {
            return reader.readLine();
        } catch (IOException e) {
            System.err.println(e.toString());
            closeEverything();
            return "";
        }
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
        printWriter.close();
        stdinScanner.close();
    }
}
