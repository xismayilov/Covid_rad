package server.user;

public class UserStudent extends User {
    public UserStudent(String email, String password, String username) {
        super(email, password, username);
    }

    @Override
    public String getAvailableCommands() {
        return "Commands:\n" +
                "wait in queue - you begin waiting in a queue\n" +
                "get number - get the number which represents your number in a queue\n" +
                "help - this help message";
    }

    @Override
    public boolean isAvailableCommand(String command) {
        switch (command.trim().toLowerCase()){
            case "wait in queue":
            case "get number":
            case "help":
                return true;
            default:
                return false;
        }
    }
}
