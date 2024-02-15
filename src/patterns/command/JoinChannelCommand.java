package patterns.command;

import patterns.observer.ConcreteChannel;
import patterns.observer.User;

import patterns.singleton.Server;

/**
 * Questa classe rappresenta un comando per consentire a un utente di unirsi a un canale specifico su un server.
 * Il comando gestisce la logica di unione di un utente a un già canale esistente.
 */
public class JoinChannelCommand implements Command {
    private Server server; // il server al quale il comando è associato
    private User user; // l'utente che vuole unirsi al canale
    private String channelName; // il nome del canale al quale l'utente vuole unirsi
    private ConcreteChannel channel; // il canale al quale l'utente vuole unirsi

    /**
     * Costruttore della classe che inizializza il comando per l'unione di un utente a un canale specifico.
     * @param user L'utente che vuole unirsi al canale
     * @param channelName Il nome del canale al quale l'utente vuole unirsi
     */
    public JoinChannelCommand(User user, String channelName) {
        this.server = Server.getInstance();
        this.user = user;
        this.channelName = channelName;
        channel = (ConcreteChannel) server.getChannelByName(channelName); // ottiene il canale dal server
    }

    /**
     * Metodo che esegue il comando di unione all'interno del canale.
     * Se il canale non esiste o se l'utente è già nel canale, vengono forniti i messaggi appropriati.
     */
    @Override
    public void execute() {
        if (channel == null) {
            user.getPrintWriter().println("Insert Valid Channel's Name");
        } else if (channel == user.getCurrentChannel()) {
            user.getPrintWriter().println("you're already in this channel");
        } else {
            if (user.getCurrentChannel() != null) {
                user.leaveChannel();
            }
            if (channel.searchBannedUser(user)) {
                user.getPrintWriter().println("you have been banned from this channel.");
            } else {
                channel.notify(user.getUsername() + " has joined to the channel !");
                channel.addUser(user);
                user.setCurrentChannel(channel);
                user.getPrintWriter().println("Welcome to " + channelName + " !! ");
            }
        }
    }
}