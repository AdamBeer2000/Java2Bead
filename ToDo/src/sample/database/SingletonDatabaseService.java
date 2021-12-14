package sample.database;

import sample.enums.Category;
import sample.enums.Importance;
import sample.groups.Group;
import sample.groups.Invite;
import sample.objects.ToDoObject;
import sample.users.SingletonLoggedUserManager;

import java.sql.*;
import java.util.ArrayList;

public class SingletonDatabaseService
{
    private SingletonDatabaseService()
    {
        try
        {
            this.UService=new UserService(this.getConnection(),new ServerSettings());
            this.TService=new TodoService(this.getConnection(),new ServerSettings());
            this.SService=new SocialService(this.getConnection(),new ServerSettings());
        }
        catch (Exception e)
        {
           e.printStackTrace();
        }

    }

    ServerSettings ss =new ServerSettings();
    private static SingletonDatabaseService instance=null;
    Connection connection=null;

    private UserService UService=null;
    private TodoService TService=null;
    private SocialService SService=null;

    public UserService UService()
    {
        return this.UService;
    }
    public TodoService TService()
    {
        return this.TService;
    }
    public SocialService SService()
    {
        return this.SService;
    }

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
        if (connection == null)
        {
            String url = "jdbc:mysql://" + ss.hostname + ":"+ss.port+"/" + ss.databaseName;
            this.connection= DriverManager.getConnection(url, ss.username, ss.password);
        }
        return this.connection;
    }

    public void Close() throws SQLException
    {
        if(this.connection!=null) this.connection.close();
        this.connection=null;
    }
}
