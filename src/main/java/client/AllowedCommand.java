package client;

public enum AllowedCommand {
    INFO("info"),
    TIME("time"),
    QUIT("quit");

    private final String command;

    AllowedCommand(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }

    public static boolean isCommandAllowed(String command) {
        for (AllowedCommand allowedCommand : AllowedCommand.values()) {
            if (allowedCommand.getCommand().equals(command)) {
                return true;
            }
        }
        return false;
    }
}
