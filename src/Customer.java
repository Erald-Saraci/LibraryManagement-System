import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class Customer extends User{
    private String customerID;
    private String MembershipID;
    private String membershipType;
    private int maxBooks;


    ArrayList<String> lines = new ArrayList<>();

    //Constructors--------------
    public Customer(){

    }

    public Customer(String userName, String hashedpass, String email, String phoneNumber, String customerID, String MembershipID, String membershipType){
        super(userName, hashedpass, email, phoneNumber);
        this.customerID = customerID;
        this.MembershipID = MembershipID;
        this.membershipType = membershipType;
        this.role = "Customer";

        if (this.membershipType.equalsIgnoreCase("Premium")) {
            this.maxBooks = 5;
        } else {
            this.maxBooks = 2;
        }
    }

    public Customer(String userName, String hashedpass, String email, String phoneNumber, String customerID, String MembershipID, String membershipType,String role){
        super(userName, hashedpass, email, phoneNumber);
        this.customerID = customerID;
        this.MembershipID = MembershipID;
        this.membershipType = membershipType;
        this.role = role;

        if (this.membershipType.equalsIgnoreCase("Premium")) {
            this.maxBooks = 5;
        } else {
            this.maxBooks = 2;
        }
    }
    //Getters-------------------

    public String getCustomerID(){
        return customerID;
    }

    public String getMembershipID() {
        return MembershipID;
    }

    public int getCurrentBorrowedCount() {
        int count = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader("borrowed.txt"))) {
            String line;

            while ((line = reader.readLine()) != null) {

                if (line.contains(this.getUserName())) {
                    count++;
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading borrowed file to count books.");
        }
        return count;
    }

    //Setters-------------------
    public void setCustomerID(String customerID){
        this.customerID = customerID;
    }



    public void borrowBook(String title, String author, LocalDate borrowDate, int nrOfDays) {

        int currentBooks = getCurrentBorrowedCount();
        if (currentBooks >= this.maxBooks) {
            System.out.println("Limit reached! You have a " + this.membershipType + " membership.");
            return;
        }

        this.lines.clear();
        boolean bookFoundAndBorrowed = false;
        LocalDate dueDate = borrowDate.plusDays(nrOfDays);

        try (BufferedReader reader = new BufferedReader(new FileReader("books.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {


                if (!bookFoundAndBorrowed && line.contains(title) && line.contains(author)) {

                    if (line.contains("Availability: true")) {

                        line = line.replace("Availability: true", "Availability: false");

                        bookFoundAndBorrowed = true;

                    } else if (line.contains("Availability: false")) {

                    }
                }
                lines.add(line);
            }
        } catch (IOException e) {
            System.out.println("Error reading books file: " + e.getMessage());
        }

        if (bookFoundAndBorrowed) {

            try (BufferedWriter writer = new BufferedWriter(new FileWriter("books.txt"))) {
                for (String wline : lines) {
                    writer.write(wline);
                    writer.newLine();
                }
            } catch (IOException e) {
                System.out.println("Error writing books.txt");
            }


            try (BufferedWriter writer = new BufferedWriter(new FileWriter("borrowed.txt", true))) {

                String userName;
                if (Main.currentUser != null) {
                    userName = Main.currentUser.getUserName();
                } else {
                    userName = "Unknown";
                }

                String borrowedLine = "Title: " + title
                        + ", Author: " + author
                        + ", Borrowed Date: " + borrowDate
                        + ", Return Date: " + dueDate
                        + ", MemID: " + this.MembershipID
                        + ", Username: " + userName;

                writer.write(borrowedLine);
                writer.newLine();

            } catch (IOException e) {
                System.out.println("Error writing to borrowed.txt");
            }

            if (Main.currentUser != null) {
                System.out.println(Main.currentUser.getUserName() + " (ID: " + this.MembershipID + ") Borrowed successfully!");
            }
        } else {
            System.out.println("Book not found or is currently unavailable.");
        }
    }

    public void returnBook(String title, String author) {
        this.lines.clear();
        boolean bookReturnedInInventory = false;

        try (BufferedReader reader = new BufferedReader(new FileReader("books.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {

                if (!bookReturnedInInventory && line.contains(title) && line.contains(author)) {

                    if (line.contains("Availability: false")) {
                        line = line.replace("Availability: false", "Availability: true");
                        bookReturnedInInventory = true;
                    }
                }
                lines.add(line);
            }
        } catch (IOException e) {
            System.out.println("Error reading books.txt: " + e.getMessage());
            return;
        }

        if (bookReturnedInInventory) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("books.txt"))) {
                for (String wline : lines) {
                    writer.write(wline);
                    writer.newLine();
                }
            } catch (IOException e) {
                System.out.println("Error writing to books.txt");
            }

            ArrayList<String> borrowedLines = new ArrayList<>();
            boolean foundInBorrowedFile = false;

            try (BufferedReader reader = new BufferedReader(new FileReader("borrowed.txt"))) {
                String line;
                while ((line = reader.readLine()) != null) {

                    boolean isMyBook = line.contains(title) &&
                            line.contains(author) &&
                            line.contains(this.MembershipID);

                    if (isMyBook) {
                        foundInBorrowedFile = true;
                    } else {
                        borrowedLines.add(line);
                    }
                }
            } catch (IOException e) {
                System.out.println("Error reading borrowed.txt");
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter("borrowed.txt"))) {
                for (String wline : borrowedLines) {
                    writer.write(wline);
                    writer.newLine();
                }
            } catch (IOException e) {
                System.out.println("Error updating borrowed.txt");
            }

            if (Main.currentUser != null) {
                if (foundInBorrowedFile) {
                    System.out.println(Main.currentUser.getUserName() + " returned the book successfully!");
                } else {
                    System.out.println("Book returned to shelf, but no record found in borrowed log.");
                }
            }

        } else {
            System.out.println("Could not return book. It is already on the shelf or not found.");
        }
    }



    public void requestExtension(String bookTitle) {
        ArrayList<String> lines = new ArrayList<>();
        boolean found = false;
        boolean updated = false;

        System.out.println("Processing extension request for: " + bookTitle);

        try (BufferedReader reader = new BufferedReader(new FileReader("borrowed.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                if (line.contains(this.getUserName()) && line.contains(bookTitle)) {

                    String[] parts = line.split(",");
                    String returnDateStr = null;
                    int returnDateIndex = -1;

                    for (int i = 0; i < parts.length; i++) {
                        if (parts[i].trim().startsWith("Return Date:")) {
                            returnDateStr = parts[i].split(":")[1].trim();
                            returnDateIndex = i;
                            break;
                        }
                    }

                    if (returnDateStr != null) {
                        try {
                            LocalDate currentReturnDate = LocalDate.parse(returnDateStr);

                            if (LocalDate.now().isAfter(currentReturnDate)) {
                                System.out.println("Request Denied: You cannot extend an overdue book. Please return it and pay the fine.");
                                lines.add(line);
                            }

                            else if (isBookReserved(bookTitle)) {
                                System.out.println("Request Denied: Another user has reserved this book.");

                                lines.add(line);
                            }
                            else {

                                LocalDate newDate = currentReturnDate.plusDays(3);

                                parts[returnDateIndex] = " Return Date: " + newDate;

                                String newLine = String.join(",", parts);
                                lines.add(newLine);
                                updated = true;
                                System.out.println("Success! Return date extended by 3 days to: " + newDate);
                            }
                        } catch (Exception e) {
                            System.out.println("Error parsing date. keeping original record.");
                            lines.add(line);
                        }
                    } else {
                        lines.add(line);
                    }
                    found = true;
                } else {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading borrowed.txt");
        }

        if (updated) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("borrowed.txt"))) {
                for (String l : lines) {
                    writer.write(l);
                    writer.newLine();
                }
            } catch (IOException e) {
                System.out.println("Error updating file.");
            }
        } else if (!found) {
            System.out.println("You do not have this book borrowed.");
        }
    }


    private boolean isBookReserved(String bookTitle) {
        File file = new File("reservations.txt");

        if (!file.exists()) {
            return false;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                if (line.contains(bookTitle) && !line.contains("Status: Completed")) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.out.println("Error checking reservations.");
        }

        return false;
    }


    @Override
    public String toString(){
        return super.toString() + "," + " Customer ID: " + customerID + "," +"Membership ID: "+ MembershipID+ "," + "Membership Type: " + membershipType + ","+ "Status: " + role;
    }

}
