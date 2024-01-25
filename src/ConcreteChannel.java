import java.util.*;

public class ConcreteChannel implements Channel {
    private String name; // nome del canale
    private List<Observer> users; // lista contenente gli oggetti che implementano l'interfaccia ( user e admin )
    private Set<String> bannedUsers;

    public ConcreteChannel(String name) { // costruttore
        this.name = name;
        users = new ArrayList<>();
        bannedUsers = new HashSet<>();
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

    public void notify (String message) {
        for(Observer user : users) {
            user.update(message);
        }
    }

    public List<Observer> getUsers () {
        return users;
    }

    public void kickUser(Admin admin, User user) {
        if (user.getCurrentChannel() == admin.getCurrentChannel()) {
            user.getPrintWriter().println(" you've been kicked from #" + name);
            user.leaveChannel();
        } else {
            admin.getPrintWriter().println(user.getUsername() + " isn't in this channel");
        }
    }

    public void banUser(Admin admin, User user) {
        if (bannedUsers.contains(user.getUsername())) {
            admin.getPrintWriter().println(user.getUsername() + " is already banned");
        } else if (!users.contains(user)) {
            admin.getPrintWriter().println(user.getUsername() + " isn't in this channel !");
        } else {
            notify(user.getUsername() + " has been banned");
            bannedUsers.add(user.getUsername());
            removeUser(user);
            user.setCurrentChannel(null);
        }
    }

    public void unbanUser(Admin admin, User user) {
        if (bannedUsers.contains(user.getUsername())) {
            bannedUsers.remove(user.getUsername());
            user.getPrintWriter().println(" you've been unbanned from #" + name);
        } else {
            admin.getPrintWriter().println(user.getUsername() + " is already unbanned or has never been banned");
        }
    }

    public void promote(Admin ad, User user) {
        Server server = Server.getInstance();

        if(!server.isAdmin(user.getUsername())) {
            Admin admin = (Admin) user;
            server.addUserToServer(admin);
            server.addAdmin(admin.getUsername());
            addUser(admin);

            if (admin.getPrintWriter() != null) {
                admin.getPrintWriter().println("Now you're admin!");
            }
        } else {
            ad.getPrintWriter().println(user.getUsername() + " is already an admin");
        }
    }

    public void sendMessage(User sender, String message) {
        for(Observer user : users) {
            if (user != sender) {
                user.update(message);
            }
        }
    }

    public boolean searchBannedUser (User user) {
        return bannedUsers.contains(user.getUsername());
    }
}
