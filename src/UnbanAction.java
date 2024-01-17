public class UnbanAction implements AdminActionStrategy {
    private Channel channel;

    public UnbanAction(Channel channel) {
        this.channel = channel;
    }

    @Override
    public void performAction(User admin, User target) {
        channel.unbanUser(target);
    }
}
