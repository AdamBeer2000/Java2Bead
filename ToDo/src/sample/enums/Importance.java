package sample.enums;

public enum Importance
{
    NOT_IMPORTANT,
    AVARAGE_IMPORTANT,
    LESS_IMPORTANT,
    IMPORTANT,
    MOST_IMPORTANT,
    NOLABEL;

    public int ToInt()
    {
        switch (this)
        {
            case NOT_IMPORTANT:return 0;
            case AVARAGE_IMPORTANT:return 1;
            case LESS_IMPORTANT:return 2;
            case IMPORTANT:return 3;
            case MOST_IMPORTANT:return 4;
            case NOLABEL:return 999;
            default:return 404;
        }
    }

    public static Importance IntToImportance(int id)
    {
        switch(id)
        {
            case 0:return Importance.NOT_IMPORTANT;
            case 1:return Importance.AVARAGE_IMPORTANT;
            case 2:return Importance.LESS_IMPORTANT;
            case 3:return Importance.IMPORTANT;
            case 4:return Importance.MOST_IMPORTANT;
            default:return Importance.NOLABEL;
        }
    }
}