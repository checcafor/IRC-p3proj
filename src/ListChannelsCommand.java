import java.util.List;

public class ListChannelsCommand implements Command {
    private Server server;
    private User user;

    public ListChannelsCommand(Server server, User user) {
        this.server = Server.getInstance();
        this.user = user;
    }

    @Override
    public void execute() { // utilizzare funzione da concretechannel
        List<Channel> channels = server.getChannels();
        StringBuilder channelList = new StringBuilder("Active channels: ");
        for (Channel channel : channels) {
            channelList.append("#").append(channel.getName()).append(" ");
        }
        user.sendMessage(channelList.toString());
    }
}