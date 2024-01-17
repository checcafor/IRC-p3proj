public class PrivateMessageCommand implements Command {
    private Server server;
    private User sender;
    private String receiverUsername;
    private String message;

    public PrivateMessageCommand(User sender, String receiverUsername, String message) {
        this.sender = sender;
        this.receiverUsername = receiverUsername;
        this.message = message;
        this.server = Server.getInstance();
    }

    @Override
    public void execute() {
        User receiver = server.getUserByName(receiverUsername);

        if (receiver != null) {
            sender.sendPrivateMessage(receiver, message);
        } else {
            sender.sendMessage("User " + receiverUsername + " not found");
        }
    }
}