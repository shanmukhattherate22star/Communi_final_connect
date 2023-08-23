package com.example.communiconnect;

public class Users {
    private String email;
   // private String id;
    private String username;

    public void setEmail(String email) {
        this.email = email;
    }

    //public void setId(String id) {
   //     this.id = id;
    //}

    public void setUsername(String username) {
        this.username = username;
    }

    public Users() {
        // Default constructor required for Firebase

    }

    public Users(String email, String username) {
        this.email = email;
      //  this.id = id;
        this.username = username;
    }

    public String getFirstName() {

        return email;
    }

//    public String getLastName() {
//        return id;
//    }

    public String getAge() {
        return username;
    }
}