public class KickAction implements AdminActionStrategy {
    private Channel channel;
    private Admin admin;
    private User target;
    public KickAction(Admin admin, String nameTarget) {
        this.channel = admin.getCurrentChannel();
        this.admin = admin;
        this.target = Server.getInstance().getUserByName(nameTarget);
    }

    @Override
    public void performAction() {
        channel.kickUser(admin, target); // esecuzione del comando mediante chiamata a funzione
    }
}
