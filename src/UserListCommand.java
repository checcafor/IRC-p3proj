import java.util.List;

public class UserListCommand implements Command {
    private ConcreteChannel channel;
    private User user;

    public UserListCommand(User user) {
        this.user = user;
        this.channel = user.getCurrentChannel();
    }

    @Override
    public void execute() { // utilizzare funzione da concretechannel
        if (user.getCurrentChannel() != null) {
        List<Observer> users = channel.getUsers();
        user.getPrintWriter().println("nel canale " + channel.getName() + " ci sono i seguenti utenti " + users);
    }
}
