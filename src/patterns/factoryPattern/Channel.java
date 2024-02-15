package patterns.factoryPattern;

import java.util.List;

import patterns.observer.Observer;
import patterns.observer.User;

import patterns.strategy.Admin;

/**
 * Questa interfaccia definisce le operazioni di base che un canale deve supportare.
 * Un canale è una struttura che permette agli utenti di comunicare tra loro.
 */
public interface Channel {
    /**
     * Restituisce il nome del canale.
     * @return Il nome del canale
     */
    public String getName();
    /**
     * Aggiunge un nuovo utente al canale.
     * @param user L'utente da aggiungere al canale
     */
    public void addUser(Observer user);
    /**
     * Rimuove un utente dal canale.
     * @param user L'utente da rimuovere dal canale
     */
    public void removeUser(Observer user);
    /**
     * Notifica a tutti gli utenti del canale un determinato messaggio.
     * @param message Il messaggio da notificare
     */
    public void notify(String message);
    /**
     * Restituisce la lista degli utenti attualmente nel canale.
     * @return La lista degli utenti nel canale
     */
    public List<Observer> getUsers ();
    /**
     * Espelle un utente dal canale.
     * @param admin L'amministratore che esegue l'operazione
     * @param user L'utente da espellere dal canale
     */
    public void kickUser(Admin admin, User user);
    /**
     * Banna un utente dal canale.
     * @param admin L'amministratore che esegue l'operazione
     * @param user L'utente da bannare dal canale
     */
    public void banUser(Admin admin, User user);
    /**
     * Rimuove il ban di un utente dal canale.
     * @param admin L'amministratore che esegue l'operazione
     * @param user L'utente da sbannare dal canale
     */
    public void unbanUser(Admin admin, User user);
    /**
     * Promuove un utente a un ruolo superiore all'interno del canale.
     * @param admin L'amministratore che esegue l'operazione
     * @param user L'utente da promuovere
     */
    public void promote(Admin admin, User user);
    /**
     * Invia un messaggio nel canale.
     * @param sender L'utente che invia il messaggio
     * @param message Il messaggio da inviare
     */
    public void sendMessage(User sender, String message);
}
