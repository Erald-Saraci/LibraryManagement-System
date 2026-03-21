import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {

        private static final String URL = "jdbc:mysql://localhost:3306/library";
        private static final String USER = "root";
        private static final String PASSWORD = "ProSWEDev1.";

        public static Connection getConnection() {
            Connection connection = null;

            try {
                connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "ProSWEDev1.");
                System.out.println("Successfully connected to the database!");
            } catch (SQLException e) {
                System.out.println("Failed to connect. Check your URL, username, or password.");
                e.printStackTrace();
            }

            return connection;
        }
}
