package com.example.demo;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

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
    private TableView<?> tblStudent;

    @FXML
    private TextField tfName;

    private Student student;
    private Database db = new Database();

    void setUser(User user) {
        this.student = (Student) user;

    }

    @FXML
    void initialize(){
        tfName.setEditable(false);
        Platform.runLater(()->{
            tfName.setText(student.lastName + ", " + student.firstName);
            ResultSet rs = db.executeQueryWithResultSet("SELECT * from student WHERE uid = ?", student.id);
            while(true){
                try {
                    if (!rs.next()) break;
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                try {
                    rs.getInt("cid");
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
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
