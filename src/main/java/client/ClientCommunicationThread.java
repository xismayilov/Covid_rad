package client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

import static common.Constants.*;
import static common.SocketHelper.createClientSocket;

public class ClientCommunicationThread extends Thread {
    private int communicationPort;
    private boolean stop = false;
    private boolean isSignedIn = false;
    private BufferedReader reader;
    private Socket socket;
    private OutputStreamWriter osw;
    private PrintWriter printWriter;
    private Scanner stdinScanner;

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
            data = reader.readLine();
            System.out.println(data);
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
        stdinScanner = new Scanner(System.in);
        while (!stop) {
            String userInput = stdinScanner.nextLine();
            performCommand(userInput);

            boolean isError = printWriter.checkError();
            if (isError) {
                System.out.println("Connection closed.");
                break;
            }
        }

        closeEverything();
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
                System.out.println("Commands:\n" +
                        "register - sign up a new user\n" +
                        "sign in - sign in to the system\n" +
                        "wait in queue - \n" +
                        "get number - get the number which represents your number in a queue\n" +
                        "help - this help message");
                break;
            case "wait in queue":
                startWaiting();
                break;
            case "get number":
                getQueueNumber();
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
            System.out.println("You are successfully signed in.");
            isSignedIn = true;
        }
        else {
            System.out.println("Wrong username or password");
        }
    }

    private void startWaiting(){
        printWriter.println("wait_in_queue");
        String result = readFromServer();
        System.out.println(result);
    }

    private void getQueueNumber(){
        printWriter.println("get_queue_number");
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
