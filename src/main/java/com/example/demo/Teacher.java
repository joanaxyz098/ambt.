package com.example.demo;

import java.util.ArrayList;

public class Teacher extends User {
    ArrayList<Course> courses = new ArrayList<Course>();
    public Teacher(int id, String firstName, String lastName, String username, String password, boolean isStudent) {
        super(id, firstName, lastName, username, password, isStudent);
    }
}
