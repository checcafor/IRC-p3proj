import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Server {
    private static Server instance;
    private List<Channel> channels;
    private Map<String, User> users;

    private Server () {
        channels = new ArrayList<>(); // instanziare un array contenente tutti i canali disponibili nel server
        users = new HashMap<>();
    }

    public static Server getInstance() {
        if ( instance == null ) {
            instance = new Server();
        }

        return instance;
    }

    public void addChannel (String name) {
        ChannelFactory factory = new ChannelFactory();
        Channel channel = factory.createChannel(name);
    }

    public void addUser (User user, String username) {
        users.put(username, user);
    }

    public List<Channel> getChannels () {
        return channels;
    }

    public User getUserByName (String username) {
        return users.get(username);
    }
}