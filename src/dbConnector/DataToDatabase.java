package dbConnector;    // package di appartenenza

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

/**
 * Classe che gestisce l'invio delle query di LOG al database
 * Prevede solamente il costruttore che invia la query al db.
 */
public class DataToDatabase {
    /**
     * Costruttore della classe DataToDatabase serve per inviare la query
     * per registrare il LOG, che sia connessione o disconnessione dal server
     * al database.
     *
     * @param message Il messaggio da inserire nel LOG del database (connessione o disconnessione).
     */
    public DataToDatabase(String message) {
        try {
            Connection connection = DatabaseConnector.getConnection();  // viene recuerato l'oggetto di tipo Connection per la connessione al databas

            String insertQuery = "INSERT INTO connections (data, messaggio) VALUES (?, ?)"; // generazione stringa da inviare al db

            // preparazione PreparedStatement per evitare SQL Injection
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                preparedStatement.setTimestamp(1, new java.sql.Timestamp(new Date().getTime()));    // set dell timestamp corrente come valore per il primo parametro.
                preparedStatement.setString(2, message);                                            // set del messaggio fornito come valore per il secondo parametro.

                preparedStatement.executeUpdate();  // esecuzione del PrearedStatement ed iserimento nel db
            }

            DatabaseConnector.closeConnection(connection);  // chiusura della connessione con il db
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
