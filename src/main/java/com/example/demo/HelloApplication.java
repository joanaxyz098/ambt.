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
    @Override
    public void start(Stage stage) throws IOException {
        File file = new File("user.txt");
        User u = null;
        FXMLLoader fxmlLoader = null;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));) {
            u = (User) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println(e.getClass());
        }

        if(!file.exists() || u == null) {
            fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login.fxml"));
        } else {
            if(u.isStudent) {
                fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("student.fxml"));
                fxmlLoader.load();
                StudentController studentController = fxmlLoader.getController();
                studentController.setUser(u);
            }else{
                fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("teacher.fxml"));
                fxmlLoader.load();
            }
        }

        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}