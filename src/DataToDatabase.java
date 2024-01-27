import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

public class DataToDatabase {
    public DataToDatabase(String message) {
        try {
            Connection connection = DatabaseConnector.getConnection();

            // inserimento del log
            String insertQuery = "INSERT INTO connections (data, messaggio) VALUES (?, ?)";

            //per evitare SQL injection
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                preparedStatement.setTimestamp(1, new java.sql.Timestamp(new Date().getTime()));
                preparedStatement.setString(2, message);

                preparedStatement.executeUpdate();
            }

            DatabaseConnector.closeConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
