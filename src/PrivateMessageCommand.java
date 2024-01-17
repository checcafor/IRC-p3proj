public class PrivateMessageCommand implements Command {
    private User sender;
    private String receiverUsername;
    private String message;

    public PrivateMessageCommand(User sender, String receiverUsername, String message) {
        this.sender = sender;
        this.receiverUsername = receiverUsername;
        this.message = message;
    }

    @Override
    public void execute() {
        Server server = Server.getInstance();
        User receiver = server.getUserByName(receiverUsername);

        if (receiver != null) {
            sender.sendPrivateMessage(receiver, message);
        } else {
            sender.sendMessage("User " + receiverUsername + " not found");
        }
    }
}