package com.example.demo;

import java.util.ArrayList;
import java.util.HashMap;

public class Student extends User{
    ArrayList<CourseGrade> courseGrades = new ArrayList<CourseGrade>();

    public Student(int id, String firstName, String lastName, String username, String password, boolean isStudent) {
        super(id, firstName, lastName, username, password, isStudent);
    }
}
