package GUI;     // package di appartenenza

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.border.AbstractBorder;
import patterns.singleton.Server;

/**
 * La classe IRCChatApp rappresenta un'applicazione client per la chat IRC.
 * Risulta essere l'effettiva interfacia grafica utilizzata dall'utente che interagisce con la classe ChatClient.
 *
 * @see ChatClient
 */
public class IRCChatApp extends JFrame {
    private JTextArea chatArea; // area messaggi della chat
    private JTextField inputField; // campo di input per permettere al client di inserire messaggi
    private final Server server = Server.getInstance(); // istanza del server (singleton)
    ChatClient chatClient;  // reference al client per invio e ricezione messaggi tramite socket

    /**
     * Costruttore per la classe IRCChatApp.
     * Appena avviato viene mostrata una pagina per il login al sistema
     */
    public IRCChatApp() {
        avviaPaginaLogin();     // chiamata a funzione per avviare interfaccia di login
        chatClient = new ChatClient(this);  // istanziazione chatClient per comunicazione con server tramite socket
    }

    /**
     * Funzione chiamata nel costruttore che avvia la pagina di login.
     */
    private void avviaPaginaLogin() {
        JFrame frameLogin = new JFrame("Login");                // finestra di login
        frameLogin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // se utente chiude termina processo
        frameLogin.setSize(300, 150);                   // dimensioni della finestra di login
        frameLogin.setLocationRelativeTo(null);                     // finestra impostata al centro dello schermo

        JPanel panelLogin = new JPanel();                           // creazione pannello
        panelLogin.setLayout(new GridLayout(3, 2));       // griglia nel pannello - layout
        panelLogin.setBackground(new Color(173, 216, 230)); // colore dello sfondo della finestra


        // etichette per pagina login
        JLabel labelNome = new JLabel("Enter Your Name");           // inizializzazione la lable
        labelNome.setHorizontalAlignment(JLabel.CENTER);                // la posiziona al centro
        labelNome.setForeground(Color.WHITE);                           // cambia il colore del testo in bianco
        labelNome.setFont(new Font("Arial", Font.BOLD, 14));  // imposta il grassetto

        JTextField campoNome = new JTextField();                        // inizializzazione il campo per iserire il nome utente
        JButton loginButton = new JButton("Login");                 // inizializzazione bottone per il login
        campoNome.setBorder(new RoundBorder());                         // arrotondamento dei bordi
        campoNome.setOpaque(true);
        campoNome.setBackground(new Color(173, 216, 230));      // viene impostato il colore dello sfondo

        loginButton.addActionListener(e -> {
            String nomeUtente = campoNome.getText();    // prende nome inserito nel campo per input
            if (!nomeUtente.isEmpty()) {                // se la variabile non è vuota
                try {
                    chatClient.handleUsernameInput(nomeUtente, frameLogin); // invio comandi al server tramite l'istanza della chatClient

                    frameLogin.dispose();       // chiusura della pagina di login
                    inizializzaApp(nomeUtente); // chiamata a funzione per inizializzare la finestra principale

                    chatClient.startMessageListener();  // avvia il listener (thread per l'ascoloto dei messaggi)
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        // aggiunta variabili (componenti) nel pannello
        panelLogin.add(labelNome);
        panelLogin.add(campoNome);
        panelLogin.add(loginButton);

        frameLogin.add(panelLogin);
        frameLogin.setVisible(true);    // viene impostata la piena visibilità del frame
    }

    /**
     * Inizializza l'applicazione principale della chat.
     * Consiste nel caricare l'interfaccia utente realtiva all'applicazione stessa
     * quindi contenente un'area per inserire i messaggi ed una per leggerli.
     *
     * @param nomeUtente Il nome utente dell'utente.
     */
    private void inizializzaApp(String nomeUtente) {
        setTitle("IRC Chat - " + nomeUtente);               // impostazione titolo della finestra principale con il nome utente
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);     // definizione comportamento pressione tasto "chiudi"
        setSize(400, 300);                      // definizione dimensione finestra
        setLocationRelativeTo(null);                        // centra la finestra al centro dello schermo

        chatArea = new JTextArea();                                 // inizializzazione area testo per chat
        chatArea.setLayout(new BorderLayout());                     // set del layout
        chatArea.setBackground(new Color(173, 216, 230));   // impostazione colore di sfondo della chat

        chatArea.setEditable(false);                        // rende la text-area non editabile e di sola lettura
        JScrollPane scrollPane = new JScrollPane(chatArea); // abilita lo scroll per l'area testo
        inputField = new JTextField();                      // inizializzazione input per invio messaggi
        JButton sendButton = new JButton("Invia");      // inizializzazione tasto per inviare messaggi

        // ascoltatore sul tasto per inviare i messaggi al server
        sendButton.addActionListener(e -> {
            inviaMessaggio(); // richiama funzione invio messaggio al server
        });

        // aggiunge un KeyListener per implementare funzioalità come il
        // suggerimento dei comandi e l'invio premendo il tasto "INVIO"
        inputField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    // se il tasto premuto è "Invio", invia il messaggio
                    inviaMessaggio();
                }
                // se è digitato il carattere "/"
                if (e.getKeyChar() == '/') {
                    // mostra l'elenco dei comandi come un menu popup
                    EventQueue.invokeLater(() -> mostraMenuComandi(nomeUtente));
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });

        setLayout(new BorderLayout());                      // definizione layout per finestra
        add(scrollPane, BorderLayout.CENTER);               // aggiunta scrollbar alla finestra

        JPanel inputPanel = new JPanel();                   // inizializzazione nuovo pannello per input
        inputPanel.setLayout(new BorderLayout());           // definizione del layout per sezione invio messaggi
        inputPanel.add(inputField, BorderLayout.CENTER);    // aggiunta input field alla finestra
        inputPanel.add(sendButton, BorderLayout.EAST);      // agiunta bottone per invio alla finestra

        add(inputPanel, BorderLayout.SOUTH);                // aggiunta del pannello per input al panello globale

        setVisible(true);   // rende visibile il pannello
    }

    /**
     * Funzione per inviare il messaggio al server.
     * In particolare utilizza l'istanza di chatCLient per inviarla.
     */
    private void inviaMessaggio() {
        String messaggio = inputField.getText();        // recuper del messaggio inserito

        if (!messaggio.isEmpty()) {                     // se il messaggio non è vuoto
            chatClient.handleUserInput(messaggio);      // richiama il metodo per inviare il messaggio al server
            inputField.setText("");                     // svuota il campo di testo
        }
    }

    /**
     * Funzione che mostra il menu dei comandi come un menu popup.
     * Fornisce un suggerimento alla pressione del comando "/" in modo
     * tale che l'utente sa quali comandi è in grado di utilizzare
     * in base al ruolo che ricopre
     *
     * @param username Il nome utente dell'utente.
     */
    private void mostraMenuComandi(String username) {
        JPopupMenu menuComandi = new JPopupMenu();  // viene inizializzato un menu a popup
        // lista dei comandi di base comune a tutti i tipi di utente
        ArrayList<String> comandi = new ArrayList<>(Arrays.asList("/join #", "/list", "/users", "/leave", "/msg", "/privmsg"));

        // viene recuperato in una variabile booleana se l'utente è admin oppure no
        Boolean condition = chatClient.isAdmin(username);
        // se l'utente risulta essere
        if (condition) {
            // vengono concatenati e qundi mostrati i comandi aggiuntivi che può fare
            comandi.add("/ban");
            comandi.add("/unban");
            comandi.add("/kick");
            comandi.add("/promote");
        }

        // per ogni comando nella lista dei comandi
        for (String comando : comandi) {
            JMenuItem menuItem = new JMenuItem(comando);    // viene istanziato un elemento per il menu
            menuItem.addActionListener(e -> {
                // azione da eseguire quando un comando viene selezionato
                String comandoSelezionato = ((JMenuItem) e.getSource()).getText();  // viene recuperato il testo del menu selezionato
                inputField.setText(comandoSelezionato);                             // imposta il testo del suggerimento
                inputField.requestFocus();
            });
            menuComandi.add(menuItem);  // aggiunge il comando al menu
        }

        // imposta la larghezza del popup
        int popupWidth = inputField.getWidth() - 10; // regolazione della larghezza del popup
        menuComandi.setPreferredSize(new Dimension(popupWidth, menuComandi.getPreferredSize().height));

        // posiziona il menu sopra l'inputField
        Point posizione = inputField.getLocationOnScreen();
        menuComandi.show(inputField, 10, -menuComandi.getPreferredSize().height);
    }

    /**
     * Visualizza un messaggio nella chat.
     *
     * @param messaggio Il messaggio da visualizzare.
     */
    public void visualizzaMessaggio(String messaggio) {
        // appende il messaggio all'area di chat
        chatArea.append(messaggio + "\n");
    }

    /**
     * Classe interna per la creazione di un bordo arrotondato.
     */
    static class RoundBorder extends AbstractBorder {
        private static final int RADIUS = 10;
        private static final Color BORDER_COLOR = Color.WHITE;

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            g.setColor(BORDER_COLOR);
            g.drawRoundRect(x, y, width - 1, height - 1, RADIUS, RADIUS);
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(4, 8, 4, 8);
        }
    }

    /**
     * Metodo principale per l'esecuzione dell'applicazione.
     *
     * @param args Argomenti della riga di comando (non utilizzato).
     */
    public static void main(String[] args) {
        // viene utilizzato per garantire che la creazione dell'interfaccia grafica Swing avvenga nel thread di interfaccia utente
        SwingUtilities.invokeLater(IRCChatApp::new);
    }
}