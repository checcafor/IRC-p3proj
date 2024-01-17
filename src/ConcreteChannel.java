import java.util.ArrayList;
import java.util.List;

public class ConcreteChannel implements Channel {
    private String name;
    private List<Observer> users;

    public ConcreteChannel(String name) {
        this.name = name;
        users = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void addUser(Observer user) {
        users.add(user);
    }

    public void removeUser(Observer user) {
        users.remove(user);
    }

    public void broadcastMessage(User sender, String message) {
        for(Observer user : users) {
            if (user != sender) {
                user.update(message);
            }
        }
    }

    public List<Observer> getUsers () {
        return users;
    }

    public void kickUser(User user) {
        removeUser(user);
    }

    public void banUser(User user) {
        // aggiungere lista per bannati
        removeUser(user);
    }

    public void unbanUser(User user) {
        // togli da lista bannati
    }

    public void promote(User user) {
        // aggiungi a lista admin
    }

    public void sendMessage(String message) {
        ////////////
    }
}
