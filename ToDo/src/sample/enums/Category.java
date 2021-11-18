package sample.enums;

public enum Category {
    TODAY,
    PLANNED,
    UNFINISHED,
    FINISHED,
    NOLABEL;

    public int ToInt()
    {
        switch (this)
        {
            case TODAY:return 0;
            case PLANNED:return 1;
            case FINISHED:return 2;
            case UNFINISHED:return 3;
            case NOLABEL:return 999;
            default:return 404;
        }
    }

    public static Category IntToCategory(int id)
    {
        switch (id)
        {
            case 0:return Category.TODAY;
            case 1:return Category.PLANNED;
            case 2:return Category.FINISHED;
            case 3:return Category.UNFINISHED;
            default:return Category.NOLABEL;
        }
    }
}
