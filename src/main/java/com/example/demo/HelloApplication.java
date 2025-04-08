package com.example.demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class HelloApplication extends Application {
    public void start(Stage stage) throws IOException {
        File file = new File("user.txt");
        User u = null;
        FXMLLoader fxmlLoader = null;

        // Attempt to read the user from file
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            u = (User) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println(e.getClass());
        }

        // Check if the file doesn't exist or the user is null
        if (!file.exists() || u == null) {
            fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root, 320, 240);
            stage.setScene(scene);
        } else {
            // User is logged in, determine their role

            if (u.isStudent) {
                fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("student.fxml"));
                // Load the FXML and inject the controller
                Parent root = fxmlLoader.load();
                StudentController studentController = fxmlLoader.getController();
                studentController.setUser(u);
                // Set the scene with the loaded FXML
                Scene scene = new Scene(root, 320, 240);
                stage.setTitle("Student Dashboard");
                stage.setScene(scene);
            } else {
                fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("teacher.fxml"));
                Parent root = fxmlLoader.load();
                TeacherController teacherController = fxmlLoader.getController();
                teacherController.setUser(u);
                Scene scene = new Scene(root, 320, 240);
                stage.setTitle("Teacher Dashboard");
                stage.setScene(scene);
            }
        }

        // Show the stage
        stage.show();
    }


    public static void main(String[] args) {
        launch();
    }
}