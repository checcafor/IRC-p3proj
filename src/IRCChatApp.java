import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

public class IRCChatApp extends JFrame {
    private JTextArea chatArea; // messaggi della chat
    private JTextField inputField; // dove client inserisce messaggi
    private Socket clientSocket;
    private PrintWriter serverWriter;
    private BufferedReader serverReader;
    private final Server server = Server.getInstance();

    public IRCChatApp() {
        avviaPaginaLogin();
    }

    private void avviaPaginaLogin() {
        JFrame frameLogin = new JFrame("Login"); // finestra di login
        frameLogin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // se utente chiude termina processo
        frameLogin.setSize(300, 150); // dimensioni della finestra di login
        frameLogin.setLocationRelativeTo(null); // finestra impostata al centro dello schermo

        JPanel panelLogin = new JPanel(); // creazione pannello
        panelLogin.setLayout(new GridLayout(3, 2)); // griglia nel pannello - layout

        // etichette per pagina login
        JLabel labelNome = new JLabel("Nome:");
        JTextField campoNome = new JTextField();
        JButton loginButton = new JButton("Login");
        ChatClient chatClient = new ChatClient(this);

        loginButton.addActionListener(e -> {
            String nomeUtente = campoNome.getText(); // prende nome inserito nel campo per input
            if (!nomeUtente.isEmpty()) { // se la variabile non è vuota
                try {
                    clientSocket = new Socket("localhost", 12347);
                    serverWriter = new PrintWriter(clientSocket.getOutputStream(), true);
                    serverReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    serverWriter.println(nomeUtente);

                    // check if the server approves the username
                    String response = serverReader.readLine();

                    if (!response.equals("OK")) {
                        while (!response.equals("OK")) {
                            // Display a message to the user that the username is not unique
                            JOptionPane.showMessageDialog(frameLogin, "Username is already taken. Please choose a different username:");

                            nomeUtente = JOptionPane.showInputDialog(frameLogin, "Enter a different username:");
                            // Clear the input field on the client side
                            // Send the new username to the server
                            serverWriter.println(nomeUtente);

                            // Read the response from the server
                            response = serverReader.readLine();
                        }
                    }

                    frameLogin.dispose(); // close the login page
                    inizializzaApp(nomeUtente); // open the main chat window

                    new Thread(() -> {
                        try {
                            String serverMessage;
                            while ((serverMessage = serverReader.readLine()) != null) {
                                chatArea.append(serverMessage + "\n");
                            }
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }).start();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        // aggiunta variabili nel pannello
        panelLogin.add(labelNome);
        panelLogin.add(campoNome);
        panelLogin.add(loginButton);

        frameLogin.add(panelLogin);
        frameLogin.setVisible(true);
    }

    private void inizializzaApp(String nomeUtente) {
        setTitle("IRC Chat - " + nomeUtente); // titolo della finestra principale
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        chatArea = new JTextArea(); // area testo per chat
        chatArea.setEditable(false); // lo rende non-editabile
        JScrollPane scrollPane = new JScrollPane(chatArea); // scroll per area testo
        inputField = new JTextField(); // input per invio messaggi
        JButton sendButton = new JButton("Invia"); // tasto per inviare messaggi

        // ascoltatore sul tasto
        sendButton.addActionListener(e -> {
            inviaMessaggio(); // richiama funzione invio messaggio al server
        });

        inputField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                // Ignora gli eventi keyTyped
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    // Se il tasto premuto è "Invio", invia il messaggio
                    inviaMessaggio();
                }
                if (e.getKeyChar() == '/') {
                    // Mostra l'elenco dei comandi come un menu popup
                    EventQueue.invokeLater(() -> mostraMenuComandi(nomeUtente));
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // Ignora gli eventi keyReleased
            }
        });

        setLayout(new BorderLayout()); // layout per finestra
        add(scrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(); // crea nuovo pannello per input
        inputPanel.setLayout(new BorderLayout()); // layout per sezione invio mex
        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        add(inputPanel, BorderLayout.SOUTH); // pannello per input al panello globale

        setVisible(true);
    }

    private void inviaMessaggio() {
        String messaggio = inputField.getText();

        if (!messaggio.isEmpty()) {
            serverWriter.println(messaggio);
            inputField.setText("");
        }
    }

    private void mostraMenuComandi(String username) {
        JPopupMenu menuComandi = new JPopupMenu();
        ArrayList<String> comandi = new ArrayList<>(Arrays.asList("/join #", "/list", "/users", "/leave", "/msg", "/privmsg"));

        if (server.getAdministrators().contains(username)) {
            comandi.add("/ban");
            comandi.add("/unban");
            comandi.add("/kick");
            comandi.add("/promote");
        }

        for (String comando : comandi) {
            JMenuItem menuItem = new JMenuItem(comando);
            menuItem.addActionListener(e -> {
                // Azione da eseguire quando un comando viene selezionato
                String comandoSelezionato = ((JMenuItem) e.getSource()).getText();
                inputField.setText(comandoSelezionato);
                inputField.requestFocus();
            });
            menuComandi.add(menuItem);
        }

        // Imposta la larghezza del popup in base alla larghezza dell'inputField
        int popupWidth = inputField.getWidth() - 10; // Regolazione della larghezza del popup
        menuComandi.setPreferredSize(new Dimension(popupWidth, menuComandi.getPreferredSize().height));

        // Posiziona il menu sopra l'inputField
        Point posizione = inputField.getLocationOnScreen();
        menuComandi.show(inputField, 10, -menuComandi.getPreferredSize().height);
    }
    public void visualizzaMessaggio(String messaggio) {
        chatArea.append(messaggio + "\n");
    }

    public static void main(String[] args) {
        // viene utilizzato per garantire che la creazione dell'interfaccia grafica Swing avvenga nel thread di interfaccia utente
        SwingUtilities.invokeLater(IRCChatApp::new);
    }
}