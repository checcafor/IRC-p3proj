import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ChatClient {
    private static IRCChatApp ircChatApp;

    public ChatClient(IRCChatApp ircChatApp) {
        this.ircChatApp = ircChatApp;
    }
    public ChatClient() {       // costruttore di default per lo starting tramite terminale
        this.ircChatApp = null;
    }
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 12347);

            // legge da socket -> socket.getInputStream()
            BufferedReader serverReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // scrive su socket -> socket.getOutputStream()
            PrintWriter serverWriter = new PrintWriter(socket.getOutputStream(), true);

            /* section INSERIMENTO NOME UTENTE */

            // crea oggetto per leggere da input
            BufferedReader userInputReader = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Enter your username: ");
            // inserisce input letto in variabile username
            String username = userInputReader.readLine();
            // invia nome inserito al socket
            serverWriter.println(username);

            // creo thread che rimane in attesa di messaggi
            new Thread(() -> {
                try { // attesa
                    String serverMessage; // variabile che conterrà il messaggio in arrivo
                    while ((serverMessage = serverReader.readLine()) != null) { // attente mex
                        System.out.println(serverMessage);  // quando lo riceve lo stampa
                        if(ircChatApp != null){             // se l'interfaccia non è stata inizializzata non visualizza il messaggio sulla UI
                            ircChatApp.visualizzaMessaggio(serverMessage);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start(); // esegue in thread, lo fa partire


            String userInput;
            while ((userInput = userInputReader.readLine()) != null) {
                serverWriter.println(userInput);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}