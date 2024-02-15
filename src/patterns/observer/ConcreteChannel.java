package patterns.observer;

import patterns.factoryPattern.Channel;

import patterns.observer.User;
import patterns.observer.Observer;

import patterns.singleton.Server;

import patterns.strategy.Admin;

import java.util.*;

/**
 * Questa classe rappresenta un canale concreto all'interno del sistema di messaggistica.
 * Implementa l'interfaccia Channel e gestisce gli utenti, le operazioni sugli utenti e i messaggi all'interno del canale.
 */
public class ConcreteChannel implements Channel {
    private String name; // nome del canale
    private List<Observer> users; // lista contenente gli oggetti che implementano l'interfaccia ( user e admin )
    private Set<String> bannedUsers; // insieme degli utenti bannati dal canale

    /**
     * Costruttore della classe che inizializza un nuovo canale con il nome specificato.
     * @param name Il nome del nuovo canale
     */
    public ConcreteChannel(String name) {
        this.name = name;
        users = new ArrayList<>();
        bannedUsers = new HashSet<>();
    }

    /**
     * Restituisce il nome del canale.
     * @return Il nome del canale
     */
    public String getName() {
        return name;
    }

    /**
     * Aggiunge un nuovo utente al canale.
     * @param user L'utente da aggiungere al canale
     */
    public void addUser(Observer user) {
        users.add(user);
    }

    /**
     * Rimuove un utente dal canale.
     * @param user L'utente da rimuovere dal canale
     */
    public void removeUser(Observer user) {
        users.remove(user);
    }

    /**
     * Notifica a tutti gli utenti del canale un determinato messaggio.
     * @param message Il messaggio da notificare
     */
    public void notify (String message) {
        for(Observer user : users) {
            user.update(message);
        }
    }
    /**
     * Restituisce la lista degli utenti attualmente nel canale.
     * @return La lista degli utenti nel canale
     */
    public List<Observer> getUsers () {
        return users;
    }

    // Metodi per la gestione degli utenti e dei permessi

    /**
     * Espelle un utente dal canale.
     * @param admin L'amministratore che esegue l'operazione
     * @param user L'utente da espellere dal canale
     */
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
    /**
     * Banna un utente dal canale.
     * @param admin L'amministratore che esegue l'operazione
     * @param user L'utente da bannare dal canale
     */
    public void banUser(Admin admin,User user) {
        if(user == null){   // se l'oggetto dell'utente cercato è null allora esso non esiste
            admin.getPrintWriter().println("user not found ! ");
            return;
        }
        if (bannedUsers.contains(user.getUsername())) { // se l'utente risulta bannato nel canale corrente
            admin.getPrintWriter().println(user.getUsername() + " is already banned");
        } else if (!users.contains(user)) { // se lìutente non si trova nel canale corrente
            admin.getPrintWriter().println(user.getUsername() + " isn't in this channel");
        } else {
            notify(user.getUsername() + " has been banned");
            bannedUsers.add(user.getUsername());    // registrazione dell'utente nella lista dei bannati
            user.leaveChannel();                    // viene espulso dal canale
            user.setCurrentChannel(null);           // viene "resettato" il canale in cui si trova essendo stato bannato ed espulso
        }
    }

    /**
     * Rimuove il ban di un utente dal canale.
     * @param admin L'amministratore che esegue l'operazione
     * @param user L'utente da sbannare dal canale
     */
    public void unbanUser(Admin admin,User user) {
        if(user == null){   // se l'oggetto dell'utente cercato è null allora esso non esiste
            admin.getPrintWriter().println("user not found ! ");
            return;
        }
        if (bannedUsers.contains(user.getUsername())) { // se l'utente risulta bannato nel canale corrente
            bannedUsers.remove(user.getUsername());     // viene rimosso dalla lista degli utenti bannati
            user.getPrintWriter().println("you've been unbanned from #" + name);
        } else {
            admin.getPrintWriter().println(user.getUsername() + " is already unbanned or has never been banned");
        }
    }

    /**
     * Promuove un utente a un ruolo superiore all'interno del canale.
     * @param admin L'amministratore che esegue l'operazione
     * @param user L'utente da promuovere
     */
    public void promote(Admin admin, User user) {
        if(user == null){   // se l'oggetto dell'utente cercato è null allora esso non esiste
            admin.getPrintWriter().println("user not found ! ");
            return;
        }

        Server server = Server.getInstance();

        if(!server.isAdmin(user.getUsername())) {   // se l'utente da promuovere non è già un'admin

            Admin new_admin = new Admin(user);          // viene creato un'oggetto admin, copia di quello utente ma con privilegi da admin
            removeUser(user);                           // viene rimosso l'utente (base) dalla lista presente nel canale
            server.removeUserToServer(user);            // viene rimosso l'utente (base) dalla lista di utenti connessi al server
            server.addUserToServer(new_admin);          // viene aggiunto l'utente (che ora figura come admin) alla lista degli utenti connessi al server
            server.addAdmin(new_admin.getUsername());   // viene aggiunto il nome delll'utente (che ora figura come admin) alla lista degli admin del server
            addUser(new_admin);                         // viene aggiunto l'utente (che ora figura come admin) alla lista degli utenti connessi al canale

            if (new_admin.getPrintWriter() != null) {   // viene segnala la promozione all'utente appena promosso
                new_admin.getPrintWriter().println("Now you're admin of the server!");
            }
        } else {
            admin.getPrintWriter().println(user.getUsername() + " is already an admin");
        }
    }

    /**
     * Invia un messaggio nel canale.
     * @param sender L'utente che invia il messaggio
     * @param message Il messaggio da inviare
     */
    public void sendMessage(User sender, String message) {
        // per tutti gli utenti
        for(Observer user : users) {
            if (user != sender) {   // se non è il mittende del messaggio
                user.update("[ ~ " + sender.getUsername() + "] : " +  message);
            } else {    // se invece è il mittende del messaggio
                sender.getPrintWriter().println("[ ~ me ] : " +  message);
            }
        }
    }

    /**
     * Verifica se un utente è stato bannato dal canale.
     * @param user L'utente da controllare
     * @return true se l'utente è stato bannato, altrimenti false
     */
    public boolean searchBannedUser (User user) {
        return bannedUsers.contains(user.getUsername());
    }
}
