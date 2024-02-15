package patterns.command;

import patterns.observer.User;

import patterns.singleton.Server;

import java.io.PrintWriter;

/**
 * Questa classe rappresenta un comando per inviare un messaggio privato a un altro utente.
 * Il comando gestisce la logica di invio di un messaggio privato da un mittente a un destinatario.
 */
public class PrivateMessageCommand implements Command {
    private Server server; // server sul quale viene eseguito il comando
    private User sender; // l'utente mittente del messaggio privato
    String[] parts; // parti del messaggio divise
    private String message; // messaggio privato completo

    /**
     * Costruttore della classe che inizializza il comando per inviare un messaggio privato.
     * @param sender L'utente mittente del messaggio privato
     * @param message Il messaggio privato da inviare
     */
    public PrivateMessageCommand(User sender, String message) {
        this.sender = sender;
        this.message = message;
        this.server = Server.getInstance();
        parts = message.split(" ", 2); // divide il messaggio in due parti, il nome utente del destinatario e il messaggio stesso
    }

    /**
     * Metodo che esegue il comando di invio del messaggio privato.
     * Se il comando è correttamente formattato, invia il messaggio privato al destinatario specificato.
     * Se il destinatario non esiste, viene fornito un messaggio di errore.
     */
    @Override
    public void execute() {
        if (parts.length == 2) {
            String receiverUsername = parts[0]; // nome destinatario
            String privateMessage = parts[1];   // messaggio

            User receiver = server.getUserByName(receiverUsername); // ottiene l'utente destinatario dal server

            // controlla se il mittente sta cercando di inviare un messaggio privato a se stesso
            if(sender.getUsername().equals(receiverUsername)){
                sender.getPrintWriter().println("You can't send private massage to yourself.");
                return;
            }

            if (receiver != null) {
                PrintWriter recipientWriter = receiver.getPrintWriter();
                // invia il messaggio privato al destinatario
                recipientWriter.println("[Private from " + sender.getUsername() + "]: " + privateMessage);
                // invia una conferma al mittente del messaggio inviato
                sender.getPrintWriter().println("[ Message to" +receiverUsername + "] :" + privateMessage);
            } else { // se l'utente destinatario non esiste, fornisce un messaggio di errore al mittente
                sender.getPrintWriter().println("patterns.observer.User " + receiverUsername + " not found.");
            }
        } else { // se il comando è malformato, fornisce un messaggio di errore al mittente
            sender.getPrintWriter().println("Invalid /privmsg command. Usage: /privmsg <username> <message>");
        }
    }
}