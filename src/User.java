public class User implements Observer {
    private String username;
    private ConcreteChannel currentChannel;

    public User(String username) {
        currentChannel = null;
        this.username = username;
    }

    public String getUsername () {
        return username;
    }

    public void joinChannel (ConcreteChannel channel) {
        if (currentChannel != null) {
            currentChannel.removeUser(this);
        }

        currentChannel = channel;
        channel.addUser(this);
    }

    public void sendMessage (String message) {
        currentChannel.broadcastMessage(this, message); // da cambiare
    }

    public void sendPrivateMessage(User sender, String message) {
        System.out.println("private message from " + sender.getUsername() + ": " + message);
    }

    @Override
    public void update(String message) {
        System.out.println("[" + currentChannel.getName() + "] " + username + ": " + message );
    }
}