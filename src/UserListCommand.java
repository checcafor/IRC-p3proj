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
        if (user.getCurrentChannel() != null) {
            List<Observer> users = channel.getUsers();
            user.getPrintWriter().println("Nel canale " + channel.getName() + " ci sono i seguenti utenti:");

            for (Observer observer : users) {
                User channelUser = (User) observer;
                user.getPrintWriter().println("- " + channelUser.getUsername());
            }
        } else {
            user.getPrintWriter().println("non sei in nessun canale !");
        }
    }
}
