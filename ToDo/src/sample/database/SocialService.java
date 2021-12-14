package sample.database;

import sample.enums.Category;
import sample.enums.Importance;
import sample.groups.Group;
import sample.groups.Invite;
import sample.objects.ToDoObject;
import sample.users.SingletonLoggedUserManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SocialService extends Service
{

    public SocialService(Connection connection, ServerSettings ss) {
        super(connection, ss);
    }

    //Groups

    //hozáad egy felhasználót egy csoporthoz
    public void addUserToGroup(int userId,int groupId)throws SQLException
    {
        String sql="INSERT INTO grupmembers(userid,grupid) VALUES(?,?)";
        PreparedStatement stmt=getConnection().prepareStatement(sql);
        stmt.setInt(1,userId);
        stmt.setInt(2,groupId);

        System.out.println(userId);
        System.out.println(groupId);

        stmt.execute();
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
    public void addTodoToGroup(int creatorId,int groupId,ToDoObject todo) throws SQLException
    {
        Connection conn =getConnection();

        ArrayList<ToDoObject> ret=new ArrayList<>();

        String query="";

        query="INSERT INTO todotable(ownerid,title,importanceid,categoryid,description,finished,start_date,deadline,grupid) "+
                "VALUES(?,?,?,?,?,?,?,?,?)";

        PreparedStatement  stmt= conn.prepareStatement(query);

        stmt.setInt(1,creatorId);
        stmt.setString(2,todo.title);
        stmt.setInt(3,todo.importance.ToInt());
        stmt.setInt(4,todo.category.ToInt());
        stmt.setString(5,todo.description);
        stmt.setBoolean(6,todo.is_finished);
        stmt.setInt(9,groupId);
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

    //Invites

    //invitek lekérdezése user id vel
    public ArrayList<Invite> getInvitesByUserId(int userId) throws SQLException
    {
        ArrayList<Invite> invites=new ArrayList<>();
        String sql="SELECT  inviteid,inviteruser.id as 'inviteruserId',inviteruser.username as 'inviteruserUsername',\n" +
                "       grups.grupId,grups.grupname\n" +
                "FROM invites\n" +
                "JOIN users as inviteruser on invites.inviterId = inviteruser.id\n" +
                "JOIN users as inviteduser on invites.invitedId = inviteduser.id\n" +
                "JOIN grups on invites.grupId=grups.grupid\n" +
                "where inviteduser.id=?";
        PreparedStatement stmt=getConnection().prepareStatement(sql);
        stmt.setInt(1,userId);
        ResultSet rs=stmt.executeQuery();
        while (rs.next())
        {
            invites.add(new Invite(rs.getInt("inviteid"),rs.getString("inviteruserUsername"),rs.getInt("inviteruserId"),
                    rs.getString("grupname"),rs.getInt("grupId")));
        }
        return invites;
    }
    //invitek lekérdezése bejelentkezett userel
    public ArrayList<Invite> getInvitesOffLoggedUser()throws SQLException
    {
        SingletonLoggedUserManager slum=SingletonLoggedUserManager.getInstance();
        return getInvitesByUserId(slum.getUserid());
    }
    //egy felhasználó meghívása id vel
    public void createInvite(int inviterId,int groupId,int invitedId)throws SQLException
    {
        String sql="INSERT INTO invites(inviterId,grupId,invitedId) VALUES(?,?,?)";
        PreparedStatement stmt=getConnection().prepareStatement(sql);
        stmt.setInt(1,inviterId);
        stmt.setInt(2,groupId);
        stmt.setInt(3,invitedId);
        stmt.execute();
    }
    //ha nem adjuk meg ki fogadja el az invitet alapból a bejelentkezet user lessz
    public void acceptInvite(int inviteId)throws SQLException
    {
        SingletonLoggedUserManager slum= SingletonLoggedUserManager.getInstance();
        acceptInvite(slum.getUserid(),inviteId);
    }
    public void acceptInvite(int invitedId ,int inviteId)throws SQLException
    {
        String sql="SELECT * FROM invites WHERE inviteId=?";
        PreparedStatement stmt=getConnection().prepareStatement(sql);
        stmt.setInt(1,inviteId);
        ResultSet rs=stmt.executeQuery();
        rs.next();
        int groupId =rs.getInt("grupid");
        addUserToGroup(invitedId,groupId);
        deleteInvite(inviteId);
    }
    public void declineInvite(int inviteId)throws SQLException
    {
        deleteInvite(inviteId);
    }
    private void deleteInvite(int inviteId) throws SQLException
    {
        String sql="DELETE FROM invites WHERE inviteId=?";
        PreparedStatement stmt=getConnection().prepareStatement(sql);
        stmt.setInt(1,inviteId);
        stmt.execute();
    }
}
