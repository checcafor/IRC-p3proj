import java.util.List;

public class ListChannelsCommand implements Command {
    private Server server;
    private User user;
    @Override
    public void execute() {
        List<Channel> channels = server.getChannels();
        StringBuilder channelList = new StringBuilder("Active channels: ");
        for (Channel channel : channels) {
            channelList.append("#").append(channel.getName()).append(" ");
        }
        user.sendMessage(channelList.toString());
    }
}
