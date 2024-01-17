import java.util.List;

public class UserListCommand implements Command {
    private ConcreteChannel channel;
    private User user;

    public UserListCommand(ConcreteChannel channel, User user) {
        this.channel = channel;
        this.user = user;
    }

    @Override
    public void execute() { // utilizzare funzione da concretechannel
        List<Observer> users = channel.getUsers();
    }
}
