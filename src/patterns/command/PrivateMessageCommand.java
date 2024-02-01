package patterns.command;

import patterns.observer.User;

import patterns.singleton.Server;

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

            if(sender.getUsername().equals(receiverUsername)){
                sender.getPrintWriter().println("You can't send private massage to yourself.");
                return;
            }

            if (receiver != null) {
                PrintWriter recipientWriter = receiver.getPrintWriter();
                // stampa sul destinatario il messaggio
                recipientWriter.println("[Private from " + sender.getUsername() + "]: " + privateMessage);
                sender.getPrintWriter().println("[ Message to" +receiverUsername + "] :" + privateMessage);
            } else { // se l'utente non esiste
                sender.getPrintWriter().println("patterns.observer.User " + receiverUsername + " not found.");
            }
        } else {
            sender.getPrintWriter().println("Invalid /privmsg command. Usage: /privmsg <username> <message>");
        }
    }
}