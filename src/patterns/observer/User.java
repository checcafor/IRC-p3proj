package patterns.observer;

import patterns.command.*;
import patterns.singleton.Server;

import java.io.PrintWriter;

/**
 * Questa classe rappresenta un utente nel sistema di messaggistica.
 * Gli utenti possono inviare e ricevere messaggi, unirsi e lasciare canali, e eseguire varie operazioni.
 */
public class User implements Observer {
    private String username; // username univoco
    private ConcreteChannel currentChannel; // canale dove si trova l'utente
    private BaseCommandHandler invoker; // invocatore dei comandi di base ( comuni a user e admin )
    private PrintWriter printWriter; // oggetto PrintWriter che gestisce l'invio di messaggi al client
    // private BufferedReader bufferReader;

    /**
     * Costruttore della classe che inizializza un nuovo utente con il nome specificato.
     * @param username Il nome univoco dell'utente
     */
    public User(String username) {
        this.username = username;
        currentChannel = null;
        invoker = new BaseCommandHandler();
    }

    /**
     * Costruttore di copia che crea un nuovo utente copiando i dati da un altro utente.
     * @param user L'utente da copiare
     */
    public User(User user){
        this.username = user.getUsername();
        this.currentChannel = user.getCurrentChannel();
        this.printWriter = user.getPrintWriter();
        this.invoker = user.getInvoker();
    }

    /**
     * Restituisce il nome univoco dell'utente.
     * @return Il nome univoco dell'utente
     */
    public String getUsername () {
        return username;
    }

    /**
     * Restituisce il canale attuale in cui si trova l'utente.
     * @return Il canale attuale dell'utente
     */
    public ConcreteChannel getCurrentChannel () {
        return currentChannel;
    }

    /**
     * Imposta il PrintWriter per l'utente per inviare messaggi al client.
     * @param clientWriter L'oggetto PrintWriter per inviare messaggi al client
     */
    public void setPrintWriter (PrintWriter clientWriter) {
        this.printWriter = clientWriter;
    }

    /**
     * Restituisce il PrintWriter associato all'utente.
     * @return L'oggetto PrintWriter associato all'utente
     */
    public PrintWriter getPrintWriter() {
        return printWriter;
    }

    /**
     * Restituisce il gestore dei comandi associato all'utente.
     * Il gestore dei comandi viene utilizzato per eseguire i comandi generici dell'utente.
     * @return Il gestore dei comandi associato all'utente
     */
    public BaseCommandHandler getInvoker(){return invoker;}

    /**
     * Imposta il canale attuale dell'utente.
     * @param currentChannel Il canale attuale dell'utente
     */
    public void setCurrentChannel(ConcreteChannel currentChannel) {
        this.currentChannel = currentChannel;
    }

    // Metodi per gestire i comandi generici dell'utente
    /**
     * Gestisce i comandi generici inviati dall'utente.
     * @param clientMessage Il messaggio inviato dall'utente
     */
    public void handleGeneralCommands(String clientMessage) {
        // se il messaggio inizia per / significa che l'utente vuole eseguire un comando
        if(clientMessage.startsWith("/")){
            if (clientMessage.startsWith("/join #")) { // se il comando inizia per "/join #"
                joinChannel(clientMessage.substring(7));    // viene eseguito il comando per entrare in un canale
            } else if (clientMessage.equals("/leave")) { // se il comando inizia per "/leave"
                leaveChannel(); // viene eseguito il comando per uscire da un canale
            } else if (clientMessage.startsWith("/msg ")) { // se il comando inizia per "/msg "
                sendMessage(clientMessage.substring(5)); // viene eseguito il comando per inviare un messaggio nel canale corrente
            } else if (clientMessage.equals("/list")) { // se il comando inizia per "/list"
                channelList(); // viene eseguito il comando per ottenere la lista dei canali attivi nel server
            } else if (clientMessage.equals("/users")) { // se il comando inizia per "/users"
                userList(); // viene eseguito il comando per ottenere la lista di utenti nel canale corrente
            } else if (clientMessage.startsWith("/privmsg ")) { // se il comando inizia per "/privmsg "
                sendmexpriv(clientMessage.substring(9)); // viene eseguito il comando per inviare un messaggio in privato ad un utente
            } else {
                this.getPrintWriter().println("insert a valid command");
            }
        } else if (clientMessage.startsWith("#")) {
            // messaggi controllo su server
        } else {
            // altrimenti il messaggio inviato dall'utente non è un comando noto
            printWriter.println("insert '/' to start using commands");
        }
    }

    /**
     * Unisce l'utente al canale specificato.
     * @param channel Il nome del canale a cui unirsi
     */
    public void joinChannel (String channel) {
        User user = Server.getInstance().getUserByName(this.getUsername()); // viene recuperato l'utente / admin che vuole eseguire il comando
        JoinChannelCommand join = new JoinChannelCommand(user, channel);    // crea un'istanza del comando join
        invoker.setCommand(join); // imposta il comando di join come comando da eseguire
        invoker.handleCommand();  // esegue il comando join
    }

    /**
     * Abbandona il canale attuale.
     */
    public void leaveChannel () {
        User user = Server.getInstance().getUserByName(this.getUsername()); // viene recuperato l'utente / admin che vuole eseguire il comando
        LeaveChannelCommand leave = new LeaveChannelCommand(user); // crea un'istanza del comando leave
        invoker.setCommand(leave); // imposta il comando di leave come comando da eseguire
        invoker.handleCommand();  // esegue il comando leave
    }

    /**
     * Invia un messaggio nel canale attuale.
     * @param message Il messaggio da inviare
     */
    public void sendMessage (String message) {
        User user = Server.getInstance().getUserByName(this.getUsername()); // viene recuperato l'utente / admin che vuole eseguire il comando
        SendMexCommand sendmex = new SendMexCommand(user, message); // crea un'istanza del comando msg
        invoker.setCommand(sendmex); // imposta il comando di msg come comando da eseguire
        invoker.handleCommand();     // esegue il comando msg
    }

    /**
     * Ottiene l'elenco degli utenti nel canale attuale.
     */
    public void userList () {
        User user = Server.getInstance().getUserByName(this.getUsername()); // viene recuperato l'utente / admin che vuole eseguire il comando
        UserListCommand userl = new UserListCommand(user); // crea un'istanza del comando users
        invoker.setCommand(userl); // imposta il comando di users come comando da eseguire
        invoker.handleCommand();   // esegue il comando users
    }

    /**
     * Ottiene l'elenco dei canali attivi nel server.
     */
    public void channelList () {
        User user = Server.getInstance().getUserByName(this.getUsername()); // viene recuperato l'utente / admin che vuole eseguire il comando
        ListChannelsCommand list = new ListChannelsCommand(user); // crea un'istanza del comando list
        invoker.setCommand(list); // imposta il comando di list come comando da eseguire
        invoker.handleCommand();  // esegue il comando list
    }

    /**
     * Invia un messaggio privato a un altro utente.
     * @param message Il messaggio privato da inviare
     */
    public void sendmexpriv (String message) {
        User user = Server.getInstance().getUserByName(this.getUsername()); // viene recuperato l'utente / admin che vuole eseguire il comando
        PrivateMessageCommand privmex = new PrivateMessageCommand(user, message); // crea un'istanza del comando privmsg
        invoker.setCommand(privmex); // imposta il comando di privmsg come comando da eseguire
        invoker.handleCommand();     // esegue il comando privmsg
    }

    /**
     * Metodo richiamato quando l'utente riceve un aggiornamento.
     * Aggiorna il client dell'utente con il messaggio ricevuto.
     * @param message Il messaggio ricevuto
     */
    @Override
    public void update(String message) {
        this.printWriter.println(message);
    }
}