package sample.groups;

import sample.users.User;

public class Invite {
    User inviter;//meghívó user
    Group group;//és grup adatai (ahova meghívtak)

    public Invite(String userName, int userId, String groupName, int groupId)
    {
        this.inviter=new User(userName,userId);
        this.group=new Group(groupId,groupName);
    }
}
