package patterns.command;

import patterns.observer.ConcreteChannel;
import patterns.observer.User;

/**
 * Questa classe rappresenta un comando per consentire a un utente di lasciare il canale corrente.
 * Il comando gestisce la logica di uscita di un utente da un canale.
 */
public class LeaveChannelCommand implements Command {
    private User user; // l'utente che vuole lasciare il canale
    private ConcreteChannel channel; // il canale corrente dell'utente

    /**
     * Costruttore della classe che inizializza il comando per lasciare il canale.
     * @param user L'utente che vuole lasciare il canale
     */
    public LeaveChannelCommand(User user) {
        this.user = user;
        this.channel = user.getCurrentChannel();
    }

    /**
     * Metodo che esegue il comando di uscita dal canale corrente.
     * Se l'utente non è in un canale, viene fornito un messaggio appropriato.
     * Altrimenti, l'utente viene rimosso dal canale e vengono forniti i messaggi di notifica corrispondenti.
     */
    @Override
    public void execute() {
        if (channel == null) {
            user.getPrintWriter().println("you're not in a channel");
        } else {
            channel.removeUser(user);
            channel.notify(user.getUsername() + " has leaved the channel !");
            user.getPrintWriter().println("you leaved the channel #" + channel.getName() + ", bye bye");
            user.setCurrentChannel(null);
        }
    }
}
