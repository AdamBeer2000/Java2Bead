package sample.objects;

import sample.enums.Category;
import sample.enums.Importance;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class TodoBuilder
{
    private int todoId=Integer.MAX_VALUE;
    private Importance importance=Importance.NOLABEL;
    private Category category=Category.NOLABEL;
    private String title="";
    private String description="";
    private Date deadline=null;
    private Date start_date=null;
    private boolean is_finished=false;

    public TodoBuilder(){}

    public TodoBuilder(String todoTitle,String description)
    {
        this.title=todoTitle;
        this.description=description;
    }

    public TodoBuilder withImportance(Importance importance)
    {
        this.importance=importance;
        return this;
    }
    public TodoBuilder withCategory(Category category)
    {
        this.category=category;
        return this;
    }
    public TodoBuilder withTitle(String title)
    {
        this.title=title;
        return this;
    }
    public TodoBuilder withDescription(String description)
    {
        this.description=description;
        return this;
    }

    public TodoBuilder withStartDate(LocalDate startDate)
    {
        Instant instant = Instant.from(startDate.atStartOfDay(ZoneId.systemDefault()));
        this.start_date= Date.from(instant);
        return this;
    }

    public TodoBuilder withDeadLine(LocalDate deadline)
    {
        Instant instant = Instant.from(deadline.atStartOfDay(ZoneId.systemDefault()));
        this.deadline= Date.from(instant);
        return this;
    }

    public TodoBuilder withStartDate(Date startDate)
    {
        this.start_date=startDate;
        return this;
    }
    public TodoBuilder withDeadLine(Date deadline)
    {
        this.deadline=deadline;
        return this;
    }

    public TodoBuilder withIsFinished(boolean isFinished)
    {
        this.is_finished=isFinished;
        return this;
    }
    public TodoBuilder withId(int id)
    {
        this.todoId=id;
        return this;
    }
    public ToDoObject Build()
    {
        return new ToDoObject(
                todoId,
                title,
                description,
                start_date,
                deadline,
                category,
                importance,
                is_finished
        );
    }

}
