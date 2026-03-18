import org.mindrot.jbcrypt.BCrypt;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LogIn {

    public LogIn(){}

    //LogIn method---------------

    public static void LogInCustomer(String userName, String password) {
        boolean found = false;

        try (BufferedReader br = new BufferedReader(new FileReader("customers.txt"))) {
            String line;

            while ((line = br.readLine()) != null) {

                if (line.trim().isEmpty() || !line.contains(",")) {
                    continue;
                }

                String[] parts = line.split(",");

                String CName = parts[0].split(":")[1].trim();
                String CPass = parts[1].split(":")[1].trim();



                if (CName.equals(userName) && BCrypt.checkpw(password, CPass)) {
                    found = true;
                    String CEmail = parts[2].split(":")[1].trim();
                    String CPnumber = parts[3].split(":")[1].trim();
                    String CcustomerID = parts[4].split(":")[1].trim();
                    String CMembershipID = parts[5].split(":")[1].trim();
                    String CMembershiptType = parts[6].split(":")[1].trim();
                    String CRole = parts[7].split(":")[1].trim();

                    Main.currentUser = new Customer(CName, CPass, CEmail, CcustomerID, CPnumber, CMembershipID, CMembershiptType,CRole);

                    String[] parts2 = parts[0].split(":");
                    System.out.println("Logged in! Welcome back: " + parts2[1].trim());
                    break;

                }

            }
            if(!found){
                System.out.println("Invalid username or password");
            }
        }catch (IOException e) {
            System.out.println("File not found!");
        }
    }

    public static void LogInAdmin(String userName, String password) {
        boolean found = false;
        try(BufferedReader br = new BufferedReader(new FileReader("admins.txt"))){
            String line;

            while((line = br.readLine()) != null){

                if (line.trim().isEmpty() || !line.contains(",")) {
                    continue;
                }

                String[] parts = line.split(",");

                String CName = parts[0].split(":")[1].trim();
                String CPass = parts[1].split(":")[1].trim();

                if(CName.equals(userName) && BCrypt.checkpw(password, CPass)){
                    found = true;
                    String CEmail = parts[2].split(":")[1].trim();
                    String CAdminID = parts[3].split(":")[1].trim();
                    String CStatus = parts[4].split(":")[1].trim();
                    Main.currentUser = new Administrator(CName, CPass, CEmail, CAdminID, CStatus);

                    String[] parts2 = parts[0].split(":");
                    System.out.println("Logged in! Welcome back: " + parts2[1].trim());
                    break;
                }

            }
            if(!found) {
                System.out.println("Invalid username or password");
            }
        }catch(IOException e){
            System.out.println("File not found!");
        }
    }

    //Log Out method---------------

    public static void LogOut(){
        Main.currentUser = null;
    }
}
