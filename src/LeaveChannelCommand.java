public class LeaveChannelCommand implements Command {
    private User user;
    private ConcreteChannel channel;

    public LeaveChannelCommand(User user) {
        this.user = user;
        this.channel = user.getCurrentChannel();
    }

    @Override
    public void execute() {
        if (channel == null) {
            user.getPrintWriter().println("you're not in a channel");
        } else {
            channel.removeUser(user);
            channel.notify(user.getUsername() + " has leaved the channel !");
            user.getPrintWriter().println("you leaved the channel #" + channel.getName() + ", bye bye");
            user.setCurrentChannel(null);
        }
    }
}
