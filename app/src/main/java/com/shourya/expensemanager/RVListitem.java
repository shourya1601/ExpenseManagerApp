package com.shourya.expensemanager;

public class RVListitem {
    private String ID;
    private String Date;
    private String Amount;
    private String Category;
    private String Note;

    public RVListitem(String ID, String Date, String Amount, String Category, String Note)
    {
        this.ID=ID;
        this.Date=Date;
        this.Amount=Amount;
        this.Category=Category;
        this.Note=Note;
    }
    public String getID() {return ID;}
    public String getDate()
    {
        return Date;
    }
    public String getAmount()
    {
        return Amount;
    }
    public String getCategory()
    {
        return Category;
    }
    public String getNote()
    {
        return Note;
    }
}
