package dbConnector;    // package di appartenenza

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe "Wrapper" che gestisce la connessione al DataBase.
 * Fornisce metodi per ottenere una connessione e chiuderla.
 */
public class DatabaseConnector {
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/logdb";  // variabile istanza contenente l'indirizzo a cui è dispiegato il database
    private static final String USERNAME = "root";                          // variabile istanza contenente l'username dell'utente con cui si sta facendo accesso al database
    private static final String PASSWORD = "parthenope15";                  // variabile istanza contenente la password dell'utente con cui si sta facendo accesso al database

    /**
     * Metodo statico per inizializzare la connessione al database. In particolare
     * viene utilizzato il metodo della classe Singleton "DriverManager" per
     * tentare di stabilire una connessione al database utilizzando l’URL del
     * database fornito. Viene selezionato il driver appropriato dall’insieme
     * di driver JDBC registrati.
     *
     * @return L'oggetto Connection per il database.
     * @throws SQLException Se si verifica un errore durante la connessione.
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);    // invocazione metodo su classe Singleton per ottenere l'unica istanza della connessione
    }

    /**
     * Chiude la connessione al database. In particolare
     * se la connessione passata come paramentro è diverso da "null"
     * allora significa che è stata precedentemente istaurata e che
     * quindi è possibile chiuderla. Successivamente si invoca il
     * metodo "close()" sull'oggetto connection.
     *
     * @param connection L'oggetto Connection da chiudere.
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {       // se la connessione è stata precedentemente istanziata (quindi diversa da null)
            try {
                connection.close();     // viene chiusa la connessione
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
