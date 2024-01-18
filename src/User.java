import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class User implements Observer {
    private String username; // username univoco
    private ConcreteChannel currentChannel; // canale dove si trova l'utente
    private BaseCommandHandler invoker; // invocatore dei comandi di base ( comuni a user e admin )
    private PrintWriter printWriter; // oggetto PrintWriter che gestisce l'invio di messaggi al client
    // private BufferedReader bufferReader;

    public User(String username) { // costruttore
        this.username = username;
        currentChannel = null;
        invoker = new BaseCommandHandler();
    }

    public String getUsername () {
        return username;
    }

    public ConcreteChannel getCurrentChannel () {
        return currentChannel;
    }

    public void setPrintWriter (PrintWriter clientWriter) {
        this.printWriter = clientWriter;
    }

    /* public void setBufferReader (BufferedReader clientReader) {
        this.bufferReader = clientReader;
    } */

    public PrintWriter getPrintWriter() {
        return printWriter;
    }

    public void setCurrentChannel(ConcreteChannel currentChannel) {
        this.currentChannel = currentChannel;
    }

    /*
    public BufferedReader getBufferReader() {
        return bufferReader;
    } */

    public void joinChannel (String channel) {
        JoinChannelCommand join = new JoinChannelCommand(this, channel); // crea un'istanza del comando join
        invoker.setCommand(join); // imposta il comando di join come comando da eseguire
        invoker.handleCommand();  // esegue il comando join
    }

    public void leaveChannel () {
        LeaveChannelCommand leave = new LeaveChannelCommand(this); // crea un'istanza del comando leave
        invoker.setCommand(leave); // imposta il comando di leave come comando da eseguire
        invoker.handleCommand();  // esegue il comando leave
    }

    public void sendMessage (String message) {
        SendMexCommand sendmex = new SendMexCommand(this, message); // crea un'istanza del comando msg
        invoker.setCommand(sendmex); // imposta il comando di msg come comando da eseguire
        invoker.handleCommand();     // esegue il comando msg
    }

    public void userList () {
        UserListCommand userl = new UserListCommand(this); // crea un'istanza del comando users
        invoker.setCommand(userl); // imposta il comando di users come comando da eseguire
        invoker.handleCommand();   // esegue il comando users
    }

    public void channelList () {
        ListChannelsCommand list = new ListChannelsCommand(this); // crea un'istanza del comando list
        invoker.setCommand(list); // imposta il comando di list come comando da eseguire
        invoker.handleCommand();  // esegue il comando list
    }
    public void sendmexpriv (String message) {
        PrivateMessageCommand privmex = new PrivateMessageCommand(this, message); // crea un'istanza del comando privmsg
        invoker.setCommand(privmex); // imposta il comando di privmsg come comando da eseguire
        invoker.handleCommand();     // esegue il comando privmsg
    }

    @Override
    public void update(String message) {
        this.printWriter.println(message);
    }
}