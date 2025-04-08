package com.example.demo;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentController {

    @FXML
    private Button btnLogout;

    @FXML
    private Label lblName;

    @FXML
    private TableView<CourseGrade> tblStudent;

    @FXML
    private TableColumn<CourseGrade, String> courseCodeColumn;

    @FXML
    private TableColumn<CourseGrade, String> courseNameColumn;

    @FXML
    private TableColumn<CourseGrade, Double> gradesColumn;

    @FXML
    private TextField tfName;

    private Student student;
    private User user;
    private Database db = new Database();

    void setUser(User user) {
        this.user = user;
        student = new Student(user.id, user.firstName, user.lastName, user.username, user.password, user.isStudent);
    }

    @FXML
    void initialize() {
        tfName.setEditable(false);
        courseCodeColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getCourse().getCourseCode())
        );

        courseNameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getCourse().getCourseName())
        );

        gradesColumn.setCellValueFactory(new PropertyValueFactory<>("grade"));

        Platform.runLater(() -> {
            tfName.setText(student.lastName + ", " + student.firstName);

            String query = """
            SELECT c.coursecode, c.coursename, s.grade 
            FROM student s 
            JOIN course c ON s.cid = c.id 
            WHERE s.uid = ?
        """;

            ResultSet rs = db.executeQueryWithResultSet(query, student.id);

            try {
                while (rs.next()) {
                    String courseCode = rs.getString("coursecode");
                    String courseName = rs.getString("coursename");
                    int id = rs.getInt("cid");
                    double grade = rs.getDouble("grade");
                    student.courseGrades.add(new CourseGrade(new Course(courseCode, courseName, id), grade));
                }
                ObservableList<CourseGrade> data = FXCollections.observableArrayList(student.courseGrades);
                tblStudent.setItems(data);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

    }


    @FXML
    void onLogout(ActionEvent event) throws IOException {
        student = null;
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("user.txt"));) {
            oos.writeObject(student);
        } catch (IOException ex) {
            System.err.println(ex.getClass());
        }
        FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
        Parent root = loader.load();
        Scene scene = ((Node) event.getSource()).getScene();
        scene.setRoot(root);
    }

}
