public class KickAction implements AdminActionStrategy {
    private Channel channel;
    public KickAction(Channel channel) {
        this.channel = channel;
    }

    @Override
    public void performAction(User admin, User target) {
        channel.kickUser(target);
        admin.sendMessage("hai kikkkkkaaatoo"); // da mandare a tutti gli admin
    }
}
