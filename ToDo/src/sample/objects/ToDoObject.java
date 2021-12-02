package sample.objects;

import javafx.scene.control.CheckBox;
import sample.enums.Category;
import sample.enums.Importance;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ToDoObject
{
    //---- [Variables] ----
    public int todoId;
    public Importance importance;
    public Category category;
    public String title;
    public String description;
    public Date deadline;
    public Date start_date;
    public boolean is_finished;
    public CheckBox is_finished_checked = new CheckBox();

    private SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); // ezt máshol felhasználhatjuk!

    //---- [CONSTRUCTORS] ----
    public ToDoObject()
    {
        this.todoId= Integer.MAX_VALUE;
        this.title = "NO TITLE";
        this.description = "";
        this.importance = Importance.NOT_IMPORTANT;
        this.start_date = new Date();
        this.is_finished = false;
        if (this.is_finished) {
            this.is_finished_checked.fire();
        }
    }
    public ToDoObject(String _title, Date _start_date, Date _deadline, String _description, boolean _is_finished)
    {
        this.title = _title;
        this.start_date = _start_date;
        this.deadline = _deadline;
        this.description = _description;

        this.importance = Importance.NOT_IMPORTANT;
        this.is_finished = _is_finished;
        if (this.is_finished) {
            this.is_finished_checked.fire();
        }

    }

    public ToDoObject(int todoId,String in_title, String in_description,
                      Category in_category, Importance in_importance,boolean is_finished)

    {
        this.todoId= todoId;
        this.title = in_title;
        this.description = in_description;
        this.category = in_category;
        this.importance = in_importance;
        this.start_date = null;
        this.deadline = null;
        this.is_finished = is_finished;
        if (this.is_finished) {
            this.is_finished_checked.fire();
        }
    }


    public ToDoObject(int todoid,String in_title, String in_description,Date start_date, Date in_deadline,
                      Category in_category, Importance in_importance,boolean is_finished)
    {
        this.todoId= todoid;
        this.title = in_title;
        this.description = in_description;
        this.category = in_category;
        this.importance = in_importance;
        this.start_date = start_date;
        this.deadline = in_deadline;
        this.is_finished = is_finished;
        if (this.is_finished) {
            this.is_finished_checked.fire();
        }
    }

    public int getTodoId() {
        return todoId;
    }

    public void setTodoId(int todoId) {
        this.todoId = todoId;
    }

    public Importance getImportance() {
        return importance;
    }

    public void setImportance(Importance importance) {
        this.importance = importance;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public Date getStart_date() {
        return start_date;
    }

    public void setStart_date(Date start_date) {
        this.start_date = start_date;
    }

    public boolean isIs_finished() {
        return is_finished;
    }

    public void setIs_finished(boolean is_finished) {
        this.is_finished = is_finished;
    }

    public CheckBox getIs_finished_checked() {
        return is_finished_checked;
    }

    public void setIs_finished_checked(CheckBox is_finished_checked) {
        this.is_finished_checked = is_finished_checked;
    }
}
