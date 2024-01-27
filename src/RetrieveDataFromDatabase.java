import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class RetrieveDataFromDatabase {
    private Date retrievedDate = null;

    public RetrieveDataFromDatabase(String mess) {
        try {
            Connection connection = DatabaseConnector.getConnection();
            String selectQuery = "SELECT data FROM connections WHERE messaggio = ? ORDER BY data DESC LIMIT 1";

            try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
                preparedStatement.setString(1, mess);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    java.sql.Timestamp timestamp = resultSet.getTimestamp("data");
                    retrievedDate = new Date(timestamp.getTime());
                }
            }

            DatabaseConnector.closeConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Date getRetrievedDate() {
        return retrievedDate;
    }
}