import java.io.*;
import java.util.UUID;
import org.mindrot.jbcrypt.BCrypt;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserRegistration {
    private boolean Passed;

    public UserRegistration(){}


    Administrator admin = new Administrator();


    public void registerCustomer(String userName, String password, String email, String phoneNumber, String membershipType){

        String customerID = UUID.randomUUID().toString();
        String membershipID = UUID.randomUUID().toString();
        String hashedPass = BCrypt.hashpw(password, BCrypt.gensalt());


        try (Connection conn = DatabaseConnector.getConnection()) {

            //Check for duplicate usernames

            String checkSql = "SELECT COUNT(*) FROM user WHERE Username = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setString(1, userName);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    System.out.println("Username already taken!\n");
                    return;
                }
            }


            String insertUserSql = "INSERT INTO user (Username, Password, Email, PhoneNumber, Role) VALUES (?, ?, ?, ?, 'CUSTOMER')";
            int generatedUserId = -1;

            try (PreparedStatement userStmt = conn.prepareStatement(insertUserSql, Statement.RETURN_GENERATED_KEYS)) {
                userStmt.setString(1, userName);
                userStmt.setString(2, hashedPass);
                userStmt.setString(3, email);
                userStmt.setString(4, phoneNumber);
                userStmt.executeUpdate();

                ResultSet generatedKeys = userStmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    generatedUserId = generatedKeys.getInt(1);
                }
            }


            String insertMembershipSql = "INSERT INTO membership (MID, MembershipType, MembershipCost) VALUES (?, ?, ?)";
            try (PreparedStatement memStmt = conn.prepareStatement(insertMembershipSql)) {
                memStmt.setString(1, membershipID);
                memStmt.setString(2, membershipType);
                memStmt.setDouble(3, 0.00);
                memStmt.executeUpdate();
            }


            String insertCustomerSql = "INSERT INTO customer (CID, userID, MembershipID) VALUES (?, ?, ?)";
            try (PreparedStatement custStmt = conn.prepareStatement(insertCustomerSql)) {
                custStmt.setString(1, customerID);
                custStmt.setInt(2, generatedUserId);
                custStmt.setString(3, membershipID);
                custStmt.executeUpdate();
            }

            System.out.println("Customer successfully registered to the database!\n");

        } catch (SQLException e) {
            System.out.println("Database error during Customer registration: " + e.getMessage());
            e.printStackTrace();
        }
    }


    public void registerAdmin(String userName, String password, String email, String phoneNumber){
        String adminId = UUID.randomUUID().toString();
        String hashedPass = BCrypt.hashpw(password, BCrypt.gensalt());

        try (Connection conn = DatabaseConnector.getConnection()) {

            //Check for duplicate usernames

            String checkSql = "SELECT COUNT(*) FROM user WHERE Username = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setString(1, userName);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    System.out.println("Admin Username already taken!\n");
                    return;
                }
            }

            String insertUserSql = "INSERT INTO user (Username, Password, Email, PhoneNumber, Role) VALUES (?, ?, ?, ?, 'ADMIN')";
            int generatedUserId = -1;

            try (PreparedStatement userStmt = conn.prepareStatement(insertUserSql, Statement.RETURN_GENERATED_KEYS)) {
                userStmt.setString(1, userName);
                userStmt.setString(2, hashedPass);
                userStmt.setString(3, email);
                userStmt.setString(4, phoneNumber);
                userStmt.executeUpdate();

                ResultSet generatedKeys = userStmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    generatedUserId = generatedKeys.getInt(1);
                }
            }

            String insertAdminSql = "INSERT INTO Admin (AID, userID) VALUES (?, ?)";
            try (PreparedStatement adminStmt = conn.prepareStatement(insertAdminSql)) {
                adminStmt.setString(1, adminId);
                adminStmt.setInt(2, generatedUserId);
                adminStmt.executeUpdate();
            }

            System.out.println("Admin successfully registered to the database!\n");

        } catch (SQLException e) {
            System.out.println("Database error during Admin registration: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void checkAdminPass(String adminPass){
        if(adminPass.equals(admin.getAdminPass())){
            this.Passed = true;
        } else {
            this.Passed = false;
            System.out.println("Admin Password Mismatch!\n");
        }
    }

    public boolean getPassed(){
        return Passed;
    }
}