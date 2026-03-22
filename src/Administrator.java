import java.util.ArrayList;
import java.time.LocalDate;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.UUID;

public class Administrator extends User {
    private String adminID;
    private String adminPass = "admin123";

    public Administrator() {}

    public Administrator(String userName, String hashedPass, String email, String phoneNumber, String adminID) {
        super(userName, hashedPass, email, phoneNumber);
        this.adminID = adminID;
        this.role = "Administrator";
    }

    public Administrator(String userName, String hashedPass, String email, String phoneNumber, String adminID, String role) {
        super(userName, hashedPass, email, phoneNumber);
        this.adminID = adminID;
        this.role = role;
    }

    // Getters & Setters
    public String getAdminID() { return adminID; }
    public String getUserName() { return userName; }
    public String getAdminPass() { return adminPass; }
    public void setAdminID(String adminID) { this.adminID = adminID; }
    public void setAdminPass(String password) { this.adminPass = password; }

    //book methods

    public void addBook(String title, String author, String genre, String ISBN, int year) {
        String checkSql = "SELECT COUNT(*) FROM books WHERE ISBN = ?";
        String insertSql = "INSERT INTO books (ISBN, Title, Author, Genre, PublishYear, Availability) VALUES (?, ?, ?, ?, ?, true)";

        try (Connection conn = DatabaseConnector.getConnection()) {

            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setString(1, ISBN);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    System.out.println("Error: A book with ISBN " + ISBN + " already exists!");
                    return;
                }
            }

            try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                insertStmt.setString(1, ISBN);
                insertStmt.setString(2, title);
                insertStmt.setString(3, author);
                insertStmt.setString(4, genre);
                insertStmt.setInt(5, year);
                insertStmt.executeUpdate();
                System.out.println("Success: Book '" + title + "' added.");
            }

        } catch (SQLException e) {
            System.out.println("Database error adding book.");
            e.printStackTrace();
        }
    }

    public void removeBook(String title, String author) {
        String sql = "DELETE FROM books WHERE Title = ? AND Author = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, title);
            pstmt.setString(2, author);
            int rows = pstmt.executeUpdate();

            if (rows > 0) {
                System.out.println("Success! Removed book: " + title);
            } else {
                System.out.println("Book not found: " + title);
            }

        } catch (SQLException e) {
            System.out.println("Database error removing book.");
            e.printStackTrace();
        }
    }

    public void updateBookTitle(String currentTitle, String author, String newTitle) {
        updateField("Title", newTitle, currentTitle, author);
    }

    public void updateBookAuthor(String title, String currentAuthor, String newAuthor) {
        updateField("Author", newAuthor, title, currentAuthor);
    }

    public void updateBookGenre(String title, String author, String newGenre) {
        updateField("Genre", newGenre, title, author);
    }

    public void updateBookYear(String title, String author, int newYear) {
        String sql = "UPDATE books SET PublishYear = ? WHERE Title = ? AND Author = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, newYear);
            pstmt.setString(2, title);
            pstmt.setString(3, author);
            int rows = pstmt.executeUpdate();
            System.out.println(rows > 0 ? "Year updated successfully." : "Book not found.");
        } catch (SQLException e) {
            System.out.println("Database error updating year.");
            e.printStackTrace();
        }
    }

    public void updateBookAvailability(String title, String author, boolean newStatus) {
        String sql = "UPDATE books SET Availability = ? WHERE Title = ? AND Author = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setBoolean(1, newStatus);
            pstmt.setString(2, title);
            pstmt.setString(3, author);
            int rows = pstmt.executeUpdate();
            System.out.println(rows > 0 ? "Availability updated successfully." : "Book not found.");
        } catch (SQLException e) {
            System.out.println("Database error updating availability.");
            e.printStackTrace();
        }
    }

    private void updateField(String field, String newValue, String title, String author) {
        String sql = "UPDATE books SET " + field + " = ? WHERE Title = ? AND Author = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newValue);
            pstmt.setString(2, title);
            pstmt.setString(3, author);
            int rows = pstmt.executeUpdate();
            System.out.println(rows > 0 ? field + " updated successfully." : "Book not found.");
        } catch (SQLException e) {
            System.out.println("Database error updating " + field + ".");
            e.printStackTrace();
        }
    }

    //Fines

    public void calculateOverdueFines() {
        double finePerDay = 0.50;
        boolean foundOverdue = false;
        LocalDate today = LocalDate.now();

        System.out.println("Current Date: " + today);

        String sql = "SELECT u.Username, b.Title, br.ReturnDate " +
                "FROM borrowed br " +
                "JOIN customer c ON br.CID = c.CID " +
                "JOIN user u ON c.userID = u.ID " +
                "JOIN books b ON br.ISBN = b.ISBN " +
                "WHERE br.ReturnDate < ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDate(1, Date.valueOf(today));
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String username = rs.getString("Username");
                String title = rs.getString("Title");
                LocalDate returnDate = rs.getDate("ReturnDate").toLocalDate();
                long daysOverdue = today.toEpochDay() - returnDate.toEpochDay();
                double totalFine = daysOverdue * finePerDay;

                System.out.println("User: " + username);
                System.out.println("Book: " + title);
                System.out.println("Due Date: " + returnDate);
                System.out.println("Days Overdue: " + daysOverdue);
                System.out.println("Fine: $" + totalFine);
                System.out.println("-----");
                foundOverdue = true;
            }

        } catch (SQLException e) {
            System.out.println("Database error checking fines.");
            e.printStackTrace();
        }

        if (!foundOverdue) {
            System.out.println("No books are currently overdue.");
        }
    }

    //Invoice

    public void generateInvoice(String customerName, double amount) {
        String checkAdminSql = "SELECT COUNT(*) FROM user u JOIN admin a ON u.ID = a.userID WHERE u.Username = ?";
        String checkCustSql  = "SELECT c.CID FROM user u JOIN customer c ON u.ID = c.userID WHERE u.Username = ?";
        String insertSql     = "INSERT INTO invoice (InvID, Date, Amount, AID, CID) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnector.getConnection()) {


            try (PreparedStatement adminCheck = conn.prepareStatement(checkAdminSql)) {
                adminCheck.setString(1, customerName);
                ResultSet rs = adminCheck.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    System.out.println("Error: Cannot issue an invoice to an Administrator!");
                    return;
                }
            }


            String customerCID = null;
            try (PreparedStatement custCheck = conn.prepareStatement(checkCustSql)) {
                custCheck.setString(1, customerName);
                ResultSet rs = custCheck.executeQuery();
                if (rs.next()) {
                    customerCID = rs.getString("CID");
                } else {
                    System.out.println("Error: Customer '" + customerName + "' not found.");
                    return;
                }
            }


            String invoiceID = "INV-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            String issuerName = (Main.currentUser != null) ? Main.currentUser.getUserName() : "Unknown";

            try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                insertStmt.setString(1, invoiceID);
                insertStmt.setDate(2, Date.valueOf(LocalDate.now()));
                insertStmt.setDouble(3, amount);
                insertStmt.setString(4, this.adminID);
                insertStmt.setString(5, customerCID);
                insertStmt.executeUpdate();
            }

            System.out.println("========= LIBRARY INVOICE =========");
            System.out.println("Invoice ID:   " + invoiceID);
            System.out.println("Date:         " + LocalDate.now());
            System.out.println("Issued By:    " + issuerName + " (Admin)");
            System.out.println("-----------------------------------");
            System.out.println("Bill To:      " + customerName);
            System.out.println("Description:  Subscription Payment");
            System.out.println("-----------------------------------");
            System.out.printf("TOTAL AMOUNT: $%.2f%n", amount);
            System.out.println(">> Invoice saved to database.");

        } catch (SQLException e) {
            System.out.println("Database error generating invoice.");
            e.printStackTrace();
        }
    }

    public void showAllBorrowedBooks() {
        String sql = "SELECT u.Username, b.Title, b.Author, br.BorrowDate, br.ReturnDate " +
                "FROM borrowed br " +
                "JOIN customer c ON br.CID = c.CID " +
                "JOIN user u ON c.userID = u.ID " +
                "JOIN books b ON br.ISBN = b.ISBN " +
                "ORDER BY br.ReturnDate ASC";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            System.out.println("--- ALL BORROWED BOOKS ---");
            boolean found = false;
            while (rs.next()) {
                System.out.println("Customer:    " + rs.getString("Username"));
                System.out.println("Title:       " + rs.getString("Title"));
                System.out.println("Author:      " + rs.getString("Author"));
                System.out.println("Borrow Date: " + rs.getString("BorrowDate"));
                System.out.println("Due Date:    " + rs.getString("ReturnDate"));
                System.out.println("-----");
                found = true;
            }
            if (!found) System.out.println("No books are currently borrowed.");
            System.out.println("--------------------------");

        } catch (SQLException e) {
            System.out.println("Database error fetching borrowed books.");
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return super.toString() + ", Admin ID: " + adminID + ", Status: " + role;
    }
}