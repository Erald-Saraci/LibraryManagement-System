import java.io.*;
import java.util.ArrayList;

public class Library {

    public ArrayList<Book> getAllBooks() {
        ArrayList<Book> bookList = new ArrayList<>();

        try (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader("books.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {

                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(",");


                if (parts.length >= 6) {
                    try {

                        String title = parts[0].substring(parts[0].indexOf(":") + 1).trim();
                        String author = parts[1].substring(parts[1].indexOf(":") + 1).trim();
                        String isbn = parts[2].substring(parts[2].indexOf(":") + 1).trim();
                        String genre = parts[3].substring(parts[3].indexOf(":") + 1).trim();


                        String yearStr = parts[4].substring(parts[4].indexOf(":") + 1).trim();
                        int year = Integer.parseInt(yearStr);

                        String availStr = parts[5].substring(parts[5].indexOf(":") + 1).trim();
                        boolean isAvailable = Boolean.parseBoolean(availStr);


                        bookList.add(new Book(title, author, genre, isbn, year, isAvailable));

                    } catch (Exception e) {
                        System.out.println("Skipping corrupted line: " + line);
                    }
                }
            }
        } catch (java.io.IOException e) {
            System.out.println("Error reading library file.");
        }
        return bookList;
    }

    //Filter by Genre
    public ArrayList<Book> filterByGenre(String genre) {
        ArrayList<Book> allBooks = getAllBooks();
        ArrayList<Book> results = new ArrayList<>();

        for (Book b : allBooks) {
            if (b.getGenre().equalsIgnoreCase(genre)) {
                results.add(b);
            }
        }
        return results;
    }

    //Filter by Year Range
    public ArrayList<Book> filterByYear(int startYear, int endYear) {
        ArrayList<Book> allBooks = getAllBooks();
        ArrayList<Book> results = new ArrayList<>();

        for (Book b : allBooks) {
            if (b.getPublicationYear() >= startYear && b.getPublicationYear() <= endYear) {
                results.add(b);
            }
        }
        return results;
    }

    public ArrayList<Book> getAvailableBooks() {
        ArrayList<Book> allBooks = getAllBooks();
        ArrayList<Book> results = new ArrayList<>();

        for (Book b : allBooks) {
            if (b.isAvailable()) {
                results.add(b);
            }
        }
        return results;
    }
}