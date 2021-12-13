package sample.groups;

import sample.objects.ToDoObject;
import java.util.ArrayList;

public class Group
{
    int id;
    String name;

    public ArrayList<ToDoObject> associatedTodos;

    public Group(int id,String name)
    {
        this.id=id;
        this.name=name;
        this.associatedTodos=new ArrayList<>();
    }
    public Group(int id,String name,ArrayList<ToDoObject> associatedTodos)
    {
        this(id,name);
        this.associatedTodos=associatedTodos;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
