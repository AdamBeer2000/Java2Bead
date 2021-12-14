package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.database.SingletonDatabaseService;

public class Main extends Application
{
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
            //SingletonLoggedUserManager slum=SingletonLoggedUserManager.getInstance();
            //slum.loginUser("TesztElek","123");
            //sds.SService().leaveGroup(4);

            //TodoBuilder tb=new TodoBuilder("AddTODroupTest","AddTODroupTest");
            //sds.SService().addTodoToGroup(slum.getUserid(),1,tb.Build());

            launch(args);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            //System.out.println(e.getMessage());
        }
    }
}
