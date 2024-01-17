public class JoinChannelCommand implements Command {
    private Server server;
    private User user;
    private ConcreteChannel channel;

    public JoinChannelCommand(Server server, User user, ConcreteChannel channel) {
        this.server = server;
        this.user = user;
        this.channel = channel;
    }

    @Override
    public void execute() { // utilizzare funzione da concretechannel
        String channelName = channel.getName();

        Channel channel = server.getChannels().stream()
                .filter(c -> c.getName().equalsIgnoreCase(channelName))
                .findFirst()
                .orElse(null);

        if (channel != null) {
            user.joinChannel((ConcreteChannel)channel);
            user.sendMessage("Joined channel " + channel.getName());
        } else {
            user.sendMessage("Channel " + channelName + " not found");
        }
    }
}