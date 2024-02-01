package patterns.factoryPattern;

import java.util.List;

import patterns.observer.Observer;
import patterns.observer.User;

import patterns.strategy.Admin;

public interface Channel {
    public String getName();
    public void addUser(Observer user);
    public void removeUser(Observer user);
    public void notify(String message);
    public List<Observer> getUsers ();
    public void kickUser(Admin admin, User user);
    public void banUser(Admin admin, User user);
    public void unbanUser(Admin admin, User user);
    public void promote(Admin admin, User user);
    public void sendMessage(User sender, String message);
}
