public class PromoteAction implements AdminActionStrategy {
    private Channel channel;
    private Admin admin;
    private User target;

    public PromoteAction(Admin admin, String nameTarger) {
        this.channel = admin.getCurrentChannel();
        this.admin = admin;
        this.target = Server.getInstance().getUserByName(nameTarger);
    }

    @Override
    public void performAction() {
        channel.promote(admin, target); // esecuzione del comando mediante chiamata a funzione
    }
}
