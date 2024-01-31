import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server {
    private static Server instance; // istanza del server ( che sarà univoca grazie a singleton )
    private Map<String, Channel> channels; // lista canali
    private Map<String, User> users; // map utenti presenti nel server ( username , oggetto di tipo User o admin grazie al polimorfismo )
    private Set<String> administrators; // map contenente gli username degli amministratori presenti nel server

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

    public Set<String> getAdministrators() {
        return administrators;
    }

    public void addChannel (String name) { // metodo per aggiungere canali
        ChannelFactory factory = new ChannelFactory();
        ConcreteChannel channel = (ConcreteChannel) factory.createChannel(name);
        channels.put(name, channel);
    }

    public void addAdmin (String name) {
        administrators.add(name);
    }

    public void addUserToServer (User user) {
        users.put(user.getUsername(), user);
    }
    public void removeUserToServer(User user){
        users.remove(user.getUsername(), user);
    }

    public Channel getChannelByName (String name) {
        return channels.get(name);
    }
    public Collection<Channel> getChannels () {
        return channels.values();
    }

    public Map<String, User> getUsers(){
        return users;
    }

    public User getUserByName (String username) {
        return users.get(username);
    }

    public boolean isAdmin (String username) {
        return administrators.contains(username);
    }

    public static void main(String[] args) {
        Server server = Server.getInstance();

        server.addChannel("general");
        server.addChannel("default");
        server.addAdmin("kekka");

        try {
            ServerSocket serverSocket = new ServerSocket(12347);

            while (true) {
                Socket clientSocket = serverSocket.accept(); // accetta richieste dal client

                // per leggere input dal client
                BufferedReader clientReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                // per inviare output al client
                PrintWriter clientWriter = new PrintWriter(clientSocket.getOutputStream(), true);

                Map<String, User> users = server.getUsers();
                Set<String> administrators = server.getAdministrators();

                String username;
                boolean userAlreadyExist;

                do {
                    username = clientReader.readLine().trim();
                    userAlreadyExist = users.containsKey(username);

                    if (userAlreadyExist) {
                        // Send a message to the client indicating that the username is already taken
                        clientWriter.println("Username is already taken. Please choose a different username:");
                    } else {
                        // Send a message to the client indicating that the username is accepted
                        clientWriter.println("OK");
                    }
                } while (userAlreadyExist);

                User user;
                if (administrators.contains(username)) {
                    user = new Admin(username);
                } else {
                    user = new User(username);
                }

                user.setPrintWriter(clientWriter);
                users.put(username, user);
                user.getPrintWriter().println("Hi " + user.getUsername() + "! Welcome in the server");

                String logMessage = user.getUsername() + " has joined the server";
                DataToDatabase logToDatabase = new DataToDatabase(logMessage);

                RetrieveDataFromDatabase retriever = new RetrieveDataFromDatabase(logMessage);
                System.out.println("[LOG ~ " + retriever.getRetrievedDate() + "]: " + logMessage);

                String _username = username;

                new Thread(() -> {
                    try {
                        String clientMessage;
                        User utente = users.get(_username);
                        Admin ad = new Admin(utente);
                        //Admin ad = (Admin) users.get(username);

                        while ((clientMessage = clientReader.readLine()) != null) {
                            if(getInstance().isAdmin(_username)) {
                                ad.adminAction(clientMessage);
                            } else {
                                utente.handleGeneralCommands(clientMessage);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        User quittingUser = server.getUserByName(_username);

                        if(quittingUser.getCurrentChannel() != null){
                            quittingUser.leaveChannel();
                        }
                        users.remove(_username);

                        String logoutMessage = _username + " has leaved the server";
                        DataToDatabase logoutToDatabase = new DataToDatabase(logoutMessage);

                        RetrieveDataFromDatabase retrieverLogout = new RetrieveDataFromDatabase(logoutMessage);
                        System.out.println("[LOG ~ " + retrieverLogout.getRetrievedDate() + "]: " + logoutMessage);
                    }
                }).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}