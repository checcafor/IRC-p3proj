public class JoinCommand implements Command {
    private Server server;
    private User user;
    private Channel channel;

    public JoinCommand(Server server, User user, Channel channel) {
        this.server = server;
        this.user = user;
        this.channel = channel;
    }

    @Override
    public void execute() {
        String channelName = channel.getName();

        Channel channel = server.getChannels().stream()
                .filter(c -> c.getName().equalsIgnoreCase(channelName))
                .findFirst()
                .orElse(null);

        if (channel != null) {
            user.joinChannel(channel);
            user.sendMessage("Joined channel " + channel.getName());
        } else {
            user.sendMessage("Channel " + channelName + " not found");
        }
    }
}