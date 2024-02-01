package patterns.strategy;

import patterns.factoryPattern.Channel;

import patterns.observer.User;

import patterns.singleton.Server;

public class UnbanAction implements AdminActionStrategy {
    private Channel channel;
    private Admin admin;
    private User target;

    public UnbanAction(Admin admin, String nameTarget) {
        this.channel = admin.getCurrentChannel();
        this.admin = admin;
        this.target = Server.getInstance().getUserByName(nameTarget);
    }

    @Override
    public void performAction() {
        if (admin.getCurrentChannel() == null) {
            admin.getPrintWriter().println("Non sei in un canale");
        } else if (target != null) {
            channel.unbanUser(admin, target); // esecuzione del comando mediante chiamata a funzione
        } else {
            admin.getPrintWriter().println("User Not Found");
        }
    }
}
