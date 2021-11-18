package sample.objects;

import sample.enums.Category;
import sample.enums.Importance;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ToDoObject
{

    //---- [Variables] ----
    public Importance importance;
    public Category category;
    public String title;
    public String description;
    public Date deadline;
    public Date start_date;
    public boolean is_finished;

    private SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); // ezt máshol felhasználhatjuk!

    //---- [CONSTRUCTORS] ----
    public ToDoObject()
    {
        this.title = "NO TITLE";
        this.description = "";
        this.importance = Importance.NOT_IMPORTANT;
        this.start_date = new Date();
        this.is_finished = false;
    }
    public ToDoObject(int todoId,String in_title, String in_description,
                      Category in_category, Importance in_importance,boolean is_finished)
    {
        this.title = in_title;
        this.description = in_description;
        this.category = in_category;
        this.importance = in_importance;
        this.start_date = null;
        this.deadline = null;
        this.is_finished = is_finished;
    }

    public ToDoObject(int todoid,String in_title, String in_description,Date start_date, Date in_deadline,
                      Category in_category, Importance in_importance,boolean is_finished)
    {
        this.title = in_title;
        this.description = in_description;
        this.category = in_category;
        this.importance = in_importance;
        this.start_date = start_date;
        this.deadline = in_deadline;
        this.is_finished = is_finished;
    }
}
