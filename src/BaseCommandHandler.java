public class BaseCommandHandler {
    private Command command;

    public BaseCommandHandler(Command command) {
        this.command = command;
    }

    public Command getCommand() {
        return command;
    }

    public void setCommand(Command command) {
        this.command = command;
    }

    public void handleCommand () {
        command.execute();
    }
}