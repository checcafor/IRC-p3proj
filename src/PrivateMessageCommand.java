import java.io.PrintWriter;

public class PrivateMessageCommand implements Command {
    private Server server;
    private User sender;
    String[] parts;
    private String message;

    public PrivateMessageCommand(User sender, String message) {
        this.sender = sender;
        this.message = message;
        this.server = Server.getInstance();
        parts = message.split(" ", 2);
    }

    @Override
    public void execute() {
        if (parts.length == 2) {
            String receiverUsername = parts[0]; // nome destinatario
            String privateMessage = parts[1];   // messaggio

            User receiver = server.getUserByName(receiverUsername);

            if (receiver != null) {
                PrintWriter recipientWriter = receiver.getPrintWriter();

                // stampa sul destinatario il messaggio
                recipientWriter.println("[Private from " + sender + "]: " + privateMessage);
            } else { // se l'utente non esiste
                sender.getPrintWriter().println("User " + receiverUsername + " not found.");
            }
        } else {
            sender.getPrintWriter().println("Invalid /privmsg command. Usage: /privmsg <username> <message>");
        }
    }
}