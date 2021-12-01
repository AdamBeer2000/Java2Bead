package sample.groups;

import sample.objects.ToDoObject;
import java.util.ArrayList;

public class Group extends ArrayList<ToDoObject>
{
    int id;
    String name;
   // ArrayList<ToDoObject> associatedTodos;
    public Group(int id,String name)
    {
        this.id=id;
        this.name=name;
        //this.associatedTodos=new ArrayList<>();
    }
    public Group(int id,String name,ArrayList<ToDoObject> associatedTodos)
    {
        this(id,name);
        this.addAll(associatedTodos);
        //this.associatedTodos=associatedTodos;
    }

    public int getGroupId()
    {
        return this.id;
    }

    public String getGroupName()
    {
        return this.name;
    }

}
