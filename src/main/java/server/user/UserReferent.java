package server.user;

public class UserReferent extends User {
    public UserReferent(String email, String password, String username) {
        super(email, password, username);
    }

    @Override
    public String getAvailableCommands() {
        return "Commands:\n" +
                "call next one - it calls the first student in a queue\n" +
                "help - this help message";
    }

    @Override
    public boolean isAvailableCommand(String command) {
        switch (command.trim().toLowerCase()){
            case "call next one":
            case "help":
                return true;
            default:
                return false;
        }
    }
}
