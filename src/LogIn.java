import org.mindrot.jbcrypt.BCrypt;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LogIn {

    public LogIn(){}

    //LogIn method---------------

    public static boolean LogInCustomer(Connection conn, String userName, String password) {

        String sql = "SELECT u.Username, u.Password, u.Email, u.PhoneNumber, u.Role, " +
                "c.CID, c.MembershipID, " +
                "m.MembershipType " +
                "FROM user u " +
                "JOIN customer c ON u.ID = c.userID " +
                "JOIN membership m ON c.MembershipID = m.MID " +
                "WHERE u.Username = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userName);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String dbPasswordHash = rs.getString("Password");


                if (BCrypt.checkpw(password, dbPasswordHash)) {


                    String dbName = rs.getString("Username");
                    String dbEmail = rs.getString("Email");
                    String dbPhone = rs.getString("PhoneNumber");
                    String dbRole = rs.getString("Role");
                    String dbCID = rs.getString("CID");
                    String dbMembershipID = rs.getString("MembershipID");
                    String dbMembershipType = rs.getString("MembershipType");


                    Main.currentUser = new Customer(
                            dbName,
                            dbPasswordHash,
                            dbEmail,
                            dbPhone,
                            dbCID,
                            dbMembershipID,
                            dbMembershipType,
                            dbRole
                    );

                    System.out.println("SUCCESS: Logged in! Welcome back: " + dbName);
                    return true;

                } else {
                    System.out.println("LOGIN FAIL: Password does not match for user: " + userName);
                    return false;
                }
            } else {

                System.out.println("LOGIN FAIL: Username not found or Account links (Customer/Membership) are missing.");
                return false;
            }

        } catch (SQLException e) {
            System.out.println("DATABASE ERROR during login: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }



    public static boolean LogInAdmin(Connection conn,String userName, String password) {

        String sql = "SELECT u.Username, u.Password, u.Email, u.PhoneNumber, u.Role, a.AID " +
                "FROM user u " +
                "JOIN admin a ON u.ID = a.userID " +
                "WHERE u.Username = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, userName);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String dbPasswordHash = rs.getString("Password");

                if (BCrypt.checkpw(password, dbPasswordHash)) {


                    String dbName = rs.getString("Username");
                    String dbEmail = rs.getString("Email");
                    String dbPhone = rs.getString("PhoneNumber");
                    String dbAdminID = rs.getString("AID");
                    String dbRole = rs.getString("Role");


                    Main.currentUser = new Administrator(dbName, dbPasswordHash, dbEmail, dbPhone, dbAdminID, dbRole);

                    System.out.println("Logged in! Welcome back: " + dbName);
                    return true;

                } else {
                    System.out.println("Invalid password.");
                    return false;
                }
            } else {
                System.out.println("Invalid username.");
                return false;
            }

        } catch (SQLException e) {
            System.out.println("Database error during login.");
            e.printStackTrace();
            return false;
        }
    }

    //Log Out method---------------

    public static void LogOut(){
        Main.currentUser = null;
    }
}
