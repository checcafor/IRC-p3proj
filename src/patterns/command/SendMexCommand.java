package patterns.command;

import patterns.observer.*;

import java.util.List;

/**
 * Questa classe rappresenta un comando per inviare un messaggio all'interno di un canale.
 * Il comando gestisce la logica di invio di un messaggio da un utente a tutti gli altri utenti presenti nello stesso canale.
 */
public class SendMexCommand  implements Command {
    String message; // il messaggio da inviare
    User user; // utente che sta inviando il messaggio
    ConcreteChannel channel; // canale in cui viene inviato il messaggio
    List<Observer> userlist; // lista utenti del canale

    /**
     * Costruttore della classe che inizializza il comando per inviare un messaggio.
     * @param user L'utente che sta inviando il messaggio
     * @param message Il messaggio da inviare
     */
    public SendMexCommand(User user, String message) {
        this.user = user;
        this.message = message;
        channel = user.getCurrentChannel();
    }

    /**
     * Metodo che esegue il comando di invio del messaggio.
     * Se l'utente è all'interno di un canale, il messaggio viene inviato a tutti gli altri utenti nel canale.
     * Altrimenti, viene fornito un messaggio di errore.
     */
    @Override
    public void execute() { // utilizzare funzione da concretechannel
        // se l'utente si trova in un canale
        if (channel != null) {
            userlist = channel.getUsers();
            // invia il messaggio a tutti gli appartenenti al canale tranne all'utente
            channel.sendMessage(user, message);
        } else {
            user.getPrintWriter().println("you're not in a Channel");
        }
    }
}
