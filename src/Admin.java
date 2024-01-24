public class Admin extends User {
    private AdminActionStrategy action; // variabile di tipo interfaccia che serve per settare ed eseguire i comandi admin

    public Admin(String username) { // costruttore che richiama il costruttore della superclasse
        super(username);
    }

    public AdminActionStrategy getAction() {
        return action;
    }

    public void setAction(AdminActionStrategy action) {
        this.action = action;
    }

    public void adminAction (String clientMessage) {
        if (clientMessage.startsWith("/kick ")) {
            setAction(new KickAction(this, clientMessage.substring(6)));
            action.performAction();
        } else if (clientMessage.startsWith("/ban ")) {
            setAction(new BanAction(this, clientMessage.substring(5)));
            action.performAction();
        } else if (clientMessage.startsWith("/unban ")) {
            setAction(new UnbanAction(this, clientMessage.substring(7)));
            action.performAction();
        } else if (clientMessage.startsWith("/promote ")) {
            setAction(new PromoteAction(this, clientMessage.substring(9)));
            action.performAction();
        } else {
            super.handleGeneralCommands(clientMessage);
        }
    }
}