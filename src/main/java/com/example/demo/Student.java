package com.example.demo;

public class Student extends User{
    int grade;
    Course[] courses;

    public Student(int id, String firstName, String lastName, String username, String password, boolean isStudent) {
        super(id, firstName, lastName, username, password, isStudent);
    }
}
