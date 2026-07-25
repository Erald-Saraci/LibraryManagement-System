import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {

    private static final String URL = System.getenv("DB_URL");
    private static final String USER = System.getenv("DB_USER");
    private static final String PASSWORD = System.getenv("DB_PASSWORD");

    public static Connection getConnection() {
        if (URL == null || USER == null || PASSWORD == null) {
            throw new IllegalStateException(
                "Missing DB env vars. Set DB_URL, DB_USER, and DB_PASSWORD before running.");
        }

        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Successfully connected to the database!");
        } catch (SQLException e) {
            System.out.println("Failed to connect. Check your URL, username, or password.");
            e.printStackTrace();
        }
        return connection;
    }
}
