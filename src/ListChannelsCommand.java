import java.util.Collection;
import java.util.List;

public class ListChannelsCommand implements Command {
    private Server server;
    private User user;

    public ListChannelsCommand(User user) {
        this.user = user;
        this.server = Server.getInstance();
    }

    @Override
    public void execute() {
        Collection<Channel> channels = server.getChannels();
        StringBuilder channelList = new StringBuilder("Active channels: ");
        for (Channel channel : channels) {
            channelList.append("#").append(channel.getName()).append(" ");
        }
        user.getPrintWriter().println(channelList.toString());
    }
}