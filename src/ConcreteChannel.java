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
        if(user == null){   // se l'oggetto dell'utente cercato è null allora esso non esiste
            admin.getPrintWriter().println("user not found ! ");
            return;
        }
        if (users.contains(user)) {
            user.getPrintWriter().println("you've been kicked from #" + name);
            user.leaveChannel();
            //admin.getPrintWriter().println("you've kicked " + user.getUsername());
        } else {
            admin.getPrintWriter().println(user.getUsername() + " isn't in this channel");
        }
    }

    public void banUser(Admin admin,User user) {
        if(user == null){   // se l'oggetto dell'utente cercato è null allora esso non esiste
            admin.getPrintWriter().println("user not found ! ");
            return;
        }
        if (bannedUsers.contains(user.getUsername())) {
            admin.getPrintWriter().println(user.getUsername() + " is already banned");
        } else if (!users.contains(user)) {
            admin.getPrintWriter().println(user.getUsername() + " isn't in this channel");
        } else {
            notify(user.getUsername() + " has been banned");
            bannedUsers.add(user.getUsername());
            user.leaveChannel();
            user.setCurrentChannel(null);
            //admin.getPrintWriter().println("You've banned " + user.getUsername());
        }
    }

    public void unbanUser(Admin admin,User user) {
        if(user == null){   // se l'oggetto dell'utente cercato è null allora esso non esiste
            admin.getPrintWriter().println("user not found ! ");
            return;
        }
        if (bannedUsers.contains(user.getUsername())) {
            bannedUsers.remove(user.getUsername());
            user.getPrintWriter().println("you've been unbanned from #" + name);
        } else {
            admin.getPrintWriter().println(user.getUsername() + " is already unbanned or has never been banned");
        }
    }

    public void promote(Admin admin, User user) {
        if(user == null){   // se l'oggetto dell'utente cercato è null allora esso non esiste
            admin.getPrintWriter().println("user not found ! ");
            return;
        }

        Server server = Server.getInstance();

        if(!server.isAdmin(user.getUsername())) {

            Admin new_admin = new Admin(user);          // viene creato un'oggetto admin, copia di quello utente ma con privilegi da admin
            removeUser(user);                           // viene rimosso l'utente (base) dalla lista presente nel canale
            server.removeUserToServer(user);            // viene rimosso l'utente (base) dalla lista di utenti connessi al server
            server.addUserToServer(new_admin);          // viene aggiunto l'utente (che ora figura come admin) alla lista degli utenti connessi al server
            server.addAdmin(new_admin.getUsername());   // viene aggiunto il nome delll'utente (che ora figura come admin) alla lista degli admin del server
            addUser(new_admin);                         // viene aggiunto l'utente (che ora figura come admin) alla lista degli utenti connessi al canale

            if (new_admin.getPrintWriter() != null) {
                new_admin.getPrintWriter().println("Now you're admin of the server!");
            }
        } else {
            admin.getPrintWriter().println(user.getUsername() + " is already an admin");
        }
    }

    public void sendMessage(User sender, String message) {
        for(Observer user : users) {
            if (user != sender) {
                user.update("[ ~ " + sender.getUsername() + "] : " +  message);
            } else {
                sender.getPrintWriter().println("[ ~ me ] : " +  message);
            }
        }
    }

    public boolean searchBannedUser (User user) {
        return bannedUsers.contains(user.getUsername());
    }
}
