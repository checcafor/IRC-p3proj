import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server {
    private static Server instance; // istanza del server ( che sarà univoca grazie a singleton )
    private static Map<String, Channel> channels; // lista canali
    private static Map<String, User> users; // map utenti presenti nel server ( username , oggetto di tipo User o admin grazie al polimorfismo )
    private static Set<String> administrators; // map contenente gli username degli amministratori presenti nel server

    private Server () {
        channels = new HashMap<>(); // instanziare un array contenente tutti i canali disponibili nel server
        users = new HashMap<>();
        administrators = new HashSet<>();
    }

    public static Server getInstance() { // implementazione singleton
        if ( instance == null ) {
            instance = new Server();
        }

        return instance;
    }

    public void addChannel (String name) { // metodo per aggiungere canali
        ChannelFactory factory = new ChannelFactory();
        ConcreteChannel channel = factory.createChannel(name);
        channels.put(name, channel);
    }

    public void addAdmin (String name) {
        administrators.add(name);
    }

    public void addUserToServer (User user) {
        users.put(user.getUsername(), user);
    }

    public Channel getChannelByName (String name) {
        return channels.get(name);
    }
    public Collection<Channel> getChannels () {
        return channels.values();
    }

    public User getUserByName (String username) {
        return users.get(username);
    }

    public boolean isAdmin (String username) {
        return administrators.contains(username);
    }

    private static boolean channelExists(String channel) {
        return channels.containsKey(channel);
    }

    public static void main(String[] args) {
        Server server = Server.getInstance();

        server.addChannel("general");
        administrators.add("kekka");

        try {
            ServerSocket serverSocket = new ServerSocket(12347);

            while (true) {
                Socket clientSocket = serverSocket.accept(); // accetta richieste dal client

                // per leggere input dal client
                BufferedReader clientReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                // per inviare output al client
                PrintWriter clientWriter = new PrintWriter(clientSocket.getOutputStream(), true);

                String username = clientReader.readLine();
                User user = null;
                if (!users.containsKey(username)) {
                    if (administrators.contains(username)) {
                        user = new Admin(username);
                    } else {
                        user = new User(username);
                    }

                    user.setPrintWriter(clientWriter);
                    users.put(username, user);
                } else {
                    clientWriter.println("Username already exists. Please choose a different one.");
                    continue;
                }

                new Thread(() -> {
                    try {
                        String clientMessage;
                        User utente = users.get(username);
                        Admin ad;

                        while ((clientMessage = clientReader.readLine()) != null) {
                            if(getInstance().isAdmin(username)) {
                                ad = (Admin) utente;
                                ad.adminAction(clientMessage);
                            } else {
                                utente.handleGeneralCommands(clientMessage);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        users.remove(username);
                    }
                }).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}