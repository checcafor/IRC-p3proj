package patterns.strategy;

import patterns.factoryPattern.Channel;

import patterns.observer.User;

import patterns.singleton.Server;

public class PromoteAction implements AdminActionStrategy {
    private Channel channel;
    private Admin admin;
    private User target;

    public PromoteAction(Admin admin, String nameTarger) {
        this.channel = admin.getCurrentChannel();
        this.admin = admin;
        this.target = Server.getInstance().getUserByName(nameTarger);
    }

    @Override
    public void performAction() {
        if (channel != null) {
            if (target.getCurrentChannel() != null && target.getCurrentChannel() == channel) {
                channel.promote(admin, target); // esecuzione del comando mediante chiamata a funzione
            } else if (target.getCurrentChannel() != null && target.getCurrentChannel() != channel) {
                admin.getPrintWriter().println( target.getUsername() + " is not in this channel");
            } else if (target.getCurrentChannel() == null){
                admin.getPrintWriter().println( target.getUsername() + " is not in a channel");
            }
        } else {
            admin.getPrintWriter().println( " you're not in a channel");
        }
    }
}
