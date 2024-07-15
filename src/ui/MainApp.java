package ui;

import database.ConnectionConfig;
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
        ConnectionConfig config = ConnectionConfig.read("connection_config.txt");
        PostgreSqlDataBase database = new PostgreSqlDataBase(config.host, config.dbName, config.username, config.password, config.port);
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
