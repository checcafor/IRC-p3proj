public class Admin extends User {
    private AdminActionStrategy action; // variabile di tipo interfaccia che serve per settare ed eseguire i comandi admin

    public Admin(String username) { // costruttore che richiama il costruttore della superclasse
        super(username);
    }

    public Admin(User user){
        super(user);
    }

    public AdminActionStrategy getAction() {
        return action;
    }

    public void setAction(AdminActionStrategy action) {
        this.action = action;
    }

    public void adminAction (String clientMessage) {
        Admin admin = (Admin) Server.getInstance().getUserByName(this.getUsername()); // viene recuperato l'admin che vuole eseguire il comando
        if (clientMessage.startsWith("/kick ")) {
            setAction(new KickAction(admin, clientMessage.substring(6)));
            action.performAction();
        } else if (clientMessage.startsWith("/ban ")) {
            setAction(new BanAction(admin, clientMessage.substring(5)));
            action.performAction();
        } else if (clientMessage.startsWith("/unban ")) {
            setAction(new UnbanAction(admin, clientMessage.substring(7)));
            action.performAction();
        } else if (clientMessage.startsWith("/promote ")) {
            setAction(new PromoteAction(admin, clientMessage.substring(9)));
            action.performAction();
        } else {
            super.handleGeneralCommands(clientMessage);
        }
    }
}