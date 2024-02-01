package patterns.command;

import patterns.observer.ConcreteChannel;
import patterns.observer.User;

import patterns.singleton.Server;

public class JoinChannelCommand implements Command {
    private Server server;
    private User user;
    private String channelName;
    private ConcreteChannel channel;

    public JoinChannelCommand(User user, String channelName) {
        this.server = Server.getInstance();
        this.user = user;
        this.channelName = channelName;
        channel = (ConcreteChannel) server.getChannelByName(channelName);
    }

    @Override
    public void execute() {
        if (channel == null) {
            user.getPrintWriter().println("Insert Valid Channel's Name");
        } else if (channel == user.getCurrentChannel()) {
            user.getPrintWriter().println("you're already in this channel");
        } else {
            if (user.getCurrentChannel() != null) {
                user.leaveChannel();
            }
            if (channel.searchBannedUser(user)) {
                user.getPrintWriter().println("you have been banned from this channel.");
            } else {
                channel.notify(user.getUsername() + " has joined to the channel !");
                channel.addUser(user);
                user.setCurrentChannel(channel);
                user.getPrintWriter().println("Welcome to " + channelName + " !! ");
            }
        }
    }
}