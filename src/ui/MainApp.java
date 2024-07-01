package ui;

import database.repositories.PostgreSqlDataBase;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Initialize the database
        PostgreSqlDataBase database = new PostgreSqlDataBase("localhost", "postgres", "postgres", "12345", 5432);
        database.initialize();

        Parent root = FXMLLoader.load(getClass().getResource("/main.fxml"));
        primaryStage.setTitle("Medical Diary");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
