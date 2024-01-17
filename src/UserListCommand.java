import java.util.List;

public class UserListCommand implements Command {
    private Channel channel;
    private User user;

    public UserListCommand(Channel channel, User user) {
        this.channel = channel;
        this.user = user;
    }

    @Override
    public void execute() {
        List<Observer> users = channel.getUsers();
    }
}
