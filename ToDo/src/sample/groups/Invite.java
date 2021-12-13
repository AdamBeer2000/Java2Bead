package sample.groups;

import sample.users.User;

public class Invite
{
    int inviteId;//meghívás azonosító
    User inviter;//meghívó user
    Group group;//és grup adatai (ahova meghívtak)

    public Invite(int inviteId,String userName, int userId, String groupName, int groupId)
    {
        this.inviteId=inviteId;
        this.inviter=new User(userName,userId);
        this.group=new Group(groupId,groupName);
    }

    public String getInviterName()
    {
        return inviter.getUserName();
    }
    public String getGroupName()
    {
        return group.getName();
    }
}
