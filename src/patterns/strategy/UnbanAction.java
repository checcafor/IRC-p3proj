package patterns.strategy;

import patterns.factoryPattern.Channel;

import patterns.observer.User;

import patterns.singleton.Server;

/**
 * La classe UnbanAction implementa l'interfaccia AdminActionStrategy
 * e rappresenta l'azione amministrativa di rimuovere il ban di un utente da un canale di chat.
 */
public class UnbanAction implements AdminActionStrategy {
    private Channel channel;
    private Admin admin;
    private User target;

    /**
     * Costruttore per la classe UnbanAction.
     *
     * @param admin      L'amministratore che esegue l'azione.
     * @param nameTarget Il nome dell'utente da rimuovere dal ban.
     */
    public UnbanAction(Admin admin, String nameTarget) {
        this.channel = admin.getCurrentChannel();
        this.admin = admin;
        this.target = Server.getInstance().getUserByName(nameTarget);
    }

    /**
     * Esegue l'azione di revocare il ban ad un determinato utente.
     */
    @Override
    public void performAction() {

        if (channel != null) {      // se l'admin si trova in un canale
            if(target == null){      // se l'utente da bannare NON è stato trovato
                admin.getPrintWriter().println( "user not found!"); // messaggio di errore inviato all'admin
            } else {
                // se l'utente da bannare si trova in un canale ed è uguale a quello del admin
                if (target.getCurrentChannel() != null && target.getCurrentChannel() == channel) {
                    channel.unbanUser(admin, target); // esecuzione del comando mediante chiamata a funzione
                } else if (target.getCurrentChannel() != null && target.getCurrentChannel() != channel) {
                    // altrimenti l'utente da bannare non si trova in quel canale
                    admin.getPrintWriter().println( target.getUsername() + " is not in this channel");
                }
            }
        } else {
            // altrimenti l'admin non è in nessun canale
            admin.getPrintWriter().println( " you're not in a channel");
        }
    }
}
