public class UnbanAction implements AdminActionStrategy {
    private Channel channel;
    private Admin admin;
    private User target;

    public UnbanAction(Admin admin, String nameTarget) {
        this.channel = admin.getCurrentChannel();
        this.admin = admin;
        this.target = Server.getInstance().getUserByName(nameTarget);
    }

    @Override
    public void performAction() {
        channel.unbanUser(target); // esecuzione del comando mediante chiamata a funzione
    }
}
