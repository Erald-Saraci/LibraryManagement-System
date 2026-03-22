import java.io.*;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Library {

    public ArrayList<Book> getAllBooks() {
        ArrayList<Book> bookList = new ArrayList<>();
        String sql = "SELECT Title, Author, Genre, ISBN, PublishYear, Availability FROM books";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String title = rs.getString("Title");
                String author = rs.getString("Author");
                String genre = rs.getString("Genre");
                String isbn = rs.getString("ISBN");
                int year = rs.getInt("PublishYear");
                boolean available = rs.getBoolean("Availability");
                bookList.add(new Book(title, author, genre, isbn, year, available));
            }

        } catch (SQLException e) {
            System.out.println("Error fetching books from database.");
            e.printStackTrace();
        }
        return bookList;
    }

    //Filter by Genre
    public ArrayList<Book> filterByGenre(String genre) {
        ArrayList<Book> results = new ArrayList<>();
        String sql = "SELECT Title, Author, Genre, ISBN, PublishYear, Availability FROM books WHERE Genre = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, genre);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                results.add(new Book(
                        rs.getString("Title"),
                        rs.getString("Author"),
                        rs.getString("Genre"),
                        rs.getString("ISBN"),
                        rs.getInt("PublishYear"),
                        rs.getBoolean("Availability")
                ));
            }

        } catch (SQLException e) {
            System.out.println("Error filtering books by genre.");
            e.printStackTrace();
        }
        return results;
    }


    //Filter by Year Range
    public ArrayList<Book> filterByYear(int startYear, int endYear) {
        ArrayList<Book> results = new ArrayList<>();
        String sql = "SELECT Title, Author, Genre, ISBN, PublishYear, Availability FROM books WHERE PublishYear BETWEEN ? AND ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, startYear);
            pstmt.setInt(2, endYear);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                results.add(new Book(
                        rs.getString("Title"),
                        rs.getString("Author"),
                        rs.getString("Genre"),
                        rs.getString("ISBN"),
                        rs.getInt("PublishYear"),
                        rs.getBoolean("Availability")
                ));
            }

        } catch (SQLException e) {
            System.out.println("Error filtering books by year.");
            e.printStackTrace();
        }
        return results;
    }

    public ArrayList<Book> getAvailableBooks() {
        ArrayList<Book> results = new ArrayList<>();
        String sql = "SELECT Title, Author, Genre, ISBN, PublishYear, Availability FROM books WHERE Availability = true";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                results.add(new Book(
                        rs.getString("Title"),
                        rs.getString("Author"),
                        rs.getString("Genre"),
                        rs.getString("ISBN"),
                        rs.getInt("PublishYear"),
                        rs.getBoolean("Availability")
                ));
            }

        } catch (SQLException e) {
            System.out.println("Error fetching available books.");
            e.printStackTrace();
        }
        return results;
    }
}