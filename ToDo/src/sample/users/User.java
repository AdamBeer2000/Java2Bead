package sample.users;

public class User
{
    private String userName;
    private int userId;

    public User() {}

    public User(String userName, int userId)
    {
        this.userName=userName;
        this.userId=userId;
    }

    public String getUserName()
    {
        return this.userName;
    }
    public int getUserid()
    {
        return this.userId;
    }
}
