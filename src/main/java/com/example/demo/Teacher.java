package com.example.demo;

public class Teacher extends User {
    Course[] coursesHandled;

    public Teacher(int id, String firstName, String lastName, String username, String password, boolean isStudent) {
        super(id, firstName, lastName, username, password, isStudent);
    }
}
