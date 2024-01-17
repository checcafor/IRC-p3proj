import java.util.List;

public interface Channel {
    public String getName();
    public void addUser(Observer user);
    public void removeUser(Observer user);
    public void broadcastMessage(User sender, String message);
    public List<Observer> getUsers ();
    public void kickUser(User user);
    public void banUser(User user);
    public void unbanUser(User user);
    public void promote(User user);
}
