package de.mrbaam.nasrt.main;

/**
 * Created by mrbaam on 24.07.2015.
 * @author mrbaam
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class Start extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        final FXMLLoader loader;
        final Parent     root;
        final Scene      scene;

        loader = new FXMLLoader(getClass().getResource("/MainPane.fxml"));
        root   = loader.load();
        scene  = new Scene(root, 800, 600);

        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

        primaryStage.setTitle("NAS RefactorTool");
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/logo.png")));
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
