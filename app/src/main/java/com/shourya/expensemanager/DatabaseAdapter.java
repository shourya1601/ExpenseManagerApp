package com.shourya.expensemanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.constraintlayout.motion.widget.KeyTimeCycle;

public class DatabaseAdapter {
    static final String KEY_ID="ID";
    static final String KEY_AMOUNT="AMOUNT";
    static final String KEY_CATEGORIES="CATEGORIES";
    static final String KEY_DATE="DATE";
    static final String KEY_NOTE="NOTE";

    static final String DATABASE_NAME="incomedb";
    static final String DATABASE_TABLE_INCOME="incometb";
    static final String DATABASE_TABLE_CATEGORIES="categoriestb";
    static final int DATABASE_VERSION=1;

    static final String DATABASE_CREATE_INCOME="create table if not exists "+DATABASE_TABLE_INCOME+" ("+
            KEY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT not null,"+
            KEY_AMOUNT+" double not null,"+
            KEY_CATEGORIES+" text not null,"+
            KEY_DATE+" long not null,"+
            KEY_NOTE+" text);";

    static final String DATABASE_CREATE_CATEGORIES="create table if not exists "+DATABASE_TABLE_CATEGORIES+" ("+
            KEY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT not null,"+
            KEY_CATEGORIES+" text not null);";

    final Context context;

    DatabaseHelper DBHelper;
    SQLiteDatabase db;

    public DatabaseAdapter(Context c)
    {
        this.context=c;
        DBHelper=new DatabaseHelper(context);
    }

    public static class DatabaseHelper extends SQLiteOpenHelper{
        DatabaseHelper(Context context)
        {
            super(context,DATABASE_NAME,null,DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase db)
        {
            try
            {
                db.execSQL(DATABASE_CREATE_INCOME);
                db.execSQL(DATABASE_CREATE_CATEGORIES);
                ContentValues values=new ContentValues();
                values.put(KEY_CATEGORIES,"GENERAL");
                db.insert(DATABASE_TABLE_CATEGORIES,null,values);
                ContentValues values1=new ContentValues();
                values1.put(KEY_CATEGORIES,"MILK");
                db.insert(DATABASE_TABLE_CATEGORIES,null,values1);
                ContentValues values2=new ContentValues();
                values2.put(KEY_CATEGORIES,"FUEL");
                db.insert(DATABASE_TABLE_CATEGORIES,null,values2);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion)
        {
            db.execSQL("DROP TABLE IF EXISTS "+DATABASE_TABLE_INCOME);
            db.execSQL("DROP TABLE IF EXISTS "+DATABASE_TABLE_CATEGORIES);
            onCreate(db);
        }
    }

    public void test()
    {
        ContentValues values=new ContentValues();
        values.put(KEY_CATEGORIES,"GENERAL");
        db.insert(DATABASE_TABLE_CATEGORIES,null,values);
        ContentValues values1=new ContentValues();
        values1.put(KEY_CATEGORIES,"MILK");
        db.insert(DATABASE_TABLE_CATEGORIES,null,values1);
        ContentValues values2=new ContentValues();
        values2.put(KEY_CATEGORIES,"FUEL");
        db.insert(DATABASE_TABLE_CATEGORIES,null,values2);

    }

    public DatabaseAdapter open() throws Exception
    {
        db=DBHelper.getWritableDatabase();
        return this;
    }

    public void close()
    {
        DBHelper.close();
    }

    public long insertDataIncome(double amt,String cat,long date,String note)
    {
        ContentValues values=new ContentValues();
        values.put(KEY_AMOUNT,amt);
        values.put(KEY_CATEGORIES,cat);
        values.put(KEY_DATE,date);
        values.put(KEY_NOTE,note);
        return db.insert(DATABASE_TABLE_INCOME,null,values);
    }

    public long insertDataCategory(String cat)
    {
        ContentValues values=new ContentValues();
        values.put(KEY_CATEGORIES,cat);
        return db.insert(DATABASE_TABLE_CATEGORIES,null,values);
    }

    public Cursor isExistCategory(String category) throws Exception
    {
        Cursor cur = db.query(true,DATABASE_TABLE_CATEGORIES,new String[]{KEY_CATEGORIES},KEY_CATEGORIES+"='"+category+"'",null,null,null,null, null);
        if(cur!=null)
            cur.moveToFirst();
        return cur;
    }

    public void deleteTableIncome()
    {
        db.execSQL("DELETE FROM " + DATABASE_TABLE_INCOME);
    }

    public void deleteTableCategories()
    {
        db.execSQL("DELETE FROM " + DATABASE_TABLE_CATEGORIES);
    }

    public boolean updateDataIncome(int id,double amt,String cat,long date,String note)
    {
        ContentValues values=new ContentValues();
        values.put(KEY_AMOUNT,amt);
        values.put(KEY_CATEGORIES,cat);
        values.put(KEY_DATE,date);
        values.put(KEY_NOTE,note);
        return db.update(DATABASE_TABLE_INCOME,values,KEY_ID+"="+id,null)>0;
    }

    public boolean updateDataIncome(String old_category,String new_category)
    {
        ContentValues values=new ContentValues();
        values.put(KEY_CATEGORIES,new_category);
        return db.update(DATABASE_TABLE_INCOME,values,KEY_CATEGORIES+"='"+old_category+"'",null)>0;
    }

    public boolean updateDataCategory(String old_category,String new_category)
    {
        ContentValues values=new ContentValues();
        values.put(KEY_CATEGORIES,new_category);
        return db.update(DATABASE_TABLE_CATEGORIES,values,KEY_CATEGORIES+"='"+old_category+"'",null)>0;
    }

    public Cursor getAllDataIncome()
    {
        return db.query(DATABASE_TABLE_INCOME,new String[]{KEY_ID,KEY_AMOUNT,KEY_CATEGORIES,KEY_DATE,KEY_NOTE},null,null,null,null,KEY_DATE+" desc");
    }

    public Cursor getAllDataCategories()
    {
        return db.query(DATABASE_TABLE_CATEGORIES,new String[]{KEY_ID,KEY_CATEGORIES},null,null,null,null,null);
    }

    public boolean deleteDataCategory(String category)
    {
        return db.delete(DATABASE_TABLE_CATEGORIES,KEY_CATEGORIES+"='"+category+"'",null)>0;
    }

    public boolean deleteIncomeCategory(String category)
    {
        return db.delete(DATABASE_TABLE_INCOME,KEY_CATEGORIES+"='"+category+"'",null)>0;
    }

    public boolean deleteIncomeData(int id)
    {
        return db.delete(DATABASE_TABLE_INCOME,KEY_ID+"="+id,null)>0;
    }

    public double categorySum(String category) throws Exception
    {
        Cursor cursor = db.rawQuery("SELECT SUM("+KEY_AMOUNT+") as Total FROM " + DATABASE_TABLE_INCOME + " WHERE "+ KEY_CATEGORIES + "='" + category + "'", null);
        double result=0;
        if (cursor.moveToFirst()) {
            result = cursor.getDouble(0);
        }
        return result;
    }

    public Cursor categoryData(String category) throws Exception
    {
        return db.rawQuery("SELECT * FROM " + DATABASE_TABLE_INCOME + " WHERE "+ KEY_CATEGORIES + "='" + category + "'" + " order by " + KEY_DATE + " desc", null);
    }

    public double customDateSum(String startdate,String enddate) throws Exception
    {
        Cursor cursor = db.rawQuery("SELECT SUM("+KEY_AMOUNT+") as Total FROM " + DATABASE_TABLE_INCOME + " WHERE "+ KEY_DATE + " BETWEEN '" + startdate + "' AND '" + enddate + "'", null);
        double result=0;
        if (cursor.moveToFirst()) {
            result = cursor.getDouble(0);
        }
        return result;
    }

    public double customDateSum(String startdate,String enddate,String category) throws Exception
    {
        Cursor cursor = db.rawQuery("SELECT SUM("+KEY_AMOUNT+") as Total FROM " + DATABASE_TABLE_INCOME + " WHERE "+ KEY_DATE + " BETWEEN '" + startdate + "' AND '" + enddate + "' AND " + KEY_CATEGORIES + "='" + category + "'", null);
        double result=0;
        if (cursor.moveToFirst()) {
            result = cursor.getDouble(0);
        }
        return result;
    }

    public Cursor customDateData(String startdate,String enddate) throws Exception
    {
        return db.rawQuery("SELECT * FROM " + DATABASE_TABLE_INCOME + " WHERE "+ KEY_DATE + " BETWEEN '" + startdate + "' AND '" + enddate + "' order by "+KEY_DATE +" desc", null);
    }

    public Cursor customDateData(String startdate,String enddate,String category) throws Exception
    {
        return db.rawQuery("SELECT * FROM " + DATABASE_TABLE_INCOME + " WHERE "+ KEY_DATE + " BETWEEN '" + startdate + "' AND '" + enddate + "' AND " + KEY_CATEGORIES + "='" + category + "' order by "+KEY_DATE +" desc", null);
    }
}
