package patterns.singleton;

import dbConnector.DataToDatabase;
import dbConnector.RetrieveDataFromDatabase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

import patterns.observer.User;
import patterns.observer.ConcreteChannel;

import patterns.factoryPattern.Channel;
import patterns.factoryPattern.ChannelFactory;

import patterns.strategy.Admin;

/**
 * Questa classe implementa il pattern Singleton per il server di messaggistica.
 * Il server gestisce l'accesso degli utenti, la creazione e la gestione dei canali, e le interazioni tra gli utenti.
 */
public class Server {
    private static Server instance = new Server(); // istanza del server ( che sarà univoca grazie a singleton )
    private Map<String, Channel> channels; // lista canali
    private Map<String, User> users; // map utenti presenti nel server ( username , oggetto di tipo patterns.observer.User o admin grazie al polimorfismo )
    private Set<String> administrators; // map contenente gli username degli amministratori presenti nel server

    private Server () {
        channels = new HashMap<>(); // instanziare un array contenente tutti i canali disponibili nel server
        users = new HashMap<>();
        administrators = new HashSet<>();
    }

    /**
     * Restituisce l'unica istanza del server (implementazione del pattern Singleton).
     * @return L'unica istanza del server
     */
    public static Server getInstance() { // implementazione singleton - Eager Inizialization
        return instance;
    }

    /**
     * Restituisce l'insieme degli username degli amministratori presenti nel server.
     * @return L'insieme degli username degli amministratori
     */
    public Set<String> getAdministrators() {
        return administrators;
    }

    // Metodi per la gestione dei canali e degli utenti nel server

    /**
     * Aggiunge un nuovo canale al server con il nome specificato.
     * @param name Il nome del nuovo canale
     */
    public void addChannel (String name) { // metodo per aggiungere canali
        ChannelFactory factory = new ChannelFactory();  // creata un istanza della ChannelFactory per creare un canale
        ConcreteChannel channel = (ConcreteChannel) factory.createChannel(name);    // creazione di un canale
        channels.put(name, channel);    // aggiunta alla lista dei canali
    }

    /**
     * Aggiunge un amministratore al server con il nome specificato.
     * @param name Il nome dell'amministratore
     */
    public void addAdmin (String name) {
        administrators.add(name);
    }

    /**
     * Aggiunge un nuovo utente al server.
     * @param user L'utente da aggiungere
     */
    public void addUserToServer (User user) {
        users.put(user.getUsername(), user);
    }
    /**
     * Rimuove un utente dal server.
     * @param user L'utente da rimuovere
     */
    public void removeUserToServer(User user){
        users.remove(user.getUsername(), user);
    }

    /**
     * Restituisce il canale con il nome specificato.
     * @param name Il nome del canale da cercare
     * @return Il canale corrispondente al nome specificato, o null se non esiste
     */
    public Channel getChannelByName (String name) {
        return channels.get(name);
    }

    /**
     * Restituisce una collezione contenente tutti i canali presenti nel server.
     * @return Una collezione contenente tutti i canali del server
     */
    public Collection<Channel> getChannels () {
        return channels.values();
    }

    /**
     * Restituisce la mappa degli utenti presenti nel server.
     * @return La mappa degli utenti del server (username, utente)
     */
    public Map<String, User> getUsers(){
        return users;
    }

    /**
     * Restituisce l'utente con lo username specificato.
     * @param username Lo username dell'utente da cercare
     * @return L'utente corrispondente allo username specificato, o null se non esiste
     */
    public User getUserByName (String username) {
        return users.get(username);
    }

    /**
     * Verifica se l'utente con lo username specificato è un amministratore del server.
     * @param username Lo username dell'utente da controllare
     * @return true se l'utente è un amministratore, false altrimenti
     */
    public boolean isAdmin (String username) {
        return administrators.contains(username);
    }

    /**
     * Metodo principale per avviare il server.
     * Il server gestisce le richieste dei client e le interazioni tra gli utenti.
     * @param args Argomenti della riga di comando
     */
    public static void main(String[] args) {
        Server server = Server.getInstance();
        // aggiunta canali al server
        server.addChannel("general");
        server.addChannel("default");
        // aggiunta admin di default
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
                    username = clientReader.readLine().trim();          // viene letto il nome inserito dall'utente
                    userAlreadyExist = users.containsKey(username);     // viene controllato che il nome non esista già

                    // se l'utente con quel nome già esiste
                    if (userAlreadyExist) {
                        // viene inviato un messaggio di errore al client
                        clientWriter.println("Username is already taken. Please choose a different username:");
                    } else {
                        // viene restituito un messaggio di conferma
                        clientWriter.println("OK");
                    }
                } while (userAlreadyExist);

                User user;
                // se l'utente risulta negli admin
                if (administrators.contains(username)) {
                    user = new Admin(username); // gli viene istanziata un'oggetto di tipo Admin
                } else {
                    user = new User(username);  // altrimenti uno di tipo User
                }

                user.setPrintWriter(clientWriter);
                users.put(username, user);
                user.getPrintWriter().println("Hi " + user.getUsername() + "! Welcome in the server");

                String logMessage = user.getUsername() + " has joined the server";
                DataToDatabase logToDatabase = new DataToDatabase(logMessage);                      // viene registrato il log nel database

                RetrieveDataFromDatabase retriever = new RetrieveDataFromDatabase(logMessage);      // viene recuperato l'ultimo log dal db (qusto per ottenere consistenza)
                System.out.println("[LOG ~ " + retriever.getRetrievedDate() + "]: " + logMessage);  // viene stampato il log anche sul terminale del Server

                String _username = username;

                new Thread(() -> {
                    try {
                        String clientMessage;
                        User utente = users.get(_username);
                        Admin ad = new Admin(utente);

                        // ciclo per la lettura dei messaggi dal client
                        while ((clientMessage = clientReader.readLine()) != null) {
                            // se il messaggio inizia con "#", verifica se l'utente è un amministratore e invia la risposta al client per il controllo nel backand
                            if (clientMessage.startsWith("#")) {
                                utente.getPrintWriter().println("#" + (server.isAdmin(clientMessage.substring(1))));
                            }
                            // se l'utente è un amministratore, gestisce l'azione dell'amministratore
                            if(getInstance().isAdmin(_username)) {
                                ad.adminAction(clientMessage);
                            } else { // altrimenti, gestisce i comandi di base dell'utente
                                utente.handleGeneralCommands(clientMessage);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        User quittingUser = server.getUserByName(_username); // viene recuperato l'oggetto dell'utente che sta uscendo dal server

                        // se l'utente si trovava prima in qualche canale
                        if(quittingUser.getCurrentChannel() != null){
                            quittingUser.leaveChannel();    // si fa uscire l'utente dal canale in cui si trovava
                        }
                        users.remove(_username);    // viene rimosso quindi dalla lista degli utenti del server

                        String logoutMessage = _username + " has leaved the server";
                        DataToDatabase logoutToDatabase = new DataToDatabase(logoutMessage);    // viene registrato il log sul database

                        RetrieveDataFromDatabase retrieverLogout = new RetrieveDataFromDatabase(logoutMessage);     // viene recuperato l'ultimo log dal db (qusto per ottenere consistenza)
                        System.out.println("[LOG ~ " + retrieverLogout.getRetrievedDate() + "]: " + logoutMessage); // viene stampato il log anche sul terminale del server
                    }
                }).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}