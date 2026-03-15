import java.io.*;
import java.util.UUID;

public class UserRegistration {
    private boolean Passed;

    public UserRegistration(){}


    Administrator admin = new Administrator();


    public void registerCustomer(String userName, String password, String email, String phoneNumber, String membershipType){
        boolean usernameTaken = false;

        try(BufferedReader reader = new BufferedReader(new FileReader("customers.txt"))){
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] parts = line.split(",");

                if (parts.length > 0 && parts[0].contains(":")) {
                    String fileUser = parts[0].split(":")[1].trim();

                    if (fileUser.equals(userName)) {
                        usernameTaken = true;
                        break;
                    }
                }
            }
        } catch(IOException e){
            System.out.println("File not found (First user?): " + e.getMessage());
        }

        if(usernameTaken){
            System.out.println("Username already taken!\n");
        } else {
            String customerID = UUID.randomUUID().toString();
            String membershipID = UUID.randomUUID().toString();

            Customer customer = new Customer(userName, password, email, phoneNumber, customerID, membershipID, membershipType);

            try (BufferedWriter writer = new BufferedWriter(new FileWriter("customers.txt", true))) {
                writer.write(customer.toString());
                writer.newLine();
                System.out.println(customer.toString() + "\n");
            } catch (IOException e) {
                System.out.println("File not found: " + e.getMessage());
            }
        }
    }


    public void registerAdmin(String userName, String password, String email, String phoneNumber){
        boolean usernameTaken = false;


        try(BufferedReader reader = new BufferedReader(new FileReader("admins.txt"))){
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(",");
                if (parts.length > 0 && parts[0].contains(":")) {
                    String fileUser = parts[0].split(":")[1].trim();

                    if (fileUser.equals(userName)) {
                        usernameTaken = true;
                        break;
                    }
                }
            }
        } catch(IOException e){
            System.out.println("There is something wron with the file: " + e.getMessage());
        }


        if(usernameTaken){
            System.out.println("Admin Username already taken!\n");
        } else {
            String adminId = UUID.randomUUID().toString();

            Administrator admin = new Administrator(userName, password, email, phoneNumber, adminId);

            try (BufferedWriter br = new BufferedWriter(new FileWriter("admins.txt", true))) {
                br.write(admin.toString());
                br.newLine();
                System.out.println(admin.toString() + "\n");
            } catch (IOException e) {
                System.out.println("File not found: " + e.getMessage());
            }
        }
    }

    public void checkAdminPass(String adminPass){
        if(adminPass.equals(admin.getAdminPass())){
            this.Passed = true;
        } else {
            this.Passed = false;
            System.out.println("Admin Password Mismatch!\n");
        }
    }

    public boolean getPassed(){
        return Passed;
    }
}