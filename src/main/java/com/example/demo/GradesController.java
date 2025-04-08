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
import org.w3c.dom.Text;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class GradesController {

    @FXML
    private Button btnLogout;

    @FXML
    private Label lblName;

    @FXML
    private TableView<StudentGrade> tblGrades;

    @FXML
    private TableColumn<StudentGrade, String> lastNameColumn;

    @FXML
    private TableColumn<StudentGrade, String> firstNameColumn;

    @FXML
    private TableColumn<StudentGrade, Void> gradeColumn;

    @FXML
    private TextField tfSubject;

    @FXML
    private TextField tfName;

    private Teacher teacher;
    private User user;
    private Database db = new Database();
    private ArrayList<Student> students;
    private int id;

    void setUser(User user) {
        this.user = user;
        teacher = new Teacher(user.id, user.firstName, user.lastName, user.username, user.password, user.isStudent);
    }

    void setCourseId(int id) {
        this.id = id;
    }

    @FXML
    void initialize() {
        tfName.setEditable(false);
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        gradeColumn.setCellValueFactory(new PropertyValueFactory<>("grade"));


        Platform.runLater(() -> {
            System.out.println("id " + id);
            tfName.setText(teacher.lastName + ", " + teacher.firstName);

            String queryS = """
                SELECT u.firstname, u.lastname, s.grade, c.coursecode, c.coursename
                FROM student s
                JOIN course c ON s.cid = c.id
                JOIN user u ON u.id = s.uid
                WHERE s.cid = ?
            """;

            ResultSet students = db.executeQueryWithResultSet(queryS, id);

            ObservableList<StudentGrade> data = FXCollections.observableArrayList();

            try {

                while (students.next()) {
                    String courseCode = students.getString("coursecode");
                    String courseName = students.getString("coursename");
                    tfSubject.setText(courseCode + ", " + courseName);
                    String firstName = students.getString("firstname");
                    String lastName = students.getString("lastname");
                    double grade = students.getDouble("grade");
                    data.add(new StudentGrade(firstName, lastName, grade));
//                    gradeColumn.setCellFactory(col -> new TableCell<StudentGrade, Void>() {
//                        private final TextField tf = new TextField(Double.toString(grade));
//                        {
//
//                        }
//
//                        @Override
//                        protected void updateItem(Void item, boolean empty) {
//                            super.updateItem(item, empty);
//                            setGraphic(empty ? null : tf);
//                        }
//                    });

                }

                tblGrades.setItems(data);

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

    }

    @FXML
    void onSave(ActionEvent event) {

    }


    @FXML
    void onLogout(ActionEvent event) throws IOException {
        teacher = null;
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("user.txt"));) {
            oos.writeObject(teacher);
        } catch (IOException ex) {
            System.err.println(ex.getClass());
        }
        FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
        Parent root = loader.load();
        Scene scene = ((Node) event.getSource()).getScene();
        scene.setRoot(root);
    }

}
