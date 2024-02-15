package patterns.strategy;  // package di appartenenza

import patterns.observer.User;

import patterns.singleton.Server;

/**
 * La classe Admin rappresenta un utente amministratore nel sistema di chat.
 * Gli amministratori hanno la capacità di eseguire azioni amministrative specifiche utilizzando strategie.
 * Estende la classe User e implementa il pattern Observer.
 */
public class Admin extends User {
    private AdminActionStrategy action; // variabile di tipo interfaccia che serve per settare ed eseguire i comandi admin

    /**
     * Costruttore per creare un oggetto Admin con un nome utente specificato.
     *
     * @param username Il nome utente dell'amministratore.
     */
    public Admin(String username) {  // costruttore che inizializza un nuovo admin utilizzando il costruttore della superclasse (User)
        super(username);
    }

    /**
     * Costruttore per creare un oggetto Admin basato su un oggetto User esistente.
     *
     * @param user L'oggetto User da convertire in Admin.
     */
    public Admin(User user){ // costruttore che inizializza un nuovo admin utilizzando come parametro una variabile di tipo admin
        super(user);
    }

    /**
     * Ottiene l'attuale AdminActionStrategy.
     *
     * @return L'attuale AdminActionStrategy.
     */
    public AdminActionStrategy getAction() {
        return action;
    }

    /**
     * Imposta l'AdminActionStrategy da utilizzare per eseguire comandi amministrativi.
     *
     * @param action L'AdminActionStrategy da impostare.
     */
    public void setAction(AdminActionStrategy action) {
        this.action = action;
    }

    /**
     * Esegue un'azione amministrativa in base al messaggio del client fornito.
     * Se il messaggio corrisponde a un comando amministrativo noto, viene impostata e eseguita la strategia appropriata.
     * In caso contrario, delega alla gestione dei comandi generali nella superclasse.
     *
     * @param clientMessage Il messaggio ricevuto dal client.
     */
    public void adminAction (String clientMessage) {
        Admin admin = (Admin) Server.getInstance().getUserByName(this.getUsername()); // viene recuperato l'admin che vuole eseguire il comando

        if (clientMessage.startsWith("/kick ")) {   // se il comando fornito inizia per "/kick "
            setAction(new KickAction(admin, clientMessage.substring(6)));   // viene settato come comando un'istanza di "KickAction"
            action.performAction(); // viene quindi eseguito
        } else if (clientMessage.startsWith("/ban ")) { // se il comando fornito inizia per "/ban "
            setAction(new BanAction(admin, clientMessage.substring(5)));
            action.performAction(); // viene quindi eseguito
        } else if (clientMessage.startsWith("/unban ")) { // se il comando fornito inizia per "/unban "
            setAction(new UnbanAction(admin, clientMessage.substring(7)));
            action.performAction(); // viene quindi eseguito
        } else if (clientMessage.startsWith("/promote ")) { // se il comando fornito inizia per "/promote "
            setAction(new PromoteAction(admin, clientMessage.substring(9)));
            action.performAction(); // viene quindi eseguito
        } else {
            // se nessuno di questi comandi corrisponde, allora l'admin ha inserito o un comando inesistente o uno relativo ad un utente di base
            super.handleGeneralCommands(clientMessage);
        }
    }
}