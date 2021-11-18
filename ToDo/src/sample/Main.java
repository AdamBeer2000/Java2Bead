package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.database.SingletonDatabaseService;

import java.sql.Connection;
import java.sql.SQLException;

public class Main extends Application {

    private static SingletonDatabaseService sds=SingletonDatabaseService.getInstance();

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("TODO");
        primaryStage.setScene(new Scene(root, 1280, 720));
        primaryStage.show();
    }


    public static void main(String[] args)
    {
        try
        {
            launch(args);
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
}
