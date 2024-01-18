import java.io.PrintWriter;
import java.util.List;

public class SendMexCommand  implements Command {
    String message;
    User user;
    ConcreteChannel channel;
    List<Observer> userlist;

    public SendMexCommand(User user, String message) {
        this.user = user;
        this.message = message;
        channel = user.getCurrentChannel();
    }

    @Override
    public void execute() { // utilizzare funzione da concretechannel
        if (channel != null) {
            userlist = channel.getUsers();

            channel.sendMessage(user, "[ ~ " + user.getUsername() + "] : " +  message);
        }
    }
}
