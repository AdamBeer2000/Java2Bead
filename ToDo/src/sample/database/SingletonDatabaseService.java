package sample.database;

import sample.enums.Category;
import sample.enums.Importance;
import sample.groups.Group;
import sample.objects.ToDoObject;
import sample.objects.TodoBuilder;
import sample.users.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class SingletonDatabaseService
{
    private SingletonDatabaseService()
    {

    }

    public class ServerSettings
    {
        private final String hostname="b4cyzvut6ovomqbnhtk0-mysql.services.clever-cloud.com";
        private final String username="u1xpyoext1l1e1mj";
        private final String password="HL9kDWHw5gfpvqqrozBc";
        private final String databaseName = "b4cyzvut6ovomqbnhtk0";
        private final int port=3306;
        public ServerSettings() {}
    }

    ServerSettings ss =new ServerSettings();
    private static SingletonDatabaseService instance=null;
    Connection connection=null;

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


    //egy todoobijektumot módosít az adatbázisban
    public void modifyTodo(int todoId,ToDoObject modifiedTodo)throws SQLException
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
                     "where ownerid=? and deadline=?";

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

        String query="SELECT title,importanceid,categoryid,description,deadline,start_date,finished,todoid from todotable where ownerid="+userid;

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
                        " FROM todotable" + " WHERE ownerid="+userid+" and importanceid="+imp.ToInt();

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
                        " FROM todotable" + " WHERE ownerid="+userid+" and categoryid="+cat.ToInt();

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

    //lekéri az összes todoját egy usernek két időpont között
    public ArrayList<ToDoObject> getAllTodoByUserIdBetween(int userid, Date dateOne, Date dateTwo)throws SQLException
    {
        return new ArrayList<ToDoObject>();
    }

    public void deleteTodoById(int id)throws SQLException
    {
        String query="DELETE from todotable where todoid="+id;
        Connection conn =getConnection();
        Statement stmt=conn.createStatement();
        stmt.execute(query);
    }

    public void addUser(String username,String password,String email)throws SQLException
    {
        Connection conn=getConnection();

        String query="INSERT INTO users(username,password,email) VALUES(?,?,?)";

        PreparedStatement stmt= conn.prepareStatement(query);
        stmt.setString(1,username);
        stmt.setString(2,password);
        stmt.setString(3,email);

        stmt.executeUpdate();
    }
    public User getUser(String username, String password) throws Exception
    {
        Connection conn=getConnection();

        String query="SELECT id FROM users where username=? and password=?";

        PreparedStatement stmt= conn.prepareStatement(query);
        stmt.setString(1,username);
        stmt.setString(2,password);

        ResultSet rs= stmt.executeQuery();

        if(rs.next())
        {
            return new User(username,rs.getInt("id"));
        }
        else
        {
            throw new Exception("No user with name/password:"+username+","+password);
        }
    }
    public User getUser(String username) throws Exception
    {
        Connection conn=getConnection();

        String query="SELECT id FROM users where username=?";

        PreparedStatement stmt= conn.prepareStatement(query);
        stmt.setString(1,username);

        ResultSet rs= stmt.executeQuery();

        if(rs.next())
        {
            return new User(username,rs.getInt("id"));
        }
        else
        {
            throw new Exception("No user with name:"+username);
        }
    }

    //hozáad egy felhasználót egy csoporthoz
    public void addUserToGroup(int userId,int groupId)throws SQLException
    {
        String sql="INSERT INTO grupmembers(userid,grupid) VALUES(?,?)";
        PreparedStatement stmt=getConnection().prepareStatement(sql);
        stmt.setInt(1,userId);
        stmt.setInt(2,groupId);
        stmt.execute();
    }

    public ArrayList<User> searchUsers(String username) throws SQLException
    {
        ArrayList<User> users=new ArrayList<User>();
        String sql="SELECT userid,username FROM users where username="+username;
        PreparedStatement stmt=getConnection().prepareStatement(sql);
        ResultSet rst=stmt.executeQuery();

        while (rst.next())
        {
            users.add(new User(rst.getString("username"),rst.getInt("userid")));
        }
        return users;
    }

    //létrahoz egy csoportot és hozáadja a készítőt
    public void createGroup(int creatorId,String name)throws SQLException
    {
        String sql="INSERT INTO grups(grupname) VALUES(?)";
        PreparedStatement stmt=getConnection().prepareStatement(sql);
        stmt.setString(1,name);
        stmt.execute();

        String query="SELECT grupid FROM grups where grupname=?";
        stmt=getConnection().prepareStatement(query);
        stmt.setString(1,name);
        ResultSet rs=stmt.executeQuery();
        rs.next();

        addUserToGroup(creatorId,rs.getInt("grupid"));
    }

    public ArrayList<Group> getAllGroup() throws SQLException
    {
        ArrayList<Group> groups=new ArrayList<>();
        Connection conn=getConnection();

        String query="SELECT grups.grupid,grups.grupname FROM `users`\n" +
                "JOIN grupmembers on users.id=grupmembers.userid\n" +
                "JOIN grups on grupmembers.grupid=grups.grupid\n";
        PreparedStatement stmt=conn.prepareStatement(query);
        ResultSet rst=stmt.executeQuery();

        while (rst.next())
        {
            int groupId=rst.getInt("grupid");
            Group currGroup=new Group(groupId,rst.getString("grupname"));
            groups.add(currGroup);

            String secondQuery="SELECT * from todotable WHERE grupid=?";
            stmt=conn.prepareStatement(secondQuery);
            stmt.setInt(1,groupId);

            ResultSet rstSecond=stmt.executeQuery();

            while (rstSecond.next())
            {
                int todoId=rstSecond.getInt("todoid");

                Importance importance;
                int impid=rstSecond.getInt("importanceid");
                importance=Importance.IntToImportance(impid);

                Category category;
                int catid=rstSecond.getInt("categoryid");
                category=Category.IntToCategory(catid);

                String title=rstSecond.getString("title");
                String description=rstSecond.getString("description");

                java.util.Date deadline=rstSecond.getDate("deadline");
                java.util.Date start_date=rstSecond.getDate("start_date");

                boolean is_finished=rstSecond.getBoolean("finished");

                //TodoBuilder tb=new TodoBuilder();
                //todo átírni builderre

                currGroup.associatedTodos.add(new ToDoObject(todoId,title,description,start_date,deadline,category,importance,is_finished));
            }
        }
        return groups;
    }

    // a userhez tartozó csoportok és azok todoik lekérése
    public ArrayList<Group> getAllGroupByUserId(int userId) throws SQLException
    {
        Connection conn=getConnection();
        ArrayList<Group> assiciates=new ArrayList<>();

        String query="SELECT grups.grupid,grups.grupname FROM `users`\n" +
                     "JOIN grupmembers on users.id=grupmembers.userid\n" +
                     "JOIN grups on grupmembers.grupid=grups.grupid\n" +
                     "WHERE users.id=?";
        PreparedStatement stmt=conn.prepareStatement(query);
        stmt.setInt(1,userId);
        ResultSet rst=stmt.executeQuery();

        while (rst.next())
        {
            int groupId=rst.getInt("grupid");
            Group currGroup=new Group(groupId,rst.getString("grupname"));
            assiciates.add(currGroup);

            String secondQuery="SELECT * from todotable WHERE grupid=?";
            stmt=conn.prepareStatement(secondQuery);
            stmt.setInt(1,groupId);

            ResultSet rstSecond=stmt.executeQuery();

            while (rstSecond.next())
            {
                int todoId=rstSecond.getInt("todoid");

                Importance importance;
                int impid=rstSecond.getInt("importanceid");
                importance=Importance.IntToImportance(impid);

                Category category;
                int catid=rstSecond.getInt("categoryid");
                category=Category.IntToCategory(catid);

                String title=rstSecond.getString("title");
                String description=rstSecond.getString("description");

                java.util.Date deadline=rstSecond.getDate("deadline");
                java.util.Date start_date=rstSecond.getDate("start_date");

                boolean is_finished=rstSecond.getBoolean("finished");

                currGroup.associatedTodos.add(new ToDoObject(todoId,title,description,start_date,deadline,category,importance,is_finished));
            }
        }
        return assiciates;
    }
}
