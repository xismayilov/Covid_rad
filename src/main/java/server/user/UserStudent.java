package server.user;

import server.ServerCommunicationThread;

import java.util.Observer;

public class UserStudent extends User {
    public UserStudent(String email, String password, String username) {
        super(email, password, username);
    }

    private boolean isCalled = false;   // tells if a student was called by a referent.

    @Override
    public void notifyObservers() {
        isCalled = true;
        for (Observer o : observers) {
            ((ServerCommunicationThread) o).notifyStudent();
        }
        super.notifyObservers(isCalled);
    }

    @Override
    public String getAvailableCommands() {
        return "Commands:\n" +
                "wait in queue - you begin waiting in a queue\n" +
                "get number - get the number which represents your number in a queue\n" +
                "help - this help message\n"+
                "logout - quit the system";
    }

    @Override
    public boolean isAvailableCommand(String command) {
        switch (command.trim().toLowerCase()){
            case "wait in queue":
            case "get number":
            case "help":
            case "logout":
                return true;
            default:
                return false;
        }
    }

    public boolean getIsCalled(){
        return isCalled;
    }

    public void setIsCalled(boolean value){
        isCalled = value;
    }
}
