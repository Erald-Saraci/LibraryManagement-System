import javafx.application.Application;
import java.io.File;
import java.io.IOException;

public class Main {


    public static User currentUser = null;

    public static void main(String[] args) {
        initializeFiles();

        Application.launch(GUI.class, args);
    }

    private static void initializeFiles() {
        String[] files = {"customers.txt", "admins.txt", "books.txt", "borrowed.txt"};

        for (String fileName : files) {
            File file = new File(fileName);
            try {
                if (file.createNewFile()) {
                    System.out.println("New file created: " + fileName);
                }
            } catch (IOException e) {
                System.out.println("Error creating file " + fileName);
            }
        }
    }
}