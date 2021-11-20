package sample.users;

import sample.database.SingletonDatabaseService;

public class SingletonLoggedUserManager
{
    private SingletonLoggedUserManager()
    {

    }

    private static SingletonLoggedUserManager instance;
    private User loggedUser=null;

    public static SingletonLoggedUserManager getInstance()
    {
        if(instance==null)
        {
            instance=new SingletonLoggedUserManager();
            return instance;
        }
        else
        {
            return instance;
        }
    }

    public User getLoggedUser()throws Exception
    {
        if(loggedUser!=null)
        return this.loggedUser;
        else throw new Exception("There is no logged user");
    }

    public boolean loginUser(String userName,String password)
    {
        SingletonDatabaseService sds=SingletonDatabaseService.getInstance();
        try
        {
            loggedUser=sds.getUser(userName,password);
            return true;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public int getUserid() {
        return loggedUser.getUserid();
    }

    public String getUserName() {
        return loggedUser.getUserName();
    }


}
