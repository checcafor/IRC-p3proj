package patterns.strategy;

import patterns.factoryPattern.Channel;

import patterns.observer.User;

import patterns.singleton.Server;

/**
 * La classe KickAction implementa l'interfaccia AdminActionStrategy
 * e rappresenta l'azione amministrativa di espellere un utente da un canale della chat.
 */
public class KickAction implements AdminActionStrategy {
    private Channel channel;
    private Admin admin;
    private User target;

    /**
     * Costruttore per la classe KickAction.
     *
     * @param admin      L'amministratore che esegue l'azione.
     * @param nameTarget Il nome dell'utente da espellere.
     */
    public KickAction(Admin admin, String nameTarget) {
        this.channel = admin.getCurrentChannel();
        this.admin = admin;
        this.target = Server.getInstance().getUserByName(nameTarget);
    }

    /**
     * Funzione che esegue l'azione di kickare l'utente dal canale di chat.
     */
    @Override
    public void performAction() {
        if (channel != null) {      // se l'admin si trova in un canale
            if(target == null){     // se l'utente da kickare NON è stato trovato
                admin.getPrintWriter().println( "user not found!"); // messaggio di errore inviato all'admin
            } else {
                // se l'utente da kickare si trova in un canale ed è uguale a quello del admin
                if (target.getCurrentChannel() != null && target.getCurrentChannel() == channel) {
                    channel.kickUser(admin, target); // esecuzione del comando mediante chiamata a funzione
                } else if (target.getCurrentChannel() != null && target.getCurrentChannel() != channel) {
                    // altrimenti l'utente da kickare non si trova in quel canale
                    admin.getPrintWriter().println( target.getUsername() + " is not in this channel");
                }
            }
        } else {    // altrimenti l'admin non è in nessun canale
            admin.getPrintWriter().println( " you're not in a channel");
        }
    }
}
