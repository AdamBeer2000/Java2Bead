package sample.database;

import sample.users.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserService extends Service
{

    public UserService(Connection connection, ServerSettings ss) {
        super(connection, ss);
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

    public ArrayList<User> getAllUsers() throws SQLException
    {
        Connection conn=getConnection();
        ArrayList<User> users=new ArrayList<>();
        String query="SELECT * FROM users";
        PreparedStatement stmt=conn.prepareStatement(query);
        ResultSet rs= stmt.executeQuery();

        while(rs.next())
        {
             users.add(new User(rs.getString("username"),rs.getInt("id")));
        }

        return users;
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

    public ArrayList<User> searchUsers(String username) throws SQLException
    {
        ArrayList<User> users=new ArrayList<>();
        String sql="SELECT id,username FROM users where username like " + "'%"+username+"%'";
        PreparedStatement stmt=getConnection().prepareStatement(sql);
        ResultSet rst=stmt.executeQuery();

        while (rst.next())
        {
            users.add(new User(rst.getString("username"),rst.getInt("id")));
        }
        return users;
    }
}

