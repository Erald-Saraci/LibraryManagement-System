import java.io.*;
import java.time.LocalDate;
import java.util.Scanner;
import java.util.UUID;
import java.util.ArrayList;

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
            choice1=sc.nextInt();
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
                            int memChoice = sc.nextInt();
                            sc.nextLine();

                            switch (memChoice){
                                case 1:
                                    memType = "Standard";
                                    break;
                                case 2:
                                    memType = "Premium";
                                    break;
                                default: {
                                    System.out.println("Invalid input!");
                                    memType="Standart";
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
                    int logInChoice = sc.nextInt();
                    sc.nextLine();

                    if (logInChoice == 1) {
                        System.out.println("Enter UserName: ");
                        String userName = sc.nextLine();
                        System.out.println("Enter Password: ");
                        String password = sc.nextLine();

                        LogIn.LogInCustomer(userName, password);

                    } else if (logInChoice == 2) {

                        System.out.println("Enter UserName: ");
                        String userName = sc.nextLine();
                        System.out.println("Enter Password: ");
                        String password = sc.nextLine();

                        LogIn.LogInAdmin(userName, password);
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
            System.out.println("0. Exit");

            System.out.println("Enter your choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {


                //    Add books ------------------


                case 1: {
                    Administrator admin = (Administrator) Main.currentUser;
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

                    adminConstructor.addBook(title, author, genre, ISBN, year);
                    break;
                }
                //  Borrow book -----------------

                case 2: {

                    System.out.println("Enter Book title: ");
                    String bookTitle = sc.nextLine();
                    System.out.println("Enter Book Author: ");
                    String bookAuthor = sc.nextLine();
                    System.out.println("Enter the amount of days you want to borrow the book: ");
                    int nrOfDays = sc.nextInt();
                    sc.nextLine();
                    LocalDate borrowDate = LocalDate.now();
                    Customer customer = (Customer) Main.currentUser;
                    customer.borrowBook(bookTitle, bookAuthor, borrowDate, nrOfDays);

                    break;
                }

                //Return Book------------------
                case 3: {
                    System.out.println("Enter Book title: ");
                    String bookTitle = sc.nextLine();
                    System.out.println("Enter Book Author: ");
                    String bookAuthor = sc.nextLine();
                    Customer customer = (Customer) Main.currentUser;
                    customer.returnBook(bookTitle, bookAuthor);

                    break;
                }
                case 4: {
                    Administrator admin = (Administrator) Main.currentUser;
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
                    }
                    break;
                }


                //Filter -----------------


                case 6: {
                    System.out.println("1. Filter by genre");
                    System.out.println("2. Filter by year");
                    System.out.println("3. Filter by available");

                    if (sc.hasNextInt()) {
                        int filter = sc.nextInt();
                        sc.nextLine();

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

                    try (BufferedReader reader = new BufferedReader(new FileReader("books.txt"))) {
                        String line;

                        while ((line = reader.readLine()) != null) {


                            String[] parts = line.split(",");
                            if (parts[0].equalsIgnoreCase("Book title is: " + search)) {
                                System.out.println(line);
                            }

                        }
                    } catch (IOException e) {
                        System.out.println("File not found: " + e.getMessage());
                    }
                    break;
                }
                case 8: {
                    System.out.println(Main.currentUser);
                    break;
                }

                case 9: {
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

                    uChoice=sc.nextInt();
                    switch (uChoice) {
                        case 1: {
                            Administrator admin = (Administrator) Main.currentUser;
                            System.out.println("Enter new Book Title: ");
                            sc.nextLine();
                            String newTitle = sc.nextLine();
                            admin.updateBookTitle(bookTitle, bookAuthor, newTitle);
                            break;
                        }
                        case 2: {
                            Administrator admin = (Administrator) Main.currentUser;
                            System.out.println("Enter new Book Author: ");
                            sc.nextLine();
                            String newAuthor = sc.nextLine();
                            admin.updateBookAuthor(bookTitle, bookAuthor, newAuthor);
                            break;
                        }
                        case 3: {
                            Administrator admin = (Administrator) Main.currentUser;
                            System.out.println("Enter new Book Genre: ");
                            sc.nextLine();
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
                            System.out.println("Enter new Book Availability: ");
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

                    System.out.println("Enter user name: ");
                    String keyword = sc.nextLine();

                    try(BufferedReader br = new BufferedReader(new FileReader("customers.txt"))){
                        String line;

                        while((line = br.readLine()) != null){
                            String[] parts = line.split(",");

                            if(parts[0].equalsIgnoreCase("Username: " + keyword)){
                                System.out.println(line);
                            }
                        }
                    }catch(IOException e){
                        System.out.println("File not found: " + e.getMessage());
                    }

                    try(BufferedReader br = new BufferedReader(new FileReader("admins.txt"))){
                        String line;

                        while((line = br.readLine()) != null){
                            String[] parts = line.split(",");
                            if(parts[0].equalsIgnoreCase("Username: " + keyword)){
                                System.out.println(line);
                            }
                        }
                    }catch(IOException e){
                        System.out.println("File not found: " + e.getMessage());
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

    private ArrayList<Book> loadBooksFromFile() {
        ArrayList<Book> books = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("books.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");

                if (parts.length < 6) {
                    continue;
                }

                String title = parts[0].trim();
                String author = parts[1].trim();
                String isbn = parts[2].trim();
                String genre = parts[3].replace("Genre:", "").trim();
                int year = Integer.parseInt(parts[4].replaceAll("[^0-9]", ""));
                boolean isAvailable = parts[5].contains("true");

                books.add(new Book(title, author, isbn, genre, year, isAvailable));
            }
        } catch (IOException e) {
            System.out.println("Error reading library: " + e.getMessage());
        }
        return books;
    }
}
