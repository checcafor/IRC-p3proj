package patterns.strategy;

import patterns.factoryPattern.Channel;

import patterns.observer.User;

import patterns.singleton.Server;

/**
 * La classe PromoteAction implementa l'interfaccia AdminActionStrategy
 * e rappresenta l'azione amministrativa di promuovere un utente a un ruolo di maggior privilegio in un canale di chat.
 */
public class PromoteAction implements AdminActionStrategy {
    private Channel channel;
    private Admin admin;
    private User target;

    /**
     * Costruttore per la classe PromoteAction.
     *
     * @param admin      L'amministratore che esegue l'azione.
     * @param nameTarget Il nome dell'utente da promuovere.
     */
    public PromoteAction(Admin admin, String nameTarget) {
        this.channel = admin.getCurrentChannel();
        this.admin = admin;
        this.target = Server.getInstance().getUserByName(nameTarget);
    }

    /**
     * Esegue l'azione di promuovere l'utente a un ruolo di maggior privilegio nel canale di chat.
     */
    @Override
    public void performAction() {
        if (channel != null) {      // se l'admin si trova in un canale
            if(target == null){     // se l'utente da promuovere NON è stato trovato
                admin.getPrintWriter().println( "user not found!"); // messaggio di errore inviato all'admin
            } else {
                // se l'utente da promuovere si trova in un canale ed è uguale a quello del admin
                if (target.getCurrentChannel() != null && target.getCurrentChannel() == channel) {
                    channel.promote(admin, target); // esecuzione del comando mediante chiamata a funzione
                } else if (target.getCurrentChannel() != null && target.getCurrentChannel() != channel) {
                    // altrimenti l'utente da promuovere non si trova in quel canale
                    admin.getPrintWriter().println( target.getUsername() + " is not in this channel");
                }
            }
        } else {    // altrimenti l'admin non è in nessun canale
            admin.getPrintWriter().println( " you're not in a channel");
        }
    }
}
