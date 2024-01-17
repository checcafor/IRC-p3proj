public class PromoteAction implements AdminActionStrategy {
    private Channel channel;

    public PromoteAction(Channel channel) {
        this.channel = channel;
    }

    @Override
    public void performAction(User admin, User target) {
        channel.promote(target);
    }
}
