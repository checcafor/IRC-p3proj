public class BanAction implements AdminActionStrategy {
    private Channel channel;
    private Admin admin;
    private User target;

    public BanAction(Admin admin, String nameTarget) {
        this.channel = admin.getCurrentChannel();
        this.admin = admin;
        this.target = Server.getInstance().getUserByName(nameTarget);
    }

    @Override
    public void performAction() {
        channel.banUser(target); // esecuzione del comando mediante chiamata a funzione
        admin.getPrintWriter().println("You've banned " + target.getUsername());
    }
}
