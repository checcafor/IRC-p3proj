package patterns.command;

import patterns.factoryPattern.Channel;

import patterns.observer.User;

import patterns.singleton.Server;

import java.util.Collection;

/**
 * Questa classe rappresenta un comando per elencare i canali attivi su un server.
 * Il comando gestisce la logica di ottenere e visualizzare l'elenco dei canali attivi.
 */
public class ListChannelsCommand implements Command {
    private Server server; // server sul quale viene eseguito il comando
    private User user; // l'utente a cui verrà inviato l'elenco dei canali

    /**
     * Costruttore della classe che inizializza il comando per elencare i canali attivi.
     * @param user L'utente a cui verrà inviato l'elenco dei canali
     */
    public ListChannelsCommand(User user) {
        this.user = user;
        this.server = Server.getInstance();
    }

    /**
     * Metodo che esegue il comando di elencare i canali attivi sul server.
     * Ottiene l'elenco dei canali attivi dal server e lo invia all'utente.
     */
    @Override
    public void execute() {
        Collection<Channel> channels = server.getChannels(); // ottiene l'elenco dei canali attivi dal server
        StringBuilder channelList = new StringBuilder("Active channels: ");
        for (Channel channel : channels) {
            channelList.append("#").append(channel.getName()).append(" "); // costruisce la lista dei canali attivi
        }
        user.getPrintWriter().println(channelList.toString()); // invia la lista dei canali all'utente
    }
}