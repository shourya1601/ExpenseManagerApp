package com.shourya.expensemanager;

public class RVListItemCategories{
    private String ID;
    private String Category;
    private String Amount;

    public RVListItemCategories(String ID,String Category,String Amount)
    {
        this.ID=ID;
        this.Category=Category;
        this.Amount=Amount;
    }

    public String getID() {return ID;}
    public String getCategory()
    {
        return Category;
    }
    public String getAmount(){return Amount; }
}
