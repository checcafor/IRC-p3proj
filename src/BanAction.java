public class BanAction implements AdminActionStrategy {
    private Channel channel;

    public BanAction(Channel channel) {
        this.channel = channel;
    }

    @Override
    public void performAction(User admin, User target) {
        channel.banUser(target);
        admin.sendMessage("hai bannato"); // da mandare a tutti gli admin
    }
}
