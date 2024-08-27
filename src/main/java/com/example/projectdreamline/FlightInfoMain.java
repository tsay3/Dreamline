package com.example.projectdreamline;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;

// creating main application class
// to load the fxml file
public class FlightInfoMain extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("FlightInfoForm.fxml"));

        //Create background image
        Image backgroundImage = new Image(getClass().getResourceAsStream("background.jpg"));

        // Create background size
        BackgroundSize backgroundSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, false);

        // Create background image
        BackgroundImage backgroundImageObj = new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, null, backgroundSize);

        // Create background
        Background background = new Background(backgroundImageObj);

        // Wrap the root node in a StackPane and set the background
        StackPane stackPane = new StackPane(root);
        stackPane.setBackground(background);

        // Create scene
        Scene scene = new Scene(stackPane, 800, 600);

        primaryStage.setTitle("Track A Flight");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
