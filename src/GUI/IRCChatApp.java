package GUI;

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

public class IRCChatApp extends JFrame {
    private JTextArea chatArea; // messaggi della chat
    private JTextField inputField; // dove client inserisce messaggi
    private final Server server = Server.getInstance();
    ChatClient chatClient;

    public IRCChatApp() {
        avviaPaginaLogin();
        chatClient = new ChatClient(this);
    }

    private void avviaPaginaLogin() {
        JFrame frameLogin = new JFrame("Login"); // finestra di login
        frameLogin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // se utente chiude termina processo
        frameLogin.setSize(300, 150); // dimensioni della finestra di login
        frameLogin.setLocationRelativeTo(null); // finestra impostata al centro dello schermo

        JPanel panelLogin = new JPanel(); // creazione pannello
        panelLogin.setLayout(new GridLayout(3, 2)); // griglia nel pannello - layout
        panelLogin.setBackground(new Color(173, 216, 230));


        // etichette per pagina login
        JLabel labelNome = new JLabel("Enter Your Name");
        labelNome.setHorizontalAlignment(JLabel.CENTER);
        labelNome.setForeground(Color.WHITE);
        labelNome.setFont(new Font("Arial", Font.BOLD, 14));

        JTextField campoNome = new JTextField();
        JButton loginButton = new JButton("Login");
        campoNome.setBorder(new RoundBorder());
        campoNome.setOpaque(true);
        campoNome.setBackground(new Color(173, 216, 230));

        loginButton.addActionListener(e -> {
            String nomeUtente = campoNome.getText(); // prende nome inserito nel campo per input
            if (!nomeUtente.isEmpty()) { // se la variabile non è vuota
                try {
                    chatClient.handleUsernameInput(nomeUtente, frameLogin);

                    frameLogin.dispose(); // close the login page
                    inizializzaApp(nomeUtente); // open the main chat window

                    chatClient.startMessageListener();
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
        chatArea.setLayout(new BorderLayout());
        chatArea.setBackground(new Color(173, 216, 230));

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
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    // se il tasto premuto è "Invio", invia il messaggio
                    inviaMessaggio();
                }
                if (e.getKeyChar() == '/') {
                    // mostra l'elenco dei comandi come un menu popup
                    EventQueue.invokeLater(() -> mostraMenuComandi(nomeUtente));
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
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
            chatClient.handleUserInput(messaggio);
            inputField.setText("");
        }
    }

    private void mostraMenuComandi(String username) {
        JPopupMenu menuComandi = new JPopupMenu();
        ArrayList<String> comandi = new ArrayList<>(Arrays.asList("/join #", "/list", "/users", "/leave", "/msg", "/privmsg"));

        Boolean condition = chatClient.isAdmin(username);
        if (condition) {
            comandi.add("/ban");
            comandi.add("/unban");
            comandi.add("/kick");
            comandi.add("/promote");
        }

        for (String comando : comandi) {
            JMenuItem menuItem = new JMenuItem(comando);
            menuItem.addActionListener(e -> {
                // azione da eseguire quando un comando viene selezionato
                String comandoSelezionato = ((JMenuItem) e.getSource()).getText();
                inputField.setText(comandoSelezionato);
                inputField.requestFocus();
            });
            menuComandi.add(menuItem);
        }

        // imposta la larghezza del popup
        int popupWidth = inputField.getWidth() - 10; // Regolazione della larghezza del popup
        menuComandi.setPreferredSize(new Dimension(popupWidth, menuComandi.getPreferredSize().height));

        // Posiziona il menu sopra l'inputField
        Point posizione = inputField.getLocationOnScreen();
        menuComandi.show(inputField, 10, -menuComandi.getPreferredSize().height);
    }

    public void visualizzaMessaggio(String messaggio) {
        chatArea.append(messaggio + "\n");
    }

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

    public static void main(String[] args) {
        // viene utilizzato per garantire che la creazione dell'interfaccia grafica Swing avvenga nel thread di interfaccia utente
        SwingUtilities.invokeLater(IRCChatApp::new);
    }
}