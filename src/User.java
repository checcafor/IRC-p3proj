public class User implements Observer {
    private String username;
    private ConcreteChannel currentChannel;
    private BaseCommandHandler invoker;

    public User(String username) {
        this.username = username;
        currentChannel = null;
    }

    public String getUsername () {
        return username;
    }

    /*
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
    */
    public void sendPrivateMessage(User sender, String message) {
        System.out.println("private message from " + sender.getUsername() + ": " + message);
    }
    public void joinChannel (ConcreteChannel channel) {
        JoinChannelCommand join = new JoinChannelCommand(Server.getInstance(), this, channel);
        invoker.setCommand(join);
        invoker.handleCommand();
    }

    public void sendMessage (String message) {
        SendMexCommand sendmex = new SendMexCommand(currentChannel, message);
        invoker.setCommand(sendmex);
        invoker.handleCommand();
    }

    public void userList () {
        UserListCommand userl = new UserListCommand(currentChannel, this);
        invoker.setCommand(userl);
        invoker.handleCommand();
    }

    public void channelList () {
        ListChannelsCommand list = new ListChannelsCommand(Server.getInstance(), this);
        invoker.setCommand(list);
        invoker.handleCommand();
    }
    public void sendmexpriv (String name, String message) {
        PrivateMessageCommand privmex = new PrivateMessageCommand(this, name, message);
        invoker.setCommand(privmex);
        invoker.handleCommand();
    }

    @Override
    public void update(String message) {
        System.out.println("[" + currentChannel.getName() + "] " + username + ": " + message );
    }
}