package sample.database;

import sample.enums.Category;
import sample.enums.Importance;
import sample.objects.ToDoObject;

import java.sql.*;
import java.util.ArrayList;

public class TodoService extends Service
{

    public TodoService(Connection connection, ServerSettings ss) {
        super(connection, ss);
    }

    //egy todoobijektumot módosít az adatbázisban
    public void modifyTodo(int todoId, ToDoObject modifiedTodo)throws SQLException
    {
        Connection conn =getConnection();

        String query="UPDATE todotable " +
                "SET title=?,importanceid=?,categoryid=?,description=?,finished=? "+
                "WHERE todoId="+todoId;

        System.out.println(query);

        PreparedStatement stmt= conn.prepareStatement(query);

        stmt.setString(1,modifiedTodo.title);
        stmt.setInt(2,modifiedTodo.importance.ToInt());
        stmt.setInt(3,modifiedTodo.category.ToInt());
        stmt.setString(4,modifiedTodo.description);
        stmt.setBoolean(5,modifiedTodo.is_finished);

        stmt.executeUpdate();

    }
    public ArrayList<ToDoObject> getAllTodoByUserIdandDate(int userid, java.util.Date date)throws SQLException
    {
        Connection conn =getConnection();
        ArrayList<ToDoObject> ret=new ArrayList<>();

        String query="SELECT title,importanceid,categoryid,description,deadline,start_date,finished,todoid " +
                "from todotable " +
                "where ownerid=? and deadline=?"+" and finished=false";

        PreparedStatement stmt= conn.prepareStatement(query);
        stmt.setInt(1,userid);
        stmt.setDate(2,new java.sql.Date(date.getTime()));

        ResultSet rs=stmt.executeQuery();
        while (rs.next())
        {
            int todoId=rs.getInt("todoid");

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

            ret.add(new ToDoObject(todoId,title,description,start_date,deadline,category,importance,is_finished));
        }
        return ret;
    }
    //lekéri az összes todoját egy usernek
    public ArrayList<ToDoObject> getAllTodoByUserId(int userid)throws SQLException
    {
        Connection conn =getConnection();
        ArrayList<ToDoObject> ret=new ArrayList<>();

        String query="SELECT title,importanceid,categoryid,description,deadline,start_date,finished,todoid " +
                     "from todotable where ownerid="+userid+" and finished=false";

        Statement stmt= conn.createStatement();

        ResultSet rs=stmt.executeQuery(query);
        while (rs.next())
        {
            int todoId=rs.getInt("todoid");

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

            ret.add(new ToDoObject(todoId,title,description,start_date,deadline,category,importance,is_finished));
        }
        return ret;
    }
    //lekéri az összes todoját egy usernek (azon belül szűr Fontosság szerint szerint)
    public ArrayList<ToDoObject> getAllTodoByUserIdAndImportance(int userid, Importance imp)throws SQLException
    {
        Connection conn =getConnection();
        ArrayList<ToDoObject> ret=new ArrayList<>();

        String query=
                "SELECT title,importanceid,categoryid,description,deadline,start_date,finished,todoid" +
                        " FROM todotable" + " WHERE ownerid="+userid+" and importanceid="+imp.ToInt()+" and finished=false";;

        Statement stmt= conn.createStatement();

        ResultSet rs=stmt.executeQuery(query);
        while (rs.next())
        {
            int todoId=rs.getInt("todoid");

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

            ret.add(new ToDoObject(todoId,title,description,start_date,deadline,category,importance,is_finished));
        }
        return ret;
    }
    //lekéri az összes todoját egy usernek (azon belül szűr kategória szerint)
    public ArrayList<ToDoObject> getAllTodoByUserIdAndCategory(int userid, Category cat)throws SQLException
    {
        Connection conn =getConnection();
        ArrayList<ToDoObject> ret=new ArrayList<>();

        String query=
                "SELECT title,importanceid,categoryid,description,deadline,start_date,finished,todoid" +
                        " FROM todotable" + " WHERE ownerid="+userid+" and categoryid="+cat.ToInt()+" and finished=false";

        System.out.println(query);
        Statement stmt= conn.createStatement();

        ResultSet rs=stmt.executeQuery(query);
        while (rs.next())
        {
            int todoId=rs.getInt("todoid");

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

            ret.add(new ToDoObject(todoId,title,description,start_date,deadline,category,importance,is_finished));

        }
        return ret;
    }
    public ArrayList<ToDoObject> getAllTodoByUserIdAndFinished(int userid)throws SQLException
    {
        Connection conn =getConnection();
        ArrayList<ToDoObject> ret=new ArrayList<>();

        String query=
                "SELECT title,importanceid,categoryid,description,deadline,start_date,finished,todoid" +
                        " FROM todotable" + " WHERE ownerid=? and finished=?";

        System.out.println(query);
        PreparedStatement stmt= conn.prepareStatement(query);
        stmt.setInt(1,userid);
        stmt.setBoolean(2,true);

        ResultSet rs=stmt.executeQuery();
        while (rs.next())
        {
            int todoId=rs.getInt("todoid");

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

            ret.add(new ToDoObject(todoId,title,description,start_date,deadline,category,importance,is_finished));

        }
        return ret;
    }
    //lekéri az összes todoját egy usernek két időpont között
    public ArrayList<ToDoObject> getAllTodoByUserIdBetween(int userid, Date dateOne, Date dateTwo)throws SQLException
    {
        return new ArrayList<ToDoObject>();
    }
    //egy todoobijektumot add hozzá az adatbázishoz
    public void addTodoToUser(int userid, ToDoObject todo)throws SQLException
    {
        Connection conn =getConnection();
        ArrayList<ToDoObject> ret=new ArrayList<>();

        String query="";

        query="INSERT INTO todotable(ownerid,title,importanceid,categoryid,description,finished,start_date,deadline) "+
                "VALUES(?,?,?,?,?,?,?,?)";

        PreparedStatement  stmt= conn.prepareStatement(query);

        stmt.setInt(1,userid);
        stmt.setString(2,todo.title);
        stmt.setInt(3,todo.importance.ToInt());
        stmt.setInt(4,todo.category.ToInt());
        stmt.setString(5,todo.description);
        stmt.setBoolean(6,todo.is_finished);

        if(todo.start_date!=null)
        {
            stmt.setDate(7,new java.sql.Date(todo.start_date.getTime()));
        }
        else
        {
            stmt.setDate(7,null);
        }

        if(todo.deadline!=null)
        {
            stmt.setDate(8,new java.sql.Date(todo.deadline.getTime()));
        }
        else
        {
            stmt.setDate(8,null);
        }

        stmt.executeUpdate();
    }
    public void deleteTodoById(int id)throws SQLException
    {
        String query="DELETE from todotable where todoid="+id;
        Connection conn =getConnection();
        Statement stmt=conn.createStatement();
        stmt.execute(query);
    }
}
