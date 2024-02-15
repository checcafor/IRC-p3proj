package patterns.command;

import patterns.observer.*;

import java.util.List;

/**
 * Questa classe rappresenta un comando per ottenere l'elenco degli utenti presenti in un canale.
 * Il comando gestisce la logica di ottenere e visualizzare l'elenco degli utenti presenti nello stesso canale.
 */
public class UserListCommand implements Command {
    private ConcreteChannel channel;  // canale di cui si desidera ottenere l'elenco degli utenti
    private User user; // utente che esegue il comando

    /**
     * Costruttore della classe che inizializza il comando per ottenere l'elenco degli utenti in un canale.
     * @param user L'utente che esegue il comando
     */
    public UserListCommand(User user) {
        this.user = user;
        this.channel = user.getCurrentChannel();
    }

    /**
     * Metodo che esegue il comando di ottenere l'elenco degli utenti in un canale.
     * Se l'utente è all'interno di un canale, visualizza l'elenco degli utenti.
     * Altrimenti, viene fornito un messaggio di errore.
     */
    @Override
    public void execute() {

        // se l'utente si trova in un canale
        if (channel != null) {
            List<Observer> users = channel.getUsers();
            user.getPrintWriter().println("In #" + channel.getName() + " there are these users:");

            // stampa l'elenco degli utenti presenti nel canale
            for (Observer observer : users) {
                User channelUser = (User) observer;
                user.getPrintWriter().println("- " + channelUser.getUsername());
            }
        } else {
            user.getPrintWriter().println("you're not in a channel !");
        }
    }
}
