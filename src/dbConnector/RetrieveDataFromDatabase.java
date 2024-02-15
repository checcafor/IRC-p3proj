package dbConnector;    // package di appartenenza

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 * Classe che gestisce il recupero del log più recente con quel messaggio dal database
 * Prevede solamente il costruttore che invia la query al db per richiedere l'ultimo log.
 */
public class RetrieveDataFromDatabase {
    private Date retrievedDate = null;  // oggetto di tipo Date per memorizzare la data recuperata

    /**
     * Costruttore della classe RetrieveDataFromDatabase. Responsabile della connessione al db,
     * dell'invio della query per recuperare il LOG più recente e di memorizzazione della data del LOG stesso
     *
     * @param mess Il messaggio da utilizzare nella query per recuperare l'ultimo LOG.
     */
    public RetrieveDataFromDatabase(String mess) {
        try {
            Connection connection = DatabaseConnector.getConnection();  // recupera l'oggetto di tipo Connection per la connessione al database
            String selectQuery = "SELECT data FROM connections WHERE messaggio = ? ORDER BY data DESC LIMIT 1"; // query per recuperare l'ultimo log dato un messaggio

            // preparazione dell'esecuzione della query
            try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
                preparedStatement.setString(1, mess);      // viene impostato il messaggio fornito come parametro nella query nel primo placeholder
                ResultSet resultSet = preparedStatement.executeQuery(); // viene eseguita la query e ottentuo il risultato

                // se nel risultato esiste almeno una entry
                if (resultSet.next()) {
                    java.sql.Timestamp timestamp = resultSet.getTimestamp("data");  // viene ottenuto il timestamp dalla colonna "data"
                    retrievedDate = new Date(timestamp.getTime());                             // viene convertio il timestamp in un oggetto Date ed inserito nella variabile istanza relativa
                }
            }

            DatabaseConnector.closeConnection(connection);  // chiusura della connessione al database
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Metodo Getter che restituisce la data recuperata dall'ultimo LOG.
     *
     * @return L'oggetto Date corrispondente alla data recuperata dell'ultumo LOG.
     */
    public Date getRetrievedDate() {
        return retrievedDate;
    }
}