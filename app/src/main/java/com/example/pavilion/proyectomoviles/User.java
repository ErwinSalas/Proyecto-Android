package com.example.pavilion.proyectomoviles;

/**
 * Created by Pavilion on 17/4/2017.
 */
public class User {
    private static User instance = null;

    private String name, lastName, email, password;

    private User(){
        this.email = "";
        this.name = "";
        this.lastName = "";
        this.password = "";
    }

    public void UserFillInformation(String email, String name, String lastName, String password){
        this.setEmail(email);
        this.setName(name);
        this.setLastName(lastName);
        this.setPassword(password);
    }

    public static User getInstance(){
        if(instance == null)
        {
            instance = new User();
        }
        return instance;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
