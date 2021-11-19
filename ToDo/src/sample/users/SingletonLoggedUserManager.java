package sample.users;

import sample.database.SingletonDatabaseService;

public class SingletonLoggedUserManager
{
    private SingletonLoggedUserManager()
    {

    }

    private User instance=null;

    public User getLoggedUser()throws Exception
    {
        if(instance!=null)
        return this.instance;
        else throw new Exception("There is no logged user");
    }

    public boolean loginUser(String userName,String password)
    {
        SingletonDatabaseService sds=SingletonDatabaseService.getInstance();
        try
        {
            instance=sds.getUser(userName,password);
            return true;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public int getUserid() {
        return instance.getUserid();
    }

    public String getUserName() {
        return instance.getUserName();
    }


}
