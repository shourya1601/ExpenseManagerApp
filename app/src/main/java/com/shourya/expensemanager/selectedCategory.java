package com.shourya.expensemanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class selectedCategory extends AppCompatActivity {

    //to store selected category
    String category;

    //for recycler view
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    List<RVListitem> listItems;

    //to access database adapter
    DatabaseAdapter da=new DatabaseAdapter(selectedCategory.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_category);

        setTitle("SELECTED CATEGORY");

        //opening database adapter
        try {
            da.open();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Intent intent=getIntent();
        category=intent.getExtras().getString("category");

        //for adding data in recycler view
        recyclerView=findViewById(R.id.rv);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        listItems=new ArrayList<>();

        //retrieving data from database for selected category
        Cursor c= null;
        try {
            c = da.categoryData(category);
        } catch (Exception e) {
            e.printStackTrace();
        }
        while(c.moveToNext())
        {
            String date = new SimpleDateFormat("dd/MM/yyyy").format(new Date(c.getLong(3)));
            RVListitem li=new RVListitem(""+c.getInt(0),""+date,"Rs. "+c.getDouble(1),""+c.getString(2),"Note-"+c.getString(4));
            listItems.add(li);
        }
        c.close();

        adapter=new RVAdapter(listItems,this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        this.recreate();
    }

}