package com.example.demo;

import javafx.application.Platform;
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

public class TeacherController {

    @FXML
    private Button btnLogout;

    @FXML
    private Label lblName;

    @FXML
    private TableView<Course> tblteacher;

    @FXML
    private TableColumn<Course, String> courseCodeColumn;

    @FXML
    private TableColumn<Course, String> courseNameColumn;

    TableColumn<Course, Void> actionColumn = new TableColumn<>("Action");


    @FXML
    private TextField tfName;

    private Teacher teacher;
    private User user;
    private Database db = new Database();

    void setUser(User user) {
        this.user = user;
        teacher = new Teacher(user.id, user.firstName, user.lastName, user.username, user.password, user.isStudent);
    }

    @FXML
    void initialize() {
        tfName.setEditable(false);
        courseCodeColumn.setCellValueFactory(new PropertyValueFactory<>("courseCode"));
        courseNameColumn.setCellValueFactory(new PropertyValueFactory<>("courseName"));

        Platform.runLater(() -> {
            tfName.setText(teacher.lastName + ", " + teacher.firstName);

            String query = """
            SELECT c.coursecode, c.coursename
            FROM teacher s 
            JOIN course c ON s.cid = c.id 
            WHERE s.uid = ?
        """;

            ResultSet rs = db.executeQueryWithResultSet(query, teacher.id);

            try {
                while (rs.next()) {
                    String courseCode = rs.getString("coursecode");
                    String courseName = rs.getString("coursename");
                    teacher.courses.add(new Course(courseCode, courseName));
                }

                ObservableList<Course> data = FXCollections.observableArrayList(teacher.courses);

                actionColumn.setCellFactory(col -> new TableCell<Course, Void>() {
                    private final Button btn = new Button("View");
                    {
                        btn.setOnAction(event -> {

                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                });

                tblteacher.setItems(data);
                tblteacher.getColumns().add(actionColumn);

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

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
