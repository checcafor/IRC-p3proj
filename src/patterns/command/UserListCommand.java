package patterns.command;

import patterns.observer.*;

import java.util.List;

public class UserListCommand implements Command {
    private ConcreteChannel channel;
    private User user;

    public UserListCommand(User user) {
        this.user = user;
        this.channel = user.getCurrentChannel();
    }

    @Override
    public void execute() {

        if (channel != null) {
            List<Observer> users = channel.getUsers();
            user.getPrintWriter().println("In #" + channel.getName() + " there are these users:");

            for (Observer observer : users) {
                User channelUser = (User) observer;
                user.getPrintWriter().println("- " + channelUser.getUsername());
            }
        } else {
            user.getPrintWriter().println("you're not in a channel !");
        }
    }
}
