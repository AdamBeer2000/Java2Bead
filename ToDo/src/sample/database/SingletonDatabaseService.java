package sample.database;

import sample.enums.Category;
import sample.enums.Importance;
import sample.objects.ToDoObject;

import java.sql.*;
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
    public void addTodoToUser(int userid, ToDoObject todo)throws SQLException
    {
        Connection conn =getConnection();
        ArrayList<ToDoObject> ret=new ArrayList<>();

        String query="";
        if(todo.start_date!=null&&todo.deadline!=null)
        {
            query="INSERT INTO todotable(ownerid,title,importanceid,categoryid,description,finished,start_date,deadline) "+
                "VALUES(?,?,?,?,?,?,?,?)";
        }
        else
        {
            query="INSERT INTO todotable(ownerid,title,importanceid,categoryid,description,finished) "+
                    "VALUES(?,?,?,?,?,?)";
        }

        PreparedStatement  stmt= conn.prepareStatement(query);

        stmt.setInt(1,userid);
        stmt.setString(2,todo.title);
        stmt.setInt(3,todo.importance.ToInt());
        stmt.setInt(4,todo.category.ToInt());
        stmt.setString(5,todo.description);
        stmt.setBoolean(6,todo.is_finished);

        if(todo.start_date!=null&&todo.deadline!=null)
        {
            stmt.setDate(7,new java.sql.Date(todo.start_date.getTime()));
            stmt.setDate(8,new java.sql.Date(todo.deadline.getTime()));
        }

        System.out.println(query);

        stmt.executeUpdate();
    }
    //egy todoobijektumot módosít az adatbázisban
    public void modifyTodo(int todoId,ToDoObject modifiedTodo)throws SQLException
    {

    }

    //lekéri az összes todoját egy usernek
    public ArrayList<ToDoObject> getAllTodoByUserId(int userid)throws SQLException
    {
        Connection conn =getConnection();
        ArrayList<ToDoObject> ret=new ArrayList<>();

        String query="SELECT title,importanceid,categoryid,description,deadline,start_date,finished,todoid from todotable where ownerid="+userid;

        Statement stmt= conn.createStatement();

        ResultSet rs=stmt.executeQuery(query);
        while (rs.next())
        {
             Importance importance;
             int impid=rs.getInt("importanceid");
             importance=Importance.IntToImportance(impid);

             Category category;
             int catid=rs.getInt("categoryid");
             category=Category.IntToCategory(catid);

             String title=rs.getString("title");
             String description=rs.getString("description");

             java.util.Date deadline=rs.getDate("deadline");
             java.util.Date start_date=rs.getDate("start_date");

             boolean is_finished=rs.getBoolean("finished");

            ret.add(new ToDoObject(title,description,start_date,deadline,category,importance));
        }

        getConnection().close();

        return ret;
    }

    //lekéri az összes todoját egy usernek (azon belül szűr Fontosság szerint szerint)
    public ArrayList<ToDoObject> getAllTodoByUserIdAndImportance(int userid, Importance imp)throws SQLException
    {
        Connection conn =getConnection();
        ArrayList<ToDoObject> ret=new ArrayList<>();

        String query=
                "SELECT title,importanceid,categoryid,description,deadline,start_date,finished,todoid" +
                        " FROM todotable" + " WHERE ownerid="+userid+" and importanceid="+imp.ToInt();

        Statement stmt= conn.createStatement();

        ResultSet rs=stmt.executeQuery(query);
        while (rs.next())
        {
            Importance importance;
            int impid=rs.getInt("importanceid");
            importance=Importance.IntToImportance(impid);

            Category category;
            int catid=rs.getInt("categoryid");
            category=Category.IntToCategory(catid);

            String title=rs.getString("title");
            String description=rs.getString("description");

            java.util.Date deadline=rs.getDate("deadline");
            java.util.Date start_date=rs.getDate("start_date");

            boolean is_finished=rs.getBoolean("finished");

            ret.add(new ToDoObject(title,description,start_date,deadline,category,importance));
        }

        getConnection().close();

        return ret;
    }
    //lekéri az összes todoját egy usernek (azon belül szűr kategória szerint)
    public ArrayList<ToDoObject> getAllTodoByUserIdAndCategory(int userid, Category cat)throws SQLException
    {
        Connection conn =getConnection();
        ArrayList<ToDoObject> ret=new ArrayList<>();

        String query=
                "SELECT title,importanceid,categoryid,description,deadline,start_date,finished,todoid" +
                        " FROM todotable" + " WHERE ownerid="+userid+" and importanceid="+cat.ToInt();
        System.out.println(query);
        Statement stmt= conn.createStatement();

        ResultSet rs=stmt.executeQuery(query);
        while (rs.next())
        {
            Importance importance;
            int impid=rs.getInt("importanceid");
            importance=Importance.IntToImportance(impid);

            Category category;
            int catid=rs.getInt("categoryid");
            category=Category.IntToCategory(catid);

            String title=rs.getString("title");
            String description=rs.getString("description");

            java.util.Date deadline=rs.getDate("deadline");
            java.util.Date start_date=rs.getDate("start_date");

            boolean is_finished=rs.getBoolean("finished");

            ret.add(new ToDoObject(title,description,start_date,deadline,category,importance));
        }

        getConnection().close();

        return ret;
    }

    //lekéri az összes todoját egy usernek két időpont között
    public ArrayList<ToDoObject> getAllTodoByUserIdBetween(int userid, Date dateOne, Date dateTwo)throws SQLException
    {

        return new ArrayList<ToDoObject>();
    }
}
