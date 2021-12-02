package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.database.SingletonDatabaseService;
import sample.enums.Category;
import sample.enums.Importance;
import sample.groups.Group;
import sample.objects.ToDoObject;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

public class Main extends Application {

    private static SingletonDatabaseService sds=SingletonDatabaseService.getInstance();

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("ToDo");
        Scene scene = new Scene(root, 1280, 720);
        scene.getStylesheets().add(getClass().getResource("design.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args)
    {
        try
        {
            ArrayList<ToDoObject>tdslist= sds.getAllTodoByUserIdAndImportance(0, Importance.NOT_IMPORTANT);

            ToDoObject ntd=new ToDoObject("nincsdatum","leiras",Category.UNFINISHED,Importance.IMPORTANT);
            ToDoObject ntd2=new ToDoObject("nincsdatum","leiras",new Date(),new Date(),Category.UNFINISHED,Importance.IMPORTANT);

            sds.addTodoToUser(0,ntd);
            sds.addTodoToUser(0,ntd2);

            for(ToDoObject td:tdslist)
            {
                System.out.println(td.title);
            }

            launch(args);
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
}
