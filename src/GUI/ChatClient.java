package GUI;    // package di appartenenza

import GUI.IRCChatApp;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * La classe ChatClient rappresenta un client per un'applicazione di chat IRC.
 * Fa da "client" di ricezione ed invio di dati al e dal server per interagire con l'interfaccia.
 *
 * @see IRCChatApp
 */
public class ChatClient {
    private static IRCChatApp ircChatApp; // riferimento all'istanza IRCChatApp per l'interazione con l'interfaccia utente (UI)
    private Socket socket; // socket per la comunicazione con il server
    private static BufferedReader serverReader; // lettore per ricevere i messaggi dal server
    private static PrintWriter serverWriter;    // scrittore per inviare i messaggi al server
    private Boolean isAdmin;                    // flag per determinare se l'utente è un amministratore

    /**
     * Costruttore per la classe ChatClient. Si occupa di istanziare la socket per la comunicazione con il server,
     * il reader ed il writer per scrivere su quest'ultima.
     *
     * @param ircChatApp Il riferimento all'istanza IRCChatApp per collegarla al client di recezione ed invio.
     */
    public ChatClient(IRCChatApp ircChatApp) {
        this.ircChatApp = ircChatApp;
        try {
            socket = new Socket("localhost", 12347);    // inizializzazione socket
            serverReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));  // inizializzazione lettore
            serverWriter = new PrintWriter(socket.getOutputStream(), true);            // inizializzazione scrittore
        } catch (IOException e) {
            throw new RuntimeException(e);  // lancio eccezione runtime se si verifica un errore durante l'inizializzazione
        }

    }

    /**
     * Gestisce l'inserimento di un nome utente durante il processo di login al server.
     *
     * @param username      Il nome utente inserito dall'utente.
     * @param frameLogin    Il frame che rappresenta la finestra di accesso.
     * @throws IOException  Se si verifica un errore di I/O durante la comunicazione con il server.
     */
    public void handleUsernameInput(String username, Component frameLogin) throws IOException {
        serverWriter.println(username);             // invia il nome utente iniziale al server
        String response = serverReader.readLine();  // viene letta la risposta dal server

        // se la risposta NON è uguale ad "OK"
        if (!response.equals("OK")) {
            // fin quando essa non sarà valida
            while (!response.equals("OK")) {
                // viene mostrato un messaggio all'utente tramite una finestra di dialogo riguardo al fatto che il nome utente già esiste
                JOptionPane.showMessageDialog(frameLogin, "Username is already taken. Please choose a different username:");

                // viene richiesto all'utente di inserire un nome utente diverso
                username = JOptionPane.showInputDialog(frameLogin, "Enter a different username:");

                serverWriter.println(username);     // reinvia il nome utente al server
                response = serverReader.readLine(); // legge la risposta ottenuta dal server
            }
        }
    }

    /**
     * Avvia un thread per ascoltare i messaggi in arrivo dal server.
     */
    public void startMessageListener() {
        new Thread(() -> {
            try {
                String serverMessage;
                // viene letto il messaggio dal server
                while ((serverMessage = serverReader.readLine()) != null) {
                    // se il messaggio inizia per "#" allora è un messaggio di controllo
                    if (serverMessage.startsWith("#")) {
                        // quindi viene controllato se l'utente è admin o meno e viene settata l'omonima variabile
                        isAdmin = Boolean.parseBoolean(serverMessage.substring(1));
                    } else if (ircChatApp != null) {    // altrimenti è un messaggio normale, quindi se è istanziata l'interfaccia
                        ircChatApp.visualizzaMessaggio(serverMessage);  // mostra il messaggio sull'interfaccia
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * Gestisce l'input dell'utente inviando il messaggio al server.
     *
     * @param message Il messaggio inserito dall'utente.
     */
    public void handleUserInput(String message) {
        serverWriter.println(message);
    }

    /**
     * Verifica se un utente è un amministratore in base al nome utente.
     *
     * @param username Il nome utente per verificare lo stato di amministratore.
     * @return True se l'utente è un amministratore, altrimenti false.
     */
    public synchronized boolean isAdmin(String username) {
        serverWriter.println("#" + username);   // invia richiesta di controlo per vedere se l'utente è admin o meno

        // fino a quando non è settata la variabile
        while (isAdmin == null) {
            try {
                Thread.sleep(50);   // metti n attesa il thread
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return isAdmin; // ritorna true o false
    }
}