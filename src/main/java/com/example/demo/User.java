package com.example.demo;

import javafx.fxml.Initializable;

import java.io.Serializable;

public class User implements Serializable{
    String username, password, firstName, lastName;
    Boolean isStudent;
    int id;

    public User(int id, String firstName, String lastName, String username, String password, boolean isStudent) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.isStudent = isStudent;
    }
}
