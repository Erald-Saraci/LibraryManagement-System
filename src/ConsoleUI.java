import java.util.ArrayList;
import java.time.LocalDate;
import java.util.Scanner;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConsoleUI {

    public void ConsoleUi() {

        Scanner sc = new Scanner(System.in);
        UserRegistration user = new UserRegistration();
        Administrator adminConstructor = new Administrator();
        Library library = new Library();


        //    Menu ----------------------

        System.out.println("1. Register.\n" +
                "2. Log In");
        int choice1;

        while(Main.currentUser == null) {
            try {
                choice1 = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }
            switch (choice1) {
                case 1: {
                    System.out.println("1. Register as a customer");
                    System.out.println("2. Register as an Administrator");
                    System.out.println("Enter your choice: ");
                    int choice2 = sc.nextInt();
                    sc.nextLine();


                    // Register Menu----------

                    switch (choice2) {


                        //  Registration as user----------

                        case 1:

                            String memType;

                            System.out.println("Enter User Name: ");
                            String customerName = sc.nextLine();
                            System.out.println("Enter Password: ");
                            String customerPassword = sc.nextLine();
                            System.out.println("Enter Email: ");
                            String customerEmail = sc.nextLine();
                            System.out.println("Enter Phone Number: ");
                            String customerPhoneNumber = sc.nextLine();
                            System.out.println("Chose Membership Type: ");
                            System.out.println("1. Standard");
                            System.out.println("2. Premium");
                            int memChoice;
                            try {
                                memChoice = Integer.parseInt(sc.nextLine());
                            } catch (NumberFormatException e) {
                                System.out.println("Invalid input. Please enter a number.");
                                break;
                            }

                            switch (memChoice){
                                case 1:
                                    memType = "Standard";
                                    break;
                                case 2:
                                    memType = "Premium";
                                    break;
                                default: {
                                    System.out.println("Invalid input!");
                                    memType="Standard";
                                }
                            }
                            user.registerCustomer(customerName, customerPassword, customerEmail, customerPhoneNumber, memType);
                            break;


                        //Register as an Admin------------------

                        case 2:
                            System.out.println("Enter admin password: ");
                            String adminPass = sc.nextLine();
                            user.checkAdminPass(adminPass);

                            if (user.getPassed()) {
                                System.out.println("Enter User Name: ");
                                String adminName = sc.nextLine();
                                System.out.println("Enter Password: ");
                                String adminPassword = sc.nextLine();
                                System.out.println("Enter Email: ");
                                String adminEmail = sc.nextLine();
                                System.out.println("Enter PhoneNumber: ");
                                String adminPhoneNumber = sc.nextLine();

                                user.registerAdmin(adminName, adminPassword, adminEmail, adminPhoneNumber);
                            }else {
                                System.out.println("Invalid input!");
                            }

                            break;

                    }


                }

                case 2: {
                    System.out.println("1.Log In as customer");
                    System.out.println("2.Log In as administrator");
                    System.out.println("Enter your choice: ");
                    int logInChoice;
                    try {
                        logInChoice = Integer.parseInt(sc.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Please enter a number.");
                        break;
                    }

                    if (logInChoice == 1) {
                        Connection conn = DatabaseConnector.getConnection();
                        System.out.println("Enter UserName: ");
                        String userName = sc.nextLine();
                        System.out.println("Enter Password: ");
                        String password = sc.nextLine();

                        LogIn.LogInCustomer(conn, userName, password);

                    } else if (logInChoice == 2) {
                        Connection conn = DatabaseConnector.getConnection();
                        System.out.println("Enter UserName: ");
                        String userName = sc.nextLine();
                        System.out.println("Enter Password: ");
                        String password = sc.nextLine();

                        LogIn.LogInAdmin(conn, userName, password);
                    } else {
                        System.out.println("Already logged in!");
                    }
                    break;
                }
            }
        }

        loopApp: while (true) {
            System.out.println("1. Add Book");
            System.out.println("2. Borrow Book");
            System.out.println("3. Return Book");
            System.out.println("4. Remove Book");
            System.out.println("5. Log Out");
            System.out.println("6. Filter");
            System.out.println("7. Search for book");
            System.out.println("8. Show current user profile");
            System.out.println("9. Update Book");
            System.out.println("10. Check Overdue Fines");
            System.out.println("11. Search users");
            System.out.println("12. Borrow time extend request");
            System.out.println("13. Reserve Book");
            System.out.println("14. My Reservations");
            System.out.println("15. Generate Invoice");
            System.out.println("16. Cancel Reservation");
            System.out.println("17. My Borrowed Books");
            System.out.println("18. All Borrowed Books");
            System.out.println("19. Change Password");
            System.out.println("0. Exit");

            System.out.println("Enter your choice: ");
            int choice;
            try {
                choice = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }

            switch (choice) {


                //    Add books ------------------


                case 1: {
                    if (!(Main.currentUser instanceof Administrator)) {
                        System.out.println("Access Denied: Only Administrators can add books.");
                        break;
                    }
                    System.out.println("Enter Book Title: ");
                    String title = sc.nextLine();

                    System.out.println("Enter Book Author: ");
                    String author = sc.nextLine();

                    System.out.println("Enter Book Genre: ");
                    String genre = sc.nextLine();

                    System.out.println("Enter Book ISBN: ");
                    String ISBN = sc.nextLine();

                    System.out.println("Enter Publication Year: ");
                    int year = sc.nextInt();
                    sc.nextLine();

                    adminConstructor.addBook(title, author, genre, ISBN, year);
                    break;
                }
                //  Borrow book -----------------

                case 2: {
                    if (!(Main.currentUser instanceof Customer)) {
                        System.out.println("Access Denied: Only Customers can borrow books.");
                        break;
                    }
                    System.out.println("Enter Book title: ");
                    String bookTitle = sc.nextLine();
                    System.out.println("Enter Book Author: ");
                    String bookAuthor = sc.nextLine();
                    System.out.println("Enter the amount of days you want to borrow the book: ");
                    int nrOfDays;
                    try {
                        nrOfDays = Integer.parseInt(sc.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Please enter a number.");
                        break;
                    }
                    LocalDate borrowDate = LocalDate.now();
                    Customer customer = (Customer) Main.currentUser;
                    customer.borrowBook(bookTitle, bookAuthor, borrowDate, nrOfDays);

                    break;
                }

                //Return Book------------------
                case 3: {
                    if (!(Main.currentUser instanceof Customer)) {
                        System.out.println("Access Denied: Only Customers can return books.");
                        break;
                    }
                    System.out.println("Enter Book title: ");
                    String bookTitle = sc.nextLine();
                    System.out.println("Enter Book Author: ");
                    String bookAuthor = sc.nextLine();
                    Customer customer = (Customer) Main.currentUser;
                    customer.returnBook(bookTitle, bookAuthor);

                    break;
                }

                case 4: {
                    if (!(Main.currentUser instanceof Administrator)) {
                        System.out.println("Access Denied: Only Administrators can remove books.");
                        break;
                    }
                    System.out.println("Enter Book title: ");
                    String bookTitle = sc.nextLine();
                    System.out.println("Enter Book Author: ");
                    String bookAuthor = sc.nextLine();

                    adminConstructor.removeBook(bookTitle, bookAuthor);
                    break;
                }
                case 5: {

                    //LogOut-------------------
                    if (Main.currentUser != null) {
                        LogIn.LogOut();
                        System.out.println("Logged out successfully.");
                    }
                    break;
                }


                //Filter -----------------


                case 6: {
                    System.out.println("1. Filter by genre");
                    System.out.println("2. Filter by year");
                    System.out.println("3. Filter by available");

                    if (sc.hasNextInt()) {
                        int filter;
                        try {
                            filter = Integer.parseInt(sc.nextLine());
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid input. Please enter a number.");
                            break;
                        }

                        ArrayList<Book> results = new ArrayList<>();

                        switch (filter) {
                            case 1:
                                System.out.println("Enter genre: ");
                                String g = sc.nextLine();
                                results = library.filterByGenre(g);
                                break;
                            case 2:
                                System.out.println("Enter start year: ");
                                int start = sc.nextInt();
                                System.out.println("Enter end year: ");
                                int end = sc.nextInt();
                                sc.nextLine();
                                results = library.filterByYear(start, end);
                                break;
                            case 3:
                                results = library.getAvailableBooks();
                                break;
                            default:
                                System.out.println("Invalid filter choice.");
                        }

                        // Print Results
                        if (results.isEmpty()) {
                            System.out.println("No books found.");
                        } else {
                            System.out.println("\n--- Search Results ---");
                            for (Book b : results) {
                                System.out.println(b.toString());
                            }
                            System.out.println("----------------------\n");
                        }
                    } else {
                        sc.nextLine();
                        System.out.println("Invalid input.");
                    }
                    break;
                }


                //Search book ---------------------


                case 7: {
                    System.out.println("Enter the book title: ");
                    String search = sc.nextLine();

                    String sql = "SELECT Title, Author, Genre, ISBN, PublishYear, Availability " +
                            "FROM books WHERE Title LIKE ?";

                    try (Connection conn = DatabaseConnector.getConnection();
                         PreparedStatement pstmt = conn.prepareStatement(sql)) {

                        pstmt.setString(1, "%" + search + "%");
                        ResultSet rs = pstmt.executeQuery();

                        System.out.println("--- SEARCH RESULTS ---");
                        boolean found = false;
                        while (rs.next()) {
                            System.out.println(new Book(
                                    rs.getString("Title"),
                                    rs.getString("Author"),
                                    rs.getString("Genre"),
                                    rs.getString("ISBN"),
                                    rs.getInt("PublishYear"),
                                    rs.getBoolean("Availability")
                            ));
                            found = true;
                        }
                        if (!found) System.out.println("No books found matching: " + search);
                        System.out.println("----------------------");

                    } catch (SQLException e) {
                        System.out.println("Database error during search.");
                        e.printStackTrace();
                    }
                    break;
                }
                case 8: {
                    System.out.println(Main.currentUser);
                    break;
                }

                case 9: {
                    if (!(Main.currentUser instanceof Administrator)) {
                        System.out.println("Access Denied: Only Administrators can update books.");
                        break;
                    }
                    int uChoice;
                    System.out.println("Enter book title: ");
                    String bookTitle = sc.nextLine();
                    System.out.println("Enter Book Author: ");
                    String bookAuthor = sc.nextLine();

                    System.out.println("1.Update Book Title");
                    System.out.println("2.Update Book Author");
                    System.out.println("3.Update Book Genre");
                    System.out.println("4.Update Book Publication Year");
                    System.out.println("5.Update Book Availability");

                    try {
                        uChoice = Integer.parseInt(sc.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Please enter a number.");
                        break;
                    }
                    switch (uChoice) {
                        case 1: {
                            Administrator admin = (Administrator) Main.currentUser;
                            System.out.println("Enter new Book Title: ");
                            String newTitle = sc.nextLine();
                            admin.updateBookTitle(bookTitle, bookAuthor, newTitle);
                            break;
                        }
                        case 2: {
                            Administrator admin = (Administrator) Main.currentUser;
                            System.out.println("Enter new Book Author: ");
                            String newAuthor = sc.nextLine();
                            admin.updateBookAuthor(bookTitle, bookAuthor, newAuthor);
                            break;
                        }
                        case 3: {
                            Administrator admin = (Administrator) Main.currentUser;
                            System.out.println("Enter new Book Genre: ");
                            String newGenre = sc.nextLine();
                            admin.updateBookGenre(bookTitle, bookAuthor, newGenre);
                            break;
                        }
                        case 4: {
                            Administrator admin = (Administrator) Main.currentUser;
                            System.out.println("Enter new Book Publication Year: ");
                            int newPublicationYear = sc.nextInt();
                            sc.nextLine();
                            admin.updateBookYear(bookTitle, bookAuthor, newPublicationYear);
                            break;
                        }
                        case 5: {
                            Administrator admin = (Administrator) Main.currentUser;
                            System.out.println("Enter new Book Availability (true/false): ");
                            boolean newAvailability = sc.nextBoolean();
                            sc.nextLine();
                            admin.updateBookAvailability(bookTitle, bookAuthor, newAvailability);
                            break;
                        }
                    }
                    break;
                }

                case 10: {
                    if (Main.currentUser instanceof Administrator) {
                        Administrator admin = (Administrator) Main.currentUser;
                        admin.calculateOverdueFines();
                    } else {
                        System.out.println("Access Denied: Only Administrators can check fines.");
                    }
                    break;
                }

                case 11: {
                    if (!(Main.currentUser instanceof Administrator)) {
                        System.out.println("Access Denied: Only Administrators can search users.");
                        break;
                    }
                    System.out.println("Enter user name: ");
                    String keyword = sc.nextLine();

                    String sql = "SELECT u.Username, u.Email, u.PhoneNumber, u.Role " +
                            "FROM user u WHERE u.Username LIKE ?";

                    try (Connection conn = DatabaseConnector.getConnection();
                         PreparedStatement pstmt = conn.prepareStatement(sql)) {

                        pstmt.setString(1, "%" + keyword + "%");
                        ResultSet rs = pstmt.executeQuery();

                        System.out.println("--- USER SEARCH RESULTS ---");
                        boolean found = false;
                        while (rs.next()) {
                            System.out.println("Username: " + rs.getString("Username") +
                                    " | Email: " + rs.getString("Email") +
                                    " | Phone: " + rs.getString("PhoneNumber") +
                                    " | Role: " + rs.getString("Role"));
                            found = true;
                        }
                        if (!found) System.out.println("No users found matching: " + keyword);
                        System.out.println("---------------------------");

                    } catch (SQLException e) {
                        System.out.println("Database error during user search.");
                        e.printStackTrace();
                    }
                    break;
                }

                case 12: {
                    if (Main.currentUser instanceof Customer) {
                        System.out.println("Enter the title of the book you want to extend: ");
                        String titleToExtend = sc.nextLine();

                        Customer customer = (Customer) Main.currentUser;
                        customer.requestExtension(titleToExtend);
                    } else {
                        System.out.println("Only Customers can perform this action.");
                    }
                    break;
                }

                //Reserve Book------------------

                case 13: {
                    if (!(Main.currentUser instanceof Customer)) {
                        System.out.println("Access Denied: Only Customers can reserve books.");
                        break;
                    }
                    System.out.println("Enter Book Title: ");
                    String title = sc.nextLine();
                    System.out.println("Enter Book Author: ");
                    String author = sc.nextLine();
                    ((Customer) Main.currentUser).reserveBook(title, author);
                    break;
                }

                //My Reservations------------------

                case 14: {
                    if (!(Main.currentUser instanceof Customer)) {
                        System.out.println("Access Denied: Only Customers can view reservations.");
                        break;
                    }
                    String sql = "SELECT b.Title, b.Author, r.ReservedDate, r.Status, " +
                            "(SELECT COUNT(*) FROM reservations r2 " +
                            " WHERE r2.ISBN = r.ISBN AND r2.Status = 'Pending' " +
                            " AND r2.ReservedDate <= r.ReservedDate) AS QueuePosition " +
                            "FROM reservations r " +
                            "JOIN books b ON r.ISBN = b.ISBN " +
                            "WHERE r.CID = ? AND r.Status != 'Completed' " +
                            "ORDER BY r.ReservedDate ASC";

                    try (Connection conn = DatabaseConnector.getConnection();
                         PreparedStatement pstmt = conn.prepareStatement(sql)) {

                        pstmt.setString(1, ((Customer) Main.currentUser).getCustomerID());
                        ResultSet rs = pstmt.executeQuery();

                        System.out.println("--- YOUR RESERVATIONS ---");
                        boolean found = false;
                        while (rs.next()) {
                            String status = rs.getString("Status");
                            int position = rs.getInt("QueuePosition");
                            System.out.println("Book:     " + rs.getString("Title") + " by " + rs.getString("Author"));
                            System.out.println("Reserved: " + rs.getString("ReservedDate"));
                            System.out.println("Status:   " + status +
                                    (status.equals("Pending") ? " (Queue position: " + position + ")" : " — You can borrow this now!"));
                            System.out.println("-----");
                            found = true;
                        }
                        if (!found) System.out.println("You have no active reservations.");
                        System.out.println("-------------------------");

                    } catch (SQLException e) {
                        System.out.println("Database error fetching reservations.");
                        e.printStackTrace();
                    }
                    break;
                }

                //Generate Invoice------------------

                case 15: {
                    if (!(Main.currentUser instanceof Administrator)) {
                        System.out.println("Access Denied: Only Administrators can generate invoices.");
                        break;
                    }
                    System.out.println("Enter Customer Username: ");
                    String customerName = sc.nextLine();
                    System.out.println("Enter Amount: ");
                    double amount = sc.nextDouble();
                    sc.nextLine();
                    if (amount <= 0) {
                        System.out.println("Error: Amount must be positive.");
                    } else {
                        ((Administrator) Main.currentUser).generateInvoice(customerName, amount);
                    }
                    break;
                }

                //Cancel Reservation------------------
                case 16: {
                    if (!(Main.currentUser instanceof Customer)) {
                        System.out.println("Access Denied: Only Customers can cancel reservations.");
                        break;
                    }
                    System.out.println("Enter Book Title to Cancel Reservation: ");
                    String title = sc.nextLine();
                    ((Customer) Main.currentUser).cancelReservation(title);
                    break;
                }

//My Borrowed Books------------------
                case 17: {
                    if (!(Main.currentUser instanceof Customer)) {
                        System.out.println("Access Denied: Only Customers can view their borrowed books.");
                        break;
                    }
                    ((Customer) Main.currentUser).showMyBorrowedBooks();
                    break;
                }

//All Borrowed Books------------------
                case 18: {
                    if (!(Main.currentUser instanceof Administrator)) {
                        System.out.println("Access Denied: Only Administrators can view all borrowed books.");
                        break;
                    }
                    adminConstructor.showAllBorrowedBooks();
                    break;
                }

//Change Password------------------
                case 19: {
                    if (!(Main.currentUser instanceof Customer)) {
                        System.out.println("Access Denied: Only Customers can change their password.");
                        break;
                    }
                    System.out.println("Enter Old Password: ");
                    String oldPassword = sc.nextLine();
                    System.out.println("Enter New Password: ");
                    String newPassword = sc.nextLine();
                    if (newPassword.length() < 8 ||
                            !newPassword.matches(".*[A-Z].*") ||
                            !newPassword.matches(".*[a-z].*") ||
                            !newPassword.matches(".*[0-9].*") ||
                            !newPassword.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*")) {
                        System.out.println("Error: Password must be 8+ characters with uppercase, lowercase, number and special character.");
                        break;
                    }
                    ((Customer) Main.currentUser).changePassword(oldPassword, newPassword);
                    break;
                }

                case 0: {
                    System.out.println("Bye!");
                    break loopApp;
                }

                default:
                    System.out.println("Invalid choice!");
                    break;
            }
        }
    }
}