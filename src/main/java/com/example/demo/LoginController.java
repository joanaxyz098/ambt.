package com.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

import javax.swing.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.security.Key;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginController {

    @FXML
    private TextField tfPassword;

    @FXML
    private TextField tfUsername;
    @FXML
    private Label lblError;

    private Database db = new Database();
    @FXML
    void initialize() {
//       db.executeQuery("INSERT INTO user(firstname, lastname, username, password, isStudent) VALUES (?, ?, ?, ?, ?)", "Elton", "John", "ej123", "123456", true);
    }
    @FXML
    void onSignIn(ActionEvent event) throws SQLException, IOException {
        String username = tfUsername.getText();
        String password = tfPassword.getText();
        ResultSet rs = db.executeQueryWithResultSet("SELECT * from user WHERE username = '" + username + "'");
        User e = null;

        FXMLLoader fxmlLoader = null;
        while(rs.next()){
            e = new User(rs.getInt("id"), rs.getString("firstName"), rs.getString("lastName"),
                    rs.getString("username"), rs.getString("password"), rs.getBoolean("isStudent"));
        }
        if(e != null && (e.username.equals(username) && e.password.equals(password))){
            if (e.isStudent) {
                fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("student.fxml"));
                // Load the FXML and inject the controller
                Parent root = fxmlLoader.load();
                StudentController studentController = fxmlLoader.getController();
                studentController.setUser(e);
                Scene scene = ((Node) event.getSource()).getScene();
                scene.setRoot(root);
            } else {
                fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("teacher.fxml"));
                Parent root = fxmlLoader.load();
                TeacherController teacherController = fxmlLoader.getController();
                teacherController.setUser(e);
                Scene scene = ((Node) event.getSource()).getScene();
                scene.setRoot(root);
            }

            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("user.txt"));) {
                oos.writeObject(e);
            } catch (IOException ex) {
                System.err.println(ex.getClass());
            }

        }else{
             lblError.setVisible(true);
        }
    }

    @FXML
    void onFieldsKeyPressed(){
        lblError.setVisible(false);
    }

}
