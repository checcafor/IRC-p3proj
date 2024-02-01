import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ChatClient {
    private static IRCChatApp ircChatApp;
    private Socket socket;
    private static BufferedReader serverReader;
    private static PrintWriter serverWriter;
    private Boolean isAdmin;

    public ChatClient(IRCChatApp ircChatApp) {
        this.ircChatApp = ircChatApp;
        try {
            socket = new Socket("localhost", 12347);
            serverReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            serverWriter = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void handleUsernameInput(String username, Component frameLogin) throws IOException {
        serverWriter.println(username);
        String response = serverReader.readLine();

        if (!response.equals("OK")) {
            while (!response.equals("OK")) {
                // display a message to the user that the username is not unique
                JOptionPane.showMessageDialog(frameLogin, "Username is already taken. Please choose a different username:");

                username = JOptionPane.showInputDialog(frameLogin, "Enter a different username:");
                // clear the input field on the client side
                // send the new username to the server
                serverWriter.println(username);

                // read the response from the server
                response = serverReader.readLine();
            }
        }
    }

    public void startMessageListener() {
        new Thread(() -> {
            try {
                String serverMessage;
                while ((serverMessage = serverReader.readLine()) != null) {
                    if (serverMessage.startsWith("#")) {
                        isAdmin = Boolean.parseBoolean(serverMessage.substring(1));
                    } else if (ircChatApp != null) {
                        ircChatApp.visualizzaMessaggio(serverMessage);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void handleUserInput(String message) {
        serverWriter.println(message);
    }

    public synchronized boolean isAdmin(String username) {
        serverWriter.println("#" + username);

        while (isAdmin == null) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return isAdmin;
    }
}