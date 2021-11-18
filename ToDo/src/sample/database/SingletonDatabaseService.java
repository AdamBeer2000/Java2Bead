package sample.database;

import sample.enums.Category;
import sample.enums.Importance;
import sample.objects.ToDoObject;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

public class SingletonDatabaseService
{
    private SingletonDatabaseService()
    {

    }

    public class ServerSettings
    {
        private final String hostname="localhost";
        private final String username="root";
        private final String password="";
        private final String databaseName="todolistdb";
        private final int port=3306;
        public ServerSettings() {}
    }

    ServerSettings ss =new ServerSettings();
    private static SingletonDatabaseService instance=null;

    public static SingletonDatabaseService getInstance()
    {
        if (instance == null)
        {
            instance = new SingletonDatabaseService();
        }
        return instance;
    }

    private Connection getConnection() throws SQLException
    {
        String url = "jdbc:mysql://" + ss.hostname + ":"+ss.port+"/" + ss.databaseName;
        return DriverManager.getConnection(url, ss.username, ss.password);
    }

    //egy todoobijektumot add hozzá az adatbázishoz
    public boolean addTodoToUser(int userid,ToDoObject todo)
    {
        return true;
    }
    //egy todoobijektumot módosít az adatbázisban
    public boolean modifyTodo(int todoId,ToDoObject modifiedTodo)
    {
        return true;
    }

    //lekéri az összes todoját egy usernek
    public ArrayList<ToDoObject> getAllTodoByUserId(int userid)
    {
        return new ArrayList<ToDoObject>();
    }

    //lekéri az összes todoját egy usernek (azon belül szűr Fontosság szerint szerint)
    public ArrayList<ToDoObject> getAllTodoByUserIdAndImportance(int userid, Importance imp)
    {
        return new ArrayList<ToDoObject>();
    }
    //lekéri az összes todoját egy usernek (azon belül szűr kategória szerint)
    public ArrayList<ToDoObject> getAllTodoByUserIdAndCategory(int userid, Category imp)
    {
        return new ArrayList<ToDoObject>();
    }

    //lekéri az összes todoját egy usernek két időpont között
    public ArrayList<ToDoObject> getAllTodoByUserIdBetween(int userid, Date dateOne, Date dateTwo)
    {
        return new ArrayList<ToDoObject>();
    }
}
