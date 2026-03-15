public abstract class User {
    protected String userName;
    protected String password;
    protected String email;
    protected String phoneNumber;
    protected String role;



    //Constructors---------
    public User(){}

    public User(String userName, String password, String email, String phoneNumber){
        this.userName = userName;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    //Getters------------
    public String getUserName(){
        return userName;
    }

    public String getPassword(){
        return password;
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
    //Setters------------
    public void setUserName(String userName){
        this.userName = userName;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


    @Override
    public String toString(){
        return "Username: " + userName + "," + " Password: " + password + "," + " Email: " + email + "," + " Phone Number: " + phoneNumber;
    }
}
