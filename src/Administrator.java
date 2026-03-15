import java.io.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.time.LocalDate;
import java.io.*;

public class Administrator extends User{
    private String adminID;
    private String adminPass = "admin123";

    public void calculateOverdueFines() {
        double finePerDay = 0.50;
        boolean foundOverdue = false;
        LocalDate today = LocalDate.now();

        System.out.println("Current Date: " + today);

        try (BufferedReader reader = new BufferedReader(new FileReader("borrowed.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                String returnDateString = null;
                String username = "Unknown";
                String title = "Unknown";

                for (String part : parts) {
                    String p = part.trim();
                    if (p.startsWith("Return Date:")) {
                        returnDateString = p.substring("Return Date:".length()).trim();
                    } else if (p.startsWith("Username:")) {
                        username = p.substring("Username:".length()).trim();
                    } else if (p.startsWith("Title:")) {
                        title = p.substring("Title:".length()).trim();
                    }
                }

                if (returnDateString != null) {
                    try {
                        LocalDate returnDate = LocalDate.parse(returnDateString);

                        if (today.isAfter(returnDate)) {
                            long daysOverdue = today.toEpochDay() - returnDate.toEpochDay();
                            double totalFine = daysOverdue * finePerDay;

                            System.out.println("User: " + username);
                            System.out.println("Book: " + title);
                            System.out.println("Due Date: " + returnDate);
                            System.out.println("Days Overdue: " + daysOverdue);
                            System.out.println("Amount: $" + totalFine);
                            foundOverdue = true;
                        }
                    } catch (Exception e) {
                        System.out.println("Error parsing date for line: " + line);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading borrowed.txt: " + e.getMessage());
        }

        if (!foundOverdue) {
            System.out.println("Good news! No books are currently overdue.");
        }
    }




    public Administrator(){
    }

    public Administrator(String userName, String password, String email, String phoneNumber, String adminID){
        super(userName, password, email, phoneNumber);
        this.adminID= adminID;
        this.role = "Administrator";
    }

    public Administrator(String userName, String password, String email, String phoneNumber, String adminID, String role){
        super(userName,password,email,phoneNumber);
        this.adminID = adminID;
        this.role = role;
    }


    public String getAdminID(){
        return adminID;
    }

    public String getUserName(){
        return userName;
    }

    public String getAdminPass(){
        return adminPass;
    }



    public void setAdminID(String employeeID){
        this.adminID = employeeID;
    }

    public void setAdminPass(String password){
        this.adminPass = password;
    }

    public void addBook(String title, String author, String genre, String ISBN, int year) {

        try (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader("books.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {

                if (line.contains("ISBN is: " + ISBN)) {

                    System.out.println("Error: A book with ISBN " + ISBN + " already exists!");

                    return;

                }
            }
        } catch (java.io.IOException e) {

            System.out.println("Error reading books file.");

        }


        try (java.io.BufferedWriter writer = new java.io.BufferedWriter(new java.io.FileWriter("books.txt", true))) {


            String entry = "Book title is: " + title +
                    ", Author is: " + author +
                    ", ISBN is: " + ISBN +
                    ", Genre: " + genre +
                    ", Published in: " + year +
                    ", Availability: true";

            writer.write(entry);

            writer.newLine();

            System.out.println("Success: Book '" + title + "' added.");

        } catch (java.io.IOException e) {
            System.out.println("Error writing to books file.");
        }
    }

    private void UpdateHandler(String targetTitle, String targetAuthor, String keyword, String newValue) {
        ArrayList<String> lines = new ArrayList<>();
        boolean found = false;

        try (BufferedReader br = new BufferedReader(new FileReader("books.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                if (line.contains(targetTitle) && line.contains(targetAuthor)) {

                    String[] parts = line.split(",");
                    StringBuilder newLine = new StringBuilder();

                    for (int i = 0; i < parts.length; i++) {
                        String part = parts[i].trim();

                        if (part.startsWith(keyword)) {
                            newLine.append(newValue);
                        } else {
                            newLine.append(part);
                        }

                        if (i < parts.length - 1) {
                            newLine.append(", ");
                        }
                    }

                    lines.add(newLine.toString());
                    found = true;
                    System.out.println("Keyword updated: " + keyword);

                } else {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
            return;
        }

        if (found) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("books.txt"))) {
                for (String wline : lines) {
                    writer.write(wline);
                    writer.newLine();
                }
            } catch (IOException e) {
                System.out.println("Error writing to books.txt");
            }
        } else {
            System.out.println("Book not found. Update failed.");
        }
    }



    public void updateBookTitle(String currentTitle, String author, String newTitle) {
        UpdateHandler(currentTitle, author, "Book title is:", "Book title is: " + newTitle);
    }

    public void updateBookAuthor(String title, String currentAuthor, String newAuthor) {
        UpdateHandler(title, currentAuthor, "Author is:", "Author is: " + newAuthor);
    }

    public void updateBookGenre(String title, String author, String newGenre) {
        UpdateHandler(title, author, "Genre:", "Genre: " + newGenre);
    }

    public void updateBookYear(String title, String author, int newYear) {
        UpdateHandler(title, author, "Published in:", "Published in: " + newYear);
    }

    public void updateBookAvailability(String title, String author, boolean newStatus) {
        UpdateHandler(title, author, "Availability:", "Availability: " + newStatus);
    }


    public void removeBook(String title, String author) {
        ArrayList<String> remainingBooks = new ArrayList<>();
        boolean found = false;

        try (BufferedReader br = new BufferedReader(new FileReader("books.txt"))) {
            String line;

            while ((line = br.readLine()) != null) {

                if (line.trim().isEmpty()) {
                    continue;
                }

                boolean isMatch = line.contains(title) && line.contains(author);

                if (isMatch) {
                    found = true;
                } else {
                    remainingBooks.add(line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
            return;
        }

        if (found) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("books.txt"))) {
                for (String wline : remainingBooks) {
                    writer.write(wline);
                    writer.newLine();
                }
                System.out.println("Success! Removed book: " + title);
            } catch (IOException e) {
                System.out.println("Error writing to books.txt");
            }
        } else {
            System.out.println("Book not found: " + title);
        }
    }

    private boolean isUserInFile(String filename, String username) {
        try (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(filename))) {
            String line;

            while ((line = reader.readLine()) != null) {

                if (line.contains("Username: " + username)) {

                    return true;

                }
            }

        } catch (java.io.IOException e) { }

        return false;
    }


    public void generateInvoice(String customerName, double amount) {

        if (isUserInFile("admins.txt", customerName)) {

            System.out.println("Error: Cannot issue an invoice to an Administrator!");

            return;

        }
        if (!isUserInFile("customers.txt", customerName)) {

            System.out.println("Error: Customer '" + customerName + "' not found in database.");

            return;
        }

        String invoiceID = "INV-" + System.currentTimeMillis();

        LocalDate date = LocalDate.now();

        String issuerName = (Main.currentUser != null) ? Main.currentUser.getUserName() : "Unknown Admin";

        System.out.println("       LIBRARY INVOICE               ");

        System.out.println("Invoice ID:   " + invoiceID);

        System.out.println("Date:         " + date);

        System.out.println("Issued By:    " + issuerName + " (Admin)");

        System.out.println("-------------------------------------");

        System.out.println("Bill To:      " + customerName);

        System.out.println("Description:  Subscription Payment");

        System.out.println("-------------------------------------");

        System.out.printf("TOTAL AMOUNT: $%.2f%n", amount);


        try (java.io.BufferedWriter writer = new java.io.BufferedWriter(new java.io.FileWriter("invoices.txt", true))) {


            String fileEntry = "Invoice ID: " + invoiceID +
                    ", Date: " + date +
                    ", Issuer: " + issuerName +
                    ", Customer: " + customerName +
                    ", Amount: " + amount;

            writer.write(fileEntry);

            writer.newLine();

            System.out.println(">> Record saved to 'invoices.txt'");

        } catch (java.io.IOException e) {
            System.out.println("Error saving invoice to file.");
        }
    }


    @Override
    public String toString(){
        return super.toString() + "," + " Admin ID: " + adminID + "," + " Status: " + role;
    }
}
