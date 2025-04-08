package com.example.demo;

public class Course {
    private String courseCode;
    private String courseName;
    private int id;

    public Course(String courseCode, String courseName, int id) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
}
