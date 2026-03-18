

public abstract class User {
    protected String userName;
    protected String hashedPass;
    protected String email;
    protected String phoneNumber;
    protected String role;



    //Constructors---------
    public User(){}

    public User(String userName, String hashedPass, String email, String phoneNumber){
        this.userName = userName;
        this.hashedPass = hashedPass;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }



    //Getters------------
    public String getUserName(){
        return userName;
    }

    public String getEmail(){
        return email;
    }

    public String getPhoneNumber(){
        return phoneNumber;
    }

    public String getRole(){
        return role;
    }

    public String getPasswordHash() {
        return hashedPass;
    }
    //Setters------------
    public void setUserName(String userName){
        this.userName = userName;
    }


    public void setEmail(String email){
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


    @Override
    public String toString(){
        return "Username: " + userName + "," + " Password: " + hashedPass +  "," + " Email: " + email + "," + " Phone Number: " + phoneNumber;
    }
}
