package sample.groups;

import sample.users.User;

public class Invite
{
    int inviteId;//meghívás azonosító
    User inviter;//meghívó user

    String InviterName;
    String GroupName;

    Group group;//és grup adatai (ahova meghívtak)


    public Invite(int inviteId,String userName, int userId, String groupName, int groupId)
    {
        this.inviteId=inviteId;
        this.inviter=new User(userName,userId);
        InviterName=userName;
        this.group=new Group(groupId,groupName);
        GroupName=groupName;
    }

    public String getInviterName()
    {
        return InviterName;
    }
    public String getGroupName()
    {
        return GroupName;
    }
    public void setInviterName(String s)
    {
        InviterName=s;
    }
    public void setGroupName(String s)
    {
        GroupName=s;
    }
}
