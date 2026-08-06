import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {

    private static final String URL = System.getenv("DB_URL");
    private static final String USER = System.getenv("DB_USER");
    private static final String PASSWORD = System.getenv("DB_PASSWORD");

    public static Connection getConnection() throws SQLException {
        if (URL == null || USER == null || PASSWORD == null) {
            throw new IllegalStateException(
                "Missing DB env vars. Set DB_URL, DB_USER, and DB_PASSWORD before running.");
        }

        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);

        } catch (SQLException e) {
            throw new SQLException("Could not connect to " + URL + " as " + USER
                    + " - check the URL, credentials, and that port 3306 is open to your IP. "
                    + e.getMessage(), e.getSQLState(), e.getErrorCode(), e);
        }
    }
}
