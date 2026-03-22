import java.util.ArrayList;
import java.time.LocalDate;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.UUID;

public class Customer extends User {
    private String customerID;
    private String MembershipID;
    private String membershipType;
    private int maxBooks;

    //Constructors--------------
    public Customer() {}

    public Customer(String userName, String hashedpass, String email, String phoneNumber, String customerID, String MembershipID, String membershipType) {
        super(userName, hashedpass, email, phoneNumber);
        this.customerID = customerID;
        this.MembershipID = MembershipID;
        this.membershipType = membershipType;
        this.role = "Customer";
        this.maxBooks = membershipType.equalsIgnoreCase("Premium") ? 5 : 2;
    }

    public Customer(String userName, String hashedpass, String email, String phoneNumber, String customerID, String MembershipID, String membershipType, String role) {
        super(userName, hashedpass, email, phoneNumber);
        this.customerID = customerID;
        this.MembershipID = MembershipID;
        this.membershipType = membershipType;
        this.role = role;
        this.maxBooks = membershipType.equalsIgnoreCase("Premium") ? 5 : 2;
    }

    //Getters-------------------
    public String getCustomerID() { return customerID; }
    public String getMembershipID() { return MembershipID; }

    //Setters-------------------
    public void setCustomerID(String customerID) { this.customerID = customerID; }

    public int getCurrentBorrowedCount() {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM borrowed WHERE CID = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, this.customerID);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) count = rs.getInt(1);

        } catch (SQLException e) {
            System.out.println("Database error checking borrowed count.");
            e.printStackTrace();
        }
        return count;
    }

    public void borrowBook(String title, String author, LocalDate borrowDate, int nrOfDays) {

        if (getCurrentBorrowedCount() >= this.maxBooks) {
            System.out.println("Limit reached! You have a " + this.membershipType + " membership (max " + this.maxBooks + " books).");
            return;
        }

        LocalDate dueDate = borrowDate.plusDays(nrOfDays);

        try (Connection conn = DatabaseConnector.getConnection()) {

            String findBookSql = "SELECT ISBN, Availability FROM books WHERE Title = ? AND Author = ? LIMIT 1";
            String targetISBN = null;
            boolean isAvailable = false;

            try (PreparedStatement findStmt = conn.prepareStatement(findBookSql)) {
                findStmt.setString(1, title);
                findStmt.setString(2, author);
                ResultSet rs = findStmt.executeQuery();
                if (rs.next()) {
                    targetISBN = rs.getString("ISBN");
                    isAvailable = rs.getBoolean("Availability");
                } else {
                    System.out.println("Book not found: " + title);
                    return;
                }
            }

            String checkReadySql = "SELECT ReservationID FROM reservations WHERE CID = ? AND ISBN = ? AND Status = 'Ready' LIMIT 1";
            String readyResID = null;

            try (PreparedStatement readyStmt = conn.prepareStatement(checkReadySql)) {
                readyStmt.setString(1, this.customerID);
                readyStmt.setString(2, targetISBN);
                ResultSet rs = readyStmt.executeQuery();
                if (rs.next()) {
                    readyResID = rs.getString("ReservationID");
                }
            }

            if (!isAvailable && readyResID == null) {
                String checkPendingSql = "SELECT COUNT(*) FROM reservations WHERE CID = ? AND ISBN = ? AND Status = 'Pending'";
                try (PreparedStatement pendingStmt = conn.prepareStatement(checkPendingSql)) {
                    pendingStmt.setString(1, this.customerID);
                    pendingStmt.setString(2, targetISBN);
                    ResultSet rs = pendingStmt.executeQuery();
                    if (rs.next() && rs.getInt(1) > 0) {
                        System.out.println("Book is unavailable. You are already in the reservation queue.");
                    } else {
                        System.out.println("Book is unavailable. Use 'Reserve Book' to join the queue.");
                    }
                }
                return;
            }

            try (PreparedStatement updateStmt = conn.prepareStatement(
                    "UPDATE books SET Availability = false WHERE ISBN = ?")) {
                updateStmt.setString(1, targetISBN);
                updateStmt.executeUpdate();
            }

            String borrowID = "BOR-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            try (PreparedStatement insertStmt = conn.prepareStatement(
                    "INSERT INTO borrowed (BorrowID, CID, BorrowDate, ReturnDate, ISBN) VALUES (?, ?, ?, ?, ?)")) {
                insertStmt.setString(1, borrowID);
                insertStmt.setString(2, this.customerID);
                insertStmt.setDate(3, Date.valueOf(borrowDate));
                insertStmt.setDate(4, Date.valueOf(dueDate));
                insertStmt.setString(5, targetISBN);
                insertStmt.executeUpdate();
            }

            if (readyResID != null) {
                try (PreparedStatement completeStmt = conn.prepareStatement(
                        "UPDATE reservations SET Status = 'Completed' WHERE ReservationID = ?")) {
                    completeStmt.setString(1, readyResID);
                    completeStmt.executeUpdate();
                }
            }

            System.out.println(this.getUserName() + " borrowed '" + title + "' successfully! Due: " + dueDate);

        } catch (SQLException e) {
            System.out.println("Database error during borrow process.");
            e.printStackTrace();
        }
    }

    public void returnBook(String title, String author) {
        try (Connection conn = DatabaseConnector.getConnection()) {

            String findLoanSql = "SELECT br.ISBN FROM borrowed br " +
                    "JOIN books b ON br.ISBN = b.ISBN " +
                    "WHERE br.CID = ? AND b.Title = ? AND b.Author = ? LIMIT 1";
            String targetISBN = null;

            try (PreparedStatement findStmt = conn.prepareStatement(findLoanSql)) {
                findStmt.setString(1, this.customerID);
                findStmt.setString(2, title);
                findStmt.setString(3, author);
                ResultSet rs = findStmt.executeQuery();
                if (rs.next()) {
                    targetISBN = rs.getString("ISBN");
                } else {
                    System.out.println("No borrowed record found for '" + title + "' by " + author);
                    return;
                }
            }

            try (PreparedStatement deleteStmt = conn.prepareStatement(
                    "DELETE FROM borrowed WHERE CID = ? AND ISBN = ?")) {
                deleteStmt.setString(1, this.customerID);
                deleteStmt.setString(2, targetISBN);
                deleteStmt.executeUpdate();
                System.out.println(this.getUserName() + " returned '" + title + "' successfully!");
            }

            String nextInQueueSql = "SELECT r.ReservationID, r.CID, u.Username " +
                    "FROM reservations r " +
                    "JOIN customer c ON r.CID = c.CID " +
                    "JOIN user u ON c.userID = u.ID " +
                    "WHERE r.ISBN = ? AND r.Status = 'Pending' " +
                    "ORDER BY r.ReservedDate ASC LIMIT 1";
            String nextCID = null;
            String nextResID = null;
            String nextUsername = null;

            try (PreparedStatement queueStmt = conn.prepareStatement(nextInQueueSql)) {
                queueStmt.setString(1, targetISBN);
                ResultSet rs = queueStmt.executeQuery();
                if (rs.next()) {
                    nextCID      = rs.getString("CID");
                    nextResID    = rs.getString("ReservationID");
                    nextUsername = rs.getString("Username");
                }
            }

            if (nextCID != null) {
                try (PreparedStatement updateStmt = conn.prepareStatement(
                        "UPDATE reservations SET Status = 'Ready' WHERE ReservationID = ?")) {
                    updateStmt.setString(1, nextResID);
                    updateStmt.executeUpdate();
                }
                System.out.println("Book is reserved! Next in queue: " + nextUsername);
            } else {
                try (PreparedStatement updateBookStmt = conn.prepareStatement(
                        "UPDATE books SET Availability = true WHERE ISBN = ?")) {
                    updateBookStmt.setString(1, targetISBN);
                    updateBookStmt.executeUpdate();
                    System.out.println("Book is now available for borrowing.");
                }
            }

        } catch (SQLException e) {
            System.out.println("Database error during return process.");
            e.printStackTrace();
        }
    }

    public void reserveBook(String title, String author) {
        try (Connection conn = DatabaseConnector.getConnection()) {

            String findBookSql = "SELECT ISBN, Availability FROM books WHERE Title = ? AND Author = ? LIMIT 1";
            String targetISBN = null;
            boolean isAvailable = false;

            try (PreparedStatement findStmt = conn.prepareStatement(findBookSql)) {
                findStmt.setString(1, title);
                findStmt.setString(2, author);
                ResultSet rs = findStmt.executeQuery();
                if (rs.next()) {
                    targetISBN  = rs.getString("ISBN");
                    isAvailable = rs.getBoolean("Availability");
                } else {
                    System.out.println("Book not found: " + title);
                    return;
                }
            }

            if (isAvailable) {
                System.out.println("This book is currently available! You can borrow it directly.");
                return;
            }

            String checkResSql = "SELECT COUNT(*) FROM reservations WHERE CID = ? AND ISBN = ? AND Status = 'Pending'";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkResSql)) {
                checkStmt.setString(1, this.customerID);
                checkStmt.setString(2, targetISBN);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    System.out.println("You have already reserved this book.");
                    return;
                }
            }

            String reservationID = "RES-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            try (PreparedStatement insertStmt = conn.prepareStatement(
                    "INSERT INTO reservations (ReservationID, CID, ISBN, ReservedDate, Status) VALUES (?, ?, ?, ?, 'Pending')")) {
                insertStmt.setString(1, reservationID);
                insertStmt.setString(2, this.customerID);
                insertStmt.setString(3, targetISBN);
                insertStmt.setDate(4, Date.valueOf(LocalDate.now()));
                insertStmt.executeUpdate();
                System.out.println("Success! Reserved '" + title + "'. Reservation ID: " + reservationID);
            }

        } catch (SQLException e) {
            System.out.println("Database error during reservation.");
            e.printStackTrace();
        }
    }

    public void requestExtension(String bookTitle) {
        System.out.println("Processing extension request for: " + bookTitle);

        try (Connection conn = DatabaseConnector.getConnection()) {

            String findLoanSql = "SELECT br.ReturnDate, br.ISBN FROM borrowed br " +
                    "JOIN books b ON br.ISBN = b.ISBN " +
                    "WHERE br.CID = ? AND b.Title = ? LIMIT 1";
            LocalDate currentDueDate = null;
            String targetISBN = null;

            try (PreparedStatement findStmt = conn.prepareStatement(findLoanSql)) {
                findStmt.setString(1, this.customerID);
                findStmt.setString(2, bookTitle);
                ResultSet rs = findStmt.executeQuery();
                if (rs.next()) {
                    currentDueDate = rs.getDate("ReturnDate").toLocalDate();
                    targetISBN = rs.getString("ISBN");
                } else {
                    System.out.println("You do not have '" + bookTitle + "' borrowed.");
                    return;
                }
            }

            if (LocalDate.now().isAfter(currentDueDate)) {
                System.out.println("Request Denied: Book is overdue. Please return it and pay the fine first.");
                return;
            }

            if (isBookReserved(conn, bookTitle, this.customerID)) {
                System.out.println("Request Denied: Another user has reserved this book.");
                return;
            }

            LocalDate newDate = currentDueDate.plusDays(3);

            try (PreparedStatement updateStmt = conn.prepareStatement(
                    "UPDATE borrowed SET ReturnDate = ? WHERE CID = ? AND ISBN = ?")) {
                updateStmt.setDate(1, Date.valueOf(newDate));
                updateStmt.setString(2, this.customerID);
                updateStmt.setString(3, targetISBN);
                updateStmt.executeUpdate();
                System.out.println("Success! Return date extended by 3 days to: " + newDate);
            }

        } catch (SQLException e) {
            System.out.println("Database error during extension request.");
            e.printStackTrace();
        }
    }

    private boolean isBookReserved(Connection conn, String bookTitle, String excludeCID) {
        String sql = "SELECT COUNT(*) FROM reservations r " +
                "JOIN books b ON r.ISBN = b.ISBN " +
                "WHERE b.Title = ? AND r.CID != ? AND r.Status = 'Pending'";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, bookTitle);
            pstmt.setString(2, excludeCID);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (SQLException e) {
            System.out.println("Error checking reservations.");
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public String toString() {
        return super.toString() + "," + " Customer ID: " + customerID + "," + "Membership ID: " + MembershipID + "," + "Membership Type: " + membershipType + "," + "Status: " + role;
    }
}